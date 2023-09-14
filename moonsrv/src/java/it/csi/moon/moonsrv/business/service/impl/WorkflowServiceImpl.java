/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.script.ScriptEngine;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.StoricoWorkflow;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.AzioneEntity;
import it.csi.moon.commons.entity.ConditionEntity;
import it.csi.moon.commons.entity.DatiAzioneEntity;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.entity.WorkflowEntity;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.commons.mapper.WorkflowMapper;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.commons.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.commons.util.decodifica.DecodificaTipoModificaDati;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.WorkflowService;
import it.csi.moon.moonsrv.business.service.helper.DatiIstanzaHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.AzioneDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ConditionDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonsrv.business.service.impl.script.ScriptEngineManager;
import it.csi.moon.moonsrv.business.service.wf.AzioneService;
import it.csi.moon.moonsrv.business.service.wf.AzioneServiceFactory;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * @author Alberto
 * Layer di logica servizi che richiama i DAO
 */
@Component
public class WorkflowServiceImpl implements WorkflowService {
	
	private static final String CLASS_NAME = "WorkflowServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	StatoDAO statoDAO;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	WorkflowDAO workflowDAO;
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	ConditionDAO conditionDAO;
	@Autowired
	ScriptEngineManager scriptEngineManager;
	@Autowired
	AzioneDAO azioneDAO;
	
	@Override
	public Optional<StoricoWorkflow> getUltimoStepWorkflow(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getUltimoStepWorkflow] IN idIstanza: "+idIstanza);
			return storicoWorkflowDAO.findLastStorico(idIstanza)
					.map(WorkflowMapper::buildFromStoricoWorkflowEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getUltimoStepWorkflow] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca ultimo step workflow per id");
		} 
	}

	@Override
	public List<Workflow> getElencoAzioniPossibili(WorkflowFilter filter) throws BusinessException {
		List<Workflow> elencoAzioni = new ArrayList<>();
		try {
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			if (elencoWorkflow != null && elencoWorkflow.size() > 0) {
				for (WorkflowEntity  entity : elencoWorkflow) {

					elencoAzioni.add(WorkflowMapper.buildFromWorkflowEntity(entity));
				}
			}
			return elencoAzioni;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoAzioniPossibili] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero elenco azioni possibili");
 		}
	}

	@Override
	public List<StoricoWorkflow> getElencoStorico(Long idIstanza) throws BusinessException {
		List<StoricoWorkflow> listStorico = new ArrayList<>();
		try {
			List<StoricoWorkflowEntity> listStoricoEntity = storicoWorkflowDAO.findByIdIstanza(idIstanza);
			if (listStoricoEntity != null && listStoricoEntity.size() > 0) {
				for (StoricoWorkflowEntity  entity : listStoricoEntity) {
					listStorico.add(WorkflowMapper.buildFromStoricoWorkflowEntity(entity));
				}
			}
			return listStorico;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoAzioniPossibili] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero elenco azioni possibili");
 		}
	}

	/**
	 * azione DEVE contenere : idWorkflow, idIstanza, datiAzione OPT
	 */
	@Override
	@Transactional
	public Azione compieAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::compieAzione] IN istanza: "+idIstanza + " id workflow: " + azione.getIdWorkflow());
			Date now = new Date();
			Workflow workflow = getWorkflowById(azione.getIdWorkflow());
			// commento il controllo in quanto le azioni sono compiute dal sistema
			// userCanDoAction(user, idIstanza, workflow, false);
			
			/* un'azione comporta:
			 * chiusura del passo precedente
			 * inserimento di un record in moon_wf_t_storico_workflow
			 * possibile invio notifica
			 * possibile invio mail
			 * possibile cambio stato sul frontoffice
			 */
			StatoEntity statoPartenzaE = statoDAO.findById(workflow.getIdStatoWfPartenza());
			StatoEntity statoArrivoE = statoDAO.findById(workflow.getIdStatoWfArrivo());
			
			String descDestinatario = identificaDestinatario(workflow);
			
			StoricoWorkflowEntity storicoEntity = new StoricoWorkflowEntity();
			storicoEntity.setAttoreUpd(user.getIdentificativoUtente());
			storicoEntity.setIdIstanza(idIstanza);
			storicoEntity.setIdProcesso(workflow.getIdProcesso());
			storicoEntity.setIdStatoWfPartenza(workflow.getIdStatoWfPartenza());
			storicoEntity.setIdStatoWfArrivo(workflow.getIdStatoWfArrivo());
			storicoEntity.setIdAzione(workflow.getIdAzione());
			storicoEntity.setDataInizio(now);
			storicoEntity.setNomeStatoWfPartenza(statoPartenzaE.getNomeStatoWf());
			storicoEntity.setNomeStatoWfArrivo(statoArrivoE.getNomeStatoWf());
			storicoEntity.setNomeAzione(workflow.getNomeAzione());
			
			LOG.info("[" + CLASS_NAME + "::doAzione] dati Azione: " + azione.getDatiAzione());
			Istanza istanza = istanzeService.getIstanzaById(idIstanza);
			if (azione != null) {
				AzioneService azioneService = new AzioneServiceFactory().getService(istanza.getModulo().getCodiceModulo());
		
				if (azione.getDatiAzione()!=null && !azione.getDatiAzione().equals("")) {
					storicoEntity.setDatiAzione(azione.getDatiAzione());
					//				
					switch(workflow.getCodiceAzione()) {
						case "INSERISCI_PROTOCOLLO" :
							azioneService.inserisciProtocollo(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;	
						default:
							break;
					}
				} // END Azione con dati
				else {
					switch(workflow.getCodiceAzione()) {
						case "indicare_codice_azione":
							azioneService.azioneSenzaDati(user, azione, idIstanza);
							break;
						default:
							break;
					}
				}	
			}
			storicoEntity.setDescDestinatario(descDestinatario);

			// Chiusura passo precedente e inserimento del nuovo record in moon_wf_t_storico_workflow
			int numRecordAggiornati = storicoWorkflowDAO.updateDataFine(now, idIstanza);
			Long idStoricoWorkflow = storicoWorkflowDAO.insert(storicoEntity);
			storicoEntity.setIdStoricoWorkflow(idStoricoWorkflow);
				
			updateStatoIstanzaIfRequestedByWf(user, now, workflow, azione);
			// updateIdStoricoWorkflowOnRepositoryFileByWf(user, azione, workflow, istanza, storicoEntity);

			Azione azioneCompiuta = new Azione();
			azioneCompiuta.setDescDestinatario(descDestinatario);
			azioneCompiuta.setNomeAzione(workflow.getNomeAzione());
			azioneCompiuta.setCodEsitoAzione("ok");
			return azioneCompiuta;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore inserimento nuovo step workflow");
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] BusinessException " + e.getMessage());
			throw new BusinessException();
		}
	}

	@Override
	public Workflow getWorkflowById(Long idWorkflow) throws BusinessException {
		try {
			WorkflowEntity entity = workflowDAO.findById(idWorkflow);
			Workflow workflow = WorkflowMapper.buildFromWorkflowEntity(entity);
			return workflow;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getWorkflowById] DAOException ", e);
			throw new BusinessException("Errore recupero workflow con id " + idWorkflow);
 		}
	}
	
	@Override
	public Workflow getWorkflowByFilter(WorkflowFilter filter) throws BusinessException {
		Workflow workflow = new Workflow();
		try {
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			if (elencoWorkflow != null) {
				if (elencoWorkflow.size() == 1) {
					workflow = WorkflowMapper.buildFromWorkflowEntity(elencoWorkflow.get(0));
				}
				if (elencoWorkflow.size() > 1) {
					throw new BusinessException("Errore getWorkflowByFilter trovati piu' di un record");
				}
			}
			return workflow;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getWorkflowByFilter] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore getWorkflowByFilter");
 		}
	}
	
	@Override
	public DatiAzione getStrutturaByIdWorkflow(Long idWorkflow) throws BusinessException {
		try {
			WorkflowEntity wf = workflowDAO.findById(idWorkflow);
			DatiAzioneEntity datiAzioneEntity = workflowDAO.findDatiAzioneById(wf.getIdDatiazione());
			DatiAzione datiAzione = WorkflowMapper.buildFromDatiAzioneEntity(datiAzioneEntity);
			return datiAzione;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] Errore invocazione DAO - ",notFoundEx );
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore ricerca modulo per id");
		}
	}
	
	private void userCanDoAction(UserInfo user, Long idIstanza, Workflow workflow, boolean isDaCancellare) throws BusinessException, DAOException {
		boolean validate = false;
		
		LOG.debug("[" + CLASS_NAME + "::userCanDoAction] user: "+user.getIdentificativoUtente() + 
				"  istanza: "+idIstanza+"  azione: " + workflow.getNomeAzione() );

		
		/* TODO 
		 * verificare se l'utente e' abilitato ad agire sull'istanza o sul processo
		 * verificare se l'azione richiesta e' prevista 
		 * verificare se sono presenti i dati richiesti dall'azione
		 */
		//user.getCodFisc().equals(entity.getCodiceFiscaleDichiarante());
		
		if (isDaCancellare) {
			// verifico se l'azione da cancellare e' annullabile
			if (Boolean.TRUE.equals(workflow.getIsAnnullabile())) {
				validate = true;
			}	
		} else {
			// verifico se l'azione richiesta e' prevista per lo stato corrente
			StoricoWorkflowEntity lastStoricoWf = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdProcesso(lastStoricoWf.getIdProcesso());
			filter.setIdStatoWfPartenza(lastStoricoWf.getIdStatoWfArrivo());
	
			List<Workflow> elenco = getElencoAzioniPossibili(filter);
			for (Workflow  step : elenco) {
				if (step.getIdAzione().intValue() == workflow.getIdAzione().intValue()) {
					validate = true;
				}
			}
		}

		if(!validate) {
			throw new BusinessException("Azione non possibile");
		}
	}

	private String identificaDestinatario(Workflow workflow) throws BusinessException {
		String descDestinatario = "";
		LOG.debug("[" + CLASS_NAME + "::identificaDestinatario] id utente destinatario: "+ workflow.getIdUtenteDestinatario() + 
				"  tipo utente destinatario: "+ workflow.getIdTipoUtenteDestinatario() +
				"  gruppo utenti destinatari: " + workflow.getIdGruppoUtentiDestinatari() );

		if (workflow.getIdUtenteDestinatario() != null) {
			// TODO acquisire nome e cognome dell'utente destinatario
			descDestinatario = "nome e cognome utente destinatario";
		}
		else if (workflow.getIdTipoUtenteDestinatario() != null) {
			// TODO acquisire il nome della tipologia di utente destinatario
			descDestinatario = "tipologia destinatario";
		}
		else if (workflow.getIdGruppoUtentiDestinatari() != null) {
			// TODO acquisire il nome del gruppo di utenti destinatari
			descDestinatario = "nome gruppo destinatari";
		}
		
		return descDestinatario;
	}

	@Override
	@Transactional
	public Azione annullaAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::annullaAzione] IN istanza: "+idIstanza + " id workflow: " + azione.getIdWorkflow());

			Date now = new Date();

			Workflow workflow = getWorkflowById(azione.getIdWorkflow());
			boolean isDaCancellare = true;
			userCanDoAction(user, idIstanza, workflow, isDaCancellare);
			
			StoricoWorkflowEntity storico = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			if (!storico.getIdProcesso().equals(workflow.getIdProcesso()) ||
				!storico.getIdAzione().equals(workflow.getIdAzione()) ||
				!storico.getIdStatoWfPartenza().equals(workflow.getIdStatoWfPartenza()) ||
				!storico.getIdStatoWfArrivo().equals(workflow.getIdStatoWfArrivo()))
			{
				LOG.info("[" + CLASS_NAME + "::annullaAzione] verifica azione da annullare fallita: " + 
							" ultimo workflow:" + storico.getIdStoricoWorkflow().intValue() +
							" workflow da annullare: " + azione.getIdWorkflow());
				throw new BusinessException("Errore su annullaAzione: annullamento azione non consentito");
			}
			
			/* un'azione comporta:
			 * chiusura del passo precedente
			 * inserimento di un record in moon_wf_t_storico_workflow
			 * possibile invio notifica
			 * possibile invio mail
			 * possibile cambio stato sul frontoffice
			 */
			
			// chiusura passo precedente
			int numRecordAggiornati = storicoWorkflowDAO.updateDataFine(now, idIstanza);
			
			// inserimento di un record in moon_wf_t_storico_workflow
			
			StatoEntity statoPartenzaE = statoDAO.findById(storico.getIdStatoWfArrivo());
			StatoEntity statoArrivoE = statoDAO.findById(storico.getIdStatoWfPartenza());
			
			String descDestinatario = storico.getDescDestinatario();
			String nomeAzione = "Annulla " + storico.getNomeAzione().toLowerCase();
			Long idAzione = DecodificaAzione.ANNULLA.getIdAzione();
					
			StoricoWorkflowEntity storicoEntity = new StoricoWorkflowEntity() ;
			storicoEntity.setAttoreUpd(user.getIdentificativoUtente());
			storicoEntity.setIdIstanza(idIstanza);
			storicoEntity.setIdProcesso(storico.getIdProcesso());
			
			storicoEntity.setIdStatoWfPartenza(storico.getIdStatoWfArrivo());
			storicoEntity.setIdStatoWfArrivo(storico.getIdStatoWfPartenza());
			storicoEntity.setDataInizio(now);
			storicoEntity.setNomeStatoWfPartenza(statoPartenzaE.getNomeStatoWf());
			storicoEntity.setNomeStatoWfArrivo(statoArrivoE.getNomeStatoWf());
			storicoEntity.setNomeAzione(nomeAzione);
			storicoEntity.setIdAzione(idAzione);
			
			if (azione != null && azione.getDatiAzione()!= null && !azione.getDatiAzione().equals("")) {
				LOG.info("[" + CLASS_NAME + "::annullaAzione] dati Azione: " + azione.getDatiAzione());
				storicoEntity.setDatiAzione(azione.getDatiAzione());
			}
			storicoEntity.setDescDestinatario(descDestinatario);
			
			Long idStoricoWorkflow = storicoWorkflowDAO.insert(storicoEntity);
			
			Azione azioneCompiuta = new Azione();
			azioneCompiuta.setDescDestinatario(descDestinatario);
			azioneCompiuta.setNomeAzione(workflow.getNomeAzione());
			azioneCompiuta.setCodEsitoAzione("ok");
			return azioneCompiuta;
		} catch (DAOException e) {
			// in caso di errore rimetto a null l'ultimo step
			try {
				storicoWorkflowDAO.setDataFineNull(idIstanza);
			} catch (DAOException e1) {
				throw new BusinessException("Errore su annullaAzione");
			}
			LOG.error("[" + CLASS_NAME + "::annullaAzione] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore su annullaAzione");
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::annullaAzione] ", e);
			try {
				storicoWorkflowDAO.setDataFineNull(idIstanza);
			} catch (DAOException e1) {
				throw new BusinessException("Errore su annullaAzione");
			}
			throw new BusinessException("Errore su annullaAzione");
		}
	}
	
	@Override
	public StoricoWorkflow getStoricoWorkflowById(Long idStoricoWorkflow) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getStoricoWorkflowById] idStoricoWorkflow: " + idStoricoWorkflow);
			StoricoWorkflowEntity storico = storicoWorkflowDAO.findStoricoWorkflowById(idStoricoWorkflow);
			StoricoWorkflow storicoWorkflow = WorkflowMapper.buildFromStoricoWorkflowEntity(storico);
			return storicoWorkflow;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getStoricoWorkflowById] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero storicoworkflow con id " + idStoricoWorkflow);
 		}
	}
	
	private void updateStatoIstanzaIfRequestedByWf(UserInfo user, Date now, Workflow workflow, Azione azione) throws Exception {
		WorkflowEntity entity = workflowDAO.findById(azione.getIdWorkflow());
		boolean riportaInBozza = DecodificaStatoIstanza.BOZZA.getIdStatoWf().equals(workflow.getIdStatoWfArrivo());
		if ("S".equals(entity.getFlagStatoIstanza()) || riportaInBozza) 
		{
			if (riportaInBozza) {
				IstanzaEntity eIstanza = istanzaDAO.findById(azione.getIdIstanza());
				eIstanza.setAttoreUpd(user.getIdentificativoUtente());
				eIstanza.setIdStatoWf(workflow.getIdStatoWfArrivo());
				eIstanza.setCurrentStep(0);
				istanzaDAO.update(eIstanza);
			} else {
				istanzaDAO.updateStato(azione.getIdIstanza(), workflow.getIdStatoWfArrivo());
			}

			// CRON
			IstanzaCronologiaStatiEntity istanzaECronologia = null;
			// last CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(azione.getIdIstanza());
			lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
			lastIstanzaCronologia.setDataFine(now);
			istanzaDAO.updateCronologia(lastIstanzaCronologia);
			// new CRON
			istanzaECronologia = new IstanzaCronologiaStatiEntity();
			istanzaECronologia.setIdIstanza(azione.getIdIstanza());
			istanzaECronologia.setIdStatoWf(workflow.getIdStatoWfArrivo());
			istanzaECronologia.setAttoreIns(user.getIdentificativoUtente());
			istanzaECronologia.setDataInizio(now);
			istanzaECronologia.setIdAzioneSvolta(workflow.getIdAzione());
			Long idCronologiaStati = istanzaDAO.insertCronologia(istanzaECronologia);
			istanzaECronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI
			IstanzaDatiEntity istanzaEDati = null;
			istanzaEDati = istanzaDAO.findDati(azione.getIdIstanza(), lastIstanzaCronologia.getIdCronologiaStati());
			istanzaEDati.setDataUpd(now);
			if (riportaInBozza) {
				DatiIstanzaHelper datiIstanzaHelper = new DatiIstanzaHelper();
				istanzaEDati.setDatiIstanza(datiIstanzaHelper.updateSubmitFalse(istanzaEDati.getDatiIstanza())); // RIPORTA IN BAZZA
			} else {
				istanzaEDati.setDatiIstanza(istanzaEDati.getDatiIstanza());
			}
			istanzaEDati.setIdCronologiaStati(istanzaECronologia.getIdCronologiaStati());
			istanzaEDati.setIdStepCompilazione(null);
			istanzaEDati.setIdTipoModifica(DecodificaTipoModificaDati.NON.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(istanzaEDati);
			istanzaEDati.setIdDatiIstanza(idIstanzaDati);
		}
					
	}
	
	@Transactional
	public void compieAzioneAutomaticaIfPresente(Long idIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::compieAzioneAutomaticaIfPresente] idIstanza: " + idIstanza);
			IstanzaEntity istanzaE = istanzaDAO.findById(idIstanza);
			WorkflowFilter filter = WorkflowFilter.builder()
				.idModulo(istanzaE.getIdModulo())
				.idStatoWfPartenza(istanzaE.getIdStatoWf())
				.isAutomatica(Boolean.TRUE)
				.escludiAzioniDiSistema(Boolean.FALSE)
				.build();
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			if (elencoWorkflow != null && elencoWorkflow.size() == 1) { // Solo se presente una sola azione automatica
				WorkflowEntity wfE = elencoWorkflow.get(0);
				LOG.info("[" + CLASS_NAME + "::compieAzioneAutomaticaIfPresente] workflow: " + wfE);
				if (validaCondition(wfE, idIstanza)) {
					LOG.info("[" + CLASS_NAME + "::compieAzioneAutomaticaIfPresente] condition valide ...");
					eseguiCompieAzioneIstanza(wfE, istanzaE.getIdIstanza());
					eseguiSendEmailOnINVIO(wfE, istanzaE.getIdIstanza());
				} else {
					LOG.info("[" + CLASS_NAME + "::compieAzioneAutomaticaIfPresente] condition NOT VALIDE for idIstanza = " + idIstanza);
				}
			} else {
				LOG.info("[" + CLASS_NAME + "::compieAzioneAutomaticaIfPresente] Nessun WF automatico da eseguire per idIstanza=" + idIstanza);
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::compieAzioneAutomaticaIfPresente] Exception ", e);
			throw new BusinessException("Errore generica compieAzioneAutomaticaIfPresente");
		}
	}
	
	private void eseguiSendEmailOnINVIO(WorkflowEntity wfE, Long idIstanza) {
		try {
			if (DecodificaAzione.INVIA.isCorrectAzione(wfE)) {
				istanzeService.inviaEmail(idIstanza);
			}
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::eseguiSendEmailOnINVIO] EMAIL_NOT_SEND for idIstanza: " + idIstanza);
		}
	}

	private void eseguiCompieAzioneIstanza(WorkflowEntity wfE, Long idIstanza) {
		LOG.info("[" + CLASS_NAME + "::eseguiCompieAzioneIstanza] BEGIN");
		UserInfo userSistema = new UserInfo();
		userSistema.setIdentificativoUtente("SISTEMA");
		Azione azione = new Azione();
		azione.setIdAzione(wfE.getIdAzione());
		azione.setIdIstanza(idIstanza);
		azione.setIdWorkflow(wfE.getIdWorkflow());
		compieAzione(userSistema, idIstanza, azione);
	}

	private boolean validaCondition(WorkflowEntity entity, Long idIstanza) {
		try {
			if (entity.getIdCondition() == null) {
				return true;
			}
			ConditionEntity condition = conditionDAO.findById(entity.getIdCondition());
			ScriptEngine engine = scriptEngineManager.getEngine(condition.getCodiceEngine(), idIstanza);
		    engine.eval(condition.getScript());
		    Boolean result = (Boolean) engine.get("result");
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::validaCondition] Exception ", e);
			return false;
		}
	}
	

	@Override
	public Azione verificaAzionePossibile(Istanza istanza, String codiceAzione) throws BusinessException {
		try {
			LOG.info("[" + CLASS_NAME + "::verificaAzionePossibile] istanza: " + istanza.getIdIstanza() + " - " + istanza.getCodiceIstanza() + " - " + codiceAzione);
			//
			// Azione :
			AzioneEntity azioneE = azioneDAO.findByCd(codiceAzione);
			StoricoWorkflowEntity lastStoricoWf = storicoWorkflowDAO.findLastStorico(istanza.getIdIstanza())
					.orElseThrow(ItemNotFoundBusinessException::new);
			
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdModulo(istanza.getModulo().getIdModulo());
			filter.setIdAzione(azioneE.getIdAzione());
			filter.setIdStatoWfPartenza(lastStoricoWf.getIdStatoWfArrivo());
//			filter.setFlagApi("S");
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			if (elencoWorkflow.isEmpty()) {
				LOG.error("[" + CLASS_NAME + "::verificaAzionePossibile] " + codiceAzione + " on " + istanza.getIdIstanza() + " Nessun Workflow trovato per filter=" + filter);
				throw new BusinessException("Azione non eseguibile","MOONSRV-00910");
			} else if (elencoWorkflow.size() > 1) {
				LOG.error("[" + CLASS_NAME + "::verificaAzionePossibile] " + codiceAzione + " on " + istanza.getIdIstanza() + " Troppi Workflow trovati per filter=" + filter);
				throw new BusinessException("Errore configurazione workflow", "MOONSRV-00911");
			}
			WorkflowEntity workflow = elencoWorkflow.get(0);
			
			Azione result = new Azione();
			result.setIdIstanza(istanza.getIdIstanza());
			result.setIdWorkflow(workflow.getIdWorkflow());
			result.setCodiceAzione(codiceAzione);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::verificaAzionePossibile] Exception " + codiceAzione + " on " + istanza.getIdIstanza(), e);
			throw new BusinessException();
		}
	}
	
}

