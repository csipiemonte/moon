/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.StoricoWorkflow;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.DatiAzioneEntity;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.ProcessoEntity;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.entity.WorkflowEntity;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.commons.mapper.WorkflowMapper;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.commons.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.commons.util.decodifica.DecodificaTipoModificaDati;
import it.csi.moon.moonfobl.business.service.WorkflowService;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ProcessoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiAzioneHelper;
import it.csi.moon.moonfobl.business.service.impl.helper.dto.IntegrazioneActionEntity;
import it.csi.moon.moonfobl.business.service.impl.initializer.IntegrazioneInitializer;
import it.csi.moon.moonfobl.business.service.impl.initializer.IstanzaInitializer;
import it.csi.moon.moonfobl.business.service.impl.processo.ProcessoServiceDelegate;
import it.csi.moon.moonfobl.business.service.impl.processo.ProcessoServiceDelegateFactory;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * @author Alberto
 * @author Danilo
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
	ProcessoDAO processoDAO;
	@Autowired
	IstanzaInitializer istanzaInitializer;
	@Autowired
	IntegrazioneInitializer integrazioneInitializer;
	@Autowired
	NotificaDAO notificaDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	LogEmailDAO logEmailDAO;
	
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
	public List<Workflow> getElencoAzioniPossibili(UserInfo user, Long idIstanza, WorkflowFilter filter) throws BusinessException {
		List<Workflow> elencoAzioni = new ArrayList<>();
		boolean esisteAzioneAutomatica = false;
		try {
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			LOG.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] filtro workflow : "+filter);
			LOG.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] numero azioni possibili : "+elencoWorkflow.size());
			if (elencoWorkflow != null && elencoWorkflow.size() > 0) {
				for (WorkflowEntity  entity : elencoWorkflow) {
					// check azione automatica
					LOG.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] flag stato automatico : "+entity.getFlagAutomatico());
					LOG.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] stato di partenza : "+entity.getIdStatoWfPartenza().intValue());
					if (isAzioneWorkflowDaEseguireInAutomatico(entity)) {
						LOG.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] IN user: "+user.getIdentificativoUtente());
						LOG.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] IN idIstanza: "+idIstanza);
						Azione azione = new Azione();
						azione.setIdAzione(entity.getIdAzione());
						azione.setIdIstanza(idIstanza);
						azione.setIdWorkflow(entity.getIdWorkflow());
						azione.setDatiAzione("");
						
						LOG.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] IN idAzione: "+azione.getIdAzione());
						_compieAzione(user, idIstanza, azione);
						esisteAzioneAutomatica = true;
					} else {
					   elencoAzioni.add(WorkflowMapper.buildFromWorkflowEntity(entity));
					}
				}
			}
			
			if (!esisteAzioneAutomatica) {
				return elencoAzioni;
			} else {
				IstanzaEntity istanza = istanzaDAO.findById(idIstanza);	
				StoricoWorkflowEntity lastStoricoWf = storicoWorkflowDAO.findLastStorico(idIstanza)
						.orElseThrow(ItemNotFoundBusinessException::new);
				filter = new WorkflowFilter();
				filter.setIdModulo(istanza.getIdModulo());
				filter.setIdProcesso(lastStoricoWf.getIdProcesso());
				filter.setIdStatoWfPartenza(lastStoricoWf.getIdStatoWfArrivo());
				filter.setEscludiAzioniUtenteCompilante(true);
				return getElencoAzioniPossibili(user, idIstanza,filter);
			}
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoAzioniPossibili] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero elenco azioni possibili");
 		}
	}
	
	private boolean isAzioneWorkflowDaEseguireInAutomatico(WorkflowEntity entity) {
		return entity.getFlagAutomatico().equals("S") && entity.getIdCondition()==null && entity.getIdStatoWfPartenza().intValue() >= 2?true:false;
	}
	
	@Override
	public List<Workflow> getWorkflow(UserInfo user, WorkflowFilter filter) throws BusinessException {		
		List<Workflow> wf = new ArrayList<>();
		try {
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			if (elencoWorkflow != null && elencoWorkflow.size() > 0) {
				for (WorkflowEntity  entity : elencoWorkflow) {								
					   wf.add(WorkflowMapper.buildFromWorkflowEntity(entity));													
				}
			}		
			return wf;				
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoAzioni] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero elenco azioni ");
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


	@Override
	public Workflow getWorkflowById(Long idWorkflow) throws BusinessException {
		try {
			WorkflowEntity entity = workflowDAO.findById(idWorkflow);
			Workflow workflow = WorkflowMapper.buildFromWorkflowEntity(entity);
			return workflow;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoAzioniPossibili] Errore invocazione DAO - ",e );
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
			LOG.warn("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] ItemNotFoundDAOException idWorkflow=" + idWorkflow, notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore ricerca getStruttura per idWorkflow");
		}
	}
	
	@Override
	public DatiAzione getStrutturaByFilter(WorkflowFilter filter) throws BusinessException {
		Workflow workflow = null;
		try {
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			if (elencoWorkflow == null) {
				throw new BusinessException("Errore getStrutturaByFilter, for workflowDAO.find with " + filter);
			}
			if (elencoWorkflow.size() > 1) {
				throw new BusinessException("Errore getStrutturaByFilter trovati piu' di un record");
			}
			if (elencoWorkflow.isEmpty()) {
				throw new BusinessException("Errore getStrutturaByFilter elencoWorkflow empty.");
			}
			if (elencoWorkflow.size() == 1) {
				workflow = WorkflowMapper.buildFromWorkflowEntity(elencoWorkflow.get(0));
			}
			if (workflow == null) {
				LOG.error("[" + CLASS_NAME + "::getStrutturaByFilter] workflow==null WorkflowMapper.buildFromWorkflowEntity() with " + elencoWorkflow.get(0));
				throw new BusinessException("WorkflowMapper.buildFromWorkflowEntity");
			}
			DatiAzioneEntity datiAzioneEntity = workflowDAO.findDatiAzioneById(workflow.getIdDatiAzione());
			DatiAzione datiAzione = WorkflowMapper.buildFromDatiAzioneEntity(datiAzioneEntity);
			return datiAzione;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getStrutturaByFilter] Errore invocazione DAO ", e);
			throw new BusinessException("Errore ricerca getStrutturaByFilter ");
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getStrutturaByFilter] BusinessException " + be.getMessage());
			throw be;
		}
	}
	
	private void userCanDoAction(UserInfo user, Long idIstanza, Workflow workflow, Boolean isDaCancellare) throws BusinessException, DAOException {
		boolean validate = false;
		LOG.debug("[" + CLASS_NAME + "::userCanDoAction] user: "+user.getIdentificativoUtente() + 
				"  istanza: "+idIstanza+"  azione: " + workflow.getNomeAzione() +"  isDaCancellare:"+isDaCancellare);
		
		/* TODO 
		 * verificare se l'utente e' abilitato ad agire sull'istanza o sul processo
		 * verificare se l'azione richiesta e' prevista 
		 * verificare se sono presenti i dati richiesti dall'azione
		 */
		//user.getCodFisc().equals(entity.getCodiceFiscaleDichiarante());
		
		if (isDaCancellare)
		{
			// verifico se l'azione da cancellare e' annullabile
			if (workflow.getIsAnnullabile())
			{
				validate = true;
			}	
		}
		else {
			// verifico se l'azione richiesta e' prevista per lo stato corrente
			StoricoWorkflowEntity ultimaAzione = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			Integer idStatoCorrente = ultimaAzione.getIdStatoWfArrivo();
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdProcesso(ultimaAzione.getIdProcesso());
			filter.setIdStatoWfPartenza(idStatoCorrente);
			filter.setEscludiAzioniUtenteCompilante(false);
			filter.setEscludiAzioniDiSistema(true);
			List<Workflow> elenco = getElencoAzioniPossibili(filter);
			for (Workflow  step : elenco) {
				if (step.getIdAzione().intValue() == workflow.getIdAzione().intValue())
				{
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

		if (workflow.getIdUtenteDestinatario() != null)
		{
			// TODO acquisire nome e cognome dell'utente destinatario
			descDestinatario = "nome e cognome utente destinatario";
		}
		else if (workflow.getIdTipoUtenteDestinatario() != null)
		{
			// TODO acquisire il nome della tipologia di utente destinatario
			descDestinatario = "tipologia destinatario";
		}
		else if (workflow.getIdGruppoUtentiDestinatari() != null)
		{
			// TODO acquisire il nome del gruppo di utenti destinatari
			descDestinatario = "nome gruppo destinatari";
		}
		
		return descDestinatario;
	}

	@Deprecated // magic Number 16-PREPARA_RICEVUTA !!
	@Override
	@Transactional
	public Azione annullaAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::annullaAzione] IN istanza: "+idIstanza + " id workflow: " + azione.getIdWorkflow());

			Date now = new Date();

			Workflow workflow = getWorkflowById(azione.getIdWorkflow());
			Boolean isDaCancellare = true;
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
			
			// inserimento di un record in moon_wf_t_storico_workflow
			
			StatoEntity statoPartenzaE = statoDAO.findById(storico.getIdStatoWfArrivo());
			StatoEntity statoArrivoE = statoDAO.findById(storico.getIdStatoWfPartenza());
			
			String descDestinatario = storico.getDescDestinatario();
			String nomeAzione = "Annulla " + storico.getNomeAzione().toLowerCase();
			Long idAzione = 16L; // PREPARA_RICEVUTA
					
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
			
			if (azione != null && azione.getDatiAzione()!= null && !azione.getDatiAzione().equals(""))
			{
				LOG.info("[" + CLASS_NAME + "::doAzione] dati Azione: " + azione.getDatiAzione());
				storicoEntity.setDatiAzione(azione.getDatiAzione());
			}
			storicoEntity.setDescDestinatario(descDestinatario);
			
			storicoWorkflowDAO.updateDataFine(now, idIstanza);
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

	@Override
	@Transactional // NEW 03/09/2021 (era presente su BO !)
	public CompieAzioneResponse compieAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException {
		return _compieAzione(user, idIstanza, azione);
	}
	
	private CompieAzioneResponse _compieAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::compieAzione] IN istanza: "+idIstanza + " id workflow: " + azione.getIdWorkflow());
			Date now = new Date();

			WorkflowEntity workflowE = workflowDAO.findById(azione.getIdWorkflow());
			Workflow workflow = WorkflowMapper.buildFromWorkflowEntity(workflowE);
			IstanzaEntity istanzaE = istanzaDAO.findById(idIstanza);

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
			storicoEntity.setDescDestinatario(descDestinatario);
			
			LOG.info("[" + CLASS_NAME + "::compieAzione] dati Azione: " + azione.getDatiAzione());
			if (azione != null && azione.getDatiAzione()!=null && !azione.getDatiAzione().equals(""))
			{
				storicoEntity.setDatiAzione(azione.getDatiAzione());
			}
			
			// Chiusura storicoWorkflow precedente e inserimento del nuovo record in moon_wf_t_storico_workflow
			int numRecordAggiornati = storicoWorkflowDAO.updateDataFine(now, idIstanza);
			Long idStoricoWorkflow = storicoWorkflowDAO.insert(storicoEntity);
			storicoEntity.setIdStoricoWorkflow(idStoricoWorkflow);
			LOG.info("[" + CLASS_NAME + "::compieAzione] NEW idStoricoWorkflow: " + idStoricoWorkflow);
			updateIdIstanzaStoricoWorkflowOnRepositoryFileByWf(user, azione, workflow, storicoEntity);

			String url = null;
			CompieAzioneResponse azioneCompiuta = null;
			if (azione != null && azione.getDatiAzione()!=null && !azione.getDatiAzione().equals("")) {
				ProcessoEntity processo = processoDAO.findById(workflow.getIdProcesso());
				ProcessoServiceDelegate processoServiceDelegate = new ProcessoServiceDelegateFactory().getDelegate(processo.getCodiceProcesso());
				azioneCompiuta = processoServiceDelegate.compieAzione(user, istanzaE, azione, workflow, processo, storicoEntity);
			} else {
				azioneCompiuta = new CompieAzioneResponse();
				azioneCompiuta.setDescDestinatario(descDestinatario);
				azioneCompiuta.setNomeAzione(workflow.getNomeAzione());
				azioneCompiuta.setCodEsitoAzione("ok");	
			}
			
			//update stato istanza e cronologia se richiesto da workflow
			updateStatoIstanzaIfRequestedByWf(user, now, idIstanza, workflowE);
			
			return azioneCompiuta;
			
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] DAOException " + e.getMessage());
			throw new BusinessException("Errore inserimento nuovo step workflow");
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::compieAzione] BusinessException " + e.getMessage());
			throw e;
		}
	} 

	private void updateStatoIstanzaIfRequestedByWf(UserInfo user, Date now, Workflow workflow, Azione azione) throws DAOException {
		WorkflowEntity workflowE = workflowDAO.findById(azione.getIdWorkflow());
		updateStatoIstanzaIfRequestedByWf(user, now, azione.getIdIstanza(), workflowE);
	}

	private void updateStatoIstanzaIfRequestedByWf(UserInfo user, Date now, Long idIstanza, WorkflowEntity workflowE) throws DAOException {
		if ("S".equals(workflowE.getFlagStatoIstanza())) {
			istanzaDAO.updateStato(idIstanza, workflowE.getIdStatoWfArrivo());		
			// CRON
			IstanzaCronologiaStatiEntity istanzaECronologia = null;
			// last CRON
			IstanzaCronologiaStatiEntity lastIstanzaCronologia = istanzaDAO.findLastCronologia(idIstanza);
			lastIstanzaCronologia.setAttoreUpd(user.getIdentificativoUtente());
			lastIstanzaCronologia.setDataFine(now);
			istanzaDAO.updateCronologia(lastIstanzaCronologia);
			// new CRON
			istanzaECronologia = new IstanzaCronologiaStatiEntity();
			istanzaECronologia.setIdIstanza(idIstanza);
			istanzaECronologia.setIdStatoWf(workflowE.getIdStatoWfArrivo());
			istanzaECronologia.setAttoreIns(user.getIdentificativoUtente());
			istanzaECronologia.setDataInizio(now);
			istanzaECronologia.setIdAzioneSvolta(workflowE.getIdAzione());
			Long idCronologiaStati = istanzaDAO.insertCronologia(istanzaECronologia);
			istanzaECronologia.setIdCronologiaStati(idCronologiaStati);

			// DATI
			IstanzaDatiEntity istanzaEDati = null;
			istanzaEDati = istanzaDAO.findDati(idIstanza, lastIstanzaCronologia.getIdCronologiaStati());
			istanzaEDati.setDataUpd(now);
			
	        istanzaEDati.setDatiIstanza(istanzaEDati.getDatiIstanza());
			
			istanzaEDati.setIdCronologiaStati(istanzaECronologia.getIdCronologiaStati());
			istanzaEDati.setIdStepCompilazione(null);
			istanzaEDati.setIdTipoModifica(DecodificaTipoModificaDati.NON.getIdTipoModifica());
			Long idIstanzaDati = istanzaDAO.insertDati(istanzaEDati);
			istanzaEDati.setIdDatiIstanza(idIstanzaDati);
		}
	}
	
	private void updateIdIstanzaStoricoWorkflowOnRepositoryFileByWf(UserInfo user, Azione azione, Workflow workflow, /*Istanza istanza,*/ StoricoWorkflowEntity storicoEntity) {
		try {
			if (azione!=null && !"".equals(azione.getDatiAzione()))
			{
				if (workflow.getCodiceAzione().equals(DecodificaAzione.INVIA_INTEGRAZIONE.getCodice()) || 
						workflow.getCodiceAzione().equals(DecodificaAzione.INVIA_RICEVUTA.getCodice()) ||
						workflow.getCodiceAzione().equals(DecodificaAzione.INVIA_OSSERVAZIONI.getCodice())						
						) {
				
					LOG.info("[" + CLASS_NAME + "::updateIdIstanzaStoricoWorkflowOnRepositoryFileByWf] Prova assegnazione IdStoricoWorkflow su RepositoryFile ...");
					// FROM Azione_Default
					IntegrazioneActionEntity notificaActionEntity = DatiAzioneHelper.parseAzioneNotificaIntegrazione(azione.getDatiAzione());
					List<String> formIoFileNames = notificaActionEntity.getIntegrazioneFormIoFileNames();
					for (String formioNomeFileName : formIoFileNames) {
						int numRecordAggiornati = repositoryFileDAO.updateIdStoricoWorkflowIdIstanzaByFormioFileName(formioNomeFileName, storicoEntity.getIdIstanza(), storicoEntity.getIdStoricoWorkflow());
						LOG.info("[" + CLASS_NAME + "::updateIdIstanzaStoricoWorkflowOnRepositoryFileByWf] idIstanza=" + storicoEntity.getIdIstanza() + " fileName=" + formioNomeFileName + " sono aggiornati " + numRecordAggiornati + " records.");
					}
				}
			}
		} catch (BusinessException e) {
			LOG.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowOnRepositoryFileByWf] BusinessException " + e.getMessage());
		}
	}

	public DatiAzione initDatiAzione(UserInfo user, Long idIstanza, Long idWorkflow, String ipAddress) throws BusinessException {
		try {
			Workflow workflow = getWorkflowById(idWorkflow);
			ProcessoEntity processo = processoDAO.findById(workflow.getIdProcesso());
			ProcessoServiceDelegate processoServiceDelegate = new ProcessoServiceDelegateFactory()
					.getDelegate(processo.getCodiceProcesso());
			return processoServiceDelegate.getInitData(user, idIstanza, workflow, ipAddress);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::initDatiAzione] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore initDatiAzione per idIstanza " + idIstanza + ", workflow " + idWorkflow);
		}
	}

	public Workflow identificaAzioneWorkflowDaEseguireDopoInviata(Long idModulo) throws ItemNotFoundBusinessException, BusinessException {
		try {
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdModulo(idModulo);
			filter.setIdStatoWfPartenza(DecodificaStatoIstanza.INVIATA.getIdStatoWf());
			filter.setEscludiAzioniUtenteCompilante(true);
			//List<Workflow> elenco = workflowService.getElencoAzioniPossibili(user, istanzaSaved.getIdIstanza(), filter); // Possible effetto indesiderabile se fosse anche con flag_automatico, verrebbe esseguita 2 volte
			List<WorkflowEntity> elenco = workflowDAO.find(filter);
			if (elenco==null) {
				LOG.error("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguireDopoInviata] NESSUN Workflow Azioni possibile per idModulo:" + 
						idModulo + " idStatoPartenza:" + DecodificaStatoIstanza.INVIATA.getIdStatoWf());
				throw new ItemNotFoundBusinessException("NESSUN Workflow Azioni");
			}
			if (elenco.size()>1) {
				LOG.error("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguireDopoInviata] "+elenco.size()+" is more than one Workflow Azioni possibile per idModulo:" + 
						idModulo + " idStatoPartenza:" + DecodificaStatoIstanza.INVIATA.getIdStatoWf());
				throw new ItemNotFoundBusinessException("More than one Workflow Azioni");
			}
			Workflow result = WorkflowMapper.buildFromWorkflowEntity(elenco.get(0));
			LOG.info("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguireDopoInviata] result = " + result);
			return result;
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguireDopoInviata] DAOException per idModulo " + idModulo);
			throw new BusinessException();
		}
	}

}

