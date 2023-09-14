/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.CurrentUserApi;
import it.csi.moon.moonbobl.business.service.BackendService;
import it.csi.moon.moonbobl.dto.moonfobl.UserChangeRequest;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.HttpRequestUtils;
import it.csi.moon.moonbobl.util.LoggerAccessor;


@Component
public class CurrentUserApiImpl implements CurrentUserApi {
	
	private final static String CLASS_NAME = "CurrentUserApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	public BackendService beService;
	
	public Response getCurrentUser(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest req ) {
		UserInfo currentUser = beService.getCurrentUser(req);
		log.info("[" + CLASS_NAME + "::getCurrentUser] Accesso eseguito con utente " + currentUser);
		return Response.ok(currentUser).build();
	}
	
	/**
	 * Aggiornamento Ente corrente dell'utente
	 */
	@Override
	public Response patchCurrentUser( UserChangeRequest userChangeRequest,
			SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ResourceNotFoundException, ServiceException {
		try {
			if (userChangeRequest==null || userChangeRequest.getEnte()==null || userChangeRequest.getEnte().getIdEnte()==null) {
				new UnprocessableEntityException("Inserire un payload corretto con ente.idEnte valido.");
			}
			UserInfo currentUser = beService.aggiornaCurrentUser(httpRequest, userChangeRequest);
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, currentUser.getIdMoonToken());
			return Response.ok(currentUser).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::patchCurrentUser] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::patchCurrentUser] Errore generico servizio patchCurrentUser",ex);
			throw new ServiceException("Errore generico servizio patchCurrentUser");
		}
	}
  	
  
}
