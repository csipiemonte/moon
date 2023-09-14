/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.extra.demografia.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.demografia.Nazione;
import it.csi.moon.moonfobl.util.LoggerAccessor;
import it.csi.moon.moonfobl.business.be.extra.demografia.NazioniApi;
import it.csi.moon.moonfobl.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia.NazioneDAO;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;

@Component
public class NazioniApiImpl extends MoonBaseApiImpl implements NazioniApi {
	
	private static final String CLASS_NAME = "NazioniApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	@Qualifier("moonsrv")
	NazioneDAO nazioneDAO;
	
	public Response getNazioni(String uso, String ue, String nome, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest/*, Request request, ServerCache cache*/ ) {
		try {
			List<Nazione> ris = new ArrayList<>();
			if (StringUtils.isEmpty(nome)) {
				ris = nazioneDAO.findAll(uso, ue);
			} else {
				ris = nazioneDAO.findByNome(nome.trim());
			}
//
// TEST Cache
//
	        EntityTag etag = new EntityTag(Integer.toString(ris.hashCode()));
//	        CacheControl cc = new CacheControl();
//	        cc.setMaxAge(86400); // 1d:86400, 30min:1800, 10min:600
//	        cc.setPrivate(true); // non è vero per nazioni ! solo per TEST
//	        Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
//	        if (rb != null) 
//	        {
//	            return rb.cacheControl(cc).tag(etag).build();
//	        }
	        
//	        return Response.ok().cacheControl(cc).tag(etag).entity(ris).build();
	        return Response.ok().tag(etag).entity(ris).build();
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getNazioni] Errore generico servizio getNazioni",ex);
			throw new ServiceException("Errore generico servizio elenco Nazioni");
		} 
	}
	
	public Response getNazioneById(Integer codice, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest/*, Request request, ServerCache cache*/ ) {
		try {
			Nazione ris = nazioneDAO.findByPK(codice);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Risorsa non trovata con codice: " + codice);
			}
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getNazioneById] Errore generico servizio getNazioneById",ex);
			throw new ServiceException("Errore generico servizio getNazioneById");
		} 
	}

}
