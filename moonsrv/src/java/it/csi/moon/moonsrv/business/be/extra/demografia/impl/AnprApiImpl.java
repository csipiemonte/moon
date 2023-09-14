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

import it.csi.apimint.demografia.v1.dto.Soggetto;
import it.csi.moon.commons.dto.Error;
import it.csi.moon.commons.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.commons.dto.extra.demografia.Nazione;
import it.csi.moon.commons.dto.extra.demografia.RelazioneParentela;
import it.csi.moon.moonsrv.business.be.extra.demografia.AnprApi;
import it.csi.moon.moonsrv.business.service.demografia.AnprNazioneService;
import it.csi.moon.moonsrv.business.service.demografia.AnprService;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.AnprRelazioniParentelaDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class AnprApiImpl  implements AnprApi {
	
	private static final String CLASS_NAME = "AnprApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	@Qualifier("mock")
    AnprRelazioniParentelaDAO relazioniParentelaDAO;
	@Autowired
	AnprNazioneService anprNazioneService;
	@Autowired
	@Qualifier("RS")
	AnprService anprServiceRS;
	
    //
    // RELAZIONI PARENTELA - ANPR
    //
	@Override
	public Response getRelazioniParentela(String fields, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<RelazioneParentela> ris = relazioniParentelaDAO.findAll();
		return Response.ok(ris).build();
	}
	
	@Override
	public Response getRelazioneParentelaById(Integer codice, String fields, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
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


    //
    // NAZIONI - DA SATI ESTERI ANPR
    //
	@Override
	public Response getNazioni(String uso, String ue, String fields, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<Nazione> ris = anprNazioneService.listaNazioni(uso, ue);
			return Response.ok(ris).build();
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getNazioni] Errore generico servizio getNazioni",ex);
			throw new ServiceException("Errore generico servizio elenco Nazioni");
		} 
	}
	
	@Override
	public Response getNazioneById(Integer codice, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Nazione ris = anprNazioneService.getNazioneById(codice);
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
	
	
	/**
	 * Recupera tutti componenti della famiglia sulla base del codice fiscale di un membro della famiglia
	 * via ORCHANPR
	 */
	@Override
    public Response getComponentiFamigliaANPR(String codiceFiscale, 
    	String userJwt, String clientProfileKey, 
    	String fields,
    	String ipAdress, String utente,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		
		List<ComponenteFamiglia> result = new ArrayList<>();
		
	    try {
	    	LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] BEGIN");
	    	LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] IN codiceFiscale: "+codiceFiscale);
	    	LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] IN userJwt: "+userJwt); // Solo preso da Shibboleth
	    	LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] IN clientProfileKey: "+clientProfileKey); // default Constants.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO
//	    	LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] IN consumerPrefix: "+consumerPrefix); // default null, "covid." per modulo BUONO
	    	LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] IN fields: "+fields);
	    	LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] IN ipAdress: "+ipAdress);
	    	LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] IN utente: "+utente);
	    	
	    	result = anprServiceRS.getFamigliaANPR(codiceFiscale, userJwt, clientProfileKey, ipAdress, utente);
	    	
	    	LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] OUT result: "+result);
	    	if (result!=null) {
	    		LOG.debug("[" + CLASS_NAME + "::getComponentiFamigliaANPR] OUT result.size(): "+result.size());
	    	}
	     } catch(Exception e) {
	    	 LOG.error("[" + CLASS_NAME + "::getComponentiFamigliaANPR] Exception "+e.getMessage());
	     }
	    
		return Response.ok(result).build();
	}
	
	
	/**
	 * Recupera tutti componenti della famiglia sulla base del codice fiscale di un membro della famiglia
	 * via ORCHANPR
	 */
	@Override
    public Response getSoggettiFamigliaByCF( String codiceFiscale, 
		String userJwt, String clientProfileKey, 
		String fields, 
		String ipAdress, String utente,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	
		List<Soggetto> result = new ArrayList<>();
		
	    try {
	    	if (LOG.isDebugEnabled()) {
		    	LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] BEGIN");
		    	LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] IN codiceFiscale: "+codiceFiscale);
		    	LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] IN userJwt: "+userJwt); // Solo preso da Shibboleth
		    	LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] IN clientProfileKey: "+clientProfileKey); // default Constants.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO
//		    	LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] IN consumerPrefix: "+consumerPrefix); // default null, "covid." per modulo BUONO
		    	LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] IN fields: "+fields);
		    	LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] IN ipAdress: "+ipAdress);
		    	LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] IN utente: "+utente);
	    	}
	    	
    		result = anprServiceRS.getSoggettiFamigliaANPR(codiceFiscale, userJwt, clientProfileKey, /*consumerPrefix,*/ ipAdress, utente);
	    	
    		if (LOG.isDebugEnabled()) {
		    	LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] OUT result: "+result);
		    	if (result!=null) {
		    		LOG.debug("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] OUT result.size(): "+result.size());
		    	}
    		}
	     } catch(BusinessException be) {
	    	 LOG.warn("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] BusinessException " + be.getMessage());
	     } catch(Exception e) {
	    	 LOG.error("[" + CLASS_NAME + "::getSoggettiFamigliaByCF] Exception "+e.getMessage());
	     }
	    
		return Response.ok(result).build();
	}

	
	/**
	 * Recupera un soggetto completo ANPR sulla base del codice fiscale
	 * via ORCHANPR
	 */
	@Override
    public Response getSoggettoANPR(String codiceFiscale,
    	String userJwt, String clientProfileKey, 
    	String fields, 
    	String ipAdress, String utente,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		
		Soggetto result = null;
		
	    try {
	    	LOG.debug("[" + CLASS_NAME + "::getSoggettoANPR] BEGIN");
	    	LOG.debug("[" + CLASS_NAME + "::getSoggettoANPR] IN codiceFiscale: "+codiceFiscale);
	    	LOG.debug("[" + CLASS_NAME + "::getSoggettoANPR] IN userJwt: "+userJwt); // Solo preso da Shibboleth
	    	LOG.debug("[" + CLASS_NAME + "::getSoggettoANPR] IN clientProfileKey: "+clientProfileKey); // default Constants.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO
//	    	LOG.debug("[" + CLASS_NAME + "::getSoggettoANPR] IN consumerPrefix: "+consumerPrefix); // default null, "covid." per modulo BUONO
	    	LOG.debug("[" + CLASS_NAME + "::getSoggettoANPR] IN fields: "+fields);
	    	LOG.debug("[" + CLASS_NAME + "::getSoggettoANPR] IN ipAdress: "+ipAdress);
	    	LOG.debug("[" + CLASS_NAME + "::getSoggettoANPR] IN utente: "+utente);
	    	
    		result = anprServiceRS.getSoggettoANPR(codiceFiscale, userJwt, clientProfileKey, /*consumerPrefix,*/ ipAdress, utente);
	    	
	    	LOG.debug("[" + CLASS_NAME + "::getSoggettoANPR] OUT result: "+result);
	     } catch(Exception e) {
	    	 LOG.error("[" + CLASS_NAME + "::getSoggettoANPR] Exception "+e.getMessage());
	     }
	    
		return Response.ok(result).build();
	}
	
}
