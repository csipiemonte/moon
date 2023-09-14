/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper.report;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.util.TokenBuffer;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.util.LoggerAccessor;



public class BaseReportIstanzaMapper {
	
	private final static String CLASS_NAME = "PrintIstanzaMapperFactory";
	private static Logger log = LoggerAccessor.getLoggerBusiness();
	
	public BaseReportIstanzaMapper () {
		//must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	/**
	 * Legge i dati dell istanza in nell oggetto istanzaJsonNode
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	protected static JsonNode readIstanzaData(String datiIstanza) throws Exception {
		try {
			log.debug("[" + CLASS_NAME + "::readIstanzaData] avvio readIstanzaData ");
			
//			JsonFactory jfactory = new JsonFactory();
//			JsonParser jParser = jfactory.createJsonParser(datiIstanza);
//			TokenBuffer buffer = new TokenBuffer(jParser.getCodec());
			

			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			//JsonNode result = objectMapper.readValue(datiIstanza, JsonNode.class);
			
			JsonNode result = objectMapper.readTree(datiIstanza);
			
			//JsonNode result = objectMapper.readTree(buffer.asParser());
			
			return result;
		} catch (IOException e) {
		    e.printStackTrace();
		    log.error("[" + CLASS_NAME + "::readIstanzaData] ERROR "+e.getMessage());
		    throw e;
		} finally {
			log.debug("[" + CLASS_NAME + "::readIstanzaData] END");
		}
	}
	
}
