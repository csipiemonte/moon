/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.extra.istat.impl;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.extra.istat.IstatApi;
import it.csi.moon.moonbobl.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonbobl.business.service.demografia.AnprNazioneService;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.AtecoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.ComuneDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.FormeGiuridicaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.ProvinciaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.RegioneDAO;
import it.csi.moon.moonbobl.dto.extra.demografia.Nazione;
import it.csi.moon.moonbobl.dto.extra.istat.Ateco;
import it.csi.moon.moonbobl.dto.extra.istat.Comune;
import it.csi.moon.moonbobl.dto.extra.istat.FormeGiuridica;
import it.csi.moon.moonbobl.dto.extra.istat.Provincia;
import it.csi.moon.moonbobl.dto.extra.istat.Regione;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class IstatApiImpl extends MoonBaseApiImpl implements IstatApi {
	
	private static final String CLASS_NAME = "IstatApiImpl";
	private static final Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	AnprNazioneService anprNazioneService; // Stati esteri di provenienza dati ANPR ma basati sucodice ISTAT (DB moon)
	@Autowired
	@Qualifier("moon")
	RegioneDAO regioneDAO;
	@Autowired
	@Qualifier("moon")
	ProvinciaDAO provinciaDAO;
	@Autowired
	@Qualifier("moon")
	ComuneDAO comuneDAO;
	
	@Autowired
	@Qualifier("moon")
	AtecoDAO atecoDAO;
	
	@Autowired
	@Qualifier("moon")
	FormeGiuridicaDAO formeGiuridicaDAO;
	
	//
	// Nazioni - Stati Esteri
	@Override
	public Response getNazioni(String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ServiceException {
		try {
			List<Nazione> ris = anprNazioneService.listaNazioniResidenza();
			return Response.ok(ris).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getNazioni] Errore generico servizio getNazioni",ex);
			throw new ServiceException("Errore generico servizio elenco Nazioni");
		} 
	}
	
	@Override
	public Response getNazioneById(String codice, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			Integer idNazione = validaInteger1Based(codice);
			Nazione ris = anprNazioneService.getNazioneById(idNazione);
			return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getNazioneById] Risorsa non trovata con codice: " + codice);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getNazioneById] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getNazioneById] Errore generico servizio getNazioneById",ex);
			throw new ServiceException("Errore generico servizio getNazioneById");
		} 
	}

	
    //
    // Regioni
	@Override
    public Response getRegioni( String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ServiceException {
		try {
			List<Regione> ris = regioneDAO.findAll();
			Collections.sort(ris);
			return Response.ok(ris).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getRegioni] Errore generico servizio getRegioni",ex);
			throw new ServiceException("Errore generico servizio elenco Regione");
		} 
	}

	@Override
    public Response getRegioneById( String codice, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			Integer idRegione = validaInteger1Based(codice);
			Regione ris = regioneDAO.findByPK(idRegione);
			return Response.ok(ris).build();
		} catch(ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getRegioneById] Risorsa non trovata con codice: " + codice);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getRegioneById] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getRegioneById] Errore generico servizio getRegioneById",ex);
			throw new ServiceException("Errore generico servizio getRegioneById");
		} 
	}
    
    
    //
    // Provincie
	@Override
    public Response getProvincie( String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ServiceException {
		try {
			List<Provincia> ris = provinciaDAO.findAll();
			Collections.sort(ris);
			return Response.ok(ris).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProvincie] Errore generico servizio getProvincie",ex);
			throw new ServiceException("Errore generico servizio elenco Provincie");
		} 
	}
	@Override
    public Response getProvince( String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ServiceException {
		try {
			List<Provincia> ris = provinciaDAO.findAll();
			Collections.sort(ris);
			return Response.ok(ris).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProvince] Errore generico servizio getProvince",ex);
			throw new ServiceException("Errore generico servizio elenco Province");
		} 
	}

	@Override
    public Response getProvinciaById( String codice, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			Integer idProvincia = validaInteger1Based(codice);
			Provincia ris = provinciaDAO.findByPK(idProvincia);
			return Response.ok(ris).build();
		} catch(ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getProvinciaById] Risorsa non trovata con codice: " + codice);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getProvinciaById] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProvinciaById] Errore generico servizio getProvinciaById",ex);
			throw new ServiceException("Errore generico servizio getProvinciaById");
		} 
	}
    
	@Override
    public Response getProvincieByRegione(String codiceRegione, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException {
		try {
			Integer idRegione = validaInteger1Based(codiceRegione);
			List<Provincia> ris = provinciaDAO.listByCodiceRegione(idRegione);
			Collections.sort(ris);
			return Response.ok(ris).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProvincieByRegione] Errore generico servizio getProvincieByRegione",ex);
			throw new ServiceException("Errore generico servizio elenco getProvincieByRegione");
		} 
	}
	@Override
    public Response getProvinceByRegione(String codiceRegione, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException {
		try {
			Integer idRegione = validaInteger1Based(codiceRegione);
			List<Provincia> ris = provinciaDAO.listByCodiceRegione(idRegione);
			Collections.sort(ris);
			return Response.ok(ris).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProvinceByRegione] Errore generico servizio getProvinceByRegione",ex);
			throw new ServiceException("Errore generico servizio elenco getProvinceByRegione");
		} 
	}
    
	@Override
    public Response getProvinciaByRegione( String codiceRegione, String codiceProvincia, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			Integer idRegione = validaInteger1Based(codiceRegione);
			Integer idProvincia = validaInteger1Based(codiceProvincia);
			Provincia ris = provinciaDAO.findByPKidx(idRegione, idProvincia);
			return Response.ok(ris).build();
		} catch(ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getProvinciaByRegione] Risorsa non trovata con codiceRegione: " + codiceRegione + "   codiceProvincia: " + codiceProvincia);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getProvinciaByRegione] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProvinciaByRegione] Errore generico servizio getProvinciaByRegione",ex);
			throw new ServiceException("Errore generico servizio getProvinciaByRegione");
		} 
	}
	@Override
    public Response getProvinciaBySigla( String siglaProvincia, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			siglaProvincia = validaStringCodeRequired(siglaProvincia);
			Provincia ris = provinciaDAO.findBySiglia(siglaProvincia);
			return Response.ok(ris).build();
		} catch(ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getProvinciaBySigla] Risorsa non trovata con siglaProvincia: " + siglaProvincia);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getProvinciaBySigla] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProvinciaBySigla] Errore generico servizio getProvinciaBySigla",ex);
			throw new ServiceException("Errore generico servizio getProvinciaBySigla");
		} 
	}
    
    
    //
    // Comuni
	@Override
    public Response getComuni( String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ServiceException {
		try {
			List<Comune> ris = comuneDAO.findAll();
			return Response.ok(ris).build();
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getComuni] Errore generico servizio getComuni",ex);
			throw new ServiceException("Errore generico servizio elenco Comuni");
		} 
	}
	@Override
    public Response getComuneById( String codice, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			Integer idComune = validaInteger1Based(codice);
			Comune ris = comuneDAO.findByPK(idComune);
			return Response.ok(ris).build();
		} catch(ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getComuneById] Risorsa non trovata con codice: " + codice);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getComuneById] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getComuneById] Errore generico servizio getComuneById",ex);
			throw new ServiceException("Errore generico servizio getComuneById");
		} 
	}
	@Override
    public Response getComuniByProvincia( String codiceProvincia, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException {
		try {
			Integer idProvincia = validaInteger1Based(codiceProvincia);
			List<Comune> ris = comuneDAO.listByCodiceProvincia(idProvincia);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getComuniByProvincia] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getComuniByProvincia] Errore generico servizio getComuniByProvincia",ex);
			throw new ServiceException("Errore generico servizio elenco ComuniByProvincia");
		} 
	}
	@Override
    public Response getComuneByProvincia( String codiceProvincia, String codiceComune, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			Integer idProvincia = validaInteger1Based(codiceProvincia);
			Integer idComune = validaInteger1Based(codiceComune);
			Comune ris = comuneDAO.findByPKidx(idProvincia, idComune);
			return Response.ok(ris).build();
		} catch(ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getComuneByProvincia] Risorsa non trovata con codiceProvincia: " + codiceProvincia + "  codiceComune: " + codiceComune);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getComuneByProvincia] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getComuneByProvincia] Errore generico servizio getComuneByProvincia",ex);
			throw new ServiceException("Errore generico servizio getComuneByProvincia");
		} 
	}
	@Override
    public Response getComuniByRegioneProvincia( String codiceRegione, String codiceProvincia, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException {
		try {
			Integer idProvincia = validaInteger1Based(codiceProvincia);
			List<Comune> ris = comuneDAO.listByCodiceProvincia(idProvincia);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getComuniByRegioneProvincia] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] Errore generico servizio getComuniByRegioneProvincia",ex);
			throw new ServiceException("Errore generico servizio elenco ComuniByRegioneProvincia");
		} 
	}
    @Deprecated
	@Override
	public Response getComuniByRegioneProvinciaOLD(String codiceRegione, String codiceProvincia, String fields,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws UnprocessableEntityException, ServiceException {
		return getComuniByRegioneProvincia(codiceRegione, codiceProvincia, fields,
			securityContext, httpHeaders, httpRequest);
	}
	@Override
    public Response getComuneByRegioneProvincia( String codiceRegione, String codiceProvincia, String codiceComune, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			Integer idProvincia = validaInteger1Based(codiceProvincia);
			Integer idComune = validaInteger1Based(codiceComune);
			Comune ris = comuneDAO.findByPKidx(idProvincia, idComune);
			return Response.ok(ris).build();
		} catch(ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] Risorsa non trovata con codiceRegione: " + codiceRegione + "   codiceProvincia: " + codiceProvincia + "  codiceComune: " + codiceComune);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getComuneByRegioneProvincia] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] Errore generico servizio getComuneByRegioneProvincia",ex);
			throw new ServiceException("Errore generico servizio getComuneByRegioneProvincia");
		} 
	}

    
    //
    // Ateco
	@Override
    public Response getCodiciAteco( String sezioni, String divisioni, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws UnprocessableEntityException, ServiceException {
		try {
			List<Ateco> ris = atecoDAO.findFinale();
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getCodiciAteco] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCodiciAteco] Errore generico servizio getCodiciAteco",ex);
			throw new ServiceException("Errore generico servizio elenco getCodiciAteco");
		} 
	}

	@Override
	public Response getCodiceAteco(String codiceAteco, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		String codiceAtecoForSearch = null;
		try {
			validaStringRequired(codiceAteco);
			int len = codiceAteco.length();
			if (len==8) {
				codiceAtecoForSearch = codiceAteco;
			} else if (!codiceAteco.contains(".") && (len==5 || len==6)) {
				codiceAtecoForSearch = (len==5?"0":"") + codiceAteco.substring(0,len==5?1:2) + "." + codiceAteco.substring(len==5?1:2,len==5?3:4) + "." + codiceAteco.substring(len==5?3:4,len==5?5:6);
			}
			if (codiceAtecoForSearch==null) {
				throw new UnprocessableEntityException("Codice Ateco not corretto.");
			}
			Ateco ris = atecoDAO.findByCodice(codiceAtecoForSearch);
			return Response.ok(ris).build();
		} catch( ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getCodiceAteco] Risorsa non trovata con codiceAteco: " + codiceAteco + "   codiceAtecoForSearch: " + codiceAtecoForSearch);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getCodiceAteco] UnprocessableEntityException codiceAteco: " + codiceAteco);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCodiceAteco] Errore generico servizio getCodiceAteco",ex);
			throw new ServiceException("Errore generico servizio getCodiceAteco");
		}
	}

    
    //
    // FormeGiuridica
	@Override
	public Response getFormeGiuridiche(
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ServiceException {
		try {
			List<FormeGiuridica> ris = formeGiuridicaDAO.find();
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getFormeGiuridiche] UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getFormeGiuridiche] Errore generico servizio getFormeGiuridiche",ex);
			throw new ServiceException("Errore generico servizio elenco getFormeGiuridiche");
		} 
	}

	@Override
	public Response getFormeGiuridica(String idFormeGiuridicaPP, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			Integer idFormeGiuridica = validaIntegerRequired(idFormeGiuridicaPP);
			FormeGiuridica ris = formeGiuridicaDAO.findById(idFormeGiuridica);
			return Response.ok(ris).build();
		} catch( ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getFormeGiuridica] Risorsa non trovata con idFormeGiuridicaPP: " + idFormeGiuridicaPP);
			throw new ResourceNotFoundException();
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::getFormeGiuridica] UnprocessableEntityException idFormeGiuridicaPP: " + idFormeGiuridicaPP);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getFormeGiuridica] Errore generico servizio getFormeGiuridica",ex);
			throw new ServiceException("Errore generico servizio getFormeGiuridica");
		}
	}

}
