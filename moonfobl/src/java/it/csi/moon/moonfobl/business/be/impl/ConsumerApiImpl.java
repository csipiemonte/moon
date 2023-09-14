/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import it.csi.moon.moonfobl.business.be.ConsumerApi;
import it.csi.moon.moonfobl.business.service.ConsumerService;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class ConsumerApiImpl<P> extends MoonBaseApiImpl implements ConsumerApi {

	private static final String CLASS_NAME = "ConsumerApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ConsumerService consumerService;

	@Override
	public Response getParameters(String consumer, String codiceEnte, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {

			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] BEGIN");
			}
					
			List<P> elenco = consumerService.getParameters(consumer, codiceEnte);
			Map<String, P> mapParameters = new HashMap<>();
			mapParameters.put("consumer", (P)consumer);
			mapParameters.put("codiceEnte", (P)codiceEnte);
			
			for (P p: elenco) {
			    Method method = p.getClass().getMethod("getId");
				mapParameters.put((String)method.invoke(p, null),p);
			}
			return Response.ok(mapParameters).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getParameters] Errore servizio getParameters", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getParameters] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getParameters] Errore generico servizio getIstanze", ex);
			throw new ServiceException("Errore generico servizio elenco getParameters");
		}
		
	}
	@Override
	public Response getParameter(String consumer, String codiceEnte, String idParameter, String type,  SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {

			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] BEGIN");
			}
					
			P parameter = consumerService.getParameter(consumer, codiceEnte, idParameter, type);
			
			return Response.ok(parameter).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getParameter] Errore servizio getParameter", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getParameter] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getParameter] Errore generico servizio getParameter", ex);
			throw new ServiceException("Errore generico servizio elenco getParameter");
		}
	}



}
