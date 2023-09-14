/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.ModuloStrutturaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloStrutturaEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla Struttura di un modulo
 * 
 * @see ModuloStrutturaEntity
 * 
 * @author Francesco
 * @author Laurent
 *
 */
@Component
public class ModuloStrutturaDAOImpl extends JdbcTemplateDAO implements ModuloStrutturaDAO {
	private final static String CLASS_NAME = "ModuloStrutturaDAOImpl";
	
	private final static String FIND_BY_ID = "SELECT id_modulostruttura as id_modulo_struttura, id_versione_modulo, id_modulo, struttura, tipo_struttura" + // , json_struttura
		" FROM moon_io_d_modulostruttura WHERE id_modulostruttura = :id_modulostruttura";
	private final static String FIND_BY_ID_MODULO = "SELECT id_modulostruttura as id_modulo_struttura, id_versione_modulo, id_modulo, struttura, tipo_struttura" +
		" FROM moon_io_d_modulostruttura WHERE id_modulo = :id_modulo";
	private final static String FIND_BY_ID_VERSIONE_MODULO = "SELECT id_modulostruttura as id_modulo_struttura, id_versione_modulo, id_modulo, struttura, tipo_struttura" +
		" FROM moon_io_d_modulostruttura WHERE id_versione_modulo = :id_versione_modulo";
	
	private static final  String INSERT = "INSERT INTO moon_io_d_modulostruttura (id_modulostruttura, id_versione_modulo, id_modulo, struttura, tipo_struttura)" + 
		" VALUES (:id_modulo_struttura, :id_versione_modulo, :id_modulo, :struttura, :tipo_struttura)";

	private static final  String UPDATE = "UPDATE moon_io_d_modulostruttura" +
		" SET id_versione_modulo=:id_versione_modulo, id_modulo=:id_modulo, struttura=:struttura, tipo_struttura=:tipo_struttura" +
		" WHERE id_modulostruttura = :id_modulo_struttura";

	private static final String DELETE = "DELETE FROM moon_io_d_modulostruttura WHERE id_versione_modulo = :id_versione_modulo";
	
	private static final String COPY = "INSERT INTO moon_io_d_modulostruttura (id_modulostruttura,id_modulo,id_versione_modulo,tipo_struttura,struttura)" +
		" SELECT :id_modulo_struttura, id_modulo, :to_id_versione_modulo, tipo_struttura, struttura" +
		" FROM moon_io_d_modulostruttura sm WHERE sm.id_modulo=:id_modulo AND id_versione_modulo=:from_id_versione_modulo";
	
	static final String SEQ_ID = "SELECT nextval('moon_io_d_modulostruttura_id_modulostruttura_seq')";
	
	@Deprecated
	@Override
	public ModuloStrutturaEntity findByIdModulo(Long idModulo) throws DAOException {

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (ModuloStrutturaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_MODULO,
					params, BeanPropertyRowMapper.newInstance(ModuloStrutturaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIdModulo] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	
	@Override
	public ModuloStrutturaEntity findByIdVersioneModulo(Long idVersioneModulo) throws DAOException {

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			return (ModuloStrutturaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_VERSIONE_MODULO,
					params, BeanPropertyRowMapper.newInstance(ModuloStrutturaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIdModulo] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloStrutturaEntity findById(Long idStruttura) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulostruttura", idStruttura);
			return (ModuloStrutturaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
					FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ModuloStrutturaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public Long insert(ModuloStrutturaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idStruttura = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdModuloStruttura(idStruttura);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapModuloStrutturaEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idStruttura;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO MODULO STRUTTURA");
		}
	}

	@Override
	public Long copy(Long idModulo, Long fromIdVersioneModulo, Long toIdVersioneModulo) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::copy] IN idModulo: "+idModulo+"  fromIdVersioneModulo: " +fromIdVersioneModulo+"  toIdVersioneModulo: " +toIdVersioneModulo);
			Long idStruttura = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo_struttura", idStruttura);
			params.addValue("id_modulo", idModulo);
			params.addValue("from_id_versione_modulo", fromIdVersioneModulo);
			params.addValue("to_id_versione_modulo", toIdVersioneModulo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(COPY, params);
			log.debug("[" + CLASS_NAME + "::copy] Record inseriti: " + numRecord);
			return idStruttura;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::copy] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE COPY MODULO STRUTTURA");
		}
	}
	
	@Override
	public int update(ModuloStrutturaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapModuloStrutturaEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO STRUTTURA");
		}
	}
	
	
	@Override
	public int delete(Long idVersioneModulo) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} 
		catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
    private MapSqlParameterSource mapModuloStrutturaEntityParameters(ModuloStrutturaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_modulo_struttura", entity.getIdModuloStruttura());
    	params.addValue("id_versione_modulo", entity.getIdVersioneModulo());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("struttura" , entity.getStruttura());
    	params.addValue("tipo_struttura", entity.getTipoStruttura());
    	return params;
    }
}
