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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.extra.demografia.Nazione;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.AnprNazioneDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;


/**
 *
 */
@Component
@Qualifier("mock")
public class AnprNazioneDAOMock implements AnprNazioneDAO {
	
	private static final String CLASS_NAME = "AnprNazioneDAOMock";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();

	/**
	 *
	 * @return
	 */
	public ArrayList<Nazione> findAll() throws DAOException {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/dem-anpr-stati_esteri.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    ArrayList<Nazione>elenco = new ArrayList<>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			ObjectMapper objectMapper = new ObjectMapper();
//			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode json = objectMapper.readValue(sb.toString(), JsonNode.class);
			JsonNode jsonArray = json.get("stati_esteri");
			Iterator<JsonNode> iter = jsonArray.elements();
			while (iter.hasNext()) { 
				JsonNode jsonObj = iter.next();
			    if (jsonObj != null) {
			    	Nazione obj = new Nazione(jsonObj.get("cod_istat").asInt(), jsonObj.get("denominazione").asText());
			        elenco.add(obj);
			    }
			}
			return elenco;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::findAll] IOException ", e);
			throw new DAOException(" IOException ");
		}
	}


	/**
	 * 
	 * @param codice
	 * @return
	 */
	public Nazione findByPK(Integer codice) throws DAOException {
		for (Nazione naz : findAll()) {
			if (naz.getCodice().equals(codice)) {
				return naz;
			}
		}
		return null;
	}



	@Override
	public Nazione findByNome(String nomeNazione) throws DAOException {
		for (Nazione naz : findAll()) {
			if (naz.getNome().equals(nomeNazione)) {
				return naz;
			}
		}
		return null;
	}
	

}
