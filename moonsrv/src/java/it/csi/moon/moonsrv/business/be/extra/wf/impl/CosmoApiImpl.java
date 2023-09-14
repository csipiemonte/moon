/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.wf.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.cosmo.callback.v1.dto.AggiornaStatoPraticaRequest;
import it.csi.cosmo.callback.v1.dto.Esito;
import it.csi.moon.moonsrv.business.be.extra.wf.CosmoApi;
import it.csi.moon.moonsrv.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonsrv.business.service.wf.CosmoService;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * API per l'uso di COSMO
 * 
 * @author Laurent
 *
 */

@Component
public class CosmoApiImpl extends MoonBaseApiImpl implements CosmoApi {

	private static final String CLASS_NAME = "CosmoApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	CosmoService cosmoService;

	@Override
    public Response callbackPutStatoPraticaV1(String idPratica, AggiornaStatoPraticaRequest body,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			validaStringRequired(idPratica);
			Esito result = cosmoService.callbackPutStatoPraticaV1(idPratica, body);
			return Response.ok(result).build();
		} catch (ItemNotFoundBusinessException nfe) {
			LOG.warn("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] ItemNotFoundBusinessException for idPratica=" + idPratica);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] Errore generico servizio callbackPutStatoPraticaV1", ex);
			throw new ServiceException("Errore generico servizio callbackPutStatoPraticaV1");
		}
    }
	
	@Override
    public Response getAllegato(String idPratica,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			validaStringRequired(idPratica);
			byte[] result = cosmoService.getAllegato(idPratica);
			return Response.ok(result).build();
		} catch (ItemNotFoundBusinessException nfe) {
			LOG.warn("[" + CLASS_NAME + "::getAllegato] ItemNotFoundBusinessException for idPratica=" + idPratica);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getAllegato] UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] Errore generico servizio getAllegato", ex);
			throw new ServiceException("Errore generico servizio getAllegato");
		}
    }
}
