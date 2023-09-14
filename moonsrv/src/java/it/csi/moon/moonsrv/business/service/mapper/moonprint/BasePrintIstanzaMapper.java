/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.moonprint;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class BasePrintIstanzaMapper {
	
	private static final String CLASS_NAME = "PrintIstanzaMapperFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	/**
	 * Legge i dati dell istanza in nell oggetto istanzaJsonNode
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	protected static JsonNode readIstanzaData(Istanza istanza) throws Exception {
		try {
			LOG.debug("[" + CLASS_NAME + "::readIstanzaData] IN istanza: "+istanza);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(istanza.getData().toString(), JsonNode.class);
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readIstanzaData] ERROR "+e.getMessage());
		    throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readIstanzaData] END");
		}
	}
	

	protected void printAllegato(MoonprintDocumentWriter localWriter, JsonNode data, String keyNameFile) {
		printAllegato(localWriter, data, keyNameFile, "Allegato");
	}
	protected void printAllegato(MoonprintDocumentWriter localWriter, JsonNode data, String keyNameFile, String label) {
		if (data.has(keyNameFile) && data.get(keyNameFile).size()>0) {
			JsonNode allegato1 = data.get(keyNameFile).get(0);
			if (allegato1.has("originalName")) {
				localWriter.addItem(label, allegato1.get("originalName").asText());
			}
		} else {
			localWriter.addItem(label, "Non presente");
		}
	}
}
