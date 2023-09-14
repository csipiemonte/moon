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

import it.csi.moon.moonbobl.business.service.AreeService;
import it.csi.moon.moonbobl.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AreaEntity;
import it.csi.moon.moonbobl.business.service.mapper.AreaMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Area;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
/**
 * Metodi di business relativi alle aree
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class AreeServiceImpl implements AreeService {
	
	private final static String CLASS_NAME = "AreeServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	public AreeServiceImpl() {
	}

	@Autowired
	AreaDAO areaDAO;
	
	@Override
	public List<Area> getElencoArea() throws BusinessException {
		try {
			List<Area> result = areaDAO.find().stream()
				.map(AreaMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoAree] Errore generico servizio getElencoAree",ex);
			throw new ServiceException("Errore generico servizio elenco Aree");
		} 
	}
	
	@Override
	public List<Area> getElencoAreaByIdEnte(Long idEnte) throws BusinessException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getElencoAreaByIdEnte] BEGIN IN idEnte="+idEnte);
			}
			List<Area> result = areaDAO.findByIdEnte(idEnte).stream()
				.map(AreaMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoAreaByIdEnte] Errore generico servizio getElencoAree",ex);
			throw new ServiceException("Errore generico servizio elenco Aree");
		} 
	}
	
	@Override
	public Area getAreaById(Long idArea) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getAreaById] BEGIN IN idArea="+idArea);
		}
		Area result = null;
		try {
			AreaEntity entity = areaDAO.findById(idArea);
			result = AreaMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getAreaById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getAreaById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getAreaById] END result="+result);
			}
		}
	}

	@Override
	public Area createArea(Area body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Area updateArea(Long idArea, Area body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}