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

import it.csi.moon.commons.dto.Ruolo;
import it.csi.moon.commons.dto.Utente;
import it.csi.moon.commons.entity.RuoloEntity;
import it.csi.moon.commons.mapper.RuoloMapper;
import it.csi.moon.moonsrv.business.service.RuoliService;
import it.csi.moon.moonsrv.business.service.impl.dao.RuoloDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
/**
 * Metodi di business relativi alle ruoli
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class RuoliServiceImpl implements RuoliService {
	
	private static final String CLASS_NAME = "RuoliServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	RuoloDAO ruoloDAO;
	
	@Override
	public List<Ruolo> getElencoRuoli() {
		try {
			List<Ruolo> result = ruoloDAO.find().stream()
					.map(RuoloMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoRuoli] Errore generico servizio getElencoRuoli",ex);
			throw new ServiceException("Errore generico servizio elenco Ruoli");
		} 
	}

	@Override
	public Ruolo getRuoloById(Integer idRuolo) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getRuoloById] BEGIN IN idRuolo="+idRuolo);
		}
		Ruolo result = null;
		try {
			RuoloEntity entity = ruoloDAO.findById(idRuolo);
			result = RuoloMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getRuoloById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRuoloById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getRuoloById] END result="+result);
			}
		}
	}

	@Override
	public Ruolo createRuolo(Ruolo body) {
		return null;
	}

	@Override
	public Ruolo updateRuolo(Integer idRuolo, Ruolo body) {
		return null;
	}

	
	//
	// Utenti
	//
	@Override
	public List<Utente> getUtentiByIdRuolo(Integer idRuolo) {
		return null;
	}


}