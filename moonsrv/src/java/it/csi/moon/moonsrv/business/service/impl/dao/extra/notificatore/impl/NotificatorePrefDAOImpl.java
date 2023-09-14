/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.common.util.StringUtils;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.stereotype.Component;

import it.csi.apirest.notify.preferences.v1.dto.ContactPreference;
import it.csi.apirest.notify.preferences.v1.dto.Service;
import it.csi.apirest.notify.preferences.v1.dto.UserPreferencesService;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApiRestTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotificatorePrefDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.EnvProperties;

/**
* Implementazione DAO Notificatore via servizi REST
* 
* @author Alberto Deiro
* @author Laurent Pissard
* 
* @since 1.0.0
*/
@Component
public class NotificatorePrefDAOImpl extends ApiRestTemplateImpl implements NotificatorePrefDAO {
	
	private static final String CLASS_NAME = "NotificatorePrefDAOImpl";
	public static final String SERVICE_NAME = "moon";
	private static final Map<Integer,String> PREF_MAP_STATUS_CODE_MESSAGE;
	static {
        Map<Integer, String> map = new HashMap<>();
        map.put(400, "Invalid input");
        map.put(401, "Unauthorized");
        map.put(403, "The token has been blacklisted.");
        map.put(404, "User not found");
        map.put(500, "Internal Server Error");
        PREF_MAP_STATUS_CODE_MESSAGE = Collections.unmodifiableMap(map);
    }
	private static final String X_AUTHENTICATION = "x-authentication";
	private static final String SHIB_IRIDE_IDENTITADIGITALE = "Shib-Iride-IdentitaDigitale";
    private static final String COMPONENT_PATH= "/notify-preferences/api/v1"; 
	private String serviceToken;
	private String identitaDigatale;
	
    public NotificatorePrefDAOImpl() {
    	super();
    	setEndpoint(EnvProperties.readFromFile(EnvProperties.NOTIFY_PREFERENCES_ENDPOINT));
    	setFAIL_ON_UNKNOWN_PROPERTIES(false);
    }
    
		
	@Override
	public ContactPreference contatti(String endpoint, String token, String identitaDigitale, String codiceFiscale) throws ItemNotFoundDAOException, DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
        try {
        	LOG.debug("[" + CLASS_NAME + "::contatti] BEGIN");
        	setEndpointAndTokenAndIdDigitale(endpoint, token, identitaDigitale);
        	String url = COMPONENT_PATH+"/users/"+codiceFiscale+"/contacts";
        	setMapStatusCodeMessage(PREF_MAP_STATUS_CODE_MESSAGE);
        	ResponseUUID<ContactPreference> rispostaNotificatore = getJson(url, ContactPreference.class);
    	    return (ContactPreference) rispostaNotificatore.getResponse();
        } catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::contatti] DAOException contatti" + daoe.getMessage());
			if (daoe.getStatus()==404) {
				throw new ItemNotFoundDAOException();
			}
			throw daoe;
        } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::contatti] Errore generico servizio contatti", e);
			errore = "Exception";
			throw new DAOException("Errore generico servizio contatti");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::contatti] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
        }
    }
	
	
	@Override
	public List<Service> services(String endpoint, String token, String cfTracciamento) throws DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
        try {
        	LOG.debug("[" + CLASS_NAME + "::services] BEGIN");
        	setEndpointAndTokenAndIdDigitale(endpoint, token, cfTracciamento);
        	String url = COMPONENT_PATH+"/services";
        	setMapStatusCodeMessage(PREF_MAP_STATUS_CODE_MESSAGE);
        	ResponseUUID<Service[]> rispostaNotificatore = getJson(url, Service[].class);
        	return List.of(rispostaNotificatore.getResponse());
        } catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::services] DAOException services" + daoe.getMessage());
			errore = "DAOException";
			throw daoe;
        } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::services] Errore generico servizio contatti", e);
			errore = "Exception";
			throw new DAOException("Errore generico servizio contatti");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::services] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
        }
    }
	
	@Override
	public List<UserPreferencesService> servicesByUser(String endpoint, String token,String cfTracciamento, String codiceFiscale) throws DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
        try {
        	LOG.debug("[" + CLASS_NAME + "::servicesByUser] BEGIN");
        	setEndpointAndTokenAndIdDigitale(endpoint, token, cfTracciamento);
        	String url = COMPONENT_PATH+"/users/" + codiceFiscale + "/preferences";
        	setMapStatusCodeMessage(PREF_MAP_STATUS_CODE_MESSAGE);
        	//UserPreferencesService u = new  UserPreferencesService();
        	//u.
        	//Service s = new Service();
  
        	ResponseUUID<UserPreferencesService[]> rispostaNotificatore = getJson(url, UserPreferencesService[].class);
        	return List.of(rispostaNotificatore.getResponse());
        } catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::servicesByUser] DAOException servicesByUser" + daoe.getMessage());
			errore = "DAOException";
			throw daoe;
        } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::servicesByUser] Errore generico servicesByUser", e);
			errore = "Exception";
			throw new DAOException("Errore generico servicesByUser");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::servicesByUser] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
        }
	}

	@Override
	public UserPreferencesService serviceByUserServiceName(String endpoint, String token, String cfTracciamento, String codiceFiscale, String serviceName) throws ItemNotFoundDAOException, DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
        try {
        	LOG.debug("[" + CLASS_NAME + "::serviceByUserServiceName] BEGIN");
        	setEndpointAndTokenAndIdDigitale(endpoint, token, cfTracciamento);
        	String url = COMPONENT_PATH+"/users/" + codiceFiscale + "/preferences/" + serviceName;
        	setMapStatusCodeMessage(PREF_MAP_STATUS_CODE_MESSAGE);
        	ResponseUUID<UserPreferencesService> rispostaNotificatore = getJson(url, UserPreferencesService.class);
        	return rispostaNotificatore.getResponse();
        } catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::serviceByUserServiceName] Errore generico serviceByUserServiceName", daoe);
			if (daoe.getStatus()==404) {
				errore = "ItemNotFoundDAOException";
				throw new ItemNotFoundDAOException();
			}
			errore = "DAOException";
			throw new DAOException();
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::serviceByUserServiceName] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
        }
	}
	
	private void setEndpointAndTokenAndIdDigitale(String endpoint, String serviceToken, String identitaDigitale) {
		if(endpoint !=null && !endpoint.isEmpty()) {
    		LOG.info("[" + CLASS_NAME + "::setEndpointAndTokenAndIdDigitale] endpoint URL da DB="+endpoint);
    		setEndpoint(endpoint);
    	}
		LOG.info("[" + CLASS_NAME + "::setEndpointAndTokenAndIdDigitale] token="+serviceToken);
		LOG.info("[" + CLASS_NAME + "::setEndpointAndTokenAndIdDigitale] identitaDigitale="+identitaDigitale);
    	this.serviceToken = serviceToken;
    	this.identitaDigatale = identitaDigitale;
	}
	
	@Override
	protected MultivaluedMap<String, Object> getHeadersExtra() {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
		String token;
		// 1.Token
		if(!StringUtils.isEmpty(this.serviceToken) ) {
			token = this.serviceToken;
		}else {
			token = EnvProperties.readFromFile(EnvProperties.NOTIFY_TOKEN);
		}
		headers.add(X_AUTHENTICATION, token);
		// 2. ID DIGITALE
		headers.add(SHIB_IRIDE_IDENTITADIGITALE, this.identitaDigatale);
		return headers;
	}
	
}
