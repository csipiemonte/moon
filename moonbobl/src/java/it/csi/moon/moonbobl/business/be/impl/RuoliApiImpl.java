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

import it.csi.moon.moonbobl.business.be.RuoliApi;
import it.csi.moon.moonbobl.business.service.RuoliService;
import it.csi.moon.moonbobl.dto.moonfobl.Ruolo;
import it.csi.moon.moonbobl.dto.moonfobl.Utente;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


@Component
public class RuoliApiImpl implements RuoliApi {
	
	private final static String CLASS_NAME = "RuoliApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	RuoliService ruoliService;
	
	@Override
	public Response getRuoli(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getRuoli] BEGIN");
			List<Ruolo> elenco = ruoliService.getElencoRuoli();
//			if (elenco.size() == 0)
//				throw new ResourceNotFoundException();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getRuoli] Errore servizio getRuoli",e);
			throw new ServiceException("Errore servizio elenco ruoli");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getRuoli] Errore generico servizio getRuoli",ex);
			throw new ServiceException("Errore generico servizio elenco ruoli");
		}  
	}
	
	@Override
	public Response getRuoloById(Integer idRuolo, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			Ruolo ruolo = ruoliService.getRuoloById(idRuolo);
			return Response.ok(ruolo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getRuoloById] ruolo non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getRuoloById] Errore servizio getRuoloById",e);
			throw new ServiceException("Errore servizio get ruolo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getRuoloById] Errore generico servizio getRuoloById",ex);
			throw new ServiceException("Errore generico servizio get ruolo");
		} 
	}
	
	@Override
	public Response createRuolo(Ruolo body, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::createRuolo] IN Ruolo body: "+body);
			Ruolo ruolo = ruoliService.createRuolo(body);
			return Response.ok(ruolo).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::createRuolo] Errore servizio createRuolo",e);
			throw new ServiceException("Errore servizio crea ruolo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::createRuolo] Errore generico servizio createRuolo",ex);
			throw new ServiceException("Errore generico servizio crea ruolo");
		} 
	}
	
	@Override
	public Response putRuolo(Integer idRuolo, Ruolo body, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::putRuolo] IN idRuolo:"+idRuolo+"\nRuolo body: "+body);
			Ruolo ruolo = ruoliService.updateRuolo(idRuolo, body);
			return Response.ok(ruolo).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::putRuolo] Errore servizio putRuolo",e);
			throw new ServiceException("Errore servizio aggiorna ruolo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::putRuolo] Errore generico servizio putRuolo",ex);
			throw new ServiceException("Errore generico servizio aggiorna ruolo");
		}
	}
	
	
	//
	// Utenti
	//
	@Override
	public Response getUtentiByIdRuolo(Integer idRuolo, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getUtentiByIdRuolo] BEGIN");
			List<Utente> elenco = ruoliService.getUtentiByIdRuolo(idRuolo);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getUtentiByIdRuolo] Errore servizio getUtentiByIdRuolo",e);
			throw new ServiceException("Errore servizio elenco utenti per idRuolo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getUtentiByIdRuolo] Errore generico servizio getUtentiByIdRuolo",ex);
			throw new ServiceException("Errore generico servizio elenco utenti per idRuolo");
		} 
	}
	
}
