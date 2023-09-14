/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.apipostazione;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.util.ModuloAttributoKeys;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public abstract class BaseApiPostAzione {

	private static final String CLASS_NAME = "BaseApiPostAzione";
	protected static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	protected Istanza istanza;
	protected Long idStoricoWorkflow;	
	protected String codiceAzione;
	protected JsonNode conf;
	protected StrReplaceHelper strReplaceHelper;
	
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	
	public enum ConfAPAEmailKeys {
		TO("to", String.class, "##email##", null, null),
		CC("cc", String.class, null, null, null),
		BCC("bcc", String.class, null, null, null),
		SUBJECT("subject", String.class, null, null, null),
		TEXT("text", String.class, null, null, null),
		HTML("html", String.class, null, null, null), // 1: Richiesta informativa generica  2: Domanda sul servizio applicativo  3: Segnalazione di malfunzionamento
		
		INCLUDE_ISTANZA("attachments.istanza", Boolean.class, null, false, null),
		INCLUDE_ALLEGATI("attachments.allegati", Boolean.class, null, false, null),
		INCLUDE_ALLEGATI_AZIONE("attachments.allegatiAzione", Boolean.class, null, false, null),
		;

		private <T> ConfAPAEmailKeys(String key, Class<T> clazz, String textDefaultValue, Boolean booleanDefaultValue, Integer integerDefaultValue) {
			this.key = key;
			this.textDefaultValue = textDefaultValue;
			this.booleanDefaultValue = booleanDefaultValue;
			this.intDefaultValue = integerDefaultValue;
		}

		private String key;
		private String textDefaultValue;
		private Boolean booleanDefaultValue;
		private Integer intDefaultValue;
		
		public String getKey() {
			return key;
		}
		public String getTextDefaultValue() {
			return textDefaultValue;
		} 
		public boolean getBooleanDefaultValue() {
			return booleanDefaultValue;
		} 
		public Integer getIntDefaultValue() {
			return intDefaultValue;
		} 
	};
	
	public BaseApiPostAzione() {
		super();			
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	// EMAIL
	// APA_EMAIL_CONF {}
	protected JsonNode retrieveAPAEmailConfByAzione(Istanza istanza) throws BusinessException {
		
		String strJson = null;
		try {
			strJson = moduloAttributiDAO.findByNome(istanza.getModulo().getIdModulo(), ModuloAttributoKeys.APA_EMAIL_CONF.name())
				.getValore();
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("[" + CLASS_NAME + "::retrieveAPAEmailConf] APA_EMAIL_CONF non trovata o vuota nei attributi del modulo for idIstanza=" + istanza.getIdIstanza() + "\nmodulo=" + istanza.getModulo());
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::retrieveAPAEmailConf] APA_EMAIL_CONF non trovata o vuota nei attributi del modulo for idIstanza=" + istanza.getIdIstanza() + "\nmodulo=" + istanza.getModulo());
			throw new BusinessException();
		}
		if(StringUtils.isBlank(strJson)) {
				LOG.warn("[" + CLASS_NAME + "::retrieveAPAEmailConf] APA_EMAIL_CONF non trovata o vuota nei attributi del modulo for idIstanza=" + istanza.getIdIstanza() + "\nmodulo=" + istanza.getModulo());
			    //throw new BusinessException("APA_EMAIL_CONF non trovata o vuota nei attributi del modulo","MOONSRV-00901");		    
				return new ObjectMapper().createObjectNode();		
		}
	
		return readConfJsonByAzione(strJson);
	}


	//
	protected String replaceDinamici(String postAzioneValue, ConfAPAEmailKeys confKey) {
		return strReplaceHelper.replaceDinamici(retrieveTextValue(postAzioneValue, confKey), istanza);
	}

	//
	private String retrieveTextValue(String postAzioneValue, ConfAPAEmailKeys keyConf) {
		return StringUtils.isNotBlank(postAzioneValue)?postAzioneValue:
			(conf != null && !conf.isEmpty()) ? (conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asText():keyConf.getTextDefaultValue()): "";
	}
	protected boolean retrieveBooleanValue(Boolean postAzioneValue, ConfAPAEmailKeys keyConf) {
		return postAzioneValue!=null?postAzioneValue:
			//(conf.get(keyConf.getKey())!=null?conf.get(keyConf.getKey()).asBoolean():keyConf.getBooleanDefaultValue());
			getConfBooleanValue(keyConf);
	}
	
	protected boolean getConfBooleanValue(ConfAPAEmailKeys keyConf) {
		boolean ret = false;
		try {
			ret = false;
			if (keyConf.getKey() != null) {
				String[] parts = StringUtils.split(keyConf.getKey(), ".");
				
				if ( conf != null && !conf.isEmpty()) {
					if (parts.length > 1) {
						ret = conf.get(parts[0]).get(parts[1]).asBoolean();
					} else {
						ret = conf.get(keyConf.getKey()).asBoolean();
					}				
				}	
			}
			else {
				ret = keyConf.getBooleanDefaultValue();
			}
		} catch (Exception e) {
			  LOG.error("[" + CLASS_NAME + "::getConfBooleanValue] Exception " + e.getMessage());
			    throw new BusinessException("APA_xxx_CONF non valid JSON","MOONSRV-00902");
		}
		return ret;

	}
		
	private JsonNode readConfJsonByAzione(String strJson) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: " + strJson);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);
			String codiceAzione =  (this.codiceAzione != null && !this.codiceAzione.equals("DEFAULT")) ? this.codiceAzione : "DEFAULT";		
			result = result.get(this.codiceAzione);
					
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readConfJson] IOException " + e.getMessage());
		    throw new BusinessException("APA_xxx_CONF non valid JSON","MOONSRV-00902");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}
	
	private JsonNode readConfJsonByKey(String strJson, String key) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: " + strJson);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);	
			result = result.get(key);
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readConfJson] IOException " + e.getMessage());
		    throw new BusinessException("APA_xxx_CONF non valid JSON","MOONSRV-00902");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}
	
	
	protected JsonNode retrieveAPAEmailConfByKey(Istanza istanza, String key) throws BusinessException {
		String strJson = moduloAttributiDAO.findByNome(istanza.getModulo().getIdModulo(), ModuloAttributoKeys.APA_EMAIL_CONF.name())
			.getValore();
		if(StringUtils.isBlank(strJson)) {
			LOG.error("[" + CLASS_NAME + "::retrieveAPAEmailConf] APA_EMAIL_CONF non trovata o vuota nei attributi del modulo for idIstanza=" + istanza.getIdIstanza() + "\nmodulo=" + istanza.getModulo());
		    throw new BusinessException("APA_EMAIL_CONF non trovata o vuota nei attributi del modulo","MOONSRV-00901");
		}
		return readConfJsonByKey(strJson, key);
	}

	
	


}
