/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione.coto;

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
import it.csi.moon.moonbobl.business.service.impl.helper.CambioResidenzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.dem.RicevutaCambioResidEntity;
import it.csi.moon.moonbobl.business.service.mapper.moonprint.PrintRicevutaMapper_RIC_DEM_001;
import it.csi.moon.moonbobl.dto.moonfobl.EmailAttachment;
import it.csi.moon.moonbobl.dto.moonfobl.EmailRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoRicevuta;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoLogEmail;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoRepositoryFile;

/**
 * Azione_DEM_001 - Azioni per COTO : SERVICE DEMOGRAFICI
 *  Serve :
 *  - sia per Cambio Indirizzo DEM_001
 *  - sia per Cambio Residenza DEM_CAMBIO_RES
 * 
 * @author laurent
 *
 */
public class Azione_DEM_001 extends Azione_Default implements AzioneService {
	
	private final static String CLASS_NAME = "Azione_DEM_001";
	
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
	public Long generaSalvaRicevutaPdf(UserInfo user, String datiAzione, Istanza istanza) throws BusinessException {
//		CambioResidenzaHelper cambioResidenzaHelper = new CambioResidenzaHelper();
		try {
			log.debug("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] BEGIN");
			log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] IN datiAzione="+datiAzione+"  idIstanza="+istanza.getIdIstanza());
			//CambioResidenzaEntity cambioResidenzaEntity = CambioResidenzaHelper.parse(istanza.getData().toString()); // datiE.getDatiIstanza());
			RicevutaCambioResidEntity ricevutaEntity = CambioResidenzaHelper.parseAzione(datiAzione);
			log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] ricevutaEntity="+ricevutaEntity);
			// genero il file pdf
			MoonprintDocumentoRicevuta documentoRicevuta = null;
			documentoRicevuta = new PrintRicevutaMapper_RIC_DEM_001().remap(istanza, ricevutaEntity);
			documentoRicevuta.setTemplate("RicevutaCambioIndirizzo");
			log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] documentoRicevuta="+documentoRicevuta);
			
			byte[] pdf = generaPdf(documentoRicevuta);
			// salvo il file in moon_wf_t_storico_workflow
			log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] salvo il pfd della ricevuta in moon_wf_t_storico_workflow ...");
			RepositoryFileEntity file = new RepositoryFileEntity();
			file.setIdTipologia(DecodificaTipoRepositoryFile.BO_RICEVUTA.getId());
			file.setCodiceFile(UUID.randomUUID().toString()); // CodiceFile MOOn UUID usato per comunicazione a sistemi esterni (Protocolo)
			file.setNomeFile("RicevutaAccettazione.pdf");
			file.setContenuto(pdf);
			file.setIdIstanza(istanza.getIdIstanza());
			file.setDataCreazione(new Date());
			file.setNumeroProtocollo("");
			Long idFile = repositoryFileDAO.insert(file);
			return idFile;
		} catch (DAOException e) {
			log.warn("[" + CLASS_NAME + "::generaRicevutaPdf] DAOException "+e.getMessage(), e);
			throw new BusinessException();
		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::generaRicevutaPdf] Exception "+e.getMessage(), e);
			throw new BusinessException();
		}
	}

	@Override
	public void inviaRicevuta(UserInfo user, String datiAzione, Istanza istanza) throws BusinessException {
		// 1. Recuperare idFile della Ricevuta dall'ultimo storicoWorkflow dell'azione GENERA_RICEVUTA dell'idIstanza
		azioneDAO.initCache();
		StoricoWorkflowEntity sw = storicoWorkflowDAO.findLastStoricoAzione(istanza.getIdIstanza(), azioneDAO.findId(DecodificaAzione.GENERA_RICEVUTA));
		Long idFileRicevuta = sw.getIdFileRendering();
				
		// (2.) Recuperare il PDF magari questo verra fatto da moonsrv e passiamo idFile nella request email.
		// NON QUI : SI FA SU moonSRV Email
		
		// 3. Recuperare il destinatario della notifica
		NotificaActionEntity notificaActionEntity = CambioResidenzaHelper.parseAzioneNotifica(datiAzione);
		
		// 4. Invio mail
		EmailRequest request = new EmailRequest();
		request.setTo(notificaActionEntity.getEmail());
		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
		
		String oggettoMail = "Ricevuta di attestazione cambio indirizzo";
		request.setSubject(oggettoMail);	
		
		String testo = "Con riferimento alla sua istanza di richiesta di cambio indirizzo si allega copia della ricevuta di attestazione di cambio indirizzo.";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testo += "\n\n" + notificaActionEntity.getTesto();
		}
		testo += "\n\n" + Constants.TESTO_NOREPLY;
		
		request.setText(testo);
		
		// imposto il file con la ricevuta
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdFile(idFileRicevuta);
		
		
		// salvo record in tabella notifica
		NotificaEntity notificaEntity = new NotificaEntity();
		notificaEntity.setIdIstanza(istanza.getIdIstanza());
		notificaEntity.setCfDestinatario(notificaActionEntity.getCodiceFiscale());
		notificaEntity.setEmailDestinatario(notificaActionEntity.getEmail());
		notificaEntity.setOggettoNotifica(oggettoMail);
		notificaEntity.setTestoNotifica(testo);
		notificaEntity.setFlagLetta("N");
		notificaEntity.setFlagArchiviata("N");
		Long idNotifica = notificaDAO.insert(notificaEntity);
		
		// creo legame tra notifica e allegati
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}
		
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		attachment.setIdNotifica(idNotifica);
		
		request.setAttachment(attachment );
		sendLogEmail(istanza, notificaActionEntity.getEmail(), request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
	}

}
