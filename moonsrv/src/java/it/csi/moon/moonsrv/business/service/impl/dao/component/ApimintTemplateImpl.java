/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ApimintToken;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;

/**
 * Template per i DAO verso API Manager API REST 
 * qui troviamo solo l'aggiunta della BasicAuth con token
 * il core deriva da ApiRestTemplateImpl
 *
 * @author Francesco Zucaro
 * @author Laurent Pissard
 * 
 * @since 1.0.0
 */
public class ApimintTemplateImpl extends ApiRestTemplateImpl {
	
	private static final String CLASS_NAME = "ApimintTemplateImpl";
	protected static final String MSG_ERR_DEFAULT = "ERRORE CHIAMATA API MANAGER INTERNET ";

	/* Parametri di identificazione della componente MOON su API Manager Outer */
	private ApimintToken token;
	/* IP da passare spesso nei headers */
	private String ip;
	
    /**
     * Construtore con inizializzazione dei parametri generici dell'API Manager Outer
     * identificazione censimento dell'applicazioni fruitrici (SP) Moon con OAuth2 grant type client credential, 
     *     con la coppia di di chiavi consumer_key e consuymer_secret 
     *     che servono per recuperare l'access token (in test ha validità di 365 giorni) che servirà per l'invocare l'API.
     */
    public ApimintTemplateImpl() {
    	setEndpoint(EnvProperties.readFromFile(EnvProperties.APIMINT_ENDPOINT));
    }
    
	/* to be overwriten */
    protected boolean useSandbox() {
    	return false;
    }
	protected String getConsumerPrefix() {
		return "";
	}
	protected String getConsumerKey() {
		return EnvProperties.readFromFile(useSandbox()?EnvProperties.APIMINT_SANDBOX_CONSUMERKEY:EnvProperties.APIMINT_PROD_CONSUMERKEY);
//		return EnvProperties.readFromFile((getConsumerPrefix()==null?"":getConsumerPrefix())+EnvProperties.APIMINT_CONSUMERKEY);
	}
    protected String getConsumerSecret() {
		return EnvProperties.readFromFile(useSandbox()?EnvProperties.APIMINT_SANDBOX_CONSUMERSECRET:EnvProperties.APIMINT_PROD_CONSUMERSECRET);
//		return EnvProperties.readFromFile((getConsumerPrefix()==null?"":getConsumerPrefix())+EnvProperties.APIMINT_CONSUMERSECRET);
    }
	protected ApimintToken getToken() throws DAOException {
//		if (token==null || token.getExpires_in() < 30) { // getExpires_in seconds, lo regenro se < 30s
//			LOG.info("[" + CLASS_NAME + "::getToken] Generate new Token getTokenAuthorization()...");
			// TODO OR NOT isValide expireTime
			token = getTokenAuthorization(getConsumerKey(), getConsumerSecret());
//		} else {
//			if(token!=null) {
				LOG.info("[" + CLASS_NAME + "::getToken] Token="+token.getAccess_token()+"  expires_in="+token.getExpires_in());
//				// es. Token [ApimintTemplateImpl::getToken] Token=fd6194ce-87a7-3821-81fb-7e05770bb594  expires_in=13903098
//			}
//		}
		return token;
	}
	
	/**
	 * Methodi per la login del prodotto Moon presso l'API Manager Outer via /token
	 * Con parametri :
	 * - String consumerKey
	 * - String consumerSecret
	 * 
	 * @return Token che deve essere trasmesso alle chiamate successive
	 * @throws DAOException
	 */
	protected ApimintToken getTokenAuthorization(String consumerKey, String consumerSecret) throws DAOException {
		String secret = consumerKey + ":" + consumerSecret;
		String encoded = new String(Base64.encodeBase64(secret.getBytes()));
		boolean requestInviata = false;
		Response response = null;
		ResteasyClient client = null;
		try {
			client = getResteasyClient();
			ResteasyWebTarget target = client.target(getEndpoint() + "/token");
			Form form = new Form();
			form.param("grant_type", "client_credentials");
			Entity<Form> entity = Entity.form(form);
			response = target.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + encoded)
				.post(entity);
			requestInviata = true;
			
			switch (response.getStatus()) {
				case 200:
				case 204:
					token = response.readEntity(ApimintToken.class);
					break;
				default:
					LOG.info("[" + CLASS_NAME + "::getTokenAuthorization] Errore response : " + response.getStatus() + " " + response.getStatusInfo() + "  with secretAuth= " + consumerKey + ":" + consumerSecret);
					throw new DAOException("Errore recupero authorization token " + response.getStatus() + " " + response.getStatusInfo());
			}
			return token;
		} catch (Exception ex) {
			LOG.info("[" + CLASS_NAME + "::getTokenAuthorization] Eccezione : ",ex);
			throw new DAOException("Eccezione recupero authorization token " + ex.getMessage() );
		} finally {
			if (requestInviata)
				response.close();
			if (client!=null && !client.isClosed())
				client.close();
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
		// HEADERS DI DEFAULT DI API MANAGER Apimint
		String accessToken = getToken().getAccess_token();
		if (StringUtils.isEmpty(accessToken)) {
			 throw new DAOException("Access token required for Authorization header.");
		}
		if (StringUtils.isEmpty(uuid.toString())) {
			 throw new DAOException("UUID required for X-Request-Id header.");
		}
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("X-Request-Id", uuid);
		headers.add("X-Forwarded-For", getIP());
		// HeadersAggiuntivi di Bussiness
		headers.putAll(getHeadersExtra());
		
		return headers;
	}

	
	protected Object getIP() {
		return (ip!=null)?ip:retrieveIP();
	}
	private String retrieveIP() {
		String result = "127.0.0.1";
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			result = inetAddress.getHostAddress();
		} catch (UnknownHostException e) {
			LOG.error("[" + CLASS_NAME + "::retrieveIP] UnknownHostException ", e);
		}
		ip = result;
		return result;
	}
	
}
