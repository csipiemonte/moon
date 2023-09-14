/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;
/**
 * DAO per accesso alle Categorie
 * 
 */
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ParametroEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.ParametroDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

@Component
public class ParametroDAOImpl extends JdbcTemplateDAO implements ParametroDAO {

	private static final String CLASS_NAME = "ParametroDAOImpl";
	
	private static final String SELECT = "SELECT id_parametro, codice_componente, codice_parametro, valore";
	
	private static final String FIND_BY_ID = SELECT + " FROM moon_c_parametro WHERE id_parametro = :id_parametro";
	private static final String FIND_BY_CODICI = SELECT + " FROM moon_c_parametro WHERE codice_componente = :codice_componente AND codice_parametro = :codice_parametro";
	private static final String ELENCO = SELECT + " FROM moon_c_parametro";
	
	private static final String INSERT = "INSERT INTO moon_c_parametro (id_parametro, codice_componente, codice_parametro, valore)" + 
			" VALUES (:id_parametro, :codice_componente, :codice_parametro, :valore)";
	private static final String DELETE = "DELETE FROM moon_c_parametro WHERE id_parametro = :id_parametro";
	private static final String UPDATE = "UPDATE moon_c_parametro" +
			" SET codice_componente=:codice_componente, codice_parametro=:codice_parametro, valore=:valore" +
			" WHERE id_parametro = :id_parametro";

	private static final String SEQ_ID = "SELECT nextval('moon_c_parametro_id_parametro_seq')";
	
	@Override
	public ParametroEntity findById(long idParametro) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_parametro", idParametro);
			return (ParametroEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ParametroEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException();
		}
	}

	@Override
	public ParametroEntity findByCodici(String codiceComponente, String codiceParametro) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_componente", codiceComponente);
			params.addValue("codice_ambito", codiceParametro);
			return (ParametroEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CODICI, params, BeanPropertyRowMapper.newInstance(ParametroEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCodici] Elemento non trovato: " + emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodici] Errore database: " + e.getMessage(),e);
			throw new DAOException();
		}
	}
	
	@Override
	public List<ParametroEntity> find() throws DAOException {
		return (List<ParametroEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(ParametroEntity.class));
	}

	
	@Override
	public Long insert(ParametroEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idParametro = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdParametro(idParametro);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapParametroEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idParametro;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: " + e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}
	
	@Override
	public int delete(ParametroEntity entity) throws DAOException {
		return delete(entity.getIdParametro());
	}
	
	@Override
	public int delete(long idParametro) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idParametro: " + idParametro);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_parametro", idParametro);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: " + e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}
	
	@Override
	public int update(ParametroEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapParametroEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: " + e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO");
		}
	}

	private MapSqlParameterSource mapParametroEntityParameters(ParametroEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_parametro", entity.getIdParametro());
		params.addValue("codice_componente", entity.getCodiceComponente());
		params.addValue("codice_parametro", entity.getCodiceParametro());
		params.addValue("valore", entity.getValore());
		return params;
	}

}
