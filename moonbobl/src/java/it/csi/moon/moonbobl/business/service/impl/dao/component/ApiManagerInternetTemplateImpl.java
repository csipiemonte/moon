/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.component;

import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.ApimintResponse;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.Message;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.Token;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;

import  java.util.UUID;

/**
 * Template per i DAO verso API Manager API REST (con BasicAuth ?)
 *
 * @author Laurent Pissard
 *
 */
public class ApiManagerInternetTemplateImpl {
	
	
	private ResteasyClient client;
	private final static String CLASS_NAME = "ApiManagerInternetTemplateImpl";
	
	protected Logger log = LoggerAccessor.getLoggerBusiness();
	
	protected final static String MSG_ERR_DEFAULT = "ERRORE CHIAMATA API MANAGER INTERNET ";
	
	protected static String getEndpoint() {
		return Constants.getWsEndpointParams(Constants.APIMINT_ENDPOINT);
	}
	protected static String getAuthUsr() {
		return Constants.getWsEndpointParams(Constants.APIMINT_USERNAME);
	}
	protected static String getAuthPwd() {
		return Constants.getWsEndpointParams(Constants.APIMINT_PASSWORD);
	}
	
	/*
	 * Can be overwriten 
	 * sample :
	 *  /extra/territorio/toponomastica/vie
	 */
	protected String getPathExtra() {
		return "";
	}
	
	
	protected Token getTokenAuthorization(String consumerKey, String consumerSecret) throws DAOException {
		String secret = consumerKey + ":" + consumerSecret;
		String encoded = new String(Base64.encodeBase64(secret.getBytes()));
		boolean requestInviata = false;
		Response response = null;
		Token token = null;
		try {
			log.info("[" + CLASS_NAME + "::getTokenAuthorization] BEGIN");
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget target = client.target(getEndpoint() + "/token");
			Form form = new Form();
			form.param("grant_type", "client_credentials");
			Entity<Form> entity = Entity.form(form);
			response = target.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + encoded)
					.post(entity);
			requestInviata = true;
			
			switch (response.getStatus()) {
			case 200:
				token = response.readEntity(Token.class);
				break;
				
			default:
				log.info("[" + CLASS_NAME + "::getTokenAuthorization] Errore response : " + response.getStatus() + " " + response.getStatusInfo());
				throw new DAOException("Errore recupero authorization token " + response.getStatus() + " " + response.getStatusInfo());
			}
			return token;
		} catch (Exception ex) {
			log.info("[" + CLASS_NAME + "::getTokenAuthorization] Eccezione : ",ex);
			throw new DAOException("Eccezione recupero authorization token " + ex.getMessage() );
		} finally {
			if (requestInviata)
				response.close();
			if (!client.isClosed())
				client.close();
			log.info("[" + CLASS_NAME + "::getTokenAuthorization] END");
		}

	}
	
	
	/* 
	 *  Invoca Servizio su APIMNINT e rotorna la risposta come Stringa
	 *  
	 */
	public ApimintResponse invokeApiMintService( String endpoint, String accessToken,
			String userToken, String clientProfile, Message body ) throws  DAOException {
		
		boolean requestInviata = false;
		Response risposta = null;
		ApimintResponse apimintResponse = null;
		try {
			log.info("[" + CLASS_NAME + "::invokeApiMintService] BEGIN");
			client = new ResteasyClientBuilder().build();
			ResteasyWebTarget targetSrv = client.target(endpoint);
			UUID uuid = UUID.randomUUID();
			Builder builder = targetSrv.request(MediaType.APPLICATION_JSON_TYPE);
			builder.accept(MediaType.APPLICATION_JSON);
			risposta =  builder
						.header("Authorization", "Bearer " + accessToken)
						.header("client-profile",clientProfile)
						.header("user-token", userToken)
						.header("X-Request-Id", uuid)
						.post(Entity.json(body));		        
		        
			 requestInviata = true;
			 switch (risposta.getStatus()) {
				case 200:
					apimintResponse = new ApimintResponse();
					apimintResponse.setUuid(uuid);
					apimintResponse.setRispostaRequest(risposta.readEntity(String.class));
					break;					
				default:
					log.info("[" + CLASS_NAME + "::invokeApiMintService] Errore response : " + risposta.getStatus() + " " + risposta.getStatusInfo());
					throw new DAOException("Errore invokeApiMintService " + risposta.getStatus() + " " + risposta.getStatusInfo());
				}
			 return apimintResponse;
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			log.info("[" + CLASS_NAME + "::invokeApiMintService] END");
		}
		
		return null;
		
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
