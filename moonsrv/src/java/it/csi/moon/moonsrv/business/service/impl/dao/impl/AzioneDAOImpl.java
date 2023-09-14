/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.AzioneEntity;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.moonsrv.business.service.impl.dao.AzioneDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle azioni del workflow
 * 
 * @see AzioneEntity
 * 
 * @author Danilo
 * @author Laurent
 * 
 * @since 1.0.0
 */

@Component
public class AzioneDAOImpl extends JdbcTemplateDAO implements AzioneDAO {

	private static final String CLASS_NAME = "AzioneDAOImpl";

	private static final String ID_AZIONE = "id_azione";
	private static final String CODICE_AZIONE = "codice_azione";
	private static final String NOME_AZIONE = "nome_azione";
	
	private static final String SELECT_FIELDS = "SELECT id_azione, nome_azione, desc_azione, codice_azione";
	private static final String FROM_MOON_WF_D_AZIONE = " FROM moon_wf_d_azione";
	
	private static final String FIND_BY_ID = SELECT_FIELDS +
			FROM_MOON_WF_D_AZIONE +
			" WHERE id_azione = :id_azione";
	
	private static final String FIND_BY_NOME = SELECT_FIELDS +
			FROM_MOON_WF_D_AZIONE +
			" WHERE nome_azione = :nome_azione";	
	
	private static final String FIND_BY_CD = SELECT_FIELDS +
			FROM_MOON_WF_D_AZIONE +
			" WHERE codice_azione = :codice_azione";
	
	private static final String FIND = SELECT_FIELDS +
			FROM_MOON_WF_D_AZIONE;
	
	
	private static final String INSERT = "INSERT INTO moon_wf_d_azione(" +
		" id_azione, nome_azione, desc_azione, codice_azione)" + 
		" VALUES (:id_azione, :nome_azione, :desc_azione, :codice_azione)";

	private static final String DELETE = "DELETE FROM moon_wf_d_azione WHERE id_azione = :id_azione";

	private static final String UPDATE = "UPDATE moon_wf_d_azione" +
		" SET nome_azione=:nome_azione, desc_azione=:desc_azione, codice_azione=:codice_azione" +
		" WHERE id_azione = :id_azione";

	private static final String SEQ_ID = "SELECT nextval('moon_wf_d_azione_id_azione_seq')";
	
	private static final Duration DURATION_CACHE = Duration.ofMinutes(30);
	private static Map<String, AzioneEntity> cache = new HashMap<>(); // by codiceAzione
	private LocalTime lastResetCache = LocalTime.now();
	
	@Override
	public AzioneEntity findById(Long idAzione) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_AZIONE, idAzione);
			return (AzioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(AzioneEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato per idAzione:"+idAzione, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public AzioneEntity findByNome(String nomeAzione) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(NOME_AZIONE, nomeAzione);		
			return (AzioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_NOME, params, BeanPropertyRowMapper.newInstance(AzioneEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato per nomeAzione:"+nomeAzione, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public AzioneEntity findByCd(String codiceAzione) throws ItemNotFoundDAOException, DAOException {
		try {
			AzioneEntity result = cache.get(codiceAzione);
			if(result==null) {
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue(CODICE_AZIONE, codiceAzione);		
				result = (AzioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(AzioneEntity.class)  );
				cache.put(codiceAzione, result);
			}
			return result;
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato per codiceAzione:"+codiceAzione, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	// Ricerca dell ID azione
	@Override
	public Long findIdByCd(String codiceAzione) throws ItemNotFoundDAOException, DAOException {
		return findByCd(codiceAzione).getIdAzione();
	}
	@Override
	public Long findId(DecodificaAzione decodificaAzione) throws ItemNotFoundDAOException, DAOException {
		return findByCd(decodificaAzione.getCodice()).getIdAzione();
	}
	
	@Override
	public List<AzioneEntity> find() throws DAOException {
		return (List<AzioneEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND, BeanPropertyRowMapper.newInstance(AzioneEntity.class));
	}



	/**
	 * Inserisce un azione nel sistema
	 * 
	 * @param {@code entity} l'azione da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idAzione} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(AzioneEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idAzione = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdAzione(idAzione);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapAzioneEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idAzione;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE INSERIMENTO AZIONE");
		}

	}

	/**
	 * Aggiorna un azione nel sistema sulla base della sua chiave primaria {@code idAzione} 
	 * 
	 * @param {@code entity} l'azione da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun azione aggiornato, 1 se azione aggiornata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(AzioneEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapAzioneEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO AZIONE");
		}
	}

	/**
	 * Cancella un azione dal sistema per chiave primaria {@code idAzione}.
	 * Per essere attuabile, non devono essere presenti referenze dell'azione in cancellazione nelle entity collegate
	 * 
	 * @param {@code idAzione} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun azione cancellata, 1 se azione cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Long idAzione) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idAzione: "+idAzione);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_AZIONE, idAzione);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE AZIONE");
		}
	}

	/**
	 * Cancella un'azione dal sistema per entita Azione {@code entity}. L'entita deve avere valorizzata almeno la sua chiave primaria {@code idAzione}.
	 * Per essere attuabile, non devono essere presenti referenze dell'azione in cancellazione nelle entity collegate
	 * 
	 * @param {@code entity} l'azione da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun azione cancellata, 1 se azione cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(AzioneEntity entity) throws DAOException {
		return delete(entity.getIdAzione());
	}
	
	// id_azione, nome_azione, desc_azione, codice_azione
	private MapSqlParameterSource mapAzioneEntityParameters(AzioneEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_azione", entity.getIdAzione());
		params.addValue("nome_azione" , entity.getNomeAzione(), Types.VARCHAR);
		params.addValue("desc_azione", entity.getDescAzione(), Types.VARCHAR);
		params.addValue("codice_azione", entity.getCodiceAzione(), Types.VARCHAR);
		return params;
	}

    
    
    //
    //
    public void forceResetAzioniCache() {
    	cache.clear();
    	lastResetCache = LocalTime.now();
    	LOG.info("[" + CLASS_NAME + "::forceResetAzioniCache] BEGIN END");
    }
    public void initCache() {
    	if (Duration.between(this.lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetAzioniCache();
    	}
    }
}
