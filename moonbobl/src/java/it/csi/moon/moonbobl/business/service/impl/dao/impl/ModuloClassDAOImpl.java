/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;


import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloClassDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloClassEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;


@Component
public class ModuloClassDAOImpl extends JdbcTemplateDAO implements ModuloClassDAO{

	private final static String CLASS_NAME = "ModuloClassDAOImpl";
	//private static MoonCache cache = CacheManager.getInstance().getCache(DecodificaMoonCache.MODULO_CLASS);

	private static final String SEQ_ID = "SELECT nextval('moon_io_d_modulo_class_id_seq')";
	private static final String FIND_BY_ID_MODULO_TIPOLOGIA = "SELECT id, id_modulo, contenuto, nome_class, tipologia" +
			" FROM moon_io_d_modulo_class" +
			" WHERE id_modulo = :id_modulo" +
			" AND tipologia = :tipologia";

	private static final String FIND_BY_ID_MODULO = "SELECT id, id_modulo, contenuto, nome_class, tipologia" +
			" FROM moon_io_d_modulo_class" +
			" WHERE id_modulo = :id_modulo";

	private static final String UPDATE = "UPDATE moon_io_d_modulo_class" +
			" SET nome_class=:nome_class, contenuto=:contenuto " +
			" WHERE id_modulo=:id_modulo AND tipologia=:tipologia";

	private static final String INSERT = "INSERT INTO moon_io_d_modulo_class(" +
			" id, id_modulo, contenuto, nome_class, tipologia)" +
			" VALUES (:id, :id_modulo, :contenuto, :nome_class, :tipologia)";
   /*
	@Override
	public ModuloClassEntity findClassbyIdModuloTipologia(Long idModulo, int tipologia) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findClassbyIdModuloTipologia] BEGIN ");
			ModuloClassEntity moduloClassEntity = null;

			cache.resetCacheIfNecessary();
			moduloClassEntity = (ModuloClassEntity)cache.get(idModulo+"-"+tipologia);
			if ( moduloClassEntity  != null){
				return moduloClassEntity;
			}
			LOG.debug("[" + CLASS_NAME + "::findClassbyIdModuloTipologia] CALL DB ");
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("tipologia", tipologia);
			moduloClassEntity = (ModuloClassEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_MODULO_TIPOLOGIA, params, BeanPropertyRowMapper.newInstance(ModuloClassEntity.class) );

			if( moduloClassEntity != null ) {
				cache.put(idModulo+"-"+tipologia, moduloClassEntity );
			}

			return moduloClassEntity;
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.warn("[" + CLASS_NAME + "::findClassbyIdModuloTipologia] Elemento non trovato for modulo= "+ idModulo+" tipologia= "+tipologia);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findClassbyIdModuloTipologia] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}*/

	@Override
	public ModuloClassEntity findDBClassbyIdModuloTipologia(Long idModulo, int tipologia) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findDBClassbyIdModuloTipologia] BEGIN ");

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("tipologia", tipologia);
			ModuloClassEntity moduloClassEntity = getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_MODULO_TIPOLOGIA, params, BeanPropertyRowMapper.newInstance(ModuloClassEntity.class) );

			return moduloClassEntity;
		} catch (EmptyResultDataAccessException emptyEx) {
			log.warn("[" + CLASS_NAME + "::findDBClassbyIdModuloTipologia] Elemento non trovato for modulo= "+ idModulo+" tipologia= "+tipologia);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findDBClassbyIdModuloTipologia] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ModuloClassEntity> findDBClassbyIdModulo(Long idModulo) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findDBClassbyIdModulo] BEGIN ");

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			List<ModuloClassEntity> moduloClassEntity = (List<ModuloClassEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(ModuloClassEntity.class) );

			return moduloClassEntity;
		} catch (EmptyResultDataAccessException emptyEx) {
			log.warn("[" + CLASS_NAME + "::findDBClassbyIdModulo] Elemento non trovato for modulo= "+ idModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findDBClassbyIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public Long insertFileClass(ModuloClassEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdModulo(id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id", id);
			params.addValue("id_modulo", entity.getIdModulo());
	    	params.addValue("contenuto", entity.getContenuto());
	    	params.addValue("nome_class", entity.getNomeClass());
	    	params.addValue("tipologia", entity.getTipologia());

			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, params);
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (DuplicateKeyException de) {
			log.error("[" + CLASS_NAME + "::insert] DuplicateKeyException: ");
			throw new DAOException("record gia presente");
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ");
		}
	}

	@Override
	public int updateFileClass(ModuloClassEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateFileClass] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", entity.getIdModulo());
	    	params.addValue("contenuto", entity.getContenuto());
	    	params.addValue("nome_class", entity.getNomeClass());
	    	params.addValue("tipologia", entity.getTipologia());
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, params);
			log.debug("[" + CLASS_NAME + "::updateFileClass] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateFileClass] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO");
		}

	}


}
