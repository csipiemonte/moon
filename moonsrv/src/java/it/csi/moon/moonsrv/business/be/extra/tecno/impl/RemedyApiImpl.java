/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.tecno.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.apimint.troubleticketing.v1.dto.CategoriaApplicativa;
import it.csi.apimint.troubleticketing.v1.dto.CategoriaOperativaTicket;
import it.csi.apimint.troubleticketing.v1.dto.ConfigurationItem;
import it.csi.apimint.troubleticketing.v1.dto.Ente;
import it.csi.apimint.troubleticketing.v1.dto.LavorazioneTicket;
import it.csi.apimint.troubleticketing.v1.dto.RichiedenteDaAnagrafica;
import it.csi.apimint.troubleticketing.v1.dto.Ticket;
import it.csi.apimint.troubleticketing.v1.dto.TicketExpo;
import it.csi.apimint.troubleticketing.v1.dto.TicketSnapshot;
import it.csi.moon.moonsrv.business.be.extra.tecno.RemedyApi;
import it.csi.moon.moonsrv.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonsrv.business.service.tecno.RemedyService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class RemedyApiImpl extends MoonBaseApiImpl implements RemedyApi {
	
	private static final String CLASS_NAME = "RemedyApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	private RemedyService remedyService = null;

	@Override
	public Response getRichiedenteDaAnagrafica(String email, String cognome, String nome,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
	    	LOG.debug("[" + CLASS_NAME + "::getRichiedenteDaAnagrafica] BEGIN");
	    	LOG.debug("[" + CLASS_NAME + "::getRichiedenteDaAnagrafica] IN email: " + email);
	    	LOG.debug("[" + CLASS_NAME + "::getRichiedenteDaAnagrafica] IN cognome: " + cognome);
	    	LOG.debug("[" + CLASS_NAME + "::getRichiedenteDaAnagrafica] IN nome: " + nome);
			List<RichiedenteDaAnagrafica> ris = remedyService.getRichiedenteDaAnagrafica(email, cognome, nome);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::findRichiedenteDaAnagrafica] Errore generico servizio findRichiedenteDaAnagrafica",ex);
			throw new ServiceException("Errore generico servizio elenco findRichiedenteDaAnagrafica");
		} 
	}
	
	@Override
	public Response getRichiedenteDaAnagraficaById(String personId,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
	    	LOG.debug("[" + CLASS_NAME + "::getRichiedenteDaAnagraficaById] BEGIN");
	    	LOG.debug("[" + CLASS_NAME + "::getRichiedenteDaAnagraficaById] IN personId: " + personId);
	    	RichiedenteDaAnagrafica ris = remedyService.getRichiedenteDaAnagraficaById(personId);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getRichiedenteDaAnagraficaById] Errore generico servizio getRichiedenteDaAnagraficaById",ex);
			throw new ServiceException("Errore generico servizio elenco getRichiedenteDaAnagraficaById");
		} 
	}
	
	@Override
	public Response getEnti (
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
	    	LOG.debug("[" + CLASS_NAME + "::getEnti] BEGIN");
	    	List<Ente> ris = remedyService.getEnti();
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getEnti] BusinessException");
			throw new ServiceException("BusinessException generico servizio elenco getEnti");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getEnti] Errore generico servizio getEnti", ex);
			throw new ServiceException("Errore generico servizio elenco getEnti");
		} 
	}

	@Override
	public Response getCategorieOperative(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
	    	LOG.debug("[" + CLASS_NAME + "::getCategorieOperative] BEGIN");
			List<CategoriaOperativaTicket> ris = remedyService.getCategorieOperative();
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getCategorieOperative] BusinessException " + be.getMessage());
			throw new ServiceException(be.getMessage(), be.getCode());
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCategorieOperative] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio elenco getCategorieOperative");
		}
	}
	
	@Override
	public Response getCategorieApplicative(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getCategorieApplicative] BEGIN");
			List<CategoriaApplicativa> ris = remedyService.getCategorieApplicative();
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getCategorieApplicative] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCategorieApplicative] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio elenco getCategorieApplicative");
		}
	}
	
	@Override
	public Response getConfigurationItems( String personId,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getConfigurationItems] BEGIN");
			List<ConfigurationItem> ris = remedyService.getConfigurationItems(personId);
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getConfigurationItems] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getConfigurationItems] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio elenco getConfigurationItems");
		}		
	}
	
	
	//
	// Entity Ticket
	public Response createTicket(Ticket ticket,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::createTicket] BEGIN");
			Ticket ris = remedyService.createTicket(ticket);
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createTicket] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createTicket] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio elenco createTicket");
		}		
	}
	public Response getWorkinfoTicket(String ticketId,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getWorkinfoTicket] BEGIN");
			LavorazioneTicket ris = remedyService.getWorkinfoTicket(ticketId);
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getWorkinfoTicket] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getWorkinfoTicket] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio elenco getWorkinfoTicket");
		}		
	}
	public Response getLastUpdated(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getLastUpdated] BEGIN");
			List<TicketSnapshot> ris = remedyService.getLastUpdated();
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getLastUpdated] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getLastUpdated] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio elenco getLastUpdated");
		}		
	}
	public Response getLastRegistered(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getLastRegistered] BEGIN");
			List<TicketExpo> ris = remedyService.getLastRegistered();
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getLastRegistered] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getLastRegistered] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio elenco getLastRegistered");
		}		
	}

}
