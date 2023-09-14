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
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.MyDocsRichiestaEntity;
import it.csi.moon.commons.entity.MyDocsRichiestaFilter;
import it.csi.moon.moonsrv.business.service.impl.dao.MyDocsRichiestaDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso al tracciamento delle richieste di Pubblicazione su MyDocs (DocMe)
 * <br>
 * <br>Tabella principale : moon_md_t_richiesta
 * 
 * @see MyDocsRichiestaEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 03/02/2023 - versione iniziale
 */

@Component
public class MyDocsRichiestaDAOImpl extends JdbcTemplateDAO implements MyDocsRichiestaDAO {
	private static final String CLASS_NAME = "MyDocsRichiestaDAOImpl";
	
	private static final String FIND_SELECT_COUNT = "SELECT count(*)";
	private static final String FIND_SELECT_FIELDS = "SELECT id_richiesta, data_richiesta, stato, tipo_doc, id_istanza, id_allegato_istanza, id_file, id_modulo, id_area, id_ente, id_storico_workflow, id_ambito_mydocs, id_tipologia_mydocs, uuid_mydocs, note, codice_esito, desc_esito, data_upd\n";

	private static final String FIND_BY_ID = FIND_SELECT_FIELDS +
			" FROM moon_md_t_richiesta WHERE id_richiesta = :id_richiesta";
	
	private static final String FIND_BY_UUID_MD = FIND_SELECT_FIELDS +
			" FROM moon_md_t_richiesta WHERE uuid_mydocs = :uuid_mydocs";
	
	private static final String FIND_FROM = " FROM moon_md_t_richiesta";
	private static final String COUNT = FIND_SELECT_COUNT + FIND_FROM;
	private static final String FIND = FIND_SELECT_FIELDS + FIND_FROM;
	
	private static final  String INSERT = "INSERT INTO moon_md_t_richiesta (id_richiesta, data_richiesta, stato, tipo_doc, id_istanza, id_allegato_istanza, id_file, id_modulo, id_area, id_ente, id_storico_workflow, id_ambito_mydocs, id_tipologia_mydocs, uuid_mydocs, note, codice_esito, desc_esito, data_upd)" + 
			" VALUES (:id_richiesta, :data_richiesta, :stato, :tipo_doc, :id_istanza, :id_allegato_istanza, :id_file, :id_modulo, :id_area, :id_ente, :id_storico_workflow, :id_ambito_mydocs, :id_tipologia_mydocs, :uuid_mydocs, :note, :codice_esito, :desc_esito, :data_upd)";

	private static final  String UPDATE_ESITO_FIELDS = "UPDATE moon_md_t_richiesta" +
			" SET codice_esito=:codice_esito, desc_esito=:desc_esito, data_upd=:data_upd";
//	" SET codice_esito=:codice_esito, desc_esito=:desc_esito, data_upd=:data_upd, note=LEFT(CONCAT_WS(', ',note,:note),2000)";
	private static final  String UPDATE_RESP_ID = UPDATE_ESITO_FIELDS +
			" WHERE id_richiesta = :id_richiesta";
	private static final  String UPDATE_RESP_UUID_MD = UPDATE_ESITO_FIELDS +
			" WHERE uuid_mydocs = :uuid_mydocs";

	private static final String SEQ_ID = "SELECT nextval('moon_md_t_richiesta_id_richiesta_seq')";
	

	@Override
	public MyDocsRichiestaEntity findById(Long idRichiesta) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_richiesta", idRichiesta);
			return (MyDocsRichiestaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(MyDocsRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public MyDocsRichiestaEntity findByUuidMydocs(String uuidMydocs) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("uuid_mydocs", uuidMydocs);
			return (MyDocsRichiestaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_UUID_MD, params, BeanPropertyRowMapper.newInstance(MyDocsRichiestaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByUuidMydocs] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByUuidMydocs] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<MyDocsRichiestaEntity> find(MyDocsRichiestaFilter filter) throws DAOException {
		List<MyDocsRichiestaEntity> result = null;
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
		result = (List<MyDocsRichiestaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params,
				BeanPropertyRowMapper.newInstance(MyDocsRichiestaEntity.class));

		return result;
	}

	private Object readFilter(MyDocsRichiestaFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		Optional<Long> longOpt = Optional.empty();
		Optional<String> strOpt = Optional.empty();
		Optional<Integer> intOpt = Optional.empty();
		if (filter != null) {
			longOpt = filter.getIdRichiesta();
			if (longOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_richiesta = :id_richiesta ");
				params.addValue("id_richiesta", longOpt.get());
			}
			strOpt = filter.getStato();
			if (strOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("stato = :stato ");
				params.addValue("stato", strOpt.get());
			}
			intOpt = filter.getTipoDoc();
			if (intOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("tipo_doc = :tipo_doc ");
				params.addValue("tipo_doc", intOpt.get());
			}
			longOpt = filter.getIdIstanza();
			if (longOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_istanza = :id_istanza ");
				params.addValue("id_istanza", longOpt.get());
			}
			longOpt = filter.getIdAllegato();
			if (longOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_allegato_istanza = :id_allegato_istanza ");
				params.addValue("id_allegato_istanza", longOpt.get());
			}
			longOpt = filter.getIdFile();
			if (longOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_file = :id_file ");
				params.addValue("id_file", longOpt.get());
			}
			longOpt = filter.getIdModulo();
			if (longOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_modulo = :id_modulo ");
				params.addValue("id_modulo", longOpt.get());
			}
			longOpt = filter.getIdArea();
			if (longOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_area = :id_area ");
				params.addValue("id_area", longOpt.get());
			}
			longOpt = filter.getIdEnte();
			if (longOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_ente = :id_ente ");
				params.addValue("id_ente", longOpt.get());
			}
			strOpt = filter.getUuidMydocs();
			if (strOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("uuid_mydocs = :uuid_mydocs ");
				params.addValue("uuid_mydocs", strOpt.get());
			}
			strOpt = filter.getCodiceEsito();
			if (strOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("codice_esito = :codice_esito ");
				params.addValue("codice_esito", strOpt.get());
			}
			longOpt = filter.getIdStoricoWorkflow();
			if (longOpt.isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_storico_workflow = :id_storico_workflow ");
				params.addValue("id_storico_workflow", longOpt.get());
			}
		}
		return sb;
	}
	
	@Override
	public Long insert(MyDocsRichiestaEntity entity) throws DAOException {
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
			throw new DAOException("ERRORE INSERT MYDOCS RICHIESTA");
		}
	}

	@Override
	public int updateResponseById(MyDocsRichiestaEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseById] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_richiesta" , entity.getIdRichiesta());
			//params.addValue("note" , entity.getNote());
	    	params.addValue("codice_esito", entity.getCodiceEsito());
	    	params.addValue("desc_esito", entity.getDescEsito());
	    	params.addValue("data_upd", new Date());
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_RESP_ID, params);
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseById] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateResponseById] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE MYDOCS RICHIESTA ID");
		}
	}
	
	@Override
	public int updateResponseByUuidMydocs(MyDocsRichiestaEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseByUuidMydocs] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("uuid_mydocs" , entity.getUuidMydocs());
			params.addValue("note" , entity.getNote()); //!=null?entity.getNote().substring(0,  Math.min(entity.getNote().length(), 2000)):null);
	    	params.addValue("codice_esito", entity.getCodiceEsito());
	    	params.addValue("desc_esito", entity.getDescEsito());
	    	params.addValue("data_upd", new Date());
	    	int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_RESP_UUID_MD, params);
			if (LOG.isDebugEnabled())
				LOG.debug("[" + CLASS_NAME + "::updateResponseByUuidMydocs] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateResponseByUuidMydocs] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE MYDOCS RICHIESTA UUID_MYDOCS");
		}
	}
	

    private MapSqlParameterSource mapEntityParameters(MyDocsRichiestaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_richiesta", entity.getIdRichiesta());
    	params.addValue("data_richiesta", entity.getDataRichiesta());
    	params.addValue("stato", entity.getStato());
    	params.addValue("tipo_doc", entity.getTipoDoc());
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("id_allegato_istanza", entity.getIdAllegatoIstanza());
    	params.addValue("id_file", entity.getIdFile());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("id_area", entity.getIdArea());
    	params.addValue("id_ente", entity.getIdEnte());
    	params.addValue("id_storico_workflow", entity.getIdStoricoWorkflow());
    	params.addValue("id_ambito_mydocs", entity.getIdAmbitoMydocs());
    	params.addValue("id_tipologia_mydocs", entity.getIdTipologiaMydocs());
    	params.addValue("uuid_mydocs", entity.getUuidMydocs());
    	params.addValue("note" , entity.getNote());
    	params.addValue("codice_esito", entity.getCodiceEsito());
    	params.addValue("desc_esito", entity.getDescEsito());
    	params.addValue("data_upd", entity.getDataUpd());
    	return params;
    }
}
