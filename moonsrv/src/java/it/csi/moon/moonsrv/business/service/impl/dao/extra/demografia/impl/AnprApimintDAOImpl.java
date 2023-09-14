/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.stereotype.Component;

import it.csi.apimint.demografia.v1.dto.Soggetto;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimintTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.AnprApimintDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ApimintResponseAnprFamiglia;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;

/**
 * Implementazione DAO ANPR via servizi REST via API Manager Outer
 * 
 * @author Francesco Zucaro
 * @author Laurent Pissard
 * 
 * @since 1.0.0
 */
@Component
public class AnprApimintDAOImpl extends ApimintTemplateImpl implements AnprApimintDAO {
	
	private static final String CLASS_NAME = "AnprApimintDAOImpl";
	private static final Map<Integer,String> FAMIGLIA_MAP_STATUS_CODE_MESSAGE;
	static {
        Map<Integer, String> map = new HashMap<>();
        map.put(204, "204 - Nessun soggetto reperito per i parametri di ricerca impostati");
        map.put(401, "401 - UnauthorizedError - Access token mancante o non valido");
        map.put(403, "403 - InsufficentPermissioneError - Il profilo utilizzato non ha sufficienti permessi per ottenere la risora richiesta");
        map.put(404, "404 - NotFound - La risorsa specificata non è stata trovata");
        map.put(500, "500 - InternalServerError - Si è verificato un'errore interno del server");
        map.put(502, "502 - BadGatewayError - Errore generato dai servizi di inner");
        FAMIGLIA_MAP_STATUS_CODE_MESSAGE = Collections.unmodifiableMap(map);
    }
	
    /*
     * Parametri specifici a Demografia
     */
    private String userToken;
    private String clientProfileKey = EnvProperties.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO;

    public AnprApimintDAOImpl() {
    	super();
    }

    /*
     * Setter Getter dei Parametri specifici a Demografia
     */
    private String getUserToken() {
		return userToken;
	}
    private void setUserToken(String userToken) {
		this.userToken = userToken;
	}
    private String getClientProfileKey() {
		return clientProfileKey;
	}
    public void setClientProfileKey(String clientProfileKey) {
		this.clientProfileKey = clientProfileKey;
	}
	private String getClientProfile() {
		return EnvProperties.readFromFile(getClientProfileKey());
	}
	
	protected String getPathExtra() {
		return "/demografia/v1";
	}
	
	
	@Override
	public List<Soggetto> getFamigliaANPR(String codiceFiscale, String userJwt) throws DAOException {
		return getFamigliaANPR(codiceFiscale, userJwt, EnvProperties.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO);
	}
	
	@Override
	public List<Soggetto> getFamigliaANPR(String codiceFiscale, String userJwt, String clientProfile) throws DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
        try {
        	setUserToken(userJwt); // usato solo pre tracciamento audit.user_token nel sistema di demografia, puo essere solo CF
        	setClientProfileKey(StringUtils.isBlank(clientProfile)?EnvProperties.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_CITTADINO:clientProfile);
        	
        	LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] BEGIN");
//        	initUserToken(codiceFiscale, userJwt); // ONLY FOR devmode TEST
        	setMapStatusCodeMessage(FAMIGLIA_MAP_STATUS_CODE_MESSAGE);
        	
        	String url = "/anagrafe/cittadini/" + codiceFiscale + "/famiglia";
        	ResponseUUID<ApimintResponseAnprFamiglia> rispostaServizioDemografia = getJson(url, ApimintResponseAnprFamiglia.class);
        	LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] 200 :: " + rispostaServizioDemografia.getResponse());
    	    return rispostaServizioDemografia.getResponse().getFamiglia();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getFamigliaANPR] DAOException " + daoe.getMessage());
			errore = "DAOException";
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getFamigliaANPR] Errore generico servizio getFamigliaANPR", e);
			errore = "Exception";
			throw new DAOException("Errore generico servizio getFamigliaANPR");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::getFamigliaANPR] END DAO_ELAPSED_TIME for "+codiceFiscale+" in " + sec + " milliseconds." + errore);
        }
    }


	@Override
	protected MultivaluedMap<String, Object> getHeadersExtra() {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
		if (!StringUtils.isEmpty(getUserToken())) {
			headers.add("user-token",  getUserToken());
		} else {
			// 01/02/2021 Nel caso di chiamata senza user JWT GASP, si deve usare il clientProfile Operatore
			// alberto+Laurent: spostato in AnprServiceRSImpl._getSoggettoANPR
			// setClientProfileKey(EnvProperties.APIMINT_DEMOGRAFIA_ANPR_CLIENT_PROFILE_OPERATORE);
		}
		if (!StringUtils.isEmpty(getClientProfile())) {
			headers.add("client-profile",  getClientProfile());
		}

		return headers;
	}
	
//	@Override
//	protected String getConsumerPrefix() {
//		return this.consumerPrefix;
//	}
//	private void setconsumerPrefix(String consumerPrefix) {
//		this.consumerPrefix = consumerPrefix;
//	}
}
