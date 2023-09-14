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
import it.csi.moon.moonbobl.business.service.impl.helper.GeneralHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.TariAreraHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.TribMixHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.TributiGeneralEntity;
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
 * @author laurent
 *
 */
public class Azione_TRIB_COSAP extends Azione_Default implements AzioneService {
	
	private final static String CLASS_NAME = "Azione_TRIB_COSAP";
	protected Logger log = LoggerAccessor.getLoggerBusiness();
	private final static String FIRMA =
			"\n Distinti saluti.";
	
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
	
	public Azione_TRIB_COSAP() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	
	@Override
	public void approva(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = GeneralHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		
		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(false);
		
		if (notificaActionEntity.getEmailCc() != null && notificaActionEntity.getEmailCc().size() > 0)
		{
			request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
		}
		String oggettoNotifica = "Accettazione istanza";
		request.setSubject(oggettoNotifica);	
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +", la informiamo che è stata "
				+ "presa in carico dall'ufficio Suolo Pubblico che invierà a breve la ricevuta di presentazione e l'avviso di pagamento. \n";			
		
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\nUlteriore comunicazione da parte dell'Ufficio:\n" + notificaActionEntity.getTesto();
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
	
	@Override
	public void richiediIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		TributiGeneralEntity entityAzione = TribMixHelper.parseAzione(datiAzione);
		String emailDestinatario = entityAzione.getEmail();
		String oggettoNotifica = "Richiesta di integrazione";
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza()
				+ " la informiamo che le è stata inviata, da parte dell'ufficio competente, una richiesta di integrazione "
				+ "riportata in calce alla presente.\n"
				+ "Può PRESENTARE LA DOCUMENTAZIONE ad integrazione accedendo "
				 + "nuovamente al sistema di consultazione delle istanze inviate, "
				 + "nella sezione \"Le mie istanze\" - \"In attesa di integrazione\".\n"
				 + "Dopo aver selezionato la propria istanza e cliccato sul pulsante verde \"Esegui integrazione\" "
				 + "sarà possibile allegare la documentazione richiesta";
				
		testo += "\n\n" + Constants.TESTO_NOREPLY + FIRMA;
		
		if (emailDestinatario != null && !emailDestinatario.equals(""))
		{
			EmailRequest request = preparaNotifica(entityAzione, oggettoNotifica, testo, istanza.getIdIstanza());
	
	     	sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RICHIESTA_INTEGRAZIONE);
		}
				
	}
		
	@Override
	public void respingi(UserInfo user, String datiAzione, Istanza istanza,
			StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		TributiGeneralEntity entityAzione = TribMixHelper.parseAzione(datiAzione);
		String emailDestinatario = entityAzione.getEmail();
		String oggettoNotifica = "Esito procedimento";
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +
		
		"\n la informiamo che la sua istanza è stata respinta. \n";
		if (entityAzione.getTestoRisposta() != null && !entityAzione.getTestoRisposta().equals(""))
		{
			testo += "Motivazione: ";
			testo += "\n" + entityAzione.getTestoRisposta();
		}
		testo += "\n\n" + Constants.TESTO_NOREPLY + FIRMA;
		
		if (emailDestinatario != null && !emailDestinatario.equals(""))
		{
			EmailRequest request = preparaNotifica(entityAzione, oggettoNotifica, testo, istanza.getIdIstanza());
	
	     	sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RESPINGI_EMAIL);
		}
	}
	
	@Override
	public void accogli(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		TributiGeneralEntity entityAzione = TribMixHelper.parseAzione(datiAzione);
		String emailDestinatario = entityAzione.getEmail();
		String oggettoNotifica = "Esito procedimento";
		
		String testo = "Con riferimento alla sua istanza n."+ istanza.getCodiceIstanza() +
				
				"\n la informiamo che l'ufficio preposto ha concluso il procedimento con esito positivo.\n";			
		
		if (entityAzione.getTestoRisposta() != null && !entityAzione.getTestoRisposta().equals(""))
		{
			testo += "\n\nComunicazione:\n" + entityAzione.getTestoRisposta()+ FIRMA;
		}
		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
		if (emailDestinatario != null && !emailDestinatario.equals(""))
		{
			EmailRequest request = preparaNotifica(entityAzione, oggettoNotifica, testo, istanza.getIdIstanza());
	
	     	sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RESPINGI_EMAIL);
		}
	}
	
	private EmailRequest preparaNotifica(TributiGeneralEntity entityAzione, 
			String oggettoNotifica, String testo, Long idIstanza) throws BusinessException {
		
		// 1. Recupera dati della notifica
		String emailDestinatario = entityAzione.getEmail();
		String cfDestinatario = entityAzione.getCodiceFiscale();
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
					
		return request;
	}
}
