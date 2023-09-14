/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.helper.task;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.moonsrv.business.service.helper.DatiIstanzaHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * PostSaveInstanzaTask EstraiDichiaranteTask
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
public class EstraiDichiaranteTask implements Callable<String> {

	private static final String CLASS_NAME = "EstraiDichiaranteTask";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private Istanza istanzaSaved;
	private String confExtractDichiarante;
	private JsonNode conf = null;
	private DatiIstanzaHelper datiIstanzaHelper;
	
	@Autowired
	IstanzaDAO istanzaDAO;

	public EstraiDichiaranteTask(Istanza istanzaSaved, String confExtractDichiarante) {
		super();
		this.istanzaSaved = istanzaSaved;
		this.confExtractDichiarante = confExtractDichiarante;
		LOG.debug("[" + CLASS_NAME + "::EstraiDichiaranteTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): " + istanzaSaved.getIdIstanza());

		// must provide autowiring support to inject SpringBean
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * Associa l'istanza ai suoi allegati : valorizza
	 * moon_fo_t_allegati_istanza.id_istanza per tutti allegati associati
	 * all'istanza in stato COMPLETATA
	 */
	public String call() throws BusinessException {
		try {
			String result = null;
			LOG.debug("[" + CLASS_NAME + "::call] BEGIN Task...");

	        // 1. Init JsonNode di configurazione e dataIstanza
			if (StringUtils.isBlank(confExtractDichiarante)) {
				return result;
			}
	        getConf();
	        LOG.debug("[" + CLASS_NAME + "::call] getConf() OK.");
	        
	        // 2. Retrieve data
	        String cf = retrieveCodiceFiscaleDichiarante();
	        String cognome = retrieveCognomeDichiarante();
	        String nome = retrieveNomeDichiarante();
	        
			// 3. Aggiorna istanza
			int i = updateDichiaranteIstanza(istanzaSaved.getIdIstanza(), cf, cognome, nome);
			result = (i==1)?"Dichiarante istanza aggiornato.":"Errore aggiornamento dichiarante.";
			istanzaSaved.setCodiceFiscaleDichiarante(cf);
			istanzaSaved.setCognomeDichiarante(cognome);
			istanzaSaved.setNomeDichiarante(nome);

			LOG.debug("[" + CLASS_NAME + "::call] END result=" + result);
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::call] BusinessException");
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::call] Exception ", e);
			throw new BusinessException();
		}
	}

	private String retrieveCodiceFiscaleDichiarante() throws BusinessException {
    	LOG.debug("[" + CLASS_NAME + "::retrieveCodiceFiscaleDichiarante] BEGIN");
		String result = null;
		String key = conf.get("codice_fiscale_dichiarante_data_key")!=null?conf.get("codice_fiscale_dichiarante_data_key").asText():null;
		LOG.debug("[" + CLASS_NAME + "::retrieveCodiceFiscaleDichiarante] Using codice_fiscale_dichiarante_data_key (if not null): " + key);
		if (StringUtils.isNotEmpty(key)) {
			result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
		}
    	LOG.debug("[" + CLASS_NAME + "::retrieveCodiceFiscaleDichiarante] END " + result);
		return result==null?null:result.toUpperCase().trim();
	}
	private String retrieveCognomeDichiarante() throws BusinessException {
    	LOG.debug("[" + CLASS_NAME + "::retrieveCognomeDichiarante] BEGIN");
		String result = null;
		String key = conf.get("cognome_dichiarante_data_key")!=null?conf.get("cognome_dichiarante_data_key").asText():null;
		LOG.debug("[" + CLASS_NAME + "::retrieveCognomeDichiarante] Using cognome_dichiarante_data_key (if not null): " + key);
		if (StringUtils.isNotEmpty(key)) {
			result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
		}
    	LOG.debug("[" + CLASS_NAME + "::retrieveCognomeDichiarante] END " + result);
    	return result==null?null:result.toUpperCase().trim();
	}
	private String retrieveNomeDichiarante() throws BusinessException {
    	LOG.debug("[" + CLASS_NAME + "::retrieveNomeDichiarante] BEGIN");
		String result = null;
		String key = conf.get("nome_dichiarante_data_key")!=null?conf.get("nome_dichiarante_data_key").asText():null;
		LOG.debug("[" + CLASS_NAME + "::retrieveNomeDichiarante] Using nome_dichiarante_data_key (if not null): " + key);
		if (StringUtils.isNotEmpty(key)) {
			result = getDatiIstanzaHelper().extractedTextValueFromDataNodeByKey(key);
		}
    	LOG.debug("[" + CLASS_NAME + "::retrieveNomeDichiarante] END " + result);
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
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: "+strJson);
			}
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readConfJson] ERROR "+e.getMessage());
		    throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}


	private DatiIstanzaHelper getDatiIstanzaHelper() throws BusinessException {
		if (datiIstanzaHelper==null) {
			datiIstanzaHelper = new DatiIstanzaHelper();
			datiIstanzaHelper.initDataNode(getIstanzaSaved());
		}
		return datiIstanzaHelper;
	}
	
}
