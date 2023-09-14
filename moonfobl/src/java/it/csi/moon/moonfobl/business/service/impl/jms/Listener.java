/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.jms;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.TextMessage;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import it.csi.moon.commons.entity.EpayNotificaPagamentoEntity;
import it.csi.moon.moonfobl.dto.moonfobl.NotificaEpayWsMessage;
import it.csi.moon.moonfobl.util.LoggerAccessor;
import it.csi.moon.moonfobl.util.MapperUtility;

@Profile("epay")
@Service
@EnableJms
public class Listener {
	
	private static final String MOONFO_EPAY_JMS_TYPE = "JMSType = 'epay-notification'";
	private static final String MOONFO_JMS_NAME = "MOOnFOTopic";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private Map<String,Set<Session>> subscriptions = new ConcurrentHashMap<>();
	
	public Listener() {
		super();
		System.out.println("-------------------------------------------------------------------");
		System.out.println("Listener actived. @Profile(\"epay\") presente.");
		LOG.info("[Listener::Listener] EpayServiceCallbackImpl actived. @Profile(\"epay\") presente.");
	}

	public void subscribe(Session session, String key) {
		Set<Session> newSet = ConcurrentHashMap.newKeySet();
		Set<Session> subscriptionSet = subscriptions.putIfAbsent(key, newSet);
		if (subscriptionSet == null) {
			subscriptionSet = newSet;
		}
		subscriptionSet.add(session);
	}
	
	public boolean unsubscribe(Session session, String key) {
		Set<Session> subscriptionSet = subscriptions.get(key);
		if (subscriptionSet != null) {
			return subscriptionSet.remove(session);
		}
		return false;
	}
	
	@JmsListener(destination = MOONFO_JMS_NAME, selector = MOONFO_EPAY_JMS_TYPE)
	public void receive(TextMessage message) throws Throwable {
		LOG.info(String.format("[Listener::receive] IN message: '%s'", message));
		// ricevo notifica come testo
		MapperUtility<EpayNotificaPagamentoEntity> mapper = new MapperUtility<>(EpayNotificaPagamentoEntity.class);
		EpayNotificaPagamentoEntity notifica = mapper.getObjFromJson(message.getText());
		handleMessage(message.getStringProperty("iuv"), notifica);
	}

	
	// Invio messaggio ai socket sottoscritti		
	public void handleMessage(String iuv, EpayNotificaPagamentoEntity notifica) throws Throwable {
		Set<Session> sessions = subscriptions.get(iuv);
		if (sessions != null) {
			sessions.forEach(session -> {
				NotificaEpayWsMessage msg = new NotificaEpayWsMessage();
				msg.setCmd("epayNotification");
				msg.setData(notifica);					
				try {
					String msgJson = MapperUtility.getJsonFromObj(msg);
					LOG.info(String.format("[Listener::handleMessage] websocket sendText to session: %s text: '%s'", session.getId(), msgJson));
					session.getBasicRemote().sendText(msgJson);
				} catch (Throwable e) {
					LOG.error("[Listener::handleMessage] ", e);
				}   
			});
		} else {
			LOG.info(String.format("[Listener::handleMessage] no subscriptions for iuv %s", iuv));
		}
	}

}
