/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaPdfDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonprintDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AreaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.LogEmailEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.NotificaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.CambioResidenzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.ComunicazioneHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaBoHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.GeneralHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.WfProtocolloEdil;
import it.csi.moon.moonbobl.business.service.impl.helper.task.ProtocollaIstanzaTask;
import it.csi.moon.moonbobl.dto.moonfobl.Azione;
import it.csi.moon.moonbobl.dto.moonfobl.EmailAttachment;
import it.csi.moon.moonbobl.dto.moonfobl.EmailRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonprint.MoonPrintDocumentoAccoglimento;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoDiniego;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoRicevuta;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoLogEmail;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoModificaDati;

/**
 * Azione_Default - Azioni di default per i moduli che non necessitano specializzazione.
 * 
 * @author laurent
 *
 */
public class Azione_Default implements AzioneService {
	
	private final static String CLASS_NAME = "Azione_Default";
	protected Logger log = LoggerAccessor.getLoggerBusiness();
	
	protected long DELAY_FIRST_SEND_EMAIL_IN_MS = 4000;
	
	@Autowired
	MoonprintDAO moonprintDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	NotificaDAO notificaDAO;
	@Autowired
	LogEmailDAO logEmailDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	IstanzaPdfDAO istanzaPdfDAO;
	@Autowired
	AreaDAO areaDAO;
	
	public Azione_Default() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	public Long generaSalvaRicevutaPdf(UserInfo user, String datiAzione, Istanza istanza) throws BusinessException {
		log.debug("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] Unimplemented FOR idModulo="+istanza.getModulo().getCodiceModulo()+" requested for idIstanza="+istanza.getIdIstanza());
		throw new BusinessException("Unimplemented");
	}

	/**
	 * Richiama MOOnPrint per generare un PDF
	 * @param moonPrintParam
	 * @return il pdf
	 * @throws BusinessException
	 */
	protected byte[] generaPdf(MoonprintDocumentoRicevuta moonPrintParam) throws BusinessException {
		try {
			return validaPdf(moonprintDAO.printPdf(moonPrintParam));
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::printPdf] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore printPdf istanza moonprintDAO");
		}
	}
	
	protected byte[] generaPdf(MoonprintDocumentoDiniego moonPrintParam) throws BusinessException {
		try {
			return validaPdf(moonprintDAO.printPdf(moonPrintParam));
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::printPdf] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore printPdf istanza moonprintDAO");
		}
	}
	protected byte[] generaPdf(MoonPrintDocumentoAccoglimento moonPrintParam) throws BusinessException {
		try {
			return validaPdf(moonprintDAO.printPdf(moonPrintParam));
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::printPdf] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore printPdf istanza moonprintDAO");
		}
	}

	@Override
	public void inviaRicevuta(UserInfo user, String datiAzione, Istanza istanza) throws BusinessException {
		log.debug("[" + CLASS_NAME + "::inviaRicevuta] Unimplemented FOR idModulo="+istanza.getModulo().getCodiceModulo()+" requested for idIstanza="+istanza.getIdIstanza());
//		throw new BusinessException("Unimplemented");
	}


	@Override
	public void richiediIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		log.debug("[" + CLASS_NAME +  "::"+"richiediIntegrazione "+istanza.getModulo().getCodiceModulo()+" requested for idIstanza="+istanza.getIdIstanza());
		
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = CambioResidenzaHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);

		String oggettoNotifica = "Richiesta di integrazione";
		request.setSubject(oggettoNotifica);	
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +" la informiamo che le è stata inviata, da parte dell'ufficio competente, una richiesta di integrazione riportata in calce alla presente.\n";
		testo += "Può PRESENTARE LA DOCUMENTAZIONE ad integrazione, accedendo nuovamente al sistema di consultazione delle istanze inviate, nella sezione \"Le mie istanze\" - \"In attesa di integrazione\"" +
			     " e cliccando sul pulsante verde \"Esegui integrazione\".\n" + 
				"Una volta entrato, clicca su \"Invia integrazione\" e allega la documentazione richiesta.";
		
		log.debug("[" + CLASS_NAME +  "::"+"richiediIntegrazione - "+ "TESTO EMAIL = "+testo);
				
		//testo += urlSistema;
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
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
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}
		
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );
		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RICHIESTA_INTEGRAZIONE);
		/*
		 *  invio email  
		 *  TODO: spostare l'invio mail in un task parallelo, affinché una eventuale errore 
		 *  nell'invio non infici tutto il processo di richiesta integrazione
		 */
				
	}
	
	@Override
	public void richiediRicevutaPagamento(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		log.debug("[" + CLASS_NAME +  "::"+"richiediRicevutaPagamento "+istanza.getModulo().getCodiceModulo()+" requested for idIstanza="+istanza.getIdIstanza());
		
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = CambioResidenzaHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);

		String oggettoNotifica = "Richiesta ricevuta di pagamento";
		request.setSubject(oggettoNotifica);	
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +" la informiamo che le è stata inviata, da parte dell'ufficio competente riportata in calce alla presente.\n"+
	            "Può PRESENTARE LA DOCUMENTAZIONE da allegare, accedendo nuovamente al  sistema di consultazione delle istanze inviate, nella sezione \"Le mie istanze\" - \"In attesa di integrazione\"" +
	            " e cliccando sul pulsante verde \"Esegui integrazione\".\n"+
	            "Una volta entrato, clicca su \"Invia ricevuta\" e allega la documentazione richiesta.";
		
		log.debug("[" + CLASS_NAME +  "::"+"richiediRicevutaPagamento - "+ "TESTO EMAIL = "+testo);
		
		//testo += urlSistema;
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
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
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}
		
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );
		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RICHIESTA_INTEGRAZIONE);
		/*
		 *  invio email  
		 *  TODO: spostare l'invio mail in un task parallelo, affinché una eventuale errore 
		 *  nell'invio non infici tutto il processo di richiesta integrazione
		 */
				
	}
	
	@Override
	public void richiediOsservazioni(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		log.debug("[" + CLASS_NAME +  "::"+"richiediOsservazioni "+istanza.getModulo().getCodiceModulo()+" requested for idIstanza="+istanza.getIdIstanza());
		
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = CambioResidenzaHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);

		String oggettoNotifica = "Richiesta osservazioni";
		request.setSubject(oggettoNotifica);	
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +" la informiamo che le è stata inviata, da parte dell'ufficio competente la richiesta di produrre delle osservazioni.\n"+
	            "Può PRESENTARE LA DOCUMENTAZIONE da allegare, accedendo nuovamente al sistema di consultazione delle istanze inviate, nella sezione \"Le mie istanze\" - \"In attesa di integrazione\"" +
	            " e cliccando sul pulsante verde \"Esegui integrazione\".\n"+
	            "Una volta entrato, clicca su \"Invia osservazioni\" e allega la documentazione richiesta.";
		
		log.debug("[" + CLASS_NAME +  "::"+"richiediOsservazioni - "+ "TESTO EMAIL = "+testo);
		
		//testo += urlSistema;
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
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
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}
		
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );
		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RICHIESTA_INTEGRAZIONE);	
				
	}

	@Override
	public void richiediUlterioreIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) 
			throws BusinessException {
		
		log.debug("[" + CLASS_NAME +  "::"+"invitoConformareDebiti] Unimplemented FOR idModulo="+istanza.getModulo().getCodiceModulo()+" requested for idIstanza="+istanza.getIdIstanza());
//		throw new BusinessException("Unimplemented");
	}
	
	protected void sendLogEmail(Istanza istanza, String emailDestinatario, EmailRequest request, DecodificaTipoLogEmail tipologia) {
		String tPadreName = Thread.currentThread().getName();
		ExecutorService ex = Executors.newSingleThreadExecutor();
		Runnable runnable = () -> {
			try {
				log.debug("[" + CLASS_NAME + "::sendLogEmail] " + Thread.currentThread().getName() + " waiting for " + DELAY_FIRST_SEND_EMAIL_IN_MS + "ms ... (" + tPadreName + ")");
				Thread.sleep(DELAY_FIRST_SEND_EMAIL_IN_MS);
				log.debug("[" + CLASS_NAME + "::sendLogEmail] " + Thread.currentThread().getName() + " running ... (" + tPadreName + ")");
			} catch (Exception e) {
				log.error("[" + CLASS_NAME + "::sendLogEmail] Exception", e);
			}
			String logEsito = sendEmail(request);
			logEmail(istanza, emailDestinatario, logEsito, tipologia);
		};
		ex.submit(runnable);	
	}
	
	private String sendEmail(EmailRequest request) {
		String logEsito = "OK";
		try {
			String response = moonsrvDAO.sendEmail(request);
			log.debug("[" + CLASS_NAME + "::sendEmail] : moonsrvDAO.sendEmail() response=" + response);
		} catch (DAOException e) {
    		logEsito = "KO";
		}
		return logEsito;
	}

	private void logEmail(Istanza istanza, String emailDestinatario, String logEsito, DecodificaTipoLogEmail tipologia) {
		// 5. LogEmail 
		LogEmailEntity logEmail = new LogEmailEntity();
		logEmail.setIdTipologia(tipologia.getId()); // 2 MOOnBO-invio richiesta integrazione
		logEmail.setIdEnte(istanza.getIdEnte());
		logEmail.setIdModulo(istanza.getModulo().getIdModulo());
		logEmail.setIdIstanza(istanza.getIdIstanza());
		logEmail.setEmailDestinatario(emailDestinatario);
		logEmail.setTipoEmail("text-attach");
		logEmail.setEsito(logEsito);
		logEmailDAO.insert(logEmail);
	}


	@Override
	public void respingiConComunicazione(UserInfo user, String datiAzione, Istanza istanza,
			StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = ComunicazioneHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
	
		
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);

		String oggettoNotifica = "Richiesta respinta";
		request.setSubject(oggettoNotifica);	
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +" la informiamo che la sua istanza è stata respinta. \n";
		testo += "Motivazione: ";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n" + notificaActionEntity.getTesto();
		}
		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
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
						
     	sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RESPINGI_EMAIL);
		
//		ExecutorService ex = Executors.newSingleThreadExecutor();
//		Runnable runnable = () -> {
//			sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RESPINGI_EMAIL);
//		};
//		ex.submit(runnable);
		
	}
	
	@Override
	public void invitoConformareDebiti(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		log.debug("[" + CLASS_NAME + "::invitoConformareDebiti] Unimplemented FOR idModulo="+istanza.getModulo().getCodiceModulo()+" requested for idIstanza="+istanza.getIdIstanza());
//		throw new BusinessException("Unimplemented");
	}

	@Override
	public void concludiEsitoPositivo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		log.debug("[" + CLASS_NAME + "::invitoConformareDebiti] Unimplemented FOR idModulo="+istanza.getModulo().getCodiceModulo()+" requested for idIstanza="+istanza.getIdIstanza());
//		throw new BusinessException("Unimplemented");
	}
	
	@Override
	public void inviaComunicazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = GeneralHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(false);
		
		String oggettoNotifica = "COMUNICAZIONE";
		request.setSubject(oggettoNotifica);	
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		
		String testo = 
				"In riferimento alla domanda " + istanza.getCodiceIstanza() +  
				" del " + dataInvio + " si comunica quanto segue: " ;

		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		
		testo += "\n\nDistinti saluti" ; 

		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
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
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}
		
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );
			
		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
				
		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
	}
	
	@Override
	public void inviaComunicazioneAvvio(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = GeneralHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(false);
		
		String oggettoNotifica = "COMUNICAZIONE DI AVVIO";
		request.setSubject(oggettoNotifica);	
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		
		String testo = 
				"In riferimento alla domanda " + istanza.getCodiceIstanza() +  
				" del " + dataInvio + " si comunica che l'ufficio ha avviato il procedimento." ;

		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		
		testo += "\n\nDistinti saluti" ; 

		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
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
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}
		
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );
			
		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
				
		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
	}
	
	@Override
	public void inviaDocumentazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = GeneralHelper.parseAzioneInvioDocumentazione(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario =  "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(false);
		
		String oggettoNotifica = "Notifica nuova documentazione";
		request.setSubject(oggettoNotifica);	
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		
		String testo = 
				"In riferimento alla domanda " + istanza.getCodiceIstanza() +  
				" del " + dataInvio + " si comunica che è stata inviata documentazione aggiuntiva " ;

		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		
		testo += "\n\nDistinti saluti" ; 

		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
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
		/*
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormIoNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}
		
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );
		*/
				
		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
	}
	
	@Override
	public void accogli(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = GeneralHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		if (emailDestinatario != null && !emailDestinatario.equals(""))
		{
			// 2. Prepara contenuto notifica
			EmailRequest request = new EmailRequest();
			request.setTo(emailDestinatario);
			request.setPec(false);
			
			if (notificaActionEntity.getEmailCc() != null && notificaActionEntity.getEmailCc().size() > 0)
			{
				request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
			}
			String oggettoNotifica = "Esito procedimento";
			request.setSubject(oggettoNotifica);	
			DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
			String dataInvio = dF.format(istanza.getCreated());
			
			String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +", la informiamo che  "
					+ "l'ufficio preposto ha concluso il procedimento.\n";
			testo += "\nDescrizione esito: ISTANZA ACCOLTA\n" ;
			
			if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
			{
				testo += "\n\n" + notificaActionEntity.getTesto();
			}
			testo += "\n\n" + Constants.TESTO_NOREPLY;
			
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
			
			if (formIoFileNames.size() > 0)
			{
				// creo legame tra notifica e allegati
				for (String formioNomeFileName : formIoFileNames) {
					RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
					notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
					repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
				}
				
				// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
				EmailAttachment attachment = new EmailAttachment();
				attachment.setIdNotifica(idNotifica);
				request.setAttachment(attachment );
			}
			sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
		}
	}
	
	@Override
	public void approva(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = GeneralHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		if (emailDestinatario != null && !emailDestinatario.equals(""))
		{
			// 2. Prepara contenuto notifica
			EmailRequest request = new EmailRequest();
			request.setTo(emailDestinatario);
			request.setPec(false);
			
			if (notificaActionEntity.getEmailCc() != null && notificaActionEntity.getEmailCc().size() > 0)
			{
				request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
			}
			String oggettoNotifica = "Esito procedimento";
			request.setSubject(oggettoNotifica);	
			DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
			String dataInvio = dF.format(istanza.getCreated());
			
			String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +", la informiamo che  "
					+ "l'ufficio preposto ha concluso il procedimento.\n";			
			
			if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
			{
				testo += "\n\nDescrizione esito:\n" + notificaActionEntity.getTesto();
			}
			testo += "\n\n" + Constants.TESTO_NOREPLY;
			
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
			
			if (formIoFileNames.size() > 0)
			{
				// creo legame tra notifica e allegati
				for (String formioNomeFileName : formIoFileNames) {
					RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
					notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
					repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
				}
				
				// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
				EmailAttachment attachment = new EmailAttachment();
				attachment.setIdNotifica(idNotifica);
				request.setAttachment(attachment );
			}
			sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
		}
	}
	
	@Override
	public void approvaParzialmente(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = GeneralHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		if (emailDestinatario != null && !emailDestinatario.equals(""))
		{
			// 2. Prepara contenuto notifica
			EmailRequest request = new EmailRequest();
			request.setTo(emailDestinatario);
			request.setPec(false);
			
			if (notificaActionEntity.getEmailCc() != null && notificaActionEntity.getEmailCc().size() > 0)
			{
				request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
			}
			String oggettoNotifica = "Esito procedimento";
			request.setSubject(oggettoNotifica);	
			DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
			String dataInvio = dF.format(istanza.getCreated());
			
			String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +", la informiamo che  "
					+ "l'ufficio preposto ha concluso il procedimento.\n";			
			
			if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
			{
				testo += "\n\nDescrizione esito:\n" + notificaActionEntity.getTesto();
			}
			testo += "\n\n" + Constants.TESTO_NOREPLY;
			
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
			
			if (formIoFileNames.size() > 0)
			{
				// creo legame tra notifica e allegati
				for (String formioNomeFileName : formIoFileNames) {
					RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
					notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
					repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
				}
				
				// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
				EmailAttachment attachment = new EmailAttachment();
				attachment.setIdNotifica(idNotifica);
				request.setAttachment(attachment );
			}
			sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
		}
	}
	
	@Override
	public void elimina(UserInfo user, Istanza istanza) throws BusinessException {
		
		IstanzaEntity eIstanza = istanzaDAO.findById(istanza.getIdIstanza());
		eIstanza.setFlagEliminata("S");
		istanzaDAO.update(eIstanza);
	}
	
	@Override
	public void presaAtto(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		log.debug("[" + CLASS_NAME + "::invitoConformareDebiti] Unimplemented FOR idModulo="+istanza.getModulo().getCodiceModulo()+" requested for idIstanza="+istanza.getIdIstanza());
//		throw new BusinessException("Unimplemented");
	}
	
	@Override
	public void riportaInBozza(UserInfo user, String datiAzione, Istanza istanza,
			StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = ComunicazioneHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
	
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);

		String oggettoNotifica = "Istanza restituita per modifiche";
		request.setSubject(oggettoNotifica);	
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +" la informiamo che le è stata restituita per modifiche. \n"
				+ "Acceda al sistema di modulistica e, nella sezione domande in lavorazione, troverà l'istanza con la  " 
				+ "possibilità di effettuare le modifiche necessarie.\n";
		testo += getUrlPortale(istanza.getIdIstanza());
		
		testo += "\nMotivazione: ";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n" + notificaActionEntity.getTesto();
		}
		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
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
		
		Date now = new Date();
		// CRON
		// last CRON
		IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(istanza.getIdIstanza());
		lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
		lastIstanzaCronologia.setDataFine(now);
		istanzaDAO.updateCronologia(lastIstanzaCronologia);
		// new CRON
		IstanzaCronologiaStatiEntity eIstanzaCronologia = new IstanzaCronologiaStatiEntity();
		eIstanzaCronologia.setIdIstanza(istanza.getIdIstanza());
		eIstanzaCronologia.setIdStatoWf(storicoEntity.getIdStatoWfArrivo());
		eIstanzaCronologia.setAttoreIns(user.getIdentificativoUtente());
		eIstanzaCronologia.setDataInizio(now);
		eIstanzaCronologia.setIdAzioneSvolta(storicoEntity.getIdAzione());
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
		eIstanza.setAttoreUpd(user.getIdentificativoUtente());
		eIstanza.setIdStatoWf(storicoEntity.getIdStatoWfArrivo());
		eIstanza.setHashUnivocita(null);
		eIstanza.setCurrentStep(0);
		istanzaDAO.update(eIstanza);
						
     	sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);

     	// Elimino PDF se presente
		istanzaPdfDAO.deleteByIdIstanza(istanza.getIdIstanza());
		
	}
	
	private String getUrlPortale (Long idIstanza) {
		String urlPortale = "";
		
		return urlPortale;
	}
	
	
	/**
	 * Protocolla la notifica se presente idFile di essa nell'ultimo storicoWorkflow dell'istanza
	 * @param user
	 * @param azione
	 * @param idIstanza
	 * @throws BusinessException
	 */
	public void protocollaRicevuta(UserInfo user, Azione azione, Long idIstanza) throws DAOException, BusinessException {
		try {
			StoricoWorkflowEntity lastStoricoWf = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			if (lastStoricoWf.getIdFileRendering()==null) {
				log.error("[" + CLASS_NAME + "::protocollaRicevuta] IdFileRendering NULL on lastStoricoWf ! "+lastStoricoWf);
				throw new BusinessException();
			}
			log.debug("[" + CLASS_NAME + "::protocollaRicevuta] idFileRendering: " + lastStoricoWf.getIdFileRendering());
			String resp = moonsrvDAO.protocollaFile(lastStoricoWf.getIdFileRendering());
			log.debug("[" + CLASS_NAME + "::protocollaRicevuta] resp = " + resp);
		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::protocollaRicevuta] Exception" + e.getMessage());
			throw new BusinessException();
		}
	}


	/**
	 * Valida che bytes inizia con %PDF
	 * Commentato il controllo sulla fine del file, sembra che venga generato con un file DOS con \n in piu final
	 * @param bytes
	 * @return bytes se inizia con %PDF altrimenti Exception
	 */
	private byte[] validaPdf(byte[] bytes) {
		byte[] header = new byte[] { bytes[0], bytes[1], bytes[2], bytes[3] };
		log.info("[" + CLASS_NAME + "::validaPdf] header: " + header + "  =>  " + new String(header) + " expected %PDF");
		var isHeaderValid = header[0] == 0x25 && header[1] == 0x50 && header[2] == 0x44 && header[3] == 0x46; //%PDF
//		byte[] trailer = new byte[] { bytes[bytes.length - 5], bytes[bytes.length - 4], bytes[bytes.length - 3], bytes[bytes.length - 2], bytes[bytes.length - 1] };
//		LOG.info("[" + CLASS_NAME + "::validaPdf] trailer: " + trailer + "  =>  " + new String(trailer) + " expected %%EOF");
//		var isTrailerValid = trailer[0] == 0x25 && trailer[1] == 0x25 && trailer[2] == 0x45 && trailer[3] == 0x4f && trailer[4] == 0x46; //%%EOF
		if (!(isHeaderValid /*&& isTrailerValid*/)) {
			log.error("[" + CLASS_NAME + "::validaPdf] NOT_VALID_PDF  isHeaderValid=" + isHeaderValid /*+ "  isTrailerValid=" + isTrailerValid*/);
			throw new BusinessException("NOT_VALID_PDF");
		}
		return bytes;
	}
	
	@Override
	public void accoglimento(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void diniego(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void creaRispostaConProtocollo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void comunicaEsitoPositivo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) {
		// TODO Auto-generated method stub
	}

	@Override
	public void respingi(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void chiudi(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void inserisciProtocollo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
		String numeroProtocollo = "";
		Date dataProtocollo = null;
		try {
			WfProtocolloEdil getProtocolloEdil = datiIstanzaBoHelper.getProtocolloEdil(datiAzione);
			if (getProtocolloEdil != null) 
			{
				numeroProtocollo = getProtocolloEdil.getNumeroProtocollo();
				String strDataProtocollo = getProtocolloEdil.getDataProtocollo().substring(0, 10);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				dataProtocollo = format.parse(strDataProtocollo);
			}
			 
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::inserisciProtocollo] Errore parsing dati protocollo - ",e );
			throw new BusinessException("Errore parsing dati protocollo");
		}
		
		IstanzaEntity eIstanza = new IstanzaEntity();
		eIstanza.setIdIstanza(istanza.getIdIstanza());
		eIstanza.setNumeroProtocollo(numeroProtocollo);
		eIstanza.setDataProtocollo(dataProtocollo);
		
		istanzaDAO.updateProtocollo(eIstanza);
	}

	@Override
	public void protocolla(UserInfo user, Long idIstanza) throws DAOException, BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::protocolla] idIstanza: " + idIstanza);
			String resp = new ProtocollaIstanzaTask(user, idIstanza).call();
//			String resp = moonsrvDAO.protocolla(idIstanza); // Sync
			log.debug("[" + CLASS_NAME + "::protocolla] resp = " + resp);
		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::protocolla] Exception idIstanza: " + idIstanza + "  " + e.getMessage());
			throw new BusinessException();
		}
	}
	
	@Override
	public void verificaAnpr(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		try {
			log.info("[" + CLASS_NAME + "::verificaAnpr] idIstanza: " + istanza.getIdIstanza() + " - " + istanza.getCodiceIstanza());
//			String resp = new ProtocollaIstanzaTask(user, idIstanza).call();
//			String resp = moonsrvDAO.protocolla(idIstanza); // Sync
//			log.debug("[" + CLASS_NAME + "::verificaAnpr] resp = " + resp);
		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::verificaAnpr] Exception idIstanza: " + istanza.getIdIstanza() + " - " + istanza.getCodiceIstanza() + "  " + e.getMessage());
			throw new BusinessException();
		}
	}

	@Override
	public void inviaIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		// 1. Recupera mail dell'ufficio destinatario
		String emailDestinatario = getEmailUfficioDestinatario(istanza.getModulo().getIdModulo());
		String cfDestinatario =  "--assente--";
		
		if (! "".equals(emailDestinatario)) 
		{
			// 2. Prepara contenuto notifica
			EmailRequest request = new EmailRequest();
			request.setTo(emailDestinatario);
			request.setPec(false);
			
			String oggettoNotifica = "Notifica di avvenuta integrazione";
			request.setSubject(oggettoNotifica);	
			DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
			String dataInvio = dF.format(istanza.getCreated());
			
			String testo = "In riferimento alla domanda " + istanza.getCodiceIstanza() +  
					" del " + dataInvio + " si comunica che è stata inviata documentazione aggiuntiva " ;
			
			testo += "\n\nDistinti saluti" ; 
	
			testo += "\n\n" + Constants.TESTO_NOREPLY;
			
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
							
			sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
		}
		else {
			log.info("[" + CLASS_NAME + "::inviaIntegrazione] idIstanza: " + istanza.getIdIstanza() + " - email destinatario non impostata");
		}
	}
	
	private String getEmailUfficioDestinatario(Long iModulo) {
		String email = "";
		AreaEntity area = areaDAO.findByIdModulo(iModulo);
		if (area != null) {
			email = area.getEmail();
		}
		return email;
	}

	@Override
	public String assegnaOperatore(UserInfo user, String datiAzione, Istanza istanza,
			StoricoWorkflowEntity storicoEntity) throws BusinessException {
		DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
		String codiceFiscaleOperatore = "";
		try {
			codiceFiscaleOperatore = datiIstanzaBoHelper.getOperatoreDaAssegnare(datiAzione);
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::assegnaOperatore] idIstanza: " + istanza.getIdIstanza() + " operatore non trovato");
		}
		return codiceFiscaleOperatore;
	}
}
