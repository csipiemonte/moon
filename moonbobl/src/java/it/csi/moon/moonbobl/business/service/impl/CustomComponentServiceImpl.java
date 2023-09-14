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

import it.csi.moon.moonbobl.business.service.CustomComponentService;
import it.csi.moon.moonbobl.business.service.impl.dao.CustomComponentDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.CustomComponentEntity;
import it.csi.moon.moonbobl.business.service.mapper.FunzioneMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Funzione;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

/**
 * Recupero Custom component form.io
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
@Component
public class CustomComponentServiceImpl implements CustomComponentService {
	
	private final static String CLASS_NAME = "CustomComponentServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	public CustomComponentServiceImpl() {
	}

	@Autowired
	CustomComponentDAO customComponentDAO;
	

	@Override
	public CustomComponentEntity findById(String idComponent) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::findById] IN idComponent=" + idComponent);
		}
		CustomComponentEntity result = null;
		try {
			CustomComponentEntity entity = customComponentDAO.findById(idComponent);
		
			return entity;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::findById] Errore invocazione DAO ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::findById] Errore invocazione DAO ", daoe);
			throw new BusinessException(daoe);
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::findById] END result=" + result);
			}
		}
	}


	@Override
	public List<CustomComponentEntity> getCustomComponents() throws BusinessException {
		try {
			List<CustomComponentEntity> result = customComponentDAO.find();
			return result;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCustomComponents] Errore generico servizio getCustomComponents",ex);
			throw new ServiceException("Errore generico servizio getCustomComponents");
		} 
	}

}