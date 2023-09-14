/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.EnteAreaRuoloFlatDTO;
import it.csi.moon.commons.entity.RuoloEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.RuoloDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai ruoli
 * <br>
 * <br>Tabella principale : moon_fo_d_ruolo
 * <br>PK: idRuolo
 * <br>AK: codiceRuolo
 * 
 * @see RuoloEntity
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
public class RuoloDAOImpl extends JdbcTemplateDAO implements RuoloDAO {

	private static final String CLASS_NAME = "RuoloDAOImpl";

	private static final  String FIND_BY_ID  = "SELECT id_ruolo, codice_ruolo, nome_ruolo, descrizione_ruolo," +
			" fl_attivo, data_upd, attore_upd" +
			" FROM moon_fo_d_ruolo" +
			" WHERE id_ruolo = :id_ruolo";

	private static final String INSERT = "INSERT INTO moon_fo_d_ruolo(" + 
			" id_ruolo, codice_ruolo, nome_ruolo, descrizione_ruolo, fl_attivo, data_upd, attore_upd)" + 
			" VALUES (:id_ruolo, :codice_ruolo, :nome_ruolo, :descrizione_ruolo, :fl_attivo, :data_upd, :attore_upd)";

	private static final String ELENCO = "SELECT id_ruolo, codice_ruolo, nome_ruolo, descrizione_ruolo," +
			" fl_attivo, data_upd, attore_upd" +
			" FROM moon_fo_d_ruolo";

	private static final String DELETE = "DELETE FROM moon_fo_d_ruolo WHERE id_ruolo = :id_ruolo";

	private static final  String UPDATE = "UPDATE moon_fo_d_ruolo" +
			" SET codice_ruolo=:codice_ruolo, nome_ruolo=:nome_ruolo, descrizione_ruolo=:descrizione_ruolo, fl_attivo=:fl_attivo, data_upd=:data_upd, attore_upd=:attore_upd" +
			" WHERE id_ruolo = :id_ruolo";

	//
	private static final  String FIND_ENTE_AREA_RUOLO_ATTIVI_BY_ID_UTENTE  = "select e.id_ente, e.codice_ente, e.nome_ente, e.descrizione_ente, e.id_tipo_ente, a.id_area, a.codice_area, a.nome_area, r.id_ruolo, r.codice_ruolo, r.nome_ruolo" + 
			" FROM moon_fo_r_utente_area_ruolo uar, moon_fo_d_area a, moon_fo_d_ente e, moon_fo_d_ruolo r" + 
			" WHERE uar.id_utente = :id_utente" + 
			" AND a.id_area = uar.id_area" + 
			" AND e.id_ente = a.id_ente" + 
			" AND e.fl_attivo = 'S'" + 
			" AND r.id_ruolo = uar.id_ruolo" + 
			" AND r.fl_attivo = 'S'" +
			" ORDER BY e.id_tipo_ente, e.descrizione_ente, a.nome_area, r.nome_ruolo";
	
	
	private static final String SEQ_ID = "SELECT nextval('moon_fo_d_ruolo_id_ruolo_seq')";

	/**
	 * Ottiene il ruolo identificato per {@code idRuolo}
	 * 
	 * @param {@code idRuolo} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il ruolo ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public RuoloEntity findById(Integer idRuolo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ruolo", idRuolo);
			return (RuoloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(RuoloEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Ottiene tutti ruoli presenti nel sistema
	 * 
	 * @return la lista di tutti ruoli.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public List<RuoloEntity> find() throws DAOException {
		return ( List<RuoloEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(RuoloEntity.class));
	}

	/**
	 * Inserisci un ruolo nel sistema
	 * 
	 * @param {@code entity} il ruolo da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idRuolo} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Integer insert(RuoloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Integer idRuolo = getCustomNamedParameterJdbcTemplateImpl().queryForInt(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdRuolo(idRuolo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapRuoloEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idRuolo;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}

	/**
	 * Aggiorna un ruolo nel sistema sulla base della sua chiave primaria {@code idRuolo} 
	 * 
	 * @param {@code entity} il ruolo da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun ruolo aggiornato, 1 se ruolo aggiornato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(RuoloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapRuoloEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO RUOLO");
		}
	}

	/**
	 * Cancella un ruolo nel sistema sulla base della sua chiave primaria {@code idRuolo}.
	 * Per essere attuabile, non devono essere presenti riferenze del ruolo cancellato nelle altre entity collegate
	 * ( RuoloUtenti e RuoloFunzioni)
	 * 
	 * @param {@code idRuolo} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ruolo cancellato, 1 se ruolo cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Integer idRuolo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idRuolo: "+idRuolo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ruolo", idRuolo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}

	/**
	 * Cancella un ruolo nel sistema sulla base dell'entita ruolo {@code entity}. L'entita deve avere valorizzato almeno la sua chiave primaria {@code idRuolo}.
	 * Per essere attuabile, non devono essere presenti riferenze del ruolo cancellato nelle altre entity collegate
	 * ( RuoloUtenti e RuoloFunzioni)
	 * 
	 * @param {@code entity} il ruolo da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun ruolo cancellato, 1 se ruolo cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(RuoloEntity entity) throws DAOException {
		return delete(entity.getIdRuolo());
	}
	
	
	// Direct EnteAreeRuoli Access
	@Override
	public List<EnteAreaRuoloFlatDTO> findEntiAreaRuoliAttiviByIdUtente(Long idUtente) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findEntiAreaRuoliAttiviByIdUtente] IN: " + idUtente);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idUtente);
			return (List<EnteAreaRuoloFlatDTO>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_ENTE_AREA_RUOLO_ATTIVI_BY_ID_UTENTE, params, new RowMapper<EnteAreaRuoloFlatDTO>() {
				@Override
				public EnteAreaRuoloFlatDTO mapRow(ResultSet rs, int rownumber) throws SQLException {
					// e.id_ente, e.codice_ente, e.nome_ente, e.descrizione_ente, e.id_tipo_ente, a.id_area, a.codice_area, a.nome_area, r.id_ruolo, r.codice_ruolo, r.nome_ruolo
					EnteAreaRuoloFlatDTO e = new EnteAreaRuoloFlatDTO();
			        e.setIdEnte(rs.getLong("id_ente"));
			        e.setCodiceEnte(rs.getString("codice_ente"));
			        e.setNomeEnte(rs.getString("nome_ente"));
			        e.setDescrizioneEnte(rs.getString("descrizione_ente"));
			        e.setIdTipoEnte(rs.getInt("id_tipo_ente"));
			        //
			        e.setIdArea(rs.getLong("id_area"));
			        e.setCodiceArea(rs.getString("codice_area"));
			        e.setNomeArea(rs.getString("nome_area"));
			        //
			        e.setIdRuolo(rs.getInt("id_ruolo"));
			        e.setCodiceRuolo(rs.getString("codice_ruolo"));
			        e.setNomeRuolo(rs.getString("nome_ruolo"));
			        return e;  
				}  
			}  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findAttiviByIdUtente] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findAttiviByIdUtente] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	

	private MapSqlParameterSource mapRuoloEntityParameters(RuoloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_ruolo", entity.getIdRuolo());
		params.addValue("codice_ruolo", entity.getCodiceRuolo());
		params.addValue("nome_ruolo", entity.getNomeRuolo());
		params.addValue("descrizione_ruolo", entity.getDescrizioneRuolo());
		params.addValue("fl_attivo", entity.getFlAttivo());
		params.addValue("data_upd", entity.getDataUpd(), Types.DATE);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);

		return params;
	}
}
