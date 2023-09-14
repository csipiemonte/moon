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

import it.csi.moon.moonbobl.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AreaEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle aree di un ente su quale sara collocato i moduli
 * <br>Un modulo potra essere solo su un area di un ente, ma potra essere su piu area di diversi enti.
 * <br>
 * <br>Tabella moon_fo_d_area
 * <br>PK: idArea
 * <br>AK: idEnte,codiceArea
 * 
 * @see AreaEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

@Component
public class AreaDAOImpl extends JdbcTemplateDAO implements AreaDAO {
	
	private final static String CLASS_NAME = "AreaDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_area, id_ente, codice_area, nome_area, data_upd, attore_upd, email";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
			" FROM moon_fo_d_area" +
			" WHERE id_area = :id_area";
	
	private static final  String FIND_BY_CD  = FIND_SELECT_FIELDS +
			" FROM moon_fo_d_area" +
			" WHERE id_ente = :id_ente" +
			" AND codice_area = :codice_area";
	
	private static final  String FIND = FIND_SELECT_FIELDS +
			" FROM moon_fo_d_area";
	
	private static final  String FIND_BY_ID_ENTE = FIND_SELECT_FIELDS +
			" FROM moon_fo_d_area" +
			" WHERE id_ente = :id_ente";
	
	private static final  String FIND_BY_ID_MODULO  = "SELECT a.id_area, a.id_ente, a.codice_area, "
			+ "a.nome_area, a.data_upd, a.attore_upd, a.email" +
			" FROM moon_fo_d_area a, moon_fo_r_area_modulo r" +
			" WHERE a.id_area = r.id_area and id_modulo = :id_modulo";
	
	private static final String INSERT = "INSERT INTO moon_fo_d_area(" + 
			" id_area, id_ente, codice_area, nome_area, data_upd, attore_upd)" + 
			" VALUES (:id_area, :id_ente, :codice_area, :nome_area, :data_upd, :attore_upd)";

	private static final String DELETE = "DELETE FROM moon_fo_d_area WHERE id_area = :id_area";

	private static final  String UPDATE = "UPDATE moon_fo_d_area" +
			" SET  id_ente=:id_ente, codice_area=:codice_area, nome_area=:nome_area, data_upd=:data_upd, attore_upd=:attore_upd" +
			" WHERE id_area = :id_area";

	private static final String SEQ_ID = "SELECT nextval('moon_fo_d_area_id_area_seq')";	
	
	/**
	 * Restituisce l'area identificata per {@code idArea}
	 * 
	 * @param {@code idArea} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return l'area ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public AreaEntity findById(Long idArea) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_area", idArea);
			return (AreaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(AreaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce l'area identificata per {@code idArea}
	 * 
	 * @param idEnte
	 * @param codiceArea
	 * 
	 * @return l'area ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public AreaEntity findByCd(Long idEnte, String codiceArea) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ente", idEnte);
			params.addValue("codice_area", codiceArea);
			return (AreaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(AreaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce l'area identificata per {@code idModulo}
	 * 
	 * @param {@code idModulo} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return l'area ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public AreaEntity findByIdModulo(Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (AreaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(AreaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce tutte le aree presenti nel sistema
	 * 
	 * @return la lista di tutte le aree.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public List<AreaEntity> find() throws DAOException {
		List<AreaEntity> result = null;
		result = (List<AreaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND, BeanPropertyRowMapper.newInstance(AreaEntity.class));
		return result;
	}

	/**
	 * Restituisce le aree presenti nel sistema selezionate per id ente
	 * 
	 * @return la lista delle aree selezionate per id ente.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public List<AreaEntity> findByIdEnte(Long idEnte) throws DAOException {
		List<AreaEntity> result = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_ente", idEnte);
		result = (List<AreaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ENTE, params, BeanPropertyRowMapper.newInstance(AreaEntity.class));
		return result;
	}
	
	/**
	 * Inserisci un' area nel sistema
	 * 
	 * @param {@code entity} l'area da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idArea} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(AreaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idArea = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdArea(idArea);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idArea;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}

	/**
	 * Aggiorna un' area nel sistema sulla base della sua chiave primaria {@code idArea} 
	 * 
	 * @param {@code entity} area da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessuna area aggiornato, 1 se area aggiornata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(AreaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO AREA");
		}
	}

	/**
	 * Cancella un area nel sistema sulla base della sua chiave primaria {@code idArea}.
	 * Per essere attuabile, non devono essere presenti referenze dell' area da cancellare nelle entity collegate
	 * 
	 * @param {@code idArea} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessuna area cancellata, 1 se area cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Long idArea) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN idArea: "+idArea);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_area", idArea);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}

	/**
	 * Cancella un' area dal sistema perentita area {@code entity}. L'entita deve avere valorizzata almeno la sua chiave primaria {@code idArea}.
	 * Per essere attuabile, non devono essere presenti referenze dell'area da cancellare nelle entity collegate
	 * 
	 * @param {@code entity} l'area da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessuna area cancellata, 1 se area cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(AreaEntity entity) throws DAOException {
		return delete(entity.getIdArea());
	}

	

	// id_area, id_ente, codice_area, nome_area, data_upd, attore_upd
    private MapSqlParameterSource mapEntityParameters(AreaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_area", entity.getIdArea());
    	params.addValue("id_ente", entity.getIdEnte());
    	params.addValue("codice_area", entity.getCodiceArea());
    	params.addValue("nome_area" , entity.getNomeArea(), Types.VARCHAR);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	return params;
    }
}