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

import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.Portale;
import it.csi.moon.commons.entity.PortaleEntity;
import it.csi.moon.commons.mapper.PortaleMapper;
import it.csi.moon.moonsrv.business.service.PortaliService;
import it.csi.moon.moonsrv.business.service.impl.dao.PortaleDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
/**
 * Metodi di business relativi alle portali
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class PortaliServiceImpl  implements PortaliService {
	
	private static final String CLASS_NAME = "PortaliServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	PortaleDAO portaleDAO;
	
	@Override
	public List<Portale> getElencoPortali() {
		try {
			return portaleDAO.find().stream()
				.map(PortaleMapper::buildFromEntity)
				.collect(Collectors.toList());
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoPortali] Errore generico servizio getElencoPortali",ex);
			throw new ServiceException("Errore generico servizio elenco Portali");
		} 
	}

	@Override
	public Portale getPortaleById(Long idPortale) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getPortaleById] BEGIN IN idPortale="+idPortale);
		}
		Portale result = null;
		try {
			PortaleEntity entity = portaleDAO.findById(idPortale);
			result = PortaleMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPortaleById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getPortaleById] Errore invocazione DAO - ", daoe);
			throw new BusinessException(daoe);
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getPortaleById] END result="+result);
			}
		}
	}

	@Override
	public Portale getPortaleByCd(String codicePortale) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getPortaleByCd] BEGIN IN codicePortale="+codicePortale);
		}
		Portale result = null;
		try {
			PortaleEntity entity = portaleDAO.findByCd(codicePortale);
			result = PortaleMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByCd] ItemNotFoundDAOException byNome " + codicePortale);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByCd] DAOException byNome " + codicePortale, daoe);
			throw new BusinessException(daoe);
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getPortaleByCd] END result="+result);
			}
		}
	}

	@Override
	public Portale getPortaleByNome(String nomePortale) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getPortaleByNome] BEGIN IN nomePortale="+nomePortale);
		}
		Portale result = null;
		try {
			PortaleEntity entity = portaleDAO.findByNome(nomePortale);
			result = PortaleMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByNome] ItemNotFoundDAOException byNome " + nomePortale);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByNome] DAOException byNome " + nomePortale, daoe);
			throw new BusinessException(daoe);
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getPortaleByNome] END result="+result);
			}
		}
	}
	
	@Override
	public Portale createPortale(Portale body) {
		return null;
	}

	@Override
	public Portale updatePortale(Long idPortale, Portale body) {
		return null;
	}

	
	//
	// Moduli
	//
	@Override
	public List<Modulo> getModuliByIdPortale(Long idPortale) {
		return null;
	}


	//
	// Enti
	//
	@Override
	public List<Ente> getEntiByIdPortale(Long idPortale) {
		return null;
	}

}