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

import it.csi.moon.moonbobl.business.service.ProtocolloMetadatoService;
import it.csi.moon.moonbobl.business.service.impl.dao.ProtocolloMetadatoDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ProtocolloMetadatoEntity;
import it.csi.moon.moonbobl.business.service.mapper.ProtocolloMetadatoMapper;
import it.csi.moon.moonbobl.dto.moonfobl.ProtocolloMetadato;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
/**
 * Metodi di business relativi ai metadati del protocollo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ProtocolloMetadatoServiceImpl implements ProtocolloMetadatoService {
	
	private final static String CLASS_NAME = "ProtocolloMetadatoServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	public ProtocolloMetadatoServiceImpl() {
	}

	@Autowired
	ProtocolloMetadatoDAO protocolloMetadatoDAO;
	
	@Override
	public List<ProtocolloMetadato> getElenco() throws BusinessException {
		try {
			List<ProtocolloMetadato> result = protocolloMetadatoDAO.findAll().stream()
				.map(ProtocolloMetadatoMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElenco] Errore generico servizio getElenco",ex);
			throw new ServiceException("Errore generico servizio elenco ProtocolloMetadato");
		} 
	}
	
	@Override
	public ProtocolloMetadato getById(Long idMetadato) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getById] BEGIN IN idMetadato="+idMetadato);
		}
		ProtocolloMetadato result = null;
		try {
			ProtocolloMetadatoEntity entity = protocolloMetadatoDAO.findById(idMetadato);
			result = ProtocolloMetadatoMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getById] Errore invocazione DAO ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getById] Errore invocazione DAO ", e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getById] END result="+result);
			}
		}
	}

	@Override
	public ProtocolloMetadato create(ProtocolloMetadato body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProtocolloMetadato update(Long idMetadato, ProtocolloMetadato body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}