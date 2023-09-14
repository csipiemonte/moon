/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.business.be.CurrentUserApi;
import it.csi.moon.moonfobl.business.service.BackendService;
import it.csi.moon.moonfobl.dto.moonfobl.UserChangeRequest;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class CurrentUserApiImpl extends MoonBaseApiImpl implements CurrentUserApi {
	
	private static final String CLASS_NAME = "CurrentUserApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	public BackendService beService;
	
	@Override
	public Response getCurrentUser(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo user = retrieveUserInfo(httpRequest);
		return Response.ok(user).build();
	}

	/**
	 * Aggiornamento Ente corrente dell'utente
	 */
	@Override
	public Response patchCurrentUser( UserChangeRequest userChangeRequest,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ResourceNotFoundException, ServiceException {
		try {
			if (userChangeRequest==null || userChangeRequest.getEnte()==null || userChangeRequest.getEnte().getIdEnte()==null) {
				throw new UnprocessableEntityException("Inserire un payload corretto con ente.idEnte valido.");
			}
//			UserInfo user = retrieveUserInfo(httpRequest);
			UserInfo currentUser = beService.aggiornaCurrentUser(httpRequest, userChangeRequest);
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, currentUser.getIdMoonToken());
			return Response.ok(currentUser).header(CLASS_NAME, currentUser) .build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::patchCurrentUser] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::patchCurrentUser] Errore generico servizio patchCurrentUser",ex);
			throw new ServiceException("Errore generico servizio patchCurrentUser");
		}
	}
  
}
