/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Processo;
import it.csi.moon.commons.entity.ProcessoEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.ProcessoDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai processi
 * <br>
 * <br>Tabella moon_wf_d_processo
 * <br>PK: idProcesso
 * <br>AK: codiceProcesso
 * 
 * @see Processo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

@Component
public class ProcessoDAOImpl extends JdbcTemplateDAO implements ProcessoDAO {
	
	private static final String CLASS_NAME = "ProcessoDAOImpl";

	private static final String ID_PROCESSO = "id_processo";
	private static final String CODICE_PROCESSO = "codice_processo";
	private static final String NOME_PROCESSO = "nome_processo";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_processo, codice_processo, nome_processo, descrizione_processo, fl_attivo AS flagAttivo, data_upd, attore_upd";
	private static final String FIND_SELECT_FIELDS_P = "SELECT p.id_processo, p.codice_processo, p.nome_processo, p.descrizione_processo, p.fl_attivo AS flag_attivo, p.data_upd, p.attore_upd";
	private static final String FROM_MOON_WF_D_PROCESSO = " FROM moon_wf_d_processo";
	
	private static final String FIND_BY_ID = FIND_SELECT_FIELDS +
		FROM_MOON_WF_D_PROCESSO +
		" WHERE id_processo = :id_processo";
	
	private static final  String FIND_BY_CD  = FIND_SELECT_FIELDS +
		FROM_MOON_WF_D_PROCESSO +
		" WHERE codice_processo = :codice_processo";
	
	private static final  String FIND_BY_NAME  = FIND_SELECT_FIELDS +
		FROM_MOON_WF_D_PROCESSO +
		" WHERE nome_processo = :nome_processo";
	
	private static final  String FIND = FIND_SELECT_FIELDS +
		FROM_MOON_WF_D_PROCESSO +
		" WHERE fl_attivo='S'";

	private static final String FIND_BY_ID_MODULO = FIND_SELECT_FIELDS_P +
		" FROM moon_wf_d_processo p, moon_wf_r_modulo_processo pm" +
	    " WHERE p.id_processo = pm.id_processo" +
		" AND pm.id_modulo = :id_modulo";
	
	private static final String INSERT = "INSERT INTO moon_wf_d_processo(" +
		" id_processo, codice_processo, nome_processo, descrizione_processo, fl_attivo AS flagAttivo, data_upd, attore_upd)" + 
		" VALUES (:id_processo, :codice_processo, :nome_processo, :descrizione_processo, :fl_attivo, :data_upd, :attore_upd)";

	private static final String DELETE = "DELETE FROM moon_wf_d_processo WHERE id_processo = :id_processo";

	private static final String UPDATE = "UPDATE moon_wf_d_processo" +
		" SET codice_processo=:codice_processo, nome_processo=:nome_processo, descrizione_processo=:descrizione_processo, fl_attivo=:fl_attivo, data_upd=:data_upd, attore_upd=:attore_upd" +
		" WHERE id_processo = :id_processo";

	private static final String SEQ_ID = "SELECT nextval('moon_wf_d_processo_id_processo_seq')";


	/**
	 * Restituisce il processo identificato per {@code idProcesso}
	 * 
	 * @param {@code idProcesso} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il processo ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public ProcessoEntity findById(Long idProcesso) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_PROCESSO, idProcesso);
			return (ProcessoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ProcessoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce il processo identificato per {@code codiceProcesso}
	 * 
	 * @param codiceProcesso
	 * 
	 * @return il processo, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public ProcessoEntity findByCd(String codiceProcesso) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(CODICE_PROCESSO, codiceProcesso);
			return (ProcessoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(ProcessoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	/**
	 * Restituisce il Processo identificata per {@code nomeProcesso}
	 * 
	 * @param nomeProcesso
	 * 
	 * @return il Processo ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public ProcessoEntity findByNome(String nomeProcesso) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(NOME_PROCESSO, nomeProcesso);
			return (ProcessoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_NAME, params, BeanPropertyRowMapper.newInstance(ProcessoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce tutti portali associati al modulo
	 * 
	 * @return la lista dei portali associati al modulo
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public List<ProcessoEntity> findByIdModulo(long idModulo) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (List<ProcessoEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(ProcessoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce tutti portali attivi presenti nel sistema
	 * 
	 * @return la lista di tutti portali attivi
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public List<ProcessoEntity> find() throws DAOException {
		List<ProcessoEntity> result = null;
		result = (List<ProcessoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND, BeanPropertyRowMapper.newInstance(ProcessoEntity.class));
		return result;
	}


	/**
	 * Inserisce un processo nel sistema
	 * 
	 * @param {@code entity} il nuovo processo da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idProcesso} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(ProcessoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idProcesso = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdProcesso(idProcesso);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapProcessoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idProcesso;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO PROCESSO");
		}
	}

	/**
	 * Aggiorna un processo nel sistema sulla base della sua chiave primaria {@code idProcesso} 
	 * 
	 * @param {@code entity} il processo da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun ente aggiornato, 1 se processo aggiornato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(ProcessoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: " + entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapProcessoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO PROCESSO");
		}
	}

	/**
	 * Cancella un processo dal sistema per chiave primaria {@code idProcesso}.
	 * Per essere attuabile, non devono essere presenti referenze del processo in cancellazione nelle entity collegate
	 * 
	 * @param {@code idProcesso} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ente cancellato, 1 se processo cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Long idProcesso) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idProcesso: " + idProcesso);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_PROCESSO, idProcesso);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE PROCESSO");
		}
	}

	/**
	 * Cancella un processo dal sistema per entita Processo {@code entity}. L'entita deve avere valorizzata almeno la sua chiave primaria {@code idProcesso}.
	 * Per essere attuabile, non devono essere presenti referenze dell'ente in cancellazione nelle entity collegate
	 * 
	 * @param {@code entity} il processo da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ente cancellato, 1 se processo cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(ProcessoEntity entity) throws DAOException {
		return delete(entity.getIdProcesso());
	}
	
	
	// id_processo, codice_processo, nome_processo, descrizione_processo, fl_attivo AS flagAttivo, data_upd, attore_upd
    private MapSqlParameterSource mapProcessoEntityParameters(ProcessoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue(ID_PROCESSO, entity.getIdProcesso());
    	params.addValue(CODICE_PROCESSO, entity.getCodiceProcesso());
    	params.addValue(NOME_PROCESSO, entity.getNomeProcesso());
    	params.addValue("descrizione_processo" , entity.getDescrizioneProcesso());
    	params.addValue("fl_attivo", entity.getFlagAttivo());
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP );
    	params.addValue("attore_upd", entity.getAttoreUpd());
    	
    	return params;
    }

}