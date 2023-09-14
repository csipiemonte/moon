/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.Processo;
import it.csi.moon.commons.entity.ProcessoEntity;
import it.csi.moon.commons.mapper.ProcessoMapper;
import it.csi.moon.moonsrv.business.service.ProcessiService;
import it.csi.moon.moonsrv.business.service.impl.dao.ProcessoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.ProcessoModuloDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
/**
 * Metodi di business relativi ai processi
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ProcessiServiceImpl  implements ProcessiService {
	
	private static final String CLASS_NAME = "ProcessiServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	ProcessoDAO processoDAO;
	@Autowired
	ProcessoModuloDAO processoModuloDAO;
	
	@Override
	public List<Processo> getElencoProcessi() throws BusinessException {
		try {
			return processoDAO.find().stream()
				.map(ProcessoMapper::buildFromEntity)
				.collect(Collectors.toList());
		} catch (Exception ex) {
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
	public Processo createProcesso(Processo body) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::createProcesso] NOT YET IMPLEMENTED");
		throw new BusinessException("NOT YET IMPLEMENTED");
	}

	@Override
	public Processo updateProcesso(Long idProcesso, Processo body) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::updateProcesso] NOT YET IMPLEMENTED");
		throw new BusinessException("NOT YET IMPLEMENTED");
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


}