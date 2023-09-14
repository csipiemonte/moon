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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.NazioneDAO;
import it.csi.moon.moonbobl.dto.extra.demografia.Nazione;
import it.csi.moon.moonbobl.exceptions.business.DAOException;


/**
 *
 */
@Component
@Qualifier("mock")
public class NazioneDAOMock implements NazioneDAO {

	/**
	 *
	 */
	public NazioneDAOMock() {

	}



	/**
	 *
	 * @return
	 */
	public ArrayList<Nazione> findAll() throws DAOException {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/dem-nao-nazioni.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    ArrayList<Nazione>elenco = new ArrayList<Nazione>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("nazioni");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			     JSONObject jsonObj = jsonArray.getJSONObject(i);

			    if (!jsonObj.equals(null)) {
			    	Nazione obj = new Nazione((Integer.parseInt(jsonObj.getString("id_nazione"))), jsonObj.getString("denominazione"));    
			        elenco.add(obj);
			    }
			}
			return elenco;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(" IOException "+ e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new DAOException(" JSONException "+ e.getMessage());
		}
	}


	/**
	 * 
	 * @param codice
	 * @return
	 */
	public Nazione findByPK(Integer codice) throws DAOException {
		for (Nazione naz : findAll()) {
			if (naz.getCodice().equals(codice)) {
				return naz;
			}
		}
		return null;
	}



	@Override
	public Nazione findByNome(String nomeNazione) throws DAOException {
		for (Nazione naz : findAll()) {
			if (naz.getNome().equals(nomeNazione)) {
				return naz;
			}
		}
		return null;
	}
	

}
