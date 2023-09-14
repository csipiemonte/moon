/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.apimint.nextcrm.v1.dto.NewTicket;
import it.csi.apimint.nextcrm.v1.dto.Ticket;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimintTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.NextcrmApimintDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Qualifier("apimint")
@Component
public class NextcrmApimintDAOImpl extends ApimintTemplateImpl implements NextcrmApimintDAO {

	private static final String CLASS_NAME = "NextcrmApimintDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	private static final Map<Integer,String> CREA_TICKET_MAP_STATUS_CODE_MESSAGE;
	static {
        Map<Integer, String> map = new HashMap<>();
        map.put(201, "201 - Nessun soggetto reperito per i parametri di ricerca impostati");
        map.put(400, "401 - UnauthorizedError - Access token mancante o non valido");
        map.put(401, "401 - UnauthorizedError - Access token mancante o non valido");
        map.put(404, "404 - NotFound - La risorsa specificata non è stata trovata");
        map.put(422, "404 - NotFound - La risorsa specificata non è stata trovata");
        map.put(500, "500 - InternalServerError - Si è verificato un'errore interno del server");
        map.put(502, "502 - BadGatewayError - Errore generato dai servizi di inner");
        CREA_TICKET_MAP_STATUS_CODE_MESSAGE = Collections.unmodifiableMap(map);
    }

    public NextcrmApimintDAOImpl() {
    	super();
    	setEndpoint(EnvProperties.readFromFile(EnvProperties.APIMINT_ENDPOINT));
    }
	
	protected String getPathExtra() {
		return "/tecno/nextcrm/v1";
	}
    
	public Ticket creaTicket(NewTicket newTicket) throws DAOException {
		final String methodName = "creaTicket";
		long start = System.currentTimeMillis();
		Ticket result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
			String url = "/tickets";
			ResponseUUID<Ticket> response = postJson(url, Ticket.class, newTicket);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = response.getResponse();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio creaTicket", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start);
			LOG.debug("[" + CLASS_NAME + "::creaTicket] END result=" + result);
            LOG.info("[" + CLASS_NAME + "::creaTicket] END in " + sec + " milliseconds.");
		}
		return result;
	}
	   
}

