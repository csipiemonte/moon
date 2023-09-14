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

import it.csi.moon.commons.entity.FunzioneEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.FunzioneDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle funzioni
 * 
 * @see FunzioneEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class FunzioneDAOImpl extends JdbcTemplateDAO implements FunzioneDAO {
	
	private static final String CLASS_NAME = "FunzioneDAOImpl";

	private static final  String SELECT_FIELDS = "SELECT id_funzione, codice_funzione, nome_funzione, descrizione_funzione," +
		" fl_attivo, data_upd, attore_upd";

	private static final  String FIND_BY_ID = SELECT_FIELDS +
		" FROM moon_fo_d_funzione" +
		" WHERE id_funzione = :id_funzione";
	
	private static final  String FIND_BY_CD = SELECT_FIELDS +
		" FROM moon_fo_d_funzione" +
		" WHERE codice_funzione = :codice_funzione";
	
	private static final String INSERT = "INSERT INTO moon_fo_d_funzione(" + 
		" id_funzione, codice_funzione, nome_funzione, descrizione_funzione, fl_attivo, data_upd, attore_upd)" + 
		" VALUES (:id_funzione, :codice_funzione, :nome_funzione, :descrizione_funzione, :fl_attivo, :data_upd, :attore_upd)";
	
	private static final String ELENCO = SELECT_FIELDS +
		" FROM moon_fo_d_funzione"; 
	
	private static final  String UPDATE = "UPDATE moon_fo_d_funzione" +
		" SET codice_funzione=:codice_funzione, nome_funzione=:nome_funzione, descrizione_funzione=:descrizione_funzione, fl_attivo=:fl_attivo, data_upd=:data_upd, attore_upd=:attore_upd" +
		" WHERE id_funzione = :id_funzione";
	
	private static final String DELETE = "DELETE FROM moon_fo_d_funzione WHERE id_funzione = :id_funzione";
	
	private static final String SEQ_ID = "SELECT nextval('moon_fo_d_funzione_id_funzione_seq')";
	
	@Override
	public FunzioneEntity findById(Integer idFunzione) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_funzione", idFunzione);
			return (FunzioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(FunzioneEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public FunzioneEntity findByCd(String codiceFunzione) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_funzione", codiceFunzione);
			return (FunzioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(FunzioneEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCd] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCd] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<FunzioneEntity> find() throws DAOException {
		return (List<FunzioneEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(FunzioneEntity.class));
	}
	
	public Integer insert(FunzioneEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Integer idFunzione = getCustomNamedParameterJdbcTemplateImpl().queryForInt(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdFunzione(idFunzione);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapFunzioneEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idFunzione;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
		
	}
	
	@Override
	public int update(FunzioneEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapFunzioneEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO FUNZIONE");
		}
	}
	
	@Override
	public int delete(Integer idFunzione) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idFunzione: "+idFunzione);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_funzione", idFunzione);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}
	
	@Override
	public int delete(FunzioneEntity entity) throws DAOException {
		return delete(entity.getIdFunzione());
	}

	private MapSqlParameterSource mapFunzioneEntityParameters(FunzioneEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_funzione", entity.getIdFunzione());
    	params.addValue("codice_funzione", entity.getCodiceFunzione());
    	params.addValue("nome_funzione", entity.getNomeFunzione());
    	params.addValue("descrizione_funzione", entity.getDescrizioneFunzione());
    	params.addValue("fl_attivo", entity.getFlAttivo());
    	params.addValue("data_upd", entity.getDataUpd(), Types.DATE);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	
    	return params;
    }
}


