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

import it.csi.moon.moonbobl.business.service.EntiService;
import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.EnteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.EntiFilter;
import it.csi.moon.moonbobl.business.service.mapper.EnteMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Ente;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

/**
 * Metodi di business relativi degli enti
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class EntiServiceImpl implements EntiService {
	
	private final static String CLASS_NAME = "EntiServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	public EntiServiceImpl() {
	}

	@Autowired
	EnteDAO enteDAO;
	
	@Override
	public List<Ente> getElencoEnti(EntiFilter filter) throws BusinessException {
		try {
			List<Ente> result = enteDAO.find(filter).stream()
				.map(EnteMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoEnti] Errore generico servizio getElencoEnti", ex);
			throw new ServiceException("Errore generico servizio elenco Enti");
		} 
	}

	@Override
	public Ente getEnteById(Long idEnte) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getEnteById] IN idEnte=" + idEnte);
		}
		Ente result = null;
		try {
			EnteEntity entity = enteDAO.findById(idEnte);
			result = EnteMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getEnteById] Errore invocazione DAO ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::getEnteById] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getEnteById] END result=" + result);
			}
		}
	}

	@Override
	public Ente getEnteByCodice(String codiceEnte) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getEnteByCodice] IN codiceEnte=" + codiceEnte);
		}
		Ente result = null;
		try {
			EnteEntity entity = enteDAO.findByCodice(codiceEnte);
			result = EnteMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getEnteByCodice] Errore invocazione DAO ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::getEnteByCodice] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getEnteByCodice] END result=" + result);
			}
		}
	}
	
	@Override
	public Ente createEnte(Ente body) throws BusinessException {
		log.error("[" + CLASS_NAME + "::createEnte] UNIMPLEMENTED METHOD !");
		throw new BusinessException("UNIMPLEMENTED METHOD");
	}

	@Override
	public Ente updateEnte(Long idEnte, Ente body) throws BusinessException {
		log.error("[" + CLASS_NAME + "::updateEnte] UNIMPLEMENTED METHOD !");
		throw new BusinessException("UNIMPLEMENTED METHOD");
	}

}