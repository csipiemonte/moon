/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.features.impl;

import java.security.Principal;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.be.extra.features.JmsApi;
import it.csi.moon.moonsrv.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Profile("feature-jms")
@Component
public class JmsApiImpl extends MoonBaseApiImpl implements JmsApi {
	
	private static final String CLASS_NAME = "JmsApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String PRFIX_JMS_QUEUE_MANE = "";

	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Override
	public Response ping(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		if (securityContext != null) {
			Principal principal = securityContext.getUserPrincipal();
			LOG.info("[" + CLASS_NAME + "::ping] OK securityContext.getUserPrincipal() = " + principal);
		}
		String response = "OK";
		return Response.ok(response).build();
	}
	
	@Override
	public Response sendMessagePrt(String destination, String multiple, String payload,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Integer nMultiple = validaInteger(multiple,1);
			for (int i=1 ; i<=nMultiple ; i++) {
				sendMessage(destination, payload, i);
			}
			return Response.accepted().build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::sendMessagePrt] Errore generico servizio sendMessagePrt", ex);
			throw new ServiceException("Errore generico servizio sendMessagePrt");
		}
	}
	
	private void sendMessage(String dest, String payload) throws Exception {
    	LOG.info("[" + CLASS_NAME + "::sendMessage] dest: " + dest + " payload:" + payload);
		jmsTemplate.setPubSubDomain(true);
		// Sol.1
		jmsTemplate.send(PRFIX_JMS_QUEUE_MANE+dest, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(payload);
			}
		});
		// Sol.2
//		jmsTemplate.convertAndSend(dest, payload);
		// Sol.3
//		jmsTemplate.convertAndSend(destination, payload, msg -> {
////			msg.setStringProperty("iuv", npe.getIuv());
//			msg.setJMSType(MoonJmsTypes.PRT_REQUEST);
//			return msg;
//		});
	}
	
	
//	private void sendMessageUsingJMSContext(String dest, String payload) throws Exception {
//    	LOG.info("[" + CLASS_NAME + "::sendMessageUsingJMSContext] dest: " + dest + " payload:" + payload);
//    	InitialContext initCtx = new InitialContext();
//    	ConnectionFactory connectionFactory = (ConnectionFactory) initCtx.lookup("java:/JmsXA");
//    	Queue msgQueue = (Queue) initCtx.lookup("jms/" + dest);
//    	try (JMSContext ctx = connectionFactory.createContext();) {
//    		TextMessage message = ctx.createTextMessage(text);
//    		ctx.createProducer().sebd(dataQueue, message);
//    	}
//	}
	
	private void sendMessage(String dest, String payload, int i) throws Exception {
    	LOG.info("[" + CLASS_NAME + "::sendMessage] dest: " + dest + " payload:" + payload + " i:" + i);
		jmsTemplate.setPubSubDomain(true);
		
//		Queue q = new Queue();
//		java:/jms/queue/
		jmsTemplate.send(PRFIX_JMS_QUEUE_MANE+dest, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(String.format(payload, i));
			}
		});
	}

}
