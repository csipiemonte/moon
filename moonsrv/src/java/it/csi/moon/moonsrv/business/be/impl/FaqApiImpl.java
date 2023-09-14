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

import it.csi.moon.commons.dto.Faq;
import it.csi.moon.moonsrv.business.be.FaqApi;
import it.csi.moon.moonsrv.business.service.FaqService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class FaqApiImpl implements FaqApi {
	
	private static final String CLASS_NAME = "FaqApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	FaqService faqService;
	
	@Override
	public Response getElencoFaq( Long idModulo,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getElencoFaq] BEGIN");
			List<Faq> elenco = faqService.getElencoFaq(idModulo);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoFaq] Errore servizio getElencoFaq",e);
			throw new ServiceException("Errore servizio elenco FAQ");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoFaq] Errore generico servizio getElencoFaq",ex);
			throw new ServiceException("Errore generico servizio elenco FAQ");
		}  
	}
	
	@Override
	public Response getFaqById( Long idFaq, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Faq result = faqService.getFaqById(idFaq);
			return Response.ok(result).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getFaqById] ruolo non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getFaqById] Errore servizio getFaqById",e);
			throw new ServiceException("Errore servizio get FAQ");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getFaqById] Errore generico servizio getFaqById",ex);
			throw new ServiceException("Errore generico servizio get FAQ");
		} 
	}
	
	@Override
	public Response createFaq( Faq body, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::createFaq] IN Faq body: "+body);
			Faq faq = faqService.createFaq(body);
			return Response.ok(faq).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::createFaq] Errore servizio createFaq",e);
			throw new ServiceException("Errore servizio crea FAQ");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createFaq] Errore generico servizio createFaq",ex);
			throw new ServiceException("Errore generico servizio crea FAQ");
		} 
	}
	
	@Override
	public Response putFaq( Long idFaq, Faq body, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::putFaq] IN idFaq:"+idFaq+"\nFaq body: "+body);
			Faq faq = faqService.updateFaq(idFaq, body);
			return Response.ok(faq).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::putFaq] Errore servizio putFaq",e);
			throw new ServiceException("Errore servizio aggiorna FAQ");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::putFaq] Errore generico servizio putFaq",ex);
			throw new ServiceException("Errore generico servizio aggiorna FAQ");
		}
	}
	
}
