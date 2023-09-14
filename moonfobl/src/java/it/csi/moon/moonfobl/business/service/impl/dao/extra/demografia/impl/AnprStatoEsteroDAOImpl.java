/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia.AnprStatoEsteroDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dto.AnprStatoEsteroEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;


/**
 *
 */
@Component
@Qualifier("moon")
public class AnprStatoEsteroDAOImpl extends JdbcTemplateDAO implements AnprStatoEsteroDAO {

	private static final String CLASS_NAME = "AnprStatoEsteroDAOImpl";

	private static final  String ELENCO  = "SELECT id_stato_estero,denominazione,denominazione_istat,denominazione_istat_en," +
			"dt_inizio_validita,dt_fine_validita,cod_iso_3166_1_alpha3,cod_mae,cod_min,cod_at,cod_istat,cittadinanza,nascita," +
			"residenza,fonte,tipo,cod_iso_sovrano,motivo, flag_ue" +
			" FROM moon_ext_anpr_c_stato_estero";

	private static final String FIND_BY_NAME = "SELECT id_stato_estero,denominazione,denominazione_istat,denominazione_istat_en, " +
			"dt_inizio_validita,dt_fine_validita,cod_iso_3166_1_alpha3,cod_mae,cod_min,cod_at,cod_istat,cittadinanza,nascita," +
			"residenza,fonte,tipo,cod_iso_sovrano,motivo, flag_ue" +
			" FROM moon_ext_anpr_c_stato_estero WHERE denominazione=:denominazione";

	private static final String WHERE_RESIDENTE = " WHERE residenza = 'S'";
	private static final String WHERE_CITTADINANZA = " WHERE cittadinanza = 'S'";
	private static final String WHERE_NASCITA = " WHERE nascita = 'S'";
	
	private static final String AND_FLAG_UE_N = " AND flag_ue = 'N'";
	private static final String AND_FLAG_UE_S_NOITA = " AND flag_ue = 'S' AND DENOMINAZIONE <> 'ITALIA'";
	private static final String AND_FLAG_UE_S = " AND flag_ue = 'S'";
	
	/**
	 * Cache locale degli stati esteri
	 */
	private List<AnprStatoEsteroEntity> listaStatiEsteri;
	
	/**
	 * Inizializa la cache
	 */
	private void init() {
		try {
			LOG.debug("[" + CLASS_NAME + "::init] BEGIN END");
			listaStatiEsteri = findAllForce();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::init] Errore database init() : "+e.getMessage(),e);
			throw new BusinessException();
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
		LOG.debug("[" + CLASS_NAME + "::findAllForse] BEGIN END");
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
		LOG.debug("[" + CLASS_NAME + "::findAll] BEGIN END");
		if (listaStatiEsteri==null) {
			init();
		}
		return listaStatiEsteri;
	}

	@Override
	public List<AnprStatoEsteroEntity> find(String uso, String ue) {
		LOG.debug("[" + CLASS_NAME + "::find] BEGIN END");
		uso = (uso == null) ? "" : uso;
		ue = (ue == null) ? "" : ue;
		StringBuilder sb = new StringBuilder().append(ELENCO);
		// cittadinanza,nascita,residenza
		switch (uso) {
			case "RES":	sb.append(WHERE_RESIDENTE); break;
			case "CIT":	sb.append(WHERE_CITTADINANZA); break;
			case "NAS":	sb.append(WHERE_NASCITA); break;
			default:	sb.append(WHERE_RESIDENTE); break;
		}
		switch (ue) {
			case "UE":		sb.append(AND_FLAG_UE_S); break;
			case "UENOITA":	sb.append(AND_FLAG_UE_S_NOITA); break;
			case "NONUE":	sb.append(AND_FLAG_UE_N); break;
			default: break;
		}
		
		return (List<AnprStatoEsteroEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), new MapSqlParameterSource(), BeanPropertyRowMapper.newInstance(AnprStatoEsteroEntity.class));
	}

	@Override
	public AnprStatoEsteroEntity findByName(String name) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByName] IN name: " + name);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("denominazione", name);
			return (AnprStatoEsteroEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_NAME, params,
					BeanPropertyRowMapper.newInstance(AnprStatoEsteroEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByName] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByName] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

}
