/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.territorio.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.territorio.ToponomasticaDAO;
import it.csi.moon.moonbobl.dto.extra.territorio.Civico;
import it.csi.moon.moonbobl.dto.extra.territorio.Piano;
import it.csi.moon.moonbobl.dto.extra.territorio.PianoNUI;
import it.csi.moon.moonbobl.dto.extra.territorio.Via;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipologieCivico;

@Component
public class ToponomasticaDAOMock implements ToponomasticaDAO {

	public ToponomasticaDAOMock() {
		// TODO Auto-generated constructor stub
	}
	
	//
	// Entity Via
	//
	@Override
	public List<Via> getVie() throws DAOException {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-vie.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<Via> elencoVie = new ArrayList<Via>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("vie");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			     JSONObject jsonVia = jsonArray.getJSONObject(i);

			    if (!jsonVia.equals(null)) {
			        Via via = new Via((Integer.parseInt(jsonVia.getString("codice"))), jsonVia.getString("denominazione_corrente_2"));    
			        elencoVie.add(via);
			    }
			}
			return elencoVie;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(" IOException "+ e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new DAOException(" JSONException "+ e.getMessage());
		}
	}
	
	@Override
	public List<Via> getVie(int limit, int skip) throws DAOException {
		
		assert limit>=0 : "Limit is negative !";
		assert skip>=0 : "Skip is negative !";
		
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-vie.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<Via> elencoVie = new ArrayList<Via>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("vie");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			int posMax = (jsonArray.length()-skip);
			if (limit>0) {
				int posMax2 = (skip+limit);
				// prendo il MIN ()
				posMax = (posMax2<posMax)?posMax2:posMax;
			}
			for (int i = skip; i < posMax ; i++) {
			     JSONObject jsonVia = jsonArray.getJSONObject(i);

			    if (!jsonVia.equals(null)) {
			        Via via = new Via((Integer.parseInt(jsonVia.getString("codice"))), jsonVia.getString("denominazione_corrente_2"));    
			        elencoVie.add(via);
			    }
			}
			return elencoVie;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(" IOException "+ e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new DAOException(" JSONException "+ e.getMessage());
		}
	}
	
	@Override
	public Via getViaById(Integer codiceVia) throws ItemNotFoundBusinessException, DAOException {
		for (Via via : getVie()) {
			if (via.getCodice().equals(codiceVia)) {
				return via;
			}
		}
		throw new ItemNotFoundBusinessException();
	}
	
	
    //
    // Numero Radici
	//
	public List<Integer> getNumeriRadiceByVia(Integer codiceVia) throws DAOException {
		return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
	}
	
	
    //
	// Entity Civico
	//
	@Override
	public List<Civico> getCiviciByVia(Integer codiceVia, DecodificaTipologieCivico tipologieCivico) throws DAOException {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-civici.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<Civico> elencoCivici = new ArrayList<Civico>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("civici");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			     JSONObject jsonVia = jsonArray.getJSONObject(i);

			    if (!jsonVia.equals(null)) {
			    	Civico civico = new Civico((Integer.parseInt(jsonVia.getString("id_civico"))), jsonVia.getString("denominazione"), "N");    
			        elencoCivici.add(civico);
			    }
			}
			return elencoCivici;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(" IOException "+ e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new DAOException(" JSONException "+ e.getMessage());
		}
	}
	
	@Override
	public Civico getCivicoById(Integer codiceVia, Integer codiceCivico) throws ItemNotFoundBusinessException, DAOException {
		for (Civico civico : getCiviciByVia(codiceVia, null)) {
			if (civico.getCodice().equals(codiceCivico)) {
				return civico;
			}
		}
		throw new ItemNotFoundBusinessException();
	}
	
	@Override
	public List<Civico> getCiviciByViaNumero(Integer codiceVia, Integer numero, DecodificaTipologieCivico tipologieCivico) throws DAOException {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-civici.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<Civico> elencoCivici = new ArrayList<Civico>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("civici");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			     JSONObject jsonVia = jsonArray.getJSONObject(i);

			    if (!jsonVia.equals(null)) {
			    	Civico civico = new Civico((Integer.parseInt(jsonVia.getString("id_civico"))), jsonVia.getString("denominazione"), "N");    
			        elencoCivici.add(civico);
			    }
			}
			return elencoCivici;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(" IOException "+ e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new DAOException(" JSONException "+ e.getMessage());
		}
	}
	
	
    //
	// Entity PianoNUI
	//
	@Override
	public List<PianoNUI> getPianiNuiByCivico(Integer codiceVia, Integer codiceCivico) throws DAOException {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-pianinui.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<PianoNUI> elencoPianoNUI = new ArrayList<PianoNUI>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("pianinui");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			     JSONObject jsonVia = jsonArray.getJSONObject(i);

			    if (!jsonVia.equals(null)) {
			    	PianoNUI pianoNui = new PianoNUI((Integer.parseInt(jsonVia.getString("id_uiu"))), "Piano "+jsonVia.getString("codice_piano")+" NUI:"+jsonVia.getString("nui"));    
			        elencoPianoNUI.add(pianoNui);
			    }
			}
			return elencoPianoNUI;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(" IOException "+ e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new DAOException(" JSONException "+ e.getMessage());
		}
	}
	
	@Override
	public PianoNUI getPianoNuiById(Integer codiceVia, Integer codiceCivico, Integer codicePianoNUI) throws ItemNotFoundBusinessException, DAOException {
		throw new DAOException("NOT IMPLEMENTED");
	}

	
    //
	// Entity Piano
	//
	@Override
	public List<Piano> getPiani() throws DAOException {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-piani.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<Piano> result = new ArrayList<Piano>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("piani");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			     JSONObject jsonVia = jsonArray.getJSONObject(i);

			    if (!jsonVia.equals(null)) {
			        Piano piano = new Piano(jsonVia.getString("codice"), jsonVia.getString("nome"));    
			        result.add(piano);
			    }
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(" IOException "+ e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new DAOException(" JSONException "+ e.getMessage());
		}
	}
	
	@Override
	public Piano getPianoById(String codicePiano) throws ItemNotFoundBusinessException, DAOException {
		for (Piano piano : getPiani()) {
			if (piano.getCodice().equals(codicePiano)) {
				return piano;
			}
		}
		throw new ItemNotFoundBusinessException();
	}
}
