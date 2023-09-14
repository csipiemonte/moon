/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.PortaleModuloEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.PortaleModuloDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * Entity della tabelladi relazione portale modulo
 * <br>
 * <br>Tabella moon_fo_r_portale_modulo
 * <br>PK: idPortale idModulo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

@Component
public class PortaleModuloDAOImpl extends JdbcTemplateDAO implements PortaleModuloDAO {
	
	private static final String CLASS_NAME = "PortaleModuloDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_portale, id_modulo, data_upd, attore_upd";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
		" FROM moon_fo_r_portale_modulo" +
		" WHERE id_portale = :id_portale" +
		" AND id_modulo = :id_modulo";
	
	private static final  String FIND = FIND_SELECT_FIELDS +
		" FROM moon_fo_r_portale_modulo";

	private static final String INSERT = "INSERT INTO moon_fo_r_portale_modulo(id_portale, id_modulo, data_upd, attore_upd)" +
		" VALUES (:id_portale, :id_modulo, :data_upd, :attore_upd)";
	private static final String DELETE = "DELETE FROM moon_fo_r_portale_modulo WHERE id_portale = :id_portale AND id_modulo = :id_modulo";
	private static final String DELETE_ALL_BY_ID_MODULO = "DELETE FROM moon_fo_r_portale_modulo WHERE id_modulo = :id_modulo";
	
	/**
	 * Restituisce la relazione modulo portale se esiste per {@code idPortale} e {@code idModulo}
	 * 
	 * @param {@code idPortale} la chiave primaria. Cannot be {@code null}
	 * @param {@code idModulo} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return la relazione modulo portale ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public PortaleModuloEntity findById(Long idPortale, Long idModulo) throws ItemNotFoundDAOException,DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_portale", idPortale);
			params.addValue("id_modulo", idModulo);
			return (PortaleModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(PortaleModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato idPortale:"+idPortale+" idModulo:"+idModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	/**
	 * Restituisce tutti portali attivi presenti nel sistema
	 * 
	 * @return la lista di tutti portali attivi
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public List<PortaleModuloEntity> find(PortaleModuloEntity filter) throws DAOException {
		try {
			StringBuilder sb = new StringBuilder(FIND);
			MapSqlParameterSource params = new MapSqlParameterSource();
			sb.append(readFilter(filter,params));
			return (List<PortaleModuloEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(PortaleModuloEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::find] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	private StringBuilder readFilter(PortaleModuloEntity filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter!=null) {
			boolean first = true;
			if (filter.getIdPortale()!=null) {
				sb.append(whereIfFirstOrAnd(first))
					.append("id_portale = :id_portale");
				params.addValue("id_portale", filter.getIdPortale());
				first = false;
			}
			if (filter.getIdModulo()!=null) {
				sb.append(whereIfFirstOrAnd(first))
					.append("id_modulo = :id_modulo");
				params.addValue("id_modulo", filter.getIdModulo());
				first = false;
			}
			if (filter.getAttoreUpd()!=null) {
				sb.append(whereIfFirstOrAnd(first))
					.append("attore_upd = :attore_upd");
				params.addValue("attore_upd", filter.getAttoreUpd());
				first = false;
			}
		}
		return sb;
	}
	private String whereIfFirstOrAnd(boolean first) {
		return first?" WHERE ":" AND ";
	}


	/**
	 * Inserisce un record di PortaleModuloEntity
	 * 
	 * @param {@code entity} l'entity che descrive la relazione tra portale e modulo
	 * 
	 * @return 1 se il record è stato correttamente inserito
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int insert(PortaleModuloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);		
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapPortaleModuloEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}

	private MapSqlParameterSource mapPortaleModuloEntityParameters(PortaleModuloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_portale", entity.getIdPortale());
		params.addValue("id_modulo", entity.getIdModulo());
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		return params;
	}
	
	@Override
	public int delete(Long idPortale, Long idModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idPortale: "+idPortale+"  idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_portale", idPortale);
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
