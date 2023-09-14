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

import it.csi.moon.moonbobl.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.EpayRichiestaEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tabella delle richieste di pagamenti EPAY
 * <br>
 * <br>Tabella moon_ep_t_richiesta
 * <br>PK: idLogEpay
 * <br>AK: idEpay identificativo di business inviato nella richiesta CODICEISTANZA_TIMESTAMP_LINEACLIENTE
 * <br>
 * <br>Tabella principale : moon_ep_t_richiesta
 * 
 * @see EpayRichiestaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 22/11/2021 - versione iniziale
 */
@Component
public class EpayRichiestaDAOImpl extends JdbcTemplateDAO implements EpayRichiestaDAO {
	
	private static final String CLASS_NAME = "EpayRichiestaDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_richiesta, id_istanza, id_modulo, id_tipologia_epay, id_storico_workflow, id_epay, iuv, codice_avviso, data_ins, data_del, attore_ins, attore_del, richiesta, risposta, codice_esito, desc_esito, id_notifica_pagamento, data_notifica_pagamento, id_ricevuta_telematica_positiva, data_ricevuta_telematica_positiva";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
		" FROM moon_ep_t_richiesta" +
		" WHERE id_richiesta = :id_richiesta";
	
	private static final  String FIND_BY_ID_ISTANZA  = FIND_SELECT_FIELDS +
		" FROM moon_ep_t_richiesta" +
		" WHERE id_istanza = :id_istanza";
	
	private static final  String INSERT = "INSERT INTO moon_ep_t_richiesta(id_richiesta, id_istanza, id_modulo, id_tipologia_epay, id_storico_workflow, id_epay, iuv, codice_avviso, data_ins, data_del, attore_ins, attore_del, richiesta, risposta, codice_esito, desc_esito, id_notifica_pagamento, data_notifica_pagamento, id_ricevuta_telematica_positiva, data_ricevuta_telematica_positiva)" + 
		" VALUES (:id_richiesta, :id_istanza, :id_modulo, :id_tipologia_epay, :id_storico_workflow, :id_epay, :iuv, :codice_avviso, :data_ins, :data_del, :attore_ins, :attore_del, :richiesta, :risposta, :codice_esito, :desc_esito, :id_notifica_pagamento, :data_notifica_pagamento, :id_ricevuta_telematica_positiva, :data_ricevuta_telematica_positiva)";
	
	private static final  String UPDATE = "UPDATE moon_ep_t_richiesta" +
		" SET id_istanza=:id_istanza, id_modulo=:id_modulo, id_tipologia_epay=:id_tipologia_epay, id_storico_workflow=:id_storico_workflow, id_epay=:id_epay, iuv=:iuv, codice_avviso=:codice_avviso, data_ins=:data_ins, data_del=:data_del, attore_ins=:attore_ins, attore_del=:attore_del, richiesta=:richiesta, risposta=:risposta, codice_esito=:codice_esito, desc_esito=:desc_esito" +
		", id_notifica_pagamento=:id_notifica_pagamento, data_notifica_pagamento=:data_notifica_pagamento, id_ricevuta_telematica_positiva=:id_ricevuta_telematica_positiva, data_ricevuta_telematica_positiva=:data_ricevuta_telematica_positiva" +
		" WHERE id_richiesta = :id_richiesta";
	
	private static final  String UPDATE_ID_STORICO_WF = "UPDATE moon_ep_t_richiesta" +
		" SET id_storico_workflow=:id_storico_workflow" +
		" WHERE id_richiesta = :id_richiesta";
	
	private static final String SEQ_ID = "SELECT nextval('moon_ep_t_richiesta_id_richiesta_seq')";
	

	@Override
	public EpayRichiestaEntity findById(Long idRichiesta) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_richiesta", idRichiesta);
			return (EpayRichiestaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(EpayRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public EpayRichiestaEntity findLastValideByIdIstanza(Long idIstanza) {
		List<EpayRichiestaEntity> richiesteE = findByIdIstanza(idIstanza);
		if (richiesteE == null || richiesteE.isEmpty()) {
			throw new BusinessException("Impossibile richiedere il pagamento, Richiesta non trovata sull'istanza","MOONSRV-30822");
		}
		return richiesteE.get(richiesteE.size()-1);
	}
	
	@Override
	public List<EpayRichiestaEntity> findByIdIstanza(Long idIstanza) {
		try {
			List<EpayRichiestaEntity> result = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			result = (List<EpayRichiestaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(EpayRichiestaEntity.class));
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID_ISTANZA EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public Long insert(EpayRichiestaEntity entity) {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdRichiesta(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public int update(EpayRichiestaEntity entity) {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public int updateIdStoricoWF(Long idRichiesta, Long idStoricoWF) {
		try {
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWF] IN idRichiesta: "+idRichiesta);
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWF] IN idStoricoWF: "+idStoricoWF);
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("id_richiesta", idRichiesta);
	    	params.addValue("id_storico_workflow", idStoricoWF);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ID_STORICO_WF, params);
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWF] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateIdStoricoWF] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO EPAY UPDATE_ID_STORICO_WF", "MOONSRV-30800");
		}
	}
	
    private MapSqlParameterSource mapEntityParameters(EpayRichiestaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_richiesta", entity.getIdRichiesta());
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("id_tipologia_epay", entity.getIdTipologiaEpay(), Types.INTEGER);
    	params.addValue("id_storico_workflow", entity.getIdStoricoWorkflow());
    	params.addValue("id_epay", entity.getIdEpay(), Types.VARCHAR);
    	params.addValue("iuv", entity.getIuv(), Types.VARCHAR);
    	params.addValue("codice_avviso", entity.getCodiceAvviso(), Types.VARCHAR);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("data_del", entity.getDataDel(), Types.TIMESTAMP);
    	params.addValue("attore_ins" , entity.getAttoreIns(), Types.VARCHAR);
    	params.addValue("attore_del", entity.getAttoreDel(), Types.VARCHAR);
    	params.addValue("richiesta" , entity.getRichiesta(), Types.VARCHAR);
    	params.addValue("risposta", entity.getRisposta(), Types.VARCHAR);
    	params.addValue("codice_esito", entity.getCodiceEsito(), Types.VARCHAR);
    	params.addValue("desc_esito", entity.getDescEsito(), Types.VARCHAR);
    	params.addValue("id_ricevuta_telematica_positiva", entity.getIdRicevutaTelematicaPositiva());
    	params.addValue("data_ricevuta_telematica_positiva", entity.getDataRicevutaTelematicaPositiva(), Types.TIMESTAMP);
    	params.addValue("id_notifica_pagamento", entity.getIdNotificaPagamento());
    	params.addValue("data_notifica_pagamento", entity.getDataNotificaPagamento(), Types.TIMESTAMP);
    	return params;
    }

}
