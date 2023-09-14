/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.ws.handlers;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.util.LoggerAccessor;

public class WSSecurityInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final String CLASS_NAME = "WSSecurityInterceptor";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	
	public WSSecurityInterceptor() {
	    super(Phase.PRE_PROTOCOL);
		LOG.info("[" + CLASS_NAME + "::WSSecurityInterceptor] WSSecurityInterceptor()");
	}   
	public WSSecurityInterceptor(String phase) {
	    super(Phase.PRE_PROTOCOL);
		LOG.info("[" + CLASS_NAME + "::WSSecurityInterceptor] WSSecurityInterceptor(String phase)");
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		LOG.info("[" + CLASS_NAME + "::WSSecurityInterceptor] handleMessage() ...");

	    Map<String, Object> props = new HashMap<String, Object>();
	    props.put("action", "UsernameToken");
	    props.put("passwordType", "PasswordText");
	    props.put("passwordCallbackClass", "it.csi.moon.moonsrv.business.ws.handlers.Epaywso2PasswordCallback");

	    WSS4JInInterceptor wss4jInHandler = new WSS4JInInterceptor(props);

	    message.getInterceptorChain().add((Interceptor<? extends Message>) wss4jInHandler);
	}
	
}
