/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.ValutazioneModuloApi;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.dto.moonfobl.ValutazioneModuloSintesi;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class ValutazioneModuloApiImpl implements ValutazioneModuloApi {
	
	private static final String CLASS_NAME = "ValutazioneModuloApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	MoonsrvDAO moonsrvDAO;

//	@Override
//	public Response postValutazioneModulo(ValutazioneModuloEntity body, 
//		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
//		try {
//			LOG.debug("[" + CLASS_NAME + "::postValutazioneModulo] IN Utente body: "+body);
//			valutazioneModuloService.insertValutazioneModulo(body);
//			return Response.ok().build();
//		} catch (BusinessException e) {
//			LOG.error("[" + CLASS_NAME + "::postValutazioneModulo] Errore servizio postValutazioneModulo",e);
//			throw new ServiceException("Errore servizio postValutazioneModulo");
//		} catch (Throwable ex) {
//			LOG.error("[" + CLASS_NAME + "::postValutazioneModulo] Errore generico servizio postValutazioneModulo",ex);
//			throw new ServiceException("Errore generico servizio postValutazioneModulo");
//		} 
//	}

	@Override
	public Response getValutazioneModuloSintesi( Long idModulo,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getValutazioneModuloSintesi] IN idModulo: "+idModulo);
			List<ValutazioneModuloSintesi> ris = moonsrvDAO.getValutazioneModuloSintesi(idModulo);
			return Response.ok(ris).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getValutazioneModuloSintesi] Errore servizio getValutazioneModuloSintesi",e);
			throw new ServiceException("Errore servizio getValutazioneModuloSintesi");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getValutazioneModuloSintesi] Errore generico servizio getValutazioneModuloSintesi",ex);
			throw new ServiceException("Errore generico servizio getValutazioneModuloSintesi");
		} 
	}
	
}
