/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.task;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.AllegatiService;
import it.csi.moon.moonbobl.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.ComuneDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AreaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.AreaModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.EnteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaSaveResponse;
import it.csi.moon.moonbobl.business.service.impl.dto.LogEmailEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.dto.moonfobl.AllegatiSummary;
import it.csi.moon.moonbobl.dto.moonfobl.ContextRequest;
import it.csi.moon.moonbobl.dto.moonfobl.EmailAttachment;
import it.csi.moon.moonbobl.dto.moonfobl.EmailRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoLogEmail;


/**
 * PostSaveInstanzaTask SendEmailDichiaranteIstanzaTask
 *
 * Necessita configuration json in attributiModulo PSIT_EMAIL_CONF
 * - to                                              *** ALTERNATIVO - SE presente verra ignorato to_istanza_data_key ***
 * - to_istanza_data_key chiave del campo email da recuperare nei dati inseriti dell'istanza     *** ALTERNATIVO a to ***
 * - cc                                              *** ALTERNATIVO - SE presente verra ignorato cc_istanza_data_key ***
 * - cc_istanza_data_key chiave del campo email da recuperare nei dati inseriti dell'istanza     *** ALTERNATIVO a cc ***
 * - bcc                                             *** ALTERNATIVO - SE presente verra ignorato cc_istanza_data_key ***
 * - bcc_istanza_data_key chiave del campo email da recuperare nei dati inseriti dell'istanza   *** ALTERNATIVO a bcc ***
 * - subject dell'email
 * - text body del email testuale
 * - html content html                               *** NOT YET IMPLEMENTED, DA TESTARE ; ALTERNATIVO a text ***
 * - attach_istanza   boolean true/false
 * - attach_allegati  boolean true/false
 * - protocollo_to    email di protocollazione, inviata separatamente dall'invio user
 * - subject_rinvio   subject dell'email nel caso di rinvio, se assente nel caso rinvio di default prende "subject" 
 * - text_rinvio      body del email testuale nel caso di rinvio, se assente nel caso rinvio di default prende "text"
 * - html_rinvio      body del email html nel caso di rinvio, se assente nel caso rinvio di default prende "html"   *** NOT YET IMPLEMENTED, DA TESTARE ; ALTERNATIVO a text ***
 *
 * {
 * 	 "to": "abc.cd@csi.it",
 * 	 "to_istanza_data_key": "email",
 *   "subject": "Conferma invio @@OGGETTO_MODULO@@",
 *   "text": "\n@@Nome@@ @@COGNOME@@,\n\nha inviato istanza di @@OGGETTO_MODULO@@, a cui è stato assegnato il codice @@CODICE_ISTANZA@@.\nLa stessa è presente in allegato.\n\nQuesta e-mail è stata generata automaticamente, si prega di non rispondere a questo messaggio.",
 *   "attach_istanza": true,
 *   "attach_allegati": true,
 *   "protocollo_to": "protocolla.moon@comune.it",
 *   "subject_rinvio": "Rinvio della Conferma invio @@OGGETTO_MODULO@@",
 *   "text_rinvio": "Questo è un rinvio aggiornando il documento in allegato.\n@@Nome@@ @@COGNOME@@,\n..."
 * }
 *
 * Replace dinamici possibible in : subject, text, html
 *
 *
 * @author laurent
 *
 */
public class SendEmailDichiaranteIstanzaTask implements Callable<String> {

	private static final long MB = 1000000L;
	private static final long LIMIT_MAX_DIMENSIONE_TOTALE_ALLEGATI = 5 * MB; // MAX 5MB
	private static final int LIMIT_MAX_NUMERO_ALLEGATI = 10; // MA 10 allegati
	private static final String CLASS_NAME = "SendEmailDichiaranteIstanzaTask";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
    public static final String EMAIL_VALIDATE_REGEX =
            "(([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4}))(((;|,|; | ;| ; | , | ,){1}"
            +"([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4}))*)";

	private JsonNode conf = null;

	private UserInfo user;
	private IstanzaSaveResponse istanzaSaveResponse;
	private String confSendEmail;
	private String dest;
	private EmailRequest emailRequest;
	private Boolean hasSuperamentoLimitAllegati = null;
	private DatiIstanzaHelper datiIstanzaHelper;
	private boolean rinvio = false;

	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	LogEmailDAO logEmailDAO;
	@Autowired
	EnteDAO enteDAO;
	@Autowired
	AreaModuloDAO areaModuloDAO;
	@Autowired
	AreaDAO areaDAO;
	@Autowired
	@Qualifier("moon")
	ComuneDAO comuneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	AllegatiService allegatiService;

    public SendEmailDichiaranteIstanzaTask(UserInfo user, IstanzaSaveResponse istanzaSaveResponse, String confSendEmail, String dest, boolean rinvio) {
		super();
		this.user = user;
		this.istanzaSaveResponse = istanzaSaveResponse;
		this.confSendEmail = confSendEmail;
		this.dest = StringUtils.isBlank(dest)?"user":dest; // Default value user
		this.rinvio = rinvio;
		log.debug("[" + CLASS_NAME + "::SendEmailDichiaranteIstanzaTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): " + getIstanzaSaved().getIdIstanza());

        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
    
    // Per postCallbackProtocollo
    public SendEmailDichiaranteIstanzaTask(UserInfo user, IstanzaSaveResponse istanzaSaveResponse, String confSendEmail, String dest) {
		super();
		this.user = user;
		this.istanzaSaveResponse = istanzaSaveResponse;
		this.confSendEmail = confSendEmail;
		this.dest = StringUtils.isBlank(dest)?"user":dest; // Default value user
		this.rinvio = false;
		log.debug("[" + CLASS_NAME + "::SendEmailDichiaranteIstanzaTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): " + getIstanzaSaved().getIdIstanza());

        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
    
	private Istanza getIstanzaSaved() {
		if (istanzaSaveResponse==null) return null;
		return istanzaSaveResponse.getIstanza();
	}

    /**
     * Invia un email al dichiarante con il pdf dell'istanza
     *
     */
    public String call() throws BusinessException {
    	try {
    		Integer idTipologiaLogEmail = DecodificaTipoLogEmail.FO_INVIO_ISTANZA.getId();
	    	String result = null;
	        log.debug("[" + CLASS_NAME + "::call] Started Task...");
	        
	        // 1. Init JsonNode di configurazione e dataIstanza
	        getConf();
	        log.debug("[" + CLASS_NAME + "::call] getConf() OK.");

	        // 2. Gestione email User Dichiarante
			String logEsito = "";
			String emailUserDichiarante = "";
			if (dest.contains("user")) {
		        // Gestione email User Dichiarante
		        emailUserDichiarante = retrieveTo();
				log.debug("[" + CLASS_NAME + "::call] moonsrvDAO.sendEmail() User ...");
				try {
			        makeValidaSendEmail(emailUserDichiarante);
					result = "Un email riepilogativo è stato mandato all'indirizzo " + retrieveTo();
					logEsito += "OK";
				} catch (DAOException e) {
		    		result = "Non è stato possibile inviare l'email riepilogativo all'indirizzo " + retrieveTo();
		    		logEsito += "KO";
				}
			}

			// 3. Gestione email Protocollo
	        String emailProtocolloTo = retrieveProtocolloTo();
	        if (dest.contains("protocollo") && StringUtils.isNotBlank(emailProtocolloTo)) {
				log.debug("[" + CLASS_NAME + "::call] moonsrvDAO.sendEmail() Protocollo ...");
				try {
					makeValidaSendEmail(emailProtocolloTo);
					result += " ; Un email di protocollazione è stato mandato all'indirizzo " + emailProtocolloTo;
			        logEsito += ";OK";
				} catch (DAOException e) {
		    		result += " ; Non è stato possibile inviare l'email di protocollazione all'indirizzo " + emailProtocolloTo;
		    		logEsito += ";KO";
				}
	        }
			
			if (dest.contains("postCallbackPrt")) {
		        // Gestione email User Dichiarante
		        emailUserDichiarante = retrieveTo();
				log.debug("[" + CLASS_NAME + "::call] moonsrvDAO.sendEmail() User ...");
				try {
			        makeValidaSendEmail(emailUserDichiarante);
					result = "Un email riepilogativo è stato mandato all'indirizzo " + retrieveTo();
					logEsito += "OK";
				} catch (DAOException e) {
		    		result = "Non è stato possibile inviare l'email riepilogativo all'indirizzo " + retrieveTo();
		    		logEsito += "KO";
				}
				idTipologiaLogEmail = DecodificaTipoLogEmail.FO_INVIO_ISTANZA.getId();
			}
			
	        // 4. LogEmail
			LogEmailEntity logEmail = new LogEmailEntity();
			logEmail.setIdTipologia(idTipologiaLogEmail);
			logEmail.setIdEnte(getIstanzaSaved().getIdEnte());
			logEmail.setIdModulo(getIstanzaSaved().getModulo().getIdModulo());
			logEmail.setIdIstanza(getIstanzaSaved().getIdIstanza());
			logEmail.setEmailDestinatario(emailUserDichiarante);
			logEmail.setTipoEmail(conf.get("html")!=null?"html-attach":"text-attach");
			logEmail.setEsito(logEsito);
			logEmailDAO.insert(logEmail);

	        log.debug("[" + CLASS_NAME + "::call] Started Completed. " + result);
	        return result;
		} catch (BusinessException be) {
			log.warn("[" + CLASS_NAME + "::call] ERROR ");
			throw be;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::call] ERROR ", e);
			throw new BusinessException();
		}
    }

    private void makeValidaSendEmail(String emailUserDichiarante) throws BusinessException, DAOException {
		EmailRequest request = makeEmailRequest(emailUserDichiarante);
		validaEmailRequest(request);
		String response = moonsrvDAO.sendEmail(request);
		log.debug("[" + CLASS_NAME + "::makeValidaSendEmail] moonsrvDAO.sendEmail() response=" + response);
	}

	private EmailRequest makeEmailRequest(String emailDestinatario) throws ItemNotFoundDAOException, DAOException {
		if (emailRequest!=null) {
			emailRequest.setTo(emailDestinatario);
		} else {
			emailRequest = new EmailRequest();
			emailRequest.setTo(emailDestinatario);
			emailRequest.setCc(retrieveCc());
			emailRequest.setBcc(retrieveBcc());
			emailRequest.setSubject(retrieveSubject());
			emailRequest.setText(retrieveText());
			emailRequest.setHtml(retrieveContentHtml());
			emailRequest.setAttachment(retrieveEmailAttach());
			emailRequest.setContext(retrieveContext());
		}
		return emailRequest;
	}

	private ContextRequest retrieveContext() {
		ContextRequest context = new ContextRequest();
		context.setIdModulo(getIstanzaSaved().getModulo().getIdModulo());
		context.setIdIstanza(getIstanzaSaved().getIdIstanza());
		return context;
	}

	private String retrieveTo() {
    	log.debug("[" + CLASS_NAME + "::retrieveTo] BEGIN");
		String result = conf.get("to")!=null?conf.get("to").getTextValue().trim():""; // 'to' e opzional in choice con 'to_istanza_data_key', init string empty ""
		String resultToIstanzaDataKey = "";
		String key = conf.get("to_istanza_data_key")!=null?conf.get("to_istanza_data_key").getTextValue().trim():null;
		log.debug("[" + CLASS_NAME + "::retrieveTo] Using to_istanza_data_key (if not null): " + key);
		if (StringUtils.isNotEmpty(key)) {
			resultToIstanzaDataKey = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
			if (StringUtils.isNotBlank(resultToIstanzaDataKey)) {
				// caso di compilazione di emtrambi toFisso & email dall istanza
				result += ((StringUtils.isNotBlank(result)?";":"") + resultToIstanzaDataKey.trim());
			}
		}
    	log.debug("[" + CLASS_NAME + "::retrieveTo] END " + result);
		return result;
	}

	private String retrieveCc() {
    	log.debug("[" + CLASS_NAME + "::retrieveCc] BEGIN");
		String result = conf.get("cc")!=null?conf.get("cc").getTextValue():null;
		if (StringUtils.isEmpty(result)) {
			String key = conf.get("cc_istanza_data_key")!=null?conf.get("cc_istanza_data_key").getTextValue():null;
			if (StringUtils.isNotEmpty(key)) {
				result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
			}
		}
    	if ("rif_asl".equals(result)) {
    		String codiceComune = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey("codiceComune");
			int codice = Integer.parseInt(codiceComune);
			result = comuneDAO.findEmailAslByCodiceComune(codice);
    	}
    	log.debug("[" + CLASS_NAME + "::retrieveCc] END " + result);
		return result;
	}

	private String retrieveBcc() {
    	log.debug("[" + CLASS_NAME + "::retrieveBcc] BEGIN");
    	String result = conf.get("bcc")!=null?conf.get("bcc").getTextValue():null;
		if (StringUtils.isEmpty(result)) {
			String key = conf.get("bcc_istanza_data_key")!=null?conf.get("bcc_istanza_data_key").getTextValue():null;
			if (StringUtils.isNotEmpty(key)) {
				result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
			}
		}
    	log.debug("[" + CLASS_NAME + "::retrieveBcc] END " + result);
		return result;
	}
	private String retrieveProtocolloTo() {
    	log.debug("[" + CLASS_NAME + "::retrieveProtocolloTo] BEGIN");
    	String result = conf.get("protocollo_to")!=null?conf.get("protocollo_to").getTextValue():null;
		log.debug("[" + CLASS_NAME + "::retrieveProtocolloTo] END " + result);
		return result;
	}
	private String retrieveSubject() {
    	log.debug("[" + CLASS_NAME + "::retrieveSubject] BEGIN");
		String result = replaceDinamici((rinvio && conf.get("subject_rinvio")!=null)?
			conf.get("subject_rinvio").getTextValue():
			(conf.get("subject")!=null?conf.get("subject").getTextValue():null));
    	log.debug("[" + CLASS_NAME + "::retrieveSubject] END " + result);
		return result;
	}

	private String retrieveText() {
    	log.debug("[" + CLASS_NAME + "::retrieveText] BEGIN");
		String result = replaceDinamici((rinvio && conf.get("text_rinvio")!=null)?
			conf.get("text_rinvio").getTextValue():
			(conf.get("text")!=null?conf.get("text").getTextValue():null));
		log.debug("[" + CLASS_NAME + "::retrieveText] END " + result);
		return result;
	}

	private String retrieveContentHtml() {
    	log.debug("[" + CLASS_NAME + "::retrieveContentHtml] BEGIN");
		String result = replaceDinamici((rinvio && conf.get("html_rinvio")!=null)?
			conf.get("html_rinvio").getTextValue():
			(conf.get("html")!=null?conf.get("html").getTextValue():null));
    	log.debug("[" + CLASS_NAME + "::retrieveContentHtml] END " + result);
		return result;
	}

	private EmailAttachment retrieveEmailAttach() {
		log.debug("[" + CLASS_NAME + "::retrieveEmailAttach] BEGIN");
		if (conf.get("attach_istanza")==null) {
			log.debug("[" + CLASS_NAME + "::retrieveEmailAttach] attach_istanza not presente, NULL.");
			return null;
		}
		EmailAttachment result = conf.get("attach_istanza").getBooleanValue()?new EmailAttachment(getIstanzaSaved().getIdIstanza(),retrieveAttachAllegati()):null;
		log.debug("[" + CLASS_NAME + "::retrieveEmailAttach] END " + result);
		return result;
	}

	protected Boolean retrieveAttachAllegati() {
		Boolean result = false;
		if (Boolean.TRUE.equals(hasConfPrevedeAllegati()) && !hasSuperamentoLimitAllegati()) {
			result = true;
		}
		return result;
	}
	
	private Boolean hasConfPrevedeAllegati() {
		return conf.get("attach_allegati")!=null?conf.get("attach_allegati").getBooleanValue():null;
	}
	
	private Boolean hasSuperamentoLimitAllegati() {
		if (hasSuperamentoLimitAllegati!=null) {
			return hasSuperamentoLimitAllegati;
		}
		boolean result = false;
		AllegatiSummary allegatiSummary = allegatiService.getAllegatiSummary(getIstanzaSaved().getIdIstanza());
		if (getLimitNumeroAllegati() > 0 && allegatiSummary.getNumeroAllegati() > getLimitNumeroAllegati()) {
			result = true;
		}
		if (allegatiSummary.getDimensioneTotale()!=null && getLimitDimensioneTotaleAllegati()!=null &&
				allegatiSummary.getDimensioneTotale() > getLimitDimensioneTotaleAllegati()) {
			result = true;
		}
		this.hasSuperamentoLimitAllegati = result;
		return result;
	}

	private int getLimitNumeroAllegati() {
		return conf.get("limit_numero_allegati")!=null?conf.get("limit_numero_allegati").getIntValue():LIMIT_MAX_NUMERO_ALLEGATI;
	}

	protected Long getLimitDimensioneTotaleAllegati() {
		return conf.get("limit_dimensione_totale_allegati")!=null?conf.get("limit_dimensione_totale_allegati").getLongValue():LIMIT_MAX_DIMENSIONE_TOTALE_ALLEGATI;
	}

	/**
	 * Replace dinamici usato per  subject, text, html
	 *
	 * @param textValue
	 *
	 * @return textValue con replace effettuati
	 * @throws DAOException 
	 * @throws ItemNotFoundDAOException 
	 */
	private String replaceDinamici(String textValue) throws ItemNotFoundDAOException, DAOException {
		if (textValue==null)
			return null;
    	log.debug("[" + CLASS_NAME + "::replaceDinamici] BEGIN");
		String result = textValue;
		if (result.contains("@@")) {
			// User
			result = result.replaceAll("@@IDENTIFICATIVO_UTENTE@@", user.getIdentificativoUtente()!=null?user.getIdentificativoUtente():"");
			result = result.replaceAll("@@CODICE_FISCALE@@", getIstanzaSaved().getCodiceFiscaleDichiarante()!=null?getIstanzaSaved().getCodiceFiscaleDichiarante():"");
			result = result.replaceAll("@@Cognome@@", getIstanzaSaved().getCognomeDichiarante()!=null?StringUtils.capitalise(getIstanzaSaved().getCognomeDichiarante()):"");
			result = result.replaceAll("@@Nome@@", getIstanzaSaved().getNomeDichiarante()!=null?StringUtils.capitalise(getIstanzaSaved().getNomeDichiarante()):"");
			result = result.replaceAll("@@COGNOME@@", getIstanzaSaved().getCognomeDichiarante()!=null?getIstanzaSaved().getCognomeDichiarante().toUpperCase():"");
			result = result.replaceAll("@@NOME@@", getIstanzaSaved().getNomeDichiarante()!=null?getIstanzaSaved().getNomeDichiarante().toUpperCase():"");
			result = result.replaceAll("@@NOME_COGNOME@@", String.join(" ", 
					getIstanzaSaved().getNomeDichiarante()!=null?getIstanzaSaved().getNomeDichiarante().toUpperCase():"",
					getIstanzaSaved().getCognomeDichiarante()!=null?getIstanzaSaved().getCognomeDichiarante().toUpperCase():""));
			result = result.replaceAll("@@NOME-COGNOME@@", String.join("-", 
					getIstanzaSaved().getNomeDichiarante()!=null?getIstanzaSaved().getNomeDichiarante().toUpperCase():"",
					getIstanzaSaved().getCognomeDichiarante()!=null?getIstanzaSaved().getCognomeDichiarante().toUpperCase():""));
			result = result.replaceAll("@@COGNOME_NOME@@", String.join(" ", 
					getIstanzaSaved().getCognomeDichiarante()!=null?getIstanzaSaved().getCognomeDichiarante().toUpperCase():"",
					getIstanzaSaved().getNomeDichiarante()!=null?getIstanzaSaved().getNomeDichiarante().toUpperCase():""));
			result = result.replaceAll("@@COGNOME-NOME@@", String.join("-", 
					getIstanzaSaved().getCognomeDichiarante()!=null?getIstanzaSaved().getCognomeDichiarante().toUpperCase():"",
					getIstanzaSaved().getNomeDichiarante()!=null?getIstanzaSaved().getNomeDichiarante().toUpperCase():""));
			//
			result = result.replaceAll("@@ATTORE_INS@@", getIstanzaSaved().getAttoreIns()!=null?getIstanzaSaved().getAttoreIns().toUpperCase():"");
			if (result.contains("_CONTO_TERZI@@")) {
				UtenteEntity operatore = null;
				if (getIstanzaSaved().getAttoreIns()!=null && !getIstanzaSaved().getAttoreIns().equals(getIstanzaSaved().getCodiceFiscaleDichiarante())) {
					operatore = utenteDAO.findByIdentificativoUtente(getIstanzaSaved().getAttoreIns());
				}
				result = result.replaceAll("@@ATTORE_INS_CONTO_TERZI@@", operatore!=null&&operatore.getIdentificativoUtente()!=null?operatore.getIdentificativoUtente().toUpperCase():"");
				result = result.replaceAll("@@Cognome_CONTO_TERZI@@", operatore!=null&&operatore.getCognome()!=null?StringUtils.capitalise(operatore.getCognome()):"");
				result = result.replaceAll("@@Nome_CONTO_TERZI@@", operatore!=null&&operatore.getNome()!=null?StringUtils.capitalise(operatore.getNome()):"");
				result = result.replaceAll("@@COGNOME_CONTO_TERZI@@", operatore!=null&&operatore.getCognome()!=null?operatore.getCognome().toUpperCase():"");
				result = result.replaceAll("@@NOME_CONTO_TERZI@@", operatore!=null&&operatore.getNome()!=null?operatore.getNome().toUpperCase():"");
			}
			// Ente
			result = result.replaceAll("@@ID_ENTE@@", getIstanzaSaved().getIdEnte().toString());
			if (result.contains("_ENTE@@")) {
				EnteEntity ente = enteDAO.findById(getIstanzaSaved().getIdEnte());
				result = result.replaceAll("@@CODICE_ENTE@@", ente.getCodiceEnte());
				result = result.replaceAll("@@NOME_ENTE@@", ente.getNomeEnte());
				result = result.replaceAll("@@DESCRIZIONE_ENTE@@", ente.getDescrizioneEnte());
				result = result.replaceAll("@@LOGO_ENTE@@", ente.getLogo());
			}
			// Area
			if (result.contains("_AREA@@")) {
				AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(getIstanzaSaved().getModulo().getIdModulo(), getIstanzaSaved().getIdEnte());
				AreaEntity area = areaDAO.findById(areaModulo.getIdArea());
				result = result.replaceAll("@@CODICE_AREA@@", area.getCodiceArea());
				result = result.replaceAll("@@NOME_AREA@@", area.getNomeArea());
			}
			// Modulo
			result = result.replaceAll("@@ID_MODULO@@", getIstanzaSaved().getModulo().getIdModulo().toString());
			result = result.replaceAll("@@CODICE_MODULO@@", getIstanzaSaved().getModulo().getCodiceModulo());
//			result = result.replaceAll("@@ID_MODULO_VERSIONE@@", istanzaSaved.getModulo().getIdModuloVersione().toString());
			result = result.replaceAll("@@VERSIONE_MODULO@@", getIstanzaSaved().getModulo().getVersioneModulo());
			result = result.replaceAll("@@OGGETTO_MODULO@@", getIstanzaSaved().getModulo().getOggettoModulo());
			result = result.replaceAll("@@DESCRIZIONE_MODULO@@", getIstanzaSaved().getModulo().getDescrizioneModulo());
			// Istanza
			result = result.replaceAll("@@ID_ISTANZA@@", getIstanzaSaved().getIdIstanza().toString());
			result = result.replaceAll("@@CODICE_ISTANZA@@", getIstanzaSaved().getCodiceIstanza());
			result = result.replaceAll("@@CREATED@@", getIstanzaSaved().getCreated()==null?"":sdf.format(getIstanzaSaved().getCreated()));
			result = result.replaceAll("@@MODIFIED@@", getIstanzaSaved().getModified()==null?"":sdf.format(getIstanzaSaved().getModified()));
			// Save Istanza Descrizione
			result = result.replaceAll("@@SAVE_ISTANZA_RESPONSE_DESCRIZIONE@@", istanzaSaveResponse!=null?istanzaSaveResponse.getDescrizione():null);
			// Specific
			if (result.contains("@@SPECIFIC:")) {
				result = replaceAllSpecific(result);
			}
			// Protocollo
			result = result.replaceAll("@@NUMERO_PROTOCOLLO@@", getIstanzaSaved().getNumeroProtocollo()==null?"":getIstanzaSaved().getNumeroProtocollo());
			result = result.replaceAll("@@DATA_PROTOCOLLO@@", getIstanzaSaved().getDataProtocollo()==null?"":sdf.format(getIstanzaSaved().getDataProtocollo()));
		}
		if (result.contains("##")) {
			ArrayList<String> keysFormated = extractByHashSign(result);
			
	        for(String keyFormated: keysFormated) {
	        	String value = "";
				String formioType = "Text";
	        	String key = keyFormated;
	        	if (keyFormated.contains("!")) {
	        		key = keyFormated.split("!")[0];
	        		formioType = keyFormated.split("!")[1];
	        	}
	        	value = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key, formioType);
	        	result = result.replaceAll("##"+keyFormated+"##", value != null ? value : "");
	        }
		}
    	log.debug("[" + CLASS_NAME + "::replaceDinamici] END " + result);
		return result;
	}


	/**
	 * Limitato ad un resplaceSpecific
	 * @@SPECIFIC:TaxiTaskHelper.getBuoni@@
	 * @param result
	 * @return
	 */
    private String replaceAllSpecific(String textValue) {
		try {
	    	String[] strings = textValue.split("@@SPECIFIC:");
	    	String result = strings[0];
	    	String pck = this.getClass().getPackageName();
	    	String specClassName = strings[1].substring(0,strings[1].indexOf(".")); // "TaxiTaskHelper"
	    	String specificClassName = pck + ".specific." + specClassName;
	    	String specMethodName = strings[1].substring(strings[1].indexOf(".")+1,strings[1].indexOf("@@"));//"getBuoni";
	    	String finalResult = strings[1].substring(strings[1].indexOf("@@")+2,strings[1].length());
	    	
			Class<?> specificTaskHelperClass = Class.forName(specificClassName);
	        Object specificTaskHelper = specificTaskHelperClass.getDeclaredConstructors()[0].newInstance(getIstanzaSaved(),this.confSendEmail);
	        Method specMethod = Class.forName(specificClassName).getMethod(specMethodName);
//	        String replacedValue = (String) specMethod.invoke(specificTaskHelper);
			
	        String replacedValue = null;
	        int count = 0;
	        final int MAX_TRIES = 10;
	        final int SLEEP_MS = 50; // MAX wait for 0.5s  500ms
	        while(StringUtils.isEmpty(replacedValue) && count++ < MAX_TRIES){
	        	replacedValue = (String) specMethod.invoke(specificTaskHelper);
	        	if (StringUtils.isEmpty(replacedValue)) {
	        		log.warn("[" + CLASS_NAME + "::replaceAllSpecific] replacedValue NOT FOUND. SLEEP_MS " + SLEEP_MS);
	        		TimeUnit.MILLISECONDS.sleep(SLEEP_MS);
	        	}
	        }
	        if(StringUtils.isEmpty(replacedValue)){
	        	log.error("[" + CLASS_NAME + "::replaceAllSpecific] replacedValue NOT FOUND. for id_istanza:" + getIstanzaSaved().getIdIstanza());
	        }
	        
			return result + replacedValue + finalResult;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::replaceAllSpecific] ERRORE ", e);
			return "";
		}
	}
    
	private ArrayList<String> extractByHashSign(String str)
    {
		String regex = "\\##(.*?)\\##";
        Pattern p = Pattern.compile(regex); 
        Matcher m = p.matcher(str);
 
        ArrayList<String> arrlist = new ArrayList<String>();
        while (m.find()) {
       	
        	arrlist.add(m.group(1));
        }
        
        return arrlist;
    }



	/**
     * Valida Request to, cc, bcc, subject, text
     * @param request
     */
    private void validaEmailRequest(EmailRequest request) throws BusinessException {
    	log.debug("[" + CLASS_NAME + "::validaEmailRequest] BEGIN");
    	StringBuilder sb = new StringBuilder();
		if (request==null) {
			log.error("[" + CLASS_NAME + "::validaEmailRequest] request null.");
			throw new BusinessException("ValidaEmailRequestException");
		}
		//
		if (!isValidEmail(request.getTo())) {
			sb.append("\nTo:").append(request.getTo()).append(" is not valide.");
		}
		if (StringUtils.isNotEmpty(request.getCc()) && !isValidEmail(request.getCc())) {
			sb.append("\nCc:").append(request.getCc()).append(" is presente and not valide.");
		}
		if (StringUtils.isNotEmpty(request.getBcc()) && !isValidEmail(request.getBcc())) {
			sb.append("\nBcc:").append(request.getBcc()).append(" is presente and not valide.");
		}
		if (StringUtils.isEmpty(request.getSubject())) {
			sb.append("\nSubject:").append(request.getSubject()).append(" is empty.");
		}
		if (StringUtils.isEmpty(request.getText())) {
			sb.append("\nText:").append(request.getText()).append(" is empty.");
		}
		//
		if (sb.length() != 0) {
			log.error("[" + CLASS_NAME + "::validaEmailRequest] " + sb.toString());
			throw new BusinessException("ValidaEmailRequestException");
		}
	}
    public static boolean isValidEmail(String email) {
        return email!=null?Pattern.matches(EMAIL_VALIDATE_REGEX, email):false;
    }


	//
	//
	private JsonNode getConf() throws Exception {
		if (conf==null) {
			conf = readConfJson(confSendEmail);
		}
		return conf;
	}

	private JsonNode readConfJson(String strJson) throws Exception {
		try {
			if(log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: "+strJson);
			}

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);

			return result;
		} catch (IOException e) {
		    log.error("[" + CLASS_NAME + "::readConfJson] ERROR "+e.getMessage());
		    throw e;
		} finally {
			log.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}

	private DatiIstanzaHelper getDatiIstanzaHelper() {
		if (datiIstanzaHelper==null) {
			datiIstanzaHelper = new DatiIstanzaHelper();
			try {
				datiIstanzaHelper.initDataNode(getIstanzaSaved());
			} catch (BusinessException e) {
				log.error("[" + CLASS_NAME + "::getDatiIstanzaHelper] + ERROR ", e);
			}
		}
		return datiIstanzaHelper;
	}
}
