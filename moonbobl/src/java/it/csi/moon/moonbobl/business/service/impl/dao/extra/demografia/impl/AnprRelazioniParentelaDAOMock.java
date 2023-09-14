/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.impl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.AnprRelazioniParentelaDAO;
import it.csi.moon.moonbobl.dto.extra.demografia.RelazioneParentela;


/**
 * Servizio di esposizione dei dati Relazioni Parentela di ANPR
 * Fonte dati ANPR (fonte Istat) : https://www.anpr.interno.it/portale/documents/20182/50186/tabella_5_relazioni_parentela.xlsx
 * Ultimo aggiornamento ANPR dal 20 gennaio 2017 con aggiunta di "Unito civilmente"
 * Dati locali /dem-anpr-relazioni_parentela.json 
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

	private static final Integer RESPONSABILE_CONVIVENZA = 24;
	private static final Integer TUTORE = 26;
	private static final Integer NON_DEFINITO = 99;
    private static final Set<Integer> HIDDEN = Collections.unmodifiableSet(new HashSet<>(
    		Arrays.asList(RESPONSABILE_CONVIVENZA,TUTORE,NON_DEFINITO)));

	
	/**
	 *
	 */
	public AnprRelazioniParentelaDAOMock() {

	}



	/**
	 *
	 * @return
	 */
	public List<RelazioneParentela> findAll() {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/dem-anpr-relazioni_parentela.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<RelazioneParentela>elenco = new ArrayList<RelazioneParentela>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("relazioni_parentela");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			     JSONObject jsonObj = jsonArray.getJSONObject(i);

			    if (!jsonObj.equals(null)) {
			    	Integer idRelazioneParentela = Integer.parseInt(jsonObj.getString("id_tipo_relazione_parentela"));
			    	if (!HIDDEN.contains(idRelazioneParentela)) {
				    	RelazioneParentela obj = new RelazioneParentela(idRelazioneParentela, jsonObj.getString("descr_tipo_relazione_parentela"));    
				        elenco.add(obj);
			    	}
			    }
			}
			return elenco;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
