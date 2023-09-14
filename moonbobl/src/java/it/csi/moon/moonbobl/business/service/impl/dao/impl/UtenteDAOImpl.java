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

import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEnteAbilitatoFlatDTO;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.dto.extra.istat.CodiceNome;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 * 
 * @see UtenteEntity
 * 
 * @author Mario
 *
 */
@Component
public class UtenteDAOImpl extends JdbcTemplateDAO implements UtenteDAO {

	private static final String CLASS_NAME = "UtenteDAOImpl";

	private static final  String SELECT_  = "SELECT id_utente, identificativo_utente, nome, cognome," +
		" username, password, email, fl_attivo, id_tipo_utente, data_ins, data_upd, attore_ins, attore_upd";
	private static final  String SELECT_U_  = "SELECT u.id_utente, u.identificativo_utente, u.nome, u.cognome," +
		" u.username, u.password, u.email, u.fl_attivo, u.id_tipo_utente, u.data_ins, u.data_upd, u.attore_ins, u.attore_upd";
	
	private static final  String FIND_BY_ID  = SELECT_ +
		" FROM moon_fo_t_utente" +
		" WHERE id_utente = :id_utente";
	
	private static final  String FIND_BY_IDENTIFICATIVO_UTENTE = SELECT_ +
		" FROM moon_fo_t_utente" +
		" WHERE identificativo_utente = :identificativo_utente";

	private static final String FIND_UTENTI_ENTE_AREA_RUOLO_BY_ID_ENTE = SELECT_U_ +
		", a.id_area, a.codice_area, a.nome_area" +
		", r.id_ruolo, r.codice_ruolo, r.nome_ruolo" + 
		", uar.data_upd as data_upd_abilitato, uar.attore_upd as attore_upd_abilitato" +
		" FROM moon_fo_d_area a, moon_fo_r_utente_area_ruolo uar, moon_fo_d_ruolo r, moon_fo_t_utente u" +
		" WHERE a.id_ente = :id_ente" +
		" AND a.id_area = uar.id_area" +
		" AND r.id_ruolo = uar.id_ruolo" +
//		" AND r.fl_attivo = 'S'" +
		" AND u.id_utente = uar.id_utente";
	private static final String ORDER_FIND_UEAR = " ORDER BY u.cognome, u.nome, u.id_utente, a.nome_area, a.id_area, r.nome_ruolo";
	
	private static final String FIND_UTENTI_ENTE_NO_ABILITATO_BY_ID_ENTE = SELECT_U_
		+ " FROM moon_fo_t_utente u"
		+ " WHERE u.id_utente NOT IN ("
		+ "	SELECT distinct u.id_utente"
		+ "	FROM moon_fo_d_area a, moon_fo_r_utente_area_ruolo uar, moon_fo_d_ruolo r, moon_fo_t_utente u"
		+ " WHERE a.id_ente = :id_ente"
		+ "	AND a.id_area = uar.id_area"
		+ "	AND r.id_ruolo = uar.id_ruolo"
		+ "	AND u.id_utente = uar.id_utente"
		+ ")";
	
	private static final String FIND_UTENTI_STESSA_AREA = "SELECT distinct u.id_utente, u.identificativo_utente, "
			+ "u.nome, u.cognome, u.username, u.password, "
			+ " u.email, u.fl_attivo, u.id_tipo_utente, u.data_ins, u.data_upd, u.attore_ins, u.attore_upd" +
			" FROM  moon_fo_r_utente_area_ruolo au, moon_fo_t_utente u, moon_fo_t_utente ope, moon_fo_r_utente_area_ruolo ao " +
			" WHERE au.id_utente = u.id_utente " +
			" AND au.id_area = ao.id_area  AND ao.id_utente = ope.id_utente " +
			" AND u.fl_attivo = 'S' AND ope.identificativo_utente = :cf" ;
	
	private static final String ORDER_FIND_U = " ORDER BY u.cognome, u.nome, u.id_utente";
	
	private static final String INSERT = "INSERT INTO moon_fo_t_utente(" + 
		" id_utente, identificativo_utente, nome, cognome, username, password, email, fl_attivo, id_tipo_utente, data_ins, data_upd, attore_ins, attore_upd)" + 
		" VALUES (:id_utente, :identificativo_utente, :nome, :cognome, :username, :password, :email, :fl_attivo, :id_tipo_utente, :data_ins, :data_upd, :attore_ins, :attore_upd)";

	private static final String ELENCO = SELECT_ +
		" FROM moon_fo_t_utente";

	private static final String DELETE = "DELETE FROM moon_fo_t_utente WHERE id_utente = :id_utente";

	private static final  String UPDATE = "UPDATE moon_fo_t_utente" +
		" SET identificativo_utente=:identificativo_utente, nome=:nome, cognome=:cognome, username=:username, password=:password, email=:email, fl_attivo=:fl_attivo, id_tipo_utente=:id_tipo_utente, data_ins=:data_ins, data_upd=:data_upd, attore_ins=:attore_ins, attore_upd=:attore_upd" +
		" WHERE id_utente = :id_utente";

	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_utente_id_utente_seq')";
	
	private static final String FIND_BY_USER_PWD = SELECT_ +
			" FROM moon_fo_t_utente" +
			" WHERE username = :user" +
			" and password = :pwd";

	/**
	 * Ottiene l'utente identificato per {@code idUtente}
	 * 
	 * @param {@code idUtente} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return l'utente ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public UtenteEntity findById(Long idUtente) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idUtente);
			return (UtenteEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(UtenteEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public UtenteEntity findByIdentificativoUtente(String identificativoUtente) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("identificativo_utente", identificativoUtente);
			return (UtenteEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_IDENTIFICATIVO_UTENTE, params, BeanPropertyRowMapper.newInstance(UtenteEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIdentificativoUtente] Elemento non trovato identificativoUtente = ["+identificativoUtente+"] " + emptyEx.getMessage());
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdentificativoUtente] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Ottiene tutti gli utenti presenti nel sistema
	 * 
	 * @return la lista di tutti gli utenti.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public List<UtenteEntity> find() throws DAOException {
		return ( List<UtenteEntity>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(UtenteEntity.class));
	}

	@Override
	public List<UtenteEntity> utentiAreaByCf(String codiceFiscale) throws DAOException {
	try {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("cf", codiceFiscale);
		return (List<UtenteEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_UTENTI_STESSA_AREA, params, BeanPropertyRowMapper.newInstance(UtenteEntity.class) );
	} catch (EmptyResultDataAccessException emptyEx) {
		log.error("[" + CLASS_NAME + "::utentiAreaByCf] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
		throw new ItemNotFoundDAOException();
	} catch (Exception e) {
		log.error("[" + CLASS_NAME + "::utentiAreaByCf] Errore database: "+e.getMessage(),e);
		throw new DAOException(getMsgErrDefault());
	}
	}
	
	/**
	 * Inserisci un utente nel sistema
	 * 
	 * @param {@code entity} l'utente da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idUtente} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(UtenteEntity entity) throws BusinessException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idUtente = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdUtente(idUtente);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapUtenteEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idUtente;
		} catch (Exception e) {
			if (e.getMessage().contains("\"unique_identificativo_utente\"")) {
				log.error("[" + CLASS_NAME + "::insert] Errore  MOONFOBL-50200  L'identificativo utente o codice fiscale è già presente nel sistema");
				throw new DAOException("L'identificativo utente o codice fiscale è già presente nel sistema","MOONFOBL-50200");
			}
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
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
	public int update(UtenteEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapUtenteEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
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
	public int delete(Long idUtente) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN idUtente: "+idUtente);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_utente", idUtente);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
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
	public int delete(UtenteEntity entity) throws DAOException {
		return delete(entity.getIdUtente());
	}

	private MapSqlParameterSource mapUtenteEntityParameters(UtenteEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_utente", entity.getIdUtente());
		params.addValue("identificativo_utente", entity.getIdentificativoUtente(), Types.VARCHAR);
		params.addValue("nome", entity.getNome(), Types.VARCHAR);
		params.addValue("cognome", entity.getCognome(), Types.VARCHAR);
		params.addValue("username", entity.getUsername(), Types.VARCHAR);
		params.addValue("password", entity.getPassword(), Types.VARCHAR);
		params.addValue("email", entity.getEmail(), Types.VARCHAR);
		params.addValue("fl_attivo", entity.getFlAttivo(), Types.VARCHAR);
		params.addValue("id_tipo_utente", entity.getIdTipoUtente());
		params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_ins", entity.getAttoreIns(), Types.VARCHAR);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		return params;
	}


	
	//
	//
	//
	
	private static final String FIND_ENTI = "SELECT distinct codice_ente as codice, nome_ente as nome " + 
			"FROM moon_fo_r_utente_area_ruolo r, moon_fo_d_area a, moon_fo_t_utente u, moon_fo_d_ente e " + 
			"WHERE a.id_area=r.id_area and u.id_utente=r.id_utente and  a.id_ente=e.id_ente " + 
			"AND u.identificativo_utente=:cf ";

	@Override
	public List<CodiceNome> findComuniAbilitatiUtente(String codiceFiscale) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findComuniAbilitatiUtente] IN CF: "+codiceFiscale);
			List<CodiceNome> result = null;
			
			StringBuilder sb = new StringBuilder(FIND_ENTI);
			
			sb.append(" ORDER BY nome_ente ");
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("cf", codiceFiscale);
			
			log.debug("[" + CLASS_NAME + "::find] params: " + params);
			result = (List<CodiceNome>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params,
					BeanPropertyRowMapper.newInstance(CodiceNome.class));
			
			return result;
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<CodiceNome> findEntiAbilitatiUtente(String codiceFiscale) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findEntiAbilitatiUtente] IN CF: "+codiceFiscale);
			List<CodiceNome> result = null;
			
			StringBuilder sb = new StringBuilder(FIND_ENTI);
			
			sb.append(" ORDER BY nome_ente ");
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("cf", codiceFiscale);
			
			log.debug("[" + CLASS_NAME + "::find] params: " + params);
			result = (List<CodiceNome>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params,
					BeanPropertyRowMapper.newInstance(CodiceNome.class));
			
			return result;
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findEntiAbilitatiUtente] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findEntiAbilitatiUtente] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	//
	//
	@Override
	public List<UtenteEnteAbilitatoFlatDTO> findUtentiEnteAbilitatoByIdEnte(Long idEnte, Long idUtente) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findUtentiEnteAbilitatoByIdEnte] IN idEnte: " + idEnte);
			log.debug("[" + CLASS_NAME + "::findUtentiEnteAbilitatoByIdEnte] IN idUtente: " + idUtente);
			StringBuilder sql = new StringBuilder();
			sql.append(FIND_UTENTI_ENTE_AREA_RUOLO_BY_ID_ENTE);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ente", idEnte);
			if (idUtente!=null) {
				params.addValue("id_utente", idUtente);
				sql.append(" AND u.id_utente = :id_utente");
			}
			sql.append(ORDER_FIND_UEAR);
			return (List<UtenteEnteAbilitatoFlatDTO>)getCustomNamedParameterJdbcTemplateImpl().query(sql.toString(), params, BeanPropertyRowMapper.newInstance(UtenteEnteAbilitatoFlatDTO.class));
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findUtentiEnteAbilitatoByIdEnte] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	//
	@Override
	public List<UtenteEnteAbilitatoFlatDTO> findUtentiEnteNoAbilitatoByIdEnte(Long idEnte/*, Long idUtente*/) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findUtentiEnteNoAbilitatoByIdEnte] IN idEnte: " + idEnte);
//			log.debug("[" + CLASS_NAME + "::findUtentiEnteNoAbilitatoByIdEnte] IN idUtente: " + idUtente);
			StringBuilder sql = new StringBuilder();
			sql.append(FIND_UTENTI_ENTE_NO_ABILITATO_BY_ID_ENTE);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ente", idEnte);
//			if (idUtente!=null) {
//				params.addValue("id_utente", idUtente);
//				sql.append(" AND u.id_utente = :id_utente");
//			}
			sql.append(ORDER_FIND_U);
			return (List<UtenteEnteAbilitatoFlatDTO>)getCustomNamedParameterJdbcTemplateImpl().query(sql.toString(), params, BeanPropertyRowMapper.newInstance(UtenteEnteAbilitatoFlatDTO.class));
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findUtentiEnteNoAbilitatoByIdEnte] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public UtenteEntity findByUsrPwd(String user, String pwd) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("user", user);
			params.addValue("pwd", pwd);
			return (UtenteEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_USER_PWD, params, BeanPropertyRowMapper.newInstance(UtenteEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByUsrPwd] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByUsrPwd] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}	
}
