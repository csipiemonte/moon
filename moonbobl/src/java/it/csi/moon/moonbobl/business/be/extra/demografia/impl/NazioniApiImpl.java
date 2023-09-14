/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.extra.demografia.impl;

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

import it.csi.moon.moonbobl.business.be.extra.demografia.NazioniApi;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.NazioneDAO;
import it.csi.moon.moonbobl.dto.extra.demografia.Nazione;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class NazioniApiImpl  implements NazioniApi {
	private final static String CLASS_NAME = "NazioniApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	@Autowired
	@Qualifier("moonsrv")
	NazioneDAO nazioneDAO;
	
	public Response getNazioneById(Integer codice,String fields,SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			Nazione ris = nazioneDAO.findByPK(codice);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Risorsa non trovata con codice: " + codice);
			}
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCittadinanzeById] Errore generico servizio getNazioneById",ex);
			throw new ServiceException("Errore generico servizio getNazioneById");
		} 
	}
	public Response getNazioni(String fields, SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			List<Nazione> ris = nazioneDAO.findAll();
			return Response.ok(ris).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCittadinanze] Errore generico servizio getCittadinanze",ex);
			throw new ServiceException("Errore generico servizio elenco Cittadinanze");
		} 
	}
}
