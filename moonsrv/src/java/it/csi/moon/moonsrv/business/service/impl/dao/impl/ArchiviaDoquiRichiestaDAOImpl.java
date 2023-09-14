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

import it.csi.moon.commons.entity.ProtocolloRichiestaEntity;
import it.csi.moon.commons.entity.ProtocolloRichiestaFilter;
import it.csi.moon.moonsrv.business.service.impl.dao.ArchiviaDoquiRichiestaDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso al tracciamento delle richieste di archiviazione su Doqui
 * <br>
 * <br>Tabella principale : moon_prar_t_richiesta
 * 
 * @see ProtocolloRichiestaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 08/04/2022 - versione iniziale
 */
@Component
public class ArchiviaDoquiRichiestaDAOImpl extends JdbcTemplateDAO implements ArchiviaDoquiRichiestaDAO {

	private static final String CLASS_NAME = "ArchiviaDoquiRichiestaDAOImpl";

	private static final String ID_RICHIESTA = "id_richiesta";
	private static final String UUID_RICHIESTA = "uuid_richiesta";
	private static final String CODICE_RICHIESTA = "codice_richiesta";
	private static final String ID_ISTANZA = "id_istanza";
	private static final String ID_MODULO = "id_modulo";
	private static final String ID_ENTE = "id_ente";
	private static final String ID_AREA = "id_area";
	private static final String ID_ALLEGATO_ISTANZA = "id_allegato_istanza";
	private static final String ID_FILE = "id_file";
	private static final String STATO = "stato";
	private static final String TIPO_ING_USC = "tipo_ing_usc";
	private static final String TIPO_DOC = "tipo_doc";
	private static final String ID_PROTOCOLLATORE = "id_protocollatore";
	private static final String UUID_PROTOCOLLATORE = "uuid_protocollatore";
	private static final String CODICE_ESITO = "codice_esito";
	private static final String DESC_ESITO = "desc_esito";
	private static final String NOTE = "note";
	private static final String DATA_UPD = "data_upd";

	private static final String FIND_SELECT_COUNT = "SELECT count(*)";
	private static final String FIND_SELECT_FIELDS = "SELECT id_richiesta, data_richiesta, codice_richiesta, uuid_richiesta, stato, tipo_ing_usc, tipo_doc, id_istanza, id_allegato_istanza, id_file, id_modulo, id_area, id_ente, id_protocollatore, uuid_protocollatore, note, codice_esito, desc_esito, data_upd\n";

	private static final String FIND_BY_ID = FIND_SELECT_FIELDS +
			" FROM moon_prar_t_richiesta WHERE id_richiesta = :id_richiesta";
	
	private static final String FIND_BY_UUID_PR = FIND_SELECT_FIELDS +
			" FROM moon_prar_t_richiesta WHERE uuid_protocollatore = :uuid_protocollatore";
	
	private static final String FIND_FROM = " FROM moon_prar_t_richiesta";
	private static final String COUNT = FIND_SELECT_COUNT + FIND_FROM;
	private static final String FIND = FIND_SELECT_FIELDS + FIND_FROM;
	
	private static final  String INSERT = "INSERT INTO moon_prar_t_richiesta (id_richiesta, data_richiesta, codice_richiesta, uuid_richiesta, stato, tipo_ing_usc, tipo_doc, id_istanza, id_allegato_istanza, id_file, id_modulo, id_area, id_ente, id_protocollatore, uuid_protocollatore, note, codice_esito, desc_esito, data_upd)" + 
			" VALUES (:id_richiesta, :data_richiesta, :codice_richiesta, :uuid_richiesta, :stato, :tipo_ing_usc, :tipo_doc, :id_istanza, :id_allegato_istanza,  :id_file, :id_modulo, :id_area, :id_ente, :id_protocollatore, :uuid_protocollatore, :note, :codice_esito, :desc_esito, :data_upd)";

	private static final  String UPDATE_ESITO_FIELDS = "UPDATE moon_prar_t_richiesta" +
			" SET note=LEFT(CONCAT_WS(', ',note,:note),2000), codice_esito=:codice_esito, desc_esito=:desc_esito, data_upd=:data_upd";
	private static final  String UPDATE_RESP_ID = UPDATE_ESITO_FIELDS +
			" WHERE id_richiesta = :id_richiesta";
	private static final  String UPDATE_RESP_UUID_PR = UPDATE_ESITO_FIELDS +
			" WHERE uuid_protocollatore = :uuid_protocollatore";

	private static final String SEQ_ID = "SELECT nextval('moon_prar_t_richiesta_id_richiesta_seq')";
	

	@Override
	public ProtocolloRichiestaEntity findById(Long idRichiesta) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_RICHIESTA, idRichiesta);
			return (ProtocolloRichiestaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ProtocolloRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public ProtocolloRichiestaEntity findByUuidProtocollatore(String uuidProtocollatore) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(UUID_PROTOCOLLATORE, uuidProtocollatore);
			return (ProtocolloRichiestaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_UUID_PR, params, BeanPropertyRowMapper.newInstance(ProtocolloRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByUuidProtocollatore] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByUuidProtocollatore] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ProtocolloRichiestaEntity> find(ProtocolloRichiestaFilter filter) throws DAOException {
		List<ProtocolloRichiestaEntity> result = null;
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
		result = (List<ProtocolloRichiestaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params,
				BeanPropertyRowMapper.newInstance(ProtocolloRichiestaEntity.class));

		return result;
	}

	private Object readFilter(ProtocolloRichiestaFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter != null) {
			readFilterPropertyWa(filter.getIdRichiesta(), "id_richiesta = :id_richiesta", ID_RICHIESTA, sb, params);
			readFilterPropertyWa(filter.getCodiceRichiesta(), "codice_richiesta = :codice_richiesta", CODICE_RICHIESTA, sb, params);
			readFilterPropertyWa(filter.getUuidRichiesta(), "uuid_richiesta = :uuid_richiesta", UUID_RICHIESTA, sb, params);
			readFilterPropertyWa(filter.getStato(), "stato = :stato", STATO, sb, params);
			readFilterPropertyWa(filter.getTipoIngUsc(), "tipo_ing_usc = :tipo_ing_usc", TIPO_ING_USC, sb, params);
			readFilterPropertyWa(filter.getTipoDoc(), "tipo_doc = :tipo_doc", TIPO_DOC, sb, params);
			readFilterPropertyWa(filter.getIdIstanza(), "id_istanza = :id_istanza", ID_ISTANZA, sb, params);
			readFilterPropertyWa(filter.getIdAllegato(), "id_allegato_istanza = :id_allegato_istanza", ID_ALLEGATO_ISTANZA, sb, params);
			readFilterPropertyWa(filter.getIdFile(), "id_file = :id_file", ID_FILE, sb, params);
			readFilterPropertyWa(filter.getIdModulo(), "id_modulo = :id_modulo", ID_MODULO, sb, params);
			readFilterPropertyWa(filter.getIdArea(), "id_area = :id_area", ID_AREA, sb, params);
			readFilterPropertyWa(filter.getIdEnte(), "id_ente = :id_ente", ID_ENTE, sb, params);
			readFilterPropertyWa(filter.getIdProtocollatore(), "id_protocollatore = :id_protocollatore", ID_PROTOCOLLATORE, sb, params);
			readFilterPropertyWa(filter.getUuidProtocollatore(), "uuid_protocollatore = :uuid_protocollatore", UUID_PROTOCOLLATORE, sb, params);
			readFilterPropertyWa(filter.getCodiceEsito(), "codice_esito = :codice_esito", CODICE_ESITO, sb, params);
		}
		return sb;
	}

	
	@Override
	public Long insert(ProtocolloRichiestaEntity entity) throws DAOException {
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
			throw new DAOException("ERRORE INSERT PROTOCOLLO RICHIESTA");
		}
	}

	@Override
	public int updateResponseById(ProtocolloRichiestaEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseById] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_RICHIESTA , entity.getIdRichiesta());
			params.addValue(NOTE , entity.getNote());
	    	params.addValue(CODICE_ESITO, entity.getCodiceEsito());
	    	params.addValue(DESC_ESITO, entity.getDescEsito());
	    	params.addValue(DATA_UPD, new Date());
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_RESP_ID, params);
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseById] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateResponseById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE PROTOCOLLO RICHIESTA ID");
		}
	}
	
	@Override
	public int updateResponseByUuidProtocollatore(ProtocolloRichiestaEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseByUuidProtocollatore] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(UUID_PROTOCOLLATORE , entity.getUuidProtocollatore());
			params.addValue(NOTE , entity.getNote()); //!=null?entity.getNote().substring(0,  Math.min(entity.getNote().length(), 2000)):null);
	    	params.addValue(CODICE_ESITO, entity.getCodiceEsito());
	    	params.addValue(DESC_ESITO, entity.getDescEsito());
	    	params.addValue(DATA_UPD, new Date());
	    	int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_RESP_UUID_PR, params);
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseByUuidProtocollatore] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateResponseByUuidProtocollatore] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE PROTOCOLLO RICHIESTA UUID_PR");
		}
	}
	

//id_richiesta, data_richiesta, codice_richiesta, uuid_richiesta, stato, tipo_ing_usc, id_istanza, id_modulo, id_area, id_ente, id_protocollatore, uuid_protocollatore, note, codice_esito, desc_esito
    private MapSqlParameterSource mapEntityParameters(ProtocolloRichiestaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue(ID_RICHIESTA, entity.getIdRichiesta());
    	params.addValue("data_richiesta", entity.getDataRichiesta());
    	params.addValue(CODICE_RICHIESTA, entity.getCodiceRichiesta());
    	params.addValue(UUID_RICHIESTA, entity.getUuidRichiesta());
    	params.addValue(STATO, entity.getStato());
    	params.addValue(TIPO_ING_USC, entity.getTipoIngUsc());
    	params.addValue(TIPO_DOC, entity.getTipoDoc());
    	params.addValue(ID_ISTANZA, entity.getIdIstanza());
    	params.addValue(ID_ALLEGATO_ISTANZA, entity.getIdAllegatoIstanza());
    	params.addValue(ID_FILE, entity.getIdFile());
    	params.addValue(ID_MODULO, entity.getIdModulo());
    	params.addValue(ID_AREA, entity.getIdArea());
    	params.addValue(ID_ENTE, entity.getIdEnte());
    	params.addValue(ID_PROTOCOLLATORE , entity.getIdProtocollatore());
    	params.addValue(UUID_PROTOCOLLATORE, entity.getUuidProtocollatore());
    	params.addValue(NOTE , entity.getNote());
    	params.addValue(CODICE_ESITO, entity.getCodiceEsito());
    	params.addValue(DESC_ESITO, entity.getDescEsito());
    	params.addValue(DATA_UPD, entity.getDataUpd());
    	
    	return params;
    }
}
