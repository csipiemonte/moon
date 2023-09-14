/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonfobl.util.LoggerAccessor;
import it.csi.moon.commons.dto.BuildInfo;
import it.csi.moon.moonfobl.business.be.CmdApi;
import it.csi.moon.moonfobl.business.service.BackendService;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;

@Component
public class CmdApiImpl implements CmdApi {
	
	private static final String CLASS_NAME = "CmdApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	BackendService beService;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	public Response ping(SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::ping] BEGIN");
			String response = beService.getMessage();
			return Response.ok(response).build();
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::ping] Errore generico servizio ping",ex);
			throw new ServiceException("Errore generico servizio ping");
		} 
	}
	
	public Response pingMoonsrv(SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::pingService] BEGIN");
			String response = moonsrvDAO.pingMoonsrv();
			return Response.ok(response).build();
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::pingService] Errore generico servizio pingService",ex);
			throw new ServiceException("Errore generico servizio pingService");
		} 
	}
	
	public Response version(SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::version] BEGIN");
			String response = beService.getVersion();
			return Response.ok(response).build();
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::version] Errore generico servizio version",ex);
			throw new ServiceException("Errore generico servizio version");
		} 
	}

	@Override
	public Response buildInfo(SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::buildInfo] BEGIN");
			BuildInfo response = beService.getBuildInfo();
			return Response.ok(response).build();
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::buildInfo] Errore generico servizio buildInfo",ex);
			throw new ServiceException("Errore generico servizio buildInfo");
		} 
	}
	
	@Override
	public Response testHttp(SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::testHttp] BEGIN");
			String response = beService.testWww("http","www.google.com");
			return Response.ok(response).build();
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::testHttp] Errore generico servizio testHttp",ex);
			throw new ServiceException("Errore generico servizio testHttp");
		} 
	}

	@Override
	public Response testHttpS(SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::testHttpS] BEGIN");
			String response = beService.testWww("https","www.google.com");
			return Response.ok(response).build();
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::testHttpS] Errore generico servizio testHttpS",ex);
			throw new ServiceException("Errore generico servizio testHttpS");
		} 
	}
	
}
