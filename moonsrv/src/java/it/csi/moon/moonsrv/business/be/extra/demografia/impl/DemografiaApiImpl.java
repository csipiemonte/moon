/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.extra.demografia.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Error;
import it.csi.moon.commons.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.commons.dto.extra.demografia.RelazioneParentela;
import it.csi.moon.moonsrv.business.be.extra.demografia.DemografiaApi;
import it.csi.moon.moonsrv.business.service.demografia.AnprService;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.AnprRelazioniParentelaDAO;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class DemografiaApiImpl  implements DemografiaApi {
	
	private static final String CLASS_NAME = "DemografiaApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	@Qualifier("mock")
    AnprRelazioniParentelaDAO relazioniParentelaDAO;
	@Autowired
	@Qualifier("RS")
	AnprService anprService;	
	
	@Override
	public Response getRelazioneParentelaById(Integer codice, String fields, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		RelazioneParentela ris = relazioniParentelaDAO.findByPK(codice);
		if (ris != null) {
			return Response.ok(ris).build();
		}
		else {
			Error err = new Error();
			err.setCode("404");
			err.setErrorMessage("relazioniParentela "+codice+" non trovata");
			return Response.serverError().entity(err).status(404).build();
		}
	}

	@Override
	public Response getRelazioniParentela(String fields, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<RelazioneParentela> ris = relazioniParentelaDAO.findAll();
		return Response.ok(ris).build();
	}


	/**
	 * Recupera tutti componenti della famiglia sulla base del codice fiscale di un menbro della famiglia
	 * via ORCHANPR
	 */
	@Override
	public Response getFamigliaANPR(String codiceFiscale, String fields, String ipAdress, String utente, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		
		List<ComponenteFamiglia> result = new ArrayList<>();
		
	    try {
	    	LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] BEGIN");
	    	LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] IN codiceFiscale: "+codiceFiscale);

	    	String jwtForRESTApiMint = null;
	    	result = anprService.getFamigliaANPR(codiceFiscale, jwtForRESTApiMint, ipAdress, utente);
	    	
	    	LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] OUT result: "+result);
	    	if (result!=null) {
	    		LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] OUT result.size(): "+result.size());
	    	}
	     } catch(Exception e) {
	    	 LOG.error("[" + CLASS_NAME + "::getFamigliaANPR] Exception "+e.getMessage());
	     }
	    
		return Response.ok(result).build();
	}
	
	
}
