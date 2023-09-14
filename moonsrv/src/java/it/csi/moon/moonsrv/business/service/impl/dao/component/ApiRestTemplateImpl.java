/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.component;

import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Template per i DAO verso API REST (con BasicAuth ?)
 *
 * @author Francesco Zucaro
 * @author Laurent Pissard
 * 
 * @since 1.0.0
 */
public class ApiRestTemplateImpl {
	
	private static final String CLASS_NAME = "ApiRestTemplateImpl";
	protected static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	protected static final String MSG_ERR_DEFAULT = "ERRORE CHIAMATA API REST";
	
	/* Parametri per servizi REST */
	private String endpoint;
	/* Parametri per servizi REST */
	private String digitalIdentity;
	private Map<Integer, String> serviceMapStatusCodeMessage;
	private boolean forseNamingStrategy_SNAKE_CASE = false; // solo nel payload su POST e PUT
	private boolean FAIL_ON_UNKNOWN_PROPERTIES = true; // solo nelle response
   
    /**
     * Funzioni di inizializzazione dei parametri generici dell'API Manager Outer
     * identificazione censimento dell'applicazioni fruitrici (SP) Moon con OAuth2 grant type client credential, 
     *     con la coppia di di chiavi consumer_key e consuymer_secret 
     *     che servono per recuperare l'access token (in test ha validità di 365 giorni) che servirà per l'invocare l'API.
     */
	protected String getEndpoint() {
		return this.endpoint;
	}
	protected void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	protected boolean isForseNamingStrategy_SNAKE_CASE() {
		return this.forseNamingStrategy_SNAKE_CASE;
	}
	protected void setForseNamingStrategy_SNAKE_CASE(boolean forse) {
		this.forseNamingStrategy_SNAKE_CASE = forse;
	}
	public boolean isFAIL_ON_UNKNOWN_PROPERTIES() {
		return FAIL_ON_UNKNOWN_PROPERTIES;
	}
	public void setFAIL_ON_UNKNOWN_PROPERTIES(boolean fAIL_ON_UNKNOWN_PROPERTIES) {
		FAIL_ON_UNKNOWN_PROPERTIES = fAIL_ON_UNKNOWN_PROPERTIES;
	}

	protected String getUsername() {
		return null;
	}
	protected String getPassword() {
		return null;
	}
	protected boolean isWithBasicAuth() {
		boolean isWithBasicAuth = (StringUtils.isNotEmpty(getUsername()) && !getUsername().startsWith("@@") && StringUtils.isNotEmpty(getPassword()))?true:false;
		return isWithBasicAuth;
	}
	
	/*
	 * Can be overwriten 
	 * sample :
	 *  /demografia/v1
	 *  /ctsapi/v1
	 */
	protected String getPathExtra() {
		return "";
	}
	/*
	 * Can be overwriten if necessary use apimint over https ; contain baseApimintEndPoint + basePath of API
	 */
	protected String getBaseApimintUrl() {
		return getEndpoint() + getPathExtra();
	}
	
	//
	protected ResteasyClient getResteasyClient() {
		return new ResteasyClientBuilder().build();
	}
	
	
	protected ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();			
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, isFAIL_ON_UNKNOWN_PROPERTIES());
		return mapper;
	}
	
	
	/**
	 * GET su servizio REST
	 * @param <T> Tipologia clazz dell'oggetto di risposta
	 * @param url http del servizio REST da richiamare
	 * @param clazz dell'oggetto di risposta
	 * @return l'oggetto di risposta ApimintResponse
	 * @throws DAOException nel caso di errori
	 */
	protected <T> ResponseUUID<T> getJson(String url, Class<T> clazz) throws DAOException {
		final String methodName = "getJson";
		boolean requestInviata = false;
		Response response = null;
		ResteasyClient client = null;
		ResponseUUID<T> responseUUID = null;
		String strResponse = null;
		String targetUrl = null;
		try {
			targetUrl = getBaseApimintUrl() + url;
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] BEGIN targetUrl=" + targetUrl);
			UUID uuid = UUID.randomUUID();
			
			client = getResteasyClient();
			ResteasyWebTarget targetSrv = client.target(targetUrl);
			addBasicAuthIfNecessary(targetSrv);
			
			response = targetSrv.request(MediaType.APPLICATION_JSON)
				.headers(getHeaders(uuid))
				.get();
			requestInviata = true;
			strResponse = retrieveStrResponseIfPossible(response);
			switch (response.getStatus()) {
				case 200:
					responseUUID = buildResponseUUID(methodName, uuid, strResponse, clazz);
					break;
				default:
					String message = getResponseMessage(response.getStatus());
					throw buildDAOException(methodName, targetUrl, strResponse, response, message);
				}
			 return responseUUID;
		} catch (DAOException e) {
			throw e;
		} catch (Exception e) {
			throw buildDAOExceptionAndLogError(methodName, targetUrl, strResponse, response);
		} finally {
			if (requestInviata)
				response.close();
			if (client!=null && !client.isClosed())
				client.close();
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] END");
		}
	}
	
	protected int getJsonStatus(String url) throws DAOException {
		final String methodName = "getJsonStatus";
		boolean requestInviata = false;
		Response response = null;
		ResteasyClient client = null;
		String strResponse = null;
		String targetUrl = null;
		try {
			targetUrl = getBaseApimintUrl() + url;
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] BEGIN targetUrl=" + targetUrl);
			UUID uuid = UUID.randomUUID();
			
			client = getResteasyClient();
			ResteasyWebTarget targetSrv = client.target(targetUrl);
			addBasicAuthIfNecessary(targetSrv);
			
			response = targetSrv.request(MediaType. APPLICATION_JSON)
				.headers(getHeaders(uuid))
				.get();
			requestInviata = true;
			switch (response.getStatus()) {
				case 200:
					break;
				default:
					String message = getResponseMessage(response.getStatus());
					strResponse = retrieveStrResponseIfPossible(response);
					throw buildDAOException(methodName, targetUrl, strResponse, response, message);
				}
			 return response.getStatus();
		} catch (DAOException e) {
			throw e;
		} catch (Exception e) {
			if (response!=null) {
				LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response : " + response.getStatus() + " " + response.getStatusInfo());
				throw new DAOException("Errore generico " + response.getStatus() + " " + response.getStatusInfo());
			} else {
				LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response NULL.");
				throw new DAOException("Errore generica.");	
			}
		} finally {
			if (requestInviata)
				response.close();
			if (client!=null && !client.isClosed())
				client.close();
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] END");
		}
	}

	protected String retrieveStrResponseIfPossible(Response response) {
		try {
			response.bufferEntity();
			return response.readEntity(String.class);
		} catch (Exception e) { /* Nothing */ }
		return null;
	}

	/**
	 * GET su servizio REST
	 * @param <T> Tipologia clazz dell'oggetto di risposta
	 * @param url http del servizio REST da richiamare
	 * @return l'oggetto di risposta byte[] 
	 * @throws DAOException nel caso di errori
	 */
	protected byte[] getJsonWithBinaryRedirect(String url) throws DAOException {
		final String methodName = "getJsonWithBinaryRedirect";
		boolean requestInviata = false;
		Response response = null;
		ResteasyClient client = null;
		String strResponse = null;
		
		byte[] bytes = null;
		String targetUrl = null;
		try {
			targetUrl = getBaseApimintUrl() + url;
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] BEGIN targetUrl=" + targetUrl);
			UUID uuid = UUID.randomUUID();
			
			client = getResteasyClient();
			ResteasyWebTarget targetSrv = client.target(targetUrl);
			addBasicAuthIfNecessary(targetSrv);			
			response = targetSrv.request(MediaType. APPLICATION_JSON)
				.headers(getHeaders(uuid))
				.get();
			requestInviata = true;
			strResponse = retrieveStrResponseIfPossible(response);
			switch (response.getStatus()) {
				case 302:
					URI uri = response.getLocation();
					LOG.info("[" + CLASS_NAME + "::" + methodName + "] STATE 302 REDIRECT TRY FOLLOW URL");
					if (uri != null) {
					   java.net.URL urlRedirect = uri.toURL();
					   String followUrl =  urlRedirect.toString();					   
					   LOG.info("[" + CLASS_NAME + "::" + methodName + "] FOLLOW URL: " + followUrl);
					   bytes = getRedirectResponse(followUrl);
					} else {
						LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore location non trovata");
						throw new DAOException("Errore recupero " + response.getStatus() + " " + response.getStatusInfo());
					}							                 
					break;					
				default:
					String message = getResponseMessage(response.getStatus());
					throw buildDAOException(methodName, targetUrl, strResponse, response, message);
				}
			 return bytes;
		} catch (DAOException e) {
			throw e;
		} catch (Exception e) {
//			LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response : " + response.getStatus() + " " + response.getStatusInfo());
//			throw new DAOException("Errore generico " + response.getStatus() + " " + response.getStatusInfo());
			throw buildDAOException(methodName, targetUrl, strResponse, response);
		} finally {
			if (requestInviata)
				response.close();
			if (client!=null && !client.isClosed())
				client.close();
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] END");
		}
	}
	
	/**
	 * GET su servizio REST
	 * @param <T> Tipologia clazz dell'oggetto di risposta
	 * @param url http del servizio REST da richiamare
	 * @return l'oggetto di risposta byte[] 
	 * @throws DAOException nel caso di errori
	 */
	protected byte[] getRedirectResponse(String targetUrl) throws DAOException {
		final String methodName = "getRedirectResponse";
		final int TIMEOUT = 20000;
		String strResponse = null;
		byte[] bytes = null;
		LOG.info("[" + CLASS_NAME + "::" + methodName + "] targetUrl = " + targetUrl);
		ResteasyClient client = new ResteasyClientBuilder()
				.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
				.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
				.build();
		client.getConfiguration();  
		ResteasyWebTarget webTarget = client.target(targetUrl);
        Builder builder = webTarget.request().accept("application/octect-stream");
        Response response = builder.get();
        LOG.info("[" + CLASS_NAME + "::" + methodName + "] RESPONSE STATUS = "+response.getStatus());
    	switch (response.getStatus()) {
		case 200:	
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] CASE 200  GET STREAM RESPONSE");
			try {
				InputStream inputStream = response.readEntity(InputStream.class);
				bytes = inputStream.readAllBytes();
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::" + methodName + "] 200 exception : "+ e.getMessage());
				throw new DAOException("Deserializzazione JSON Apimint Response Errore");
			}
			break;
		default:
			String message = getResponseMessage(response.getStatus());
			strResponse = retrieveStrResponseIfPossible(response);
			throw buildDAOException(methodName, targetUrl, strResponse, response, message);
		}
	    LOG.info("[" + CLASS_NAME + "::getRedirectResponse] bytes.length=" + (bytes!=null?bytes.length:"null"));		   		   
	    return bytes;
	}


	/**
	 * POST su servizio REST
	 * @param <T> Tipologia clazz dell'oggetto di risposta
	 * @param url http del servizio REST da richiamare
	 * @param clazzT dell'oggetto di risposta
	 * @param message   *** TODO DA GENERALIZZARE ***
	 * @return l'oggetto di risposta ApimintResponse
	 * @throws DAOException nel caso di errori
	 */
	protected <T> ResponseUUID<T> postJson(String url, Class<T> clazz, Object body) {
		UUID uuid = UUID.randomUUID();
		return postJson(url, clazz, body, uuid);
	}
	protected <T> ResponseUUID<T> postJson(String url, Class<T> clazz, Object body, UUID uuid) {
		final String methodName = "postJson";
		boolean requestInviata = false;
		Response response = null;
		ResteasyClient client = null;
		ResponseUUID<T> responseUUID = null;
		String strResponse = null;
		String targetUrl = null;
		try {
			targetUrl = getBaseApimintUrl() + url;
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] BEGIN targetUrl=" + targetUrl);
			logWarnIfBodyIsNull(methodName, body);

			String bodySnake = null;
			if (isForseNamingStrategy_SNAKE_CASE()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE);
				bodySnake = mapper.writeValueAsString(body);
			}
			
			client = getResteasyClient();
			ResteasyWebTarget targetSrv = client.target(targetUrl);
			addBasicAuthIfNecessary(targetSrv);
			response = targetSrv.request(MediaType.APPLICATION_JSON)
				.headers(getHeaders(uuid))
				.post(Entity.entity(isForseNamingStrategy_SNAKE_CASE()?bodySnake:body,MediaType.APPLICATION_JSON));
			requestInviata = true;
			strResponse = retrieveStrResponseIfPossible(response);
			switch (response.getStatus()) {
				case 200:
				case 201: /* 201 - Used in NOTIFY-MB */
					responseUUID = buildResponseUUID(methodName, uuid, strResponse, clazz);
					break;
				default:
					String strRequest = null;
					if (bodySnake==null) {
						ObjectMapper mapper = new ObjectMapper();
						strRequest = mapper.writeValueAsString(body);
					} else {
						strRequest = bodySnake;
					}
					LOG.debug("[" + CLASS_NAME + "::" + methodName + "] status code " + response.getStatus() + "\nREQUEST: " + strRequest + "\nRESPONSE: " + strResponse);
					String message = getResponseMessage(response.getStatus());
					throw buildDAOExceptionWithBody(methodName, targetUrl, strResponse, response, message, body, bodySnake);
				}
			 return responseUUID;
		} catch (DAOException e) {
			throw e;
		} catch (Exception e) {
			throw buildDAOExceptionAndLogError(methodName, targetUrl, strResponse, response);
		} finally {
			if (requestInviata)
				response.close();
			if (client!=null && !client.isClosed())
				client.close();
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] END");
		}
	}
	protected void addBasicAuthIfNecessary(ResteasyWebTarget targetSrv) {
		if (isWithBasicAuth()) {
			targetSrv.register(new BasicAuthentication(getUsername(), getPassword()));
		}
	}
	protected Exception buildDAOExceptionWithBody(final String methodName, String url, String strResponse, Response response, String message, 
		Object body, String bodySnake) {
		DAOException daoe = null;
		if (response!=null) {
			if (message!=null) {
				// Gestione degli warning / errori settati dal servizio chiamate via setMapStatusCodeMessage()
				LOG.error("[" + CLASS_NAME + "::" + methodName + "] " + message + " for " + url + "\nREQUEST: " + ((bodySnake!=null)?bodySnake:body) + "\nRESPONSE: " + strResponse);
				throw new DAOException(response.getStatus(),message);
			} else {
				LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response : " + response.getStatus() + " " + response.getStatusInfo() + " for " + url + "\nREQUEST: " + ((bodySnake!=null)?bodySnake:body) + "\nRESPONSE: " + strResponse);
				throw new DAOException("Errore recupero " + response.getStatus() + " " + response.getStatusInfo());
			}
		} else {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response NULL.  strResponse=" + strResponse + " for url=" + url);
			daoe = new DAOException("Errore recupero generica");
		}
		return daoe;
	}
	
	protected void logWarnIfBodyIsNull(final String methodName, Object body) {
		if (body==null) {
			LOG.warn("[" + CLASS_NAME + "::" + methodName + "] POST with body NULL !");
		}
	}
	
	protected DAOException buildDAOExceptionAndLogError(final String methodName, final String url, final String strResponse, Response response) {
		if (response!=null) {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response : " + response.getStatus() + " " + response.getStatusInfo());
			return new DAOException("Errore generico " + response.getStatus() + " " + response.getStatusInfo());
		} else {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response null. strResponse=" + strResponse + " for URL=" + url);
			return new DAOException("Errore generico " + methodName);
		}
	}

	protected DAOException buildDAOException(final String methodName, final String url,  final String strResponse, Response response) {
		return buildDAOException(methodName, url, strResponse, response);
	}
	protected DAOException buildDAOException(final String methodName, final String url,  final String strResponse, Response response, String message) {
		DAOException daoe = null;
		if (response!=null) {
			if (message!=null) {
				// Gestione degli warning / errori settati dal servizio chiamate via setMapStatusCodeMessage()
				LOG.error("[" + CLASS_NAME + "::" + methodName + "] " + message + " strResponse=" + strResponse + " for url=" + url);
				daoe = new DAOException(response.getStatus(), message);
			} else {
				LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response : " + response.getStatus() + " " + response.getStatusInfo() + " strResponse=" + strResponse + " for url=" + url);
				daoe = new DAOException("Errore recupero " + response.getStatus() + " " + response.getStatusInfo());
			}
		} else {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore response NULL.  strResponse=" + strResponse + " for url=" + url);
			daoe = new DAOException("Errore recupero generica");
		}
		return daoe;
	}
	
	protected <T> ResponseUUID<T> postJsonMultipartForm(String url, Class<T> clazz, MultipartFormDataOutput bodyMultipart) {
		return callJsonMultipartForm(url, clazz, bodyMultipart, UUID.randomUUID(), HttpMethod.POST);
	}
	protected <T> ResponseUUID<T> postJsonMultipartForm(String url, Class<T> clazz, MultipartFormDataOutput bodyMultipart, UUID uuid) {
		return callJsonMultipartForm(url, clazz, bodyMultipart, uuid, HttpMethod.POST);
	}
	protected <T> ResponseUUID<T> putJsonMultipartForm(String url, Class<T> clazz, MultipartFormDataOutput bodyMultipart) {
		return callJsonMultipartForm(url, clazz, bodyMultipart, UUID.randomUUID(), HttpMethod.PUT);
	}
	protected <T> ResponseUUID<T> putJsonMultipartForm(String url, Class<T> clazz, MultipartFormDataOutput bodyMultipart, UUID uuid) {
		return callJsonMultipartForm(url, clazz, bodyMultipart, uuid, HttpMethod.PUT);
	}
	private <T> ResponseUUID<T> callJsonMultipartForm(String url, Class<T> clazz, MultipartFormDataOutput bodyMultipart, UUID uuid, String method) {
		final String methodName = "callJsonMultipartForm";
		boolean requestInviata = false;
		Response response = null;
		ResteasyClient client = null;
		ResponseUUID<T> responseUUID = null;
		String strResponse = null;
		String targetUrl = null;
		try {
			targetUrl = getBaseApimintUrl() + url;
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] BEGIN targetUrl=" + targetUrl);

			client = getResteasyClient();
			ResteasyWebTarget targetSrv = client.target(targetUrl);
			addBasicAuthIfNecessary(targetSrv);
			GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(bodyMultipart) { };
			if(HttpMethod.POST.equals(method)) {
				response = targetSrv.request(MediaType.MULTIPART_FORM_DATA_TYPE)
						.headers(getHeaders(uuid))
						.post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
				requestInviata = true;
			} else if(HttpMethod.PUT.equals(method)) {
				response = targetSrv.request(MediaType.MULTIPART_FORM_DATA_TYPE)
						.headers(getHeaders(uuid))
						.put(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
				requestInviata = true;
			}
			if (requestInviata) {
				strResponse = retrieveStrResponseIfPossible(response);
				switch (response.getStatus()) {
					case 200:
					case 201: /* 201 - Used in NOTIFY-MB */
						responseUUID = buildResponseUUID(methodName, uuid, strResponse, clazz);
						break;
					default:
						LOG.debug("[" + CLASS_NAME + "::" + methodName + "] status code" + response.getStatus());
						String message = getResponseMessage(response.getStatus());
						throw buildDAOException(methodName, targetUrl, strResponse, response, message);
				}
			}
			return responseUUID;
		} catch (DAOException e) {
			throw e;
		} catch (Exception e) {
			throw buildDAOException(methodName, targetUrl, strResponse, response);
		} finally {
			if (requestInviata && response!=null)
				response.close();
			if (client!=null && !client.isClosed())
				client.close();
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] END");
		}
	}

	
	/**
	 * PUT su servizio REST
	 * @param <T> Tipologia clazz dell'oggetto di risposta
	 * @param url http del servizio REST da richiamare
	 * @param clazzT dell'oggetto di risposta
	 * @param message   *** TODO DA GENERALIZZARE ***
	 * @return l'oggetto di risposta ApimintResponse
	 * @throws DAOException nel caso di errori
	 */
	protected <T> ResponseUUID<T> putJson(String url, Class<T> clazz, Object body) {
		UUID uuid = UUID.randomUUID();
		return putJson(url, clazz, body, uuid);
	}
	protected <T> ResponseUUID<T> putJson(String url, Class<T> clazz, Object body, UUID uuid) {
		final String methodName = "putJson";
		boolean requestInviata = false;
		Response response = null;
		ResteasyClient client = null;
		ResponseUUID<T> responseUUID = null;
		String strResponse = null;
		String targetUrl = null;
		try {
			targetUrl = getBaseApimintUrl() + url;
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] BEGIN targetUrl=" + targetUrl);

			String bodySnake = null;
			if (isForseNamingStrategy_SNAKE_CASE()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE);			
				bodySnake = mapper.writeValueAsString(body);
			}
			
			client = getResteasyClient();
			ResteasyWebTarget targetSrv = client.target(targetUrl);
			addBasicAuthIfNecessary(targetSrv);
			response = targetSrv.request(MediaType. APPLICATION_JSON)
				.headers(getHeaders(uuid))
				.put(Entity.entity(isForseNamingStrategy_SNAKE_CASE()?bodySnake:body,MediaType. APPLICATION_JSON));
			requestInviata = true;
			strResponse = retrieveStrResponseIfPossible(response);
			switch (response.getStatus()) {
				case 200:
				case 201: /* 201 - Used in NOTIFY-MB */
					responseUUID = buildResponseUUID(methodName, uuid, strResponse, clazz);
					break;
				default:
					LOG.debug("[" + CLASS_NAME + "::" + methodName + "] status code" + response.getStatus());
					String message = getResponseMessage(response.getStatus());
					throw buildDAOException(methodName, targetUrl, strResponse, response, message);
				}
			 return responseUUID;
		} catch (DAOException e) {
			throw e;
		} catch (Exception e) {
			throw buildDAOExceptionAndLogError(methodName, targetUrl, strResponse, response);
		} finally {
			if (requestInviata)
				response.close();
			if (client!=null && !client.isClosed())
				client.close();
			LOG.info("[" + CLASS_NAME + "::" + methodName + "] END");
		}
	}
	
	/**
	 * Generazione dell header alla chiamata dell'API Manager Outer diverse da login
	 * Contiene due attributi di default per API Manager 
	 * - il token ottenuta della login indentificatore di moon
	 * - il uuid della richiesta per tracciamento
	 * 
	 * In seguito sono aggiunti gli headers di businness settati nelle Class di Implementazione
	 * con overwrite della funzione getHeadersExtra()
	 * 
	 * @return mappa completa dei headers
	 * @throws DAOException in caso di mancanza di header obbligatorio
	 */
	protected MultivaluedMap<String, Object> getHeaders(UUID uuid) throws DAOException {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
		if (StringUtils.isEmpty(uuid.toString())) {
			 throw new DAOException("UUID required for X-Request-Id header.");
		}
		headers.add("X-Request-Id", uuid);
		
		// HeadersAggiuntivi di Bussiness
		headers.putAll(getHeadersExtra());
		
		return headers;
	}
	
	protected MultivaluedMap<String, Object> getHeadersExtra() {
		// Implementazione vuota ; to be overwrited to add business headers for specific endpoint
		return new MultivaluedMapImpl<>();
	}

	//
	protected String map2String(MultivaluedMap<String, Object> multivaluedMap) {
		final String methodName = "map2String";
		StringBuilder sb = new StringBuilder();
		try {
		    Iterator<?> it = multivaluedMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<?, ?> pair = (Map.Entry<?, ?>) it.next();
		        sb.append(pair.getKey()).append(": ").append(pair.getValue()).append("\n");
		    }
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::" + methodName + "] ERROR in map2String.");
		}
	    return sb.toString();
	}
	
	//
	protected void setMapStatusCodeMessage(Map<Integer, String> serviceMapStatusCodeMessage) {
		this.serviceMapStatusCodeMessage = serviceMapStatusCodeMessage;
	}
	private Map<Integer, String> getMapStatusCodeMessage() {
		return this.serviceMapStatusCodeMessage;
	}
	protected String getResponseMessage(Integer statusCode) {
		return getMapStatusCodeMessage()==null?null:getMapStatusCodeMessage().get(statusCode);
	}
	
	protected <T> ResponseUUID<T> buildResponseUUID(final String methodName, final UUID uuid, String strResponse, Class<T> clazz) throws DAOException {
		ResponseUUID<T> responseUUID = new ResponseUUID<>();
		responseUUID.setUuid(uuid);
		try {
			LOG.info("[" + CLASS_NAME + "::" + methodName + "-buildResponseUUID] strResponse=" + strResponse);
			if( clazz.isInstance("java.lang.String") ) {
				responseUUID.setResponse((T)strResponse);
			} else {
				LOG.info("[" + CLASS_NAME + "::" + methodName + "-buildResponseUUID] readValue to " + clazz);
				responseUUID.setResponse(getMapper().readValue(strResponse, clazz));
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] 200 ResponseString : " + strResponse + "\n"+ e.getMessage());
			throw new DAOException("Deserializzazione JSON Apimint Response Errore");
		}
		return responseUUID;
	}

	protected String getDigitalIdentity() {
		return digitalIdentity;
	}

	protected void setDigitalIdentity(String digitalIdentity) {
		this.digitalIdentity = digitalIdentity;
	}
}
