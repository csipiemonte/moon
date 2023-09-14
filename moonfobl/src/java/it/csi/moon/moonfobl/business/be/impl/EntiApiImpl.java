/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.EntiFilter;
import it.csi.moon.moonfobl.business.be.EntiApi;
import it.csi.moon.moonfobl.business.service.EntiService;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class EntiApiImpl extends MoonBaseApiImpl implements EntiApi {
	
	private static final String CLASS_NAME = "EntiApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	EntiService entiService;
	
	@Override
	public Response getEnti(String idEnteQP, String codiceEnteQP, String nomeEnteQP,
		String descrizioneEnteQP, String flagAttivoQP, String idTipoEnteQP,
		String logoQP, /*String nomePortaleQP,*/ String idPortaleQP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getEnti] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getEnti] IN idEnte: "+idEnteQP);
				LOG.debug("[" + CLASS_NAME + "::getEnti] IN codiceEnte: "+codiceEnteQP);
				LOG.debug("[" + CLASS_NAME + "::getEnti] IN nomeEnte: "+nomeEnteQP);
				LOG.debug("[" + CLASS_NAME + "::getEnti] IN descrizioneEnte: "+descrizioneEnteQP);
				LOG.debug("[" + CLASS_NAME + "::getEnti] IN flagAttivo: "+flagAttivoQP);
				LOG.debug("[" + CLASS_NAME + "::getEnti] IN idTipoEnte: "+idTipoEnteQP);
				LOG.debug("[" + CLASS_NAME + "::getEnti] IN logo: "+logoQP);
				LOG.debug("[" + CLASS_NAME + "::getEnti] IN idPortale: "+idPortaleQP);
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			String nomePortale = retrievePortalName(httpRequest);
			
			EntiFilter filter = new EntiFilter();
			filter.setIdEnte(validaLong(idEnteQP));
			filter.setCodiceEnte(codiceEnteQP);
			filter.setNomeEnte(nomeEnteQP);
			filter.setDescrizioneEnte(descrizioneEnteQP);
			filter.setFlAttivo(flagAttivoQP);
			filter.setIdTipoEnte(validaInteger(idTipoEnteQP));
			filter.setLogo(logoQP);			
			filter.setNomePortale(nomePortale); // Filtro obbligatorio
			filter.setIdPortale(validaLong(idPortaleQP));
			
			LOG.debug("[" + CLASS_NAME + "::getEnti] BEGIN");
			List<Ente> elenco = entiService.getElencoEnti(filter);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getEnti] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getEnti] Errore generico servizio getEnti", ex);
			throw new ServiceException("Errore generico servizio elenco enti");
		}  
	}
	
	@Override
	public Response getEnteById(Long idEnte, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Ente ente = entiService.getEnteById(idEnte);
			return Response.ok(ente).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getEnteById] ruolo non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getEnteById] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getEnteById] Errore generico servizio getEnteById",ex);
			throw new ServiceException("Errore generico servizio get ente");
		} 
	}
	
	@Override
	public Response getEnteByCd(String codiceEnte, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Ente ente = entiService.getEnteByCodice(codiceEnte);
			return Response.ok(ente).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getEnteByCd] ruolo non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getEnteByCd] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getEnteByCd] Errore generico servizio getEnteByCd", ex);
			throw new ServiceException("Errore generico servizio get ente by codice");
		} 
	}
	
	@Override
	public Response createEnte(Ente body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::createEnte] IN Ente body: " + body);
			Ente ente = entiService.createEnte(body);
			return Response.ok(ente).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createEnte] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::createEnte] Errore generico servizio createEnte", ex);
			throw new ServiceException("Errore generico servizio crea ente");
		} 
	}
	
	@Override
	public Response putEnte(Long idEnte, Ente body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::putEnte] IN idEnte:" + idEnte + "\nEnte body: " + body);
			Ente ente = entiService.updateEnte(idEnte, body);
			return Response.ok(ente).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::putEnte] BusinessException ", be);
			throw new ServiceException("Errore servizio aggiorna ente");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::putEnte] Errore generico servizio putEnte", ex);
			throw new ServiceException("Errore generico servizio aggiorna ente");
		}
	}
	
}
