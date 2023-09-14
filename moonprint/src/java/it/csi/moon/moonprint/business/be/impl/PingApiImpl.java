/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.business.be.impl;

import java.security.Principal;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonprint.business.be.PingApi;
import it.csi.moon.moonprint.business.service.BackendService;
import it.csi.moon.moonprint.util.LoggerAccessor;

@Component
public class PingApiImpl  implements PingApi {
	
	private final static String CLASS_NAME = "PingApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	private BackendService beService;

	public Response ping(SecurityContext securityContext) {
		LOG.info("[" + CLASS_NAME + "::ping] BEGIN END");
		
		Principal principal = null;
		if (securityContext != null) {
			principal = securityContext.getUserPrincipal();
			LOG.debug("[" + CLASS_NAME + "::ping] init principal=" + principal);
		}
		
		String response = beService.getMessage();
		return Response.ok(response).build();
	}
	
}
