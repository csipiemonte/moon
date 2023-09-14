/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ProcessoModuloEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.ProcessoModuloDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * Entity della tabelladi relazione modulo processo
 * <br>
 * <br>Tabella moon_wf_r_modulo_processo
 * <br>PK: idModulo idProcesso
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

@Component
public class ProcessoModuloDAOImpl extends JdbcTemplateDAO implements ProcessoModuloDAO {
	
	private static final String CLASS_NAME = "ProcessoModuloDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_modulo, id_processo, ordine";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
		" FROM moon_wf_r_modulo_processo" +
		" WHERE id_processo = :id_processo" +
		" AND id_modulo = :id_modulo";
	
	private static final  String FIND = FIND_SELECT_FIELDS +
		" FROM moon_wf_r_modulo_processo";

	private static final String INSERT = "INSERT INTO moon_wf_r_modulo_processo (id_modulo, id_processo, ordine)" +
		" VALUES (:id_modulo, :id_processo, :ordine)";
	private static final String DELETE = "DELETE FROM moon_wf_r_modulo_processo WHERE id_processo = :id_processo AND id_modulo = :id_modulo";
	private static final String DELETE_ALL_BY_ID_MODULO = "DELETE FROM moon_wf_r_modulo_processo WHERE id_modulo = :id_modulo";
	
	/**
	 * Restituisce la relazione modulo processo se esiste per {@code idProcesso} e {@code idModulo}
	 * 
	 * @param {@code idProcesso} la chiave primaria. Cannot be {@code null}
	 * @param {@code idModulo} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return la relazione modulo processo ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public ProcessoModuloEntity findById(Long idProcesso, Long idModulo) throws ItemNotFoundDAOException,DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_processo", idProcesso);
			params.addValue("id_modulo", idModulo);
			return (ProcessoModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ProcessoModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato idProcesso:"+idProcesso+" idModulo:"+idModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	/**
	 * Restituisce tutti le relazione modulo processo  presenti nel sistema
	 * 
	 * @return la lista di tutte le relazione modulo processo
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public List<ProcessoModuloEntity> find(ProcessoModuloEntity filter) throws DAOException {
		try {
			StringBuilder sb = new StringBuilder(FIND);
			MapSqlParameterSource params = new MapSqlParameterSource();
			sb.append(readFilter(filter,params));
			return (List<ProcessoModuloEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(ProcessoModuloEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::find] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	private StringBuilder readFilter(ProcessoModuloEntity filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter!=null) {
			boolean first = true;
			if (filter.getIdProcesso()!=null) {
				sb.append(whereIfFirstOrAnd(first))
					.append("id_processo = :id_processo");
				params.addValue("id_processo", filter.getIdProcesso());
				first = false;
			}
			if (filter.getIdModulo()!=null) {
				sb.append(whereIfFirstOrAnd(first))
					.append("id_modulo = :id_modulo");
				params.addValue("id_modulo", filter.getIdModulo());
				first = false;
			}
		}
		return sb;
	}
	private String whereIfFirstOrAnd(boolean first) {
		return first?" WHERE ":" AND ";
	}


	/**
	 * Inserisce un record di ProcessoModuloEntity
	 * 
	 * @param {@code entity} l'entity che descrive la relazione tra modulo e processo
	 * 
	 * @return 1 se il record è stato correttamente inserito
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int insert(ProcessoModuloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);		
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapProcessoModuloEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}

	private MapSqlParameterSource mapProcessoModuloEntityParameters(ProcessoModuloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_processo", entity.getIdProcesso());
		params.addValue("id_modulo", entity.getIdModulo());
		params.addValue("ordine", entity.getOrdine());
		return params;
	}
	
	@Override
	public int delete(Long idProcesso, Long idModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idProcesso: "+idProcesso+"  idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_processo", idProcesso);
			params.addValue("id_modulo", idModulo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}


	@Override
	public int deleteAllByIdModulo(Long idModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteAllByIdModulo] IN idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_ALL_BY_ID_MODULO, params);
			LOG.debug("[" + CLASS_NAME + "::deleteAllByIdModulo] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::deleteAllByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}

}
