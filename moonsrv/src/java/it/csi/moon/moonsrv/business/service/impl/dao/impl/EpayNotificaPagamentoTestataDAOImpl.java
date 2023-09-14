/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.EpayNotificaPagamentoTestataEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayNotificaPagamentoTestataDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella delle testata di notifica di pagamento
 * <br>
 * <br>Tabella moon_ep_t_notifica_pagam_testa
 * <br>PK: idNotificaPagamTestata
 * <br>
 * 
 * @see EpayNotificaPagamentoTestataEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 26/11/2021 - versione iniziale
 */
@Component
public class EpayNotificaPagamentoTestataDAOImpl extends JdbcTemplateDAO implements EpayNotificaPagamentoTestataDAO {
	
	private static final String CLASS_NAME = "EpayNotificaPagamentoTestataDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_notifica_pagam_testa, id_messaggio, cf_ente_creditore, codice_versamento, pagamenti_spontanei, numero_pagamenti, importo_totale_pagamenti, data_ins";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
		" FROM moon_ep_t_notifica_pagam_testa" +
		" WHERE id_notifica_pagam_testa = :id_notifica_pagam_testa";
	
	private static final  String INSERT = "INSERT INTO moon_ep_t_notifica_pagam_testa(id_notifica_pagam_testa, id_messaggio, cf_ente_creditore, codice_versamento, pagamenti_spontanei, numero_pagamenti, importo_totale_pagamenti, data_ins)" + 
		" VALUES (:id_notifica_pagam_testa, :id_messaggio, :cf_ente_creditore, :codice_versamento, :pagamenti_spontanei, :numero_pagamenti, :importo_totale_pagamenti, :data_ins)";
	
	private static final  String UPDATE = "UPDATE moon_ep_t_notifica_pagam_testa" +
		" SET id_messaggio=:id_messaggio, cf_ente_creditore=:cf_ente_creditore, codice_versamento=:codice_versamento, pagamenti_spontanei=:pagamenti_spontanei, numero_pagamenti=:numero_pagamenti, importo_totale_pagamenti=:importo_totale_pagamenti, data_ins=:data_ins" +
		" WHERE id_notifica_pagam_testa = :id_notifica_pagam_testa";
	
	private static final String SEQ_ID = "SELECT nextval('moon_ep_t_notifica_pagam_testa_id_notifica_pagam_testa_seq')";
	

	@Override
	public EpayNotificaPagamentoTestataEntity findById(Long idNotificaPagamTestata) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_notifica_pagam_testa", idNotificaPagamTestata);
			return (EpayNotificaPagamentoTestataEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(EpayNotificaPagamentoTestataEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FIND_ID EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public Long insert(EpayNotificaPagamentoTestataEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdNotificaPagamTestata(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE INSERT EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public int update(EpayNotificaPagamentoTestataEntity entity) throws DAOException {
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
	
    private MapSqlParameterSource mapEntityParameters(EpayNotificaPagamentoTestataEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_notifica_pagam_testa", entity.getIdNotificaPagamTestata());
    	params.addValue("id_messaggio", entity.getIdMessaggio(), Types.VARCHAR);
    	params.addValue("cf_ente_creditore", entity.getCfEnteCreditore(), Types.VARCHAR);
    	params.addValue("codice_versamento", entity.getCodiceVersamento(), Types.VARCHAR);
    	params.addValue("pagamenti_spontanei", entity.getPagamentiSpontanei(), Types.VARCHAR);
    	params.addValue("numero_pagamenti", entity.getNumeroPagamenti(), Types.SMALLINT);
    	params.addValue("importo_totale_pagamenti", entity.getImportoTotalePagamenti(), Types.DECIMAL);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	return params;
    }

}
