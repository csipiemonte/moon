/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione.coto;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.azione.AzioneService;
import it.csi.moon.moonbobl.business.service.impl.azione.Azione_Default;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.NotificaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.CambioResidenzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.TariAreraHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.tari.TariAreraEntity;
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
public class Azione_TRIB_TARI_ARERA extends Azione_Default implements AzioneService {
	
	private final static String CLASS_NAME = "Azione_TRIB_TARI_ARERA";
	protected Logger log = LoggerAccessor.getLoggerBusiness();
	private final static String FIRMA = "Cordiali saluti \n" 
			+ "Divisione Tributi e Catasto \n"
			+ "Servizio IMU e TARI - Ufficio TARI \n"
			+ "Corso Racconigi, 49 - 10139 Torino";
	
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
	
	public Azione_TRIB_TARI_ARERA() {
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
				+ " - sul sistema TORINO FACILE, accedere al modulo TARI inerente la sua richiesta iniziale\n" 
				+ " - una volta completato l'accesso al servizio, accedere alla sezione \"Le mie istanze\", scheda \"In attesa di integrazione\" \n" 
				+ " - premere su \"Esegui integrazione\" \n" 
				+ " - allegare i seguenti documenti: ";
					
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		testo += "\n\n" + FIRMA + "\n\n" + Constants.TESTO_NOREPLY ;
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
	public void accogli(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		TariAreraEntity entityAzione = TariAreraHelper.parseAzioneAccogliRespingi(datiAzione);
		String emailDestinatario = entityAzione.getEmail();
		String oggettoNotifica = "Esito procedimento";
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +
				"\n codice attività: " + entityAzione.getTipologiaRichiesta() +
				"\n codice utente: " + entityAzione.getCodiceUtente() +
				"\n codice utenza: " + entityAzione.getCodiceUtenza() +
				
				"\n la informiamo che l'ufficio preposto ha concluso il procedimento con esito positivo.\n";			
		
		if (entityAzione.getTestoRisposta() != null && !entityAzione.getTestoRisposta().equals(""))
		{
			testo += "\n\n" + entityAzione.getTestoRisposta();
		}
		testo += "\n\n" + FIRMA + "\n\n" + Constants.TESTO_NOREPLY ;
		
		if (emailDestinatario != null && !emailDestinatario.equals(""))
		{
			EmailRequest request = preparaNotifica(entityAzione, oggettoNotifica, testo, istanza.getIdIstanza());
	
	     	sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
		}
	}
	
	@Override
	public void respingi(UserInfo user, String datiAzione, Istanza istanza,
			StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		TariAreraEntity entityAzione = TariAreraHelper.parseAzioneAccogliRespingi(datiAzione);
		String emailDestinatario = entityAzione.getEmail();
		String oggettoNotifica = "Esito procedimento";
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +
				"\n codice attività: " + entityAzione.getTipologiaRichiesta() +
				"\n codice utente: " + entityAzione.getCodiceUtente() +
				"\n codice utenza: " + entityAzione.getCodiceUtenza() +
		
		"\n la informiamo che la sua istanza è stata respinta. \n";
		testo += "Motivazione: ";
		if (entityAzione.getTestoRisposta() != null && !entityAzione.getTestoRisposta().equals(""))
		{
			testo += "\n" + entityAzione.getTestoRisposta();
		}
		testo += "\n\n" + FIRMA + "\n\n" + Constants.TESTO_NOREPLY ;
		
		if (emailDestinatario != null && !emailDestinatario.equals(""))
		{
			EmailRequest request = preparaNotifica(entityAzione, oggettoNotifica, testo, istanza.getIdIstanza());
	
	     	sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
		}
	}
	
	
	private EmailRequest preparaNotifica(TariAreraEntity entityAzione, 
			String oggettoNotifica, String testo, Long idIstanza) throws BusinessException {
		
		// 1. Recupera dati della notifica
		String emailDestinatario = entityAzione.getEmail();
		String cfDestinatario = entityAzione.getCodiceUtente();
		List<String> formIoFileNames = entityAzione.getFormIoFileNames();
	
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);

		request.setSubject(oggettoNotifica);	
		
		request.setText(testo);
		
		// salvo record in tabella notifica
		NotificaEntity notificaEntity = new NotificaEntity();
		notificaEntity.setIdIstanza(idIstanza);
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
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), idIstanza);
		}
		
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );
			
		request.setCc(String.join(";", entityAzione.getEmailCc()));
		//request.setBcc(String.join(";", entityAzione.getEmailCcn()));
		
		return request;
	}
}
