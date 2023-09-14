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

import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.MyDocsParametroEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.MyDocsParametroDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai parametri di configurazione per la pubblicazione su MyDocs (DOCME)
 * <br>
 * <br>Tabella principale : moon_md_d_parametri
 * 
 * @see MyDocsParametroEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 07/02/2023 - versione iniziale
 */
@Component
public class MyDocsParametroDAOImpl extends JdbcTemplateDAO implements MyDocsParametroDAO {

	private static final String CLASS_NAME = "MyDocsParametroDAOImpl";
	
	private static final String SELECT_FILEDS = "SELECT id_parametro, id_ente, id_area, id_modulo, nome_attributo, valore, data_upd, attore_upd ";
	private static final String FIND_BY_ID = SELECT_FILEDS +
			" FROM moon_md_d_parametri WHERE id_parametro = :id_parametro";
	
	private static final String FIND_FOR_MODULO = SELECT_FILEDS + " FROM (\n" + 
			" select 1 priority, * \n" + 
			" FROM moon_md_d_parametri\n" + 
			" where id_ente = :id_ente and id_area is null and id_modulo is null\n" + 
			" union all \n" + 
			" select 2 priority, * \n" + 
			" FROM moon_md_d_parametri\n" + 
			" where id_ente = :id_ente and id_area = :id_area2 and id_modulo is null\n" + 
			" union all \n" + 
			" select 3 priority, * \n" + 
			" FROM moon_md_d_parametri\n" + 
			" where id_ente = :id_ente and id_area = :id_area3 and id_modulo = :id_modulo\n" + 
			") p ORDER BY p.priority";
	
	private static final String FIND_ALL_BY_ID_MODULO = SELECT_FILEDS + " FROM (\n" + 
			" select 1 priority, * \n" + 
			" FROM moon_md_d_parametri\n" + 
			" where id_ente in (select distinct id_ente from moon_fo_r_area_modulo where id_modulo=:id_modulo)  and id_area is null and id_modulo is null\n" + 
			" union all \n" + 
			" select 2 priority, * \n" + 
			" FROM moon_md_d_parametri\n" + 
			" where (id_ente,id_area) in (select distinct id_ente,id_area from moon_fo_r_area_modulo where id_modulo=:id_modulo) and id_modulo is null\n" + 
			" union all \n" + 
			" select 3 priority, * \n" + 
			" FROM moon_md_d_parametri\n" + 
			" where (id_ente,id_area,id_modulo) in (select distinct id_ente,id_area,id_modulo from moon_fo_r_area_modulo where id_modulo=:id_modulo)\n" + 
			") p ORDER BY p.priority";
	
	private static final  String INSERT = "INSERT INTO moon_md_d_parametri (id_parametro, id_ente, id_area, id_modulo, nome_attributo, valore, data_upd, attore_upd)" + 
			" VALUES (:id_parametro, :id_ente, :id_area, :id_modulo, :nome_attributo, :valore, :data_upd, :attore_upd)";

	private static final  String UPDATE = "UPDATE moon_md_d_parametri" +
			" SET id_ente=:id_ente, id_area=:id_area, id_modulo=:id_modulo, nome_attributo=:nome_attributo, valore=:valore, data_upd=:data_upd, attore_upd=:attore_upd" +
			" WHERE id_parametro = :id_parametro";

	private static final String DELETE = "DELETE FROM moon_md_d_parametri WHERE id_parametro = :id_parametro";
		
	private static final String SEQ_ID = "SELECT nextval('moon_md_d_parametri_id_parametro_seq')";
	
	@Override
	public List<MyDocsParametroEntity> findForModulo(Long idEnte, Long idArea, Long idModulo) throws DAOException {
		List<MyDocsParametroEntity> result = null;
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::findForModulo] IN idEnte:" + idEnte + "  idArea:" + idArea + "  idModulo:" + idModulo);
			}
			if (idEnte==null || idArea==null || idModulo==null ) {
				LOG.error("[" + CLASS_NAME + "::findForModulo] ArgumentException 3 must be not null: idEnte:" + idEnte + "  idArea:" + idArea + "  idModulo:" + idModulo);
				throw new DAOException("ArgumentException");
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ente", idEnte);
			params.addValue("id_area2", idArea);
			params.addValue("id_area3", idArea);
			params.addValue("id_modulo", idModulo);
			result = ( List<MyDocsParametroEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_FOR_MODULO,
				params, BeanPropertyRowMapper.newInstance(MyDocsParametroEntity.class));
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findForModulo] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<MyDocsParametroEntity> findForModulo(AreaModuloEntity areaModulo) throws DAOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::findForModulo] IN areaModulo:" + areaModulo);
		}
		return findForModulo(areaModulo.getIdEnte(), areaModulo.getIdArea(), areaModulo.getIdModulo());
	}
	
	@Override
	public List<MyDocsParametroEntity> findAllByIdModulo(Long idModulo) throws DAOException {
		List<MyDocsParametroEntity> result = null;
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::findAllByIdModulo] IN idModulo:" + idModulo);
			}
			if (idModulo==null ) {
				LOG.error("[" + CLASS_NAME + "::findAllByIdModulo] ArgumentException must be not null: idModulo:" + idModulo);
				throw new DAOException("ArgumentException");
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			result = ( List<MyDocsParametroEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_ALL_BY_ID_MODULO,
				params, BeanPropertyRowMapper.newInstance(MyDocsParametroEntity.class));
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findAllByIdModulo] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public MyDocsParametroEntity findById(Long idParametro) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_parametro", idParametro);
			return (MyDocsParametroEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
					FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(MyDocsParametroEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public Long insert(MyDocsParametroEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idParametro = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdParametro(idParametro);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapMyDocsParametroEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idParametro;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT PROTOCOLLO PARAMETRO");
		}
	}

	@Override
	public int update(MyDocsParametroEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapMyDocsParametroEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE PROTOCOLLO PARAMETRO");
		}
	}
	

	@Override
	public int delete(Long idAttributo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idAttributo: "+idAttributo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_attributo", idAttributo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE DELETE MYDOCS PARAMETRO");
		}
	}
	
    private MapSqlParameterSource mapMyDocsParametroEntityParameters(MyDocsParametroEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_parametro", entity.getIdParametro());
    	params.addValue("id_ente", entity.getIdEnte());
    	params.addValue("id_area", entity.getIdArea());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("nome_attributo" , entity.getNomeAttributo());
    	params.addValue("valore", entity.getValore());
    	params.addValue("data_upd" , entity.getDataUpd());
    	params.addValue("attore_upd", entity.getAttoreUpd());
    	
    	return params;
    }


}
