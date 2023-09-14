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

import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.EnteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.EntiFilter;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai enti
 * <br>
 * <br>Tabella principale : moon_fo_d_ente
 * 
 * @see EnteEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/06/2020 - versione iniziale
 */
@Component
public class EnteDAOImpl extends JdbcTemplateDAO implements EnteDAO {

	private static final String CLASS_NAME = "EnteDAOImpl";

	private static final String SELECT_FIELDS = "SELECT id_ente, codice_ente, nome_ente, descrizione_ente, fl_attivo, id_tipo_ente, logo, indirizzo, data_upd, attore_upd, id_ente_padre, codice_ipa";

	private static final String FIND_BY_ID = SELECT_FIELDS+
		" FROM moon_fo_d_ente" +
		" WHERE id_ente = :id_ente";
	private static final String FIND_BY_CD = SELECT_FIELDS +
		" FROM moon_fo_d_ente" +
		" WHERE codice_ente = :codice_ente";
	
	private static final String FIND = SELECT_FIELDS +
		" FROM moon_fo_d_ente" +
		" WHERE 1 = 1";
	
	private static final String INSERT = "INSERT INTO moon_fo_d_ente(" +
		" id_ente, codice_ente, nome_ente, descrizione_ente, fl_attivo, id_tipo_ente, logo, indirizzo, data_upd, attore_upd, id_ente_padre, codice_ipa)" + 
		" VALUES (:id_ente, :codice_ente, :nome_ente, :descrizione_ente, :fl_attivo, :id_tipo_ente, :logo, :indirizzo, :data_upd, :attore_upd, :id_ente_padre, :codice_ipa)";

	private static final String DELETE = "DELETE FROM moon_fo_d_ente WHERE id_ente = :id_ente";

	private static final String UPDATE = "UPDATE moon_fo_d_ente" +
		" SET codice_ente=:codice_ente, nome_ente=:nome_ente, descrizione_ente=:descrizione_ente, fl_attivo=:fl_attivo, id_tipo_ente=:id_tipo_ente, logo=:logo, indirizzo=:indirizzo, data_upd=:data_upd, attore_upd=:attore_upd, id_ente_padre=:id_ente_padre, codice_ipa=:codice_ipa" +
		" WHERE id_ente = :id_ente";

	private static final String SEQ_ID = "SELECT nextval('moon_fo_d_enti_id_ente_seq')";


	/**
	 * Restituisce l'ente identificato per {@code idEnte}
	 * 
	 * @param {@code idEnte} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return l'ente ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public EnteEntity findById(Long idEnte) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ente", idEnte);
			return (EnteEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(EnteEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+idEnte,emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce l'ente identificato per {@code idEnte}
	 * 
	 * @param {@code idEnte} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return l'ente ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public EnteEntity findByCodice(String codiceEnte) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_ente", codiceEnte);
			return (EnteEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(EnteEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+codiceEnte,emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	/**
	 * Restituisce tutti gli enti presenti nel sistema
	 * 
	 * @return la lista di tutti gli enti.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public List<EnteEntity> find(EntiFilter filter) throws DAOException {
		List<EnteEntity> result = null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		StringBuilder sb = new StringBuilder(FIND)
			.append(readFilter(filter, params));
		
		log.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<EnteEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(EnteEntity.class));
		
		return result;
	}
	
	private StringBuilder readFilter(EntiFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter!=null) {
			if (filter.getIdEnte().isPresent()) {
				sb.append(" AND m.id_ente = :id_ente");
				params.addValue("id_ente", filter.getIdEnte().get());
			}
			if (filter.getCodiceEnte().isPresent()) {
				sb.append(" AND codice_ente = :codice_ente ");
				params.addValue("codice_ente", filter.getCodiceEnte().get());
			}
			if (filter.getNomeEnte().isPresent()) {
				sb.append(" AND nome_ente = :nome_ente");
				params.addValue("nome_ente", filter.getNomeEnte().get());
			}
			if (filter.getDescrizioneEnte().isPresent()) {
				sb.append(" AND descrizione_ente LIKE :descrizione_ente");
				params.addValue("descrizione_ente", "%"+filter.getDescrizioneEnte().get()+"%");
			}
			if (filter.getFlAttivo().isPresent()) {
				sb.append(" AND fl_attivo = :fl_attivo ");
				params.addValue("fl_attivo", filter.getFlAttivo().get());
			}
			if (filter.getIdTipoEnte().isPresent()) {
				sb.append(" AND id_tipo_ente = :id_tipo_ente ");
				params.addValue("id_tipo_ente", filter.getIdTipoEnte().get());
			}
			if (filter.getLogo().isPresent()) {
				sb.append(" AND logo = :logo ");
				params.addValue("logo", filter.getLogo().get());
			}
			if (filter.getNomePortale().isPresent()) {
				sb.append(" AND EXISTS (SELECT 1" + 
					" FROM moon_fo_r_portale_ente rpe INNER JOIN moon_fo_d_portale p on rpe.id_portale=p.id_portale" + 
					" WHERE rpe.id_ente = moon_fo_d_ente.id_ente" + 
					" AND p.nome_portale = :nome_portale)");
				params.addValue("nome_portale", filter.getNomePortale().get());
			}
			if (filter.getIdPortale().isPresent()) {
				sb.append(" AND EXISTS (SELECT 1" + 
					" FROM moon_fo_r_portale_ente rpe" + 
					" WHERE rpe.id_ente = moon_fo_d_ente.id_ente" + 
					" AND rpe.id_portale = :id_portale)");
				params.addValue("id_portale", filter.getIdPortale().get());
			}
			if (filter.getCodiceEnte().isPresent()) {
				sb.append(" AND codice_ipa = :codice_ipa ");
				params.addValue("codice_ipa", filter.getCodiceIpa().get());
			}
			if (filter.getUtenteAbilitato().isPresent()) {				
				sb.append(" AND EXISTS (SELECT 1" + 
					" FROM moon_fo_t_utente u INNER JOIN moon_fo_r_utente_area_ruolo uar ON (uar.id_utente = u.id_utente)" +
					" INNER JOIN moon_fo_d_area a ON (a.id_area = uar.id_area)" +
					" WHERE u.identificativo_utente = :identificativo_utente" +
					" AND a.id_ente = moon_fo_d_ente.id_ente)");
				params.addValue("identificativo_utente", filter.getUtenteAbilitato().get().toUpperCase());
			}
		}
		return sb;
	}
	
	/**
	 * Inserisce un ente nel sistema
	 * 
	 * @param {@code entity} l'ente da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idEnte} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(EnteEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idEnte = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdEnte(idEnte);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEnteEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idEnte;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}

	/**
	 * Aggiorna un ente nel sistema sulla base della sua chiave primaria {@code idEnte} 
	 * 
	 * @param {@code entity} il ente da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun ente aggiornato, 1 se ente aggiornato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(EnteEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEnteEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ENTE");
		}
	}

	/**
	 * Cancella un ente dal sistema per chiave primaria {@code idEnte}.
	 * Per essere attuabile, non devono essere presenti referenze dell'ente in cancellazione nelle entity collegate
	 * 
	 * @param {@code idEnte} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ente cancellato, 1 se ente cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Long idEnte) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN idEnte: "+idEnte);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ente", idEnte);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}

	/**
	 * Cancella un' ente dal sistema per entita Ente {@code entity}. L'entita deve avere valorizzata almeno la sua chiave primaria {@code idEnte}.
	 * Per essere attuabile, non devono essere presenti referenze dell'ente in cancellazione nelle entity collegate
	 * 
	 * @param {@code entity} l'ente da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ente cancellato, 1 se ente cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(EnteEntity entity) throws DAOException {
		return delete(entity.getIdEnte());
	}	
	
	
	// id_ente, codice_ente, nome_ente, descrizione_ente, fl_attivo, id_tipo_ente, logo, indirizzo, data_upd, attore_upd, id_ente_padre
    private MapSqlParameterSource mapEnteEntityParameters(EnteEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_ente", entity.getIdEnte());
    	params.addValue("codice_ente", entity.getCodiceEnte());
    	params.addValue("nome_ente", entity.getNomeEnte());
    	params.addValue("descrizione_ente" , entity.getDescrizioneEnte());
    	params.addValue("fl_attivo", entity.getFlAttivo());
    	params.addValue("id_tipo_ente", entity.getIdTipoEnte());
    	params.addValue("logo" , entity.getLogo());
    	params.addValue("indirizzo", entity.getIndirizzo());
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP ); 
    	params.addValue("attore_upd", entity.getAttoreUpd());
    	params.addValue("id_ente_padre", entity.getIdEntePadre());
    	params.addValue("codice_ipa", entity.getCodiceIpa());
    	
    	return params;
    }

}
