/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.sql.Types;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonfobl.business.service.impl.dao.UtenteAreaRuoloDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.UtenteAreaRuoloEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per la gestione della relazione utente area ruolo
 * 
 * @see UtenteAreaRuoloEntity
 * 
 * @author Danilo
 */
@Component
public class UtenteAreaRuoloDAOImpl extends JdbcTemplateDAO implements UtenteAreaRuoloDAO {

	
	private static final String CLASS_NAME = "UtenteAreaRuoloDAOImpl";

	private static final  String FIND_BY_UTENTE_AREA_RUOLO  = "SELECT id_utente, id_area, id_ruolo, data_upd, attore_upd" +
			" FROM moon_fo_r_utente_area_ruolo" +
			" WHERE id_utente = :id_utente and id_area = :id_area and id_ruolo = :id_ruolo";

	private static final String INSERT = "INSERT INTO moon_fo_r_utente_area_ruolo(" + 
			" id_utente, id_area, id_ruolo, data_upd, attore_upd)" + 
			" VALUES (:id_utente, :id_area, :id_ruolo, :data_upd, :attore_upd)";

	/**
	 * Restituisce l'entity che descrive la relazione tripla : utente/area/ruolo
	 * 
	 * @param {@code idUtente idArea id Modulo} chiave primaria. 
	 * 
	 * @return l'entity ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public UtenteAreaRuoloEntity findByUtenteAreaRuolo(Long idUtente, Long idArea, Integer idRuolo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idUtente);
			params.addValue("id_area", idArea);
			params.addValue("id_ruolo", idRuolo);
			return (UtenteAreaRuoloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_UTENTE_AREA_RUOLO, params, BeanPropertyRowMapper.newInstance(UtenteAreaRuoloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	/**
	 * Inserisce un record di relazione tra utente area ruolo
	 * 
	 * @param {@code entity} l'entity che descrive la relazione tra utente area modulo
	 * 
	 * @return 1 se il record è stato correttamente inserito
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int insert(UtenteAreaRuoloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);		
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapUtenteAreaRuoloEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}

	private MapSqlParameterSource mapUtenteAreaRuoloEntityParameters(UtenteAreaRuoloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_utente", entity.getIdUtente());
		params.addValue("id_area", entity.getIdArea());
		params.addValue("id_ruolo", entity.getIdRuolo());
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);		
		return params;
	}


}
