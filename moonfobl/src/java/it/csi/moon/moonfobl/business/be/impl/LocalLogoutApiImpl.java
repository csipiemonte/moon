/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonfobl.util.LoggerAccessor;
import it.csi.moon.moonfobl.business.be.LocalLogoutApi;
import it.csi.moon.moonfobl.business.service.BackendService;

@Component
public class LocalLogoutApiImpl implements LocalLogoutApi {
	
	private static final String CLASS_NAME = "LocalLogoutApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	public BackendService beService;
	
	public Response localLogout(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest req ) {
		String result = beService.localLogout(req);
		return Response.ok(result).build();
	}
}
