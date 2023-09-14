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

import it.csi.moon.commons.dto.Stato;
import it.csi.moon.moonsrv.business.be.StatiApi;
import it.csi.moon.moonsrv.business.service.StatiService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class StatiApiImpl implements StatiApi {
	
	private static final String CLASS_NAME = "StatiApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	StatiService statiService;
	
	@Override
	public Response getStati(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getStati] BEGIN");
			List<Stato> elenco = statiService.getElencoStati();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getStati] BusinessException getStati", e);
			throw new ServiceException("Errore servizio getStati");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getRuoli] Exception getStati", ex);
			throw new ServiceException("Errore generico servizio getStati");
		}  
	}
	
	@Override
	public Response getStatoById(Integer idStato, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Stato result = statiService.getStatoById(idStato);
			return Response.ok(result).build();
		} catch (ItemNotFoundBusinessException infbe) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] ItemNotFoundBusinessException idStato=" + idStato);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] Errore servizio getStatoById", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] Errore generico servizio getStatoById", ex);
			throw new ServiceException("Errore generico servizio getStatoById");
		} 
	}
	
	@Override
	public Response getStatoByCd(String codiceStato, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Stato result = statiService.getStatoByCd(codiceStato);
			return Response.ok(result).build();
		} catch (ItemNotFoundBusinessException infbe) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] ItemNotFoundBusinessException codiceStato=" + codiceStato);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] Errore servizio getStatoByCd", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] Errore generico servizio getStatoByCd", ex);
			throw new ServiceException("Errore generico servizio getStatoByCd");
		} 
	}
	
	@Override
	public Response createStato(Stato body, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::createStato] IN Stato body: " + body);
			Stato result = statiService.createStato(body);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createStato] Errore servizio createStato with body: " + body, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createStato] Errore generico servizio createStato", ex);
			throw new ServiceException("Errore generico servizio createStato");
		} 
	}
	
	@Override
	public Response putStato(Integer idStato, Stato body,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::putStato] IN idStato:" + idStato + " Stato body: " + body);
			Stato result = statiService.updateStato(idStato, body);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::putStato] Errore servizio putStato for idStato:" + idStato + " Stato body: " + body, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::putStato] Errore generico servizio putStato", ex);
			throw new ServiceException("Errore generico servizio putStato");
		}
	}
	
	@Override
	public Response deleteStatoById(Integer idStato, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			statiService.deleteStatoById(idStato);
			return Response.ok().build();
		} catch (ItemNotFoundBusinessException infbe) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] ItemNotFoundBusinessException idStato=" + idStato);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] Errore servizio deleteStatoById", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] Errore generico servizio deleteStatoById", ex);
			throw new ServiceException("Errore generico servizio deleteStatoById");
		} 
	}

}
