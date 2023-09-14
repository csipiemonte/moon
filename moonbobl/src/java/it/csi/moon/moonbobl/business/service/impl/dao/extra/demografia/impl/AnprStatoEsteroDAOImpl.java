/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.AnprStatoEsteroDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.AnprStatoEsteroEntity;
import it.csi.moon.moonbobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonbobl.exceptions.business.DAOException;


/**
 *
 */
@Component
@Qualifier("moon")
public class AnprStatoEsteroDAOImpl extends JdbcTemplateDAO implements AnprStatoEsteroDAO {

	private final static String CLASS_NAME = "AnprStatoEsteroDAOImpl";

	private static final  String ELENCO  = "SELECT id_stato_estero,denominazione,denominazione_istat,denominazione_istat_en,dt_inizio_validita,dt_fine_validita,cod_iso_3166_1_alpha3,cod_mae,cod_min,cod_at,cod_istat,cittadinanza,nascita,residenza,fonte,tipo,cod_iso_sovrano,motivo" +
		" FROM moon_ext_anpr_c_stato_estero";

	/**
	 * Cache locale degli stati esteri
	 */
	private List<AnprStatoEsteroEntity> listaStatiEsteri;
	
	/**
	 * Inizializa la cache
	 */
	private void init() {
		try {
			log.debug("[" + CLASS_NAME + "::init] BEGIN END");
			listaStatiEsteri = findAllForce();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::init] Errore database findAllForce() : "+e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Ottiene tutti stati esteri presenti nel sistema da Database
	 * Server per inizializzare la cache
	 * 
	 * @return la lista di tutti stati esteri
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	private List<AnprStatoEsteroEntity> findAllForce() throws DAOException {
		log.debug("[" + CLASS_NAME + "::findAllForse] BEGIN END");
		log.debug("[" + CLASS_NAME + "::findAllForce] ========================================");
		return ( List<AnprStatoEsteroEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(AnprStatoEsteroEntity.class));
	}

	/**
	 * Ottiene tutti gli stati esteri presenti nel sistema (da cache locale)
	 * 
	 * @return la lista di tutti stati esteri
	 * 
	 * @throws DAOException se si verificano altri errori. Null se lista vuota.
	 */
	@Override
	public List<AnprStatoEsteroEntity> findAll() {
		log.debug("[" + CLASS_NAME + "::findAll] BEGIN END");
		log.debug("[" + CLASS_NAME + "::findAll] ========================================");
		if (listaStatiEsteri==null) {
			init();
		}
		return listaStatiEsteri;
	}
	

	
//	
//	
//	/**
//	 *
//	 */
//	public AnprStatoEsteroDAOImpl() {
//
//	}
//
//
//
//	/**
//	 *
//	 * @return
//	 */
//	public ArrayList<AnprStatoEsteroEntity> findAll() throws DAOException {
//		StringBuilder sb = new StringBuilder();
//		InputStream in = getClass().getResourceAsStream("/dem-anpr-stati_esteri.json"); 
//		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//		String line;
//	    ArrayList<Nazione>elenco = new ArrayList<Nazione>();
//	    try {
//			while ((line = reader.readLine()) != null) {
//			    sb.append(line);
//			}
//			JSONObject json = new JSONObject(sb.toString());
//			JSONArray jsonArray = json.getJSONArray("stati_esteri");
//			//JSONArray jsonArray = new JSONArray(sb.toString());
//			for (int i = 0; i < jsonArray.length(); i++) {
//			     JSONObject jsonObj = jsonArray.getJSONObject(i);
//
//			    if (!jsonObj.equals(null)) {
//			    	Nazione obj = new Nazione((Integer.parseInt(jsonObj.getString("cod_istat"))), jsonObj.getString("denominazione"));
////			    	Nazione obj = new Nazione((Integer.parseInt(jsonObj.getString("id_stato_estero"))), jsonObj.getString("denominazione"));
//			        elenco.add(obj);
//			    }
//			}
//			return elenco;
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new DAOException(" IOException ");
//		} catch (JSONException e) {
//			e.printStackTrace();
//			throw new DAOException(" JSONException ");
//		}
//	}
//
//
//	/**
//	 * 
//	 * @param codice
//	 * @return
//	 */
//	public Nazione findByPK(Integer codice) throws DAOException {
//		for (Nazione naz : findAll()) {
//			if (naz.getCodice().equals(codice)) {
//				return naz;
//			}
//		}
//		return null;
//	}
//
//
//
//	@Override
//	public Nazione findByNome(String nomeNazione) throws DAOException {
//		for (Nazione naz : findAll()) {
//			if (naz.getNome().equals(nomeNazione)) {
//				return naz;
//			}
//		}
//		return null;
//	}
//	

}
