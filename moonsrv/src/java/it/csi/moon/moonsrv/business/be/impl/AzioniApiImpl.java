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

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.moonsrv.business.be.AzioniApi;
import it.csi.moon.moonsrv.business.service.AzioniService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class AzioniApiImpl implements AzioniApi {
	
	private static final String CLASS_NAME = "AzioniApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	AzioniService azioniService;
	
	@Override
	public Response getAzioni(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getAzioni] BEGIN");
			List<Azione> elenco = azioniService.getElencoAzioni();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getAzioni] BusinessException getAzioni", e);
			throw new ServiceException("Errore servizio getAzioni");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getRuoli] Exception getAzioni", ex);
			throw new ServiceException("Errore generico servizio getAzioni");
		}  
	}
	
	@Override
	public Response getAzioneById(Long idAzione, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Azione result = azioniService.getAzioneById(idAzione);
			return Response.ok(result).build();
		} catch (ItemNotFoundBusinessException infbe) {
			LOG.error("[" + CLASS_NAME + "::getAzioneById] ItemNotFoundBusinessException idAzione=" + idAzione);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAzioneById] Errore servizio getAzioneById", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAzioneById] Errore generico servizio getAzioneById", ex);
			throw new ServiceException("Errore generico servizio getAzioneById");
		} 
	}
	
	@Override
	public Response getAzioneByCd(String codiceAzione, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Azione result = azioniService.getAzioneByCd(codiceAzione);
			return Response.ok(result).build();
		} catch (ItemNotFoundBusinessException infbe) {
			LOG.error("[" + CLASS_NAME + "::getAzioneByCd] ItemNotFoundBusinessException codiceAzione=" + codiceAzione);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getAzioneByCd] Errore servizio getAzioneByCd", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAzioneByCd] Errore generico servizio getAzioneByCd", ex);
			throw new ServiceException("Errore generico servizio getAzioneByCd");
		} 
	}
	
	@Override
	public Response createAzione(Azione body, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::createAzione] IN Azione body: " + body);
			Azione result = azioniService.createAzione(body);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createAzione] Errore servizio createAzione with body: " + body, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createAzione] Errore generico servizio createAzione", ex);
			throw new ServiceException("Errore generico servizio createAzione");
		} 
	}
	
	@Override
	public Response putAzione(Long idAzione, Azione body,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::putAzione] IN idAzione:" + idAzione + " Azione body: " + body);
			Azione result = azioniService.updateAzione(idAzione, body);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::putAzione] Errore servizio putAzione for idAzione:" + idAzione + " Azione body: " + body, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::putAzione] Errore generico servizio putAzione", ex);
			throw new ServiceException("Errore generico servizio putAzione");
		}
	}
	
	@Override
	public Response deleteAzioneById(Long idAzione, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			azioniService.deleteAzioneById(idAzione);
			return Response.ok().build();
		} catch (ItemNotFoundBusinessException infbe) {
			LOG.error("[" + CLASS_NAME + "::deleteAzioneById] ItemNotFoundBusinessException idAzione=" + idAzione);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::deleteAzioneById] Errore servizio deleteAzioneById", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::deleteAzioneById] Errore generico servizio deleteAzioneById", ex);
			throw new ServiceException("Errore generico servizio deleteAzioneById");
		} 
	}

}
