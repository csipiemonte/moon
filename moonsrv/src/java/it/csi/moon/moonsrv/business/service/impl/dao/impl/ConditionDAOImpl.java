/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ConditionEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.ConditionDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle condition
 * <br>
 * <br>Tabella principale : moon_wf_t_condition
 * <br>PK : idCondition
 * <br>AK : codice_condition
 * 
 * @see ConditionEntity
 * 
 * @author Laurent
 * 
 * @since 2.9.2
 */
@Component
public class ConditionDAOImpl extends JdbcTemplateDAO implements ConditionDAO {
	
	private static final String CLASS_NAME = "ConditionDAOImpl";

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final  String SELECT_FULL_FILEDS = "SELECT id_condition, codice_condition, desc_condition, codice_engine, script";
	
	private static final  String FIND_BY_ID = SELECT_FULL_FILEDS +
			" FROM moon_wf_t_condition" +
			" WHERE id_condition = :id_condition";
	private static final  String FIND_BY_CD = SELECT_FULL_FILEDS +
			" FROM moon_wf_t_condition" +
			" WHERE codice_condition = :codice_condition";

	
	private static final String INSERT = "INSERT INTO moon_wf_t_condition (codice_condition, desc_condition, codice_engine, script)" + 
			" VALUES (:codice_condition, :desc_condition, :codice_engine, :script)";
	
	private static final String UPDATE = "UPDATE moon_wf_t_condition" + 
			" SET codice_condition=:codice_condition, desc_condition=:desc_condition, codice_engine=:codice_engine, script=:script" + 
			" WHERE id_condition = :id_condition";
	
	private static final String DELETE = "DELETE FROM moon_wf_t_condition WHERE id_condition = :id_condition";
	
	private static final String SEQ_ID = "SELECT nextval('moon_wf_t_condition_id_condition_seq')";
	
	@Override
	public ConditionEntity findById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_condition", id);
			return (ConditionEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ConditionEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ConditionEntity findByCodice(String codice) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodice] IN codice: " + codice);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_condition", codice);
			return (ConditionEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(ConditionEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	

	@Override
	public Long insert(ConditionEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);
			Long idCondition = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdCondition(idCondition);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapConditionEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idCondition;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE INSERIMENTO CONDITION");
		}
	}

	@Override
	public int delete(Long id) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN id: " + id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_allegato", id);
			//return (ModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(findModuloByID, params, new ModuloRowMapper());
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE DELETE CONDITION");
		}
	}

	@Override
	public int update(ConditionEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapConditionEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO CONDITION");
		}
	}


    private MapSqlParameterSource mapConditionEntityParameters(ConditionEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_condition", entity.getIdCondition(), Types.BIGINT);
    	params.addValue("codice_condition" , entity.getCodiceCondition(), Types.VARCHAR);
    	params.addValue("desc_condition", entity.getDescCondition(), Types.VARCHAR);
    	params.addValue("codice_engine", entity.getCodiceEngine(), Types.VARCHAR);
    	params.addValue("script", entity.getScript(), Types.VARCHAR);
    	return params;
    }
	
}
