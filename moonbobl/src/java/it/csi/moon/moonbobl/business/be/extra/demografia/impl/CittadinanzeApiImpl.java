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

import it.csi.moon.moonbobl.business.be.extra.demografia.CittadinanzeApi;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.CittadinanzeDAO;
import it.csi.moon.moonbobl.dto.extra.demografia.Cittadinanza;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class CittadinanzeApiImpl  implements CittadinanzeApi {
	private final static String CLASS_NAME = "CittadinanzeApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	@Qualifier("moonsrv")
	CittadinanzeDAO cittadinanzeDAO;
	
//	public Response getCittadinanze(String fields,SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
//		ArrayList<Cittadinanza> ris = cittadinanzeDAO.findAll();
//		return Response.ok(ris).build();
//	}

	@Override
	public Response getCittadinanze(int limit, int skip, String fields, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<Cittadinanza> ris = cittadinanzeDAO.findAll(limit, skip);
			return Response.ok(ris).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCittadinanze] Errore generico servizio getCittadinanze",ex);
			throw new ServiceException("Errore generico servizio elenco Cittadinanze");
		} 
	}
	
	public Response getCittadinanzeById(Integer codice, String fields, SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException {
		try {
			Cittadinanza ris = cittadinanzeDAO.findByPK(codice);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Risorsa non trovata con codice: " + codice);
			}
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCittadinanzeById] Errore generico servizio getCittadinanzeById",ex);
			throw new ServiceException("Errore generico servizio getCittadinanzeById");
		} 
	}


}
