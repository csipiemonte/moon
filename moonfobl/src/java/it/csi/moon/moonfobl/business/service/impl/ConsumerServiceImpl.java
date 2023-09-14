/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 *
 */
package it.csi.moon.moonfobl.business.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonfobl.business.service.ConsumerService;
import it.csi.moon.moonfobl.business.service.impl.dao.ConsumerDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * Metodi di business relativi al Consumer
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
@Component
public class ConsumerServiceImpl implements ConsumerService {
	
	private static final String CLASS_NAME = "ConsumerServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ConsumerDAO consumerDAO;
	
	@Override
	public <P> List<P> getParameters(String consumer, String codiceEnte) throws BusinessException {
		try {
			return consumerDAO.getParameters(consumer, codiceEnte);
			
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getParameters] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero consumer parameters");
 		}
	}
	@Override
	public <P> P getParameter(String consumer, String codiceEnte, String id, String type) throws BusinessException {
		try {
			return consumerDAO.getParameter(consumer, codiceEnte,id, type);			
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getParameter] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero consumer parameter type/id = "+type+"/"+id);
 		}
	}
	



		
}
