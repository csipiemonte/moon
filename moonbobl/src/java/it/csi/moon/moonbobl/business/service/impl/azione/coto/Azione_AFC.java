/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione.coto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.NotificaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.ComunicazioneHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaBoHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.RinnovoConcessioniHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.WfProtocolloEdil;
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
public class Azione_AFC extends Azione_Default implements AzioneService {

	private final static String CLASS_NAME = "Azione_AFC";
	protected Logger log = LoggerAccessor.getLoggerBusiness();

	private final static String FIRMA =
		"Cogliamo l'occasione per porgere distinti saluti.\n" + 
		"Comune di Torino – Ufficio Elettorale.\n";

			
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

	public Azione_AFC() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}


	@Override
	public void inserisciProtocollo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {

		DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
		String numeroProtocollo = "";
		String strDataProtocollo = "";
		Date dataProtocollo = null;
		try {
			WfProtocolloEdil getDatiProtocollo = datiIstanzaBoHelper.getProtocolloEdil(datiAzione);
			if (getDatiProtocollo != null) 
			{
				numeroProtocollo = getDatiProtocollo.getNumeroProtocollo();
				strDataProtocollo = getDatiProtocollo.getDataProtocollo().substring(0, 10);
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
		
		// 1. Recupera dati per invio mail
		DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
		datiIstanzaHelper.initDataNode(istanza);
		String emailDestinatario = datiIstanzaHelper.extractedTextValueFromDataNodeByKey("contatti.email");
		
		String cfDestinatario = istanza.getCodiceFiscaleDichiarante();

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(false);

		String oggettoNotifica = "Comunicazione AFC Torino";
		request.setSubject(oggettoNotifica);
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());

		String testo = "Buongiorno,\n" + 
				"si comunica che la domanda di partecipazione alla Selezione Pubblica da parte dei Servizi Cimiteriali della Città di Torino, "
				+ "presentata in data " + dataInvio 
				+ " codice presentazione " + istanza.getCodiceIstanza()
				+ ", è stata protocollata con il numero " + numeroProtocollo +" del " + strDataProtocollo +".\n\n" 
				+  FIRMA;

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
