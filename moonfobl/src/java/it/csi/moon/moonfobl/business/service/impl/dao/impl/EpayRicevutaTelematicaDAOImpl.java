/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.EpayRicevutaTelematicaEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.EpayRicevutaTelematicaDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle ricevute telematiche
 * <br>
 * <br>Tabella moon_ep_t_ricevuta_telematica
 * <br>PK: idRicevutaTelematica
 * <br>
 * 
 * @see EpayRicevutaTelematicaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 26/11/2021 - versione iniziale
 */
@Component
public class EpayRicevutaTelematicaDAOImpl extends JdbcTemplateDAO implements EpayRicevutaTelematicaDAO {
	
	private static final String CLASS_NAME = "EpayRicevutaTelematicaDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_ricevuta_telematica, id_ricevuta_telem_testa, id, xml, data_ins";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
		" FROM moon_ep_t_ricevuta_telematica" +
		" WHERE id_ricevuta_telematica = :id_ricevuta_telematica";

	private static final  String FIND_BY_ID_TESTATA  = FIND_SELECT_FIELDS +
		" FROM moon_ep_t_ricevuta_telematica" +
		" WHERE id_notifica_pagam_testa = :id_notifica_pagam_testa";
	
	private static final  String INSERT = "INSERT INTO moon_ep_t_ricevuta_telematica(id_ricevuta_telematica, id_ricevuta_telem_testa, id, xml, data_ins)" + 
		" VALUES (:id_ricevuta_telematica, :id_ricevuta_telem_testa, :id, :xml, :data_ins)";
	
	private static final  String UPDATE = "UPDATE moon_ep_t_ricevuta_telematica" +
		" SET id_ricevuta_telem_testa=:id_ricevuta_telem_testa, id=:id, xml=:xml, data_ins=:data_ins" +
		" WHERE id_ricevuta_telematica = :id_ricevuta_telematica";
	
	private static final String SEQ_ID = "SELECT nextval('moon_ep_t_ricevuta_telematica_id_ricevuta_telematica_seq')";
	

	@Override
	public EpayRicevutaTelematicaEntity findById(Long idRicevutaTelematica) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ricevuta_telematica", idRicevutaTelematica);
			return (EpayRicevutaTelematicaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(EpayRicevutaTelematicaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public List<EpayRicevutaTelematicaEntity> findByIdTestata(Long idTestata) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ricevuta_telem_testa", idTestata);
			return (List<EpayRicevutaTelematicaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_TESTATA, params, BeanPropertyRowMapper.newInstance(EpayRicevutaTelematicaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_BY_ID_TESTATA EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public Long insert(EpayRicevutaTelematicaEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdRicevutaTelemematica(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public int update(EpayRicevutaTelematicaEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO EPAY", "MOONSRV-30800");
		}
	}
	
    private MapSqlParameterSource mapEntityParameters(EpayRicevutaTelematicaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_ricevuta_telematica", entity.getIdRicevutaTelemematica());
    	params.addValue("id_ricevuta_telem_testa", entity.getIdRicevutaTelemTestata());
    	params.addValue("id", entity.getId(), Types.VARCHAR);
    	params.addValue("xml", entity.getXml(), Types.VARCHAR);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	return params;
    }

}
