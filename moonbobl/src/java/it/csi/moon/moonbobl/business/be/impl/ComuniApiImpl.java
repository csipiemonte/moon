/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.ComuniApi;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.ComuneDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.ProvinciaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.RegioneDAO;
import it.csi.moon.moonbobl.dto.extra.istat.Comune;
import it.csi.moon.moonbobl.dto.extra.istat.Provincia;
import it.csi.moon.moonbobl.dto.extra.istat.Regione;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Deprecated // DA CONTROLLARE SE IL PATH VIENE USATO IN ALCUNI MODULI, PRIMA DI ELIMINARE USARE IstatApi /extra/istat/
@Component
public class ComuniApiImpl implements ComuniApi {
	
	private final static String CLASS_NAME = "RegioniApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	@Qualifier("moon")
	RegioneDAO regioneDAO;
	@Autowired
	@Qualifier("moon")
	ProvinciaDAO provinciaDAO;
	@Autowired
	@Qualifier("moon")
	ComuneDAO comuneDAO;
	
	@Override
	public Response getComuneById(Integer codiceRegione, Integer codiceProvincia, Integer codiceComune, String fields,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getComuneById] IN codiceRegione: " + codiceRegione);
			log.debug("[" + CLASS_NAME + "::getComuneById] IN codiceProvincia: " + codiceProvincia);
			log.debug("[" + CLASS_NAME + "::getComuneById] IN codiceComune: " + codiceComune);
//			UserInfo user = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			Comune ris = comuneDAO.findByPKidx(codiceProvincia, codiceComune);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Comune " + codiceComune + " non trovato.");
			}
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getComuneById] Errore generico servizio getComuneById", ex);
			throw new ServiceException("Errore generico servizio getComuneById");
		}
	}
	
	@Override
	public Response getComuniByProvincia(Integer codiceRegione, Integer codiceProvincia, String fields,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getComuniByProvincia] IN codiceRegione: "+codiceRegione);
			log.debug("[" + CLASS_NAME + "::getComuniByProvincia] IN codiceProvincia: "+codiceProvincia);
//			UserInfo user = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			List<Comune> ris = comuneDAO.listByCodiceProvincia(codiceProvincia);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Comuni della Provincia " + codiceProvincia + " non trovate.");
			}
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getComuniByProvincia] Errore generico servizio getComuniByProvincia", ex);
			throw new ServiceException("Errore generico servizio elenco getComuniByProvincia");
		}
	}

	@Override
	public Response getProvinciaById(Integer codiceRegione, Integer codiceProvincia, String fields, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getProvinciaById] IN codiceRegione: "+codiceRegione);
			log.debug("[" + CLASS_NAME + "::getProvinciaById] IN codiceProvincia: "+codiceProvincia);
//			UserInfo user = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			Provincia ris = provinciaDAO.findByPKidx(codiceRegione, codiceProvincia);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Provincia " + codiceRegione + " non trovata.");
			}
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProvinciaById] Errore generico servizio getProvinciaById", ex);
			throw new ServiceException("Errore generico servizio getProvinciaById");
		}  
	}

	@Override
	public Response getProvinceByRegione(Integer codiceRegione, String fields, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getProvinceByRegione] IN codiceRegione: " + codiceRegione);
//			UserInfo user = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			List<Provincia> ris = provinciaDAO.listByCodiceRegione(codiceRegione);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Province della Regione " + codiceRegione + " non trovate.");
			}
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProvinceByRegione] Errore generico servizio getProvinceByRegione", ex);
			throw new ServiceException("Errore generico servizio elenco getProvinceByRegione");
		}
	}

	@Override
	public Response getRegioneById(Integer codiceRegione, String fields, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getRegioneById] IN codiceRegione: " + codiceRegione);
//			UserInfo user = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			Regione ris = regioneDAO.findByPK(codiceRegione);
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Regione " + codiceRegione + " non trovata.");
			}
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getRegioneById] Errore generico servizio getRegioneById", ex);
			throw new ServiceException("Errore generico servizio getRegioneById");
		} 
	}

	@Override
	public Response getRegioni(String fields, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
//			UserInfo user = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			List<Regione> ris = regioneDAO.findAll();
			if (ris != null) {
				return Response.ok(ris).build();
			} else {
				throw new ResourceNotFoundException("Regioni non trovate.");
			}
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getRegioni] Errore generico servizio getRegioni", ex);
			throw new ServiceException("Errore generico servizio elenco getRegioni");
		}
	}


}
