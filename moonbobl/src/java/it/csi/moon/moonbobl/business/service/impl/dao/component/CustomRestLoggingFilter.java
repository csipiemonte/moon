/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.component;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.apache.log4j.Logger;

import it.csi.moon.moonbobl.util.LoggerAccessor;

public class CustomRestLoggingFilter implements ClientRequestFilter {

	private final static String CLASS_NAME = "CustomRestLoggingFilter";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Override
	public void filter(ClientRequestContext clientRequestContext) throws IOException {
		if(log.isDebugEnabled())
			log.debug("[CustomRestLoggingFilter:filter] reqest: " + clientRequestContext.getUri());
	}

}
