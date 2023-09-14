/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.RichiestaSupporto;
import it.csi.moon.commons.entity.RichiestaSupportoFilter;
import it.csi.moon.moonfobl.business.be.SupportoApi;
import it.csi.moon.moonfobl.business.service.SupportoService;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class SupportoApiImpl extends MoonBaseApiImpl implements SupportoApi {
	
	private static final String CLASS_NAME = "SupportoApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	SupportoService supportoService;
	
	@Override
	public Response getElencoSupporto( Long idRichiestaSupporto, Long idIstanza, 
	    	Long idModulo, String flagInAttesaDiRisposta, 
	    	String descMittente, String emailMittente, String attoreIns, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getElencoSupporto] BEGIN");
			RichiestaSupportoFilter filter = new RichiestaSupportoFilter();
			filter.setIdRichiestaSupporto(idRichiestaSupporto);
			filter.setIdIstanza(idIstanza);
			filter.setIdModulo(idModulo);
			filter.setFlagInAttesaDiRisposta(flagInAttesaDiRisposta);
			filter.setDescMittente(descMittente);
			filter.setEmailMittente(emailMittente);
			filter.setAttoreIns(attoreIns);
			List<RichiestaSupporto> elenco = supportoService.getElencoRichiestaSupporto(filter);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoSupporto] Errore servizio getElencoSupporto",e);
			throw new ServiceException("Errore servizio elenco richieste supporto");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoSupporto] Errore generico servizio getElencoSupporto",ex);
			throw new ServiceException("Errore generico servizio elenco richieste supporto");
		}  
	}
	
	@Override
	public Response getRichiestaSupportoById( Long idRichiestaSupporto, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			RichiestaSupporto result = supportoService.getRichiestaSupportoById(idRichiestaSupporto);
			return Response.ok(result).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getRichiestaSupportoById] richiesta supporto non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getRichiestaSupportoById] Errore servizio getRichiestaSupportoById",e);
			throw new ServiceException("Errore servizio get richiesta supporto");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getRichiestaSupportoById] Errore generico servizio getRichiestaSupportoById",ex);
			throw new ServiceException("Errore generico servizio get richiesta supporto");
		} 
	}
	
	@Override
	public Response createRichiesta( RichiestaSupporto body, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::createRichiesta] IN RichiestaSupporto body: "+body);
			validaRichiestaForInsert(body);
			
			RichiestaSupporto result = supportoService.createRichiestaSupporto(body);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::createRichiesta] Errore UnprocessableEntityException");
			throw uee;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::createRichiesta] Errore servizio createRichiesta",e);
			throw new ServiceException("Errore servizio crea FAQ");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::createRichiesta] Errore generico servizio createRichiesta",ex);
			throw new ServiceException("Errore generico servizio crea richiesta supporto");
		} 
	}

	private void validaRichiestaForInsert(RichiestaSupporto body) throws UnprocessableEntityException {
		required(body.getIdModulo(),"idModulo");
		required(body.getIdIstanza(),"idIstanza");
		if (body.getMessaggi()==null || body.getMessaggi().isEmpty()) {
			throw new UnprocessableEntityException("One message required in messaggi.");
		}
		if (body.getMessaggi().size()>1) {
			throw new UnprocessableEntityException("Only ONE message required in messaggi.");
		}
		required(body.getMessaggi().get(0).getContenuto(),"messaggi[0].contenuto");
		required(body.getMessaggi().get(0).getAttoreIns(),"messaggi[0].attoreIns");
	}
	
//	@Override
//	public Response putRichiesta( Long idRichiestaSupporto, RichiestaSupporto body, 
//			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//		try {
//			LOG.debug("[" + CLASS_NAME + "::putRichiesta] IN idRichiestaSupporto:"+idRichiestaSupporto+"\nFaq body: "+body);
//			Faq faq = supportoService. .(idRichiestaSupporto, body);
//			return Response.ok(faq).build();
//		} catch (BusinessException e) {
//			LOG.error("[" + CLASS_NAME + "::putRichiesta] Errore servizio putFaq",e);
//			throw new ServiceException("Errore servizio aggiorna FAQ");
//		} catch (Throwable ex) {
//			LOG.error("[" + CLASS_NAME + "::putRichiesta] Errore generico servizio putFaq",ex);
//			throw new ServiceException("Errore generico servizio aggiorna FAQ");
//		}
//	}
	
}
