/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione.regp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.moon.moonbobl.business.service.impl.azione.AzioneService;
import it.csi.moon.moonbobl.business.service.impl.azione.Azione_Default;
import it.csi.moon.moonbobl.business.service.impl.dao.AzioneDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.NotificaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.GeneralHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.ProtocolloUscitaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.RinnovoConcessioniHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.RpTcrOssDisHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RicevutaTcrAccoglimentoEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RicevutaTcrDiniegoEntity;
import it.csi.moon.moonbobl.business.service.mapper.moonprint.PrintRicevutaMapper_WF_TCR_ACCOGLIMENTO;
import it.csi.moon.moonbobl.business.service.mapper.moonprint.PrintRicevutaMapper_WF_TCR_DINIEGO;
import it.csi.moon.moonbobl.dto.moonfobl.EmailAttachment;
import it.csi.moon.moonbobl.dto.moonfobl.EmailRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonprint.MoonPrintDocumentoAccoglimento;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoDiniego;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.JsonHelper;
import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoLogEmail;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoRepositoryFile;

/**
 * 
 * 
 *
 */
public class Azione_RP_FINE_CONC extends Azione_Default implements AzioneService {
	
	private final static String CLASS_NAME = "Azione_RP_FINE_CONC";
	
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	NotificaDAO notificaDAO;
	@Autowired
	AzioneDAO azioneDAO;
	@Autowired
	LogEmailDAO logEmailDAO;
		
	@Override
	public void inviaIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
			
		// 1. Recupera mail dell'ufficio destinatario
		String emailDestinatario = "tutela.acque@regione.piemonte.it";
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
				
		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
	}

}
