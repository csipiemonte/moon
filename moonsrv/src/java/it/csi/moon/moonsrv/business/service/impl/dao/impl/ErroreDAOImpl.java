/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ErroreEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.ErroreDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

@Component
public class ErroreDAOImpl extends JdbcTemplateDAO implements ErroreDAO {
	
	private static final String CLASS_NAME = "ErroreDAOImpl";
	
	private static final  String SELECT_FIELDS = "SELECT uuid, id_componente, inet_adress, data_ins, attore_ins, codice, message, class_name, method_name, id_istanza, id_modulo, ex_class_name, ex_message, ex_cause, ex_trace, info, elapsed_time_ms, note";
	private static final  String FIND_BY_UUID = SELECT_FIELDS +
		" FROM moon_l_errori" +
		" WHERE uuid = :uuid";
	
	private static final String INSERT = "INSERT INTO moon_l_errori" + 
		" (uuid, id_componente, inet_adress, data_ins, attore_ins, codice, message, class_name, method_name, id_istanza, id_modulo, ex_class_name, ex_message, ex_cause, ex_trace, info, elapsed_time_ms, note)" + 
		" VALUES (:uuid, :id_componente, :inet_adress, :data_ins, :attore_ins, :codice, :message, :class_name, :method_name, :id_istanza, :id_modulo, :ex_class_name, :ex_message, :ex_cause, :ex_trace, :info, :elapsed_time_ms, :note)";


	@Override
	public ErroreEntity findById(String uuid) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("uuid", uuid);
			return (ErroreEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_UUID, params, BeanPropertyRowMapper.newInstance(ErroreEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	@Override
	public void insert(ErroreEntity entity) throws DAOException {
		try {
//			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapErroreEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}


	private MapSqlParameterSource mapErroreEntityParameters(ErroreEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("uuid", entity.getUuid(), Types.VARCHAR);
    	params.addValue("id_componente", entity.getIdComponente());
    	params.addValue("inet_adress", StringUtils.left(entity.getInetAdress(),255), Types.VARCHAR);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("attore_ins", StringUtils.left(entity.getAttoreIns(),16), Types.VARCHAR);
    	params.addValue("codice", StringUtils.left(entity.getCodice(),50), Types.VARCHAR);
    	params.addValue("message", StringUtils.left(entity.getMessage(),255), Types.VARCHAR);
    	params.addValue("class_name", StringUtils.left(entity.getClassName(),50), Types.VARCHAR);
    	params.addValue("method_name", StringUtils.left(entity.getMethodName(),50), Types.VARCHAR);
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("ex_class_name", StringUtils.left(entity.getExClassName(),255), Types.VARCHAR);
    	params.addValue("ex_message", entity.getExMessage(), Types.VARCHAR);
    	params.addValue("ex_cause", entity.getExCause(), Types.VARCHAR);
    	params.addValue("ex_trace", entity.getExTrace(), Types.VARCHAR);
    	params.addValue("info", entity.getInfo(), Types.VARCHAR);
    	params.addValue("elapsed_time_ms", entity.getElapsedTimeMs());
    	params.addValue("note", StringUtils.left(entity.getNote(),50), Types.VARCHAR);
    	return params;
    }

}


