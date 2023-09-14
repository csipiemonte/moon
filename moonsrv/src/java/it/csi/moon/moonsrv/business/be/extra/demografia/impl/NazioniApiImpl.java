/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.demografia.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.demografia.Nazione;
import it.csi.moon.moonsrv.business.be.extra.demografia.NazioniApi;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.NazioneDAO;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class NazioniApiImpl implements NazioniApi {
	private static final String CLASS_NAME = "NazioniApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	@Qualifier("mock")
	NazioneDAO nazioneDAO;
	
	public Response getNazioneById(Integer codice,String fields,SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			Nazione ris = nazioneDAO.findByPK(codice);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Risorsa non trovata con codice: " + codice);
			}
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getNazioneById] Errore generico servizio getNazioneById",ex);
			throw new ServiceException("Errore generico servizio getNazioneById");
		} 
	}
	public Response getNazioni(String fields, SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			List<Nazione> ris = nazioneDAO.findAll();
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getNazioni] Errore generico servizio getNazioni",ex);
			throw new ServiceException("Errore generico servizio elenco Nazioni");
		} 
	}
}
