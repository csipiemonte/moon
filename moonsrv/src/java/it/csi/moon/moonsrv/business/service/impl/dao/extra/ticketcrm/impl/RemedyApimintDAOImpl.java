/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.springframework.stereotype.Component;

import it.csi.apimint.troubleticketing.v1.dto.CategoriaApplicativa;
import it.csi.apimint.troubleticketing.v1.dto.CategoriaOperativaTicket;
import it.csi.apimint.troubleticketing.v1.dto.ConfigurationItem;
import it.csi.apimint.troubleticketing.v1.dto.Ente;
import it.csi.apimint.troubleticketing.v1.dto.InfoNotaWLog;
import it.csi.apimint.troubleticketing.v1.dto.LavorazioneTicket;
import it.csi.apimint.troubleticketing.v1.dto.RichiedenteDaAnagrafica;
import it.csi.apimint.troubleticketing.v1.dto.RichiedentePerAnagrafica;
import it.csi.apimint.troubleticketing.v1.dto.Ticket;
import it.csi.apimint.troubleticketing.v1.dto.TicketExpo;
import it.csi.apimint.troubleticketing.v1.dto.TicketSnapshot;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimintTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.RemedyApimintDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;

/**
* Implementazione DAO TroubleTicketing Remedy via servizi REST via API Manager Outer
* - via API Manager Outer
* 
* @author Laurent Pissard
* 
* @since 1.0.0
*/
@Component
public class RemedyApimintDAOImpl extends ApimintTemplateImpl implements RemedyApimintDAO {
	
	private static final String CLASS_NAME = "RemedyApimintDAOImpl";
	private static final Map<Integer,String> USERS_MAP_STATUS_CODE_MESSAGE;
	static {
        Map<Integer, String> map = new HashMap<>();
        map.put(400, "400 - Richiesta utente errata");
        map.put(401, "401 - Autorizzazione mancante o non valida");
        map.put(403, "404 - Elemento richiesto errato o risorsa inesistente");
        map.put(404, "404 - NotFound - La risorsa specificata non è stata trovata");
        map.put(500, "500 - InternalServerError");
        map.put(502, "503 - Servizio API non disponibile");
        USERS_MAP_STATUS_CODE_MESSAGE = Collections.unmodifiableMap(map);
    }
    
    public RemedyApimintDAOImpl() {
    	super();
    	setFAIL_ON_UNKNOWN_PROPERTIES(false);
    }
    
	protected String getPathExtra() {
		//return "/tecno/troubleticketing/v1";
		return EnvProperties.readFromFile(EnvProperties.TS_TROUBLETICKETING_APIMINT_PATH);
	}

    protected boolean useSandbox() {
    	boolean useSandbox = Boolean.TRUE.equals(Boolean.valueOf(EnvProperties.readFromFile(EnvProperties.TS_TROUBLETICKETING_USE_SANDBOX)));
    	if (useSandbox) {
    		LOG.info("[" + CLASS_NAME + "::useSandbox] true");
    	}
    	return useSandbox;
    }
    
	//
	// Entity RichiedenteDaAnagrafica
	@Override
	public List<RichiedenteDaAnagrafica> findUsers(String emailFilter, String cognomeFilter, String nomeFilter) {
		final String methodName = "findUsers";
		long start = System.currentTimeMillis();
		List<RichiedenteDaAnagrafica> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String filter = "";
			if(StringUtils.isNotBlank(emailFilter)) {
				filter = "{ \"email\": {\"eq\": \""+emailFilter+"\"} }";
			}
			if(StringUtils.isNotBlank(cognomeFilter)) {
				filter = "{ \"cognome\": {\"eq\": \""+cognomeFilter+"\"} }";
			}
			if(StringUtils.isNotBlank(nomeFilter)) {
				filter = "{ \"nome\": {\"eq\": \""+cognomeFilter+"\"} }";
			}
			filter = URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
			String url = "/users?filter="+filter;
			ResponseUUID<RichiedenteDaAnagrafica[]> response = getJson(url, RichiedenteDaAnagrafica[].class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = List.of(response.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result.size()=" + ((result!=null)?result.size():"null"));
		}
		return result;
	}

	@Override
	public RichiedenteDaAnagrafica findUserById(String personId) {
		final String methodName = "findUserById";
		long start = System.currentTimeMillis();
		RichiedenteDaAnagrafica result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/users/" + personId;
			ResponseUUID<RichiedenteDaAnagrafica> response = getJson(url, RichiedenteDaAnagrafica.class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = response.getResponse();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result=" + result);
		}
		return result;
	}


	@Override
	public RichiedentePerAnagrafica createUser(RichiedentePerAnagrafica u) {
		final String methodName = "createUser";
		long start = System.currentTimeMillis();
		RichiedentePerAnagrafica result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/users";
			ResponseUUID<RichiedentePerAnagrafica> response = postJson(url, RichiedentePerAnagrafica.class, u);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = response.getResponse();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result=" + result);
		}
		return result;
	}
	
	
	//
	// Entity Ente
	@Override
	public List<Ente> findEnti() {
		final String methodName = "findEnti";
		long start = System.currentTimeMillis();
		List<Ente> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/enti";
			ResponseUUID<Ente[]> response = getJson(url, Ente[].class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = List.of(response.getResponse());
		} catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio");
			throw daoe;
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result.size()=" + ((result!=null)?result.size():"null"));
		}
		return result;
	}

	
	//
	// Entity Categorie
	@Override
	public List<CategoriaOperativaTicket> findCategorieOperative() {
		final String methodName = "findCategorieOperative";
		long start = System.currentTimeMillis();
		List<CategoriaOperativaTicket> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/categorie-operative";
			ResponseUUID<CategoriaOperativaTicket[]> response = getJson(url, CategoriaOperativaTicket[].class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = List.of(response.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result.size()=" + ((result!=null)?result.size():"null"));
		}
		return result;
	}

	@Override
	public List<CategoriaApplicativa> getCategorieApplicative() {
		final String methodName = "getCategorieApplicative";
		long start = System.currentTimeMillis();
		List<CategoriaApplicativa> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/categorie-applicative";
			ResponseUUID<CategoriaApplicativa[]> response = getJson(url, CategoriaApplicativa[].class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = List.of(response.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result.size()=" + ((result!=null)?result.size():"null"));
		}
		return result;
	}

	
	//
	// Entity ConfigurationItem
	@Override
	public List<ConfigurationItem> getConfigurationItems(String personId) {
		final String methodName = "getConfigurationItems";
		long start = System.currentTimeMillis();
		List<ConfigurationItem> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/users/"+personId+"/configuration-items";
			ResponseUUID<ConfigurationItem[]> response = getJson(url, ConfigurationItem[].class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = List.of(response.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result.size()=" + ((result!=null)?result.size():"null"));
		}
		return result;
	}
		
	
	//
	//
	public Ticket createTicket(Ticket ticket) {
		final String methodName = "createTicket";
		long start = System.currentTimeMillis();
		Ticket result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/tickets";
			ResponseUUID<Ticket> response = postJson(url, Ticket.class, ticket);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = response.getResponse();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
		return result;	
	}
	public LavorazioneTicket getWorkinfoTicket(String ticketId) {
		final String methodName = "getLastUpdated";
		long start = System.currentTimeMillis();
		LavorazioneTicket result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/tickets/monitoraggio";
			ResponseUUID<LavorazioneTicket> response = getJson(url, LavorazioneTicket.class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = response.getResponse();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
		return result;	
	}
	public List<TicketSnapshot> getLastUpdated() {
		final String methodName = "getLastUpdated";
		long start = System.currentTimeMillis();
		List<TicketSnapshot> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/tickets/monitoraggio";
			ResponseUUID<TicketSnapshot[]> response = getJson(url, TicketSnapshot[].class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = List.of(response.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result.size()=" + ((result!=null)?result.size():"null"));
		}
		return result;
	}
	public List<TicketExpo> getLastRegistered() {
		final String methodName = "getLastRegistered";
		long start = System.currentTimeMillis();
		List<TicketExpo> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/tickets/esposizione";
			ResponseUUID<TicketExpo[]> response = getJson(url, TicketExpo[].class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = List.of(response.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result.size()=" + ((result!=null)?result.size():"null"));
		}
		return result;
	}
	
	public InfoNotaWLog putInfoDettagli(String ticketId, String tipologia, String riepilogo, String nomeFile, byte[] pdf) {
		final String methodName = "putInfoDettagli";
		long start = System.currentTimeMillis();
		InfoNotaWLog result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
        	setMapStatusCodeMessage(USERS_MAP_STATUS_CODE_MESSAGE);
			String url = "/tickets/"+ticketId+"/stato/info-dettagli";
			
			MultipartFormDataOutput bodyMultipart = new MultipartFormDataOutput();
			bodyMultipart.addFormData("riepilogo", riepilogo, MediaType.TEXT_PLAIN_TYPE);
			bodyMultipart.addFormData("tipologia", tipologia, MediaType.TEXT_PLAIN_TYPE);
			bodyMultipart.addFormData("nomeAllegato1", nomeFile, MediaType.TEXT_PLAIN_TYPE);
			try( InputStream bis = new ByteArrayInputStream(pdf) ) {
				bodyMultipart.addFormData(nomeFile, bis, MediaType.APPLICATION_OCTET_STREAM_TYPE,nomeFile);
			}
			ResponseUUID<InfoNotaWLog> response = putJsonMultipartForm(url, InfoNotaWLog.class, bodyMultipart);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = response.getResponse();
		} catch(DAOException dao) {
			LOG.warn("[" + CLASS_NAME + "::" + methodName +"] Errore servizio" + dao.getMessage());
			throw dao;
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
		return result;	
	}

	
}
