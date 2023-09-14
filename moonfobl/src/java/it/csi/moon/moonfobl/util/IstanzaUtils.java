/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util;

import java.io.IOException;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Istanza;

public class IstanzaUtils {
	
	private static final String CLASS_NAME = "IstanzaUtils";
	//private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	
	/**
	 * Legge i dati dell istanza in nell oggetto istanzaJsonNode
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	public static JsonNode readIstanzaData(Istanza istanza) throws Exception {
		try {
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			return objectMapper.readValue(istanza.getData().toString(), JsonNode.class);
		} catch (IOException e) {
		    throw e;
		}
	}
	
	/**
	 * modifica host del json dati
	 * 
	 * @param json dati
	 *
	 */
	public static String modificaHost(String jsonData) {
		//LOG.debug("[" + CLASS_NAME + "::modificaHost] IN jsonData"+ jsonData);
		try {			
			String REGEX = "https://[a-zA-Z0-9\\-]+\\.patrim\\.csi\\.it/";
			Pattern pvh = Pattern.compile(REGEX, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			return pvh.matcher(jsonData).replaceAll("/");			
		} catch (Exception e) {			
			//LOG.debug("[" + CLASS_NAME + "::modificaHost] Errore in sostituzione contesto");
			return jsonData;
		}
	}
	
}
