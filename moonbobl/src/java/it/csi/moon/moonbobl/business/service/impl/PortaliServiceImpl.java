/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.PortaliService;
import it.csi.moon.moonbobl.business.service.impl.dao.PortaleDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.PortaleEntity;
import it.csi.moon.moonbobl.business.service.mapper.PortaleMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.Portale;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
/**
 * Metodi di business relativi alle portali
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class PortaliServiceImpl  implements PortaliService {
	
	private final static String CLASS_NAME = "PortaliServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	public PortaliServiceImpl() {
	}

	@Autowired
	PortaleDAO portaleDAO;
	
	@Override
	public List<Portale> getElencoPortali() throws BusinessException {
		try {
			return portaleDAO.find().stream()
				.map(PortaleMapper::buildFromEntity)
				.collect(Collectors.toList());
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoPortali] Errore generico servizio getElencoPortali",ex);
			throw new ServiceException("Errore generico servizio elenco Portali");
		} 
	}

	@Override
	public Portale getPortaleById(Long idPortale) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getPortaleById] BEGIN IN idPortale="+idPortale);
		}
		Portale result = null;
		try {
			PortaleEntity entity = portaleDAO.findById(idPortale);
			result = PortaleMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getPortaleById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getPortaleById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getPortaleById] END result="+result);
			}
		}
	}

	@Override
	public Portale getPortaleByCd(String codicePortale) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getPortaleByCd] BEGIN IN codicePortale="+codicePortale);
		}
		Portale result = null;
		try {
			PortaleEntity entity = portaleDAO.findByCd(codicePortale);
			result = PortaleMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getPortaleByCd] ItemNotFoundDAOException byNome " + codicePortale);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getPortaleByCd] DAOException byNome " + codicePortale, e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getPortaleByCd] END result="+result);
			}
		}
	}

	@Override
	public Portale getPortaleByNome(String nomePortale) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getPortaleByNome] BEGIN IN nomePortale="+nomePortale);
		}
		Portale result = null;
		try {
			PortaleEntity entity = portaleDAO.findByNome(nomePortale);
			result = PortaleMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getPortaleByNome] ItemNotFoundDAOException byNome " + nomePortale);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getPortaleByNome] DAOException byNome " + nomePortale, e);
			throw new BusinessException("Errore ricerca portale per nome");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getPortaleByNome] END result="+result);
			}
		}
	}
	
	@Override
	public Portale createPortale(Portale body) throws BusinessException {
		log.debug("[" + CLASS_NAME + "::createPortale] NOT YET IMPLEMENTED");
		throw new BusinessException("NOT YET IMPLEMENTED");
	}

	@Override
	public Portale updatePortale(Long idPortale, Portale body) throws BusinessException {
		log.debug("[" + CLASS_NAME + "::updatePortale] NOT YET IMPLEMENTED");
		throw new BusinessException("NOT YET IMPLEMENTED");
	}

	
	//
	// Moduli
	//
	@Override
	public List<Modulo> getModuliByIdPortale(Long idPortale) throws BusinessException {
		log.debug("[" + CLASS_NAME + "::getModuliByIdPortale] NOT YET IMPLEMENTED");
		throw new BusinessException("NOT YET IMPLEMENTED");
	}


}