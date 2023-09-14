/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione.coto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.azione.AzioneService;
import it.csi.moon.moonbobl.business.service.impl.azione.Azione_Default;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonprintDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.NotificaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.CambioResidenzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.ComunicazioneHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.GeneralHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.dto.moonfobl.EmailAttachment;
import it.csi.moon.moonbobl.dto.moonfobl.EmailRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoLogEmail;

/**
 * Azione_Default - Azioni di default per i moduli che non necessitano specializzazione.
 * 
 * @author Alberto
 *
 */
public class Azione_TRIB_TARI extends Azione_Default implements AzioneService {
	
	private final static String CLASS_NAME = "Azione_TRIB_TARI";
	protected Logger log = LoggerAccessor.getLoggerBusiness();
	
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
	
	public Azione_TRIB_TARI() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
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
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +" la informiamo che le è stata inviata, da parte dell'ufficio competente, una richiesta di integrazione.\n";
		testo += "Può presentare la documentazione necessaria seguendo le seguenti istruzioni: \n" 
				+ " - sul sistema TORINO FACILE, accedere al servizio TARI - agevolazione COVID \n" 
				+ " - una volta completato l'accesso al servizio, accedere alla sezione \"Le mie istanze\", scheda \"In attesa di integrazione\" \n" 
				+ " - premere su \"Esegui integrazione\" \n" 
				+ " - allegare i seguenti documenti: ";
					
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		testo += "\n\n" + Constants.TESTO_NOREPLY;
		log.debug("[" + CLASS_NAME +  "::"+"richiediIntegrazione - "+ "TESTO EMAIL = "+testo);

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
					+ "l'ufficio preposto ha concluso il procedimento con esito positivo.\n";			
			
			if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
			{
				testo += "\n\nComunicazione:\n" + notificaActionEntity.getTesto();
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
				testo += "\n\nComunicazione:\n" + notificaActionEntity.getTesto();
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
		
		
	}
}
