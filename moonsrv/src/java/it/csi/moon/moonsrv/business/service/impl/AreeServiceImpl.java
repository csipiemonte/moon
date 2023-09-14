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

import it.csi.moon.commons.dto.Area;
import it.csi.moon.commons.entity.AreaEntity;
import it.csi.moon.commons.entity.AreeFilter;
import it.csi.moon.commons.mapper.AreaMapper;
import it.csi.moon.moonsrv.business.service.AreeService;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi degli aree
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class AreeServiceImpl implements AreeService {
	
	private static final String CLASS_NAME = "AreeServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	AreaDAO areaDAO;
	
	@Override
	public List<Area> getElencoAree(AreeFilter filter) throws BusinessException {
		try {
			List<Area> result = areaDAO.find(filter).stream()
				.map(AreaMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoAree] Errore generico servizio getElencoAree", ex);
			throw new ServiceException("Errore generico servizio elenco Aree");
		} 
	}
	
	@Override
	public Area getAreaById(Long idArea) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getAreaById] IN idArea=" + idArea);
		}
		Area result = null;
		try {
			AreaEntity entity = areaDAO.findById(idArea);
			result = AreaMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getAreaById] Errore invocazione DAO ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getAreaById] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getAreaById] END result=" + result);
			}
		}
	}

	@Override
	public Area getAreaByIdEnteCodice(Long idEnte, String codiceArea) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getAreaByIdEnteCodice] IN idEnte=" + idEnte + " codiceArea=" + codiceArea);
		}
		Area result = null;
		try {
			AreaEntity entity = areaDAO.findByCd(idEnte, codiceArea);
			result = AreaMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getAreaByIdEnteCodice] Errore invocazione DAO ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getAreaByIdEnteCodice] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getAreaByIdEnteCodice] END result=" + result);
			}
		}
	}

	
	@Override
	public Area createArea(Area body) throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::createArea] UNIMPLEMENTED METHOD !");
		throw new BusinessException("UNIMPLEMENTED METHOD");
	}

	@Override
	public Area updateArea(Long idArea, Area body) throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::updateArea] UNIMPLEMENTED METHOD !");
		throw new BusinessException("UNIMPLEMENTED METHOD");
	}

}