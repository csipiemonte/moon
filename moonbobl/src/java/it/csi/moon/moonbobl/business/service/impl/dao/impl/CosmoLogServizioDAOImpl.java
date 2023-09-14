/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.CosmoLogServizioDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.CosmoLogServizioEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;



/**
 * DAO per l'accesso alla tabella dei log delle pratiche che devono essere create su COSMO
 * <br>
 * <br>Tabella moon_cs_t_log_servizio
 * <br>PK: idLogServizio
 * <br>
 * <br>Tabella principale : moon_cs_t_log_servizio
 * 
 * @see CosmoLogServizioEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 12/01/2022 - versione iniziale
 */
@Component
public class CosmoLogServizioDAOImpl extends JdbcTemplateDAO implements CosmoLogServizioDAO {
	
	private static final String CLASS_NAME = "CosmoServizioDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_log_servizio, id_log_pratica, id_pratica, id_istanza, id_modulo, servizio, data_ins, data_upd, richiesta, risposta, errore";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
		" FROM moon_cs_t_log_servizio" +
		" WHERE id_log_servizio = :id_log_servizio";
	
	private static final  String FIND_BY_ID_ISTANZA  = FIND_SELECT_FIELDS +
		" FROM moon_cs_t_log_servizio" +
		" WHERE id_istanza = :id_istanza order by 1 asc";
	
	private static final  String FIND_BY_ID_ISTANZA_SERVIZIO  = FIND_SELECT_FIELDS +
			" FROM moon_cs_t_log_servizio" +
			" WHERE id_istanza = :id_istanza"+
			" AND servizio = :servizio";
	
	private static final  String FIND_BY_ID_SERVIZIO  = FIND_SELECT_FIELDS +
		" FROM moon_cs_t_log_servizio" +
		" WHERE id_servizio = :id_servizio";
	
	
	private static final  String INSERT = "INSERT INTO moon_cs_t_log_servizio(id_log_servizio, id_log_pratica, id_pratica, id_istanza, id_modulo, servizio, data_ins, data_upd, richiesta, risposta, errore)" + 
		" VALUES (:id_log_servizio, :id_log_pratica, :id_pratica, :id_istanza, :id_modulo, :servizio, :data_ins, :data_upd, :richiesta, :risposta, :errore)";
	
	private static final  String UPDATE = "UPDATE moon_cs_t_log_servizio" +
		" SET id_log_pratica=:id_log_pratica, id_pratica=:id_pratica, id_istanza=:id_istanza, id_modulo=:id_modulo, servizio=:servizio, data_ins=:data_ins, data_upd=:data_upd, richiesta=:richiesta, risposta=:risposta, errore=:errore" +
		" WHERE id_log_servizio = :id_log_servizio";
	
	private static final String SEQ_ID = "SELECT nextval('moon_cs_t_log_servizio_id_log_servizio_seq')";
	

	@Override
	public CosmoLogServizioEntity findById(Long idLogServizio) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_log_servizio", idLogServizio);
			return (CosmoLogServizioEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(CosmoLogServizioEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID LOG SERVIZIO COSMO");
		}
	}
	
	@Override
	public List<CosmoLogServizioEntity> findByIdServizio(String idServizio) {
		try {
			List<CosmoLogServizioEntity> result = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_Servizio", idServizio);
			result = (List<CosmoLogServizioEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_SERVIZIO, params, BeanPropertyRowMapper.newInstance(CosmoLogServizioEntity.class));
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdServizio] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_BY_ID_SERVIZIO LOG Servizio COSMO");
		}
	}
	
	@Override
	public List<CosmoLogServizioEntity> findByIdIstanza(Long idIstanza) {
		try {
			List<CosmoLogServizioEntity> result = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			result = (List<CosmoLogServizioEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(CosmoLogServizioEntity.class));
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID_ISTANZA LOG Servizio COSMO");
		}
	}
	
	@Override
	public CosmoLogServizioEntity findByIdIstanzaServizio(Long idIstanza, String servizio) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("servizio", servizio);
			return (CosmoLogServizioEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_ISTANZA_SERVIZIO, params, BeanPropertyRowMapper.newInstance(CosmoLogServizioEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIstanzaServizio] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIstanzaServizio] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_BY_ID_ISTANZA_SERVIZIO LOG Servizio COSMO");
		}
	}
	
	@Override
	public Long insert(CosmoLogServizioEntity entity) {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdLogServizio(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT LOG Servizio COSMO");
		}
	}
	
	@Override
	public int update(CosmoLogServizioEntity entity) {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO LOG Servizio COSMO");
		}
	}
	
    private MapSqlParameterSource mapEntityParameters(CosmoLogServizioEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_log_servizio", entity.getIdLogServizio());
    	params.addValue("id_log_pratica", entity.getIdLogPratica());
    	params.addValue("id_pratica", entity.getIdPratica(), Types.VARCHAR);
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("servizio", entity.getServizio(), Types.VARCHAR);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("richiesta" , entity.getRichiesta(), Types.VARCHAR);
    	params.addValue("risposta", entity.getRisposta(), Types.VARCHAR);
    	params.addValue("errore", entity.getErrore(), Types.VARCHAR);
    	return params;
    }

}
