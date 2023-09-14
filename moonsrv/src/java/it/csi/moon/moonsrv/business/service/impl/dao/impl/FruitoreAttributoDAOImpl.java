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

import it.csi.moon.commons.entity.FruitoreAttributoEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.FruitoreAttributoDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai attributi di un fruitore
 * 
 * @see FruitoreAttributoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class FruitoreAttributoDAOImpl extends JdbcTemplateDAO implements FruitoreAttributoDAO {
	private static final String CLASS_NAME = "FruitoreAttributoDAOImpl";
	
	private static final String FIND_BY_ID = "SELECT id_attributo, id_fruitore, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_wf_t_fruitore_attributo WHERE id_attributo = :id_attributo";
	private static final String FIND_BY_NOME = "SELECT id_attributo, id_fruitore, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_wf_t_fruitore_attributo WHERE id_fruitore = :id_fruitore AND nome_attributo = :nome_attributo";
	private static final String FIND_BY_ID_FRUITORE = "SELECT id_attributo, id_fruitore, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_wf_t_fruitore_attributo WHERE id_fruitore = :id_fruitore";
	
	private static final  String INSERT = "INSERT INTO moon_wf_t_fruitore_attributo (id_attributo, id_fruitore, nome_attributo, valore, data_upd, attore_upd)" + 
			" VALUES (:id_attributo, :id_fruitore, :nome_attributo, :valore, :data_upd, :attore_upd)";

	private static final  String UPDATE = "UPDATE moon_wf_t_fruitore_attributo" +
			" SET id_fruitore=:id_fruitore, nome_attributo=:nome_attributo, valore=:valore, data_upd=:data_upd, attore_upd=:attore_upd" +
			" WHERE id_attributo = :id_attributo";

	private static final String DELETE = "DELETE FROM moon_wf_t_fruitore_attributo WHERE id_attributo = :id_attributo";
	private static final String DELETE_ALL_BY_MODULO = "DELETE FROM moon_wf_t_fruitore_attributo WHERE id_fruitore = :id_fruitore";
		
	private static final String SEQ_ID = "SELECT nextval('moon_wf_t_fruitore_attributo_id_attributo_seq')";
	
	@Override
	public List<FruitoreAttributoEntity> findByIdFruitore(Integer idFruitore) {
		List<FruitoreAttributoEntity> result = null;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_fruitore", idFruitore);
			result = (List<FruitoreAttributoEntity>)  getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_FRUITORE,
					params, BeanPropertyRowMapper.newInstance(FruitoreAttributoEntity.class));
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdFruitore] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FRUITORE ATTRIBUTI");
		}
	}
	
	@Override
	public FruitoreAttributoEntity findById(Long idAttributo) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_attributo", idAttributo);
			return (FruitoreAttributoEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
					FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(FruitoreAttributoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FRUITORE ATTRIBUTO ID");
		}
	}
	
	@Override
	public FruitoreAttributoEntity findByNome(Integer idFruitore, String nomeAttributo) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_fruitore", idFruitore);
			params.addValue("nome_attributo", nomeAttributo);
			return (FruitoreAttributoEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
					FIND_BY_NOME, params, BeanPropertyRowMapper.newInstance(FruitoreAttributoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByNome] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByNome] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FRUITORE ATTRIBUTO NOME");
		}
	}
	
	@Override
	public FruitoreAttributoEntity findByAttributo(Integer idFruitore, FruitoreAttributoEnum attributo) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_fruitore", idFruitore);
			params.addValue("nome_attributo", attributo);
			return (FruitoreAttributoEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
					FIND_BY_NOME, params, BeanPropertyRowMapper.newInstance(FruitoreAttributoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByAttributo] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByAttributo] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE FRUITORE ATTRIBUTO ENUM");
		}
	}
	
	@Override
	public Long insert(FruitoreAttributoEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idAttributo = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdAttributo(idAttributo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapFruitoreAttributoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idAttributo;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO FRUITORE ATTRIBUTO");
		}
	}

	@Override
	public int update(FruitoreAttributoEntity entity) {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapFruitoreAttributoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO FRUITORE ATTRIBUTO");
		}
	}
	

	@Override
	public int delete(Long idAttributo) {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idAttributo: "+idAttributo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_attributo", idAttributo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE DELETE FRUITORE ATTRIBUTO");
		}
	}

	@Override
	public int deleteAllByIdFruitore(Long idFruitore) {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteAllByIdFruitore] IN idFruitore: "+idFruitore);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_fruitore", idFruitore);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_ALL_BY_MODULO, params);
			LOG.debug("[" + CLASS_NAME + "::deleteAllByIdFruitore] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::deleteAllByIdFruitore] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE DELETE FRUITORE ATTRIBUTI");
		}
	}
	
    private MapSqlParameterSource mapFruitoreAttributoEntityParameters(FruitoreAttributoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_attributo", entity.getIdAttributo());
    	params.addValue("id_fruitore", entity.getIdFruitore());
    	params.addValue("nome_attributo" , entity.getNomeAttributo());
    	params.addValue("valore", entity.getValore());
    	params.addValue("data_upd" , entity.getDataUpd());
    	params.addValue("attore_upd", entity.getAttoreUpd());
    	
    	return params;
    }

}
