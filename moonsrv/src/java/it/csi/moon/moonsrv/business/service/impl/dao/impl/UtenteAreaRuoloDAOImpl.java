/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.UtenteAreaRuoloEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.UtenteAreaRuoloDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai ruoli utenti
 * 
 * @see UtenteAreaRuoloEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class UtenteAreaRuoloDAOImpl extends JdbcTemplateDAO implements UtenteAreaRuoloDAO {

	private static final String CLASS_NAME = "UtenteAreaRuoloDAOImpl";

	private static final  String FIND_BY_ID_UTENTE  = "SELECT id_utente, id_area, id_ruolo, data_upd, attore_upd " +
			" FROM moon_fo_r_utente_area_ruolo" +
			" WHERE id_utente = :id_utente";

	private static final  String FIND_BY_ID_RUOLO  = "SELECT id_utente, id_area, id_ruolo, data_upd, attore_upd " +
			" FROM moon_fo_r_utente_area_ruolo" +
			" WHERE id_ruolo = :id_ruolo";
	private static final  String FIND_BY_ID_AREA_RUOLO  = "SELECT id_utente, id_area, id_ruolo, data_upd, attore_upd " +
			" FROM moon_fo_r_utente_area_ruolo" +
			" WHERE id_area = :id_area" +
			" AND id_ruolo = :id_ruolo";

	private static final String ELENCO = "SELECT id_utente, id_area, id_ruolo, data_upd, attore_upd" +
			" FROM moon_fo_r_utente_area_ruolo" ;

	private static final String INSERT = "INSERT INTO moon_fo_r_utente_area_ruolo(" + 
			" id_utente, id_area, id_ruolo, data_upd, attore_upd)" + 
			" VALUES (:id_utente, :id_area, :id_ruolo, :data_upd, :attore_upd)";

	private static final String DELETE = "DELETE FROM moon_fo_r_utente_area_ruolo WHERE id_utente = :id_utente AND id_area = :id_area AND id_ruolo = :id_ruolo";

	private static final String DELETE_ALL_BY_ID_UTENTE = "DELETE FROM moon_fo_r_utente_area_ruolo WHERE id_utente = :id_utente";

	private static final String DELETE_ALL_BY_ID_RUOLO = "DELETE FROM moon_fo_r_utente_area_ruolo WHERE id_ruolo = :id_ruolo";



	@Override
	public List<UtenteAreaRuoloEntity> findByIdUtente(Long idUtente) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idUtente);
			return (List<UtenteAreaRuoloEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_UTENTE, params, BeanPropertyRowMapper.newInstance(UtenteAreaRuoloEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdUtente] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdUtente] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<UtenteAreaRuoloEntity> findByIdRuolo(Integer idRuolo) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ruolo", idRuolo);
			return (List<UtenteAreaRuoloEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_RUOLO, params, BeanPropertyRowMapper.newInstance(UtenteAreaRuoloEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdUtente] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdUtente] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<UtenteAreaRuoloEntity> findByIdAreaIdRuolo(Long idArea, Integer idRuolo) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_area", idArea);
			params.addValue("id_ruolo", idRuolo);
			return (List<UtenteAreaRuoloEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_AREA_RUOLO, params, BeanPropertyRowMapper.newInstance(UtenteAreaRuoloEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdUtente] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdUtente] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<UtenteAreaRuoloEntity> find() throws DAOException {
		return ( List<UtenteAreaRuoloEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(UtenteAreaRuoloEntity.class));
	}

	
	@Override
	public int insert(UtenteAreaRuoloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapRuoloUtentiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}


	@Override
	public int delete(Long idUtente, Long idArea, Integer idRuolo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idUtente: "+idUtente);
			LOG.debug("[" + CLASS_NAME + "::delete] IN idArea: "+idArea);
			LOG.debug("[" + CLASS_NAME + "::delete] IN idRuolo: "+idRuolo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idUtente);
			params.addValue("id_area", idArea);
			params.addValue("id_ruolo", idRuolo);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}



	@Override
	public int delete(UtenteAreaRuoloEntity entity) throws DAOException {
		return delete(entity.getIdUtente(), entity.getIdArea(), entity.getIdRuolo());
	}


	@Override
	public int deleteAllByIdUtente(Long idUtente) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN id_utente: "+idUtente);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idUtente);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_ALL_BY_ID_UTENTE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	@Override
	public int deleteAllByIdRuolo(Integer idRuolo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN id_ruolo: "+idRuolo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idRuolo);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_ALL_BY_ID_RUOLO, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	private MapSqlParameterSource mapRuoloUtentiEntityParameters(UtenteAreaRuoloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_utente", entity.getIdUtente());
		params.addValue("id_ruolo", entity.getIdRuolo());
		params.addValue("data_upd", entity.getDataUpd(), Types.DATE);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		return params;
	}



}
