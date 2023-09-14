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

import it.csi.moon.commons.entity.MessaggioSupportoEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.MessaggioSupportoDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai Messaggi di Supporto (Chat)
 * 
 * @see MessaggioSupportoEntity
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
@Component
public class MessaggioSupportoDAOImpl extends JdbcTemplateDAO implements MessaggioSupportoDAO {

	private static final String CLASS_NAME = "MessaggioSupportoDAOImpl";

	private static final String SELECT_FIELDS_ = "SELECT id_messaggio_supporto, id_richiesta_supporto, contenuto, provenienza, data_ins, attore_ins"; 
	
	private static final String FIND_BY_ID = SELECT_FIELDS_ +
			" FROM moon_fo_t_messaggio_supporto" +
			" WHERE id_messaggio_supporto = :id_messaggio_supporto";

	private static final String FIND_BY_ID_RICHESTA = SELECT_FIELDS_ +
			" FROM moon_fo_t_messaggio_supporto" +
			" WHERE id_richiesta_supporto = :id_richiesta_supporto";
	
	private static final String INSERT = "INSERT INTO moon_fo_t_messaggio_supporto (" + 
			" id_messaggio_supporto, id_richiesta_supporto, contenuto, provenienza, data_ins, attore_ins)" + 
			" VALUES (:id_messaggio_supporto, :id_richiesta_supporto, :contenuto, :provenienza, :data_ins, :attore_ins)";

	private static final String ELENCO = SELECT_FIELDS_ +
			" FROM moon_fo_t_messaggio_supporto";

	private static final String DELETE = "DELETE FROM moon_fo_t_messaggio_supporto WHERE id_messaggio_supporto = :id_messaggio_supporto";

	private static final  String UPDATE = "UPDATE moon_fo_t_messaggio_supporto" +
			" SET id_richiesta_supporto=:id_richiesta_supporto, contenuto=:contenuto, provenienza=:provenienza, data_ins=:data_ins, attore_ins=:attore_ins" +
			" WHERE id_messaggio_supporto = :id_messaggio_supporto";
	
	private static final String ORDER_BY_DATA_INS_DESC = " ORDER BY data_ins DESC";
	
	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_messaggio_supporto_id_messaggio_supporto_seq')";

	/**
	 * Ottiene il Messaggio Supporto identificato per {@code idMessaggioSupporto}
	 * 
	 * @param {@code idMessaggioSupporto} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il Messaggio Supporto ricercata, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public MessaggioSupportoEntity findById(Long idMessaggioSupporto) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_messaggio_supporto", idMessaggioSupporto);
			return (MessaggioSupportoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(MessaggioSupportoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Ottiene tutti Messaggi di Supporto legati ad una Richiesta di supporto
	 * 
	 * @return la lista di tutti Messagi di Supporto.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public List<MessaggioSupportoEntity> findByIdRichista(Long idRichiestaSupporto) throws DAOException {
		List<MessaggioSupportoEntity> result = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_richiesta_supporto", idRichiestaSupporto);		
		
		LOG.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<MessaggioSupportoEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_RICHESTA + ORDER_BY_DATA_INS_DESC, params, BeanPropertyRowMapper.newInstance(MessaggioSupportoEntity.class));
		return result;
	}

	/**
	 * Inserisci un Mesaggio di Supporto nel sistema
	 * 
	 * @param {@code entity} il Messaggio di Supporto da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idMessaggioSupporto} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(MessaggioSupportoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idMessaggioSupporto = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdMessaggioSupporto(idMessaggioSupporto);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idMessaggioSupporto;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}

	/**
	 * Aggiorna un Messaggio di Supporto nel sistema sulla base della sua chiave primaria {@code idMessaggioSupporto} 
	 * 
	 * @param {@code entity} il Messaggio di Supporto da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun ruolo aggiornato, 1 se Messaggio di Supporto aggiornato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(MessaggioSupportoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO RUOLO");
		}
	}

	/**
	 * Cancella un Messaggio di Supporto nel sistema sulla base della sua chiave primaria {@code idMessaggioSupporto}.
	 * 
	 * @param {@code idMessaggioSupporto} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ruolo cancellato, 1 se Messaggio di Supporto cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Long idMessaggioSupporto) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idMessaggioSupporto: "+idMessaggioSupporto);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_messaggio_supporto", idMessaggioSupporto);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}

	/**
	 * Cancella un Messaggio di Supporto nel sistema sulla base dell'entita {@code entity}.
	 * L'entita deve avere valorizzato almeno la sua chiave primaria {@code idMessaggioSupporto}.
	 * 
	 * @param {@code entity} il messaggio da cancellare per PK. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ruolo cancellato, 1 se Richiesta di Supporto cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(MessaggioSupportoEntity entity) throws DAOException {
		return delete(entity.getIdMessaggioSupporto());
	}
	
	
	//id_messaggio_supporto, id_richiesta_supporto, contenuto, provenienza, data_ins, attore_ins
	private MapSqlParameterSource mapEntityParameters(MessaggioSupportoEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_messaggio_supporto", entity.getIdMessaggioSupporto());
		params.addValue("id_richiesta_supporto", entity.getIdRichiestaSupporto());
		params.addValue("contenuto", entity.getContenuto());
		params.addValue("provenienza", entity.getProvenienza());
		params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
		params.addValue("attore_ins", entity.getAttoreIns(), Types.VARCHAR);
		return params;
	}
}
