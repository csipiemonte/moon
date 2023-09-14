/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.common.util.StringUtils;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.stereotype.Component;

import it.csi.apirest.notify.status.v1.dto.Status;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApiRestTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotificatoreStatusDAO;
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
//@Qualifier("notifyRS")
public class NotificatoreStatusDAOImpl extends ApiRestTemplateImpl implements NotificatoreStatusDAO {
	
	private static final String CLASS_NAME = "NotificatoreStatusDAOImpl";
	private static final Map<Integer,String> STATUS_MAP_STATUS_CODE_MESSAGE;
	static {
        Map<Integer, String> map = new HashMap<>();
        map.put(400, "Invalid input");
        map.put(401, "Unauthorized");
        map.put(403, "The token has been blacklisted.");
        map.put(404, "User not found");
        map.put(500, "Internal Server Error");
        STATUS_MAP_STATUS_CODE_MESSAGE = Collections.unmodifiableMap(map);
    }
	private static final String X_AUTHENTICATION = "x-authentication";
	private static final String SHIB_IRIDE_IDENTITADIGITALE = "Shib-Iride-IdentitaDigitale";
    private static final String COMPONENT_PATH= "/notify-status/api/v1"; 
	private String serviceToken;
	private String identitaDigatale;
	
    public NotificatoreStatusDAOImpl() {
    	super();
    	setEndpoint(EnvProperties.readFromFile(EnvProperties.NOTIFY_STATUS_ENDPOINT));
    	setFAIL_ON_UNKNOWN_PROPERTIES(false);
    }
    
    @Override
	public Status status(String endpoint, String token,String cfTracciamento,String id) throws DAOException {    	
    	 long start = System.currentTimeMillis();
         String errore = "";
         try {
         	LOG.debug("[" + CLASS_NAME + "::status] BEGIN");
         	setEndpointAndTokenAndIdDigitale(endpoint, token, cfTracciamento);
         	String url = COMPONENT_PATH+"/status/messages/"+id;
         	setMapStatusCodeMessage(STATUS_MAP_STATUS_CODE_MESSAGE);
         	ResponseUUID<Status> rispostaNotificatore = getJson(url, Status.class);
         	return rispostaNotificatore.getResponse();
         } catch(DAOException daoe) {
 			LOG.error("[" + CLASS_NAME + "::status] Errore generico status", daoe);
 			if (daoe.getStatus()==404) {
 				errore = "ItemNotFoundDAOException";
 				throw new ItemNotFoundDAOException();
 			}
			errore = "DAOException";
 			throw new DAOException();
 	    } finally {
             long end = System.currentTimeMillis();
             float sec = (end - start); 
             LOG.info("[" + CLASS_NAME + "::status] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
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
