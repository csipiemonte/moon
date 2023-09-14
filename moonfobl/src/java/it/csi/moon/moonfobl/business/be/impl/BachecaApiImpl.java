/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.business.be.BachecaApi;
import it.csi.moon.moonfobl.business.service.BachecaService;
import it.csi.moon.moonfobl.dto.moonfobl.MessaggioBacheca;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class BachecaApiImpl extends MoonBaseApiImpl implements BachecaApi {
	
	private static final String CLASS_NAME = "BachecaApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	//@Autowired
	//public BackendService beService;
	
	@Autowired
	public BachecaService bachecaService;
	
	@Override
	public Response getElencoMessaggi(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	
		try {
			LOG.debug("[" + CLASS_NAME + "::getElencoSupporto] BEGIN");
			
			UserInfo user = retrieveUserInfo(httpRequest);
	
			List<MessaggioBacheca> elenco = bachecaService.getElencoMessaggiBacheca(user);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoMessaggi] Errore servizio getElencoMessaggi",e);
			throw new ServiceException("Errore servizio elenco richieste supporto");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoMessaggi] Errore generico servizio getElencoMessaggi",ex);
			throw new ServiceException("Errore generico servizio elenco messaggi bacheca");
		}  

	}

	
}
