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
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.CittadinanzeDAO;
import it.csi.moon.moonbobl.dto.extra.demografia.Cittadinanza;
import it.csi.moon.moonbobl.exceptions.business.DAOException;


/**
 *
 */
@Component
@Qualifier("mock")
public class CittadinanzeDAOMock implements CittadinanzeDAO {

	/**
	 *
	 */
	public CittadinanzeDAOMock() {

	}

	/**
	 *
	 * @return
	 */
	public List<Cittadinanza> findAll() throws DAOException {
		return findAll(0,0);
	}
	public List<Cittadinanza> findAll(int limit, int skip) throws DAOException {
		
		assert limit>=0 : "Limit is negative !";
		assert skip>=0 : "Skip is negative !";
		
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/dem-nao-cittadinanze.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<Cittadinanza>elenco = new ArrayList<Cittadinanza>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("cittadinanze");
			
			int posMax = (jsonArray.length()-skip);
			if (limit>0) {
				int posMax2 = (skip+limit);
				// prendo il MIN ()
				posMax = (posMax2<posMax)?posMax2:posMax;
			}
			for (int i = skip; i < posMax ; i++) {
			     JSONObject jsonObj = jsonArray.getJSONObject(i);

			    if (!jsonObj.equals(null)) {
			    	Cittadinanza obj = new Cittadinanza((Integer.parseInt(jsonObj.getString("id_nazione"))), jsonObj.getString("desc_cittadinanza_maschile"), jsonObj.getString("flag_ue"));    
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
	public Cittadinanza findByPK(Integer codice) throws DAOException {
		for (Cittadinanza cittd : findAll()) {
			if (cittd.getCodice().equals(codice)) {
				return cittd;
			}
		}
		return null;
	}


	public Cittadinanza findByDesc(String descMaschile) throws DAOException {
		for (Cittadinanza cittd : findAll()) {
			if (cittd.getNome().equals(descMaschile)) {
				return cittd;
			}
		}
		return null;
	}
	
}
