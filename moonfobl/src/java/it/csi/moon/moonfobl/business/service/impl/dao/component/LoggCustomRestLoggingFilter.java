/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.component;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.apache.log4j.Logger;

import it.csi.moon.moonfobl.util.LoggerAccessor;

public class LoggCustomRestLoggingFilter implements ClientRequestFilter {
	
	private static final String CLASS_NAME = "CustomRestLoggingFilter";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Override
	public void filter(ClientRequestContext clientRequestContext) throws IOException {
		if(LOG.isDebugEnabled())
			LOG.debug("[CustomRestLoggingFilter:filter] reqest: " + clientRequestContext.getUri());
	}

}
