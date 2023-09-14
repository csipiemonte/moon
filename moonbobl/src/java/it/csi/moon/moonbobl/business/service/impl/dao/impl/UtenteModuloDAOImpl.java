/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.UtenteModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteModuloAbilitatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteModuloEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per la gestione della relazione utente modulo
 * 
 * @see UtenteModuloEntity
 * 
 * @author Danilo
 */
@Component
public class UtenteModuloDAOImpl extends JdbcTemplateDAO implements UtenteModuloDAO {

	private final static String CLASS_NAME = "UtenteModuloDAOImpl";

	private static final String FIND_BY_ID_UTENTE_MODULO = "SELECT id_utente, id_modulo, data_upd, attore_upd" +
		" FROM moon_fo_r_utente_modulo" +
		" WHERE id_utente = :id_utente" +
		" AND id_modulo = :id_modulo";
	private static final String FIND_BY_CF_UTENTE_MODULO = "SELECT rum.id_utente, rum.id_modulo, rum.data_upd, rum.attore_upd" + 
		" FROM moon_fo_t_utente u, moon_fo_r_utente_modulo rum" + 
		" WHERE u.identificativo_utente = :identificativo_utente" + 
		" AND rum.id_utente = u.id_utente" + 
		" AND rum.id_modulo = :id_modulo";
	
	private static final String INSERT = "INSERT INTO moon_fo_r_utente_modulo(id_utente, id_modulo, data_upd, attore_upd)" +
		" VALUES (:id_utente, :id_modulo, :data_upd, :attore_upd)";
	
	private static final String DELETE_ALL_BY_ID_MODULO = "DELETE from moon_fo_r_utente_modulo where id_modulo = :id_modulo";

	private static final String FIND_UTENTI_MODULO = "SELECT u.id_utente, u.identificativo_utente, u.nome, u.cognome, u.email, u.fl_attivo, u.id_tipo_utente" +
		", rum.data_upd AS data_upd_abilitazione, rum.attore_upd AS attore_upd_abilitazione" +
		" FROM moon_fo_t_utente u, moon_fo_r_utente_modulo rum" +
		" WHERE rum.id_modulo = :id_modulo" +
		" AND rum.id_utente = u.id_utente";
	
	/**
	 * Restituisce l'entity che descrive la relazione utente modulo per chiave
	 * 
	 * @param {@code idUtente idModulo} chiave primaria. 
	 * 
	 * @return l'entity ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public UtenteModuloEntity findByIdUtenteModulo(Long idUtente, Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idUtente);
			params.addValue("id_modulo", idModulo);
			return (UtenteModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_UTENTE_MODULO, params, BeanPropertyRowMapper.newInstance(UtenteModuloEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.warn("[" + CLASS_NAME + "::findByIdUtenteModulo] Elemento non trovato for "+idUtente + "/" + idModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdUtenteModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}



	@Override
	public UtenteModuloEntity findByCfUtenteModulo(String identificativoUtente, Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("identificativo_utente", identificativoUtente);
			params.addValue("id_modulo", idModulo);
			return (UtenteModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CF_UTENTE_MODULO, params, BeanPropertyRowMapper.newInstance(UtenteModuloEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.warn("[" + CLASS_NAME + "::findByCfUtenteModulo] Elemento non trovato for "+identificativoUtente + "/" + idModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByCfUtenteModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	/**
	 * Inserisce un record di relazione tra utente e modulo
	 * 
	 * @param {@code entity} l'entity che descrive la relazione tra utente e modulo
	 * 
	 * @return 1 se il record è stato correttamente inserito
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int insert(UtenteModuloEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);		
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapUtenteModuloEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}

	private MapSqlParameterSource mapUtenteModuloEntityParameters(UtenteModuloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_utente", entity.getIdUtente());
		params.addValue("id_modulo", entity.getIdModulo());
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		
		return params;
	}


	@Override
	public int delete(UtenteModuloEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN entity: "+entity);				
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", entity.getIdUtente());
			params.addValue("id_modulo", entity.getIdModulo());	
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_ALL_BY_ID_MODULO, params);
			log.debug("[" + CLASS_NAME + "::delete] Record eliminati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE DELETE");
		}
	}
	
	@Override
	public int deleteAllByIdModulo(Long idModulo) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::deleteAllByIdModulo] IN id_modulo : "+idModulo);				
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);			
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_ALL_BY_ID_MODULO, params);
			log.debug("[" + CLASS_NAME + "::deleteAllByIdModulo] Record eliminati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::deleteAllByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE DELETE");
		}
	}


	@Override
	public List<UtenteModuloAbilitatoEntity> findUtenteModuloAbilitatoByIdModulo(Long idModulo) {
		log.debug("[" + CLASS_NAME + "::findUtenteModuloAbilitatoByIdModulo] IN id_modulo : "+idModulo);				
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_modulo", idModulo);	
		return ( List<UtenteModuloAbilitatoEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_UTENTI_MODULO, params, BeanPropertyRowMapper.newInstance(UtenteModuloAbilitatoEntity.class));
	}

}
