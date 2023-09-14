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

import it.csi.moon.moonbobl.business.be.FunzioniApi;
import it.csi.moon.moonbobl.business.service.FunzioniService;
import it.csi.moon.moonbobl.dto.moonfobl.Funzione;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


@Component
public class FunzioniApiImpl implements FunzioniApi {
	
	private final static String CLASS_NAME = "FunzioniApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	FunzioniService funzioniService;
	
	@Override
	public Response getFunzioni(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			log.debug("[" + CLASS_NAME + "::getFunzioni] BEGIN");
			List<Funzione> elenco = funzioniService.getElencoFunzioni();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getFunzioni] Errore servizio getFunzioni",e);
			throw new ServiceException("Errore servizio elenco funzioni");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getFunzioni] Errore generico servizio getFunzioni",ex);
			throw new ServiceException("Errore generico servizio elenco funzioni");
		} 
	}
	@Override
	public Response getFunzioneById(Integer idFunzione, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Funzione funzione = funzioniService.getFunzioneById(idFunzione);
			return Response.ok(funzione).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getFunzioneById] funzione non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getFunzioneById] Errore servizio getFunzioneById",e);
			throw new ServiceException("Errore servizio get funzione");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getFunzioneById] Errore generico servizio getFunzioneById",ex);
			throw new ServiceException("Errore generico servizio get funzione");
		} 
	}
	
	@Override
	public Response saveFunzione(Funzione body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			log.debug("[" + CLASS_NAME + "::saveFunzione] IN Funzione body: "+body);
			Funzione funzione = funzioniService.createFunzione(body);
			return Response.ok(funzione).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::saveFunzione] Errore servizio saveFunzione",e);
			throw new ServiceException("Errore servizio crea funzione");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::saveFunzione] Errore generico servizio saveFunzione",ex);
			throw new ServiceException("Errore generico servizio crea funzione");
		} 
	}
	
	@Override
	public Response putFunzione(Integer idFunzione, Funzione body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			log.debug("[" + CLASS_NAME + "::putFunzione] IN idFunzione:"+idFunzione+"\nFunzione body: "+body);
			Funzione funzione = funzioniService.updateFunzione(idFunzione, body);
			return Response.ok(funzione).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::putFunzione] Errore servizio putFunzione",e);
			throw new ServiceException("Errore servizio aggiorna funzione");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::putFunzione] Errore generico servizio putFunzione",ex);
			throw new ServiceException("Errore generico servizio aggiorna funzione");
		}
	}

}
