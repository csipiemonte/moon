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

import it.csi.moon.moonbobl.business.be.UtentiApi;
import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.UtentiService;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.dto.extra.istat.CodiceNome;
import it.csi.moon.moonbobl.dto.moonfobl.Funzione;
import it.csi.moon.moonbobl.dto.moonfobl.Gruppo;
import it.csi.moon.moonbobl.dto.moonfobl.Ruolo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.Utente;
import it.csi.moon.moonbobl.dto.moonfobl.UtenteEnteAbilitato;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


@Component
public class UtentiApiImpl extends MoonBaseApiImpl implements UtentiApi {
	
	private static final String CLASS_NAME = "UtentiApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	private UtentiService utentiService;
	@Autowired 
	AuditService auditService;
	
	@Override
	public Response getUtenti(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getUtenti] BEGIN");
			List<Utente> elenco = utentiService.getElencoUtenti();
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getUtenti] Errore servizio getUtenti " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getUtenti] Errore generico servizio getUtenti",ex);
			throw new ServiceException("Errore generico servizio elenco utenti");
		}
	}
	
	@Override
	public Response getUtentiEnteAbilitato(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getUtentiEnteAbilitato] BEGIN");
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			List<UtenteEnteAbilitato> elenco = utentiService.getUtentiEnteAbilitato(user);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getUtentiEnteAbilitato] Errore servizio getUtentiEnteAbilitato " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getUtentiEnteAbilitato] Errore generico servizio getUtentiEnteAbilitato",ex);
			throw new ServiceException("Errore generico servizio elenco utenti dell'ente");
		}
	}
	
	@Override
	public Response getUtentiEnteNoAbilitato(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getUtentiEnteNoAbilitato] BEGIN");
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			List<UtenteEnteAbilitato> elenco = utentiService.getUtentiEnteNoAbilitato(user);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getUtentiEnteNoAbilitato] Errore servizio getUtentiEnteNoAbilitato " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getUtentiEnteNoAbilitato] Errore generico servizio getUtentiEnteNoAbilitato",ex);
			throw new ServiceException("Errore generico servizio elenco all utenti senza abilitazione sull'ente");
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
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getUtenteById] Errore servizio getUtenteById " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
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
			LOG.warn("[" + CLASS_NAME + "::getUtenteByIdentificativo] utente non trovato identificativoUtente= " + identificativoUtente);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getUtenteByIdentificativo] Errore servizio getUtenteByIdentificativo " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getUtenteByIdentificativo] Errore generico servizio getUtenteByIdentificativo",ex);
			throw new ServiceException("Errore generico servizio get utente byIdentificativo");
		}
	}

	@Override
	public Response createUtente(Utente body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::createUtente] IN Utente body: "+body);
			UserInfo user = retrieveUserInfo(httpRequest);
			Utente utente = utentiService.createUtente(user, body);
			return Response.ok(utente).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::createUtente] Errore servizio createUtente " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::createUtente] Errore generico servizio createUtente",ex);
			throw new ServiceException("Errore generico servizio crea utente");
		} 
	}

	@Override
	public Response putUtente(Long idUtente, Utente body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::putUtente] IN idRuolo:"+idUtente+"\nRuolo body: "+body);
			UserInfo user = retrieveUserInfo(httpRequest);
			Utente utente = utentiService.updateUtente(user, idUtente, body);
			return Response.ok(utente).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::putUtente] Errore servizio putUtente " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::putUtente] Errore generico servizio putUtente",ex);
			throw new ServiceException("Errore generico servizio aggiorna utente");
		}
	}

	@Override
	public Response patchUtenteById(Long idUtente, Utente partialUtente, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::patchUtenteById] IN idUtente:"+idUtente+"\npartialUtente body: "+partialUtente);
			UserInfo user = retrieveUserInfo(httpRequest);
			Utente utente = utentiService.patchUtente(user, idUtente, partialUtente);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.UPDATE,
					"PATCH_Utente-idUtente", 
					partialUtente.toString() );
			auditService.traceOperazione(auditEntity);

			return Response.ok(utente).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::patchUtenteById] Errore servizio patchUtenteById " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
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
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getRuoliByIdUtente] Errore servizio getRuoliByIdUtente " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getRuoliByIdUtente] Errore generico servizio getRuoliByIdUtente",ex);
			throw new ServiceException("Errore generico servizio elenco ruoli dell utente");
		}
	}

    public Response postEnteAreaRuolo(String idUtentePP, String idEntePP, String idAreaPP, String idRuoloPP, 
        SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException {
		try {
			Long idUtente = validaLongRequired(idUtentePP);
			Long idEnte = validaLongRequired(idEntePP);
			Long idArea = validaLongRequired(idAreaPP);
			Integer idRuolo = validaIntegerRequired(idRuoloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			UtenteEnteAbilitato ris = utentiService.addEnteAreaRuolo(user, idUtente, idEnte, idArea, idRuolo);
			return Response.ok(ris).build();
		} catch(UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::postEnteAreaRuolo] Errore UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postEnteAreaRuolo] utente non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postEnteAreaRuolo] Errore servizio postEnteAreaRuolo " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::postEnteAreaRuolo] Errore generico servizio postEnteAreaRuolo",ex);
			throw new ServiceException("Errore generico servizio postEnteAreaRuolo");
		}
    }
    public Response deleteEnteAreaRuolo(String idUtentePP, String idEntePP, String idAreaPP, String idRuoloPP, 
        SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException {
		try {
			Long idUtente = validaLongRequired(idUtentePP);
			Long idEnte = validaLongRequired(idEntePP);
			Long idArea = validaLongRequired(idAreaPP);
			Integer idRuolo = validaIntegerRequired(idRuoloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			UtenteEnteAbilitato ris = utentiService.deleteEnteAreaRuolo(user, idUtente, idEnte, idArea, idRuolo);
			return Response.ok(ris).build();
		} catch(UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::deleteEnteAreaRuolo] Errore UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::deleteEnteAreaRuolo] utente non trovato", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::deleteEnteAreaRuolo] Errore servizio deleteEnteAreaRuolo " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::deleteEnteAreaRuolo] Errore generico servizio deleteEnteAreaRuolo",ex);
			throw new ServiceException("Errore generico servizio deleteEnteAreaRuolo");
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
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getFunzioniByIdUtente] Errore servizio getFunzioniByIdUtente " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
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
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getGruppiByIdUtente] Errore servizio getGruppiByIdUtente " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getGruppiByIdUtente] Errore generico servizio getGruppiByIdUtente",ex);
			throw new ServiceException("Errore generico servizio elenco gruppi dell utente");
		}
	}

	@Override
	public Response getComuniAbilitati(SecurityContext securityContext, HttpHeaders httpHeaders , HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getComuniAbilitati] start ");
			UserInfo user = retrieveUserInfo(httpRequest);

			List<CodiceNome> ris = utentiService.getComuniAbilitati(user);
			
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Non sono stati trovati comune abilitati per l'utente " + user.getIdentificativoUtente());
			}
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getComuniByProvincia] Errore generico servizio getComuniByProvincia", ex);
			throw new ServiceException("Errore generico servizio elenco getComuniByProvincia");
		}
	}
	
    // Moduli Abilitati
	@Override
	public Response getEntiAbilitati(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getEntiAbilitati] start ");
			UserInfo user = retrieveUserInfo(httpRequest);

			List<CodiceNome> ris = utentiService.getEntiAbilitati(user);
			
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Non sono stati trovati enti abilitati per l'utente " + user.getIdentificativoUtente());
			}
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getEntiAbilitati] Errore generico servizio getEntiAbilitati", ex);
			throw new ServiceException("Errore generico servizio elenco getEntiAbilitati");
		}
	}

	@Override
	public Response postUtenteModulo(String idUtentePP, String idModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::postUtenteModulo] BEGIN ");
			LOG.debug("[" + CLASS_NAME + "::postUtenteModulo] IN idUtentePP="+idUtentePP+"  idModuloPP="+idModuloPP);
			Long idUtente = validaLongRequired(idUtentePP);
			Long idModulo = validaLongRequired(idModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);

			utentiService.addUtenteModulo(user, idUtente, idModulo);
			
			return Response.ok().build();
		} catch(UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::postUtenteModulo] Errore UnprocessableEntityException idUtentePP="+idUtentePP+"  idModuloPP="+idModuloPP);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postUtenteModulo] Errore servizio postUtenteModulo " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::postUtenteModulo] Errore generico servizio postUtenteModulo idUtentePP="+idUtentePP+"  idModuloPP="+idModuloPP, ex);
			throw new ServiceException("Errore generico servizio elenco postUtenteModulo");
		}
	}
	
	@Override
	public Response deleteUtenteModulo(String idUtentePP, String idModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteUtenteModulo] BEGIN ");
			LOG.debug("[" + CLASS_NAME + "::deleteUtenteModulo] IN idUtentePP="+idUtentePP+"  idModuloPP="+idModuloPP);
			Long idUtente = validaLongRequired(idUtentePP);
			Long idModulo = validaLongRequired(idModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);

			utentiService.deleteUtenteModulo(user, idUtente, idModulo);
			
			return Response.ok().build();
		} catch(UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::deleteUtenteModulo] Errore UnprocessableEntityException idUtentePP="+idUtentePP+"  idModuloPP="+idModuloPP);
			throw uee;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::deleteUtenteModulo] Errore servizio deleteUtenteModulo " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::deleteUtenteModulo] Errore generico servizio deleteUtenteModulo idUtentePP="+idUtentePP+"  idModuloPP="+idModuloPP, ex);
			throw new ServiceException("Errore generico servizio elenco deleteUtenteModulo");
		}
	}
}
