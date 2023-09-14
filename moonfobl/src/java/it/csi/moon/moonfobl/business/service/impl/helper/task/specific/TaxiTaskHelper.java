/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper.task.specific;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.moonfobl.business.service.impl.dao.ext.BuoniTaxiDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.ExtTaxiEntity;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * TaxiTaskHelper
 *
 * @author laurent
 *
 */
public class TaxiTaskHelper {

	private static final String CLASS_NAME = "TaxiTaskHelper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private JsonNode conf = null;
	private Istanza istanza;
	private String confSendEmail;
	private DatiIstanzaHelper datiIstanzaHelper;

	@Autowired
	BuoniTaxiDAO buoniTaxiDAO;

    public TaxiTaskHelper(Istanza istanza, String confSendEmail) {
		super();
		if(istanza == null) {
			LOG.error("[" + CLASS_NAME + "::TaxiTaskHelper] TaxiTaskHelper() with istanze NULL.");
			throw new BusinessException();
		}
		this.istanza = istanza;
		this.confSendEmail = confSendEmail;
		LOG.debug("[" + CLASS_NAME + "::TaxiTaskHelper] CONSTRUCTOR istanzaSaved.getIdIstanza(): "+getIstanzaSaved().getIdIstanza());

        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	private Istanza getIstanzaSaved() {
		return istanza;
	}

	
	/**
	 * Ritorna la lista dei buoni taxi per SendEmail
	 * sulla base dell idIstanza
	 * @return
	 */
	public String getBuoni() {
		List<ExtTaxiEntity> buoni = buoniTaxiDAO.findByIdIstanza(getIstanzaSaved().getIdIstanza());

		String listaBuoni =  buoni.stream()
				.map( b -> String.join("   ",b.getCognome(),b.getNome(),"    CODICE:",b.getCodice_buono()))
				.collect(Collectors.joining("\n"));
		return listaBuoni;

		//return "COGNOME1 NOME1 BUONO001\nCOGNOME2 NOME2 BUONO002";
	}


	//
	//
	private JsonNode getConf() throws Exception {
		if (conf==null) {
			conf = readConfJson(confSendEmail);
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


	private DatiIstanzaHelper getDatiIstanzaHelper() {
		if (datiIstanzaHelper==null) {
			datiIstanzaHelper = new DatiIstanzaHelper();
			datiIstanzaHelper.initDataNode(getIstanzaSaved());
		}
		return datiIstanzaHelper;
	}
}
