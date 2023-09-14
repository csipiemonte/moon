/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ValutazioneModuloEntity;
import it.csi.moon.moonfobl.business.be.ValutazioneModuloApi;
import it.csi.moon.moonfobl.business.service.ValutazioneModuloService;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class ValutazioneModuloApiImpl  implements ValutazioneModuloApi {
	
	private static final String CLASS_NAME = "ValutazioneModuloApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ValutazioneModuloService valutazioneModuloService;

	@Override
	public Response postValutazioneModulo(ValutazioneModuloEntity body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::postValutazioneModulo] IN Utente body: "+body);
			valutazioneModuloService.insertValutazioneModulo(body);
			return Response.ok().build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::postValutazioneModulo] Errore servizio postValutazioneModulo",e);
			throw new ServiceException("Errore servizio postValutazioneModulo");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::postValutazioneModulo] Errore generico servizio postValutazioneModulo",ex);
			throw new ServiceException("Errore generico servizio postValutazioneModulo");
		} 
	}

}
