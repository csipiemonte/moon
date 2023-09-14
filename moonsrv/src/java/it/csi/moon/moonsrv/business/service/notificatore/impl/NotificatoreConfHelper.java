/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.notificatore.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.apirest.notify.preferences.v1.dto.UserPreferencesService;
import it.csi.moon.commons.entity.NotifyParametroEntity;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class NotificatoreConfHelper {
	
	private static final String CLASS_NAME = "NotificatoreConfHelper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String NTF_PARAM_NAME_TOKEN = "TOKEN";
	private static final String NTF_PARAM_NAME_URL = "URL";

	public static final String REQUIRED_LEVEL_PORTALE = "PORTALE";
	public static final String REQUIRED_LEVEL_SERVIZIO = "SERVIZIO";
	private static final Set<String> REQUIRED_LEVELS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(REQUIRED_LEVEL_PORTALE,REQUIRED_LEVEL_SERVIZIO)));

	protected JsonNode confJson = null;
	
	private List<NotifyParametroEntity> listParametri = null;

	
	public NotificatoreConfHelper(String conf) {
		this.confJson = readConfJson(conf);
	}
	public NotificatoreConfHelper(String conf, List<NotifyParametroEntity> lp) {
		this.confJson = readConfJson(conf);
		this.setListParametri(lp);
	}
	public JsonNode getConfJson() {
		return confJson;
	}
	private JsonNode readConfJson(String strJson) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: "+strJson);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readConfJson] ERROR "+e.getMessage());
		    throw new BusinessException();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}
	
	public boolean isINITRequiredWhen() throws BusinessException {
		return "INIT".equalsIgnoreCase(readRequiredKey("requiredWhen"));
	}
	public String readRequiredKey(String key) throws BusinessException {
		String result = null;
		if (confJson.has(key)) {
			result = confJson.get(key).asText().trim();
		}
		if (StringUtils.isEmpty(result)) {
	    	LOG.error("[" + CLASS_NAME + "::readRequiredKey] " + key + " required in conf.");
	    	throw buildBusinessException(key + " required in conf", "MOONSRV-30411");
		}
		return result;
	}

	public String readRequiredLevel() throws BusinessException {
		String result = null;
		if (confJson.has("requiredLevel")) {
			result = confJson.get("requiredLevel").asText().trim();
		}
		if (StringUtils.isEmpty(result)) {
	    	LOG.error("[" + CLASS_NAME + "::readRequiredLevel] requiredLevel required in conf.");
	    	throw buildBusinessException("requiredLevel required in conf", "MOONSRV-30412");
		}
		if (!REQUIRED_LEVELS.contains(result)) {
	    	LOG.error("[" + CLASS_NAME + "::readRequiredLevel] requiredLevel not validate value, expected PORTALE|SERVIZIO. Actual value: " + result);
	    	throw buildBusinessException("requiredLevel not validate", "MOONSRV-30413");
		}
		return result;
	}

	public void validaPrefRequired(UserPreferencesService serviceSubscribed) {
		List<String> channelsNotSubscribed = new ArrayList<>();
		validaPrefRequiredChannel(serviceSubscribed, "email", channelsNotSubscribed);
		validaPrefRequiredChannel(serviceSubscribed, "sms", channelsNotSubscribed);
//		validaPrefRequiredChannel(serviceSubscribed, "push", channelsNotSubscribed);
//		validaPrefRequiredChannel(serviceSubscribed, "mex", channelsNotSubscribed);
//		validaPrefRequiredChannel(serviceSubscribed, "io", channelsNotSubscribed);
		if (channelsNotSubscribed.size()>0) {
			LOG.error("[" + CLASS_NAME + "::validaPrefRequired] Attenzione! E' necessario sottoscrivere al servizio le preferenze : " + channelsNotSubscribed.toString());						
			String defaultMex = "Attenzione! E' necessario sottoscrivere al servizio le preferenze : " + channelsNotSubscribed.toString();
			throw buildBusinessException(defaultMex,"MOONSRV-30416");
		}
	}
	private BusinessException buildBusinessException(String defaultMex,String code)  {
		String mex = getMexErroreByCode(code);
		if(mex ==null || mex.isBlank()) {
			mex = defaultMex;
		}
		BusinessException ex = new BusinessException(mex, code);
		return ex;
	}

	public void validaPrefRequiredChannel(UserPreferencesService serviceSubscribed, String channel, List<String> channelsNotSubscribed) {
		if (hasPrefRequiredForChannel(channel, confJson) && !isSubscribedOnChannel(channel, serviceSubscribed)) {
			channelsNotSubscribed.add(channel);
		}
	}
	private boolean hasPrefRequiredForChannel(String channel, JsonNode confJson) throws BusinessException {
		boolean result = false;
		if (confJson.has(channel) && confJson.get(channel).has("prefRequired")) {
			result = confJson.get(channel).get("prefRequired").asBoolean();
		}
		return result;
	}
	private boolean isSubscribedOnChannel(String channel, UserPreferencesService serviceSubscribed) {
		if (serviceSubscribed.getChannels()==null) {
			return false;
		}
		return serviceSubscribed.getChannels().contains(channel);
	}

	public JsonNode readJsonNode(String channel) {
		JsonNode result = null;
		if (confJson.has(channel)) {
			result = confJson.get(channel);
		}
		return result;
	}
	public List<NotifyParametroEntity> getListParametri() {
		return listParametri;
	}
	public void setListParametri(List<NotifyParametroEntity> listParametri) {
		this.listParametri = listParametri;
	}
	
	public String getEndpoint() throws BusinessException {
		return retrieveParametroRequired(NTF_PARAM_NAME_URL);
	}
	public String getTokenMoon() throws BusinessException {
		return retrieveParametroRequired(NTF_PARAM_NAME_TOKEN);
	}
	/**
	 * Recupera il valore di un parametro di cui il valore è obbligatorio (notBlank)
	 * @param key
	 * @return valoreParametro valorizzed
	 */
	protected String retrieveParametroRequired(String key) throws BusinessException {
		String result = retrieveParametroOrElseThrow(key);
		if (StringUtils.isBlank(result)) {
			throw new BusinessException();
		}
		return result;
	}
	/**
	 * Recupera il valore di un parametro obbligatorio (key obbligatorio)
	 * @param key
	 * @return valoreParametro (valorizzed, blank, null)
	 */
	protected String retrieveParametroOrElseThrow(String key) throws BusinessException {
		String result = null;
		if(!this.listParametri.isEmpty()) {
			NotifyParametroEntity param = listParametri.stream().filter(x -> x.getNomeAttributo().equals(key)).findFirst().orElseThrow(BusinessException::new);
			result = param.getValore();
		}
		return result;
	}
	
	public boolean isSendEnable() {
		if(readJsonNode("email") != null && readJsonNode("email").get("send").asBoolean()) {
			return true;
		}
		if(readJsonNode("sms") != null && readJsonNode("sms").get("send").asBoolean()) {
			return true;
		}
		return false;
	}
	
	public String getMexErroreByCode(String code) {
		String result = null;
		if (confJson.has("errori") && confJson.get("errori").has(code)) {
			result = confJson.get("errori").get(code).asText();
		}
		return result;
	}
	
	public String readKey(String key) throws BusinessException {
		String result = null;
		if (confJson.has(key)) {
			result = confJson.get(key).asText().trim();
			result = result.isEmpty() ? null : result;
		}	
		return result;
	}

}
