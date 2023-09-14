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

import it.csi.moon.moonbobl.business.service.FunzioniService;
import it.csi.moon.moonbobl.business.service.impl.dao.FunzioneDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.FunzioneEntity;
import it.csi.moon.moonbobl.business.service.mapper.FunzioneMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Funzione;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

/**
 * Metodi di business relativi alle funzioni
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class FunzioniServiceImpl implements FunzioniService {
	
	private final static String CLASS_NAME = "FunzioniServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	FunzioneDAO funzioneDAO;

	@Override
	public List<Funzione> getElencoFunzioni() throws BusinessException {
		try {
			List<Funzione> result = funzioneDAO.find().stream()
					.map(FunzioneMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoFunzioni] Errore generico servizio getElencoFunzioni",ex);
			throw new ServiceException("Errore generico servizio elenco Funzioni");
		} 
	}

	@Override
	public Funzione getFunzioneById(Integer idFunzione) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getFunzioneById] BEGIN IN idFunzione="+idFunzione);
		}
		Funzione result = null;
		try {
			FunzioneEntity entity = funzioneDAO.findById(idFunzione);
			result = FunzioneMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getFunzioneById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getFunzioneById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getFunzioneById] END result="+result);
			}
		}
	}

	@Override
	public Funzione createFunzione(Funzione body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Funzione updateFunzione(Integer idFunzione, Funzione body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
}
