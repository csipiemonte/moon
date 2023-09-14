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

import it.csi.moon.commons.entity.EpayRicevutaTelematicaTestataEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.EpayRicevutaTelematicaTestataDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella delle testate di ricevuta telematica
 * <br>
 * <br>Tabella moon_ep_t_ricevuta_telem_testa
 * <br>PK: idRicevutaTelemTesta
 * <br>
 * 
 * @see EpayRicevutaTelematicaTestataEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 26/11/2021 - versione iniziale
 */
@Component
public class EpayRicevutaTelematicaTestataDAOImpl extends JdbcTemplateDAO implements EpayRicevutaTelematicaTestataDAO {
	
	private static final String CLASS_NAME = "EpayRicevutaTelematicaTestataDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_ricevuta_telem_testa, id_messaggio, cf_ente_creditore, codice_versamento, numero_rt, data_ins";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
		" FROM moon_ep_t_ricevuta_telem_testa" +
		" WHERE id_ricevuta_telem_testa = :id_ricevuta_telem_testa";
	
	private static final  String INSERT = "INSERT INTO moon_ep_t_ricevuta_telem_testa(id_ricevuta_telem_testa, id_messaggio, cf_ente_creditore, codice_versamento, numero_rt, data_ins)" + 
		" VALUES (:id_ricevuta_telem_testa, :id_messaggio, :cf_ente_creditore, :codice_versamento, :numero_rt, :data_ins)";
	
	private static final  String UPDATE = "UPDATE moon_ep_t_ricevuta_telem_testa" +
		" SET id_messaggio=:id_messaggio, cf_ente_creditore=:cf_ente_creditore, codice_versamento=:codice_versamento, numero_rt=:numero_rt, data_ins=:data_ins" +
		" WHERE id_ricevuta_telem_testa = :id_ricevuta_telem_testa";
	
	private static final String SEQ_ID = "SELECT nextval('moon_ep_t_ricevuta_telem_testa_id_ricevuta_telem_testa_seq')";
	

	@Override
	public EpayRicevutaTelematicaTestataEntity findById(Long idRicevutaTelematica) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ricevuta_telem_testa", idRicevutaTelematica);
			return (EpayRicevutaTelematicaTestataEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(EpayRicevutaTelematicaTestataEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public Long insert(EpayRicevutaTelematicaTestataEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdRicevutaTelemTestata(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public int update(EpayRicevutaTelematicaTestataEntity entity) {
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
	
    private MapSqlParameterSource mapEntityParameters(EpayRicevutaTelematicaTestataEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_ricevuta_telem_testa", entity.getIdRicevutaTelemTestata());
    	params.addValue("id_messaggio", entity.getIdMessaggio(), Types.VARCHAR);
    	params.addValue("cf_ente_creditore", entity.getCfEnteCreditore(), Types.VARCHAR);
    	params.addValue("codice_versamento", entity.getCodiceVersamento(), Types.VARCHAR);
    	params.addValue("numero_rt", entity.getNumeroRt(), Types.SMALLINT);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	return params;
    }

}
