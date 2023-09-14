/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.impl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.extra.demografia.RelazioneParentela;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia.AnprRelazioniParentelaDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;


/**
 * Servizio di esposizione dei dati Relazioni Parentela di ANPR
 * Fonte dati ANPR (fonte Istat) : https://www.anpr.interno.it/portale/documents/20182/50186/tabella_5_relazioni_parentela.xlsx
 * Ultimo aggiornamento ANPR dal 20 gennaio 2017 con aggiunta di "Unito civilmente"
 * Dati locali /dem-relazioniParentela.json 
 * 
 * Presenti 26 rows su 30
 * Escluse le seguente 4 valori :
 *   24	Responsabile della convivenza non affettiva
 *   26	Tutore
 *   99	Non definito/comunicato
 *   80	Adottato
 *
 * @author Laurent Pissard
 */
@Component
@Qualifier("mock")
public class AnprRelazioniParentelaDAOMock implements AnprRelazioniParentelaDAO {
	
	private static final String CLASS_NAME = "AnprRelazioniParentelaDAO";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();

	private static final Integer RESPONSABILE_CONVIVENZA = 24;
	private static final Integer TUTORE = 26;
	private static final Integer NON_DEFINITO = 99;
    private static final Set<Integer> HIDDEN = Collections.unmodifiableSet(new HashSet<>(
    		Arrays.asList(RESPONSABILE_CONVIVENZA,TUTORE,NON_DEFINITO)));

	/**
	 *
	 * @return
	 */
	public List<RelazioneParentela> findAll() {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/dem-anpr-relazioni_parentela.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<RelazioneParentela>elenco = new ArrayList<>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			ObjectMapper objectMapper = new ObjectMapper();
//			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode json = objectMapper.readValue(sb.toString(), JsonNode.class);
			JsonNode jsonArray = json.get("relazioni_parentela");
			Iterator<JsonNode> iter = jsonArray.elements();
			while (iter.hasNext()) { 
				JsonNode jsonObj = iter.next();
				if (jsonObj != null) {
			    	Integer idRelazioneParentela = jsonObj.get("id_tipo_relazione_parentela").asInt();
			    	if (!HIDDEN.contains(idRelazioneParentela)) {
				    	RelazioneParentela obj = new RelazioneParentela(idRelazioneParentela, jsonObj.get("descr_tipo_relazione_parentela").asText());    
				        elenco.add(obj);
			    	}
			    }
			}
			return elenco;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::findAll] IOException ", e);
			throw new DAOException();
		}
	}


	/**
	 * 
	 * @param codice
	 * @return
	 */
	public RelazioneParentela findByPK(Integer codice) {
		for (RelazioneParentela relaz : findAll()) {
			if (relaz.getCodice().equals(codice)) {
				return relaz;
			}
		}
		return null;
	}

}
