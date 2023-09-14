/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.extra.territorio.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonfobl.business.be.extra.territorio.GinevraApi;
import it.csi.moon.moonfobl.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.territorio.GinevraDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class GinevraApiImpl extends MoonBaseApiImpl implements GinevraApi {
	
	private static final String CLASS_NAME = "GinevraApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	@Qualifier("moonsrv")
	GinevraDAO ginevraDAO;
    
	//
	// SEDIMI
	@Override
	public Response getSedimi( 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			List<Via> result = new ArrayList<>();
			try {			
				result = ginevraDAO.getSedimi();
				return Response.ok(result).build();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getSedimi] BusinessException");
			throw new ServiceException("Errore generico servizio elenco getSedimi");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getSedimi] Errore generico servizio getSedimi", e);
			throw new ServiceException("Errore generico servizio elenco getSedimi");
		}
	}
	@Override
	public Response getSedimeById( String idPP,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			Via result = null;
			try {
				Long idTipoVia = validaLongRequired(idPP);
				result = ginevraDAO.getSedimeById(idTipoVia);
				return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getSedimeById] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getSedimeById] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
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
				
				result = ginevraDAO.getVie(idComune, nome);
				return Response.ok(result).build();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getVie] BusinessException");
			throw new ServiceException("Errore generico servizio elenco Vie");
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvgetVieince] Errore generico servizio getVie", e);
			throw new ServiceException("Errore generico servizio elenco getVie");
	    }
	}
	@Override
    public Response getViaById(String idComunePP, String idViaPP,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Via result = null;
		try {
			Long idComune = validaLong(idComunePP);
			Long idVia = validaLong(idViaPP);
			
			result = ginevraDAO.getViaById(idComune, idVia);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getViaById] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getViaById] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getViaById] BusinessException");
			throw new ServiceException("Errore generico servizio getViaById");
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
			
			result = ginevraDAO.getCivici(idL2, numero);
			return Response.ok(result).build();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getCivici] BusinessException");
			throw new ServiceException("Errore generico servizio getCivici");
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCivici] Errore generico servizio getCivici", e);
			throw new ServiceException("Errore generico servizio elenco getCivici");
	     }
	}
	@Override
	public Response getCivicoById(String idCivicoPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Via result = null;
		try {
			Long idCivico = validaLong(idCivicoPP);
			
			result = ginevraDAO.getCivicoById(idCivico);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] BusinessException");
			throw new ServiceException("Errore generico servizio getCivicoById");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Errore generico servizio getCivicoById", e);
			throw new ServiceException("Errore generico servizio getCivicoById");
		}
	}
	
}
