/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione.coto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.LogEmailEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.NotificaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.CambioResidenzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.ComunicazioneHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.RinnovoConcessioniHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.dto.moonfobl.EmailAttachment;
import it.csi.moon.moonbobl.dto.moonfobl.EmailRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
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
public class Azione_ELET_RINUNCIA_SCRUTATORI extends Azione_Default implements AzioneService {

	private final static String CLASS_NAME = "Azione_ELET_SCRUTATORI";
	protected Logger log = LoggerAccessor.getLoggerBusiness();

	private final static String FIRMA =
			"Cogliamo l'occasione per porgere distinti saluti.\n" + 
			"Comune di Torino – Ufficio Elettorale.\n";

	private final static Boolean flagPec = false;
			
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

	public Azione_ELET_RINUNCIA_SCRUTATORI() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}


	@Override
	public void approva(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {

		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = RinnovoConcessioniHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(flagPec);

		String oggettoNotifica = "Accoglimento cancellazione da Albo scrutatori";
		request.setSubject(oggettoNotifica);
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		String testoAggiuntivo = "";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testoAggiuntivo = notificaActionEntity.getTesto() +"\n\n";
		}

		String testo =	"La Sua domanda atta alla cancellazione dall'Albo delle persone idonee alla funzione "
				+ "di SCRUTATORE di SEGGIO ELETTORALE è regolarmente pervenuta. \n"
				+ "La cancellazione dall'Albo decorrerà a partire dal mese di gennaio dell'anno successivo "
				+ "alla presentazione della domanda, se inviata entro il mese di dicembre. \n\n"
				+ testoAggiuntivo + FIRMA;

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
	public void respingiConComunicazione(UserInfo user, String datiAzione, Istanza istanza,
			StoricoWorkflowEntity storicoEntity) throws BusinessException {

		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = ComunicazioneHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(flagPec);
		
		String oggettoNotifica = "Cancellazione da Albo scrutatori – ARCHIVIAZIONE";
		request.setSubject(oggettoNotifica);
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		String testoAggiuntivo = "";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testoAggiuntivo = notificaActionEntity.getTesto() +"\n\n";
		}
			
		String testo =
				"Si comunica che, in base a quanto emerso dall'attività istruttoria, la domanda n." + istanza.getCodiceIstanza() +
				" del " + dataInvio + " viene respinta per la seguente motivazione: \n"

				+ testoAggiuntivo + FIRMA;

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

}
