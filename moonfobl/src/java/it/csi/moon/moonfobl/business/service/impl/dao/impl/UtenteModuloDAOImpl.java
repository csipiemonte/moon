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

import it.csi.moon.moonfobl.business.service.impl.dao.UtenteModuloDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.UtenteModuloEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per la gestione della relazione utente modulo
 * 
 * @see UtenteModuloEntity
 * 
 * @author Danilo
 */
@Component
public class UtenteModuloDAOImpl extends JdbcTemplateDAO implements UtenteModuloDAO {

	private static final String CLASS_NAME = "UtenteModuloDAOImpl";

	private static final  String FIND_BY_MODULO_ENTE  = "SELECT id_utente, id_modulo, data_upd, attore_upd" +		
			" FROM moon_fo_r_utente_modulo" +
			" WHERE id_utente = :id_utente and id_modulo = :id_modulo";
	
	private static final String INSERT = "INSERT INTO moon_fo_r_utente_modulo(" + 
			" id_utente, id_modulo, data_upd, attore_upd)" + 
			" VALUES (:id_utente, :id_modulo, :data_upd, :attore_upd)";


	/**
	 * Restituisce l'entity che descrive la relazione utente modulo per chiave
	 * 
	 * @param {@code idUtente idModulo} chiave primaria. 
	 * 
	 * @return l'entity ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public UtenteModuloEntity findByUtenteModulo(Long idUtente, Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idUtente);
			params.addValue("id_modulo", idModulo);
			return (UtenteModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_MODULO_ENTE, params, BeanPropertyRowMapper.newInstance(UtenteModuloEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByUtenteModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByUtenteModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	/**
	 * Inserisce un record di relazione tra utente e modulo
	 * 
	 * @param {@code entity} l'entity che descrive la relazione tra utente e modulo
	 * 
	 * @return 1 se il record è stato correttamente inserito
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int insert(UtenteModuloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);		
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapUtenteModuloEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}

	private MapSqlParameterSource mapUtenteModuloEntityParameters(UtenteModuloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_utente", entity.getIdUtente());
		params.addValue("id_modulo", entity.getIdModulo());
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		
		return params;
	}


}
