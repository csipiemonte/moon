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

import it.csi.moon.moonbobl.business.be.EntiApi;
import it.csi.moon.moonbobl.business.service.AreeService;
import it.csi.moon.moonbobl.business.service.EntiService;
import it.csi.moon.moonbobl.business.service.impl.dto.EntiFilter;
import it.csi.moon.moonbobl.dto.moonfobl.Area;
import it.csi.moon.moonbobl.dto.moonfobl.Ente;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoUtente;

@Component
public class EntiApiImpl extends MoonBaseApiImpl implements EntiApi {
	
	private final static String CLASS_NAME = "EntiApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	EntiService entiService;
	@Autowired
	AreeService areeService;
	
	@Override
	public Response getEnti(String idEnteQP, String codiceEnteQP, String nomeEnteQP,
		String descrizioneEnteQP, String flagAttivoQP, String idTipoEnteQP,
		String logoQP, /*String nomePortaleQP,*/ String idPortaleQP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getEnti] BEGIN");
				log.debug("[" + CLASS_NAME + "::getEnti] IN idEnte: "+idEnteQP);
				log.debug("[" + CLASS_NAME + "::getEnti] IN codiceEnte: "+codiceEnteQP);
				log.debug("[" + CLASS_NAME + "::getEnti] IN nomeEnte: "+nomeEnteQP);
				log.debug("[" + CLASS_NAME + "::getEnti] IN descrizioneEnte: "+descrizioneEnteQP);
				log.debug("[" + CLASS_NAME + "::getEnti] IN flagAttivo: "+flagAttivoQP);
				log.debug("[" + CLASS_NAME + "::getEnti] IN idTipoEnte: "+idTipoEnteQP);
				log.debug("[" + CLASS_NAME + "::getEnti] IN logo: "+logoQP);
//				log.debug("[" + CLASS_NAME + "::getEnti] IN nomePortale: "+nomePortaleQP);
				log.debug("[" + CLASS_NAME + "::getEnti] IN idPortale: "+idPortaleQP);
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			//String nomePortale = (String) httpRequest.getSession().getAttribute(Constants.SESSION_PORTALNAME);
			//nomePortale = (nomePortale.equals("localhost")) ? "*" : nomePortale;
			String nomePortale = retrievePortalName(httpRequest);
			
			EntiFilter filter = new EntiFilter();
			filter.setIdEnte(validaLong(idEnteQP));
			filter.setCodiceEnte(codiceEnteQP);
			filter.setNomeEnte(nomeEnteQP);
			filter.setDescrizioneEnte(descrizioneEnteQP);
			filter.setFlAttivo(flagAttivoQP);
			filter.setIdTipoEnte(validaInteger(idTipoEnteQP));
			filter.setLogo(logoQP);			
			filter.setNomePortale(nomePortale); // Filtro obbligatorio
			filter.setIdPortale(validaLong(idPortaleQP));
			filter.setUtenteAbilitato(user.isMultiEntePortale()&&!DecodificaTipoUtente.ADM.isCorrectType(user)?user.getIdentificativoUtente():null);
			
			log.debug("[" + CLASS_NAME + "::getEnti] BEGIN");
			List<Ente> elenco = entiService.getElencoEnti(filter);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getEnti] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getEnti] Errore generico servizio getEnti", ex);
			throw new ServiceException("Errore generico servizio elenco enti");
		}  
	}
	
	@Override
	public Response getEnteById(Long idEnte, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Ente ente = entiService.getEnteById(idEnte);
			return Response.ok(ente).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getEnteById] ruolo non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getEnteById] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getEnteById] Errore generico servizio getEnteById",ex);
			throw new ServiceException("Errore generico servizio get ente");
		} 
	}
	
	@Override
	public Response getEnteByCd(String codiceEnte, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			Ente ente = entiService.getEnteByCodice(codiceEnte);
			return Response.ok(ente).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getEnteByCd] ruolo non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getEnteByCd] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getEnteByCd] Errore generico servizio getEnteByCd", ex);
			throw new ServiceException("Errore generico servizio get ente by codice");
		} 
	}
	
	@Override
	public Response createEnte(Ente body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			log.debug("[" + CLASS_NAME + "::createEnte] IN Ente body: " + body);
			Ente ente = entiService.createEnte(body);
			return Response.ok(ente).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::createEnte] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::createEnte] Errore generico servizio createEnte", ex);
			throw new ServiceException("Errore generico servizio crea ente");
		} 
	}
	
	@Override
	public Response putEnte(Long idEnte, Ente body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			log.debug("[" + CLASS_NAME + "::putEnte] IN idEnte:" + idEnte + "\nEnte body: " + body);
			Ente ente = entiService.updateEnte(idEnte, body);
			return Response.ok(ente).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::putEnte] BusinessException ", be);
			throw new ServiceException("Errore servizio aggiorna ente");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::putEnte] Errore generico servizio putEnte", ex);
			throw new ServiceException("Errore generico servizio aggiorna ente");
		}
	}
	
    public Response getAreeByIdEnte(Long idEnte, 
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getAreeByIdEnte] BEGIN");
				log.debug("[" + CLASS_NAME + "::getAreeByIdEnte] IN idEnte: " + idEnte);
			}
//			UserInfo user = retrieveUserInfo(httpRequest);
			//
//			String nomePortale = (String) httpRequest.getSession().getAttribute(Constants.SESSION_PORTALNAME);
//			nomePortale = (nomePortale.equals("localhost")) ? "*" : nomePortale;
			
			log.debug("[" + CLASS_NAME + "::getAreeByIdEnte] BEGIN");
			List<Area> elenco = areeService.getElencoAreaByIdEnte(idEnte);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getAreeByIdEnte] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getAreeByIdEnte] Errore generico servizio getEnti", ex);
			throw new ServiceException("Errore generico servizio getAreeByIdEnte");
		} 
    }

    public Response getAreaById(Long idEnte, Long idArea, 
        SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getAreaById] BEGIN");
				log.debug("[" + CLASS_NAME + "::getAreaById] IN idEnte: " + idEnte);
				log.debug("[" + CLASS_NAME + "::getAreaById] IN idArea: " + idArea);
			}
			Area ris = areeService.getAreaById(idArea);
			if (!ris.getIdEnte().equals(idEnte)) {
				log.error("[" + CLASS_NAME + "::getAreaById] idEnte " + idEnte + " non corrispondente all'area trovata " + ris);
				throw new ResourceNotFoundException();
			}
			return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getAreaById] ruolo non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getAreaById] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getAreaById] Errore generico servizio getAreaById", ex);
			throw new ServiceException("Errore generico servizio getAreaById");
		}
    }

}
