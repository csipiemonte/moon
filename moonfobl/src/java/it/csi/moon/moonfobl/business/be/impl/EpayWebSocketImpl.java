/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.moonfobl.business.SpringApplicationContextHelper;
import it.csi.moon.moonfobl.business.service.impl.jms.Listener;
import it.csi.moon.moonfobl.dto.moonfobl.NotificaEpayWsMessage;
import it.csi.moon.moonfobl.util.LoggerAccessor;
import it.csi.moon.moonfobl.util.MapperUtility;

@Profile("epay")
@Service
@ServerEndpoint(value = "/websocket/epay")
public class EpayWebSocketImpl {
	
	private static final String CLASS_NAME = "EpayWebSocketImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private Listener listener = (Listener) SpringApplicationContextHelper.getBean("listener");
	private Set<String> subscriptions = new HashSet<>();

	public EpayWebSocketImpl() {
		super();
		System.out.println("-------------------------------------------------------------------");
		System.out.println("EpayWebSocketImpl actived. @Profile(\"epay\") presente.");
		LOG.info("[EpayWebSocketImpl::EpayWebSocketImpl] EpayWebSocketImpl actived. @Profile(\"epay\") presente.");
	}
	
	@OnOpen
	public void onOpen(Session session) {
		LOG.info("[EpayWebSocketImpl::onOpen] IN session: " + session.getId());
//		Async remote = session.getAsyncRemote();
//		remote.sendText("You are now connected to " + this.getClass().getName());
// SE TORNA qualcosa, DEVE tornare un JSON ! altrimenti 
	}

	@OnMessage
	public String onMessage(String message, Session session) throws Throwable {
		LOG.info("[EpayWebSocketImpl::onMessage] IN session:" + session.getId() + "  messaggio:" + message);
		NotificaEpayWsMessage response = new NotificaEpayWsMessage();
		String responseMsg = "";
		try {
			// Traduco messaggio da jsonb
			ObjectMapper mapper = new ObjectMapper();
			NotificaEpayWsMessage msg = mapper.readValue(message, NotificaEpayWsMessage.class);

			// TODO: Verifcare correttezza auth
			// responseMsg.setAuthToken(tokenAuthVerificato);
			
			if (msg.getCmd().equalsIgnoreCase("subscribe")) {
				String iuv = msg.getIuv();
				LOG.info("[EpayWebSocketImpl::onMessage] subscribe " + iuv);
				if (iuv != null) {
					subscriptions.add(iuv);
					listener.subscribe(session, iuv);
				}
			} else if (msg.getCmd().equalsIgnoreCase("unsubscribe")) {
				String iuv = msg.getIuv();
				LOG.info("[EpayWebSocketImpl::onMessage] unsubscribe " + iuv);
				if (iuv != null) {
					listener.unsubscribe(session, iuv);
					subscriptions.remove(iuv);
				}
			}
			response.setCmd("OK");
			String result = MapperUtility.getJsonFromObj(response);
			LOG.info("[EpayWebSocketImpl::onMessage] result " + result);
			return result;
		} catch (Throwable e) {
			LOG.error("[EpayWebSocketImpl::onMessage]", e);
			response.setCmd("KO");
			return MapperUtility.getJsonFromObj(response);
		}
	}

	@OnClose
	public void onClose(Session session) {
		LOG.info("[EpayWebSocketImpl::onClose] IN session: " + session.getId());
		subscriptions.forEach(iuv -> listener.unsubscribe(session, iuv));
		subscriptions.clear();
	}

	@OnError
	public void onError(Throwable exception, Session session) {
		LOG.error("[EpayWebSocketImpl::onError] IN session: " + session.getId() + "  exception.message:" + exception.getMessage());
	}

}
