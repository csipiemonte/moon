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
public class Azione_TCR extends Azione_Default implements AzioneService {
	
	private final static String CLASS_NAME = "Azione_TCR";
	
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
	
	private final static String FIRMA =
			"\n\nDistinti saluti.\n\n" ;
	
	public Long generaRicevutaPdf
	(UserInfo user, String datiAzione, Istanza istanza, String tipoTemplateRicevuta, String datiProtocolloUscita) throws BusinessException {
		byte[] pdf = null;
		try {
			log.debug("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] BEGIN");
			log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] IN datiAzione="+datiAzione+"  idIstanza="+istanza.getIdIstanza());

			int tipoRisposta = 0;
			String numeroProtocolloUscita = "";
			String dataProtocolloUscita = "";
			String nomeDocumento = "";
			if(tipoTemplateRicevuta.equals("diniegoConProtocollo")) {
				tipoRisposta = 0;
				// leggo il protocollo in uscita
				numeroProtocolloUscita = ProtocolloUscitaHelper.parseNumeroProtocolloUscita(datiProtocolloUscita);
				dataProtocolloUscita = ProtocolloUscitaHelper.parseDataProtocolloUscita(datiProtocolloUscita);
				nomeDocumento = "Risposta_finale_diniego.pdf";
			}
			else if(tipoTemplateRicevuta.equals("accoglimentoConProtocollo")) {
				tipoRisposta = 1;
				// leggo il protocollo in uscita
				numeroProtocolloUscita = ProtocolloUscitaHelper.parse(datiProtocolloUscita);
				dataProtocolloUscita = ProtocolloUscitaHelper.parseDataProtocolloUscita(datiProtocolloUscita);
				nomeDocumento = "Risposta_finale_accoglimento.pdf";
			}
			else if(tipoTemplateRicevuta.equals("diniego")) {
				tipoRisposta = 0;
				nomeDocumento = "Risposta_diniego.pdf";
			}
			else if(tipoTemplateRicevuta.equals("accoglimento")) {
				tipoRisposta = 1;
				nomeDocumento = "Risposta_accoglimento.pdf";
			}
			
			if(tipoRisposta == 0) {
				
				RicevutaTcrDiniegoEntity ricevutaEntity = RpTcrOssDisHelper.parseAzioneDiniego(datiAzione);
				log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] DINIEGO ricevutaEntity="+ricevutaEntity);
				// genero il file pdf
				
				MoonprintDocumentoDiniego documentoRicevuta = null;
				documentoRicevuta = new PrintRicevutaMapper_WF_TCR_DINIEGO().remap(istanza, ricevutaEntity, numeroProtocolloUscita);
				
				switch(ricevutaEntity.getTipologiaTemplate()) {
					case "comunicazionePrecedente":
						documentoRicevuta.setTemplate("TCR_OSS_DINIEGO");
						break;
					case "osservazioniconMotivazione":
						documentoRicevuta.setTemplate("TCR_OSS_DINIEGO_MOT");
						break;
					case "discaricoconMotivazione":
						documentoRicevuta.setTemplate("TCR_DISC_DINIEGO_MOT");
						break;
					case "diniegoDomandaRimborsoconMotivazione":
						documentoRicevuta.setTemplate("TCR_DDR_DINIEGO_MOT");
						break;	
						
				}
				log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] DINIEGO documentoRicevuta="+documentoRicevuta);

				pdf = generaPdf(documentoRicevuta);
			}else if(tipoRisposta == 1) {
				RicevutaTcrAccoglimentoEntity ricevutaEntity = RpTcrOssDisHelper.parseAzioneAccoglimento(datiAzione);
				log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] ACCOGLIMENTO ricevutaEntity="+ricevutaEntity);
				// genero il file pdf
				MoonPrintDocumentoAccoglimento documentoRicevuta = null;
			    documentoRicevuta = new PrintRicevutaMapper_WF_TCR_ACCOGLIMENTO().remap(istanza, ricevutaEntity, numeroProtocolloUscita);
			    		    
			    
			    switch(ricevutaEntity.getTipologiaTemplate()) {
			    	case "conAnnullamentoParzialeDellAccertamento":
			    		documentoRicevuta.setTemplate("TCR_OSS_ACCOGLIMENTO_MOT");
			    		break;
			    	case "conAnnullamentoDellAccertamento":
			    		documentoRicevuta.setTemplate("TCR_OSS_ACCOGLIMENTO");
			    		break;
			    	case "parzialeConElencoDiMotivazioni":
			    		documentoRicevuta.setTemplate("TCR_DISC_ACCOGLIMENTO_MOT");
			    		break;
			    	case "totale":
			    		documentoRicevuta.setTemplate("TCR_DISC_ACCOGLIMENTO");
			    		break;
			    	case "perAvvenutoPagamento":
				    	documentoRicevuta.setTemplate("TCR_DISC_ACCOGLIMENTO_TOT_PAGATO");
				    	break;		
			    	case "accoglimentoDomandaRimborso":
				    	documentoRicevuta.setTemplate("TCR_DDR_ACCOGLIMENTO");
				    	break;		
				    		    	
			    }
			    
				log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] ACCOGLIMENTO documentoRicevuta="+documentoRicevuta);
				
				pdf = generaPdf(documentoRicevuta);
			}
			

			// salvo il file in moon_wf_t_storico_workflow
			log.info("[" + CLASS_NAME + "::generaSalvaRicevutaPdf] salvo il pfd della ricevuta in moon_wf_t_storico_workflow ...");
			RepositoryFileEntity file = new RepositoryFileEntity();
			file.setIdTipologia(DecodificaTipoRepositoryFile.BO_RICEVUTA.getId());
			file.setCodiceFile(UUID.randomUUID().toString()); // CodiceFile MOOn UUID usato per comunicazione a sistemi esterni (Protocolo)
			file.setNomeFile(nomeDocumento);			
			file.setContenuto(pdf);
			file.setIdIstanza(istanza.getIdIstanza());
			file.setDataCreazione(new Date());
			file.setDataProtocollo(!dataProtocolloUscita.isBlank()? new SimpleDateFormat("yyyyy-mm-dd").parse(dataProtocolloUscita.substring(0, 10)): null);
			file.setNumeroProtocollo(!numeroProtocolloUscita.isBlank()? numeroProtocolloUscita: null);
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
	public void diniego(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) {
		String tipoTemplateRicevuta = "diniego";
		log.info("[" + CLASS_NAME + "::diniego] Risposta di tipo diniego");
		Long idFile=generaRicevutaPdf(user, datiAzione, istanza, tipoTemplateRicevuta, null);
        storicoEntity.setIdFileRendering(idFile);	
	}
	
	@Override
	public void accoglimento(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) {		
				String tipoTemplateRicevuta = "accoglimento";
				log.info("[" + CLASS_NAME + "::accoglimento] Risposta di tipo accoglimento");
				Long idFile=generaRicevutaPdf(user, datiAzione, istanza, tipoTemplateRicevuta, null);
		        storicoEntity.setIdFileRendering(idFile);	
	}

	@Override
	public void creaRispostaConProtocollo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) {	
		
		/* cerco il passo in storicoworkflow dove sono stati salvati i dati della ricevuta senza protocollo
		 * 
		 */
		List<String> listAzioni = Arrays.asList("DINIEGO","ACCOGLIMENTO");
		String tipoTemplateRicevuta = "";
		
		StoricoWorkflowEntity storicoRicevuta = storicoWorkflowDAO.findLastStoricoListAzioni(istanza.getIdIstanza(),listAzioni);
		String datiAzioneStorico = storicoRicevuta.getDatiAzione();
		int idAzione = storicoRicevuta.getIdAzione().intValue();
		/*
		58	Accoglimento
		59	Diniego
		*/
		if (idAzione == 58) {
			tipoTemplateRicevuta = "accoglimentoConProtocollo";
		}
		else if (idAzione == 59) {
			tipoTemplateRicevuta = "diniegoConProtocollo";
		}
		/*
		 * Leggo i dati di protocollo salvati nell'ultimo passo eseguito dello storicoWorkflow
		 */
		StoricoWorkflowEntity storicoDatiProtocollo = storicoWorkflowDAO.findLastStorico(istanza.getIdIstanza())
				.orElseThrow(ItemNotFoundBusinessException::new);
		String datiProtocolloUscita = storicoDatiProtocollo.getDatiAzione();
		
		Long idFile=generaRicevutaPdf(user, datiAzioneStorico, istanza, tipoTemplateRicevuta, datiProtocolloUscita);
        storicoEntity.setIdFileRendering(idFile);	
				
	}
	
	@Override
	public void inviaComunicazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		log.debug("[" + CLASS_NAME + "::inviaComunicazione] BEGIN");
		log.info("[" + CLASS_NAME + "::inviaComunicazione] IN datiAzione="+datiAzione+"  idIstanza="+istanza.getIdIstanza());


		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = RinnovoConcessioniHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(false);

		String oggettoNotifica = "COMUNICAZIONE RISPOSTA";
		request.setSubject(oggettoNotifica);
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		String testoAggiuntivo = "";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testoAggiuntivo = notificaActionEntity.getTesto() +"\n\n";
		}

		String testo =
				"In riferimento alla domanda n." + istanza.getCodiceIstanza() +
				" del " + dataInvio + ", si allega il documento di esito. \n"

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
		StoricoWorkflowEntity storicoWorkwflow = storicoWorkflowDAO.findLastStoricoAzione(istanza.getIdIstanza(), DecodificaAzione.CREA_RISPOSTA_CON_PROTOCOLLO.getIdAzione());	
		
		log.debug("[" + CLASS_NAME + "::inviaComunicazione] idNotifica"+idNotifica);
		log.debug("[" + CLASS_NAME + "::inviaComunicazione] IN IdFileRendering ="+storicoWorkwflow.getIdFileRendering());
		
		notificaDAO.insertAllegatoNotifica(idNotifica, storicoWorkwflow.getIdFileRendering());
				
		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
//		attachment.setIdNotifica(idNotifica);		
		attachment.setIdFile(storicoWorkwflow.getIdFileRendering());
		request.setAttachment(attachment);		
		log.debug("[" + CLASS_NAME + "::inviaComunicazione] attachment "+JsonHelper.getJsonFromObject(attachment));

		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
	}

}
