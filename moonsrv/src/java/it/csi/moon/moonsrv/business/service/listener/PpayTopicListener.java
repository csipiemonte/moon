/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.listener;

import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;

import it.csi.moon.moonsrv.util.LoggerAccessor;

// ALREADY COMMENT, ONLY FOR TEST
//@Profiles("jms")
//@Service
//@EnableJms
public class PpayTopicListener {

	private static final String CLASS_NAME = "PpayTopicListener";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@JmsListener(destination = "MOOnFOTopic")
	public void receive(TextMessage msg) throws Throwable {
		LOG.info(msg.getText());
	}
	
}
