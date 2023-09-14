/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.EpayRichiestaEntity;
import it.csi.moon.commons.entity.EpayRichiestaFilter;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

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

	private static final String SELECT_FIELDS = "SELECT id_richiesta, id_istanza, id_modulo, id_tipologia_epay, id_storico_workflow, id_epay, iuv, codice_avviso, data_ins, data_del, attore_ins, attore_del, richiesta, risposta, codice_esito, desc_esito, id_notifica_pagamento, data_notifica_pagamento, id_ricevuta_telematica_positiva, data_ricevuta_telematica_positiva";
	private static final String FROM_MOON_EP_T_RICHIESTA = " FROM moon_ep_t_richiesta";
	private static final String WHERE_ID_RICHIESTA_ID_RICHIESTA = " WHERE id_richiesta = :id_richiesta";
	private static final String WHERE_CODICE_AVVISO_CODICE_AVVISO = " WHERE codice_avviso = :codice_avviso";
	private static final String UPDATE_MOON_EP_T_RICHIESTA = "UPDATE moon_ep_t_richiesta";

	private static final String ID_RICHIESTA = "id_richiesta";
	private static final String CODICE_AVVISO = "codice_avviso";
	private static final String ID_ISTANZA = "id_istanza";
	private static final String ID_STORICO_WORKFLOW = "id_storico_workflow";

	private static final String FIND_BY_ID  = SELECT_FIELDS +
		FROM_MOON_EP_T_RICHIESTA +
		WHERE_ID_RICHIESTA_ID_RICHIESTA;
	
	private static final String FIND_BY_ID_EPAY  = SELECT_FIELDS +
		FROM_MOON_EP_T_RICHIESTA +
		" WHERE id_epay = :id_epay";
	
	private static final String FIND_BY_IUV  = SELECT_FIELDS +
			FROM_MOON_EP_T_RICHIESTA +
			" WHERE iuv = :iuv";
	
	private static final String FIND_BY_ID_ISTANZA  = SELECT_FIELDS +
		FROM_MOON_EP_T_RICHIESTA +
		" WHERE id_istanza = :id_istanza";
	
	private static final String FIND_BY_CD_AVVISO  = SELECT_FIELDS +
		FROM_MOON_EP_T_RICHIESTA +
		WHERE_CODICE_AVVISO_CODICE_AVVISO;
	
	private static final String INSERT = "INSERT INTO moon_ep_t_richiesta(id_richiesta, id_istanza, id_modulo, id_tipologia_epay, id_storico_workflow, id_epay, iuv, codice_avviso, data_ins, data_del, attore_ins, attore_del, richiesta, risposta, codice_esito, desc_esito, id_notifica_pagamento, data_notifica_pagamento, id_ricevuta_telematica_positiva, data_ricevuta_telematica_positiva)" + 
		" VALUES (:id_richiesta, :id_istanza, :id_modulo, :id_tipologia_epay, :id_storico_workflow, :id_epay, :iuv, :codice_avviso, :data_ins, :data_del, :attore_ins, :attore_del, :richiesta, :risposta, :codice_esito, :desc_esito, :id_notifica_pagamento, :data_notifica_pagamento, :id_ricevuta_telematica_positiva, :data_ricevuta_telematica_positiva)";
	
	private static final String UPDATE = UPDATE_MOON_EP_T_RICHIESTA +
		" SET id_istanza=:id_istanza, id_modulo=:id_modulo, id_tipologia_epay=:id_tipologia_epay, id_storico_workflow=:id_storico_workflow, id_epay=:id_epay, iuv=:iuv, codice_avviso=:codice_avviso, data_ins=:data_ins, data_del=:data_del, attore_ins=:attore_ins, attore_del=:attore_del, richiesta=:richiesta, risposta=:risposta, codice_esito=:codice_esito, desc_esito=:desc_esito" +
		", id_notifica_pagamento=:id_notifica_pagamento, data_notifica_pagamento=:data_notifica_pagamento, id_ricevuta_telematica_positiva=:id_ricevuta_telematica_positiva, data_ricevuta_telematica_positiva=:data_ricevuta_telematica_positiva" +
		WHERE_ID_RICHIESTA_ID_RICHIESTA;

	private static final String UPDATE_ID_STORICO_WF = UPDATE_MOON_EP_T_RICHIESTA +
		" SET id_storico_workflow=:id_storico_workflow" +
		WHERE_ID_RICHIESTA_ID_RICHIESTA;

	private static final String UPDATE_RICEVUTA = UPDATE_MOON_EP_T_RICHIESTA +
		" SET id_ricevuta_telematica_positiva=:id_ricevuta_telematica_positiva, data_ricevuta_telematica_positiva=:data_ricevuta_telematica_positiva" +
		WHERE_CODICE_AVVISO_CODICE_AVVISO;
	
	private static final String UPDATE_NOTIFICA = UPDATE_MOON_EP_T_RICHIESTA +
		" SET id_notifica_pagamento=:id_notifica_pagamento, data_notifica_pagamento=:data_notifica_pagamento" +
		WHERE_CODICE_AVVISO_CODICE_AVVISO;
		
	private static final String SEQ_ID = "SELECT nextval('moon_ep_t_richiesta_id_richiesta_seq')";
	

	@Override
	public EpayRichiestaEntity findById(Long idRichiesta) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_RICHIESTA, idRichiesta);
			return (EpayRichiestaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(EpayRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FIND_ID EPAY", "MOONSRV-30800");
		}
	}

	@Override
	public EpayRichiestaEntity findByIdEpay(String idEpay) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_epay", idEpay);
			return (EpayRichiestaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_EPAY, params, BeanPropertyRowMapper.newInstance(EpayRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdEpay] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FIND_ID_EPAY EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public EpayRichiestaEntity findLastValideByIdIstanza(Long idIstanza) throws ItemNotFoundDAOException, DAOException {
		List<EpayRichiestaEntity> richiesteE = findByIdIstanza(idIstanza);
		if (richiesteE == null || richiesteE.isEmpty()) {
			throw new ItemNotFoundDAOException("Impossibile richiedere il pagamento, Richiesta non trovata sull'istanza","MOONSRV-30822");
		}
		return richiesteE.get(richiesteE.size()-1);
	}
	
	@Override
	public EpayRichiestaEntity findByCodiceAvviso(String codiceAvviso) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(CODICE_AVVISO, codiceAvviso);
			return (EpayRichiestaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD_AVVISO, params, BeanPropertyRowMapper.newInstance(EpayRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceAvviso] Elemento non trovato: " + codiceAvviso + " - " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceAvviso] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FIND_BY_CD_AVVISO EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public List<EpayRichiestaEntity> findByIdIstanza(Long idIstanza) {
		try {
			List<EpayRichiestaEntity> result = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_ISTANZA, idIstanza);
			result = (List<EpayRichiestaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(EpayRichiestaEntity.class));
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID_ISTANZA EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public List<EpayRichiestaEntity> find(EpayRichiestaFilter filter) {
		try {
			List<EpayRichiestaEntity> result = null;
			
			StringBuilder sb = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			sb = new StringBuilder(SELECT_FIELDS)
				.append(FROM_MOON_EP_T_RICHIESTA)
				.append(readFilter(filter, params)); // Filtro
			
			result = (List<EpayRichiestaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(EpayRichiestaEntity.class));
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE FIND_ID_ISTANZA EPAY", "MOONSRV-30800");
		}
	}
	
	private StringBuilder readFilter(EpayRichiestaFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		Optional<Boolean> boolOpt = Optional.empty();
		if (filter!=null) {
			readFilterPropertyWa(filter.getIdRichiesta(), "id_richiesta = :id_richiesta", ID_RICHIESTA, sb, params);
			readFilterPropertyWa(filter.getIdIstanza(), "id_istanza = :id_istanza", ID_ISTANZA, sb, params);
			readFilterPropertyWa(filter.getIdModulo(), "id_modulo = :id_modulo", "id_modulo", sb, params);
			readFilterPropertyWa(filter.getIdStoricoWorkflow(), "id_storico_workflow = :id_storico_workflow", ID_STORICO_WORKFLOW, sb, params);
			readFilterPropertyWa(filter.getCodiceAvviso(), "codice_avviso = :codice_avviso", CODICE_AVVISO, sb, params);
			readFilterPropertyWa(filter.getIuv(), "iuv = :iuv", "iuv", sb, params);
			boolOpt = filter.getDeleted();
			if (boolOpt.isPresent()) {
				if (Boolean.TRUE.equals(boolOpt.get())) {
					sb = appendWhereOrAnd(sb).append("data_del IS NOT NULL");
				} else {
					sb = appendWhereOrAnd(sb).append("data_del IS NULL");
				}
			}
		}
		return sb;
	}

	@Override
	public Long insert(EpayRichiestaEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdRichiesta(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT EPAY", "MOONSRV-30800");
		}
	}
	
	@Override
	public int update(EpayRichiestaEntity entity) {
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
	
	@Override
	public int updateIdStoricoWF(Long idRichiesta, Long idStoricoWF) {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateIdStoricoWF] IN idRichiesta: "+idRichiesta);
			LOG.debug("[" + CLASS_NAME + "::updateIdStoricoWF] IN idStoricoWF: "+idStoricoWF);
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue(ID_RICHIESTA, idRichiesta);
	    	params.addValue(ID_STORICO_WORKFLOW, idStoricoWF);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ID_STORICO_WF, params);
			LOG.debug("[" + CLASS_NAME + "::updateIdStoricoWF] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateIdStoricoWF] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO EPAY UPDATE_ID_STORICO_WF", "MOONSRV-30800");
		}
	}
	
	
	@Override
	public int updateNotifica(String codiceAvviso, Long idNotificaPagamento, Date dataNotificaPagamento) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateNotifica] IN codiceAvviso: "+codiceAvviso);
				LOG.debug("[" + CLASS_NAME + "::updateNotifica] IN idNotificaPagamento: "+idNotificaPagamento);
				LOG.debug("[" + CLASS_NAME + "::updateNotifica] IN dataNotificaPagamento: "+dataNotificaPagamento);
			}
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue(CODICE_AVVISO, codiceAvviso);
	    	params.addValue("id_notifica_pagamento", idNotificaPagamento);
	    	params.addValue("data_notifica_pagamento", dataNotificaPagamento);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_NOTIFICA, params);
			LOG.debug("[" + CLASS_NAME + "::updateNotifica] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateNotifica] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO EPAY UPDATE_NOTIFICA", "MOONSRV-30800");
		}
	}
	
	
	@Override
	public int updateRicevuta(String codiceAvviso, Long idRicevutaTelematica, Date dataRicevutaTelematica) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateRicevuta] IN codiceAvviso: "+codiceAvviso);
				LOG.debug("[" + CLASS_NAME + "::updateRicevuta] IN idRicevutaTelematica: "+idRicevutaTelematica);
				LOG.debug("[" + CLASS_NAME + "::updateRicevuta] IN dataRicevutaTelematica: "+dataRicevutaTelematica);
			}
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue(CODICE_AVVISO, codiceAvviso);
	    	params.addValue("id_ricevuta_telematica_positiva", idRicevutaTelematica);
	    	params.addValue("data_ricevuta_telematica_positiva", dataRicevutaTelematica);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_RICEVUTA, params);
			LOG.debug("[" + CLASS_NAME + "::updateRicevuta] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateRicevuta] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO EPAY UPDATE_RICEVUTA", "MOONSRV-30800");
		}
	}
	
    private MapSqlParameterSource mapEntityParameters(EpayRichiestaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue(ID_RICHIESTA, entity.getIdRichiesta());
    	params.addValue(ID_ISTANZA, entity.getIdIstanza());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("id_tipologia_epay", entity.getIdTipologiaEpay(), Types.INTEGER);
    	params.addValue(ID_STORICO_WORKFLOW, entity.getIdStoricoWorkflow());
    	params.addValue("id_epay", entity.getIdEpay(), Types.VARCHAR);
    	params.addValue("iuv", entity.getIuv(), Types.VARCHAR);
    	params.addValue(CODICE_AVVISO, entity.getCodiceAvviso(), Types.VARCHAR);
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

	@Override
	public EpayRichiestaEntity findByIuv(String iuv) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("iuv", iuv);
			return (EpayRichiestaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_IUV, params, BeanPropertyRowMapper.newInstance(EpayRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIuv] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIuv] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FIND_IUV EPAY", "MOONSRV-30800");
		}
	}


}
