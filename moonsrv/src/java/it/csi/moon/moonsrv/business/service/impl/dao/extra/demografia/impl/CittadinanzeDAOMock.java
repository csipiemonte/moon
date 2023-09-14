/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.impl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.extra.demografia.Cittadinanza;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.CittadinanzeDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;


/**
 *
 */
@Component
@Qualifier("mock")
public class CittadinanzeDAOMock implements CittadinanzeDAO {
	
	private static final String CLASS_NAME = "CittadinanzeDAOMock";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();

	/**
	 *
	 * @return
	 */
	public List<Cittadinanza> findAll() throws DAOException {
		return findAll(0,0);
	}
	public List<Cittadinanza> findAll(int limit, int skip) throws DAOException {
		
		assert limit>=0 : "Limit is negative !";
		assert skip>=0 : "Skip is negative !";
		
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/dem-nao-cittadinanze.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<Cittadinanza>elenco = new ArrayList<>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			ObjectMapper objectMapper = new ObjectMapper();
//			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode json = objectMapper.readValue(sb.toString(), JsonNode.class);
			JsonNode jsonArray = json.get("cittadinanze");
			Iterator<JsonNode> iter = jsonArray.elements();
			int i = 0;
			while (iter.hasNext() && (limit==0 || elenco.size()<limit)) { 
				JsonNode jsonObj = iter.next();
				if (jsonObj != null && (skip==0 || i<skip)) {
					Cittadinanza obj = new Cittadinanza(jsonObj.get("id_nazione").asInt(), jsonObj.get("desc_cittadinanza_maschile").asText(), jsonObj.get("flag_ue").asText());    
					elenco.add(obj);
				}
			     i++;
			}
			return elenco;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::findAll] IOException ", e);
			throw new DAOException(" IOException "+ e.getMessage());
		}
	}
	

	/**
	 * 
	 * @param codice
	 * @return
	 */
	public Cittadinanza findByPK(Integer codice) throws DAOException {
		for (Cittadinanza cittd : findAll()) {
			if (cittd.getCodice().equals(codice)) {
				return cittd;
			}
		}
		return null;
	}


	public Cittadinanza findByDesc(String descMaschile) throws DAOException {
		for (Cittadinanza cittd : findAll()) {
			if (cittd.getNome().equals(descMaschile)) {
				return cittd;
			}
		}
		return null;
	}
	
}
