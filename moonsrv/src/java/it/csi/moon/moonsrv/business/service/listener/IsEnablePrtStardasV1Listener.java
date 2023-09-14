/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.listener;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import it.csi.moon.moonsrv.util.LoggerAccessor;

public class IsEnablePrtStardasV1Listener implements Condition {

	private static final String CLASS_NAME = "IsEnablePrtStardasV1Listener";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

//	@Autowired
//	private ParametroDAO parametroDAO;
	
	@Override
	public boolean matches(ConditionContext ctx, AnnotatedTypeMetadata type) {
		LOG.debug("[" + CLASS_NAME + "::matches] BEGIN");
		return true;
//		boolean result = false;
//		try {
//			if (parametroDAO==null) {
//				LOG.debug("[" + CLASS_NAME + "::matches] SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this); ...");
//				SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
//			}
//			if (parametroDAO==null) {
//				LOG.debug("[" + CLASS_NAME + "::matches] parametroDAO = new ParametroDAOImpl(); ...");
//				parametroDAO = new ParametroDAOImpl();
//			}
//			ParametroEntity paramAmqNodes = parametroDAO.findByCodici(ParametroDAO.COMPONENTE_SRV, ParametroDAO.PARAMETRO_AMQ_NODES);
//			String amqNodes = paramAmqNodes.getValore();
//			String hostname = InetAddress.getLocalHost().getHostName();
//			if (StringUtils.isNotBlank(hostname) && StringUtils.isNotBlank(amqNodes)) {
//				LOG.info("[" + CLASS_NAME + "::matches] verify " + hostname + " present in parametro " + ParametroDAO.COMPONENTE_SRV + "." + ParametroDAO.PARAMETRO_AMQ_NODES + " is " + amqNodes);
//				List<String> listAmqNodes = Arrays.asList(amqNodes.split(","));
//				if (listAmqNodes.contains(hostname)) {
//					LOG.debug("[" + CLASS_NAME + "::matches] " + hostname + " enable.");
//					result = true;
//				}
//				if (LOG.isDebugEnabled()) {
//					LOG.debug("[" + CLASS_NAME + "::matches] " + hostname + " disable.");
//				}
//			}
//		} catch (UnknownHostException uhe) {
//			LOG.warn("[" + CLASS_NAME + "::matches] UnknownHostException", uhe);
//		} catch (Exception e) {
//			LOG.warn("[" + CLASS_NAME + "::matches] Exception", e);
//		}
//		return result;
	}

}
