/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.notificatore.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.apirest.notify.preferences.v1.dto.ContactPreference;
import it.csi.apirest.notify.preferences.v1.dto.Service;
import it.csi.apirest.notify.preferences.v1.dto.UserPreferencesService;
import it.csi.apirest.notify.status.v1.dto.Status;
import it.csi.moon.moonsrv.business.be.extra.notificatore.NotificatoreApi;
import it.csi.moon.moonsrv.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotificatorePrefDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotificatoreStatusDAO;
import it.csi.moon.moonsrv.business.service.notificatore.NotificatoreService;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * API per l'uso del Notificatore - NOTIFY Message Broker
 * 
 * @author Laurent
 *
 */
@Component
public class NotificatoreApiImpl extends MoonBaseApiImpl implements NotificatoreApi {

	private static final String CLASS_NAME = "NotificatoreApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	NotificatoreService notificatoreService;
	@Autowired
	NotificatorePrefDAO notificatorePrefDAO;
	@Autowired
	NotificatoreStatusDAO notificatoreStatusDAO;
	
/*	
	@Override
	public Response inviaMessaggio(Message body, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			valida(body);
			String ris = notificatoreService.inviaMessaggio(body);
//			return Response.status(201).entity(ris).build();
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggio] UnprocessableEntityException",uee);
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggio] Errore generico servizio inviaMessaggio",ex);
			throw new ServiceException("Errore generico servizio inviaMessaggio");
		} 
	}
	private void valida(Message message) throws UnprocessableEntityException {
		if (message==null ||
			message.getPayload()==null) {
			throw new UnprocessableEntityException("Message non valido");
		}
	}
	@Override
	public Response getServices(String identitaDigitale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {	
			List<Service> services = notificatoreService.services(identitaDigitale);		
			return Response.ok(services).build();		
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getServices] UnprocessableEntityException",uee);
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getServices] Errore generico servizio getServices",ex);
			throw new ServiceException("Errore generico servizio getServices");
		} 
	}
	@Override
	public Response getStatus(String idMessaggio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {	
			Status status = notificatoreService.status(idMessaggio);
			return Response.ok(status).build();		
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getStatus] UnprocessableEntityException",uee);
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getStatus] Errore generico servizio getStatus",ex);
			throw new ServiceException("Errore generico servizio getStatus");
		} 
	}	
	@Override
	public Response getContacts(String identitaDigitale, String codiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			ContactPreference contactPref = notificatoreService.contacts(identitaDigitale, codiceFiscale);
			return Response.ok(contactPref).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getContacts] UnprocessableEntityException", uee);
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getContacts] Errore generico servizio getContacts", ex);
			throw new ServiceException("Errore generico servizio getContacts");
		}
	}
*/
	@Override
	public Response inviaRichiestaNotify(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::inviaRichiestaNotify] IN idIstanza: " + idIstanza);
			notificatoreService.inviaRichiestaNotify(idIstanza);
			LOG.debug("[" + CLASS_NAME + "::inviaRichiestaNotify] END");
			return Response.ok().build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::inviaRichiestaNotify] Errore generico servizio inviaRichiestaNotify", e);
			throw new ServiceException("Errore generico servizio inviaRichiestaNotify");
		}
	}


	//**********************************************************************
    //API x TEST
	@Override
	public Response getContacts(String endpointUrl, String token, String identitaDigitale, String cf,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ContactPreference result = notificatorePrefDAO.contatti(endpointUrl, token, identitaDigitale, cf);
		return Response.ok(result).build();
	}
	@Override
	public Response getAllServices(String endpointUrl, String token, String identitaDigitale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<Service> result = notificatorePrefDAO.services(endpointUrl, token, identitaDigitale);
		return Response.ok(result).build();
	}
	@Override
	public Response getPreferences(String endpointUrl, String token, String identitaDigitale, String cf,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<UserPreferencesService> result = notificatorePrefDAO.servicesByUser(endpointUrl, token, identitaDigitale, cf);
		return Response.ok(result).build();
	}
	@Override
	public Response getPreferencesByServiceName(String endpointUrl, String token, String identitaDigitale,
			String serviceName, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		UserPreferencesService result = notificatorePrefDAO.serviceByUserServiceName(endpointUrl, token, identitaDigitale, identitaDigitale, serviceName);
		return Response.ok(result).build();
	}
	@Override
	public Response getStatus(String endpointUrl, String token, String identitaDigitale, String idPayload,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Status result = notificatoreStatusDAO.status(endpointUrl, token, identitaDigitale, idPayload);
		return Response.ok(result).build();
	}


}
