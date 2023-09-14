/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.component;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.moon.moonsrv.business.service.dto.IstanzaInitCompletedParams;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Template per i DAO verso Moonprint API REST con BasicAuth
 *
 * @author Laurent Pissard
 *
 */
public class MoonprintTemplateImpl {
	
	protected static final String MSG_ERR_DEFAULT = "ERRORE CHIAMATA MOONPRINT ";
	protected static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	
	protected static String getEndpoint() {
		return EnvProperties.readFromFile(EnvProperties.MOONPRINT_ENDPOINT) + "/restfacade/be";
	}
	protected static String getAuthUsr() {
		return EnvProperties.readFromFile(EnvProperties.MOONPRINT_USERNAME);
	}
	protected static String getAuthPwd() {
		return EnvProperties.readFromFile(EnvProperties.MOONPRINT_PASSWORD);
	}
	
	/*
	 * Can be overwriten 
	 * sample :
	 *  /extra/territorio/toponomastica/vie
	 */
	protected String getPathExtra() {
		return "";
	}

	
	/**
	 * GET su Moonprint in TEXT
	 * @param url
	 * @return String
	 * @throws DAOException
	 */
	protected String getMoonprint(String url) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request();
        Response response = builder.get();
        gestioneResponseKO(response, webTarget);
    	return builder.get(String.class);
	}
	
	/**
	 * GET su Moonprint in JSON
	 * @param <T>
	 * @param url
	 * @param class1
	 * @return JSON (Object or List)
	 * @throws DAOException
	 */
	protected <T> T getMoonprintJson(String url, Class<T> class1) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(); // MediaType.APPLICATION_JSON_TYPE
        builder.accept(MediaType.APPLICATION_JSON);
        Response response = builder.get();
        gestioneResponseKO(response, webTarget);
    	return builder.get(class1);
	}
	
	/**
	 * GET su Moonprint in JSON with params
	 * @param <T>
	 * @param url
	 * @param class1
	 * @param queryParams
	 * @return JSON (Object or List)
	 * @throws DAOException
	 */
	protected <T> T getMoonprintJson(String url, Class<T> class1, MultivaluedMap<String, Object> queryParams) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
		webTarget.queryParams(queryParams);
        Builder builder = webTarget.request(); // MediaType.APPLICATION_JSON_TYPE
        builder.accept(MediaType.APPLICATION_JSON);
        Response response = builder.get();
        gestioneResponseKO(response, webTarget);
    	return builder.get(class1);
	}
	
	
	
	

	protected <T, E> T postMoonprintJson(String url, Class<T> class1, E body) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        builder.accept(MediaType.APPLICATION_JSON);
        Response response = builder.post(Entity.json(body));
        gestioneResponseKO(response, webTarget);
    	return builder.post(Entity.json(body),class1);
	}
	protected <T> T getMoonprintJson(String url, Class<T> class1, IstanzaInitCompletedParams initParams) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		
//		ClientConfig config = new ClientConfig();
//		config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
//		JerseyClient client = JerseyClientBuilder.createClient(config);
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        builder.accept(MediaType.APPLICATION_JSON);
        Entity<IstanzaInitCompletedParams> e = Entity.json(initParams);
        Response response = builder.post(e);
        gestioneResponseKO(response, webTarget);
    	return builder.post(Entity.json(initParams),class1);
	}
	
	
	protected void gestioneResponseKO(Response response, ResteasyWebTarget webTarget) throws DAOException {
        if (response.getStatus() != Status.OK.getStatusCode()) {
            if (response.getStatus() == Status.UNAUTHORIZED.getStatusCode()) {
                throw new DAOException("MOONPRN User not authenticated or session timeout.");
            } else {
                throw new DAOException(String.format("MOONPRN Cannot list the specified location: %s", webTarget.getUri().getPath()));
            }
        }
	}
}
