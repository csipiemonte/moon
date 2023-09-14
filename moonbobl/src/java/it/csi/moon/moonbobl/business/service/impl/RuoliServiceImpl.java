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

import it.csi.moon.moonbobl.business.service.RuoliService;
import it.csi.moon.moonbobl.business.service.impl.dao.RuoloDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.RuoloEntity;
import it.csi.moon.moonbobl.business.service.mapper.RuoloMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Ruolo;
import it.csi.moon.moonbobl.dto.moonfobl.Utente;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Metodi di business relativi alle ruoli
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class RuoliServiceImpl  implements RuoliService {
	
	private final static String CLASS_NAME = "RuoliServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	public RuoliServiceImpl() {
	}

	@Autowired
	RuoloDAO ruoloDAO;
	
	@Override
	public List<Ruolo> getElencoRuoli() throws BusinessException {
		try {
			List<Ruolo> result = ruoloDAO.find().stream()
					.map(RuoloMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoRuoli] Errore generico servizio getElencoRuoli",ex);
			throw new ServiceException("Errore generico servizio elenco Ruoli");
		} 
	}

	@Override
	public Ruolo getRuoloById(Integer idRuolo) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getRuoloById] BEGIN IN idRuolo="+idRuolo);
		}
		Ruolo result = null;
		try {
			RuoloEntity entity = ruoloDAO.findById(idRuolo);
			result = RuoloMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getRuoloById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getRuoloById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getRuoloById] END result="+result);
			}
		}
	}

	@Override
	public Ruolo createRuolo(Ruolo body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ruolo updateRuolo(Integer idRuolo, Ruolo body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	
	//
	// Utenti
	//
	@Override
	public List<Utente> getUtentiByIdRuolo(Integer idRuolo) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}


}