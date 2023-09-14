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

import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.Portale;
import it.csi.moon.moonsrv.business.be.PortaliApi;
import it.csi.moon.moonsrv.business.service.PortaliService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class PortaliApiImpl implements PortaliApi {
	
	private static final String CLASS_NAME = "PortaliApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	PortaliService portaliService;
	
	@Override
	public Response getPortali(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPortali] BEGIN");
			List<Portale> elenco = portaliService.getElencoPortali();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getPortali] Errore servizio getPortali",e);
			throw new ServiceException("Errore servizio elenco portali");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPortali] Errore generico servizio getPortali",ex);
			throw new ServiceException("Errore generico servizio elenco portali");
		}  
	}
	
	@Override
	public Response getPortaleById(Long idPortale, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Portale ruolo = portaliService.getPortaleById(idPortale);
			return Response.ok(ruolo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPortaleById] portale non trovato per idPortale" + idPortale);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getPortaleById] Errore servizio getPortaleById",e);
			throw new ServiceException("Errore servizio getPortaleById");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPortaleById] Errore generico servizio getPortaleById",ex);
			throw new ServiceException("Errore generico servizio getPortaleById");
		} 
	}
	
	@Override
	public Response getPortaleByCd(String codicePortale, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Portale ruolo = portaliService.getPortaleByCd(codicePortale);
			return Response.ok(ruolo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByCd] portale non trovato per codicePortale: " + codicePortale);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByCd] Errore servizio getPortaleByCd",e);
			throw new ServiceException("Errore servizio getPortaleByCd");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByCd] Errore generico servizio getPortaleByCd",ex);
			throw new ServiceException("Errore generico servizio getPortaleByCd");
		} 
	}
	
	@Override
	public Response getPortaleByNome(String nomePortale, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Portale ruolo = portaliService.getPortaleByNome(nomePortale);
			return Response.ok(ruolo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByNome] portale non trovato per nomePortale " + nomePortale);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByNome] Errore servizio getPortaleByNome",e);
			throw new ServiceException("Errore servizio getPortaleByNome");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPortaleByNome] Errore generico servizio getPortaleByNome",ex);
			throw new ServiceException("Errore generico servizio getPortaleByNome");
		} 
	}
	
	@Override
	public Response createPortale(Portale body, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::createPortale] IN Portale body: "+body);
			Portale ruolo = portaliService.createPortale(body);
			return Response.ok(ruolo).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::createPortale] Errore servizio createPortale",e);
			throw new ServiceException("Errore servizio crea portale");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createPortale] Errore generico servizio createPortale",ex);
			throw new ServiceException("Errore generico servizio crea portale");
		} 
	}
	
	@Override
	public Response putPortale(Long idPortale, Portale body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::putPortale] IN idPortale:"+idPortale+"\nPortale body: "+body);
			Portale ruolo = portaliService.updatePortale(idPortale, body);
			return Response.ok(ruolo).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::putPortale] Errore servizio putPortale",e);
			throw new ServiceException("Errore servizio aggiorna portale");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::putPortale] Errore generico servizio putPortale",ex);
			throw new ServiceException("Errore generico servizio aggiorna portale");
		}
	}
	
	
	//
	// Moduli
	//
	@Override
	public Response getModuliByIdPortale(Long idPortale, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getUtentiByIdPortale] IN idPortale = " + idPortale);
			List<Modulo> elenco = portaliService.getModuliByIdPortale(idPortale);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getUtentiByIdPortale] Errore servizio getUtentiByIdPortale", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getUtentiByIdPortale] Errore generico servizio getUtentiByIdPortale", ex);
			throw new ServiceException("Errore generico servizio elenco moduli per idPortale");
		} 
	}
	
	
	//
	// Enti
	//
	@Override
	public Response getEntiByIdPortale(Long idPortale, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getEntiByIdPortale] IN idPortale = " + idPortale);
			List<Ente> elenco = portaliService.getEntiByIdPortale(idPortale);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getEntiByIdPortale] Errore servizio getEntiByIdPortale", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getEntiByIdPortale] Errore generico servizio getEntiByIdPortale", ex);
			throw new ServiceException("Errore generico servizio elenco enti per idPortale");
		} 
	}
}
