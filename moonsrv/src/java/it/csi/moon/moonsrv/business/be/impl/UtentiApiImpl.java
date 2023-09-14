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

import it.csi.moon.commons.dto.Funzione;
import it.csi.moon.commons.dto.Gruppo;
import it.csi.moon.commons.dto.Ruolo;
import it.csi.moon.commons.dto.Utente;
import it.csi.moon.moonsrv.business.be.UtentiApi;
import it.csi.moon.moonsrv.business.service.UtentiService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class UtentiApiImpl  implements UtentiApi {
	
	private static final String CLASS_NAME = "UtentiApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	UtentiService utentiService;

	@Override
	public Response getUtenti(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getUtenti] BEGIN");
			List<Utente> elenco = utentiService.getElencoUtenti();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getUtenti] Errore servizio getUtenti",e);
			throw new ServiceException("Errore servizio elenco utenti");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getUtenti] Errore generico servizio getUtenti",ex);
			throw new ServiceException("Errore generico servizio elenco utenti");
		}
	}

	@Override
	public Response getUtenteById(Long idUtente, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Utente utente = utentiService.getUtenteById(idUtente);
			return Response.ok(utente).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getUtenteById] utente non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getUtenteById] Errore servizio getUtenteById",e);
			throw new ServiceException("Errore servizio get utente");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getUtenteById] Errore generico servizio getUtenteById",ex);
			throw new ServiceException("Errore generico servizio get utente");
		}
	}

	@Override
	public Response getUtenteByIdentificativo(String identificativoUtente, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Utente utente = utentiService.getUtenteByIdentificativo(identificativoUtente);
			return Response.ok(utente).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getUtenteByIdentificativo] utente non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getUtenteByIdentificativo] Errore servizio getUtenteByIdentificativo",e);
			throw new ServiceException("Errore servizio get utente byIdentificativo");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getUtenteByIdentificativo] Errore generico servizio getUtenteByIdentificativo",ex);
			throw new ServiceException("Errore generico servizio get utente byIdentificativo");
		}
	}

	@Override
	public Response createUtente(Utente body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::createUtente] IN Utente body: "+body);
			Utente utente = utentiService.createUtente(body);
			return Response.ok(utente).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::createUtente] Errore servizio createUtente",e);
			throw new ServiceException("Errore servizio crea utente");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createUtente] Errore generico servizio createUtente",ex);
			throw new ServiceException("Errore generico servizio crea utente");
		} 
	}

	@Override
	public Response putUtente(Long idUtente, Utente body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::putUtente] IN idRuolo:"+idUtente+"\nRuolo body: "+body);
			Utente utente = utentiService.updateUtente(idUtente, body);
			return Response.ok(utente).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::putUtente] Errore servizio putUtente",e);
			throw new ServiceException("Errore servizio aggiorna utente");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::putUtente] Errore generico servizio putUtente",ex);
			throw new ServiceException("Errore generico servizio aggiorna utente");
		}
	}

	@Override
	public Response patchUtenteById(Long idUtente, Utente partialUtente, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::patchUtenteById] IN idRuolo:"+idUtente+"\nRuolo body: "+partialUtente);
			Utente utente = utentiService.patchUtente(idUtente, partialUtente);
			return Response.ok(utente).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::patchUtenteById] Errore servizio patchUtenteById",e);
			throw new ServiceException("Errore servizio aggiorna partial utente");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::patchUtenteById] Errore generico servizio patchUtenteById",ex);
			throw new ServiceException("Errore generico servizio aggiorna partial utente");
		}
	}


	//
	// Ruoli
	//
	@Override
	public Response getRuoliByIdUtente(Long idUtente, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			List<Ruolo> elenco = utentiService.getRuoliByIdUtente(idUtente);
			return Response.ok(elenco).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getRuoliByIdUtente] utente non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getRuoliByIdUtente] Errore servizio getRuoliByIdUtente",e);
			throw new ServiceException("Errore servizio elenco ruoli dell utente");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getRuoliByIdUtente] Errore generico servizio getRuoliByIdUtente",ex);
			throw new ServiceException("Errore generico servizio elenco ruoli dell utente");
		}
	}

	//
	// Funzioni
	//
	@Override
	public Response getFunzioniByIdUtente(Long idUtente, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			List<Funzione> elenco = utentiService.getFunzioniByIdUtente(idUtente);
			return Response.ok(elenco).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getFunzioniByIdUtente] utente non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getFunzioniByIdUtente] Errore servizio getFunzioniByIdUtente",e);
			throw new ServiceException("Errore servizio elenco funzioni dell utente");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getFunzioniByIdUtente] Errore generico servizio getFunzioniByIdUtente",ex);
			throw new ServiceException("Errore generico servizio elenco funzioni dell utente");
		}
	}

	//
	// Gruppi
	//
	@Override
	public Response getGruppiByIdUtente(Long idUtente, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			List<Gruppo> elenco = utentiService.getGruppiByIdUtente(idUtente);
			return Response.ok(elenco).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getGruppiByIdUtente] utente non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getGruppiByIdUtente] Errore servizio getGruppiByIdUtente",e);
			throw new ServiceException("Errore servizio elenco gruppi dell utente");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getGruppiByIdUtente] Errore generico servizio getGruppiByIdUtente",ex);
			throw new ServiceException("Errore generico servizio elenco gruppi dell utente");
		}
	}

}
