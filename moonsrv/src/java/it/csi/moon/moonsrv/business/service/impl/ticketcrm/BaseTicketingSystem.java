/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.ticketcrm;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ModuloAttributo;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public abstract class BaseTicketingSystem {

	private static final String CLASS_NAME = "BaseTicketingSystem";
	protected static final Logger LOG = LoggerAccessor.getLoggerBusiness();
		
	public BaseTicketingSystem() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public TicketingSystemParams readParams() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	
	//
	//
	protected JsonNode retrieveConf(Istanza istanza) throws BusinessException {
		String strJson = findValueCRMConfInAttributi(istanza.getModulo().getAttributi());
		if(StringUtils.isBlank(strJson)) {
			LOG.error("[" + CLASS_NAME + "::retrieveConf] PSIT_CRM_CONF non trovata nei attributi del modulo for idIstanza=" + istanza.getIdIstanza() + "\nmodulo=" + istanza.getModulo());
		    throw new BusinessException("PSIT_CRM_CONF non trovata nei attributi del modulo","MOONSRV-30700");
		}
		return readConfJson(strJson);
	}

	private String findValueCRMConfInAttributi(List<ModuloAttributo> attributi) {
		return attributi.stream()
			.filter(a -> getModuloAttributoKeysName().equals(a.getNome()))
			.map(a -> a.getValore())
			.findAny()
			.orElse(null);
	}

	protected String getModuloAttributoKeysName() {
		return ModuloAttributoKeys.PSIT_CRM_CONF_NEXTCRM.name();
	}

	private JsonNode readConfJson(String strJson) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: " + strJson);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readConfJson] IOException " + e.getMessage());
		    throw new BusinessException("PSIT_CRM_CONF non valid JSON","MOONSRV-30701");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}

}
