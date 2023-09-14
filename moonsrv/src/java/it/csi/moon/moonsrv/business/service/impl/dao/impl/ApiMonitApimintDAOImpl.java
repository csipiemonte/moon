/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.api.FruitoreIstanza;
import it.csi.moon.commons.dto.api.FruitoreModuloVersione;
import it.csi.moon.commons.dto.api.StatoAcquisizioneRequest;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.moonsrv.business.service.impl.dao.ApiTestApimintDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimintTemplateImpl;
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
public class ApiMonitApimintDAOImpl extends ApimintTemplateImpl implements ApiTestApimintDAO {

	private static final String CLASS_NAME = "ApiMonitApimintDAOImpl";
			
    private String clientProfile;
    private String apimintUrl;
    private static final String APIMINTMONIT_URL = EnvProperties.readFromFile(EnvProperties.APIMONIT_APIMINT_URL);

    public ApiMonitApimintDAOImpl() {
    	super();
    }

	protected String getConsumerKey() {
		return EnvProperties.readFromFile(EnvProperties.APIMONIT_APIMINT_CONSUMERKEY);
	}
    protected String getConsumerSecret() {
		return EnvProperties.readFromFile(EnvProperties.APIMONIT_APIMINT_CONSUMERSECRET);
    }

	protected String getBaseApimintUrl() {
		return StringUtils.isEmpty(getApimintUrl())?APIMINTMONIT_URL:getApimintUrl();
	}
	
	@Override
	public List<String> getIstanze(String codiceModulo, String stato, boolean test, String apimintUrl,
			String clientProfile) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	setClientProfile(clientProfile);
        	setApimintUrl(apimintUrl);
        	LOG.debug("[" + CLASS_NAME + "::getIstanze] BEGIN");
        	stato = StrUtils.isEmpty(stato)?"INVIATA":stato;
        	String url = "/istanze?codice_modulo="+codiceModulo+"&stato="+stato+"&test="+(test?"true":"false");
        	ResponseUUID<String[]> response = getJson(url, String[].class);
        	LOG.debug("[" + CLASS_NAME + "::getIstanze] " + response.getResponse());
    	    return List.of(response.getResponse());
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getIstanze] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio getIstanze", e);
			throw new DAOException("Errore generico servizio getIstanze");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.debug("[" + CLASS_NAME + "::getIstanze] END in " + sec + " milliseconds.");
        }
    }


	@Override
	public FruitoreIstanza getIstanza(String codice, String apimintUrl,
			String clientProfile) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	setClientProfile(clientProfile);
        	setApimintUrl(apimintUrl);
        	LOG.debug("[" + CLASS_NAME + "::getIstanza] BEGIN");
        
        	String url = "/istanze/"+codice;

        	ResponseUUID<FruitoreIstanza> response = getJson(url, FruitoreIstanza.class);
        	LOG.debug("[" + CLASS_NAME + "::getIstanza] " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getIstanza] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore generico servizio getIstanza", e);
			throw new DAOException("Errore generico servizio getIstanze");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.debug("[" + CLASS_NAME + "::getIstanza] END in " + sec + " milliseconds.");
        }
    }
	
	
	@Override
	public String updateStatoAcquisizione(String codiceIstanza, String codiceAzione, String apimintUrl,
			StatoAcquisizioneRequest body,
			String clientProfile) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	setClientProfile(clientProfile);
        	setApimintUrl(apimintUrl);
        	LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] BEGIN");
       
        	String url = "/istanze/"+codiceIstanza+"/azione/"+codiceAzione;
        	
        	ResponseUUID<String> response = postJson(url, String.class, body);
        	LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::updateStatoAcquisizione] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateStatoAcquisizione] Errore generico servizio getIstanze", e);
			throw new DAOException("Errore generico servizio getIstanze");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.debug("[" + CLASS_NAME + "::updateStatoAcquisizione] END in " + sec + " milliseconds.");
        }
    }	

	@Override
	protected MultivaluedMap<String, Object> getHeadersExtra() {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
		if (!StringUtils.isEmpty(getClientProfile())) {
			headers.add("client-profile",  getClientProfile());
		}
		return headers;
	}

	private String getClientProfile() {
		return clientProfile;
	}
	public void setClientProfile(String clientProfile) {
		this.clientProfile = clientProfile;
	}

	public String getApimintUrl() {
		return apimintUrl;
	}
	public void setApimintUrl(String apimintUrl) {
		this.apimintUrl = apimintUrl;
	}

	@Override
	public List<FruitoreModuloVersione> getModuli(Map<String, String> queryParams, String apimintUrl, String clientProfile) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	setClientProfile(clientProfile);
        	setApimintUrl(apimintUrl);
        	LOG.debug("[" + CLASS_NAME + "::getModuli] BEGIN");
        	String params = "";
        	if (queryParams!=null && queryParams.size()>0) {
        		for (Entry e : queryParams.entrySet()) {
        			params += ((params.length()==0)?"?":"&") + e.getKey() + "=" + e.getValue();
        		}
        	}
        	String url = "/moduli" + params;
        	ResponseUUID<FruitoreModuloVersione[]> response = getJson(url, FruitoreModuloVersione[].class);
        	LOG.debug("[" + CLASS_NAME + "::getModuli] " + response.getResponse());
    	    return List.of(response.getResponse());
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getModuli] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getModuli] Errore generico servizio getIstanze", e);
			throw new DAOException("Errore generico servizio getModuli");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.debug("[" + CLASS_NAME + "::getModuli] END in " + sec + " milliseconds.");
        }
	}

	@Override
	public FruitoreModuloVersione getModulo(String codiceModulo, String versioneModulo, String apimintUrl, String clientProfile)
			throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	setClientProfile(clientProfile);
        	setApimintUrl(apimintUrl);
        	LOG.debug("[" + CLASS_NAME + "::getModulo] BEGIN");
        	String url = "/moduli/"+codiceModulo+"/v/"+versioneModulo;
        	ResponseUUID<FruitoreModuloVersione> response = getJson(url, FruitoreModuloVersione.class);
        	LOG.debug("[" + CLASS_NAME + "::getModulo] " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getModulo] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getModulo] Errore generico servizio getModulo", e);
			throw new DAOException("Errore generico servizio getModulo");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.debug("[" + CLASS_NAME + "::getModulo] END in " + sec + " milliseconds.");
        }
	}


}
