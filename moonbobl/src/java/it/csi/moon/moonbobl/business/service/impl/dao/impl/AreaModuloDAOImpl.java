/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AreaModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.AreaModuloFilter;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

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
	
	private final static String CLASS_NAME = "AreaModuloDAOImpl";
	
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
	

	@Override
	public AreaModuloEntity findByIdAreaModulo(Long idArea, Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_area", idArea);
			params.addValue("id_modulo", idModulo);
			return (AreaModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_PK, params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIdAreaModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdAreaModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	@Deprecated
	public AreaModuloEntity findByIdModuloEnte(Long idModulo, Long idEnte) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("id_ente", idEnte);
			return (AreaModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_AK, params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIdModuloEnte] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdModuloEnte] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	public AreaModuloEntity findByIdModulo(Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (AreaModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIdModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	public AreaModuloEntity findByCdModulo(String codiceModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_modulo", codiceModulo);
			return (AreaModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD_MODULO, params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByCdModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByCdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	
	@Override
	public List<AreaModuloEntity> find(AreaModuloFilter filter) throws DAOException {
		List<AreaModuloEntity> result = null;
		log.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);
		
		StringBuilder sb = new StringBuilder(FIND);
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		// Filtro
		sb.append(readFilter(filter, params));
		
		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}
		
		log.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<AreaModuloEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(AreaModuloEntity.class));
		
		return result;
	}
	
	private StringBuilder readFilter(AreaModuloFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter!=null) {
			if (filter.getIdArea().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_area = :id_area");
				params.addValue("id_area", filter.getIdArea().get());
			}
			if (filter.getIdEnte().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_ente = :id_ente");
				params.addValue("id_ente", filter.getIdEnte().get());
			}
			if (filter.getIdModulo().isPresent()) {
				sb = appendWhereOrAnd(sb).append("id_modulo = :id_modulo");
				params.addValue("id_modulo", filter.getIdModulo().get());
			}
		}
		return sb;
	}
	

	// id_area, id_ente, codice_area, nome_area, data_upd, attore_upd
//    private MapSqlParameterSource mapEntityParameters(AreaEntity entity) {
//    	MapSqlParameterSource params = new MapSqlParameterSource();
//    	
//    	params.addValue("id_area", entity.getIdArea());
//    	params.addValue("id_ente", entity.getIdEnte());
//    	params.addValue("codice_area", entity.getCodiceArea());
//    	params.addValue("nome_area" , entity.getNomeArea(), Types.VARCHAR);
//    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
//    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
//    	return params;
//    }

}
