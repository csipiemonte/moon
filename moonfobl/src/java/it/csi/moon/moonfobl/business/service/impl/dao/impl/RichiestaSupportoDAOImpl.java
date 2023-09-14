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
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.RichiestaSupportoEntity;
import it.csi.moon.commons.entity.RichiestaSupportoFilter;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.moonfobl.business.service.impl.dao.RichiestaSupportoDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle Richieste di Supporto (Chat)
 * 
 * @see RichiestaSupportoEntity
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
@Component
public class RichiestaSupportoDAOImpl extends JdbcTemplateDAO implements RichiestaSupportoDAO {

	private static final String CLASS_NAME = "RichiestaSupportoDAOImpl";

	private static final String ID_RICHIESTA_SUPPORTO = "id_richiesta_supporto";
	private static final String ID_ISTANZA = "id_istanza";
	private static final String ID_MODULO = "id_modulo";
	private static final String FLAG_IN_ATTESA_DI_RISPOSTA = "flag_in_attesa_di_risposta";
	private static final String DESC_MITTENTE = "desc_mittente";
	private static final String EMAIL_MITTENTE = "email_mittente";
	private static final String ATTORE_INS = "attore_ins";
	
	private static final String SELECT_FIELDS = "SELECT id_richiesta_supporto, id_istanza, id_modulo, flag_in_attesa_di_risposta, desc_mittente, email_mittente, data_ins, attore_ins, data_upd, attore_upd"; 
	
	private static final  String FIND_BY_ID = SELECT_FIELDS +
			" FROM moon_fo_t_richiesta_supporto" +
			" WHERE id_richiesta_supporto = :id_richiesta_supporto";

	private static final String INSERT = "INSERT INTO moon_fo_t_richiesta_supporto (" +
			" id_richiesta_supporto, id_istanza, id_modulo, flag_in_attesa_di_risposta, desc_mittente, email_mittente, data_ins, attore_ins, data_upd, attore_upd)" + 
			" VALUES (:id_richiesta_supporto, :id_istanza, :id_modulo, :flag_in_attesa_di_risposta, :desc_mittente, :email_mittente, :data_ins, :attore_ins, :data_upd, :attore_upd)";

	private static final String ELENCO = SELECT_FIELDS +
			" FROM moon_fo_t_richiesta_supporto";

	private static final String DELETE = "DELETE FROM moon_fo_t_richiesta_supporto WHERE id_richiesta_supporto = :id_richiesta_supporto";

	private static final  String UPDATE = "UPDATE moon_fo_t_richiesta_supporto" +
			" SET flag_in_attesa_di_risposta=:flag_in_attesa_di_risposta, data_upd=:data_upd, attore_upd=:attore_upd" +
			" WHERE id_richiesta_supporto = :id_richiesta_supporto";
	
	private static final String ORDER_BY_DATA_UPD_DESC = " ORDER BY data_upd DESC";
	
	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_richiesta_supporto_id_richiesta_supporto_seq')";

	/**
	 * Ottiene la Richiesta Supporto identificato per {@code idRichiestaSupporto}
	 * 
	 * @param {@code idRichiestaSupporto} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return la Richiesta Supporto ricercata, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public RichiestaSupportoEntity findById(Long idRichiestaSupporto) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_RICHIESTA_SUPPORTO, idRichiestaSupporto);
			return (RichiestaSupportoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(RichiestaSupportoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Ottiene tutte Richieste di Supporto presenti nel sistema
	 * 
	 * @return la lista di tutte Richieste di Supporto.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public List<RichiestaSupportoEntity> find(RichiestaSupportoFilter filter) throws DAOException {
		List<RichiestaSupportoEntity> result = null;
		
		StringBuilder sb = new StringBuilder(ELENCO);
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		// Filtro
		sb.append(readFilter(filter, params));
		sb.append(ORDER_BY_DATA_UPD_DESC);
		
		LOG.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<RichiestaSupportoEntity>)getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(RichiestaSupportoEntity.class));
		return result;
	}

	private Object readFilter(RichiestaSupportoFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		Optional<Long> longOpt = Optional.empty();
		Optional<String> strOpt = Optional.empty();
		if (filter!=null) {
			if (filter.getIdRichiestaSupporto().isPresent()) {
				readFilterPropertyWa(filter.getIdRichiestaSupporto(), "id_richiesta_supporto=:id_richiesta_supporto", ID_RICHIESTA_SUPPORTO, sb, params);
			} else {
				readFilterPropertyWa(filter.getIdIstanza(), "id_istanza = :id_istanza", ID_ISTANZA, sb, params);
				readFilterPropertyWa(filter.getIdModulo(), "id_modulo = :id_modulo", ID_MODULO, sb, params);
				readFilterPropertyWa(filter.getFlagInAttesaDiRisposta(), "flag_in_attesa_di_risposta = :flag_in_attesa_di_risposta", FLAG_IN_ATTESA_DI_RISPOSTA, sb, params);
				readFilterPropertyContainsWa(filter.getDescMittente(), "desc_mittente LIKE :desc_mittente", DESC_MITTENTE, sb, params);
				readFilterPropertyContainsWa(filter.getEmailMittente(), "email_mittente LIKE :email_mittente", EMAIL_MITTENTE, sb, params);
				readFilterPropertyWa(filter.getAttoreIns(), "attore_ins = :attore_ins", ATTORE_INS, sb, params);
			}
		}
		return sb;
	}
	
	/**
	 * Inserisci una Richiesta di Supporto nel sistema
	 * 
	 * @param {@code entity} la Richiesta di Supporto da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idRichiestaSupporto} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(RichiestaSupportoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idRichiestaSupporto = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdRichiestaSupporto(idRichiestaSupporto);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapRichiestaSupportoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idRichiestaSupporto;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}

	/**
	 * Aggiorna una Richiesta di Supporto nel sistema sulla base della sua chiave primaria {@code idRichiestaSupporto} 
	 * 
	 * @param {@code entity} la Richiesta di Supporto da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun ruolo aggiornato, 1 se Richiesta di Supporto aggiornata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(RichiestaSupportoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapRichiestaSupportoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO RUOLO");
		}
	}

	/**
	 * Cancella una Richiesta di Supporto nel sistema sulla base della sua chiave primaria {@code idRichiestaSupporto}.
	 * 
	 * @param {@code idRichiestaSupporto} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ruolo cancellato, 1 se Richiesta di Supporto cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Long idRichiestaSupporto) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idRichiestaSupporto: "+idRichiestaSupporto);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_RICHIESTA_SUPPORTO, idRichiestaSupporto);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}

	/**
	 * Cancella una Richiesta di Supporto nel sistema sulla base dell'entita {@code entity}.
	 * L'entita deve avere valorizzato almeno la sua chiave primaria {@code idRichiestaSupporto}.
	 * 
	 * @param {@code idRichiestaSupporto} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ruolo cancellato, 1 se Richiesta di Supporto cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(RichiestaSupportoEntity entity) throws DAOException {
		return delete(entity.getIdRichiestaSupporto());
	}
	
	
	//id_richiesta_supporto, id_istanza, id_modulo, flag_in_attesa_di_risposta, data_ins, attore_ins, data_upd, attore_upd
	private MapSqlParameterSource mapRichiestaSupportoEntityParameters(RichiestaSupportoEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ID_RICHIESTA_SUPPORTO, entity.getIdRichiestaSupporto());
		params.addValue(ID_ISTANZA, entity.getIdIstanza());
		params.addValue(ID_MODULO, entity.getIdModulo());
		params.addValue(FLAG_IN_ATTESA_DI_RISPOSTA, entity.getFlagInAttesaDiRisposta(), Types.VARCHAR);
		params.addValue(DESC_MITTENTE, StrUtils.toUpperCase(entity.getDescMittente()), Types.VARCHAR);
		params.addValue(EMAIL_MITTENTE, StrUtils.toLowerCase(entity.getEmailMittente()), Types.VARCHAR);
		params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
		params.addValue(ATTORE_INS, entity.getAttoreIns(), Types.VARCHAR);
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		return params;
	}
}
