/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.ProcessiService;
import it.csi.moon.moonbobl.business.service.WorkflowService;
import it.csi.moon.moonbobl.business.service.impl.dao.ProcessoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ProcessoModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ProcessoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowFilter;
import it.csi.moon.moonbobl.business.service.impl.helper.Processo2ImageHelper;
import it.csi.moon.moonbobl.business.service.mapper.ProcessoMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.Processo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
/**
 * Metodi di business relativi ai processi
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ProcessiServiceImpl implements ProcessiService {
	
	private static final String FIELD_WORKFLOWS = "workflows";
	private final static String CLASS_NAME = "ProcessiServiceImpl";
	private Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	public ProcessiServiceImpl() {
	}

	@Autowired
	ProcessoDAO processoDAO;
	@Autowired
	ProcessoModuloDAO processoModuloDAO;
	@Autowired
	WorkflowDAO workflowDAO;
	@Autowired
	WorkflowService workflowService;
	
	@Override
	public List<Processo> getElencoProcessi() throws BusinessException {
		try {
			return processoDAO.find().stream()
				.map(ProcessoMapper::buildFromEntity)
				.collect(Collectors.toList());
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoProcessi] Errore generico servizio getElencoProcessi",ex);
			throw new ServiceException("Errore generico servizio elenco Processi");
		} 
	}

	@Override
	public Processo getProcessoById(Long idProcesso) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getProcessoById] BEGIN IN idProcesso="+idProcesso);
		}
		Processo result = null;
		try {
			ProcessoEntity entity = processoDAO.findById(idProcesso);
			result = ProcessoMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getProcessoById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getProcessoById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getProcessoById] END result="+result);
			}
		}
	}

	@Override
	public Processo getProcessoById(Long idProcesso, String fields) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getProcessoById] BEGIN IN idProcesso="+idProcesso);
			LOG.debug("[" + CLASS_NAME + "::getProcessoById] BEGIN IN fields="+fields);
		}
		Processo result = null;
		try {
			result = getProcessoById(idProcesso);
			result = completeRequestFields(fields, result);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getProcessoById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getProcessoById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getProcessoById] END result="+result);
			}
		}
	}
	
	private Processo completeRequestFields(String fields, Processo result) {
		if (StringUtils.isEmpty(fields)) {
			return result;
		}
		List<String> listFields = Arrays.asList(fields.split(","));

		if (listFields.contains(FIELD_WORKFLOWS)) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] " + FIELD_WORKFLOWS);
			result = completaProcessoWorkflows(result);
		}
		return result;
	}

	/**
	 * Completa l'oggetto Processo delle righe di workflows
	 * @param processo
	 * @return processo completato
	 * @throws DAOException
	 */
	private Processo completaProcessoWorkflows(Processo result) {
		try {
//			List<Workflow> workflows = workflowService.getElencoAzioniPossibili(
			List<Workflow> workflows = workflowService.getElencoWorkflow(
				WorkflowFilter.builder()
					.idProcesso(result.getIdProcesso())
					.escludiAzioniDiSistema(false)
					.build()
				, WorkflowService.FIELD_STATO_PARTENZA + "," + WorkflowService.FIELD_STATO_ARRIVO);
			result.setWorkflows(workflows);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::completaProcessoWorkflows] ", e);
			throw new BusinessException("Errore completaProcessoWorkflows");
		}
		return result;
	}
	
	@Override
	public Processo getProcessoByCd(String codiceProcesso) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getProcessoByCd] BEGIN IN codiceProcesso="+codiceProcesso);
		}
		Processo result = null;
		try {
			ProcessoEntity entity = processoDAO.findByCd(codiceProcesso);
			result = ProcessoMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByCd] ItemNotFoundDAOException byNome " + codiceProcesso);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByCd] DAOException byNome " + codiceProcesso, e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getProcessoByCd] END result="+result);
			}
		}
	}

	@Override
	public Processo getProcessoByNome(String nomeProcesso) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getProcessoByNome] BEGIN IN nomeProcesso="+nomeProcesso);
		}
		Processo result = null;
		try {
			ProcessoEntity entity = processoDAO.findByNome(nomeProcesso);
			result = ProcessoMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByNome] ItemNotFoundDAOException byNome " + nomeProcesso);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByNome] DAOException byNome " + nomeProcesso, e);
			throw new BusinessException("Errore ricerca Processo per nome");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getProcessoByNome] END result="+result);
			}
		}
	}
	
	@Override
	public Processo createProcesso(UserInfo user, Processo processo) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::createProcesso] IN processo: " + processo);
			ProcessoEntity entity = ProcessoMapper.buildFromObj(processo);
			entity.setFlagAttivo("S");
			entity.setAttoreUpd(user.getIdentificativoUtente());
			entity.setDataUpd(new Date());
	        Long idProcesso = processoDAO.insert(entity);
	        processo.setIdProcesso(idProcesso);
			return processo;
		} catch (DAOException dao) {
			LOG.warn("[" + CLASS_NAME + "::createProcesso] Errore servizio createProcesso " + dao.getMessage());
			throw new BusinessException(dao);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createProcesso] Errore servizio createProcesso", be);
			throw be;
		}
	}

	@Override
	public Processo updateProcesso(UserInfo user, Processo processo) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateProcesso] IN processo: " + processo);
			ProcessoEntity entity = ProcessoMapper.buildFromObj(processo);
			entity.setAttoreUpd(user.getIdentificativoUtente());
			entity.setDataUpd(new Date());
	        processoDAO.update(entity);
	        processo.setAttoreUpd(entity.getAttoreUpd());
	        processo.setDataUpd(entity.getDataUpd());
			return processo;
		} catch (DAOException dao) {
			LOG.warn("[" + CLASS_NAME + "::updateProcesso] Errore servizio updateProcesso " + dao.getMessage());
			throw new BusinessException(dao);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::updateProcesso] Errore servizio updateProcesso", be);
			throw be;
		}
	}

	
	//
	// Moduli
	//
	@Override
	public Processo getProcessoByIdModulo(Long idModulo) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getProcessoByIdModulo] BEGIN IN idModulo="+idModulo);
		}
		Processo result = null;
		try {
			List<ProcessoEntity> processi = processoDAO.findByIdModulo(idModulo);
			if (processi!=null && !processi.isEmpty()) {
				result = ProcessoMapper.buildFromEntity(processi.get(0));
			} else {
				LOG.warn("[" + CLASS_NAME + "::getProcessoByIdModulo] NESSUN Processo per idModulo " + idModulo);
			}
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByIdModulo] ItemNotFoundDAOException idModulo " + idModulo);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByIdModulo] DAOException idModulo " + idModulo, e);
			throw new BusinessException("Errore ricerca Processo per nome");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getProcessoByIdModulo] END result="+result);
			}
		}
	}
	
	@Override
	public List<Modulo> getModuliByIdProcesso(Long idProcesso) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getModuliByIdProcesso] NOT YET IMPLEMENTED");
		throw new BusinessException("NOT YET IMPLEMENTED");
	}

	//
	// Image
	@Override
	public byte[] getByteArrayImageById(Long idProcesso) throws BusinessException {
		try {
			Processo processoCompleto = getProcessoById(idProcesso, FIELD_WORKFLOWS);
			LOG.debug("[" + CLASS_NAME + "::getByteArrayImageById] processoCompleto = " + processoCompleto);
			return new Processo2ImageHelper().makeStateDiagram(processoCompleto);
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::getByteArrayImageById] DAOException for idProcesso = " + idProcesso);
			throw new BusinessException("Errore ricerca Workflows per idProcesso");
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getByteArrayImageById] BusinessException for idProcesso = " + idProcesso);
			throw be;
		}
	}
	
}