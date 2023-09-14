/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.TicketingSystemRichiestaEntity;
import it.csi.moon.commons.entity.TicketingSystemRichiestaFilter;
import it.csi.moon.moonsrv.business.service.impl.dao.TicketingSystemRichiestaDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

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

	private static final String ID_RICHIESTA = "id_richiesta";
	private static final String CODICE_RICHIESTA = "codice_richiesta";
	private static final String UUID_RICHIESTA = "uuid_richiesta";
	private static final String STATO = "stato";
	private static final String TIPO_DOC = "tipo_doc";
	private static final String ID_ISTANZA = "id_istanza";
	private static final String ID_ALLEGATO_ISTANZA = "id_allegato_istanza";
	private static final String ID_FILE = "id_file";
	private static final String ID_MODULO = "id_modulo";
	private static final String ID_AREA = "id_area";
	private static final String ID_ENTE = "id_ente";
	private static final String ID_TICKETING_SYSTEM = "id_ticketing_system";
	private static final String UUID_TICKETING_SYSTEM = "uuid_ticketing_system";
	private static final String CODICE_ESITO = "codice_esito";

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

	private static final  String UPDATE_ESITO_FIELDS = "UPDATE moon_ts_t_richiesta" +
			" SET note=LEFT(CONCAT_WS(', ',note,:note),2000), codice_esito=:codice_esito, desc_esito=:desc_esito, stato=:stato, data_upd=:data_upd";
	private static final  String UPDATE_ESITO_BY_ID = UPDATE_ESITO_FIELDS +
			" WHERE id_richiesta = :id_richiesta";
	private static final  String UPDATE_ESITO_BY_UUID_TS = UPDATE_ESITO_FIELDS +
			" WHERE uuid_ticketing_system = :uuid_ticketing_system";
	
	private static final  String UPDATE_RESP_ID = "UPDATE moon_ts_t_richiesta" +
			" SET note=:note, codice_esito=:codice_esito, desc_esito=:desc_esito, stato=:stato, data_upd=:data_upd, uuid_ticketing_system = :uuid_ticketing_system" +
			" WHERE id_richiesta = :id_richiesta";

	private static final String SEQ_ID = "SELECT nextval('moon_ts_t_richiesta_id_richiesta_seq')";
	

	@Override
	public TicketingSystemRichiestaEntity findById(Long idRichiesta) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_RICHIESTA, idRichiesta);
			return (TicketingSystemRichiestaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(TicketingSystemRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public TicketingSystemRichiestaEntity findByUuidTicketingSystem(String uuidticketing_system) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(UUID_TICKETING_SYSTEM, uuidticketing_system);
			return (TicketingSystemRichiestaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_UUID_TS, params, BeanPropertyRowMapper.newInstance(TicketingSystemRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByUuidTicketingSystem] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByUuidTicketingSystem] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<TicketingSystemRichiestaEntity> find(TicketingSystemRichiestaFilter filter) throws DAOException {
		List<TicketingSystemRichiestaEntity> result = null;
		if (LOG.isDebugEnabled())
			LOG.debug("[" + CLASS_NAME + "::find] IN filter: " + filter);

		StringBuilder sb = new StringBuilder(FIND);
		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(readFilter(filter, params));

		// Paginazione
		if (filter != null && filter.isUsePagination()) {
			sb.append(" LIMIT " + filter.getLimit() + " OFFSET " + filter.getOffset());
		}

		if (LOG.isDebugEnabled())
			LOG.debug("[" + CLASS_NAME + "::find] params: " + params);
		result = (List<TicketingSystemRichiestaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params,
				BeanPropertyRowMapper.newInstance(TicketingSystemRichiestaEntity.class));

		return result;
	}

	private Object readFilter(TicketingSystemRichiestaFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter != null) {
			readFilterPropertyWa(filter.getIdRichiesta(), "id_richiesta = :id_richiesta", ID_RICHIESTA, sb, params);
			readFilterPropertyWa(filter.getCodiceRichiesta(), "codice_richiesta = :codice_richiesta", CODICE_RICHIESTA, sb, params);
			readFilterPropertyWa(filter.getUuidRichiesta(), "uuid_richiesta = :uuid_richiesta", UUID_RICHIESTA, sb, params);
			readFilterPropertyWa(filter.getStato(), "stato = :stato", STATO, sb, params);
			readFilterPropertyWa(filter.getTipoDoc(), "tipo_doc = :tipo_doc", TIPO_DOC, sb, params);
			readFilterPropertyWa(filter.getIdIstanza(), "id_istanza = :id_istanza", ID_ISTANZA, sb, params);
			readFilterPropertyWa(filter.getIdAllegato(), "id_allegato_istanza = :id_allegato_istanza", ID_ALLEGATO_ISTANZA, sb, params);
			readFilterPropertyWa(filter.getIdFile(), "id_file = :id_file", ID_FILE, sb, params);
			readFilterPropertyWa(filter.getIdModulo(), "id_modulo = :id_modulo", ID_MODULO, sb, params);
			readFilterPropertyWa(filter.getIdArea(), "id_area = :id_area", ID_AREA, sb, params);
			readFilterPropertyWa(filter.getIdEnte(), "id_ente = :id_ente", ID_ENTE, sb, params);
			readFilterPropertyWa(filter.getIdTicketingSystem(), "id_ticketing_system = :id_ticketing_system", ID_TICKETING_SYSTEM, sb, params);
			readFilterPropertyWa(filter.getUuidTicketingSystem(), "uuid_ticketing_system = :uuid_ticketing_system", UUID_TICKETING_SYSTEM, sb, params);
			readFilterPropertyWa(filter.getCodiceEsito(), "codice_esito = :codice_esito", CODICE_ESITO, sb, params);
		}
		return sb;
	}
	
	@Override
	public Long insert(TicketingSystemRichiestaEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idRichiesta = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdRichiesta(idRichiesta);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idRichiesta;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT crm RICHIESTA");
		}
	}

	@Override
	public int updateResponseById(TicketingSystemRichiestaEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseById] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_RICHIESTA , entity.getIdRichiesta());
			params.addValue(STATO, entity.getStato());
			params.addValue("note" , entity.getNote());
	    	params.addValue(CODICE_ESITO, entity.getCodiceEsito());
	    	params.addValue("desc_esito", entity.getDescEsito());
	    	params.addValue("data_upd", new Date());
	    	params.addValue(UUID_TICKETING_SYSTEM, entity.getUuidTicketingSystem());
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_RESP_ID, params);
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseById] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateResponseById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE crm RICHIESTA ID");
		}
	}
	
	@Override
	public int updateResponseByUuidTicketingSystem(TicketingSystemRichiestaEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseByUuidTicketingSystem] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(UUID_TICKETING_SYSTEM , entity.getUuidTicketingSystem());
			params.addValue("note" , entity.getNote()); //!=null?entity.getNote().substring(0,  Math.min(entity.getNote().length(), 2000)):null);
	    	params.addValue(CODICE_ESITO, entity.getCodiceEsito());
	    	params.addValue("desc_esito", entity.getDescEsito());
	    	params.addValue("data_upd", new Date());
	    	params.addValue(STATO, entity.getStato());
	    	int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ESITO_BY_UUID_TS, params);
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseByUuidTicketingSystem] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateResponseByUuidTicketingSystem] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE crm RICHIESTA UUID_TS");
		}
	}
	

//id_richiesta, data_richiesta, codice_richiesta, uuid_richiesta, stato, id_istanza, id_modulo, id_area, id_ente, id_ticketing_system, uuid_ticketing_system, note, codice_esito, desc_esito
    private MapSqlParameterSource mapEntityParameters(TicketingSystemRichiestaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue(ID_RICHIESTA, entity.getIdRichiesta());
    	params.addValue("data_richiesta", entity.getDataRichiesta());
    	params.addValue(CODICE_RICHIESTA, entity.getCodiceRichiesta());
    	params.addValue(UUID_RICHIESTA, entity.getUuidRichiesta());
    	params.addValue(STATO, entity.getStato());
    	params.addValue(TIPO_DOC, entity.getTipoDoc());
    	params.addValue(ID_ISTANZA, entity.getIdIstanza());
    	params.addValue(ID_ALLEGATO_ISTANZA, entity.getIdAllegatoIstanza());
    	params.addValue(ID_FILE, entity.getIdFile());
    	params.addValue(ID_MODULO, entity.getIdModulo());
    	params.addValue(ID_AREA, entity.getIdArea());
    	params.addValue(ID_ENTE, entity.getIdEnte());
    	params.addValue(ID_TICKETING_SYSTEM , entity.getIdTicketingSystem());
    	params.addValue(UUID_TICKETING_SYSTEM, entity.getUuidTicketingSystem());
    	params.addValue("note" , entity.getNote());
    	params.addValue(CODICE_ESITO, entity.getCodiceEsito());
    	params.addValue("desc_esito", entity.getDescEsito());
    	params.addValue("data_upd", entity.getDataUpd());
    	
    	return params;
    }
}
