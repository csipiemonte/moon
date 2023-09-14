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

import it.csi.moon.moonbobl.business.be.PortaliApi;
import it.csi.moon.moonbobl.business.service.PortaliService;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.Portale;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class PortaliApiImpl implements PortaliApi {
	
	private final static String CLASS_NAME = "PortaliApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	PortaliService portaliService;
	
	@Override
	public Response getPortali(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getPortali] BEGIN");
			List<Portale> elenco = portaliService.getElencoPortali();
//			if (elenco.size() == 0)
//				throw new ResourceNotFoundException();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getPortali] Errore servizio getPortali",e);
			throw new ServiceException("Errore servizio elenco portali");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getPortali] Errore generico servizio getPortali",ex);
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
			log.error("[" + CLASS_NAME + "::getPortaleById] portale non trovato per idPortale" + idPortale);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getPortaleById] Errore servizio getPortaleById",e);
			throw new ServiceException("Errore servizio get portale");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getPortaleById] Errore generico servizio getPortaleById",ex);
			throw new ServiceException("Errore generico servizio get portale");
		} 
	}
	
	@Override
	public Response getPortaleByCd(String codicePortale, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Portale ruolo = portaliService.getPortaleByCd(codicePortale);
			return Response.ok(ruolo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getPortaleByCd] portale non trovato per codicePortale: " + codicePortale);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getPortaleByCd] Errore servizio getPortaleByCd",e);
			throw new ServiceException("Errore servizio get portale");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getPortaleByCd] Errore generico servizio getPortaleByCd",ex);
			throw new ServiceException("Errore generico servizio get portale by Codice");
		} 
	}
	
	@Override
	public Response getPortaleByNome(String nomePortale, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Portale ruolo = portaliService.getPortaleByNome(nomePortale);
			return Response.ok(ruolo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getPortaleByNome] portale non trovato per nomePortale " + nomePortale);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getPortaleByNome] Errore servizio getPortaleByNome",e);
			throw new ServiceException("Errore servizio get portale");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getPortaleByNome] Errore generico servizio getPortaleByNome",ex);
			throw new ServiceException("Errore generico servizio get portale by nome");
		} 
	}
	
	@Override
	public Response createPortale(Portale body, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::createPortale] IN Portale body: "+body);
			Portale ruolo = portaliService.createPortale(body);
			return Response.ok(ruolo).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::createPortale] Errore servizio createPortale",e);
			throw new ServiceException("Errore servizio crea portale");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::createPortale] Errore generico servizio createPortale",ex);
			throw new ServiceException("Errore generico servizio crea portale");
		} 
	}
	
	@Override
	public Response putPortale(Long idPortale, Portale body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::putPortale] IN idPortale:"+idPortale+"\nPortale body: "+body);
			Portale ruolo = portaliService.updatePortale(idPortale, body);
			return Response.ok(ruolo).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::putPortale] Errore servizio putPortale",e);
			throw new ServiceException("Errore servizio aggiorna portale");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::putPortale] Errore generico servizio putPortale",ex);
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
			log.debug("[" + CLASS_NAME + "::getUtentiByIdPortale] BEGIN");
			List<Modulo> elenco = portaliService.getModuliByIdPortale(idPortale);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getUtentiByIdPortale] Errore servizio getUtentiByIdPortale",e);
			throw new ServiceException("Errore servizio elenco moduli per idPortale");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getUtentiByIdPortale] Errore generico servizio getUtentiByIdPortale",ex);
			throw new ServiceException("Errore generico servizio elenco moduli per idPortale");
		} 
	}
	
}
