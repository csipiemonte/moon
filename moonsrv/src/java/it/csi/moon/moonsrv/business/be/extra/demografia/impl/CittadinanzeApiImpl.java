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

import it.csi.moon.commons.dto.extra.demografia.Cittadinanza;
import it.csi.moon.moonsrv.business.be.extra.demografia.CittadinanzeApi;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.CittadinanzeDAO;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class CittadinanzeApiImpl implements CittadinanzeApi {
	private static final String CLASS_NAME = "CittadinanzeApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	@Qualifier("mock")
	CittadinanzeDAO cittadinanzeDAO;

	@Override
	public Response getCittadinanze(int limit, int skip, String fields, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<Cittadinanza> ris = cittadinanzeDAO.findAll(limit, skip);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCittadinanze] Errore generico servizio getCittadinanze",ex);
			throw new ServiceException("Errore generico servizio elenco Cittadinanze");
		} 
	}
	
	public Response getCittadinanzeById(Integer codice, String fields, SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			Cittadinanza ris = cittadinanzeDAO.findByPK(codice);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Risorsa non trovata con codice: " + codice);
			}
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCittadinanzeById] Errore generico servizio getCittadinanzeById",ex);
			throw new ServiceException("Errore generico servizio getCittadinanzeById");
		} 
	}


}
