/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.TicketingSystemRichiestaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.TicketingSystemRichiestaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.TicketingSystemRichiestaFilter;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso al tracciamento delle richieste verso i systemi di ticketing CRM es.: NextCRM
 * <br>
 * <br>Tabella principale : moon_ts_t_richiesta
 * 
 * @see TicketingSystemRichiestaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 22/09/2021 - versione iniziale
 */
@Component
public class TicketingSystemRichiestaDAOImpl extends JdbcTemplateDAO implements TicketingSystemRichiestaDAO {
	private static final String CLASS_NAME = "TicketingSystemRichiestaDAOImpl";
	
	private static final String FIND_SELECT_COUNT = "SELECT count(*)";
	private static final String FIND_SELECT_FIELDS = "SELECT id_richiesta, data_richiesta, codice_richiesta, uuid_richiesta, stato, tipo_doc, id_istanza, id_allegato_istanza, id_file, id_modulo, id_area, id_ente, id_ticketing_system, uuid_ticketing_system, note, codice_esito, desc_esito, data_upd\n";

	private static final String FIND_BY_ID = FIND_SELECT_FIELDS +
			" FROM moon_ts_t_richiesta WHERE id_richiesta = :id_richiesta";
	
	private static final String FIND_BY_UUID_TS = FIND_SELECT_FIELDS +
			" FROM moon_ts_t_richiesta WHERE uuid_ticketing_system = :uuid_ticketing_system";
	
	private static final String FIND_FROM = " FROM moon_ts_t_richiesta";
	private static final String COUNT = FIND_SELECT_COUNT + FIND_FROM;
	private static final String FIND = FIND_SELECT_FIELDS + FIND_FROM;
	
	private static final  String INSERT = "INSERT INTO moon_ts_t_richiesta (id_richiesta, data_richiesta, codice_richiesta, uuid_richiesta, stato, tipo_doc, id_istanza, id_allegato_istanza, id_file, id_modulo, id_area, id_ente, id_ticketing_system, uuid_ticketing_system, note, codice_esito, desc_esito, data_upd)" + 
			" VALUES (:id_richiesta, :data_richiesta, :codice_richiesta, :uuid_richiesta, :stato, :tipo_doc, :id_istanza, :id_allegato_istanza, :id_file, :id_modulo, :id_area, :id_ente, :id_ticketing_system, :uuid_ticketing_system, :note, :codice_esito, :desc_esito, :data_upd)";

	private static final  String UPDATE_ESITO_FIELDS_ = "UPDATE moon_ts_t_richiesta" +
			" SET note=LEFT(CONCAT_WS(', ',note,:note),2000), codice_esito=:codice_esito, desc_esito=:desc_esito, data_upd=:data_upd";
	private static final  String UPDATE_RESP_ID = UPDATE_ESITO_FIELDS_ +
			" WHERE id_richiesta = :id_richiesta";
	private static final  String UPDATE_RESP_UUID_TS = UPDATE_ESITO_FIELDS_ +
			" WHERE uuid_ticketing_system = :uuid_ticketing_system";

	private static final String SEQ_ID = "SELECT nextval('moon_ts_t_richiesta_id_richiesta_seq')";
	

	@Override
	public TicketingSystemRichiestaEntity findById(Long idRichiesta) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_richiesta", idRichiesta);
			return (TicketingSystemRichiestaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(TicketingSystemRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public TicketingSystemRichiestaEntity findByUuidTicketingSystem(String uuidticketing_system) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("uuid_ticketing_system", uuidticketing_system);
			return (TicketingSystemRichiestaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_UUID_TS, params, BeanPropertyRowMapper.newInstance(TicketingSystemRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByUuidTicketingSystem] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByUuidTicketingSystem] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<TicketingSystemRichiestaEntity> find(TicketingSystemRichiestaFilter filter) throws DAOException {
		List<TicketingSystemRichiestaEntity> result = null;
		if (log.isDebugEnabled())
			log.debug("[" + CLASS_NAME + "::find] IN filter: " + filter);

		StringBuilder sb = new StringBuilder(FIND);
		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(readFilter(filter, params));

		// Paginazione
		if (filter != null && filter.isUsePagination()) {
			sb.append(" LIMIT " + filter.getLimit() + " OFFSET " + filter.getOffset());
		}

		if (log.isDebugEnabled())
			log.debug("[" + CLASS_NAME + "::find] params: " + params);
		result = (List<TicketingSystemRichiestaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params,
				BeanPropertyRowMapper.newInstance(TicketingSystemRichiestaEntity.class));

		return result;
	}

	private Object readFilter(TicketingSystemRichiestaFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter != null) {
			if (filter.getIdRichiesta().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_richiesta = :id_richiesta ");
				params.addValue("id_richiesta", filter.getIdRichiesta().get());
			}
			if (filter.getCodiceRichiesta().isPresent()) {
				sb = appendWhereOrAnd(sb).append("codice_richiesta = :codice_richiesta ");
				params.addValue("codice_richiesta", filter.getCodiceRichiesta().get());
			}
			if (filter.getUuidRichiesta().isPresent()) {
				sb = appendWhereOrAnd(sb).append("uuid_richiesta = :uuid_richiesta ");
				params.addValue("uuid_richiesta", filter.getUuidRichiesta().get());
			}
			if (filter.getStato().isPresent()) {
				sb = appendWhereOrAnd(sb).append("stato = :stato ");
				params.addValue("stato", filter.getStato().get());
			}
			if (filter.getTipoDoc().isPresent()) {
				sb = appendWhereOrAnd(sb).append("tipo_doc = :tipo_doc ");
				params.addValue("tipo_doc", filter.getTipoDoc().get());
			}
			if (filter.getIdIstanza().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_istanza = :id_istanza ");
				params.addValue("id_istanza", filter.getIdIstanza().get());
			}
			if (filter.getIdAllegato().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_allegato_istanza = :id_allegato_istanza ");
				params.addValue("id_allegato_istanza", filter.getIdAllegato().get());
			}
			if (filter.getIdFile().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_file = :id_file ");
				params.addValue("id_file", filter.getIdFile().get());
			}
			if (filter.getIdModulo().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_modulo = :id_modulo ");
				params.addValue("id_modulo", filter.getIdModulo().get());
			}
			if (filter.getIdArea().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_area = :id_area ");
				params.addValue("id_area", filter.getIdArea().get());
			}
			if (filter.getIdEnte().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_ente = :id_ente ");
				params.addValue("id_ente", filter.getIdEnte().get());
			}
			if (filter.getIdTicketingSystem().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_ticketing_system = :id_ticketing_system ");
				params.addValue("id_ticketing_system", filter.getIdTicketingSystem().get());
			}
			if (filter.getUuidTicketingSystem().isPresent()) {
				sb = appendWhereOrAnd(sb).append("uuid_ticketing_system = :uuid_ticketing_system ");
				params.addValue("uuid_ticketing_system", filter.getUuidTicketingSystem().get());
			}
			if (filter.getCodiceEsito().isPresent()) {
				sb = appendWhereOrAnd(sb).append("codice_esito = :codice_esito ");
				params.addValue("codice_esito", filter.getCodiceEsito().get());
			}
		}
		return sb;
	}

	
	@Override
	public Long insert(TicketingSystemRichiestaEntity entity) throws DAOException {
		try {
			if (log.isDebugEnabled())
				log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idRichiesta = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdRichiesta(idRichiesta);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			if (log.isDebugEnabled())
				log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idRichiesta;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT Nextcrm RICHIESTA");
		}
	}

	@Override
	public int updateResponseById(TicketingSystemRichiestaEntity entity) throws DAOException {
		try {
			if (log.isDebugEnabled())
				log.debug("[" + CLASS_NAME + "::updateResponseById] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_richiesta" , entity.getIdRichiesta());
			params.addValue("note" , entity.getNote());
	    	params.addValue("codice_esito", entity.getCodiceEsito());
	    	params.addValue("desc_esito", entity.getDescEsito());
	    	params.addValue("data_upd", new Date());
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_RESP_ID, params);
			if (log.isDebugEnabled())
				log.debug("[" + CLASS_NAME + "::updateResponseById] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateResponseById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE Nextcrm RICHIESTA ID");
		}
	}
	
	@Override
	public int updateResponseByUuidTicketingSystem(TicketingSystemRichiestaEntity entity) throws DAOException {
		try {
			if (log.isDebugEnabled())
				log.debug("[" + CLASS_NAME + "::updateResponseByUuidTicketingSystem] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("uuid_ticketing_system" , entity.getUuidTicketingSystem());
			params.addValue("note" , entity.getNote()); //!=null?entity.getNote().substring(0,  Math.min(entity.getNote().length(), 2000)):null);
	    	params.addValue("codice_esito", entity.getCodiceEsito());
	    	params.addValue("desc_esito", entity.getDescEsito());
	    	params.addValue("data_upd", new Date());
	    	int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_RESP_UUID_TS, params);
			if (log.isDebugEnabled())
				log.debug("[" + CLASS_NAME + "::updateResponseByUuidTicketingSystem] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateResponseByUuidTicketingSystem] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE Nextcrm RICHIESTA UUID_TS");
		}
	}
	

//id_richiesta, data_richiesta, codice_richiesta, uuid_richiesta, stato, id_istanza, id_modulo, id_area, id_ente, id_ticketing_system, uuid_ticketing_system, note, codice_esito, desc_esito
    private MapSqlParameterSource mapEntityParameters(TicketingSystemRichiestaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_richiesta", entity.getIdRichiesta());
    	params.addValue("data_richiesta", entity.getDataRichiesta());
    	params.addValue("codice_richiesta", entity.getCodiceRichiesta());
    	params.addValue("uuid_richiesta", entity.getUuidRichiesta());
    	params.addValue("stato", entity.getStato());
    	params.addValue("tipo_doc", entity.getTipoDoc());
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("id_allegato_istanza", entity.getIdAllegatoIstanza());
    	params.addValue("id_file", entity.getIdFile());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("id_area", entity.getIdArea());
    	params.addValue("id_ente", entity.getIdEnte());
    	params.addValue("id_ticketing_system" , entity.getIdTicketingSystem());
    	params.addValue("uuid_ticketing_system", entity.getUuidTicketingSystem());
    	params.addValue("note" , entity.getNote());
    	params.addValue("codice_esito", entity.getCodiceEsito());
    	params.addValue("desc_esito", entity.getDescEsito());
    	params.addValue("data_upd", entity.getDataUpd());
    	
    	return params;
    }
}
