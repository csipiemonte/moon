/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.territorio.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonsrv.business.be.extra.territorio.GinevraApi;
import it.csi.moon.moonsrv.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonsrv.business.service.territorio.GinevraService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class GinevraApiImpl extends MoonBaseApiImpl implements GinevraApi {
	
	private static final String CLASS_NAME = "GinevraApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	GinevraService ginevraService;
	
	//
	// SEDIMI
	@Override
	public Response getSedimi( 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<Via> result = new ArrayList<>();
		try {			
			result = ginevraService.getSedimi();
			return Response.ok(result).build();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getSedimi] BusinessException");
			throw new ServiceException("Errore generico servizio elenco Sedimi");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getSedimi] Errore generico servizio getSedimi", e);
			throw new ServiceException("Errore generico servizio elenco Sedimi");
		}
	}
	
	@Override
	public Response getSedimeById( String idPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Via result = null;
		try {
			Long idTipoVia = validaLongRequired(idPP);
			result = ginevraService.getSedimeById(idTipoVia);
			return Response.ok(result).build();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getSedimeById] BusinessException");
			throw new ServiceException("Errore generico servizio getSedimeById");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getSedimeById] Errore generico servizio getSedimeById", e);
			throw new ServiceException("Errore generico servizio getSedimeById");
		}
	}
	
	
	//
	// VIE
	@Override
	public Response getVie(String idComunePP,
		String nomeViaQP,
		int limit, int skip,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<Via> result = new ArrayList<>();
		try {
			Long idComune = validaLong(idComunePP);
			String nome = validaStringRequired(nomeViaQP);
			
			result = ginevraService.getVie(nome, idComune);
			return Response.ok(result).build();
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getVie] Errore generico servizio getVie", e);
			throw new ServiceException("Errore generico servizio elenco Vie");
	     }
	}
	@Override
    public Response getViaById(String idComunePP, String idViaPP,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Via result = null;
		try {
			Long idComune = validaLong(idComunePP);
			Long idVia = validaLong(idViaPP);
			
			result = ginevraService.getViaById(idComune, idVia);
			return Response.ok(result).build();
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getViaById] Errore generico servizio getViaById", e);
			throw new ServiceException("Errore generico servizio getViaById");
	     }
    }
    
    
	//
	// CIVICI
	@Override
	public Response getCivici(String idL2PP, String numeroPP,
		int limit, int skip,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<Via> result = new ArrayList<>();
		try {
			Long idL2 = validaLong(idL2PP);
			Long numero = validaLong(numeroPP);
			
			result = ginevraService.getCivici(idL2, numero);
			return Response.ok(result).build();
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCivici] Errore generico servizio getCivici", e);
			throw new ServiceException("Errore generico servizio elenco Civici");
	     }
	}
	@Override
	public Response getCivicoById(String idCivicoPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Via result = null;
		try {
			Long idCivico = validaLong(idCivicoPP);
			
			result = ginevraService.getCivicoById(idCivico);
			return Response.ok(result).build();
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Errore generico servizio getCivicoById", e);
			throw new ServiceException("Errore generico servizio getCivicoById");
	     }
	}
	
}
