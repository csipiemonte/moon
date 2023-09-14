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

import it.csi.moon.commons.entity.FruitoreEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.FruitoreDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai dati del Fruitore
 * 
 * @see FruitoreEntity
 * 
 * @author Danilo Mosca
 *
 * @since 1.0.0
 */
@Component
public class FruitoreDAOImpl extends JdbcTemplateDAO implements FruitoreDAO {
	
	
	private static final String CLASS_NAME = "FruitoreDAOImpl";
	
	private static final  String FIND_BY_CODICE  = "SELECT id_fruitore, desc_fruitore " +
			"FROM moon_wf_d_fruitore WHERE desc_fruitore = :codice";
	
	private static final  String FIND_BY_ID  = "SELECT id_fruitore, desc_fruitore " +
			"FROM moon_wf_d_fruitore WHERE id_fruitore = :id_fruitore";
	
	private static final  String FIND_BY_ID_MODULO  = "SELECT fr.id_fruitore, fr.desc_fruitore " +
			"FROM moon_wf_d_fruitore fr, moon_wf_r_fruitore_modulo frmd WHERE fr.id_fruitore = frmd.id_fruitore AND frmd.id_modulo = :id_modulo";
	
	private static final  String COUNT_BY_ID_MODULO  = "SELECT count(*) " +
			"FROM moon_wf_d_fruitore fr, moon_wf_r_fruitore_modulo frmd  WHERE fr.id_fruitore = frmd.id_fruitore and frmd.id_modulo = :id_modulo and fr.id_fruitore = :id_fruitore";	
	
	private static final  String COUNT_BY_ID_ENTE  = "SELECT count(*) " +
			"FROM moon_wf_d_fruitore fr, moon_wf_r_fruitore_ente fre WHERE fr.id_fruitore = fre.id_fruitore and fre.id_ente = :id_ente and fr.id_fruitore = :id_fruitore";
	private static final  String COUNT_BY_CODICE_ENTE  = "SELECT count(*) " +
			"FROM moon_wf_d_fruitore fr, moon_wf_r_fruitore_ente fre, moon_fo_d_ente e WHERE fr.id_fruitore = :id_fruitore AND fre.id_fruitore=fr.id_fruitore AND e.id_ente=fre.id_ente AND e.codice_ente = :codice_ente";
	private static final  String COUNT_BY_CODICE_ENTE_AREA  = "SELECT count(*) " +
			"FROM moon_wf_d_fruitore fr, moon_wf_r_fruitore_ente fre, moon_fo_d_area a WHERE fr.id_fruitore = :id_fruitore AND fre.id_fruitore=fr.id_fruitore AND a.id_ente=fre.id_ente AND a.codice_area = :codice_area";
	
	private static final String FIND = "SELECT id_fruitore, desc_fruitore FROM moon_wf_d_fruitore" ;
	
	private static final String INSERT = "INSERT INTO moon_wf_d_fruitore(id_fruitore, desc_fruitore) VALUES (:id_fruitore, :desc_fruitore)";	

	private static final String DELETE = "DELETE FROM moon_wf_d_fruitore WHERE id_fruitore = :id_fruitore";

	private static final  String UPDATE = "UPDATE moon_wf_d_fruitore SET desc_fruitore=:desc_fruitore WHERE id_fruitore = :id_fruitore";

	private static final String SEQ_ID = "SELECT nextval('moon_t_fruitore_id_fruitore_seq')";
	 
	
	/**
	 * Restituisce il fruitore per {@code codice}
	 * 
	 * @param {@code idFruitore} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il fruitore, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public FruitoreEntity getFruitoreByCodice(String codice) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice", codice);
			return (FruitoreEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CODICE, params, BeanPropertyRowMapper.newInstance(FruitoreEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::getFruitoreByCodice] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getFruitoreByCodice] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	/**
	 * Restituisce il fruitore per {@code idFruitore}
	 * 
	 * @param {@code idFruitore} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il fruitore, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public FruitoreEntity getFruitoreById(Integer idFruitore) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_fruitore", idFruitore);
			return (FruitoreEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(FruitoreEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::getFruitoreById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getFruitoreById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
		
	/**
	 * Restituisce il numero di fruitori associati a modulo 
	 * 
	 * @param idModulo
	 * @param idFruitore
	 * 
	 * @return il numero di fruitori associati.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public Integer countByIdModulo(Long idModulo, Integer idFruitore) throws DAOException {
		Integer result = null;
		try {		
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("id_fruitore", idFruitore);
			
			LOG.debug("[" + CLASS_NAME + "::countByIdModulo] IN id_modulo: "+idModulo);
			LOG.debug("[" + CLASS_NAME + "::countByIdModulo] IN id_fruitore: "+idFruitore);
			
			StringBuilder sb = new StringBuilder(COUNT_BY_ID_MODULO);

			LOG.debug("[" + CLASS_NAME + "::countByIdModulo] params: "+params);
			result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sb.toString(), params);
			
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::countByIdModulo] ItemNotFoundDAOException idModulo: "+idModulo+"  id_fruitore: "+idFruitore);
			throw new ItemNotFoundDAOException("Fruitore non associato al modulo");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::countByIdModulo] Exception idModulo: "+idModulo+"  id_fruitore: "+idFruitore+"  database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		} finally {
			LOG.info("[" + CLASS_NAME + "::countByIdModulo] idModulo: "+idModulo+"  id_fruitore: "+idFruitore+"  result: "+result);
		}
	}
	
	/**
	 * Restituisce il numero di fruitori associato ad ente 
	 * 
	 * @param idEnte
	 * @param idFruitore
	 * 
	 * @return il numero di fruitori associati.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */		
	@Override
	public Integer countByIdEnte(Long idEnte, Integer idFruitore) throws DAOException {
		Integer result = null;
		try {			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ente", idEnte);
			params.addValue("id_fruitore", idFruitore);
		
			LOG.debug("[" + CLASS_NAME + "::countByIdEnte] IN id_ente: "+idEnte);
			LOG.debug("[" + CLASS_NAME + "::countByIdEnte] IN id_fruitore: "+idFruitore);
//			LOG.debug("[" + CLASS_NAME + "::countByIdEnte] params: "+params);
			
			result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(COUNT_BY_ID_ENTE, params);
				        
			return result;		
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::countByIdEnte] ItemNotFoundDAOException idEnte: "+idEnte+"  id_fruitore: "+idFruitore);
			throw new ItemNotFoundDAOException("Fruitore non associato ad ente");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::countByIdEnte] Exception id_ente: "+idEnte+"  id_fruitore: "+idFruitore+"  database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		} finally {
			LOG.info("[" + CLASS_NAME + "::countByIdEnte] id_ente: "+idEnte+"  id_fruitore: "+idFruitore+"  result: "+result);
		}
	}
	
	/**
	 * Restituisce il numero di fruitori associato ad ente 
	 * 
	 * @param codiceEnte
	 * @param idFruitore
	 * 
	 * @return il numero di fruitori associati.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */		
	@Override
	public Integer countByCodiceEnte(String codiceEnte, Integer idFruitore) throws DAOException {
		Integer result = null;
		try {			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_ente", codiceEnte);
			params.addValue("id_fruitore", idFruitore);
		
			LOG.debug("[" + CLASS_NAME + "::countByCodiceEnte] IN codice_ente: "+codiceEnte);
			LOG.debug("[" + CLASS_NAME + "::countByCodiceEnte] IN id_fruitore: "+idFruitore);
//			LOG.debug("[" + CLASS_NAME + "::countByIdEnte] params: "+params);
			
			result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(COUNT_BY_CODICE_ENTE, params);
				        
			return result;		
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::countByIdEnte] ItemNotFoundDAOException codiceEnte: "+codiceEnte+"  id_fruitore: "+idFruitore);
			throw new ItemNotFoundDAOException("Fruitore non associato ad ente");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::countByIdEnte] Exception codice_ente: "+codiceEnte+"  id_fruitore: "+idFruitore+"  database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		} finally {
			LOG.info("[" + CLASS_NAME + "::countByIdEnte] codice_ente: "+codiceEnte+"  id_fruitore: "+idFruitore+"  result: "+result);
		}
	}
	
	/**
	 * Restituisce il numero di fruitori associato all area 
	 * 
	 * @param codiceArea
	 * @param idFruitore
	 * 
	 * @return il numero di fruitori associati.
	 * 
	 * @throws ItemNotFoundDAOException se la risorsa non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */		
	@Override
	public Integer countByCodiceEnteArea(String codiceEnte, String codiceArea, Integer idFruitore) throws DAOException {
		Integer result = null;
		try {			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_ente", codiceEnte);
			params.addValue("codice_area", codiceArea);
			params.addValue("id_fruitore", idFruitore);
		
			LOG.debug("[" + CLASS_NAME + "::countByCodiceEnteArea] IN codice_ente: "+codiceEnte);
			LOG.debug("[" + CLASS_NAME + "::countByCodiceEnteArea] IN codice_area: "+codiceArea);
			LOG.debug("[" + CLASS_NAME + "::countByCodiceEnteArea] IN id_fruitore: "+idFruitore);
//			LOG.debug("[" + CLASS_NAME + "::countByCodiceEnteArea] params: "+params);
			
			result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(COUNT_BY_CODICE_ENTE_AREA, params);
				        
			return result;		
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::countByCodiceEnteArea] ItemNotFoundDAOException codiceArea: "+codiceArea+"  id_fruitore: "+idFruitore);
			throw new ItemNotFoundDAOException("Fruitore non associato ad ente");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::countByCodiceEnteArea] Exception codice_area: "+codiceArea+"  id_fruitore: "+idFruitore+"  database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		} finally {
			LOG.info("[" + CLASS_NAME + "::countByCodiceEnteArea] codice_area: "+codiceArea+"  id_fruitore: "+idFruitore+"  result: "+result);
		}
	}
	
	/**
	 * Restituisce tutti i fruitori presenti nel sistema
	 * 
	 * @return la lista di tutti i fruitori.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public List<FruitoreEntity> find() throws DAOException {
		List<FruitoreEntity> result = null;
		result = (List<FruitoreEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND, BeanPropertyRowMapper.newInstance(FruitoreEntity.class));
		return result;
	}
	
	@Override
	public List<FruitoreEntity> findByIdModulo(Long idModulo) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (List<FruitoreEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(FruitoreEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	/**
	 * Inserisce un fruitore nel sistema
	 * 
	 * @param {@code entity} fruitore da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idFruitore} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Integer insert(FruitoreEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Integer idFruitore = getCustomNamedParameterJdbcTemplateImpl().queryForInt(SEQ_ID, new MapSqlParameterSource());
			entity.setIdFruitore(idFruitore);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapFruitoreEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idFruitore;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}

	/**
	 * Aggiorna un fruitore nel sistema per chiave primaria {@code idFruitore} 
	 * 
	 * @param {@code entity} fruitore da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun fruitore aggiornato, 1 se fruitore aggiornato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(FruitoreEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapFruitoreEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO AREA");
		}
	}

	/**
	 * Cancella un area nel sistema per chiave primaria {@code idFruitore}.
	 * Per essere attuabile, non devono essere presenti referenze del fruitore da cancellare nelle entity collegate
	 * 
	 * @param {@code idFruitore} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun fruitore cancellato, 1 se fruitore cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Integer idFruitore) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idArea: "+idFruitore);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_fruitore", idFruitore);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}

	/**
	 * Cancella un fruitore dal sistema per entita fruitore {@code entity}. L'entita deve avere valorizzata almeno la sua chiave primaria {@code idFruitore}.
	 * Per essere attuabile, non devono essere presenti referenze del fruitore da cancellare nelle entity collegate
	 * 
	 * @param {@code entity} fruitore da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun fruitore cancellato, 1 se fruitore cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(FruitoreEntity entity) throws DAOException {
		return delete(entity.getIdFruitore());
	}

	
	// id_area, id_ente, codice_area, nome_area, data_upd, attore_upd
    private MapSqlParameterSource mapFruitoreEntityParameters(FruitoreEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();

    	params.addValue("id_fruitore", entity.getIdFruitore());
    	params.addValue("desc_fruitore", entity.getDescFruitore());

    	return params;
    }	
	




}
