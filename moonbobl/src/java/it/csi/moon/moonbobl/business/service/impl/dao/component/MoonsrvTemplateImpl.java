/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.component;


import java.io.IOException;
import java.io.InputStream;

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

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaInitParams;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloClass;
import it.csi.moon.moonbobl.dto.moonfobl.MoonError;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.EnvProperties;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Template per i DAO verso moonsrv API REST con BasicAuth
 *
 * @author Laurent Pissard
 *
 */
public class MoonsrvTemplateImpl {
	
	protected final static String MSG_ERR_DEFAULT = "ERRORE CHIAMATA MOONSRV ";
	protected final static String CLASS_NAME = "MoonsrvTemplateImpl";
	protected static final Logger log = LoggerAccessor.getLoggerIntegration();
	
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


	// Altra possibilitia uso BasicAuth :
	//  builder.header(HttpHeaders.AUTHORIZATION, getAuthHeader());
	//
//		public static String getAuthHeader() {
//			String auth = Constants.getWsEndpointParams(Constants.MOONSRV_USERNAME) + ":" + Constants.getWsEndpointParams(Constants.MOONSRV_PASSWORD);
//		    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
//		    String authHeader = "Basic " + new String(encodedAuth);
//		    return authHeader;
//		}
	
	/**
	 * GET su moonsrv in TEXT
	 * @param url
	 * @return String
	 * @throws DAOException
	 */
	protected String getMoonsrv(String url) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request();
        Response response = builder.get();
        gestioneResponseKO(response, webTarget);
    	return response.readEntity(String.class);
	}
	
	/**
	 * DELETE su moonsrv
	 * @param url
	 * @throws DAOException
	 */
	protected void deleteMoonsrv(String url) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request();
        Response response = builder.delete();
        gestioneResponseKO(response, webTarget);
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
		ResteasyWebTarget webTarget = client.target(URL).queryParams(queryParams)
														.register(new CustomRestLoggingFilter())
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
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(); // MediaType.APPLICATION_JSON_TYPE
        // builder.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        Response response = builder.get();
        gestioneResponseKO(response, webTarget);
         		    
        String contentDisposition = response.getHeaderString("Content-Disposition");
        Long contentLength = Long.valueOf( (response.getHeaderString("Content-Length")==null ? "0" : response.getHeaderString("Content-Length") ));
        log.debug("[" + CLASS_NAME + "::getMoonsrvBytes] contentDisposition=" + contentDisposition);
        log.debug("[" + CLASS_NAME + "::getMoonsrvBytes] contentLength=" + contentLength);
	    
        InputStream inputStream = response.readEntity(InputStream.class);

	    byte[] bytes;
		try {
			bytes = inputStream.readAllBytes();
		} catch (IOException e) {
			log.error("[" + CLASS_NAME + "::getMoonsrvBytes] IOException ", e);
			throw new DAOException();
		} // IOUtils.toByteArray(inputStream);
	    return bytes;
	}
	
	/**
	 * POST su moonsrv OUT bytes
	 * @param url
	 * @return
	 * @throws DAOException
	 */
	protected <E> byte[] postMoonsrvBytes(String url, E body) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(); // MediaType.APPLICATION_JSON_TYPE
        // builder.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        Response response = builder.post(Entity.json(body));
        gestioneResponseKO(response, webTarget);
         		    
        String contentDisposition = response.getHeaderString("Content-Disposition");
        Long contentLength = Long.valueOf( (response.getHeaderString("Content-Length")==null ? "0" : response.getHeaderString("Content-Length") ));
        log.debug("[" + CLASS_NAME + "::postMoonsrvBytes] contentDisposition=" + contentDisposition);
        log.debug("[" + CLASS_NAME + "::postMoonsrvBytes] contentLength=" + contentLength);
	    
        InputStream inputStream = response.readEntity(InputStream.class);

	    byte[] bytes;
		try {
			bytes = inputStream.readAllBytes();
		} catch (IOException e) {
			log.error("[" + CLASS_NAME + "::postMoonsrvBytes] IOException ", e);
			throw new DAOException();
		} // IOUtils.toByteArray(inputStream);
	    return bytes;
	}
	
	protected <T, E> T postMoonsrvJson(String url, Class<T> class1, E body) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        builder.accept(MediaType.APPLICATION_JSON);
        Response response = builder.post(body!=null?Entity.json(body):null);
        gestioneResponseKO(response, webTarget);
//    	return builder.post(Entity.json(body),class1);
    	return response.readEntity(class1);
	}
	
	/**
	 * POST senza body
	 */
	protected <T, E> T postMoonsrvJson(String url, Class<T> class1) throws ItemNotFoundDAOException, DAOException {
    	return postMoonsrvJson(url, class1, null);
	}
	
	protected <T> T postMoonsrvJson(String url, Class<T> class1, IstanzaInitParams initParams) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        builder.accept(MediaType.APPLICATION_JSON);
        Entity e = Entity.json(initParams);
        Response response = builder.post(e);
        gestioneResponseKO(response, builder, e, webTarget);
//    	return builder.post(e,class1);
    	return response.readEntity(class1);
	}
	
	
	protected void gestioneResponseKO(Response response, Builder builder, Entity entity, ResteasyWebTarget webTarget) throws DAOException {
        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new DAOException(String.format("MOONSRV: %d - %s FOR: %s", response.getStatus(), response.getStatusInfo(), webTarget.getUri().getPath()));

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

//            if (response.getStatus() == Status.UNAUTHORIZED.getStatusCode()) {
//                throw new DAOException("MOONSRV User not authenticated or session timeout.");
//            } else {
//                throw new DAOException(String.format("MOONSRV: %d - %s for location %s", response.getStatus(), response.getStatusInfo(), webTarget.getUri().getPath()));
//            }
        }
	}
	
	/**
	 * POST su moonsrv for upload file.class  
	 * @param url
	 * @return ModuloClass
	 */
	protected ModuloClass postMoonsrvFileClass(String url, byte[] body) throws DAOException {
		String URL = getEndpoint() + getPathExtra() + url;
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget webTarget = client.target(URL);
		webTarget.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()));
        Builder builder = webTarget.request();
        
        Entity entity = Entity.entity(body, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        Response response = builder.post(entity);
        gestioneResponseKOwithNotFound(response, webTarget);
         		    
        //String contentDisposition = response.getHeaderString("Content-Disposition");
        //Long contentLength = Long.valueOf( (response.getHeaderString("Content-Length")==null ? "0" : response.getHeaderString("Content-Length") ));
        //log.debug("[" + CLASS_NAME + "::postMoonsrvFileClass] contentDisposition=" + contentDisposition);
        //log.debug("[" + CLASS_NAME + "::postMoonsrvFileClass] contentLength=" + contentLength);
	    
        ModuloClass ris = response.readEntity(ModuloClass.class);

	    return ris;
	}
	
	protected void gestioneResponseKOwithNotFound(Response response, ResteasyWebTarget webTarget) throws ItemNotFoundDAOException, DAOException {
        if (response.getStatus() != Status.OK.getStatusCode() ) {
        	if (response.getStatus() == 404) {
        		throw new ItemNotFoundDAOException();
        	} else {
        		MoonError moonError = response.readEntity(MoonError.class);
        		throw new DAOException(moonError.getMsg(),moonError.getCode(),moonError.getTitle());
        	}
        }
	}

}
