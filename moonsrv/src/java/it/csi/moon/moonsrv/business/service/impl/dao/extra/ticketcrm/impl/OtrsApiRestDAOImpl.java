/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.impl;

import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.apirest.otrs.v1.dto.Error;
import it.csi.apirest.otrs.v1.dto.TicketRequest;
import it.csi.apirest.otrs.v1.dto.TicketResponse;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApiRestTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.OtrsApiRestDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;

@Component
public class OtrsApiRestDAOImpl extends ApiRestTemplateImpl implements OtrsApiRestDAO{

	private static final String CLASS_NAME = "OtrsApiRestDAOImpl";
	private static final String TS_OTRS_USERNAME = EnvProperties.readFromFile(EnvProperties.TS_OTRS_USERNAME);
	private static final String TS_OTRS_PASSWORD = EnvProperties.readFromFile(EnvProperties.TS_OTRS_PASSWORD);
	
	public OtrsApiRestDAOImpl() {
    	super();
    	setEndpoint(EnvProperties.readFromFile(EnvProperties.TS_OTRS_ENDPOINT));
    	setFAIL_ON_UNKNOWN_PROPERTIES(false);
    }
	
	private static String getLoginParams() {
		String result;
		result = "?UserLogin="+TS_OTRS_USERNAME
				+"&Password="+TS_OTRS_PASSWORD;
		return result;
	}
	@Override
	protected String getUsername() {
		return TS_OTRS_USERNAME;
	}
	@Override
	protected String getPassword() {
		return TS_OTRS_PASSWORD;
	}

	@Override
	public TicketResponse createTicket(TicketRequest request) {
		long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::createTicket] BEGIN " + request);        	
        	String url = "/ticket"+getLoginParams();
        	ResponseUUID<TicketResponse> response = postJson(url, TicketResponse.class, request);
        	LOG.debug("[" + CLASS_NAME + "::createTicket] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::createTicket] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::createTicket] Errore generico servizio", e);
			throw new DAOException("Errore generico servizio");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info(CLASS_NAME +".createTicket() in " + sec + " milliseconds.");
            LOG.debug("[" + CLASS_NAME + "::createTicket] END");
        }
	}
	
	
	
	//
	//non e' un servizio rest
	@Override
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
					responseUUID = (ResponseUUID<T>) buildResponseUUID(methodName, uuid, strResponse);
					break;
				default:
					LOG.debug("[" + CLASS_NAME + "::" + methodName + "] status code " + response.getStatus() + "\nREQUEST: " + ((bodySnake!=null)?bodySnake:body) + "\nRESPONSE: " + strResponse);
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

	private ResponseUUID<TicketResponse> buildResponseUUID(final String methodName, final UUID uuid, String strResponse) throws DAOException {
		ResponseUUID<TicketResponse> responseUUID = new ResponseUUID();
		responseUUID.setUuid(uuid);
		TicketResponse tr = new TicketResponse();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode json = objectMapper.readValue(strResponse, JsonNode.class);
			if(json.has("Error")) {
				JsonNode errorJson = json.get("Error");
				Error e = new Error();
				e.setErrorCode(errorJson.get("ErrorCode").asText());
				e.setErrorMessage(errorJson.get("ErrorMessage").asText());
				tr.setError(e);
			} else {
				String ticketID = json.get("TicketID").asText();
				String ticketNumber = json.get("TicketNumber").asText();
				tr.setTicketID(ticketID);
				tr.setTicketNumber(ticketNumber);
			}
			responseUUID.setResponse(tr);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] 200 ResponseString : " + strResponse + "\n"+ e.getMessage());
			throw new DAOException("Deserializzazione JSON Apimint Response Errore");
		}
		return responseUUID;
	}
}
