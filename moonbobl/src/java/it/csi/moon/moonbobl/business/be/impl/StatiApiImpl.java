/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.StatiApi;
import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.IstanzeService;
import it.csi.moon.moonbobl.business.service.StatiService;
import it.csi.moon.moonbobl.business.service.UtentiService;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorterBuilder;
import it.csi.moon.moonbobl.business.service.impl.dto.StatiFilter;
import it.csi.moon.moonbobl.dto.moonfobl.Stato;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class StatiApiImpl extends MoonBaseApiImpl implements StatiApi {
	
	private static final String CLASS_NAME = "StatiApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	StatiService statiService;
	@Autowired
	UtentiService utentiService;
	@Autowired
	private IstanzeService istanzeService;
	@Autowired
	AuditService auditService;
	
	@Override
	public Response getStati( Long idModulo, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getStati] BEGIN IN idModulo=" + idModulo);
			StatiFilter filter = new StatiFilter();
			filter.setIdModulo(idModulo);
			List<Stato> elenco = statiService.getElencoStati(filter);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getStati] BusinessException getStati", e);
			throw new ServiceException("Errore servizio getStati");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getRuoli] Exception getStati", ex);
			throw new ServiceException("Errore generico servizio getStati");
		}  
	}
	
	@Override
	public Response getStatiBoByModulo( Long idModulo, String sort, String filtroRicerca,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getStatiBoByModulo] IN idModulo: " + idModulo);
				LOG.debug("[" + CLASS_NAME + "::getStatiBoByModulo] IN sort: " + sort);
				LOG.debug("[" + CLASS_NAME + "::getStatiBoByModulo] IN filtroRicerca: " + filtroRicerca);
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			LOG.debug("[" + CLASS_NAME + "::getStatiBoByModulo] utente : "+ user.getIdentificativoUtente());
			// check abilitazione ad ente
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicerca);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato al modulo");
			}
			IstanzeFilter filter = new IstanzeFilter();
			filter.setIdModuli(Arrays.asList(idModulo));
			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			
			List<Stato> elenco = istanzeService.getElencoStatiBoSuElencoIstanze(filter, optSorter, filtroRicerca);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"StatiBoByModulo-idModulo", 
					idModulo.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(elenco).build();
			
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getStatiBoByModulo] Errore servizio getIstanze",e);
			throw new ServiceException("Errore servizio elenco stati BO by modulo");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getStatiBoByModulo] Errore generico servizio getIstanze",ex);
			throw new ServiceException("Errore generico servizio stati BO by modulo ");
		} 
	}
	
	@Override
	public Response getStatoById(Integer idStato, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Stato result = statiService.getStatoById(idStato);
			return Response.ok(result).build();
		} catch (ItemNotFoundBusinessException infbe) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] ItemNotFoundBusinessException idStato=" + idStato);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] Errore servizio getStatoById", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getStatoById] Errore generico servizio getStatoById", ex);
			throw new ServiceException("Errore generico servizio getStatoById");
		} 
	}
	
	@Override
	public Response getStatoByCd(String codiceStato, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Stato result = statiService.getStatoByCd(codiceStato);
			return Response.ok(result).build();
		} catch (ItemNotFoundBusinessException infbe) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] ItemNotFoundBusinessException codiceStato=" + codiceStato);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] Errore servizio getStatoByCd", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getStatoByCd] Errore generico servizio getStatoByCd", ex);
			throw new ServiceException("Errore generico servizio getStatoByCd");
		} 
	}
	
	@Override
	public Response createStato(Stato body, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::createStato] IN Stato body: " + body);
			Stato result = statiService.createStato(body);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createStato] Errore servizio createStato with body: " + body, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createStato] Errore generico servizio createStato", ex);
			throw new ServiceException("Errore generico servizio createStato");
		} 
	}
	
	@Override
	public Response putStato(Integer idStato, Stato body,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::putStato] IN idStato:" + idStato + " Stato body: " + body);
			Stato result = statiService.updateStato(idStato, body);
			return Response.ok(result).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::putStato] Errore servizio putStato for idStato:" + idStato + " Stato body: " + body, be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::putStato] Errore generico servizio putStato", ex);
			throw new ServiceException("Errore generico servizio putStato");
		}
	}
	
	@Override
	public Response deleteStatoById(Integer idStato, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			statiService.deleteStatoById(idStato);
			return Response.ok().build();
		} catch (ItemNotFoundBusinessException infbe) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] ItemNotFoundBusinessException idStato=" + idStato);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] Errore servizio deleteStatoById", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::deleteStatoById] Errore generico servizio deleteStatoById", ex);
			throw new ServiceException("Errore generico servizio deleteStatoById");
		} 
	}

}
