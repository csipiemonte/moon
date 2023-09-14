/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;
import java.sql.Types;
/**
 * DAO per accesso alle Categorie
 * 
 */
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.CategoriaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.CategoriaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.CategoriaModuloEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

@Component
public class CategoriaDAOImpl extends JdbcTemplateDAO implements CategoriaDAO {

	private final static String CLASS_NAME = "CategoriaDAOImpl";
	
	private static final String FIND_BY_ID  = "SELECT id_categoria, nome_categoria, id_categoria_padre, data_upd, attore_upd from moon_fo_d_categoria where id_categoria = :id_categoria";
	private static final String ELENCO  = "SELECT id_categoria, nome_categoria, id_categoria_padre, data_upd, attore_upd from moon_fo_d_categoria";
	private static final String FIND_BY_ID_MODULO  = "SELECT cat.id_categoria, cat.nome_categoria, cat.id_categoria_padre, cat.data_upd, cat.attore_upd" 
												+ " FROM moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod "  
											    + " WHERE cat.id_categoria = catmod.id_categoria and " 
												+ " catmod.id_modulo = :id_modulo";
	
	private static final String INSERT_CATEGORIA_MODULO = "INSERT INTO moon_fo_r_categoria_modulo(" + 
			" id_categoria, id_modulo, data_upd, attore_upd)" + 
			" VALUES (:id_categoria, :id_modulo, :data_upd, :attore_upd)";


	private static final String DELETE_CATEGORIA_MODULO_BY_ID_MODULO = "DELETE FROM moon_fo_r_categoria_modulo WHERE id_modulo = :id_modulo";
	@Override
	public CategoriaEntity findById(long idCategoria) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_categoria", idCategoria);
			return (CategoriaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(CategoriaEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<CategoriaEntity> find() throws DAOException {
		return (List<CategoriaEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(CategoriaEntity.class));
	}

	@Override
	public List<CategoriaEntity> findByIdModulo(long idModulo) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (List<CategoriaEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(CategoriaEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Inserisce un record di relazione categoria/modulo nel sistema
	 * 
	 * @param {@code categoriaModuloEntity} il record di relazione da inserire. Cannot be {@code null}
	 * 
	 * @return il numero di righe inserite nel database
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int insertCategoriaModulo(CategoriaModuloEntity categoriaModuloEntity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insertCategoriaModulo] IN categoriaModuloEntity: "+categoriaModuloEntity);		
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_CATEGORIA_MODULO, mapCategoriaModuloEntityParameters(categoriaModuloEntity));
			log.debug("[" + CLASS_NAME + "::insertCategoriaModulo] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insertCategoriaModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}

	/**
	 * Cancella tutte le relazione categoria/modulo nel sistema di un modulo
	 * 
	 * @param {@code idModulo} il modulo di relazione da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int deleteCategoriaModuloByIdModulo(Long idModulo) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::deleteCategoriaModuloByIdModulo] IN idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_CATEGORIA_MODULO_BY_ID_MODULO, params);
			log.debug("[" + CLASS_NAME + "::deleteCategoriaModuloByIdModulo] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::deleteCategoriaModuloByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}

	private MapSqlParameterSource mapCategoriaModuloEntityParameters(CategoriaModuloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_categoria", entity.getIdCategoria());
		params.addValue("id_modulo", entity.getIdModulo());	
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_upd", entity.getAttoreUpd());

		return params;
	}
}
