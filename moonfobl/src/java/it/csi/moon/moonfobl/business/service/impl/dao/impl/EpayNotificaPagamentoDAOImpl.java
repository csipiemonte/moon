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

import it.csi.moon.commons.entity.EpayNotificaPagamentoEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.EpayNotificaPagamentoDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella delle notifiche di pagamento
 * <br>
 * <br>Tabella moon_ep_t_notifica_pagamento
 * <br>PK: idNotificaPagamento
 * <br>
 * 
 * @see EpayNotificaPagamentoEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 26/11/2021 - versione iniziale
 */
@Component
public class EpayNotificaPagamentoDAOImpl extends JdbcTemplateDAO implements EpayNotificaPagamentoDAO {
	
	private static final String CLASS_NAME = "EpayNotificaPagamentoTestataDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_notifica_pagamento, id_notifica_pagam_testa, id_posizione_debitoria, anno_di_riferimento, iuv, importo_pagato, data_scadenza, desc_causale_versamento, data_esito_pagamento, soggetto_debitore, soggetto_versante, dati_transazione_psp, dati_specifici_riscossione, note, codice_avviso, data_ins";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
		" FROM moon_ep_t_notifica_pagamento" +
		" WHERE id_notifica_pagamento = :id_notifica_pagamento";

	private static final  String FIND_BY_ID_TESTATA  = FIND_SELECT_FIELDS +
		" FROM moon_ep_t_notifica_pagamento" +
		" WHERE id_notifica_pagam_testa = :id_notifica_pagam_testa";
	
	private static final  String INSERT = "INSERT INTO moon_ep_t_notifica_pagamento(id_notifica_pagamento, id_notifica_pagam_testa, id_posizione_debitoria, anno_di_riferimento, iuv, importo_pagato, data_scadenza, desc_causale_versamento, data_esito_pagamento, soggetto_debitore, soggetto_versante, dati_transazione_psp, dati_specifici_riscossione, note, codice_avviso, data_ins)" + 
		" VALUES (:id_notifica_pagamento, :id_notifica_pagam_testa, :id_posizione_debitoria, :anno_di_riferimento, :iuv, :importo_pagato, :data_scadenza, :desc_causale_versamento, :data_esito_pagamento, :soggetto_debitore, :soggetto_versante, :dati_transazione_psp, :dati_specifici_riscossione, :note, :codice_avviso, :data_ins)";
	
	private static final  String UPDATE = "UPDATE moon_ep_t_notifica_pagamento" +
		" SET id_notifica_pagamento=:id_notifica_pagamento, id_notifica_pagam_testa=:id_notifica_pagam_testa, id_posizione_debitoria=:id_posizione_debitoria, anno_di_riferimento=:anno_di_riferimento, iuv=:iuv, importo_pagato=:importo_pagato, data_scadenza=:data_scadenza, desc_causale_versamento=:desc_causale_versamento, data_esito_pagamento=:data_esito_pagamento, soggetto_debitore=:soggetto_debitore, soggetto_versante=:soggetto_versante, dati_transazione_psp=:dati_transazione_psp, dati_specifici_riscossione=:dati_specifici_riscossione, note=:note, codice_avviso=:codice_avviso, data_ins=:data_ins" +
		" WHERE id_notifica_pagamento = :id_notifica_pagamento";
	
	private static final String SEQ_ID = "SELECT nextval('moon_ep_t_notifica_pagamento_id_notifica_pagamento_seq')";
	

	@Override
	public EpayNotificaPagamentoEntity findById(Long idNotificaPagamento) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_notifica_pagamento", idNotificaPagamento);
			return (EpayNotificaPagamentoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(EpayNotificaPagamentoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public List<EpayNotificaPagamentoEntity> findByIdTestata(Long idTestata) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_notifica_pagam_testata", idTestata);
			return (List<EpayNotificaPagamentoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_TESTATA, params, BeanPropertyRowMapper.newInstance(EpayNotificaPagamentoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_BY_ID_TESTATA EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public Long insert(EpayNotificaPagamentoEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdNotificaPagamento(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public int update(EpayNotificaPagamentoEntity entity) {
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
	
    private MapSqlParameterSource mapEntityParameters(EpayNotificaPagamentoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_notifica_pagamento", entity.getIdNotificaPagamento());
    	params.addValue("id_notifica_pagam_testa", entity.getIdNotificaPagamTesta());
    	params.addValue("id_posizione_debitoria", entity.getIdPosizioneDebitoria(), Types.VARCHAR);
    	params.addValue("anno_di_riferimento", entity.getAnnoDiRiferimento(), Types.SMALLINT);
    	params.addValue("iuv", entity.getIuv(), Types.VARCHAR);
    	params.addValue("importo_pagato", entity.getImportoPagato(), Types.DECIMAL);
    	params.addValue("data_scadenza", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("desc_causale_versamento", entity.getDescCausaleVersamento(), Types.VARCHAR);
    	params.addValue("data_esito_pagamento" , entity.getDataEsitoPagamento(), Types.TIMESTAMP);
    	params.addValue("soggetto_debitore", entity.getSoggettoDebitore(), Types.VARCHAR);
    	params.addValue("soggetto_versante" , entity.getSoggettoVersante(), Types.VARCHAR);
    	params.addValue("dati_transazione_psp", entity.getDatiTransazionePsp(), Types.VARCHAR);
    	params.addValue("dati_specifici_riscossione", entity.getDatiSpecificiRiscossione(), Types.VARCHAR);
    	params.addValue("note", entity.getNote(), Types.VARCHAR);
    	params.addValue("codice_avviso", entity.getCodiceAvviso(), Types.VARCHAR);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	return params;
    }

}
