/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.component;


import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.MoonError;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.util.EnvProperties;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * Template per i DAO verso moonsrv API REST con BasicAuth
 *
 * @author Laurent Pissard
 *
 */
public class MoonsrvTemplateImpl {
	
	protected static final String CLASS_NAME = "MoonsrvTemplateImpl";
	protected static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	protected static final String MSG_ERR_DEFAULT = "ERRORE CHIAMATA MOONSRV ";
	
	protected static String getEndpoint() {
		return EnvProperties.readFromFile(EnvProperties.MOONSRV_ENDPOINT) + "/restfacade/be";
	}
	protected static String getAuthUsr() {
		return EnvProperties.readFromFile(EnvProperties.MOONSRV_USERNAME);
	}
	protected static String getAuthPwd() {
		return EnvProperties.readFromFile(EnvProperties.MOONSRV_PASSWORD);
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
	 * GET su moonsrv in TEXT
	 * @param url
	 * @return String
	 * @throws DAOException
	 */
	protected String getMoonsrv(String url) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		client.getConfiguration();  // puo andare NullPointerException resteasy.client.jaxrs.internal.ClientWebTarget
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request();
        Response response = builder.get();
        gestioneResponseKO(response, webTarget);
    	return response.readEntity(String.class);
	}
	
	/**
	 * GET su moonsrv in JSON
	 * @param <T>
	 * @param url
	 * @param class1
	 * @return JSON (Object or List)
	 * @throws DAOException
	 */
	protected <T> T getMoonsrvJson(String url, Class<T> class1) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		client.getConfiguration();  // puo andare NullPointerException resteasy.client.jaxrs.internal.ClientWebTarget
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(); // MediaType.APPLICATION_JSON_TYPE
        builder.accept(MediaType.APPLICATION_JSON);
        Response response = builder.get();
        gestioneResponseKO(response, webTarget);
    	return response.readEntity(class1);
	}
	
	/**
	 * GET su moonsrv in JSON with params
	 * @param <T>
	 * @param url
	 * @param class1
	 * @param queryParams
	 * @return JSON (Object or List)
	 * @throws DAOException
	 */
	protected <T> T getMoonsrvJson(String url, Class<T> class1, MultivaluedMap<String, Object> queryParams) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		client.getConfiguration();  // puo andare NullPointerException resteasy.client.jaxrs.internal.ClientWebTarget
		ResteasyWebTarget webTarget = client.target(URL).queryParams(queryParams)
														.register(new LoggCustomRestLoggingFilter())
														.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(); // MediaType.APPLICATION_JSON_TYPE
        builder.accept(MediaType.APPLICATION_JSON);
        Response response = builder.get();
        gestioneResponseKO(response, webTarget);
    	return response.readEntity(class1);
	}
	
	/**
	 * GET su moonsrv OUT bytes
	 * @param url
	 * @return
	 * @throws DAOException
	 */
	protected byte[] getMoonsrvBytes(String url) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		client.getConfiguration();  // puo andare NullPointerException resteasy.client.jaxrs.internal.ClientWebTarget
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(); // MediaType.APPLICATION_JSON_TYPE
        // builder.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        Response response = builder.get();
        gestioneResponseKO(response, webTarget);
         		    
        String contentDisposition = response.getHeaderString("Content-Disposition");
        Long contentLength = Long.valueOf( (response.getHeaderString("Content-Length")==null ? "0" : response.getHeaderString("Content-Length") ));
        LOG.debug("[" + CLASS_NAME + "::getMoonsrvBytes] contentDisposition=" + contentDisposition);
        LOG.debug("[" + CLASS_NAME + "::getMoonsrvBytes] contentLength=" + contentLength);
	    
        InputStream inputStream = response.readEntity(InputStream.class);

	    byte[] bytes;
		try {
			bytes = inputStream.readAllBytes();
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::getMoonsrvBytes] IOException ", e);
			throw new DAOException();
		} // IOUtils.toByteArray(inputStream);
	    return bytes;
	}
	

	protected <T, E> T postMoonsrvJson(String url, Class<T> class1, E body) throws ItemNotFoundDAOException, DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		client.getConfiguration();  // puo andare NullPointerException resteasy.client.jaxrs.internal.ClientWebTarget
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        builder.accept(MediaType.APPLICATION_JSON);
        Response response = builder.post(body!=null?Entity.json(body):null);
        gestioneResponseKOwithNotFound(response, webTarget);
//    	return builder.post(Entity.json(body),class1);
    	return response.readEntity(class1);
	}
	/**
	 * POST senza body
	 */
	protected <T, E> T postMoonsrvJson(String url, Class<T> class1) throws ItemNotFoundDAOException, DAOException {
    	return postMoonsrvJson(url, class1, null);
	}
	
//	protected <T> T postMoonsrvJson(String url, Class<T> class1, IstanzaInitParams initParams) throws DAOException {
//		String URL = getEndpoint() + getPathExtra() + url;
//		
//		ResteasyClient client = new ResteasyClientBuilder().build();
//		ResteasyWebTarget webTarget = client.target(URL);
//		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
//        Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
//        builder.accept(MediaType.APPLICATION_JSON);
//        Entity e = Entity.json(initParams);
//        Response response = builder.post(e);
//        gestioneResponseKO(response, builder, e, webTarget);
////    	return builder.post(e,class1);
//    	return response.readEntity(class1);
//	}
	
	
	protected void gestioneResponseKO(Response response, Builder builder, Entity entity, ResteasyWebTarget webTarget) throws DAOException {
        if (response.getStatus() != Status.OK.getStatusCode()) {
    		throw new DAOException(String.format("MOONSRV: %d - %s FOR: %s", response.getStatus(), response.getStatusInfo(), webTarget.getUri().getPath()));
//        	MoonError moonError = response.readEntity(MoonError.class);
//    		throw new DAOException(moonError.getMsg(),moonError.getCode(),moonError.getTitle());

//            if (response.getStatus() == Status.UNAUTHORIZED.getStatusCode()) {
//                throw new DAOException("MOONSRV User not authenticated or session timeout.");
//            } else if (response.getStatus() == Status.BAD_REQUEST.getStatusCode()) {
//                throw new DAOException(String.format("MOONSRV Cannot list the specified location: %s", webTarget.getUri().getPath()));
//            } else {
//                throw new DAOException(String.format("MOONSRV Cannot list the specified location: %s", webTarget.getUri().getPath()));
//            }
        }
	}
	
	protected void gestioneResponseKO(Response response, ResteasyWebTarget webTarget) throws DAOException {
        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new DAOException(String.format("MOONSRV: %d - %s FOR: %s", response.getStatus(), response.getStatusInfo(), webTarget.getUri().getPath()));
//        	MoonError moonError = response.readEntity(MoonError.class);
//    		throw new DAOException(moonError.getMsg(),moonError.getCode(),moonError.getTitle());

//            if (response.getStatus() == Status.UNAUTHORIZED.getStatusCode()) {
//                throw new DAOException("MOONSRV User not authenticated or session timeout.");
//            } else {
//            	if (response.getStatus() == 404) 
//            		throw new ItemNotFoundDAOException();
//            	else {
//		   		MoonError moonError = response.readEntity(MoonError.class);
//    			throw new DAOException(moonError.getMsg(),moonError.getCode(),moonError.getTitle());
//                throw new DAOException(String.format("MOONSRV: %d - %s for location %s", response.getStatus(), response.getStatusInfo(), webTarget.getUri().getPath()));
//            	}
//            }
        }
	}
	
	protected void gestioneResponseKOwithNotFound(Response response, ResteasyWebTarget webTarget) throws ItemNotFoundDAOException, DAOException {
        if (response.getStatus() != Status.OK.getStatusCode() ) {
        	if (response.getStatus() == 404) {
        		throw new ItemNotFoundDAOException();
//        		throw response.readEntity(ItemNotFoundDAOException.class);
        	} else {
//        		throw new DAOException(String.format("MOONSRV: %d - %s FOR: %s", response.getStatus(), response.getStatusInfo(), webTarget.getUri().getPath()));
        		MoonError moonError = response.readEntity(MoonError.class);
        		throw new DAOException(moonError.getMsg(),moonError.getCode(),moonError.getTitle());
        	}
        }
	}

	
	protected <T,E> T postJsonMultipartForm(String url, Class<T> clazzT, MultipartFormDataOutput bodyMultipart) {
		return callJsonMultipartForm(url, clazzT, bodyMultipart, HttpMethod.POST);
	}
	protected <T,E> T putJsonMultipartForm(String url, Class<T> clazzT, MultipartFormDataOutput bodyMultipart) {
		return callJsonMultipartForm(url, clazzT, bodyMultipart, HttpMethod.PUT);
	}
	private <T,E> T callJsonMultipartForm(String url, Class<T> clazzT, MultipartFormDataOutput bodyMultipart, String method) {
		final String methodName = "callJsonMultipartForm";
		boolean requestInviata = false;
		Response response = null;
		ResteasyClient client = null;
		String strResponse = null;
		T result = null;
		try {
			String URL = getEndpoint() + getPathExtra() + url;
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] BEGIN URL="+URL);

			client = new ResteasyClientBuilder().build();
			client.getConfiguration();  // puo andare NullPointerException resteasy.client.jaxrs.internal.ClientWebTarget
			ResteasyWebTarget webTarget = client.target(URL);
			webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
	        Builder builder = webTarget.request(MediaType.MULTIPART_FORM_DATA_TYPE);
	        builder.accept(MediaType.APPLICATION_JSON);
			GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(bodyMultipart) { };
			if(HttpMethod.POST.equals(method)) {
				response = builder.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
			} else
			if(HttpMethod.PUT.equals(method)) {
				response = builder.put(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
			}
			requestInviata = true;
			switch (response.getStatus()) {
				case 200:
				case 201: /* 201 - Used in NOTIFY-MB */
					try {
						response.bufferEntity();
						strResponse = response.readEntity(String.class);
						result = getMapper().readValue(strResponse, clazzT);
					} catch (Exception e) {
						LOG.error("[" + CLASS_NAME + "::" + methodName + "] 200 ResponseString : " + strResponse + "\n"+ e.getMessage());
						throw new DAOException("Deserializzazione JSON Apimint Response Errore");
					}
					break;
				default:
					LOG.debug("[" + CLASS_NAME + "::" + methodName + "] status code" + response.getStatus());
					String message = String.valueOf(response.getStatus());
					strResponse = retrieveStrResponseIfPossible(response);
					if (message!=null) {
						// Gestione degli warning / errori settati dal servizio chiamate via setMapStatusCodeMessage()
						LOG.error("[" + CLASS_NAME + "::" + methodName + "] " + message + " strResponse=" + strResponse + " forURL=" + URL);
						throw new DAOException(response.getStatus(),message);
					} else {
						LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response : " + response.getStatus() + " " + response.getStatusInfo() + " strResponse=" + strResponse + " forURL=" + URL);
						throw new DAOException("Errore recupero " + response.getStatus() + " " + response.getStatusInfo());
					}
				}
			 return result;
		} catch (DAOException e) {
			throw e;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response : " + response.getStatus() + " " + response.getStatusInfo());
			throw new DAOException("Errore generico " + response.getStatus() + " " + response.getStatusInfo());
		} finally {
			if (requestInviata)
				response.close();
			if (client!=null && !client.isClosed())
				client.close();
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] END");
		}
	}
	
	private ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();			
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		return mapper;
	}
	private String retrieveStrResponseIfPossible(Response response) {
		try {
			response.bufferEntity();
			return response.readEntity(String.class);
		} catch (Exception e) { /* Nothing */ }
		return null;
	}
	
}
