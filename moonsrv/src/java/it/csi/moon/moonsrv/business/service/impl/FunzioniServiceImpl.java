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

import it.csi.moon.commons.dto.Funzione;
import it.csi.moon.commons.entity.FunzioneEntity;
import it.csi.moon.commons.mapper.FunzioneMapper;
import it.csi.moon.moonsrv.business.service.FunzioniService;
import it.csi.moon.moonsrv.business.service.impl.dao.FunzioneDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi alle funzioni
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class FunzioniServiceImpl implements FunzioniService {
	
	private static final String CLASS_NAME = "FunzioniServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	FunzioneDAO funzioneDAO;

	@Override
	public List<Funzione> getElencoFunzioni() throws BusinessException {
		try {
			List<Funzione> result = funzioneDAO.find().stream()
					.map(FunzioneMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoFunzioni] Errore generico servizio getElencoFunzioni",ex);
			throw new ServiceException("Errore generico servizio elenco Funzioni");
		} 
	}

	@Override
	public Funzione getFunzioneById(Integer idFunzione) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getFunzioneById] BEGIN IN idFunzione="+idFunzione);
		}
		Funzione result = null;
		try {
			FunzioneEntity entity = funzioneDAO.findById(idFunzione);
			result = FunzioneMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getFunzioneById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getFunzioneById] Errore invocazione DAO - ", daoe);
			throw new BusinessException(daoe);
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getFunzioneById] END result="+result);
			}
		}
	}

	@Override
	public Funzione createFunzione(Funzione body) throws BusinessException {
		return null;
	}

	@Override
	public Funzione updateFunzione(Integer idFunzione, Funzione body) throws BusinessException {
		return null;
	}
}
