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

import it.csi.moon.moonbobl.business.be.ProtocolloMetadatiApi;
import it.csi.moon.moonbobl.business.service.ProtocolloMetadatoService;
import it.csi.moon.moonbobl.dto.moonfobl.ProtocolloMetadato;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class ProtocolloMetadatiApiImpl extends MoonBaseApiImpl implements ProtocolloMetadatiApi {
	
	private final static String CLASS_NAME = "ProtocolloMetadatiApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ProtocolloMetadatoService protocolloMetadatoService;
	
	@Override
	public Response getMetadati(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getMetadati] BEGIN");
			}
			List<ProtocolloMetadato> elenco = protocolloMetadatoService.getElenco();
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getMetadati] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getMetadati] Errore generico servizio getMetadati", ex);
			throw new ServiceException("Errore generico servizio elenco metadati");
		}  
	}
	
	@Override
	public Response getMetadatoById(Long idMetadato, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			ProtocolloMetadato ris = protocolloMetadatoService.getById(idMetadato);
			return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getMetadatoById] metadato non trovato idMetadato=" + idMetadato, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getMetadatoById] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getMetadatoById] Errore generico servizio getMetadatoById",ex);
			throw new ServiceException("Errore generico servizio get Metadato");
		} 
	}

}
