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

import it.csi.moon.moonbobl.business.be.CustomComponentApi;
import it.csi.moon.moonbobl.business.be.UtentiApi;
import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.CustomComponentService;
import it.csi.moon.moonbobl.business.service.UtentiService;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.CustomComponentEntity;
import it.csi.moon.moonbobl.dto.extra.istat.CodiceNome;
import it.csi.moon.moonbobl.dto.moonfobl.Funzione;
import it.csi.moon.moonbobl.dto.moonfobl.Gruppo;
import it.csi.moon.moonbobl.dto.moonfobl.Ruolo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.Utente;
import it.csi.moon.moonbobl.dto.moonfobl.UtenteEnteAbilitato;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


@Component
public class CustomComponentApiImpl extends MoonBaseApiImpl implements CustomComponentApi {
	
	private static final String CLASS_NAME = "CustomComponentApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	private CustomComponentService customComponentService;

	
	@Override
	public Response getComponentById(String idComponent,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getComponentById] BEGIN");
			CustomComponentEntity  entity = customComponentService.findById(idComponent);
			return Response.ok(entity).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComponentById] Errore servizio getUtenti " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getUtgetComponentByIdenti] Errore generico servizio getUtenti",ex);
			throw new ServiceException("Errore generico servizio elenco utenti");
		}
	}


	@Override
	public Response getComponents(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getComponents] BEGIN");
			List<CustomComponentEntity> elenco = customComponentService.getCustomComponents();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getComponents] Errore servizio getComponents",e);
			throw new ServiceException("Errore servizio getComponents");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getComponents] Errore generico servizio getComponents",ex);
			throw new ServiceException("Errore generico servizio getComponents");
		}
	}
	

}
