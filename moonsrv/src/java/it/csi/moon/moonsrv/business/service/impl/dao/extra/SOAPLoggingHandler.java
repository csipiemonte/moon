/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.util.LoggerAccessor;

public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {
	
	private static final String CLASS_NAME = "MaggioliProtocolloSoapDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();	

	@Override
	public final Set<QName> getHeaders() {
		return null;
	}

	@Override
	public final boolean handleMessage(SOAPMessageContext smc) {
		log(smc);
		return true;
	}

	@Override
	public final boolean handleFault(SOAPMessageContext smc) {
		log(smc);
		return true;
	}

	@Override
	public void close(MessageContext messageContext) {
        // do nothing
	}

	protected void log(SOAPMessageContext smc) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			smc.getMessage().writeTo(out);
			LOG.info("[" + CLASS_NAME + "::handleMessage] " + out.toString());
		} catch (IOException ioe) {
			LOG.error("[" + CLASS_NAME + "::handleMessage] IOException ", ioe);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::handleMessage] Exception ", e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void addSOAPLogger(BindingProvider bp) {
		LOG.info("[" + CLASS_NAME + "::addSOAPLogger] Add SOAPLoggingHandler() to handlerChain");

		Binding binding = bp.getBinding();
		List<Handler> handlerList = binding.getHandlerChain();
		if (null == handlerList) {
			handlerList = new ArrayList<>();
		}			
		handlerList.add(new SOAPLoggingHandler());
		binding.setHandlerChain(handlerList);
		
//		binding.setHandlerResolver(HandlerFactory.build(new SOAPLoggingHandler(), new CDataHandler()));
	}

}
