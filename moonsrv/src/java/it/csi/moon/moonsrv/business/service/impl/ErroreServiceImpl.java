/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.ErroreEntity;
import it.csi.moon.moonsrv.business.service.ErroreService;
import it.csi.moon.moonsrv.business.service.impl.dao.ErroreDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ErroreServiceImpl implements ErroreService {
	
	private static final String CLASS_NAME = "ErroreServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ErroreDAO erroreDAO;
	
	@Override
	public void salva(ErroreEntity errore) {
		try {
//			AuditEntity auditEntity = new AuditEntity(ipAdress, 
//				retrieveUser(userInfo),
//				AuditEntity.EnumOperazione.LOGIN, 
//				"", 
//				"");
			erroreDAO.insert(errore);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::salva] DAOException", e);
		}
	}
	

	//
	@Override
	public String retrieveUser(UserInfo userInfo) {
		return userInfo!=null ? userInfo.getIdentificativoUtente() + ";" + userInfo.getCognome() + ";" + userInfo.getNome() :"NULL";
	}
	
}
