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

import it.csi.apirest.notify.mb.v1.dto.Message;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApiRestTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore.NotificatoreMbDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
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
public class NotificatoreMbDAOImpl extends ApiRestTemplateImpl implements NotificatoreMbDAO {
	
	private static final String CLASS_NAME = "NotificatoreMbDAOImpl";
	private static final Map<Integer,String> TOPICS_MAP_STATUS_CODE_MESSAGE;
	static {
        Map<Integer, String> map = new HashMap<>();
        map.put(201, "message added to the queue 'messages'");
        map.put(400, "Invalid inmap.put");
        map.put(401, "Wrong token in x-authentication header");
        map.put(403, "The token has been blacklisted.");
        map.put(406, "the topic or the queue ':wrong_name' does not exist");
        map.put(500, "Internal Server Error");
        TOPICS_MAP_STATUS_CODE_MESSAGE = Collections.unmodifiableMap(map);
    }
	private static final String COMPONENT_PATH= "/notify-mb/api/v1";
	private String serviceToken;
    
    public NotificatoreMbDAOImpl() {
    	super();
    	setEndpoint(EnvProperties.readFromFile(EnvProperties.NOTIFY_ENDPOINT));
    }
    
	//endpoint puo esser null, perche ce quello di defualt preso dalle properties
    //serviceToken e' un campo obbligatorio da mettere nella CONF del modulo
	@Override
	public String invia(String endpoint, String serviceToken, Message message) throws DAOException {
        long start = System.currentTimeMillis();
        String errore = "";
        try {
        	LOG.debug("[" + CLASS_NAME + "::invia] BEGIN");
        	setEndpointAndToken(endpoint,serviceToken);
        	
        	String url = COMPONENT_PATH+"/topics/messages";
        	
        	setForseNamingStrategy_SNAKE_CASE(true);
        	ResponseUUID<String> rispostaNotificatore = postJson(url, String.class, message/*, Message.class*/);
    	    return (String) rispostaNotificatore.getResponse();
        } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::invia] Errore generico servizio invia", e);
			errore = "Exception";
			throw new ServiceException("Errore generico servizio invia");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("[" + CLASS_NAME + "::invia] END DAO_ELAPSED_TIME in " + sec + " milliseconds." + errore);
        }
    }

	private void setEndpointAndToken(String endpoint, String serviceToken) {
		if(endpoint !=null && !endpoint.isEmpty()) {
    		LOG.info("[" + CLASS_NAME + "::setEndpointAndToken] endpoint URL da DB="+endpoint);
    		setEndpoint(endpoint);
    	}
		LOG.info("[" + CLASS_NAME + "::setEndpointAndToken] token="+serviceToken);
    	this.serviceToken = serviceToken;
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
		headers.add("x-authentication", token);
		return headers;
	}
	
}
