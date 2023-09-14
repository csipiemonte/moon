/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * EstraiDichiaranteHelper
 * Di default i dati del dichiarante provengono da userInfo e gia valorizzati.
 * Nel caso di compilazione compila_bo i dati vengono estratti da istanza
 * 
 * PSIT_EXTRAI_DICHIARANTE
 * {
 *  "nome_dichiarante_data_key": "richiedente.nome",
 *  "cognome_dichiarante_data_key": "richiedente.cognome",
 *  "codice_fiscale_dichiarante_data_key": "richiedente.codiceFiscale"
 * }
 * 
 * @author laurent
 * 
 */
public class EstraiDichiaranteHelper{

	private static final String CLASS_NAME = "EstraiDichiaranteTask";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	private Istanza istanzaSaved;
	private String confExtractDichiarante;
	private JsonNode conf = null;
	private DatiIstanzaHelper datiIstanzaHelper;
	
	@Autowired
	IstanzaDAO istanzaDAO;

	public EstraiDichiaranteHelper(UserInfo user, Istanza istanzaSaved, String confExtractDichiarante) {
		super();
//		this.user = user;
		this.istanzaSaved = istanzaSaved;
		this.confExtractDichiarante = confExtractDichiarante;
		log.debug("[" + CLASS_NAME + "::EstraiDichiaranteTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): " + istanzaSaved.getIdIstanza());

//		// must provide autowiring support to inject SpringBean
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * Aggiornamento dei dati del dichiarante
	 */
	public String updateDichiarante() throws BusinessException {
		try {
			String result = null;
			log.debug("[" + CLASS_NAME + "::updateDichiarante]");

	        // 1. Init JsonNode di configurazione e dataIstanza
			if (StringUtils.isBlank(confExtractDichiarante)) {
				return result;
			}
	        getConf();
	        log.debug("[" + CLASS_NAME + "::calupdateDichiarantel] getConf() OK.");
	        
			// 2. Aggiorna istanza
			int i = updateDichiaranteIstanza(istanzaSaved.getIdIstanza(), retrieveCodiceFiscaleDichiarante(), retrieveCognomeDichiarante(), retrieveNomeDichiarante());
			result = (i==1)?"Dichiarante istanza aggiornato.":"Errore aggiornamento dichiarante.";

			log.debug("[" + CLASS_NAME + "::updateDichiarante] END result=" + result);
			return result;
		} catch (BusinessException be) {
			log.warn("[" + CLASS_NAME + "::updateDichiarante] BusinessException");
			throw be;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateDichiarante] Exception ", e);
			throw new BusinessException();
		}
	}

	private String retrieveCodiceFiscaleDichiarante() throws BusinessException {
    	log.debug("[" + CLASS_NAME + "::retrieveCodiceFiscaleDichiarante] BEGIN");
		String result = null;
		String key = conf.get("codice_fiscale_dichiarante_data_key")!=null?conf.get("codice_fiscale_dichiarante_data_key").getTextValue():null;
		log.debug("[" + CLASS_NAME + "::retrieveCodiceFiscaleDichiarante] Using codice_fiscale_dichiarante_data_key (if not null): " + key);
		if (StringUtils.isNotEmpty(key)) {
			result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
		}
    	log.debug("[" + CLASS_NAME + "::retrieveCodiceFiscaleDichiarante] END " + result);
		return result==null?null:result.toUpperCase().trim();
	}
	private String retrieveCognomeDichiarante() throws BusinessException {
    	log.debug("[" + CLASS_NAME + "::retrieveCognomeDichiarante] BEGIN");
		String result = null;
		String key = conf.get("cognome_dichiarante_data_key")!=null?conf.get("cognome_dichiarante_data_key").getTextValue():null;
		log.debug("[" + CLASS_NAME + "::retrieveCognomeDichiarante] Using cognome_dichiarante_data_key (if not null): " + key);
		if (StringUtils.isNotEmpty(key)) {
			result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
		}
    	log.debug("[" + CLASS_NAME + "::retrieveCognomeDichiarante] END " + result);
		return result==null?null:result.toUpperCase().trim();
	}
	private String retrieveNomeDichiarante() throws BusinessException {
    	log.debug("[" + CLASS_NAME + "::retrieveNomeDichiarante] BEGIN");
		String result = null;
		String key = conf.get("nome_dichiarante_data_key")!=null?conf.get("nome_dichiarante_data_key").getTextValue():null;
		log.debug("[" + CLASS_NAME + "::retrieveNomeDichiarante] Using nome_dichiarante_data_key (if not null): " + key);
		if (StringUtils.isNotEmpty(key)) {
			result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
		}
    	log.debug("[" + CLASS_NAME + "::retrieveNomeDichiarante] END " + result);
		return result==null?null:result.toUpperCase().trim();
	}
	
	private int updateDichiaranteIstanza(Long idIstanza, String codiceFiscale, String cognome, String nome) throws DAOException {
		int result = 0;
		result = istanzaDAO.updateDichiarante(idIstanza, codiceFiscale, cognome, nome);
		return result;
	}

	private Istanza getIstanzaSaved() {
		return istanzaSaved;
	}
	
	//
	//
	private JsonNode getConf() throws Exception {
		if (conf==null) {
			conf = readConfJson(confExtractDichiarante);
		}
		return conf;
	}

	private JsonNode readConfJson(String strJson) throws Exception {
		try {
			if(log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: "+strJson);
			}
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);

			return result;
		} catch (IOException e) {
		    log.error("[" + CLASS_NAME + "::readConfJson] ERROR "+e.getMessage());
		    throw e;
		} finally {
			log.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}


	private DatiIstanzaHelper getDatiIstanzaHelper() {
		if (datiIstanzaHelper==null) {
			datiIstanzaHelper = new DatiIstanzaHelper();
			datiIstanzaHelper.initDataNode(getIstanzaSaved());
		}
		return datiIstanzaHelper;
	}
	
}
