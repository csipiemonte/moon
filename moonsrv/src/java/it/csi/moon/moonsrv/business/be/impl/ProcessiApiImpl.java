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

import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.Processo;
import it.csi.moon.moonsrv.business.be.ProcessiApi;
import it.csi.moon.moonsrv.business.service.ProcessiService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ProcessiApiImpl implements ProcessiApi {
	
	private static final String CLASS_NAME = "ProcessiApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ProcessiService processiService;
	
	@Override
	public Response getProcessi(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getProcessi] BEGIN");
			List<Processo> elenco = processiService.getElencoProcessi();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getProcessi] Errore servizio getProcessi",e);
			throw new ServiceException("Errore servizio elenco processi");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getProcessi] Errore generico servizio getProcessi",ex);
			throw new ServiceException("Errore generico servizio elenco processi");
		}  
	}
	
	@Override
	public Response getProcessoById(Long idProcesso, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Processo ruolo = processiService.getProcessoById(idProcesso);
			return Response.ok(ruolo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getProcessoById] processo non trovato per idProcesso" + idProcesso);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getProcessoById] Errore servizio getProcessoById",e);
			throw new ServiceException("Errore servizio get processo");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getProcessoById] Errore generico servizio getProcessoById",ex);
			throw new ServiceException("Errore generico servizio get processo by Id");
		} 
	}
	
	@Override
	public Response getProcessoByCd(String codiceProcesso, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Processo ruolo = processiService.getProcessoByCd(codiceProcesso);
			return Response.ok(ruolo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByCd] processo non trovato per codiceProcesso: " + codiceProcesso);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByCd] Errore servizio getProcessoByCd",e);
			throw new ServiceException("Errore servizio get processo by Codice");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByCd] Errore generico servizio getProcessoByCd",ex);
			throw new ServiceException("Errore generico servizio get processo by Codice");
		} 
	}
	
	@Override
	public Response getProcessoByNome(String nomeProcesso, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Processo ruolo = processiService.getProcessoByNome(nomeProcesso);
			return Response.ok(ruolo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByNome] processo non trovato per nomeProcesso " + nomeProcesso);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByNome] Errore servizio getProcessoByNome",e);
			throw new ServiceException("Errore servizio get processo");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getProcessoByNome] Errore generico servizio getProcessoByNome",ex);
			throw new ServiceException("Errore generico servizio get processo by nome");
		} 
	}
	
	@Override
	public Response createProcesso(Processo body, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::createProcesso] IN Processo body: "+body);
			Processo ruolo = processiService.createProcesso(body);
			return Response.ok(ruolo).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::createProcesso] Errore servizio createProcesso",e);
			throw new ServiceException("Errore servizio crea processo");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createProcesso] Errore generico servizio createProcesso",ex);
			throw new ServiceException("Errore generico servizio crea processo");
		} 
	}
	
	@Override
	public Response putProcesso(Long idProcesso, Processo body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::putProcesso] IN idProcesso:"+idProcesso+"\nProcesso body: "+body);
			Processo ruolo = processiService.updateProcesso(idProcesso, body);
			return Response.ok(ruolo).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::putProcesso] Errore servizio putProcesso",e);
			throw new ServiceException("Errore servizio aggiorna processo");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::putProcesso] Errore generico servizio putProcesso",ex);
			throw new ServiceException("Errore generico servizio aggiorna processo");
		}
	}
	
	
	//
	// Moduli
	//
	@Override
	public Response getModuliByIdProcesso(Long idProcesso, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getUtentiByIdProcesso] BEGIN");
			List<Modulo> elenco = processiService.getModuliByIdProcesso(idProcesso);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getUtentiByIdProcesso] Errore servizio getUtentiByIdProcesso",e);
			throw new ServiceException("Errore servizio elenco moduli per idProcesso");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getUtentiByIdProcesso] Errore generico servizio getUtentiByIdProcesso",ex);
			throw new ServiceException("Errore generico servizio elenco moduli per idProcesso");
		} 
	}
	
}
