/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.ModulisticaApi;
import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.ModuliService;
import it.csi.moon.moonbobl.business.service.ModulisticaService;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.PortaleModuloLogonModeEntity;
import it.csi.moon.moonbobl.dto.moonfobl.LogonMode;
import it.csi.moon.moonbobl.dto.moonfobl.PortaleLogonMode;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class ModulisticaApiImpl extends MoonBaseApiImpl implements ModulisticaApi {
	
	private final static String CLASS_NAME = "ModulisticaApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ModuliService moduliService;
	@Autowired
	ModulisticaService modulisticaService;
	@Autowired 
	AuditService auditService;
	
	@Override
	public Response getElencoLogonMode(
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getElencoLogonMode] BEGIN");
			List<LogonMode> elenco = modulisticaService.getElencoLogonMode();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getElencoLogonMode] Errore servizio getElencoLogonMode",e);
			throw new ServiceException("Errore servizio elenco LogonMode");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoLogonMode] Errore generico servizio getElencoLogonMode",ex);
			throw new ServiceException("Errore generico servizio elenco LogonMode");
		}
	}

	@Override
	public Response getPortaliLogonModeByIdModulo(String idModuloPP, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getPortaliLogonModeByIdModulo] IN idModuloPP: " + idModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			List<PortaleLogonMode> portaliLogonMode = modulisticaService.getPortaliLogonModeByIdModulo(user, idModulo);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"getPortaliLogonModeByIdModulo-idModulo", 
														idModulo.toString()));
			return Response.ok(portaliLogonMode).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getPortaliLogonModeByIdModulo] Errore UnprocessableEntityException " + idModuloPP);
			throw uee;
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getPortaliLogonModeByIdModulo] modulo non trovato " + idModuloPP);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getPortaliLogonModeByIdModulo] Errore BusinessException " + idModuloPP);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getPortaliLogonModeByIdModulo] Errore generico servizio getModuloById " + idModuloPP, ex);
			throw new ServiceException("Errore generico servizio getPortaliLogonModeByIdModulo");
		} 
	}

	private void verificaAbilitazioneModulo(Long idModulo, UserInfo user) throws ServiceException {
		boolean isUtenteAbilitato = moduliService.verificaAbilitazioneModulo(idModulo, user);
		if (!isUtenteAbilitato) {
			log.error("[" + CLASS_NAME + "::verificaAbilitazioneModulo] NonAbilitato " + user.getIdentificativoUtente() + " / " + idModulo);
			throw new ServiceException("Utente non abilitato al modulo indicato");
		}
	}

	@Override
	public Response postPortaleModuloLogonMode(PortaleModuloLogonModeEntity body,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postPortaleModuloLogonMode] IN body: " + body);
			}
			validaPortaleModuloLogonMode(body);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(body.getIdModulo(), user);
			PortaleLogonMode portaliLogonMode = modulisticaService.inserisciPortaleModuloLogonMode(user, body);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.INSERT,
														"postPortaleModuloLogonMode-idModulo-idPortale-idLogonMode", 
														body.getIdModulo().toString() + "-" + body.getIdPortale().toString() + "-" + body.getIdLogonMode().toString()));
			return Response.ok(portaliLogonMode).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::postPortaleModuloLogonMode] Errore UnprocessableEntityException " + body);
			throw uee;
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::postPortaleModuloLogonMode] modulo non trovato " + body);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postPortaleModuloLogonMode] Errore BusinessException " + body);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postPortaleModuloLogonMode] Errore generico servizio postPortaleModuloLogonMode " + body, ex);
			throw new ServiceException("Errore generico servizio postPortaleModuloLogonMode");
		}
	}

	private void validaPortaleModuloLogonMode(PortaleModuloLogonModeEntity body) {
		// TODO Auto-generated method stub
		
	}
	
    public Response deletePortaleModuloLogonMode( @PathParam("idPortale") String idPortalePP, @PathParam("idModulo") String idModuloPP,
        	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::deletePortaleModuloLogonMode] IN idPortalePP: " + idPortalePP + " idModuloPP: " + idModuloPP);
			}
			Long idPortale = validaLongRequired(idPortalePP);
			Long idModulo = validaLongRequired(idModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			modulisticaService.cancellaPortaleModuloLogonMode(user, idPortale, idModulo);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.DELETE,
														"deletePortaleModuloLogonMode-idModulo-idPortale", 
														idModuloPP  + "-" + idPortalePP));
			return Response.ok().build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::deletePortaleModuloLogonMode] Errore UnprocessableEntityException idPortalePP: " + idPortalePP + " idModuloPP: " + idModuloPP);
			throw uee;
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::deletePortaleModuloLogonMode] modulo non trovato idPortalePP: " + idPortalePP + " idModuloPP: " + idModuloPP);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::deletePortaleModuloLogonMode] Errore BusinessException idPortalePP: " + idPortalePP + " idModuloPP: " + idModuloPP);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::deletePortaleModuloLogonMode] Errore generico servizio deletePortaleModuloLogonMode idPortalePP: " + idPortalePP + " idModuloPP: " + idModuloPP, ex);
			throw new ServiceException("Errore generico servizio deletePortaleModuloLogonMode");
		}
    }
    
}
