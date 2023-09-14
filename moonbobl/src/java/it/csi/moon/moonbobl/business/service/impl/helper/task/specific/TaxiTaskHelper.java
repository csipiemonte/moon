/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.task.specific;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import it.csi.moon.moonbobl.business.service.impl.dao.ext.BuoniTaxiDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ext.ExtTaxiEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.util.LoggerAccessor;

/**
 * TaxiTaskHelper
 *
 * @author laurent
 *
 */
public class TaxiTaskHelper {

	private final static String CLASS_NAME = "TaxiTaskHelper";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	private JsonNode conf = null;
	private Istanza istanza;
	private String confSendEmail;
	private DatiIstanzaHelper datiIstanzaHelper;

	@Autowired
	BuoniTaxiDAO buoniTaxiDAO;

    public TaxiTaskHelper(Istanza istanza, String confSendEmail) {
		super();
		this.istanza = istanza;
		this.confSendEmail = confSendEmail;
		log.debug("[" + CLASS_NAME + "::SendEmailDichiaranteIstanzaTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): "+getIstanzaSaved().getIdIstanza());

        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	private Istanza getIstanzaSaved() {
		if (istanza==null) return null;
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
