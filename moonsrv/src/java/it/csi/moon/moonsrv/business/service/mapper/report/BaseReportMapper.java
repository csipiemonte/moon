/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.report;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.moonsrv.util.JsonPathUtil;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class BaseReportMapper {
	
	
	private static final String CLASS_NAME = "BaseReportMapper";
	protected final static Logger LOG = LoggerAccessor.getLoggerBusiness();

	protected static final char CSV_SEPARATOR = ';';
	protected static final String REPLACE_PATTERN = ";";
	
	protected final DateFormat fDataOra = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	protected final DateFormat fData = new SimpleDateFormat("yyyy-MM-dd");	
	protected final DateFormat fTimeStamp = new SimpleDateFormat("yyyyMMddhhmmss");
	
	protected static JsonNode readData(String data) throws Exception {
		try {
			LOG.debug("[" + CLASS_NAME + "::readData] IN istanza: "+data);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(data, JsonNode.class);
			return result;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::readData] ERROR "+e.getMessage());
		    throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readData] END");
		}
	}
	
	
	
	protected JsonPathUtil jsonPathUtil = null;
	protected String root = null;

	public BaseReportMapper() {		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	private JsonPathUtil getJsonPathUtil(String jsonData) {
		JsonPathUtil<?> jsonPathUtil = new JsonPathUtil(jsonData);
		jsonPathUtil.setDefaultIfNull("");	
		return jsonPathUtil;
	}

	/**
	 * @param jsonPathUtil the jsonPathUtil to set
	 */
	public void setJsonPathUtil(String jsonData) {
		this.jsonPathUtil = this.getJsonPathUtil(jsonData);
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(String root) {
		this.root = root;
	}
	
}
