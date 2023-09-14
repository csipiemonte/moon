/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.listener;

import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import it.csi.moon.moonsrv.business.be.extra.features.JmsApi;
import it.csi.moon.moonsrv.util.LoggerAccessor;

// ONLY FOR TEST
@Conditional(value = { IsEnablePrtStardasV1Listener.class })
@Profile("feature-jms")
@Service
@EnableJms
public class PrtStardasV1Listener {

	private static final String CLASS_NAME = "MOOnSrvPrtStardasV1QueueListener";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
//	private static final String JMS_UNKNOWN_QUEUE = null;
	
	@JmsListener(destination = JmsApi.JMS_MOONSRV_PRT_STARDAS_V1)
	public void receive(TextMessage msg) throws Throwable {
		LOG.info("[" + CLASS_NAME + "::receive] BEGIN " + msg.getText());
//		Enumeration props = msg.getPropertyNames();
//		if (props!=null) {
//			props.asIterator()
//            	.forEachRemaining(s -> System.out.println(s));
//			LOG.info("[" + CLASS_NAME + "::receive] prop = " + props.toString());
//		}
		Thread.sleep(3 * 1000L); // 3s.
//		LOG.info("[" + CLASS_NAME + "::receive] END" + msg.getText());
	}

//	private static String getEnableDestination() {
//		boolean enable = true;
//		if (enable) {
//			return JmsApi.JMS_MOONSRV_PRT_STARDAS_V1;
//		} else {
//			return JMS_UNKNOWN_QUEUE;
//		}
//	}
	
}
