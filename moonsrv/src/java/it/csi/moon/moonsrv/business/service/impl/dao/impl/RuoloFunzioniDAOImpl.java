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

import it.csi.moon.commons.entity.RuoloFunzioniEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.RuoloFunzioniDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai ruoli delle funzioni
 * 
 * @see RuoloFunzioniEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class RuoloFunzioniDAOImpl extends JdbcTemplateDAO implements RuoloFunzioniDAO {
	private static final String CLASS_NAME = "RuoloFunzioniDAOImpl";
	
	private static final  String FIND_BY_ID_RUOLO  = "SELECT id_ruolo, id_funzione, data_upd, attore_upd" +
			" FROM moon_fo_r_ruolo_funzioni" +
			" WHERE id_ruolo = :id_ruolo and id_funzione = :id_funzione";

	private static final String ELENCO = "SELECT id_ruolo, id_funzione, data_upd, attore_upd" +
			" FROM moon_fo_r_ruolo_funzioni" ;

	private static final String INSERT = "INSERT INTO moon_fo_r_ruolo_funzioni(" + 
			" id_ruolo, id_funzione, data_upd, attore_upd)" + 
			" VALUES (:id_ruolo, :id_funzione, :data_upd, :attore_upd)";

	private static final String DELETE = "DELETE FROM moon_fo_r_ruolo_funzioni WHERE id_ruolo = :id_ruolo AND id_funzione = :id_funzione";

	private static final String DELETE_ALL_BY_ID_RUOLO = "DELETE FROM moon_fo_r_ruolo_funzioni WHERE id_ruolo = :id_ruolo";

	private static final String SEQ_ID = "SELECT nextval('moon_fo_d_funzione_id_funzione_seq')";


	@Override
	public List<RuoloFunzioniEntity> findByIdRuolo(Integer idRuolo) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ruolo", idRuolo);
			return (List<RuoloFunzioniEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_RUOLO, params, BeanPropertyRowMapper.newInstance(RuoloFunzioniEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdRuolo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdRuolo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<RuoloFunzioniEntity> find() throws DAOException {
		return ( List<RuoloFunzioniEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(RuoloFunzioniEntity.class));
	}

	@Override
	public int insert(RuoloFunzioniEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Integer idRuolo = getCustomNamedParameterJdbcTemplateImpl().queryForInt(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdRuolo(idRuolo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapRuoloFunzioniEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idRuolo;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}



	@Override
	public int delete(Integer idRuolo, Integer idFunzione) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN id_ruolo: "+idRuolo);
			LOG.debug("[" + CLASS_NAME + "::delete] IN id_funzione: "+idFunzione);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ruolo", idRuolo);
			params.addValue("id_funzione", idFunzione);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public int delete(RuoloFunzioniEntity entity) throws DAOException {
		return delete(entity.getIdRuolo(),entity.getIdFunzione());
	}

	@Override
	public int deleteAllByIdRuolo(Integer idRuolo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN id_ruolo: "+idRuolo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ruolo", idRuolo);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_ALL_BY_ID_RUOLO, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public int insertList(List<RuoloFunzioniEntity> entity) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}


	private MapSqlParameterSource mapRuoloFunzioniEntityParameters(RuoloFunzioniEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_ruolo", entity.getIdRuolo());
		params.addValue("id_funzione" , entity.getIdFunzione());
		params.addValue("data_upd", entity.getDataUpd(), Types.DATE);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		return params;
	}
}
