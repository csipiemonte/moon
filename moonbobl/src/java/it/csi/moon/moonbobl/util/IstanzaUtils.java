/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
//import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;


public class IstanzaUtils {
	
	private final static String CLASS_NAME = "IstanzaUtils";
	//private final static Logger log = LoggerAccessor.getLoggerBusiness();
	
	/**
	 * Legge i dati dell istanza in nell oggetto istanzaJsonNode
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	public static JsonNode readIstanzaData(Istanza istanza) throws Exception {
		try {
//			log.debug("[" + CLASS_NAME + "::readIstanzaData] IN istanza: "+istanza);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			JsonNode result = objectMapper.readValue(istanza.getData().toString(), JsonNode.class);
			
			return result;
		} catch (IOException e) {
		    e.printStackTrace();
//		    log.error("[" + CLASS_NAME + "::readIstanzaData] ERROR "+e.getMessage());
		    throw e;
		} finally {
//			log.debug("[" + CLASS_NAME + "::readIstanzaData] END");
		}
	}
	
	/**
	 * modifica il contesto del json dati
	 * 
	 * @param json dati
	 *
	 */
	
	public static String modificaContestoBoToFo(String jsonData) {

		//log.debug("[" + CLASS_NAME + "::modificaContestoBoToFo] IN jsonData"+ jsonData);
		try {
			Pattern p = Pattern.compile("/moonbobl/", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			String result = p.matcher(jsonData).replaceAll("/moonfobl/");		
			return result;
		} catch (Exception e) {			
			//log.debug("[" + CLASS_NAME + "::modificaContestoBoToFo] Errore in sostituzione contesto");
			return jsonData;
		}
	}
	
	public static String modificaContestoFoToBo(String jsonData) {

		//log.debug("[" + CLASS_NAME + "::modificaContestoFoToBo] IN jsonData"+ jsonData);
		try {
			Pattern p = Pattern.compile("/moonfobl/", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			String result = p.matcher(jsonData).replaceAll("/moonbobl/");
			
			Pattern pm = Pattern.compile("/modulistica/", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			result = pm.matcher(result).replaceAll("/moonbobl/");	
			
			String REGEX = "https://[a-zA-Z0-9\\-]+\\.patrim\\.csi\\.it/";
			Pattern pvh = Pattern.compile(REGEX, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			result = pvh.matcher(result).replaceAll("/");	
			
			String REGEX2 = "https://[a-zA-Z0-9\\-]+\\.csi\\.it/";
			Pattern pvh2 = Pattern.compile(REGEX2, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			result = pvh2.matcher(result).replaceAll("/");
						
			return result;
		} catch (Exception e) {			
			//log.debug("[" + CLASS_NAME + "::modificaContestoFoToBo] Errore in sostituzione contesto");
			return jsonData;
		}
	}
	
}
