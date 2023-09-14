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

import it.csi.moon.moonbobl.business.service.impl.dao.CosmoLogPraticaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.CosmoLogPraticaEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella dei log delle pratiche che devono essere create su COSMO
 * <br>
 * <br>Tabella moon_cs_t_log_pratica
 * <br>PK: idLogPratica
 * <br>AK: idPratica
 * <br>AK: idIstanza,idx
 * <br>
 * <br>Tabella principale : moon_cs_t_log_pratica
 * 
 * @see CosmoLogPraticaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 21/04/2021 - versione iniziale
 */
@Component
public class CosmoLogPraticaDAOImpl extends JdbcTemplateDAO implements CosmoLogPraticaDAO {
	
	private static final String CLASS_NAME = "CosmoPraticaDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_log_pratica, id_pratica, id_istanza, idx, id_modulo, data_ins, data_avvio, data_upd, crea_richiesta, crea_risposta, crea_documento_richiesta, crea_documento_risposta, avvia_processo_richiesta, avvia_processo_risposta, errore, pratica";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
		" FROM moon_cs_t_log_pratica" +
		" WHERE id_log_pratica = :id_log_pratica";
	
	private static final  String FIND_BY_ID_ISTANZA  = FIND_SELECT_FIELDS +
		" FROM moon_cs_t_log_pratica" +
		" WHERE id_istanza = :id_istanza";
	
	private static final  String FIND_BY_ID_PRATICA  = FIND_SELECT_FIELDS +
		" FROM moon_cs_t_log_pratica" +
		" WHERE id_pratica = :id_pratica";
	
	private static final  String INSERT = "INSERT INTO moon_cs_t_log_pratica(id_log_pratica, id_pratica, id_istanza, idx, id_modulo, data_ins, data_avvio, data_upd, crea_richiesta, crea_risposta, crea_documento_richiesta, crea_documento_risposta, avvia_processo_richiesta, avvia_processo_risposta, errore, pratica)" + 
		" VALUES (:id_log_pratica, :id_pratica, :id_istanza, :idx, :id_modulo, :data_ins, :data_avvio, :data_upd, :crea_richiesta, :crea_risposta, :crea_documento_richiesta, :crea_documento_risposta, :avvia_processo_richiesta, :avvia_processo_risposta, :errore, :pratica)";
	
	private static final  String UPDATE = "UPDATE moon_cs_t_log_pratica" +
		" SET id_pratica=:id_pratica, id_istanza=:id_istanza, idx=:idx, id_modulo=:id_modulo, data_ins=:data_ins, data_avvio=:data_avvio, data_upd=:data_upd, crea_richiesta=:crea_richiesta, crea_risposta=:crea_risposta, crea_documento_richiesta=:crea_documento_richiesta, crea_documento_risposta=:crea_documento_risposta, avvia_processo_richiesta=:avvia_processo_richiesta, avvia_processo_risposta=:avvia_processo_risposta, errore=:errore, pratica=:pratica" +
		" WHERE id_log_pratica = :id_log_pratica";
	
	private static final String SEQ_ID = "SELECT nextval('moon_cs_t_log_pratica_id_log_pratica_seq')";
	

	@Override
	public CosmoLogPraticaEntity findById(Long idLogPratica) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_log_pratica", idLogPratica);
			return (CosmoLogPraticaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(CosmoLogPraticaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID LOG PRATICA COSMO");
		}
	}
	
	@Override
	public List<CosmoLogPraticaEntity> findByIdPratica(String idPratica) {
		try {
			List<CosmoLogPraticaEntity> result = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_pratica", idPratica);
			result = (List<CosmoLogPraticaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_PRATICA, params, BeanPropertyRowMapper.newInstance(CosmoLogPraticaEntity.class));
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdPratica] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_BY_ID_PRATICA LOG PRATICA COSMO");
		}
	}
	
	@Override
	public List<CosmoLogPraticaEntity> findByIdIstanza(Long idIstanza) {
		try {
			List<CosmoLogPraticaEntity> result = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			result = (List<CosmoLogPraticaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(CosmoLogPraticaEntity.class));
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID_ISTANZA LOG PRATICA COSMO");
		}
	}
	
	@Override
	public Long insert(CosmoLogPraticaEntity entity) {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdLogPratica(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT LOG PRATICA COSMO");
		}
	}
	
	@Override
	public int update(CosmoLogPraticaEntity entity) {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO LOG PRATICA COSMO");
		}
	}
	
    private MapSqlParameterSource mapEntityParameters(CosmoLogPraticaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_log_pratica", entity.getIdLogPratica());
    	params.addValue("id_pratica", entity.getIdPratica(), Types.VARCHAR);
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("idx", entity.getIdx());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("data_avvio", entity.getDataAvvio(), Types.TIMESTAMP);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("crea_richiesta" , entity.getCreaRichiesta(), Types.VARCHAR);
    	params.addValue("crea_risposta", entity.getCreaRisposta(), Types.VARCHAR);
    	params.addValue("crea_documento_richiesta" , entity.getCreaDocumentoRichiesta(), Types.VARCHAR);
    	params.addValue("crea_documento_risposta", entity.getCreaDocumentoRisposta(), Types.VARCHAR);
    	params.addValue("avvia_processo_richiesta" , entity.getAvviaProcessoRichiesta(), Types.VARCHAR);
    	params.addValue("avvia_processo_risposta", entity.getAvviaProcessoRisposta(), Types.VARCHAR);
    	params.addValue("errore", entity.getErrore(), Types.VARCHAR);
    	params.addValue("pratica", entity.getPratica(), Types.VARCHAR);
    	return params;
    }

}
