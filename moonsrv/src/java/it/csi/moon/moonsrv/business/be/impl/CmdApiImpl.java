/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.management.AttributeList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.BuildInfo;
import it.csi.moon.commons.dto.CacheInfo;
import it.csi.moon.commons.dto.SysInfo;
import it.csi.moon.commons.util.CacheManager;
import it.csi.moon.commons.util.MoonCache;
import it.csi.moon.commons.util.decodifica.DecodificaMoonCache;
import it.csi.moon.moonsrv.business.be.CmdApi;
import it.csi.moon.moonsrv.business.service.BackendService;
import it.csi.moon.moonsrv.business.service.impl.dao.MoonprintDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class CmdApiImpl implements CmdApi {
	
	private static final String CLASS_NAME = "CmdApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	private BackendService beService;
	@Autowired
	private MoonprintDAO moonprintDAO;
	

	
	public Response ping(SecurityContext securityContext) {
		LOG.debug("[" + CLASS_NAME + "::ping] BEGIN END");
		
		Principal principal = null;
		if (securityContext != null) {
			principal = securityContext.getUserPrincipal();
			LOG.debug("[" + CLASS_NAME + "::ping] init principal=" + principal);
		}
		
		String response = beService.getMessage();
		return Response.ok(response).build();
	}
	
	public Response pingMoonprint(SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::pingMoonprint] BEGIN");
			String response = moonprintDAO.pingMoonprint();
			return Response.ok(response).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::pingMoonprint] Errore generico servizio pingMoonprint",ex);
			throw new ServiceException("Errore generico servizio pingMoonprint");
		} 
	}
	
	@Override
	public Response version(SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::version] BEGIN");
			String response = beService.getVersion();
			return Response.ok(response).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::version] Errore generico servizio version", ex);
			throw new ServiceException("Errore generico servizio version");
		} 
	}
	
	@Override
	public Response buildInfo(SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::buildInfo] BEGIN");
			BuildInfo response = beService.getBuildInfo();
			return Response.ok(response).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::buildInfo] Errore generico servizio buildInfo", ex);
			throw new ServiceException("Errore generico servizio buildInfo");
		} 
	}
	
	@Override
    public Response getAttributeList(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getAttributeList] BEGIN");
			AttributeList response = beService.getAttributeList();
			return Response.ok(response).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getAttributeList] Errore generico servizio getAttributeList", ex);
			throw new ServiceException("Errore generico servizio getAttributeList");
		} 
    }
	
	@Override
	public Response sysInfo(String fields,
			SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::sysInfo] BEGIN fields=" + fields);
			SysInfo response = beService.getSysInfo(fields);
			return Response.ok(response).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::sysInfo] BusinessException" + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::sysInfo] Errore generico servizio sysInfo", ex);
			throw new ServiceException("Errore generico servizio sysInfo");
		} 
	}
	
	@Override
	public Response getProp(SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getProp] BEGIN");
			Map response = beService.getProp();
			return Response.ok(response).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getProp] BusinessException" + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getProp] Errore generico servizio getProp", ex);
			throw new ServiceException("Errore generico servizio getProp");
		} 
	}
	
	@Override
	public Response getEnv(SecurityContext securityContext, HttpHeaders httpHeaders ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getEnv] BEGIN");
			Map response = beService.getEnv();
			return Response.ok(response).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getEnv] BusinessException" + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getEnv] Errore generico servizio getEnv", ex);
			throw new ServiceException("Errore generico servizio getEnv");
		} 
	}
	
    /**************************************
     * cache
     *************************************/
	
	@Override
	public Response keysCacheByCodice(String codice, SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::keysCacheByCodice] BEGIN");
			DecodificaMoonCache dm = DecodificaMoonCache.byCodice(codice);
			if ( dm == null) {
				LOG.error("[" + CLASS_NAME + "::keysCacheByCodice] codice non trovato in cache");
				throw new ResourceNotFoundException();
			}
			Optional<MoonCache> response = CacheManager.getInstance().getCacheByCodice(codice);
			if(response.isEmpty() ) {
				return Response.ok(null).build();	
			}
			return Response.ok(response.get().keys()).build();
		} catch (ServiceException se) {
			LOG.warn("[" + CLASS_NAME + "::keysCacheByCodice] Errore generico servizio keysCacheByCodice",se);
			throw se;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::keysCacheByCodice] Errore generico servizio keysCacheByCodice",ex);
			throw new ServiceException("Errore generico servizio keysCacheByCodice");
		} 
	}
	
	@Override
	public Response countCacheByCodice(String codice, SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::countCacheByCodice] BEGIN");
			DecodificaMoonCache dm = DecodificaMoonCache.byCodice(codice);
			if ( dm == null) {
				LOG.error("[" + CLASS_NAME + "::countCacheByCodice] codice non trovato in cache");
				throw new ResourceNotFoundException();
			}
			Optional<MoonCache> response = CacheManager.getInstance().getCacheByCodice(codice);
			if(response.isEmpty() ) {
				return Response.ok(null).build();	
			}
			return Response.ok(response.get().count()).build();
		} catch (ServiceException se) {
			LOG.warn("[" + CLASS_NAME + "::countCacheByCodice] Errore generico servizio countCacheByCodice",se);
			throw se;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::countCacheByCodice] Errore generico servizio countCacheByCodice",ex);
			throw new ServiceException("Errore generico servizio countCacheByCodice");
		} 
	}

	@Override
	public Response lastResetCacheByCodice(String codice, SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::lastResetCacheByCodice] BEGIN");
			DecodificaMoonCache dm = DecodificaMoonCache.byCodice(codice);
			if ( dm == null) {
				LOG.error("[" + CLASS_NAME + "::lastResetCacheByCodice] codice non trovato in cache");
				throw new ResourceNotFoundException();
			}
			Optional<MoonCache> response = CacheManager.getInstance().getCacheByCodice(codice);
			if(response.isEmpty() ) {
				return Response.ok(null).build();	
			}
			return Response.ok(""+response.get().getLastResetCache()).build();
		} catch (ServiceException se) {
			LOG.warn("[" + CLASS_NAME + "::lastResetCacheByCodice] Errore generico servizio lastResetCacheByCodice",se);
			throw se;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::lastResetCacheByCodice] Errore generico servizio lastResetCacheByCodice",ex);
			throw new ServiceException("Errore generico servizio lastResetCacheByCodice");
		} 
	}

	@Override
	public Response resetAll(String codice, SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::resetAll] BEGIN");
			DecodificaMoonCache dm = DecodificaMoonCache.byCodice(codice);
			if ( dm == null) {
				LOG.error("[" + CLASS_NAME + "::resetAll] codice non trovato in cache");
				throw new ResourceNotFoundException();
			}
			Optional<MoonCache> response = CacheManager.getInstance().getCacheByCodice(codice);
			if(response.isEmpty() ) {
				return Response.ok(null).build();	
			}
			response.get().resetAll();
			return Response.ok().build();
		} catch (ServiceException se) {
			LOG.warn("[" + CLASS_NAME + "::resetAll] Errore generico servizio resetAll",se);
			throw se;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::resetAll] Errore generico servizio resetAll",ex);
			throw new ServiceException("Errore generico servizio resetAll");
		} 
	}

	@Override
	public Response reset(String codice, String key, SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::reset] BEGIN");
			DecodificaMoonCache dm = DecodificaMoonCache.byCodice(codice);
			if ( dm == null) {
				LOG.error("[" + CLASS_NAME + "::reset] codice non trovato in cache");
				throw new ResourceNotFoundException();
			}
			Optional<MoonCache> response = CacheManager.getInstance().getCacheByCodice(codice);
			if(response.isEmpty() ) {
				return Response.ok(null).build();	
			}
			response.get().reset(key);
			return Response.ok().build();
		} catch (ServiceException se) {
			LOG.warn("[" + CLASS_NAME + "::reset] Errore generico servizio reset",se);
			throw se;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::reset] Errore generico servizio reset",ex);
			throw new ServiceException("Errore generico servizio reset");
		} 
	}

	@Override
	public Response info(SecurityContext securityContext, HttpHeaders httpHeaders) {
		try {
			LOG.debug("[" + CLASS_NAME + "::info] BEGIN");
			List<CacheInfo> result = CacheManager.getInstance(). getInfo();
			return Response.ok(result).build();
		} catch (ServiceException se) {
			LOG.warn("[" + CLASS_NAME + "::info] Errore generico servizio info",se);
			throw se;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::info] Errore generico servizio info",ex);
			throw new ServiceException("Errore generico servizio info");
		} 
	}
}
