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

import it.csi.moon.commons.dto.TipoCodiceIstanza;
import it.csi.moon.moonsrv.business.be.TipiCodiceIstanzaApi;
import it.csi.moon.moonsrv.business.service.TipiCodiceIstanzaService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class TipiCodiceIstanzaApiImpl implements TipiCodiceIstanzaApi {
	
	private static final String CLASS_NAME = "TipoCodiceIstanzaApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	TipiCodiceIstanzaService tipoCodiceIstanzaService;
	
	@Override
	public Response getTipiCodiceIstanza(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getTipiCodiceIstanza] BEGIN");
			List<TipoCodiceIstanza> elenco = tipoCodiceIstanzaService.getElencoTipoCodiceIstanza();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getTipiCodiceIstanza] Errore servizio getTipiCodiceIstanza",e);
			throw new ServiceException("Errore servizio elencoTipiCodiceIstanza");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getTipiCodiceIstanza] Errore generico servizio getTipiCodiceIstanza",ex);
			throw new ServiceException("Errore generico servizio elenco TipiCodiceIstanza");
		}  
	}
	
	@Override
	public Response getTipoCodiceIstanzaById(Integer idTipoCodiceIstanza, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			TipoCodiceIstanza tipoCodiceIstanza = tipoCodiceIstanzaService.getTipoCodiceIstanzaById(idTipoCodiceIstanza);
			return Response.ok(tipoCodiceIstanza).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaById] TipoCodiceIstanzaById non trovato");
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaById] BusinessException", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaById] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio get TipoCodiceIstanza ById");
		} 
	}
	
	@Override
	public Response getTipoCodiceIstanzaByCodice(String descCodice, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			TipoCodiceIstanza tipoCodiceIstanza = tipoCodiceIstanzaService.getTipoCodiceIstanzaByCodice(descCodice);
			return Response.ok(tipoCodiceIstanza).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaByCodice] TipoCodiceIstanzaById non trovato");
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaByCodice] BusinessException", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getTipoCodiceIstanzaByCodice] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio get TipoCodiceIstanza ByCodice");
		} 
	}
	
}
