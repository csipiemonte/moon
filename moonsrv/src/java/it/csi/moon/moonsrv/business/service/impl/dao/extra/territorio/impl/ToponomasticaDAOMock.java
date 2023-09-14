/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.extra.territorio.Civico;
import it.csi.moon.commons.dto.extra.territorio.PianoNUI;
import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.ToponomasticaDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
* DAO Toponomastica del Comune di Torino via servizi Mock 
* Usare ToponomasticaApiRestDAO per i oggetti della Fonte Toponomastica
* Meglio, usare i ToponomasticaService con @Qualifier("RS") che torna oggetti Moon (impl: ToponomasticaServiceRSImpl)
*  
* @author Francesco Zucaro
* @author Laurent Pissard
* 
* @deprecated Use ToponomasticaApiRestDAO interface
* 
* @since 1.0.0
*/

@Deprecated
@Component
@Qualifier("mock")
public class ToponomasticaDAOMock implements ToponomasticaDAO {
	
	private static final String CLASS_NAME = "ToponomasticaDAOMock";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();

	public ToponomasticaDAOMock() {
		// TODO Auto-generated constructor stub
	}
	

	//
	// Entity Via
	@Override
	public List<Via> getVie() throws DAOException {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-vie-light.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<Via>elencoVie = new ArrayList<>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			ObjectMapper objectMapper = new ObjectMapper();
//					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode json = objectMapper.readValue(sb.toString(), JsonNode.class);
			JsonNode jsonArray = json.get("vieLight");

			Iterator<JsonNode> iter = jsonArray.elements();
			while (iter.hasNext()) { 
				JsonNode jsonVia = iter.next();
			    if (jsonVia != null) {
			        Via via = new Via();
			        via.setCodice(jsonVia.get("idVia").asInt());
			        via.setNome(StrUtils.join(" ", 
						jsonVia.has("sedime")&&jsonVia.get("sedime").has("descrizione")?jsonVia.get("sedime").get("descrizione").asText():null,
						jsonVia.has("sedime")&&jsonVia.get("sedime").has("preposizione")?jsonVia.get("sedime").get("preposizione").asText():null,
						jsonVia.get("denominazionePrincipale").asText()).trim());
			        elencoVie.add(via);
			    }
			}
			return elencoVie;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::getVie] IOException ", e);
			throw new DAOException(" IOException "+ e.getMessage());
		}
	}

	@Override
	public List<Via> getVie(int limit, int skip) throws DAOException {
		assert limit>=0 : "Limit is negative !";
		assert skip>=0 : "Skip is negative !";
		
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-vie-light.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    List<Via>elencoVie = new ArrayList<>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			ObjectMapper objectMapper = new ObjectMapper();
//				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode json = objectMapper.readValue(sb.toString(), JsonNode.class);
			JsonNode jsonArray = json.get("vieLight");
			int i = 0;
			Iterator<JsonNode> iter = jsonArray.elements();
			while (iter.hasNext() && (limit==0 || elencoVie.size()<limit)) { 
				JsonNode jsonVia = iter.next();
			    if (jsonVia != null && (skip==0 || i<skip)) {
			        Via via = new Via();
			        via.setCodice(jsonVia.get("idVia").asInt());
			        via.setNome(StrUtils.join(" ", 
						jsonVia.has("sedime")&&jsonVia.get("sedime").has("descrizione")?jsonVia.get("sedime").get("descrizione").asText():null,
						jsonVia.has("sedime")&&jsonVia.get("sedime").has("preposizione")?jsonVia.get("sedime").get("preposizione").asText():null,
						jsonVia.get("denominazionePrincipale").asText()).trim());
			        elencoVie.add(via);
			    }
			    i++;
			}
			return elencoVie;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::getVie] IOException ", e);
			throw new DAOException(" IOException "+ e.getMessage());
		}
	}

	
	 
	@Override
	public Via getViaByPK(Integer codiceVia) throws DAOException {
		for (Via via : getVie()) {
			if (via.getCodice().equals(codiceVia)) {
				return via;
			}
		}
		return null;
	}
	

	@Override
	public Via findVieByDesc(String descVia) throws DAOException {
		for (Via via : getVie()) {
			if (via.getNome().equals(descVia)) {
				return via;
			}
		}
		return null;
	}
	
	
	

	@Override
	public Civico findCivicoByPK(Integer codiceVia, Integer codiceCivico) throws DAOException {
		for (Civico civico : findCiviciByCodiceVia(codiceVia)) {
			if (civico.getCodice().equals(codiceCivico)) {
				return civico;
			}
		}
		return null;
	}
	

	
	
	@Override
	public ArrayList<Civico> findCiviciByCodiceVia(Integer codiceVia) throws DAOException {
		/*
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-civici.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    ArrayList<Civico>elencoCivici = new ArrayList<Civico>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("civici");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			     JSONObject jsonVia = jsonArray.getJSONObject(i);

			    if (jsonVia != null) {
			    	Civico civico = new Civico((Integer.parseInt(jsonVia.getString("id_civico"))), jsonVia.getString("denominazione"), null);    
			        elencoCivici.add(civico);
			    }
			}
			return elencoCivici;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::findCiviciByCodiceVia] IOException: ",e);
			throw new DAOException(" IOException "+ e.getMessage());
		} catch (JSONException e) {
			LOG.error("[" + CLASS_NAME + "::findCiviciByCodiceVia] JSONException: ",e);
			throw new DAOException(" JSONException "+ e.getMessage());
		}
		*/ return null;
	}



	/**
	 * 
	 * @param codice
	 * @return
	 */
	@Override
	public ArrayList<PianoNUI> findPianiNUIByCivico(Integer codiceVia, Integer codiceCivico) throws DAOException {
		/*
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("/topo-pianinui.json"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
	    ArrayList<PianoNUI>elencoPianoNUI = new ArrayList<PianoNUI>();
	    try {
			while ((line = reader.readLine()) != null) {
			    sb.append(line);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonArray = json.getJSONArray("pianinui");
			//JSONArray jsonArray = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
			     JSONObject jsonVia = jsonArray.getJSONObject(i);

			    if (jsonVia != null) {
			    	PianoNUI pianoNui = new PianoNUI((Integer.parseInt(jsonVia.getString("id_uiu"))), "Piano "+jsonVia.getString("codice_piano")+" NUI:"+jsonVia.getString("nui"));    
			        elencoPianoNUI.add(pianoNui);
			    }
			}
			return elencoPianoNUI;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::findPianiNUIByCivico] IOException: ",e);
			throw new DAOException(" IOException "+ e.getMessage());
		} catch (JSONException e) {
			LOG.error("[" + CLASS_NAME + "::findPianiNUIByCivico] JSONException: ",e);
			throw new DAOException(" JSONException "+ e.getMessage());
		}
		*/ return null;
	}

	
}
