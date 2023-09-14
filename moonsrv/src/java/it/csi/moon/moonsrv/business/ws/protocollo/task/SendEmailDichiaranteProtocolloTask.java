/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.ws.protocollo.task;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.ContextRequest;
import it.csi.moon.commons.dto.EmailAttachment;
import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.AreaEntity;
import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.entity.LogEmailEntity;
import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.commons.util.decodifica.DecodificaTipoLogEmail;
import it.csi.moon.moonsrv.business.service.helper.DatiIstanzaHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.istat.ComuneDAO;
import it.csi.moon.moonsrv.business.service.mail.EmailService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EmailRequestValidator;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * SendEmailDichiaranteProtocolloTask
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
public class SendEmailDichiaranteProtocolloTask implements Callable<String> {

	private static final String CLASS_NAME = "SendEmailDichiaranteProtocolloTask";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private JsonNode conf = null;

	private Istanza istanza;
	private String confSendEmail;
	private EmailRequest emailRequest;

	private DatiIstanzaHelper datiIstanzaHelper;

	@Autowired
	EmailService emailService;
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
	
    public SendEmailDichiaranteProtocolloTask(Istanza istanza, String confSendEmail) {
		super();
		this.istanza = istanza;
		this.confSendEmail = confSendEmail;
		LOG.debug("[" + CLASS_NAME + "::SendEmailDichiaranteProtocolloTask] CONSTRUCTOR istanza.getIdIstanza(): " + getIstanza().getIdIstanza());

        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	private Istanza getIstanza() {
		return istanza;
	}

    /**
     * Invia un email al dichiarante con il pdf dell'istanza
     *
     */
    public String call() throws BusinessException {
    	try {
	    	String result = null;
	        LOG.debug("[" + CLASS_NAME + "::call] Started Task...");

	        // 1. Init JsonNode di configurazione e dataIstanza
	        getConf();
	        LOG.debug("[" + CLASS_NAME + "::call] getConf() OK.");

			String logEsito = "";
			String emailUserDichiarante = "";
	        // Gestione email User Dichiarante
	        emailUserDichiarante = retrieveTo();
			LOG.debug("[" + CLASS_NAME + "::call] moonsrvDAO.sendEmail() User ...");
			try {
				makeValidaSendEmail(emailUserDichiarante);
				result = "Un email riepilogativo è stato mandato all'indirizzo " + retrieveTo();
	    		logEsito += "OK";
			} catch (DAOException e) {
	    		result = "Non è stato possibile inviare l'email riepilogativo all'indirizzo " + retrieveTo();
	    		logEsito += "KO";
			}
			
			// 6. LogEmail
			LogEmailEntity logEmail = new LogEmailEntity();
			logEmail.setIdTipologia(DecodificaTipoLogEmail.SRV_PRT_IN_CALLBACK.getId()); // 9 - SRV_PRT_IN_CALLBACK
			logEmail.setIdEnte(getIstanza().getIdEnte());
			logEmail.setIdModulo(getIstanza().getModulo().getIdModulo());
			logEmail.setIdIstanza(getIstanza().getIdIstanza());
			logEmail.setEmailDestinatario(emailUserDichiarante);
			logEmail.setTipoEmail(conf.get("html")!=null?"html-attach":"text-attach");
			logEmail.setEsito(logEsito);
			logEmailDAO.insert(logEmail);

	        LOG.debug("[" + CLASS_NAME + "::call] Started Completed. " + result);
	        return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::call] ERROR ");
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::call] ERROR ", e);
			throw new BusinessException();
		}
    }

	private void makeValidaSendEmail(String emailUserDichiarante) {
		EmailRequest request = makeEmailRequest(emailUserDichiarante);
		new EmailRequestValidator().valida(request);
		String response = "OK";
		emailService.sendMail(request);
		LOG.debug("[" + CLASS_NAME + "::makeValidaSendEmail] moonsrvDAO.sendEmail() response=" + response);
	}

	private EmailRequest makeEmailRequest(String emailDestinatario) {
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
		context.setIdModulo(getIstanza().getModulo().getIdModulo());
		context.setIdIstanza(getIstanza().getIdIstanza());
		return context;
	}

	private String retrieveTo() {
    	LOG.debug("[" + CLASS_NAME + "::retrieveTo] BEGIN");
		String result = conf.get("to")!=null?conf.get("to").asText().trim():""; // 'to' e opzional in choice con 'to_istanza_data_key', init string empty ""
		String resultToIstanzaDataKey = "";
		String key = conf.get("to_istanza_data_key")!=null?conf.get("to_istanza_data_key").asText().trim():null;
		LOG.debug("[" + CLASS_NAME + "::retrieveTo] Using to_istanza_data_key (if not null): " + key);
		if (StringUtils.isNotEmpty(key)) {
			resultToIstanzaDataKey = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
			if (StringUtils.isNotBlank(resultToIstanzaDataKey)) {
				// caso di compilazione di emtrambi toFisso & email dall istanza
				result += ((StringUtils.isNotBlank(result)?";":"") + resultToIstanzaDataKey.trim());
			}
		}
    	LOG.debug("[" + CLASS_NAME + "::retrieveTo] END " + result);
		return result;
	}

	private String retrieveCc() {
    	LOG.debug("[" + CLASS_NAME + "::retrieveCc] BEGIN");
		String result = conf.get("cc")!=null?conf.get("cc").asText():null;
		if (StringUtils.isEmpty(result)) {
			String key = conf.get("cc_istanza_data_key")!=null?conf.get("cc_istanza_data_key").asText():null;
			if (StringUtils.isNotEmpty(key)) {
				result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
			}
		}
    	if ("rif_asl".equals(result)) {
    		String codiceComune = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey("codiceComune");
			int codice = Integer.parseInt(codiceComune);
			result = comuneDAO.findEmailAslByCodiceComune(codice);
    	}
    	LOG.debug("[" + CLASS_NAME + "::retrieveCc] END " + result);
		return result;
	}

	private String retrieveBcc() {
    	LOG.debug("[" + CLASS_NAME + "::retrieveBcc] BEGIN");
    	String result = conf.get("bcc")!=null?conf.get("bcc").asText():null;
		if (StringUtils.isEmpty(result)) {
			String key = conf.get("bcc_istanza_data_key")!=null?conf.get("bcc_istanza_data_key").asText():null;
			if (StringUtils.isNotEmpty(key)) {
				result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
			}
		}
    	LOG.debug("[" + CLASS_NAME + "::retrieveBcc] END " + result);
		return result;
	}
	private String retrieveProtocolloTo() {
    	LOG.debug("[" + CLASS_NAME + "::retrieveProtocolloTo] BEGIN");
    	String result = conf.get("protocollo_to")!=null?conf.get("protocollo_to").asText():null;
		LOG.debug("[" + CLASS_NAME + "::retrieveProtocolloTo] END " + result);
		return result;
	}
	private String retrieveSubject() {
    	LOG.debug("[" + CLASS_NAME + "::retrieveSubject] BEGIN");
		String result = replaceDinamici(conf.get("subject")!=null?conf.get("subject").asText():null);
    	LOG.debug("[" + CLASS_NAME + "::retrieveSubject] END " + result);
		return result;
	}

	private String retrieveText() {
    	LOG.debug("[" + CLASS_NAME + "::retrieveText] BEGIN");
		String result = replaceDinamici(conf.get("text")!=null?conf.get("text").asText():null);
    	LOG.debug("[" + CLASS_NAME + "::retrieveText] END " + result);
		return result;
	}

	private String retrieveContentHtml() {
    	LOG.debug("[" + CLASS_NAME + "::retrieveContentHtml] BEGIN");
		String result = replaceDinamici(conf.get("html")!=null?conf.get("html").asText():null);
    	LOG.debug("[" + CLASS_NAME + "::retrieveContentHtml] END " + result);
		return result;
	}

	private EmailAttachment retrieveEmailAttach() {
		LOG.debug("[" + CLASS_NAME + "::retrieveEmailAttach] BEGIN");
		if (conf.get("attach_istanza")==null) {
			LOG.debug("[" + CLASS_NAME + "::retrieveEmailAttach] attach_istanza not presente, NULL.");
			return null;
		}
		Boolean bAllegati = conf.get("attach_allegati")!=null?conf.get("attach_allegati").asBoolean():null;
		EmailAttachment result = conf.get("attach_istanza").asBoolean()?new EmailAttachment(getIstanza().getIdIstanza(),bAllegati):null;
		LOG.debug("[" + CLASS_NAME + "::retrieveEmailAttach] END " + result);
		return result;
	}
	
	
	/**
	 * Replace dinamici usato per  subject, text, html
	 *
	 * @param textValue
	 *
	 * @return textValue con replace effettuati
	 */
	private String replaceDinamici(String textValue) {
		if (textValue==null)
			return null;
    	LOG.debug("[" + CLASS_NAME + "::replaceDinamici] BEGIN");
		String result = textValue;
		if (result.contains("@@")) {
			// User
			result = result.replace("@@IDENTIFICATIVO_UTENTE@@", getIstanza().getIdentificativoUtente()!=null?getIstanza().getIdentificativoUtente():"");
			result = result.replace("@@CODICE_FISCALE@@", getIstanza().getCodiceFiscaleDichiarante()!=null?getIstanza().getCodiceFiscaleDichiarante():""); // Attento non funziona per conto terzi
			result = result.replace("@@Cognome@@", getIstanza().getCognomeDichiarante()!=null?WordUtils.capitalize(getIstanza().getCognomeDichiarante()):"");
			result = result.replace("@@Nome@@", getIstanza().getNomeDichiarante()!=null?WordUtils.capitalize(getIstanza().getNomeDichiarante()):"");
			result = result.replace("@@COGNOME@@", getIstanza().getCognomeDichiarante()!=null?getIstanza().getCognomeDichiarante().toUpperCase():"");
			result = result.replace("@@NOME@@", getIstanza().getNomeDichiarante()!=null?getIstanza().getNomeDichiarante().toUpperCase():"");
			result = result.replace("@@NOME_COGNOME@@", String.join(" ", 
					getIstanza().getNomeDichiarante()!=null?getIstanza().getNomeDichiarante().toUpperCase():"",
					getIstanza().getCognomeDichiarante()!=null?getIstanza().getCognomeDichiarante().toUpperCase():""));
			result = result.replace("@@NOME-COGNOME@@", String.join("-", 
					getIstanza().getNomeDichiarante()!=null?getIstanza().getNomeDichiarante().toUpperCase():"",
					getIstanza().getCognomeDichiarante()!=null?getIstanza().getCognomeDichiarante().toUpperCase():""));
			result = result.replace("@@COGNOME_NOME@@", String.join(" ", 
					getIstanza().getCognomeDichiarante()!=null?getIstanza().getCognomeDichiarante().toUpperCase():"",
					getIstanza().getNomeDichiarante()!=null?getIstanza().getNomeDichiarante().toUpperCase():""));
			result = result.replace("@@COGNOME-NOME@@", String.join("-", 
					getIstanza().getCognomeDichiarante()!=null?getIstanza().getCognomeDichiarante().toUpperCase():"",
					getIstanza().getNomeDichiarante()!=null?getIstanza().getNomeDichiarante().toUpperCase():""));
			//
			result = result.replace("@@ATTORE_INS@@", getIstanza().getAttoreIns()!=null?getIstanza().getAttoreIns().toUpperCase():"");
			if (result.contains("_CONTO_TERZI@@")) {
				UtenteEntity operatore = null;
				if (getIstanza().getAttoreIns()!=null && !getIstanza().getAttoreIns().equals(getIstanza().getCodiceFiscaleDichiarante())) {
					operatore = utenteDAO.findByIdentificativoUtente(getIstanza().getAttoreIns());
				}
				result = result.replace("@@ATTORE_INS_CONTO_TERZI@@", operatore!=null&&operatore.getIdentificativoUtente()!=null?operatore.getIdentificativoUtente().toUpperCase():"");
				result = result.replace("@@Cognome_CONTO_TERZI@@", operatore!=null&&operatore.getCognome()!=null?WordUtils.capitalize(operatore.getCognome()):"");
				result = result.replace("@@Nome_CONTO_TERZI@@", operatore!=null&&operatore.getNome()!=null?WordUtils.capitalize(operatore.getNome()):"");
				result = result.replace("@@COGNOME_CONTO_TERZI@@", operatore!=null&&operatore.getCognome()!=null?operatore.getCognome().toUpperCase():"");
				result = result.replace("@@NOME_CONTO_TERZI@@", operatore!=null&&operatore.getNome()!=null?operatore.getNome().toUpperCase():"");
			}
			// Ente
			result = result.replace("@@ID_ENTE@@", getIstanza().getIdEnte().toString());
			if (result.contains("_ENTE@@")) {
				EnteEntity ente = enteDAO.findById(getIstanza().getIdEnte());
				result = result.replace("@@CODICE_ENTE@@", ente.getCodiceEnte());
				result = result.replace("@@NOME_ENTE@@", ente.getNomeEnte());
				result = result.replace("@@DESCRIZIONE_ENTE@@", ente.getDescrizioneEnte());
				result = result.replace("@@LOGO_ENTE@@", ente.getLogo());
			}
			// Area
			if (result.contains("_AREA@@")) {
				AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(getIstanza().getModulo().getIdModulo(), getIstanza().getIdEnte());
				AreaEntity area = areaDAO.findById(areaModulo.getIdArea());
				result = result.replace("@@CODICE_AREA@@", area.getCodiceArea());
				result = result.replace("@@NOME_AREA@@", area.getNomeArea());
			}
			// Modulo
			result = result.replace("@@ID_MODULO@@", getIstanza().getModulo().getIdModulo().toString());
			result = result.replace("@@CODICE_MODULO@@", getIstanza().getModulo().getCodiceModulo());
//			result = result.replace("@@ID_MODULO_VERSIONE@@", istanzaSaved.getModulo().getIdModuloVersione().toString());
			result = result.replace("@@VERSIONE_MODULO@@", getIstanza().getModulo().getVersioneModulo());
			result = result.replace("@@OGGETTO_MODULO@@", getIstanza().getModulo().getOggettoModulo());
			result = result.replace("@@DESCRIZIONE_MODULO@@", getIstanza().getModulo().getDescrizioneModulo());
			// Istanza
			result = result.replace("@@ID_ISTANZA@@", getIstanza().getIdIstanza().toString());
			result = result.replace("@@CODICE_ISTANZA@@", getIstanza().getCodiceIstanza());
			result = result.replace("@@CREATED@@", getIstanza().getCreated()==null?"":sdf.format(getIstanza().getCreated()));
			result = result.replace("@@MODIFIED@@", getIstanza().getModified()==null?"":sdf.format(getIstanza().getModified()));
			// Save Istanza Descrizione
//			result = result.replace("@@SAVE_ISTANZA_RESPONSE_DESCRIZIONE@@", istanzaSaveResponse!=null?istanzaSaveResponse.getDescrizione():null);
			// Save Istanza Descrizione
			if (result.contains("@@SPECIFIC:")) {
				result = replaceAllSpecific(result);
			}
			// Protocollo
			result = result.replace("@@NUMERO_PROTOCOLLO@@", getIstanza().getNumeroProtocollo()==null?"":getIstanza().getNumeroProtocollo());
			result = result.replace("@@DATA_PROTOCOLLO@@", getIstanza().getDataProtocollo()==null?"":sdf.format(getIstanza().getDataProtocollo()));
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
    	LOG.debug("[" + CLASS_NAME + "::replaceDinamici] END " + result);
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
	        Object specificTaskHelper = specificTaskHelperClass.getDeclaredConstructors()[0].newInstance(getIstanza(),this.confSendEmail);
	        Method specMethod = Class.forName(specificClassName).getMethod(specMethodName);
//	        String replacedValue = (String) specMethod.invoke(specificTaskHelper);
			
	        String replacedValue = null;
	        int count = 0;
	        final int MAX_TRIES = 10;
	        final int SLEEP_MS = 50; // MAX wait for 0.5s  500ms
	        while(StringUtils.isEmpty(replacedValue) && count++ < MAX_TRIES){
	        	replacedValue = (String) specMethod.invoke(specificTaskHelper);
	        	if (StringUtils.isEmpty(replacedValue)) {
	        		LOG.warn("[" + CLASS_NAME + "::replaceAllSpecific] replacedValue NOT FOUND. SLEEP_MS " + SLEEP_MS);
	        		sleepMilliseconds(SLEEP_MS);
	        	}
	        }
	        if(StringUtils.isEmpty(replacedValue)){
	        	LOG.error("[" + CLASS_NAME + "::replaceAllSpecific] replacedValue NOT FOUND. for id_istanza:" + getIstanza().getIdIstanza());
	        }
	        
			return result + replacedValue + finalResult;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::replaceAllSpecific] ERRORE ", e);
			return "";
		}
	}
    
    public static void sleepMilliseconds (int val) {
        try { 
            TimeUnit.MILLISECONDS.sleep(val);
        } catch (InterruptedException e) {
        	LOG.error("[" + CLASS_NAME + "::sleepMilliseconds] Thread interrupted");
        	Thread.currentThread().interrupt();
        }
    }
    
	private ArrayList<String> extractByHashSign(String str)
    {
		String regex = "\\##(.*?)\\##";
        Pattern p = Pattern.compile(regex); 
        Matcher m = p.matcher(str);
 
        ArrayList<String> arrlist = new ArrayList<>();
        while (m.find()) {
       	
        	arrlist.add(m.group(1));
        }
        return arrlist;
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
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: "+strJson);
			}
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readConfJson] ERROR "+e.getMessage());
		    throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}

	private DatiIstanzaHelper getDatiIstanzaHelper() {
		if (datiIstanzaHelper==null) {
			datiIstanzaHelper = new DatiIstanzaHelper();
			try {
				datiIstanzaHelper.initDataNode(getIstanza());
			} catch (BusinessException e) {
				LOG.error("[" + CLASS_NAME + "::getDatiIstanzaHelper] + ERROR ", e);
			}
		}
		return datiIstanzaHelper;
	}
}
