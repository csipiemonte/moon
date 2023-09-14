/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.edilizia.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.edilizia.PraticaEdilizia;
import it.csi.moon.moonsrv.business.be.extra.edilizia.GestionePraticheApi;
import it.csi.moon.moonsrv.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonsrv.business.service.edilizia.GestionePraticheService;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class GestionePraticheApiImpl extends MoonBaseApiImpl implements GestionePraticheApi {
	
	private static final String CLASS_NAME = "GestionePraticheApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
//	@Qualifier("RS")
	private GestionePraticheService gestionePraticheService;


	@Override
	public Response getPratica(String registroPP, String progressivoPP, String annoPP,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ServiceException {
		try {
			
			Integer registro = validaIntegerRequired(registroPP);
			Integer progressivo = validaIntegerRequired(progressivoPP);
			Integer anno = validaIntegerRequired(annoPP);
						
			PraticaEdilizia result = gestionePraticheService.getPratica(registro, progressivo, anno);
						
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException("Pratica edilizia non trovata");
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPratica] Risorsa non trovata con ..."+ "");
			throw new ResourceNotFoundException(notFoundEx.getMessage());
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getPratica] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPratica] Errore generico servizio getPratica", ex);
			throw new ServiceException("Errore generico servizio getPratica");
		}
	}
	
	@Override
	public Response getPratiche(String registroQP, String progressivoQP, String annoQP, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			Integer registro = validaIntegerRequired(registroQP);
			Integer progressivo = validaIntegerRequired(progressivoQP);
			Integer anno = validaIntegerRequired(annoQP);
						
			List<PraticaEdilizia> result = gestionePraticheService.getPratiche(registro, progressivo, anno);
						
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException("Pratiche edilizie non trovate");
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPratiche] Risorsa non trovata con ..."+ "");
			throw new ResourceNotFoundException(notFoundEx.getMessage());
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getPratiche] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPratiche] Errore generico servizio getPratiche", ex);
			throw new ServiceException("Errore generico servizio getPratiche");
		}
	}

	@Override
	public Response getPraticaJson(String registroPP, String progressivoPP, String annoPP, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			
			Integer registro = validaIntegerRequired(registroPP);
			Integer progressivo = validaIntegerRequired(progressivoPP);
			Integer anno = validaIntegerRequired(annoPP);
			
			String result = gestionePraticheService.getPraticaJson(registro, progressivo, anno);
						
			if (result != null) {
				return Response.ok(result).build();
			} else {
				throw new ResourceNotFoundException("Pratica edilizia non trovata");
			}
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getPraticaJson] Risorsa non trovata con ..."+ "");
			throw new ResourceNotFoundException(notFoundEx.getMessage());
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getPraticaJson] Errore UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getPraticaJson] Errore generico servizio getPraticaJson", ex);
			throw new ServiceException("Errore generico servizio getPraticaJson");
		}
	}

	@Override
	public Response getPing(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ServiceException {
			return Response.ok().build();
	}


	


}
