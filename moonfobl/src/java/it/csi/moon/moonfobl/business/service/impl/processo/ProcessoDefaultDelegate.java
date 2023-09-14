/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.processo;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.JsonParseException;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.EmailAttachment;
import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.LogEmailEntity;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.entity.NotificaEntity;
import it.csi.moon.commons.entity.ProcessoEntity;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.commons.util.decodifica.DecodificaTipoLogEmail;
import it.csi.moon.commons.util.decodifica.DecodificaTipoModificaDati;
import it.csi.moon.commons.util.decodifica.DecodificaTipoRepositoryFile;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ProcessoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonfobl.business.service.impl.dto.IntegrazioneInitParams;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiAzioneHelper;
import it.csi.moon.moonfobl.business.service.impl.helper.dto.IntegrazioneActionEntity;
import it.csi.moon.moonfobl.business.service.impl.helper.task.AssociaRepositoryFileAdIstanzaTask;
import it.csi.moon.moonfobl.business.service.impl.helper.task.ProtocollaIntegrazioneTask;
import it.csi.moon.moonfobl.business.service.impl.initializer.IntegrazioneInitializer;
import it.csi.moon.moonfobl.business.service.impl.initializer.IstanzaInitializer;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * ProcessoDefaultDelegate - Operazioni di default per i moduli che non hanno specializzazione.
 * 
 * Delegate delle operazioni di IstanzeServiceImpl che possono essere specializzate per Modulo
 * Attualmente 2 metodi :
 * - getInitIstanzaByIdModulo : generalmente per bloccare l'accesso al modulo per esaurimento delle risorsa
 * - saveIstanza : generalmente per salvataggio ulteriore in altre tabelle
 * 
 * saveIstanza rimane transazionale da IstanzeServiceImpl, quindi qualsiasi Exception annulla l'inserimento dell'istanza
 * 
 * @author laurent
 *
 */
public class ProcessoDefaultDelegate implements ProcessoServiceDelegate {

	private static final String CLASS_NAME = "ProcessoDefaultDelegate";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	NotificaDAO notificaDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	LogEmailDAO logEmailDAO;
	@Autowired
	@Qualifier("nocache")
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	WorkflowDAO workflowDAO;
	@Autowired
	ProcessoDAO processoDAO;
	@Autowired
	IstanzaInitializer istanzaInitializer;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	IntegrazioneInitializer integrazioneInitializer;


	
	public ProcessoDefaultDelegate() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	//
	// COMPIE AZIONE
	//
	@Override
	public CompieAzioneResponse compieAzione(UserInfo user, IstanzaEntity istanza, Azione azione, Workflow workflow, ProcessoEntity processo, StoricoWorkflowEntity newStoricoWfEntity) throws BusinessException {
		try {
			LOG.info("[" + CLASS_NAME + "::compieAzione] BEGIN");
			
			CompieAzioneResponse azioneCompiuta = null;

			if (DecodificaAzione.INVIA_INTEGRAZIONE.isCorrectAzione(workflow.getCodiceAzione()) || 
				DecodificaAzione.INVIA_RICEVUTA.isCorrectAzione(workflow.getCodiceAzione()) ||
				DecodificaAzione.INVIA_OSSERVAZIONI.isCorrectAzione(workflow.getCodiceAzione())	) {
				// inviaIntegrazione con/senzaEmail
				azioneCompiuta = compieAzione_inviaIntegrazione(user, azione.getDatiAzione(), istanza, workflow);
				// gestione Protocollazione Integrazione
				LOG.info("[" + CLASS_NAME + "::compieAzione] Gestione Protocollazione Integrazione");
				List<ModuloAttributoEntity> attributiModuloE = moduloAttributiDAO.findByIdModulo(istanza.getIdModulo());
				MapModuloAttributi attributi = new MapModuloAttributi(attributiModuloE);
	    		Boolean protocolloIntegrazione = attributi.getWithCorrectType(ModuloAttributoKeys.PSIT_PROTOCOLLO_INTEGRAZIONE);
	    		LOG.info("[" + CLASS_NAME + "::compieAzione] Gestione Protocollazione Integrazione " + protocolloIntegrazione);
	    		if (Boolean.TRUE.equals(protocolloIntegrazione)) {
	    			LOG.info("[" + CLASS_NAME + "::run] new ProtocollaIntegrazioneTask().call() ...");
	    			Future<String> responseProtocolloIntegrazione = Executors.newSingleThreadExecutor().submit( new ProtocollaIntegrazioneTask(user, istanza.getIdIstanza(), newStoricoWfEntity.getIdStoricoWorkflow() ));
	    		}
			}

			if (DecodificaAzione.CARICA_RICEVUTA_FIRMATA.isCorrectAzione(workflow.getCodiceAzione())) {
				azioneCompiuta = compieAzione_caricaRicevuta(user, azione.getDatiAzione(), istanza, workflow);
				LOG.info("[" + CLASS_NAME + "::compieAzione] CARICA_RICEVUTA_FIRMATA");
			}
// SOLO xBO			
//			if (DecodificaAzione.VERIFICA_ANPR.isCorrectAzione(workflow.getCodiceAzione())) {
//				azioneCompiuta = compieAzione_cambiaStatoIstanza(user, azione.getDatiAzione(), istanza, workflow);
//				LOG.info("[" + CLASS_NAME + "::compieAzione] VERIFICA_ANPR");
//			}
			
			return azioneCompiuta;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] Exception ", e);
			throw new BusinessException();
		}
	}

	//
	// cambiaStatoIstanza
	//
	protected CompieAzioneResponse compieAzione_cambiaStatoIstanza(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) {
		cambiaStatoIstanza(user, datiAzione, istanza, workflow);
		CompieAzioneResponse azioneCompiuta = new CompieAzioneResponse();
		azioneCompiuta.setNomeAzione(workflow.getNomeAzione());
		azioneCompiuta.setCodEsitoAzione("ok");			
		return azioneCompiuta;
	}
	
	//
	// caricRicevuta - Di default e senza email => cambiaStatoIstanza
	//
	protected CompieAzioneResponse compieAzione_caricaRicevuta(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) {
		assegnaIdIstanzaOnRepositoryFile(user, datiAzione, istanza, workflow, DecodificaTipoRepositoryFile.FO_RICEVUTA_CAF);
		return compieAzione_cambiaStatoIstanza(user, datiAzione, istanza, workflow);
	}
	
	private void assegnaIdIstanzaOnRepositoryFile(UserInfo user, String datiAzione, IstanzaEntity istanza,
			Workflow workflow, DecodificaTipoRepositoryFile dTipoRepositoryFile) {
		new AssociaRepositoryFileAdIstanzaTask(user, istanza.getIdIstanza(), workflow.getIdDatiAzione(), datiAzione).call();
	}

	//
	// inviaIntegrazione - Di default e senza email => cambiaStatoIstanza
	//
	protected CompieAzioneResponse compieAzione_inviaIntegrazione(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) {
		return compieAzione_cambiaStatoIstanza(user, datiAzione, istanza, workflow);
	}

	protected void inviaIntegrazioneConEmail(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) {
		LOG.debug("[" + CLASS_NAME + "::inviaIntegrazioneConEmail] BEGIN");
		
		// 1. Notifica integrazione	
		IntegrazioneActionEntity notificaActionEntity = DatiAzioneHelper.parseAzioneNotificaIntegrazione(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> integrazioneFormIoFileNames = notificaActionEntity.getIntegrazioneFormIoFileNames();
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		
		String oggettoNotifica = "Invio integrazione";
		request.setSubject(oggettoNotifica);
		
		// definire testo appropriato per la email di invio integrazione		
		String testo = "Con riferimento all'istanza n."+ istanza.getCodiceIstanza() +" si informa che è stato effettuato un invio integrazione. \n";

		if (notificaActionEntity.getTesto()!= null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		testo += "\n\n" +"Questa e-mail è stata generata automaticamente, si prega di non rispondere a questo messaggio.";
		
		request.setText(testo);
		
		// salvo record in tabella notifica
		NotificaEntity notificaEntity = new NotificaEntity();
		notificaEntity.setIdIstanza(istanza.getIdIstanza());
		notificaEntity.setCfDestinatario(cfDestinatario);
		notificaEntity.setEmailDestinatario(emailDestinatario);
		notificaEntity.setOggettoNotifica(oggettoNotifica);
		notificaEntity.setTestoNotifica(testo);
		notificaEntity.setFlagLetta("N");
		notificaEntity.setFlagArchiviata("N");
		Long idNotifica = notificaDAO.insert(notificaEntity);
		
		// creo legame tra notifica e allegati
		for (String formioNomeFileName : integrazioneFormIoFileNames) {
			LOG.debug("[" + CLASS_NAME + "::inviaIntegrazioneConEmail] LOOP integrazioneFormIoFileNames formioNomeFileName=" + formioNomeFileName);
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica( idNotifica, repositoryFileEntity.getIdFile());		
//			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}
		
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
	    attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment);
		
		// invio email
		String logEsito = "OK";
		try {
			String response = moonsrvDAO.sendEmail(request);
			LOG.debug("[" + CLASS_NAME + "::inviaIntegrazioneConEmail] : moonsrvDAO.sendEmail() response=" + response);
		} catch (DAOException e) {
    		logEsito = "KO";
		}
		
		cambiaStatoIstanza(user, datiAzione, istanza, workflow);
		
		// 5. LogEmail 
		LogEmailEntity logEmail = new LogEmailEntity();
		
		logEmail.setIdTipologia(DecodificaTipoLogEmail.FO_RISPOSTA_INTEGRAZIONE.getId());
		logEmail.setIdEnte(istanza.getIdEnte());
		logEmail.setIdModulo(istanza.getIdModulo());
		logEmail.setIdIstanza(istanza.getIdIstanza());
		logEmail.setEmailDestinatario(emailDestinatario);
		logEmail.setTipoEmail("text-attach");
		logEmail.setEsito(logEsito);
		
		logEmailDAO.insert(logEmail);
	}

	protected void cambiaStatoIstanza(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) {
		LOG.debug("[" + CLASS_NAME + "::cambiaStatoIstanza] BEGIN");
		
		//  inserimento nuovo record in moon_fo_t_cronologia_stati 
		Date now = new Date();
		IstanzaCronologiaStatiEntity eIstanzaCronologia = null;
		IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(istanza.getIdIstanza());
		
		lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
		lastIstanzaCronologia.setDataFine(now);
		istanzaDAO.updateCronologia(lastIstanzaCronologia);

		eIstanzaCronologia = new IstanzaCronologiaStatiEntity();
		eIstanzaCronologia.setIdIstanza(istanza.getIdIstanza());
		eIstanzaCronologia.setIdStatoWf(workflow.getIdStatoWfArrivo());

		eIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
		eIstanzaCronologia.setDataInizio(now);
		eIstanzaCronologia.setIdAzioneSvolta(workflow.getIdAzione());
		istanzaDAO.insertCronologia(eIstanzaCronologia);
	
		// inserisco un nuovo record anche nella tabella dati_istanza
		// DATI
		IstanzaDatiEntity eIstanzaDati = null;
		IstanzaDatiEntity lastIstanzaDati = null;
		lastIstanzaDati = istanzaDAO.findDati(istanza.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
		eIstanzaDati = new IstanzaDatiEntity();
		eIstanzaDati.setIdIstanza(istanza.getIdIstanza());
		eIstanzaDati.setAttoreUpd(user.getIdentificativoUtente());
		eIstanzaDati.setDataUpd(now);
		eIstanzaDati.setDatiIstanza(lastIstanzaDati.getDatiIstanza());
		eIstanzaDati.setIdCronologiaStati(eIstanzaCronologia.getIdCronologiaStati());
		eIstanzaDati.setIdStepCompilazione(null);
		eIstanzaDati.setIdTipoModifica(DecodificaTipoModificaDati.NON.getIdTipoModifica());
		Long idIstanzaDati = istanzaDAO.insertDati(eIstanzaDati);
		eIstanzaDati.setIdDatiIstanza(idIstanzaDati);
		
		// aggiorno lo stato dell'istanza
		IstanzaEntity eIstanza = istanzaDAO.findById(istanza.getIdIstanza());
		eIstanza.setIdStatoWf(workflow.getIdStatoWfArrivo());
		istanzaDAO.update(eIstanza);	
	}

	@Override
	public DatiAzione getInitData(UserInfo user, Long idIstanza, Workflow workflow, String ipAddress) throws BusinessException {

		LOG.info("[" + CLASS_NAME + "::getInitData] IN idIstanza=" + idIstanza + "  idWorkflow="
				+ workflow.getIdWorkflow());

		DatiAzione datiAzione = new DatiAzione();
		String initDataNomeClass = "";

		ProcessoEntity processo = processoDAO.findById(workflow.getIdProcesso());
		if (DecodificaAzione.INVIA_INTEGRAZIONE.isCorrectAzione(workflow.getCodiceAzione()) || 
				DecodificaAzione.INVIA_RICEVUTA.isCorrectAzione(workflow.getCodiceAzione()) || 
				DecodificaAzione.INVIA_OSSERVAZIONI.isCorrectAzione(workflow.getCodiceAzione()) ) { 
			IntegrazioneInitParams initParams = new IntegrazioneInitParams();
			StoricoWorkflowEntity storico = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			
			//TO_CHECK valutare quale deve essere inizializzazione di default
			initDataNomeClass = "it.csi.moon.moonfobl.business.service.impl.initializer.coto.dem.ModIntegrazioneInitializer";
			integrazioneInitializer.initialize(getInitParams(initParams, storico.getDatiAzione()));
			String datiInit = integrazioneInitializer.getDati(initDataNomeClass);
			/*
			 * Il contesto viene modificato per consentire di visualizzare su FO dei documenti inseriti da BO
			 */
			datiInit = modificaContestoBoToFo(datiInit);
			datiAzione.setData(datiInit);

		} else {
			if (DecodificaAzione.VERIFICA_ANPR.isCorrectAzione(workflow.getCodiceAzione())) {
				initDataNomeClass = "it.csi.moon.moonfobl.business.service.impl.initializer.coto.dem.VerificaAnprInitializer";
			}
			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			AzioneInitParams initParams = new AzioneInitParams();
			initParams.setDatiIstanza(datiE.getDatiIstanza());
			initParams.setCodiceFiscale(user.getCodFiscDichIstanza());
			initParams.setCognome(user.getCognome());
			initParams.setNome(user.getNome());
			initParams.setIdIstanza(idIstanza);
			initParams.setUser(user);
			initParams.setIpAddress(ipAddress);
			istanzaInitializer.initialize(initParams);
			String datiInit = istanzaInitializer.getDati(initDataNomeClass);
			datiInit = modificaContestoBoToFo(datiInit);
			datiAzione.setData(datiInit);
			LOG.info("[" + CLASS_NAME + "::getInitData] OUT datiAzione=" + datiAzione);

		}
		return datiAzione;
	}
	
	
	protected IntegrazioneInitParams getInitParams(IntegrazioneInitParams params, String datiAzione)  throws BusinessException {		

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readValue(datiAzione, JsonNode.class);
			JsonNode data = jsonNode.get("data");

			if (data.has("testo") && data.get("testo") != null) {
				params.setTesto(data.get("testo").asText());
			}
			if (data.has("allegati") && data.get("allegati") != null) {
				params.setAllegati((ArrayNode) data.get("allegati"));
			}
			if (data.has("nome") && data.get("nome") != null) {
				params.setNome(data.get("nome").asText());
			}
			if (data.has("cognome") && data.get("cognome") != null) {
				params.setCognome(data.get("cognome").asText());
			}
			if (data.has("codiceFiscale") && data.get("codiceFiscale") != null) {
				params.setCodiceFiscale(data.get("codiceFiscale").asText());
			}
			if (data.has("email") && data.get("email") != null) {
				params.setEmail(data.get("email").asText());
			}
			if (data.has("emailCc") && data.get("emailCc") != null) {
				params.setEmailCc((ArrayNode) data.get("emailCc"));
			}
						
		} catch (JsonParseException e) {
			LOG.error("[" + CLASS_NAME + "::getInitParams] Errore objectMapper.readValue - ",
					e);
			throw new BusinessException("JsonParseException");
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::getInitParams] Errore objectMapper.readValue - ",
					e);
			throw new BusinessException("IOException");
		}			
		return params;
	
	}
	
	public static String modificaContestoBoToFo(String jsonData) {

		//LOG.debug("[" + CLASS_NAME + "::modificaContestoFoToBo] IN jsonData"+ jsonData);
		try {
			Pattern p = Pattern.compile("/moonbobl/", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			String result = p.matcher(jsonData).replaceAll("/moonfobl/");
			
			String REGEX = "https://[a-zA-Z0-9\\-]+\\.patrim\\.csi\\.it/";
			Pattern pvh = Pattern.compile(REGEX, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			result = pvh.matcher(result).replaceAll("/");	
			
			String REGEX2 = "https://[a-zA-Z0-9\\-]+\\.csi\\.it/";
			Pattern pvh2 = Pattern.compile(REGEX2, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			result = pvh2.matcher(result).replaceAll("/");
						
			return result;
		} catch (Exception e) {			
			//LOG.debug("[" + CLASS_NAME + "::modificaContestoFoToBo] Errore in sostituzione contesto");
			return jsonData;
		}
	}		
	
	
//	protected void protocollaAllegati(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) {
//		LOG.info("[" + CLASS_NAME + "::protocollaAllegati] BEGIN");
//		List<RepositoryFileEntity> fileAllegati = repositoryFileDAO.findByIdIstanza(istanza.getIdIstanza()); // FIXME filtrare gia su DAO idTipologia 3 , e rimove il "if" poco sotto
//		for (RepositoryFileEntity file : fileAllegati) {
//			if (DecodificaTipoRepositoryFile.FO_ALLEGATO_RISPOSTA_INTEGRAZIONE.isCorrectTipologia(file)) {
//				LOG.info("[" + CLASS_NAME + "::protocollaAllegati] responseProtocollo="+file);
//				Future<String> responseProtocollo = Executors.newSingleThreadExecutor().submit( new ProtocollaRepositoryFileTask(user, file.getIdFile()) );
//				LOG.info("[" + CLASS_NAME + "::protocollaAllegati] responseProtocollo="+responseProtocollo);
//			}
//		}
//	}
	
}
