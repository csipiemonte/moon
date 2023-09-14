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

import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.AreaModuloFilter;
import it.csi.moon.moonfobl.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle aree di un ente su quale sara collocato i moduli
 * <br>Un modulo potra essere solo su un area di un ente, ma potra essere su piu area di diversi enti.
 * <br>
 * <br>Tabella moon_fo_r_area_modulo
 * <br>PK: idArea,idModulo
 * <br>AK: idModulo,idEnte
 * 
 * @see AreaModuloEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

@Component
public class AreaModuloDAOImpl extends JdbcTemplateDAO implements AreaModuloDAO {
	
	private static final String CLASS_NAME = "AreaModuloDAOImpl";
	
	private static final String FIND_SELECT_COUNT = "SELECT count(*)";
	private static final String FIND_SELECT_FIELDS = "SELECT id_area, id_modulo, id_ente, data_upd, attore_upd";

	private static final  String FIND_BY_PK  = FIND_SELECT_FIELDS +
			" FROM moon_fo_r_area_modulo" +
			" WHERE id_area = :id_area" +
			" AND id_modulo = :id_modulo";
	
	private static final  String FIND_BY_AK  = FIND_SELECT_FIELDS +
			" FROM moon_fo_r_area_modulo" +
			" WHERE id_modulo = :id_modulo" +
			" AND id_ente = :id_ente";
	private static final  String FIND_BY_ID_MODULO  = FIND_SELECT_FIELDS +
			" FROM moon_fo_r_area_modulo" +
			" WHERE id_modulo = :id_modulo";
	private static final  String FIND_BY_CD_MODULO  = FIND_SELECT_FIELDS +
			" FROM moon_fo_r_area_modulo" +
			" WHERE id_modulo = (SELECT id_modulo FROM moon_io_d_modulo WHERE codice_modulo=:codice_modulo)";
	
	private static final  String FIND = FIND_SELECT_FIELDS +
			" FROM moon_fo_r_area_modulo";
	
	private static final String INSERT = "INSERT INTO moon_fo_r_area_modulo(" +
			" id_area, id_modulo, id_ente, data_upd, attore_upd)" +
			" VALUES (:id_area, :id_modulo, :id_ente, :data_upd, :attore_upd)";


	private static final String DELETE = "DELETE FROM moon_fo_r_area_modulo WHERE id_area = :id_area and id_modulo = :id_modulo";

	private static final  String UPDATE = "UPDATE moon_fo_r_area_modulo" +
			" SET id_ente= :id_ente, data_upd= :data_upd, attore_upd= :attore_upd" +
			" WHERE id_area = :id_area and id_modulo = :id_modulo";
	
	/**
	 * Restituisce  un record di relazione area/modulo identificato per chiava primaria
	 * 
	 * @param idArea
	 * @param idModulo
	 * 
	 * @return il record di relazione area/modulo, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public AreaModuloEntity findByIdAreaModulo(Long idArea, Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_area", idArea);
			params.addValue("id_modulo", idModulo);
			return (AreaModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_PK, params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdAreaModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdAreaModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce  un record di relazione area/modulo identificato per unique index
	 * 
	 * @param idModulo
	 * @param idEnte
	 * 
	 * @return il record di relazione area/modulo, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	@Deprecated
	public AreaModuloEntity findByIdModuloEnte(Long idModulo, Long idEnte) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("id_ente", idEnte);
			return (AreaModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_AK, params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdModuloEnte] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModuloEnte] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	/**
	 * @deprecated use findByIdModuloEnte
	 */
	@Deprecated
	public AreaModuloEntity findByIdModulo(Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (AreaModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	public AreaModuloEntity findByCdModulo(String codiceModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_modulo", codiceModulo);
			return (AreaModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD_MODULO, params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCdModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce i record di relazione area/modulo presenti nel sistema attraverso un filtro di selezione
	 * 
	 * @return la lista dei record di relazione area/modulo selezionati.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public List<AreaModuloEntity> find(AreaModuloFilter filter) throws DAOException {
		List<AreaModuloEntity> result = null;
		LOG.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);
		
		StringBuilder sb = new StringBuilder(FIND);
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		// Filtro
		sb.append(readFilter(filter, params));
		
		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}
		
		LOG.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<AreaModuloEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		
		return result;
	}
	
	private StringBuilder readFilter(AreaModuloFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		Optional<Long> longOpt = Optional.empty();
		if (filter!=null) {
			longOpt = filter.getIdArea();
			if (longOpt.isPresent()) {
				params.addValue("id_area", longOpt.get());
				sb = appendWhereOrAnd(sb).append("id_area = :id_area");
			}
			longOpt = filter.getIdEnte();
			if (longOpt.isPresent()) {
				params.addValue("id_ente", longOpt.get());
				sb = appendWhereOrAnd(sb).append("id_ente = :id_ente");
			}
			longOpt = filter.getIdModulo();
			if (longOpt.isPresent()) {
				params.addValue("id_modulo", longOpt.get());
				sb = appendWhereOrAnd(sb).append("id_modulo = :id_modulo");
			}
		}
		return sb;
	}

	/**
	 * Inserisce un record di relazione area/modulo nel sistema
	 * 
	 * @param {@code entity} il record di relazione da inserire. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int insert(AreaModuloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapAreaModuloEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}

	/**
	 * Aggiorna un record di relazione area/modulo
	 * 
	 * @param {@code entity} il record di relazione area/modulo da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun record aggiornato, 1 se record aggiornato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(AreaModuloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapAreaModuloEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO");
		}
	}

	/**
	 * Cancella un record di relazione area/modulo nel sistema per chiave primaria.
	 * 
	 * @param idArea
	 * @param idModulo
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun record di relazione area/modulo cancellato, 1 se record di relazione area/modulo cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Long idArea,Long idModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idArea: "+idArea);
			LOG.debug("[" + CLASS_NAME + "::delete] IN idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_area", idArea);
			params.addValue("id_modulo", idModulo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}

	/**
	 * Cancella un record di relazione area/modulo nel sistema per chiave primaria.
	 * 
	 * @param {@code entity} il record di relazione area/modulo da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun record di relazione area/modulo cancellato, 1 se record di relazione area/modulo cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(AreaModuloEntity entity) throws DAOException {
		return delete(entity.getIdArea(),entity.getIdModulo());
	}

	private MapSqlParameterSource mapAreaModuloEntityParameters(AreaModuloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_area", entity.getIdArea());
		params.addValue("id_modulo", entity.getIdModulo());
		params.addValue("id_ente", entity.getIdEnte());
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_upd", entity.getAttoreUpd());
		return params;
	}

}
