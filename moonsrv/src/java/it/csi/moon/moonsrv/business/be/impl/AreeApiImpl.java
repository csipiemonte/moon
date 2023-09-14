/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Area;
import it.csi.moon.commons.entity.AreeFilter;
import it.csi.moon.moonsrv.business.be.AreeApi;
import it.csi.moon.moonsrv.business.service.AreeService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class AreeApiImpl extends MoonBaseApiImpl implements AreeApi {
	
	private static final String CLASS_NAME = "AreeApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	AreeService areeService;
	
	@Override
	public Response getAree(String idAreaQP, String codiceAreaQP, String nomeAreaQP,
		String idEnteQP,  
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getAree] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getAree] IN idArea: "+idAreaQP);
				LOG.debug("[" + CLASS_NAME + "::getAree] IN codiceArea: "+codiceAreaQP);
				LOG.debug("[" + CLASS_NAME + "::getAree] IN nomeArea: "+nomeAreaQP);
				LOG.debug("[" + CLASS_NAME + "::getAree] IN idEnte: "+idEnteQP);
			}
			AreeFilter filter = new AreeFilter();
			filter.setIdArea(validaLong(idAreaQP));
			filter.setCodiceArea(codiceAreaQP);
			filter.setNomeArea(nomeAreaQP);
			filter.setIdEnte(validaLong(idEnteQP));
			LOG.debug("[" + CLASS_NAME + "::getAree] BEGIN");
			List<Area> elenco = areeService.getElencoAree(filter);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAree] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAree] Errore generico servizio getAree", ex);
			throw new ServiceException("Errore generico servizio elenco aree");
		}  
	}
	
	@Override
	public Response getAreaById(Long idArea, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Area ris = areeService.getAreaById(idArea);
			return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getAreaById] elemento non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAreaById] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAreaById] Errore generico servizio getAreaById",ex);
			throw new ServiceException("Errore generico servizio get area");
		} 
	}
	
	@Override
	public Response getAreaByIdEnteCodice(String codiceArea, String idEntePP, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Area ris = areeService.getAreaByIdEnteCodice(validaLongRequired(idEntePP), validaStringRequired(codiceArea));
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException upe) {
			throw upe;
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getAreaByIdEnteCodice] elemento non trovato codiceArea " + codiceArea + " of ente " + idEntePP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAreaByIdEnteCodice] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAreaByIdEnteCodice] Errore generico servizio getAreaByCd", ex);
			throw new ServiceException("Errore generico servizio get area by codice");
		}
	}
	
	@Override
	public Response createArea(Area body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::createArea] IN Area body: " + body);
			Area ris = areeService.createArea(body);
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createArea] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createArea] Errore generico servizio createArea", ex);
			throw new ServiceException("Errore generico servizio crea area");
		} 
	}
	
	@Override
	public Response putArea(Long idArea, Area body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::putArea] IN idArea:" + idArea + "\nArea body: " + body);
			Area ris = areeService.updateArea(idArea, body);
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::putArea] BusinessException ", be);
			throw new ServiceException("Errore servizio aggiorna area");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::putArea] Errore generico servizio putArea", ex);
			throw new ServiceException("Errore generico servizio aggiorna area");
		}
	}
	
}
