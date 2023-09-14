/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.CmdApi;
import it.csi.moon.moonbobl.business.service.BackendService;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.dto.moonfobl.BuildInfo;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class CmdApiImpl implements CmdApi {
	
	private final static String CLASS_NAME = "CmdApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	public BackendService beService;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	public Response ping(SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			log.debug("[" + CLASS_NAME + "::ping] BEGIN");
			String response = beService.getMessage();
			return Response.ok(response).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::ping] Errore generico servizio ping",ex);
			throw new ServiceException("Errore generico servizio ping");
		} 
	}
	
	public Response pingMoonsrv(SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			log.debug("[" + CLASS_NAME + "::pingService] BEGIN");
			String response = moonsrvDAO.pingMoonsrv();
			return Response.ok(response).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::pingService] Errore generico servizio pingService",ex);
			throw new ServiceException("Errore generico servizio pingService");
		} 
	}
	
	public Response version(SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			log.debug("[" + CLASS_NAME + "::version] BEGIN");
			String response = beService.getVersion();
			return Response.ok(response).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::version] Errore generico servizio version",ex);
			throw new ServiceException("Errore generico servizio version");
		} 
	}
	public Response buildInfo(SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			log.debug("[" + CLASS_NAME + "::buildInfo] BEGIN");
			BuildInfo response = beService.getBuildInfo();
			return Response.ok(response).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::buildInfo] Errore generico servizio buildInfo",ex);
			throw new ServiceException("Errore generico servizio buildInfo");
		} 
	}
}
