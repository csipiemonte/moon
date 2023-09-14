/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.moon.moonbobl.business.service.IstanzeService;
import it.csi.moon.moonbobl.business.service.WorkflowService;
import it.csi.moon.moonbobl.business.service.impl.azione.AzioneService;
import it.csi.moon.moonbobl.business.service.impl.azione.AzioneServiceFactory;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ProcessoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.OneriCostrDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.OneriCostrIbanDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrDomandaEntity;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrIbanEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.AzioneInitParams;
import it.csi.moon.moonbobl.business.service.impl.dto.DatiAzioneEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ProcessoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowFilter;
import it.csi.moon.moonbobl.business.service.impl.helper.CambioResidenzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiAzioneHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaBoHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.OneriCostrDatiIstanzaHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.MyDocsActionEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.WfProtocolloEdil;
import it.csi.moon.moonbobl.business.service.impl.initializer.IstanzaInitializer;
import it.csi.moon.moonbobl.business.service.mapper.StatoMapper;
import it.csi.moon.moonbobl.business.service.mapper.WorkflowMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Azione;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAzione;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.StoricoWorkflow;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoIstanza;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoModificaDati;

/**
 * @author Alberto
 * @author Danilo
 * Layer di logica servizi che richiama i DAO
 */
@Component
public class WorkflowServiceImpl implements WorkflowService {
	
	private final static String CLASS_NAME = "WorkflowServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
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
	IstanzaInitializer istanzaInitializer;
	@Autowired
	OneriCostrDAO oneriCostrDAO;
	@Autowired
	OneriCostrIbanDAO oneriCostrIbanDAO;
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	ProcessoDAO processoDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	@Override
	public Optional<StoricoWorkflow> getUltimoStepWorkflow(Long idIstanza) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::getUltimoStepWorkflow] IN idIstanza: "+idIstanza);
			return storicoWorkflowDAO.findLastStorico(idIstanza)
					.map(WorkflowMapper::buildFromStoricoWorkflowEntity);
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getUltimoStepWorkflow] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getUltimoStepWorkflow] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca ultimo step workflow per id");
		} 
	}

	@Override
	public List<Workflow> getElencoAzioniPossibili(UserInfo user, Long idIstanza, WorkflowFilter filter, String ipAddress) throws BusinessException {
		
		List<Workflow> elencoAzioni = new ArrayList<Workflow>();
		Istanza istanza = istanzeService.getIstanzaById(user, idIstanza);
		boolean esisteAzioneAutomatica = false;
		
		try {
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			if (elencoWorkflow != null && elencoWorkflow.size() > 0) {
				for (WorkflowEntity  entity : elencoWorkflow) {
					
					// check azione automatica
					if (entity.getFlagAutomatico().equals("S") && entity.getIdStatoWfPartenza().intValue() >= 2) {
						
						log.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] IN user: "+user.getIdentificativoUtente());
						log.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] IN idIstanza: "+idIstanza);
																		
						Azione azione = new Azione();
						azione.setIdAzione(entity.getIdAzione());						
						azione.setIdIstanza(idIstanza);
						azione.setIdWorkflow(entity.getIdWorkflow());
						azione.setDatiAzione("");
						
						log.debug("[" + CLASS_NAME + "::getElencoAzioniPossibili] IN idAzione: "+azione.getIdAzione());
																				
						compieAzione(user, idIstanza, azione, ipAddress);
						
						esisteAzioneAutomatica = true;
											
					}
					else {
						if (!(istanza.getFlagArchiviata() && DecodificaAzione.ARCHIVIA.isCorrectCodice(entity.getCodiceAzione())))
							elencoAzioni.add(WorkflowMapper.buildFromWorkflowEntity(entity));
					}
				}
			}
			
			if (!esisteAzioneAutomatica) {
				return elencoAzioni;
			}
			else {
				istanza = istanzeService.getIstanzaById(user, idIstanza);				
				StoricoWorkflowEntity lastStoricoWf = storicoWorkflowDAO.findLastStorico(idIstanza)
						.orElseThrow(ItemNotFoundBusinessException::new);				
				filter = new WorkflowFilter();
				filter.setIdModulo(istanza.getModulo().getIdModulo());
				filter.setIdProcesso(lastStoricoWf.getIdProcesso());
				filter.setIdStatoWfPartenza(lastStoricoWf.getIdStatoWfArrivo());
				filter.setEscludiAzioniUtenteCompilante(true);
				filter.setEscludiAzioniDiSistema(true);
				return getElencoAzioniPossibili(user, idIstanza,filter,ipAddress);
			}
			
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoAzioniPossibili] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero elenco azioni possibili");
 		}
	}
	
	
	@Override
	public List<Workflow> getElencoAzioniPossibili(WorkflowFilter filter) throws BusinessException {
		List<Workflow> elencoAzioni = new ArrayList<Workflow>();
		try {
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			elencoAzioni = elencoWorkflow.stream()
				.map(WorkflowMapper::buildFromWorkflowEntity)
				.collect(Collectors.toList());
			return elencoAzioni;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoAzioniPossibili] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero elenco azioni possibili");
 		}
	}

	@Override
	public List<Workflow> getElencoWorkflow(WorkflowFilter filter, String fields) throws BusinessException {
		List<Workflow> elencoAzioni = new ArrayList<Workflow>();
		try {
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			elencoAzioni = elencoWorkflow.stream()
				.map(WorkflowMapper::buildFromWorkflowEntity)
				.map(w -> completeRequestFields(fields, w))
				.collect(Collectors.toList());
			return elencoAzioni;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoWorkflow] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero getElencoWorkflow");
 		}
	}
	
	private Workflow completeRequestFields(String fields, Workflow result) {
		if (StringUtils.isEmpty(fields)) {
			return result;
		}
		List<String> listFields = Arrays.asList(fields.split(","));
		if (listFields.contains(FIELD_STATO_PARTENZA)) {
			log.debug("[" + CLASS_NAME + "::completeRequestFields] " + FIELD_STATO_PARTENZA);
			result = completaWorkflowStatoPartenza(result);
		}
		if (listFields.contains(FIELD_STATO_ARRIVO)) {
			log.debug("[" + CLASS_NAME + "::completeRequestFields] " + FIELD_STATO_ARRIVO);
			result = completaWorkflowStatoArrivo(result);
		}
		return result;
	}

	private Workflow completaWorkflowStatoPartenza(Workflow result) {
		result.setStatoPartenza(StatoMapper.buildFromEntity(statoDAO.findById(result.getIdStatoWfPartenza())));
		return result;
	}

	private Workflow completaWorkflowStatoArrivo(Workflow result) {
		result.setStatoArrivo(StatoMapper.buildFromEntity(statoDAO.findById(result.getIdStatoWfArrivo())));
		return result;
	}

	@Override
	public List<StoricoWorkflow> getElencoStorico(Long idIstanza) throws BusinessException {
		List<StoricoWorkflow> listStorico = new ArrayList<StoricoWorkflow>();
		try {
			List<StoricoWorkflowEntity> listStoricoEntity = storicoWorkflowDAO.findByIdIstanza(idIstanza);
			listStorico = listStoricoEntity.stream()
				.map(WorkflowMapper::buildFromStoricoWorkflowEntity)
				.collect(Collectors.toList());
			return listStorico;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoStorico] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero elenco storico");
 		}
	}

	@Override
	public Workflow getWorkflowById(Long idWorkflow, String fields) throws BusinessException {
		try {
			WorkflowEntity entity = workflowDAO.findById(idWorkflow);
			Workflow workflow = WorkflowMapper.buildFromWorkflowEntity(entity);
			return completeRequestFields(fields, workflow);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getWorkflowById] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero workflow con id " + idWorkflow);
 		}
	}
	@Override
	public Workflow getWorkflowById(Long idWorkflow) throws BusinessException {
		try {
			WorkflowEntity entity = workflowDAO.findById(idWorkflow);
			Workflow workflow = WorkflowMapper.buildFromWorkflowEntity(entity);
			return workflow;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getWorkflowById] Errore invocazione DAO - ",e );
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
			log.error("[" + CLASS_NAME + "::getWorkflowByFilter] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore getWorkflowByFilter");
 		}
	}
	
	@Override
	public StoricoWorkflow getStoricoWorkflowByFilter(StoricoWorkflowFilter filter) throws BusinessException {
		try {
			StoricoWorkflow storicoWorkflow = new StoricoWorkflow();
			List<StoricoWorkflowEntity> elencoStorico = storicoWorkflowDAO.find(filter);
			if (elencoStorico != null) {
				if (elencoStorico.size() == 1) {
					storicoWorkflow = WorkflowMapper.buildFromStoricoWorkflowEntity(elencoStorico.get(0));
				}
				if (elencoStorico.size() > 1) {
					throw new BusinessException("Errore getStoricoWorkflowByFilter trovati piu' di un record");
				}
			}
			return storicoWorkflow;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getWorkflowByFilter] Errore invocazione DAO - ",e );
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
			log.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] Errore invocazione DAO - ",notFoundEx );
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdWorkflow] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore ricerca modulo per id");
		}
	}
	
	@Override
	public DatiAzione getStrutturaByCodicedAzione(String codice) throws BusinessException {
		try {			
			DatiAzioneEntity datiAzioneEntity = workflowDAO.findDatiAzioneByCodice(codice);
			DatiAzione datiAzione = WorkflowMapper.buildFromDatiAzioneEntity(datiAzioneEntity);
			return datiAzione;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getStrutturaByCodicedAzione] Errore invocazione DAO - ",notFoundEx );
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getStrutturaByCodicedAzione] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore ricerca struttura per codiced");
		}
	}
	
	@Override
	public DatiAzione getStrutturaByFilter(WorkflowFilter filter) throws BusinessException {
		Workflow workflow = new Workflow();
		try {
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			if (elencoWorkflow != null) {
				if (elencoWorkflow.size() == 1) {
					workflow = WorkflowMapper.buildFromWorkflowEntity(elencoWorkflow.get(0));
				}
				if (elencoWorkflow.size() > 1) {
					throw new BusinessException("Errore getStrutturaByFilter trovati piu' di un record");
				}
			}
			DatiAzioneEntity datiAzioneEntity = workflowDAO.findDatiAzioneById(workflow.getIdDatiAzione());
			DatiAzione datiAzione = WorkflowMapper.buildFromDatiAzioneEntity(datiAzioneEntity);
			return datiAzione;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getStrutturaByFilter] Errore invocazione DAO - ",notFoundEx );
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getStrutturaByFilter] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore ricerca getStrutturaByFilter ");
		}
	}
	
	private void userCanDoAction(UserInfo user, Long idIstanza, Workflow workflow, Boolean isDaCancellare) throws BusinessException, DAOException {
		boolean validate = false;
		log.debug("[" + CLASS_NAME + "::userCanDoAction] user: "+user.getIdentificativoUtente() + 
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
		log.debug("[" + CLASS_NAME + "::identificaDestinatario] id utente destinatario: "+ workflow.getIdUtenteDestinatario() + 
				"  tipo utente destinatario: "+ workflow.getIdTipoUtenteDestinatario() +
				"  gruppo utenti destinatari: " + workflow.getIdGruppoUtentiDestinatari() );

		if (workflow.getIdUtenteDestinatario() != null)
		{
			// TODO acquisire nome e cognome dell'utente destinatario
			descDestinatario = "";
		}
		else if (workflow.getIdTipoUtenteDestinatario() != null)
		{
			// TODO acquisire il nome della tipologia di utente destinatario
			descDestinatario = "";
		}
		else if (workflow.getIdGruppoUtentiDestinatari() != null)
		{
			// TODO acquisire il nome del gruppo di utenti destinatari
			descDestinatario = "";
		}
		
		return descDestinatario;
	}

	@Override
	@Transactional
	public Azione annullaAzione(UserInfo user, Long idIstanza, Azione azione) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::annullaAzione] IN istanza: "+idIstanza + " id workflow: " + azione.getIdWorkflow());

			Date now = new Date();

			Workflow workflow = getWorkflowById(azione.getIdWorkflow());
			Boolean isDaCancellare = true;
			userCanDoAction(user, idIstanza, workflow, isDaCancellare);
			
			StoricoWorkflowEntity storico = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			if (storico.getIdProcesso().intValue() != workflow.getIdProcesso() ||
				storico.getIdAzione().intValue() != workflow.getIdAzione() ||
				storico.getIdStatoWfPartenza() != workflow.getIdStatoWfPartenza() ||
				storico.getIdStatoWfArrivo() != workflow.getIdStatoWfArrivo())
			{
				log.info("[" + CLASS_NAME + "::annullaAzione] verifica azione da annullare fallita: " + 
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
			
			if (azione != null && azione.getDatiAzione()!= null && !azione.getDatiAzione().equals(""))
			{
				log.info("[" + CLASS_NAME + "::annullaAzione] dati Azione: " + azione.getDatiAzione());
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
			
			log.error("[" + CLASS_NAME + "::annullaAzione] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore su annullaAzione");
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::annullaAzione] ", e);
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
			log.debug("[" + CLASS_NAME + "::getStoricoWorkflowById] idStoricoWorkflow: " + idStoricoWorkflow);
			StoricoWorkflowEntity storico = storicoWorkflowDAO.findStoricoWorkflowById(idStoricoWorkflow);
			StoricoWorkflow storicoWorkflow = WorkflowMapper.buildFromStoricoWorkflowEntity(storico);
			return storicoWorkflow;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getStoricoWorkflowById] Errore invocazione DAO - ",e );
			throw new BusinessException("Errore recupero storicoworkflow con id " + idStoricoWorkflow);
 		}
	}

	
	@Override
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Transactional
	public Azione compieAzione(UserInfo user, Long idIstanza, Azione azione, String ipAddress) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::compieAzione] IN istanza: "+idIstanza + " id workflow: " + azione.getIdWorkflow());
			
			Date now = new Date();
			azione.setIdIstanza(idIstanza);
			
			Workflow workflow = getWorkflowById(azione.getIdWorkflow());
			userCanDoAction(user, idIstanza, workflow, false);
			
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
			
			StoricoWorkflowEntity storicoEntity = new StoricoWorkflowEntity() ;
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
			storicoEntity.setIdDatiazione(workflow.getIdDatiAzione());
			
			log.info("[" + CLASS_NAME + "::compieAzione] dati Azione: " + azione.getDatiAzione());
			Istanza istanza = null;
			
			if (azione != null)
			{
				// AzioneService
				istanza = istanzeService.getIstanzaById(user, idIstanza);
				AzioneService azioneService = new AzioneServiceFactory().getService(istanza.getModulo().getCodiceModulo());
				
				if (!azione.getDatiAzione().equals(""))
				{
					storicoEntity.setDatiAzione(azione.getDatiAzione());

					switch(workflow.getCodiceAzione()) {
						case "INSERISCI_PROTOCOLLO" :
							azioneService.inserisciProtocollo(user, azione.getDatiAzione(), istanza, storicoEntity);
							// protocolla(idIstanza, azione); // Da capire se PROTOCOLLA puo essere generalizzato e quindi forse da spostare su azioneService cosi da essere specializzato per modulo mantenendo un configurazione PROCOLLA generica
							break;
						case "VERIFICA_IMPORTI" :
							updateExtOneriCostr(user, azione, idIstanza); // Da Spostare in Class Specifica di ExtOneriCostr
							break;
						case "GESTISCI_IBAN" :
							updateExtOneriIban(user, azione); // Da Spostare in Class Specifica di ExtOneriCostr
							break;
						case "GENERA_RICEVUTA" :
							Long idFile = azioneService.generaSalvaRicevutaPdf(user, azione.getDatiAzione(), istanza);
							storicoEntity.setIdFileRendering(idFile);
							break;
						case "INVIA_RICEVUTA" :
							azioneService.inviaRicevuta(user, azione.getDatiAzione(), istanza);
							break;
						case "RICHIEDI_RICEVUTA" :
							azioneService.richiediRicevutaPagamento(user, azione.getDatiAzione(), istanza, storicoEntity);	
							break;
						case "RICHIEDI_OSSERVAZIONI" :
							azioneService.richiediOsservazioni(user, azione.getDatiAzione(), istanza, storicoEntity);						
							break;
						case "RICHIEDI_INTEGRAZIONE" :
							azioneService.richiediIntegrazione(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "RICHIEDI_ULTERIORE_INTEGRAZIONE" :
							azioneService.richiediUlterioreIntegrazione(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;	
						case "INVITO_CONFOR_DEBITI" :
							azioneService.invitoConformareDebiti(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;	
						case "CONCLUDI_CON_ESITO_POSITIVO" :
							azioneService.concludiEsitoPositivo(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "INVIA_COMUNICAZIONE" :
						case "RESTITUISCI_AL_PROPONENTE" :			
							azioneService.inviaComunicazione(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "INVIA_COMUNICAZIONE_AVVIO" :
							azioneService.inviaComunicazioneAvvio(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "INVIA_DOCUMENTAZIONE" :
							azioneService.inviaDocumentazione(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "INVIA_INTEGRAZIONE" :
							azioneService.inviaIntegrazione(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "RIFIUTA" :						
							updateDatiRifiutata(user,workflow,azione,storicoEntity);
							break;
						case "RESPINGI_EMAIL" :	
						case "COMUNICA_ESITO_NEGATIVO":
							azioneService.respingiConComunicazione(user, azione.getDatiAzione(), istanza, storicoEntity);						
							break;
						case "COMUNICA_ESITO_POSITIVO":
							azioneService.comunicaEsitoPositivo(user, azione.getDatiAzione(), istanza, storicoEntity);						
							break;
						case "APPROVA" :
							azioneService.approva(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "APPROVA_PARZIALMENTE" :
							azioneService.approvaParzialmente(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "ACCOGLI" :
							azioneService.accogli(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "RESPINGI" :
							azioneService.respingi(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "PRESA_ATTO" :
							azioneService.presaAtto(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "RESTITUISCI_IN_BOZZA":
						case "RIPORTA_IN_BOZZA" :
							azioneService.riportaInBozza(user, azione.getDatiAzione(), istanza, storicoEntity);
							break;
						case "DINIEGO":
							azioneService.diniego(user,azione.getDatiAzione(),istanza,storicoEntity);
							break;
						case "ACCOGLIMENTO":
							azioneService.accoglimento(user,azione.getDatiAzione(),istanza,storicoEntity);
							break;
						case "CREA_RISPOSTA_CON_PROTOCOLLO":
							azioneService.creaRispostaConProtocollo(user,azione.getDatiAzione(),istanza,storicoEntity);
							break;
						case "ARCHIVIA":
							updateFlagArchiviata(user, idIstanza);
							break;
						case "VERIFICA_ANPR":
							azioneService.verificaAnpr(user,azione.getDatiAzione(),istanza,storicoEntity);
							break;
						case "ASSEGNA_OPERATORE":
							String identificativoOperatore = azioneService.assegnaOperatore(user,azione.getDatiAzione(),istanza,storicoEntity);
							storicoEntity.setAttoreUpd(identificativoOperatore);
							break;
					}
				} // END Azione con dati
				else
				{
					switch(workflow.getCodiceAzione()) {
						case "ELIMINA" :
							azioneService.elimina(user, istanza);
							break;
						case "ARCHIVIA":
							updateFlagArchiviata(user, idIstanza);
							break;
						case "PROTOCOLLA":
							azioneService.protocolla(user, idIstanza);
							break;
						case "PROTOCOLLA_RICEVUTA":
							azioneService.protocollaRicevuta(user, azione, idIstanza);
							break;
						case "CREA_RISPOSTA_CON_PROTOCOLLO":
							azioneService.creaRispostaConProtocollo(user,null,istanza,storicoEntity);
							break;
					}
				}	
				//INVIA compilaBO
				if (workflow.getCodiceAzione().equals("INVIA")) {
					istanzeService.invia(user, istanza.getIdIstanza(), null, ipAddress);
				}
			} 

			storicoEntity.setDescDestinatario(descDestinatario);

			// Chiusura passo precedente e inserimento del nuovo record in moon_wf_t_storico_workflow
			int numRecordAggiornati = storicoWorkflowDAO.updateDataFine(now, idIstanza);
			Long idStoricoWorkflow = storicoWorkflowDAO.insert(storicoEntity);
			storicoEntity.setIdStoricoWorkflow(idStoricoWorkflow);
				
			updateStatoIstanzaIfRequestedByWf(user, now, workflow, azione);
			updateIdStoricoWorkflowOnRepositoryFileByWf(user, azione, workflow, istanza, storicoEntity);
			
			Azione azioneCompiuta = new Azione();
			azioneCompiuta.setDescDestinatario(descDestinatario);
			azioneCompiuta.setNomeAzione(workflow.getNomeAzione());
			azioneCompiuta.setIdIstanza(idIstanza);
			azioneCompiuta.setIdStoricoWorkflow(storicoEntity.getIdStoricoWorkflow());
			azioneCompiuta.setCodEsitoAzione("ok");
			return azioneCompiuta;
			
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::compieAzione] DAOException " + e.getMessage());
			throw new BusinessException("Errore inserimento nuovo step workflow");
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::compieAzione] BusinessException " + e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::compieAzione] BusinessException " + e.getMessage());
			throw new BusinessException();
		}
	}
	
	public DatiAzione initDatiAzione(UserInfo user, Long idIstanza, Long idWorkflow, String ipAddress) throws BusinessException {
		try {
			log.info("[" + CLASS_NAME + "::initDatiAzione] IN idIstanza="+idIstanza+"  idWorkflow="+idWorkflow);
			String initDataNomeClass = "";
			Workflow workflow = getWorkflowById(idWorkflow);
			Long idProcesso = workflow.getIdProcesso();
			ProcessoEntity processo = processoDAO.findById(idProcesso);
			// TODO passare in workflow la init
			if ("RP_CCDC".equals(processo.getCodiceProcesso())) { // idProcesso 5-RP_CCDC
				if (workflow.getCodiceAzione().equals("VERIFICA_IMPORTI")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.regp.ccdc.ContributiCDCInitializer";
				}
				else if (workflow.getCodiceAzione().equals("GESTISCI_IBAN")) {
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.regp.ccdc.VerificaIbanInitializer";
				}
				else if (workflow.getCodiceAzione().equals("INIZIA_VERIFICA_DATI")) {
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.regp.ccdc.DatiPraticaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("PROSEGUI")) {
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.regp.ccdc.DatiBeneficiarioInitializer";
				}
				if (workflow.getCodiceAzione().equals("RESPINGI_EMAIL")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.dem.NotificaInitializer";
				}
			}
			else if ("COTO_RESID".equals(processo.getCodiceProcesso()) || 
					"COTO_RESID_CAF".equals(processo.getCodiceProcesso()) ) { // idProcesso 6-COTO_RESID e // idProcesso 45
				if (workflow.getCodiceAzione().equals("GENERA_RICEVUTA")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.dem.DatiRichiedenteInitializer";
				}
				if (workflow.getCodiceAzione().equals("INVIA_RICEVUTA")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.dem.NotificaInitializer";
				}
				if (workflow.getCodiceAzione().equals("RICHIEDI_INTEGRAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.dem.NotificaInitializer";
				}
				if (workflow.getCodiceAzione().equals("RESPINGI_EMAIL")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.dem.NotificaInitializer";
				}
				if (workflow.getCodiceAzione().equals("RIPORTA_IN_BOZZA") || workflow.getCodiceAzione().equals("RESTITUISCI_IN_BOZZA")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.dem.NotificaInitializer";
				}
				
			}
			else if ("COTO_RIN".equals(processo.getCodiceProcesso())) { // idProcesso 4-COTO_RIN
				if (workflow.getCodiceAzione().equals("RICHIEDI_INTEGRAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.comm.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("RICHIEDI_ULTERIORE_INTEGRAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.comm.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("INVITO_CONFOR_DEBITI")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.comm.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("CONCLUDI_CON_ESITO_POSITIVO")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.comm.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("RESPINGI_EMAIL")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.comm.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("INVIA_COMUNICAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.comm.NotificaInitializer";
				}
			}
			else if ("GEST_AMIA".equals(processo.getCodiceProcesso())) { // idProcesso 11-GEST_AMIA
				if (workflow.getCodiceAzione().equals("PRESA_ATTO")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
			}
			else if ("ASL_AUTOR".equals(processo.getCodiceProcesso())) { // idProcesso 12-ASL_AUTOR
				if (workflow.getCodiceAzione().equals("APPROVA")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("RICHIEDI_INTEGRAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("RESPINGI_EMAIL")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
			}
			else if ("RP_TCR_ACC".equals(processo.getCodiceProcesso())  || 
					"RP_TCR_DDR".equals(processo.getCodiceProcesso())) { // idProcesso 30-RP_TCR_ACC e // idProcesso 21-RP_TCR_DDR
				if (workflow.getCodiceAzione().equals("ACCOGLIMENTO")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.regp.tcr.WfTcrAccoglimentoInitializer";
				}
				else if (workflow.getCodiceAzione().equals("DINIEGO")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.regp.tcr.WfTcrDiniegoInitializer";
				}
				else if (workflow.getCodiceAzione().equals("INVIA_COMUNICAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.regp.tcr.NotificaInitializer";
				}
			}
			else if ("FINE_CONC".equals(processo.getCodiceProcesso())) { 
				if (workflow.getCodiceAzione().equals("INVIA_DOCUMENTAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.regp.fineconc.WfDocumentazioneInitializer";
				}
				else {
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
			}
			else if ("COTO_ARERA".equals(processo.getCodiceProcesso()) || 
					"COTO_ARERA_SP".equals(processo.getCodiceProcesso())  ) { 
				if (workflow.getCodiceAzione().equals("RESPINGI") || 
						workflow.getCodiceAzione().equals("ACCOGLI") || 
						workflow.getCodiceAzione().equals("RICHIEDI_INTEGRAZIONE") ||
						workflow.getCodiceAzione().equals("CHIUDI") ||
						workflow.getCodiceAzione().equals("CHIUDI_SENZA_COMUNICAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.trib.AreraInitializer";
				}
			}
			else if ("COTO_TRIB_MIX".equals(processo.getCodiceProcesso()) ) { 
				if (workflow.getCodiceAzione().equals("RESPINGI") || 
						workflow.getCodiceAzione().equals("RICHIEDI_INTEGRAZIONE") ) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.trib.TribMixInitializer";
				}
			}
			else {
				// vale per qualsiasi processo
				if (workflow.getCodiceAzione().equals("VERIFICA_ANPR")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.coto.dem.VerificaAnprInitializer";
				}
				else if (workflow.getCodiceAzione().equals("RICHIEDI_INTEGRAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("RICHIEDI_RICEVUTA")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("RICHIEDI_OSSERVAZIONI")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("RESPINGI_EMAIL") || 
						workflow.getCodiceAzione().equals("RESTITUISCI_AL_PROPONENTE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("APPROVA") || workflow.getCodiceAzione().equals("APPROVA_PARZIALMENTE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("INVIA_COMUNICAZIONE")) { 
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
				else if (workflow.getCodiceAzione().equals("INVIA_DOCUMENTAZIONE")) { 
					// consente di inviare documentazione all'ufficio
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.InviaDocumentazioneInitializer";
				}
				else if (workflow.getCodiceAzione().equals("ASSEGNA_OPERATORE")) { 
					// consente di assegnare il task di lavorazione ad un operatore
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.AssegnaOperatoreInitializer";
				}
				else {
					// vale per qualsiasi azione
					initDataNomeClass = "it.csi.moon.moonbobl.business.service.impl.initializer.NotificaInitializer";
				}
			}
			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
			
			AzioneInitParams initParams = new AzioneInitParams();
			initParams.setDatiIstanza(datiE.getDatiIstanza());
			initParams.setCodiceFiscale(user.getIdentificativoUtente());
			initParams.setCognome(user.getCognome());
			initParams.setNome(user.getNome());
			initParams.setIpAddress(ipAddress);
			initParams.setIdIstanza(idIstanza);
			
			istanzaInitializer.initialize(initParams);
			String datiInit = istanzaInitializer.getDati(initDataNomeClass);
			DatiAzione datiAzione = new DatiAzione();
			datiAzione.setData(datiInit);
			log.info("[" + CLASS_NAME + "::initDatiAzione] OUT datiAzione="+datiAzione);
			return datiAzione;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore init per idIstanza " + idIstanza + ", workflow " + idWorkflow);
		}
	}	


//	private void pubblicaMyDocs(Long idIstanza, Long idStoricoWorkflow) throws BusinessException{	
//		
//		log.debug("[" + CLASS_NAME + "::pubblicaMyDocs] - id istanza: "+ idIstanza+" - id storico workflow: " + idStoricoWorkflow);		
//		List<RepositoryFileLazyEntity> listFiles = repositoryFileDAO.findByIdIstanzaStoricoWf(idIstanza, idStoricoWorkflow);
//		
//		String res = "";
//		for (RepositoryFileLazyEntity file : listFiles) {			
//			try {
//				res = moonsrvDAO.pubblicaFileMyDocs(file.getIdFile(),idIstanza,idStoricoWorkflow);
//				log.info("[" + CLASS_NAME + "::pubblicaMyDocs] idFile=" + file.getIdFile() + " call result: "+res);
//			} catch (DAOException e) {
//				log.error("[" + CLASS_NAME + "::pubblicaMyDocs] idFile=" + file.getIdFile() + " call failed: "+ e.getCode()+ " - "+e.getMessage());
////				throw new BusinessException(e);
//			}
//		}	
//	}

	@Override
	@Transactional
	public Azione motivaModifica(UserInfo user, Long idIstanza, Azione azione) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::motivaModifica] IN istanza: "+idIstanza);
			
			Date now = new Date();
			azione.setIdIstanza(idIstanza);
			
			StoricoWorkflowEntity storico = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			StatoEntity statoPartenzaE = statoDAO.findById(storico.getIdStatoWfPartenza());
			StatoEntity statoArrivoE = statoDAO.findById(storico.getIdStatoWfArrivo());			
			String descDestinatario = storico.getDescDestinatario();
			
			StoricoWorkflowEntity storicoEntity = new StoricoWorkflowEntity() ;
			storicoEntity.setAttoreUpd(user.getIdentificativoUtente());
			storicoEntity.setIdIstanza(idIstanza);
			storicoEntity.setIdProcesso(storico.getIdProcesso());
			storicoEntity.setIdStatoWfPartenza(storico.getIdStatoWfPartenza());
			storicoEntity.setIdStatoWfArrivo(storico.getIdStatoWfArrivo());
			storicoEntity.setIdAzione(storico.getIdAzione());
			storicoEntity.setDataInizio(now);
			storicoEntity.setNomeStatoWfPartenza(statoPartenzaE.getNomeStatoWf());
			storicoEntity.setNomeStatoWfArrivo(statoArrivoE.getNomeStatoWf());
			storicoEntity.setNomeAzione(storico.getNomeAzione());
			storicoEntity.setDatiAzione(azione.getDatiAzione());
			storicoEntity.setDescDestinatario(descDestinatario);

			// Chiusura passo precedente e inserimento del nuovo record in moon_wf_t_storico_workflow
			int numRecordAggiornati = storicoWorkflowDAO.updateDataFine(now, idIstanza);
			Long idStoricoWorkflow = storicoWorkflowDAO.insert(storicoEntity);
			storicoEntity.setIdStoricoWorkflow(idStoricoWorkflow);
		

			Azione azioneCompiuta = new Azione();
			azioneCompiuta.setDescDestinatario(descDestinatario);
			azioneCompiuta.setNomeAzione(storico.getNomeAzione());
			azioneCompiuta.setCodEsitoAzione("ok");
			return azioneCompiuta;
			
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::motivaModifica] DAOException " + e.getMessage());
			throw new BusinessException("Errore inserimento nuovo step workflow");
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::motivaModifica] BusinessException " + e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::motivaModifica] BusinessException " + e.getMessage());
			throw new BusinessException();
		}
	}

	private void updateIdStoricoWorkflowOnRepositoryFileByWf(UserInfo user, Azione azione, Workflow workflow, Istanza istanza, StoricoWorkflowEntity storicoEntity) {
		try {
			if (azione!=null && istanza!=null && !azione.getDatiAzione().equals(""))
			{
				switch(workflow.getCodiceAzione()) {
					case "RICHIEDI_INTEGRAZIONE" :
					case "RICHIEDI_ULTERIORE_INTEGRAZIONE" :
						log.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowOnRepositoryFileByWf] Prova assegnazione IdStoricoWorkflow su RepositoryFile ...");
						// FROM Azione_Default
						NotificaActionEntity notificaActionEntity = CambioResidenzaHelper.parseAzioneNotifica(azione.getDatiAzione());
						List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();
						for (String formioNomeFileName : formIoFileNames) {
							int numRecordAggiornati = repositoryFileDAO.updateIdStoricoWorkflowIdIstanzaByFormioFileName(istanza.getIdIstanza(), formioNomeFileName, storicoEntity.getIdStoricoWorkflow());
							log.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowOnRepositoryFileByWf] idIstanza=" + istanza.getIdIstanza() + " fileName=" + formioNomeFileName + " sono aggiornati " + numRecordAggiornati + " records.");
						}

					default:
						log.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowOnRepositoryFileByWf] Azione non prevede l'uso di RepositoryFile !");
				}
				
				// gestione generica dati azione che contenga allegati da pubblicare su mydocs							
				updateIdStoricoWorkflowFilesMydocs(azione,workflow,istanza,storicoEntity);
			}
			else if (azione != null && istanza != null) {
				switch (workflow.getCodiceAzione()) {
					case "CREA_RISPOSTA_CON_PROTOCOLLO":
						int numRecordAggiornati = repositoryFileDAO.updateIdStoricoWorkflowByIdFile(istanza.getIdIstanza(),
								storicoEntity.getIdFileRendering(), storicoEntity.getIdStoricoWorkflow());
						log.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowOnRepositoryFileByWf] idIstanza="
								+ istanza.getIdIstanza() + " idFile=" + storicoEntity.getIdFileRendering()
								+ " sono aggiornati " + numRecordAggiornati + " records.");
						break;
					default:
						log.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowOnRepositoryFileByWf] Azione non prevede l'uso di RepositoryFile !");
				}

			}
		} catch (BusinessException e) {
			log.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowOnRepositoryFileByWf] BusinessException " + e.getMessage());
		}
	}

	private void updateIdStoricoWorkflowFilesMydocs(Azione azione, Workflow workflow, Istanza istanza, StoricoWorkflowEntity storicoEntity) {
		log.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowFilesMydocs] assegnazione IdStoricoWorkflow su RepositoryFile per files in pubblicazione su mydocs");
		MyDocsActionEntity myDocsActionEntity = DatiAzioneHelper.parseMyDocsAttachmentsAzione(azione.getDatiAzione());
		List<String> formIoFileNames = myDocsActionEntity.getFormIoFileNames();
		for (String formioNomeFileName : formIoFileNames) {
			int numRecordAggiornati = repositoryFileDAO.updateIdStoricoWorkflowIdIstanzaByFormioFileName(istanza.getIdIstanza(), formioNomeFileName, storicoEntity.getIdStoricoWorkflow());
			log.info("[" + CLASS_NAME + "::updateIdStoricoWorkflowFilesMydocs] idIstanza=" + istanza.getIdIstanza() + " fileName=" + formioNomeFileName + " sono aggiornati " + numRecordAggiornati + " records.");
		}		
	}

	private void updateDatiRifiutata(UserInfo user,Workflow workflow, Azione azione, StoricoWorkflowEntity storicoEntity) {

		Istanza istanza = istanzeService.getIstanzaById(user, azione.getIdIstanza());
		
		if ("CONT_COSTR".equals(istanza.getModulo().getCodiceModulo())) {
			
			WorkflowEntity entity = workflowDAO.findById(azione.getIdWorkflow());
			
			if ("S".equals(entity.getFlagStatoIstanza())) 
			{														
				OneriCostrDomandaEntity oneriCostrDomanda = oneriCostrDAO.findByIdIstanza(istanza.getIdIstanza());									
				oneriCostrDomanda.setBoEsitoControlli("RIFIUTATA");
				oneriCostrDomanda.setBoOperatore(user.getIdentificativoUtente());
				oneriCostrDomanda.setBoImportoPagato(0L);
				oneriCostrDAO.update(oneriCostrDomanda);
			}			
		}
	}

	private void updateFlagArchiviata(UserInfo user, Long idIstanza) throws Exception {
		IstanzaEntity eIstanza = istanzaDAO.findById(idIstanza);
		eIstanza.setAttoreUpd(user.getIdentificativoUtente());
		eIstanza.setFlagArchiviata("S");
		istanzaDAO.update(eIstanza);
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

	private void protocolla(Long idIstanza, Azione azione) throws BusinessException, DAOException {
		DatiIstanzaBoHelper datiIstanzaBoHelper = new DatiIstanzaBoHelper();
		String numeroProtocollo = "";
		Date dataProtocollo = null;
		try {
			WfProtocolloEdil getProtocolloEdil = datiIstanzaBoHelper.getProtocolloEdil(azione.getDatiAzione());
			if (getProtocolloEdil != null) 
			{
				numeroProtocollo = getProtocolloEdil.getNumeroProtocollo();
				String strDataProtocollo = getProtocolloEdil.getDataProtocollo().substring(0, 10);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				dataProtocollo = format.parse(strDataProtocollo);
			}
			 
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::compieAzione] Errore parsing dati protocollo - ",e );
			throw new BusinessException("Errore parsing dati protocollo");
		}
		
		IstanzaEntity eIstanza = new IstanzaEntity();
		eIstanza.setIdIstanza(idIstanza);
		eIstanza.setNumeroProtocollo(numeroProtocollo);
		eIstanza.setDataProtocollo(dataProtocollo);
		
		istanzaDAO.updateProtocollo(eIstanza);
	}
	

//	private void saveUlteriori(UserInfo user, String codiceAzione, String datiAzione, Long idIstanza) throws DAOException, BusinessException {
//		switch(codiceAzione) {
//			case "VERIFICA_IMPORTI" :
//				updateExtOneriCostr(user, datiAzione, idIstanza);
//				break;
//			case "GESTISCI_IBAN" :
//				updateExtOneriIban(user, datiAzione);
//				break;
//		}
//	}

	@Transactional
	private void updateExtOneriCostr(UserInfo user, Azione azione, Long idIstanza) throws DAOException, BusinessException {
		OneriCostrDatiIstanzaHelper oneriCostrHelper = new OneriCostrDatiIstanzaHelper();
		try {
			// Preparazione Entity Ext 
			OneriCostrDomandaEntity entity = oneriCostrHelper.parseAzione(azione.getDatiAzione());
			entity.setIdIstanza(idIstanza);
			entity.setBoOperatore(user.getIdentificativoUtente());
			entity.setAttoreUpd(user.getIdentificativoUtente());
			int result = oneriCostrDAO.update(entity);
		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::insertExtOneriCostr] Exception"+e.getMessage());
			throw new BusinessException();
		}
	}
	
	@Transactional
	private void updateExtOneriIban(UserInfo user,  Azione azione) throws DAOException, BusinessException {
		OneriCostrDatiIstanzaHelper oneriCostrHelper = new OneriCostrDatiIstanzaHelper();
		try {
			// Preparazione Entity Ext 
			OneriCostrIbanEntity entity = oneriCostrHelper.parseIban(azione.getDatiAzione());
			entity.setAttoreUpd(user.getIdentificativoUtente());
			if (!entity.getCodIstat().equals(""))
			{
				oneriCostrIbanDAO.update(entity);
			}
		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::insertExtOneriCostr] Exception"+e.getMessage());
			throw new BusinessException();
		}
	}
	
//	@Transactional
//	private Long generaSalvaRicevutaPdf(UserInfo user, String datiAzione, Long idIstanza) throws BusinessException {
//		CambioResidenzaHelper cambioResidenzaHelper = new CambioResidenzaHelper();
//		try {
//			// Preparazione Entity  
//			IstanzaCronologiaStatiEntity cronE = istanzaDAO.findLastCronologia(idIstanza);
//			IstanzaDatiEntity datiE = istanzaDAO.findDati(idIstanza, cronE.getIdCronologiaStati());
//			CambioResidenzaEntity cambioResidenzaEntity = CambioResidenzaHelper.parse(datiE.getDatiIstanza());
//			RicevutaCambioResidEntity ricevutaEntity = CambioResidenzaHelper.parseAzione(datiAzione);
//			
//			// genero il file pdf
//			PrintRicevutaMapper_RIC_DEM_001 mapper = new PrintRicevutaMapper_RIC_DEM_001();
//			MoonprintDocument document = mapper.remap( cambioResidenzaEntity, ricevutaEntity);
//			document.setTemplate("CambioResidenzaAccetta");
//			byte[] pdf = generaPdf(document);
//			// salvo il file in moon_wf_t_storico_workflow
//			RepositoryFileEntity file = new RepositoryFileEntity();
//			file.setNomeFile("RicevutaAccettazione.pdf");
//			file.setContenuto(generaPdf(document));
//			Long idFile = repositoryFileDAO.insert(file);
//			return idFile;
//		} catch (Exception e) {
//			log.warn("[" + CLASS_NAME + "::generaRicevutaPdf] Exception"+e.getMessage());
//			throw new BusinessException();
//		}
//	}
	
//	private byte[] generaPdf(MoonprintDocument moonPrintParam) throws BusinessException {
//		try {
//			return moonprintDAO.printPdf(moonPrintParam);
//		} catch (DAOException e) {
//			log.error("[" + CLASS_NAME + "::printPdf] Errore invocazione DAO - ", e);
//			throw new BusinessException("Errore printPdf istanza moonprintDAO");
//		}
//	}
	

	public Workflow identificaAzioneWorkflowDaEseguireDopoInviata(Long idModulo) throws ItemNotFoundBusinessException, BusinessException {
		try {
			WorkflowFilter filter = new WorkflowFilter();
			filter.setIdModulo(idModulo);
			filter.setIdStatoWfPartenza(DecodificaStatoIstanza.INVIATA.getIdStatoWf());
			filter.setEscludiAzioniUtenteCompilante(true);
			//List<Workflow> elenco = workflowService.getElencoAzioniPossibili(user, istanzaSaved.getIdIstanza(), filter); // Possible effetto indesiderabile se fosse anche con flag_automatico, verrebbe esseguita 2 volte
			List<WorkflowEntity> elenco = workflowDAO.find(filter);
			if (elenco==null || elenco.isEmpty()) {
				log.error("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguireDopoInviata] NESSUN Workflow Azioni possibile per idModulo:" + 
						idModulo + " idStatoPartenza:" + DecodificaStatoIstanza.INVIATA.getIdStatoWf());
				throw new ItemNotFoundBusinessException("NESSUN Workflow Azioni");
			}
			if (elenco.size()>1) {
				log.error("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguireDopoInviata] "+elenco.size()+" is more than one Workflow Azioni possibile per idModulo:" + 
						idModulo + " idStatoPartenza:" + DecodificaStatoIstanza.INVIATA.getIdStatoWf());
				throw new ItemNotFoundBusinessException("More than one Workflow Azioni");
			}
			Workflow result = WorkflowMapper.buildFromWorkflowEntity(elenco.get(0));
			log.info("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguireDopoInviata] result = " + result);
			return result;
		} catch (DAOException e) {
			log.warn("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguireDopoInviata] DAOException per idModulo " + idModulo);
			throw new BusinessException();
		}
	}
	
	//
	// CRUD DettaglioProcesso
	@Override
	public List<Workflow> getWorkflowsByIdProcesso(Long idProcesso) throws BusinessException {
		try {
			WorkflowFilter filter = new WorkflowFilter.Builder().idProcesso(idProcesso).build();
			List<WorkflowEntity> elencoWorkflow = workflowDAO.find(filter);
			return elencoWorkflow.stream()
				.map(WorkflowMapper::buildFromWorkflowEntity)
				.collect(Collectors.toList());
		} catch (DAOException daoe) {
			log.warn("[" + CLASS_NAME + "::getWorkflowsByIdProcesso] Errore invocazione DAO idProcesso=" + idProcesso + " - " + daoe.getMessage());
			throw new BusinessException("Errore recupero elenco getWorkflowsByIdProcesso");
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::getWorkflowsByIdProcesso] Exception idProcesso=" + idProcesso, ex);
			throw new BusinessException("Errore deleteWorkflow");
 		}
	}
	
	@Override
	public Workflow saveWorkflow(UserInfo user, Workflow workflow) throws BusinessException {
		try {
			WorkflowEntity entity = WorkflowMapper.buildFromObj(workflow);
			if (entity.getIdWorkflow()==null) {
				Long idWorkflow = workflowDAO.insert(entity);
				entity.setIdWorkflow(idWorkflow);
				log.debug("[" + CLASS_NAME + "::saveWorkflow] workflowDAO.insert idWorkflow=" + idWorkflow);
			} else {
				int rows = workflowDAO.update(entity);
				log.debug("[" + CLASS_NAME + "::saveWorkflow] workflowDAO.update rows=" + rows);
			}
			return WorkflowMapper.buildFromWorkflowEntity(entity);
		} catch (DAOException daoe) {
			log.warn("[" + CLASS_NAME + "::saveWorkflow] DAOException workflow=" + workflow + " - " + daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::saveWorkflow] Exception workflow=" + workflow, ex);
			throw new BusinessException("Errore saveWorkflow");
		} 
	}
	
	@Override
	public Workflow patchWorkflow(UserInfo user, Long idWorkflow, Workflow partialWorkflow) throws BusinessException {
		try {
			WorkflowEntity entity = workflowDAO.findById(idWorkflow);
			if (partialWorkflow.getIdStatoWfPartenza()!=null) {
				entity.setIdStatoWfPartenza(partialWorkflow.getIdStatoWfPartenza());
			}
			if (partialWorkflow.getIdStatoWfArrivo()!=null) {
				entity.setIdStatoWfArrivo(partialWorkflow.getIdStatoWfArrivo());
			}
			if (partialWorkflow.getIdAzione()!=null) {
				entity.setIdAzione(partialWorkflow.getIdAzione());
			}
//			entity.setDataUpd(new Date());
//			entity.setAttoreUpd(user.getIdentificativoUtente());
	        // Salva
			workflowDAO.update(entity);
			
			// Rilegge l utente
			Workflow result = getWorkflowById(idWorkflow);
			return result;
		} catch (DAOException daoe) {
			log.warn("[" + CLASS_NAME + "::patchWorkflow] DAOException partialWorkflow=" + partialWorkflow + " - " + daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::patchWorkflow] Exception partialWorkflow=" + partialWorkflow, ex);
			throw new BusinessException("Errore patchWorkflow");
		} 
	}
	
	@Override
	public void deleteWorkflow(UserInfo user, Long idWorkflow) throws ItemNotFoundBusinessException, BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::deleteWorkflow] IN idWorkflow: " + idWorkflow);
			workflowDAO.delete(idWorkflow);
		} catch (DAOException daoe) {
			log.warn("[" + CLASS_NAME + "::deleteWorkflow] DAOException idWorkflow=" + idWorkflow + " - " + daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (Exception ex) {
			log.error("[" + CLASS_NAME + "::deleteWorkflow] Exception idWorkflow=" + idWorkflow, ex);
			throw new BusinessException("Errore deleteWorkflow");
		} 
	}

}

