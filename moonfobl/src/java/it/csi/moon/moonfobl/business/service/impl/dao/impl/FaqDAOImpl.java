/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.FaqEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.FaqDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle FAQ
 * 
 * @see FaqEntity
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
public class FaqDAOImpl extends JdbcTemplateDAO implements FaqDAO {

	private static final String CLASS_NAME = "FaqDAOImpl";

	private static final String FIND_BY_ID = "SELECT id_faq, titolo, contenuto, data_ins, attore_ins, data_upd, attore_upd" +
			" FROM moon_fo_t_faq" +
			" WHERE id_faq = :id_faq";

	private static final String ELENCO_BY_MODULO = "SELECT f.id_faq, f.titolo, f.contenuto, f.data_ins, f.attore_ins, f.data_upd, f.attore_upd" +
			" FROM moon_fo_t_faq f, moon_r_faq_modulo m" +
			" WHERE m.id_modulo = :id_modulo" +
			" AND f.id_faq = m.id_faq" +
			" ORDER BY m.ordine";

	private static final String INSERT = "INSERT INTO moon_fo_t_faq (id_faq, titolo, contenuto, data_ins, attore_ins, data_upd, attore_upd)" + 
			" VALUES (:id_faq, :titolo, :contenuto, :data_ins, :attore_ins, :data_upd, :attore_upd)";
	private static final String INSERT_FAQ_MODULO = "INSERT INTO moon_r_faq_modulo (id_faq, id_modulo, ordine)" + 
			" VALUES (:id_faq, :id_modulo, :ordine)";
	
	private static final String DELETE = "DELETE FROM moon_fo_t_faq WHERE id_faq = :id_faq";
	private static final String DELETE_FAQ_MODULO = "DELETE FROM moon_r_faq_modulo WHERE id_faq = :id_faq AND id_modulo = :id_modulo";

	private static final String UPDATE = "UPDATE moon_fo_t_faq" +
			" SET titolo=:titolo, contenuto=:contenuto, data_upd=:data_upd, attore_upd=:attore_upd" +
			" WHERE id_faq = :id_faq";

	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_faq_id_faq_seq')";

	/**
	 * Ottiene una FAQ identificato per {@code idFaq}
	 * 
	 * @param {@code idFaq} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return la FAQ ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public FaqEntity findById(Long idFaq) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_faq", idFaq);
			return (FaqEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(FaqEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: ",e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Ottiene tutte FAQ ordinate, presenti nel sistema per un Modulo
	 * 
	 * @return la lista delle FAQ per un modulo.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public List<FaqEntity> findByIdModulo(Long idModulo) throws DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return ( List<FaqEntity>)getCustomNamedParameterJdbcTemplateImpl().query(ELENCO_BY_MODULO, params, BeanPropertyRowMapper.newInstance(FaqEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: ",e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Inserisci una FAQ nel sistema
	 * 
	 * @param {@code entity} la FAQ da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idFaq} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(FaqEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idFaq = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdFaq(idFaq);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapFaqEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idFaq;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}

	/**
	 * Inserisci l'associazione di una FAQ a un modulo con la sua posizione (ordine)
	 * 
	 * @param idFaq
	 * @param idModulo
	 * @param ordine
	 * @return in numero di righe inserite
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int insertFaqModulo(Long idFaq, Long idModulo, Integer ordine) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insertFaqModulo] IN idFaq: "+idFaq+" , idModulo: "+idModulo+" , ordine: "+ordine);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_faq", idFaq);
			params.addValue("id_modulo", idModulo);
			params.addValue("ordine", ordine);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_FAQ_MODULO, params);
			LOG.debug("[" + CLASS_NAME + "::insertFaqModulo] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertFaqModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}

	}
	
	/**
	 * Cancella una FAQ nel sistema sulla base della sua chiave primaria {@code idFaq}.
	 * Per essere attuabile, non devono essere presenti referenze della FAQ nelle altre entity collegate
	 * ( FaqModulo )
	 * 
	 * @param {@code idFaq} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun faq cancellato, 1 se FAQ cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Long idFaq) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idFaq: "+idFaq);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_faq", idFaq);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}
	}

	/**
	 * Cancella una FAQ nel sistema sulla base dell'entita FAQ {@code entity}. L'entita deve avere valorizzato almeno la sua chiave primaria {@code idFaq}.
	 * Per essere attuabile, non devono essere presenti referenze della FAQ nelle altre entity collegate
	 * ( FaqModulo )
	 * 
	 * @param {@code idFaq} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun faq cancellato, 1 se FAQ cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(FaqEntity entity) throws DAOException {
		return delete(entity.getIdFaq());
	}

	/**
	 * Cancella una relazione idFaq / idModulo
	 * 
	 * @param {@code idFaq} Cannot be {@code null}
	 * @param {@code idModulo} Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun faq cancellato, 1 se relazione cancellata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	public int delete(Long idFaq, Long idModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idFaq: "+idFaq+"idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_faq", idFaq);
			params.addValue("id_modulo", idModulo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_FAQ_MODULO, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE");
		}	
	}
	
	/**
	 * Aggiorna una FAQ nel sistema sulla base della sua chiave primaria {@code idFaq} 
	 * 
	 * @param {@code entity} la FAQ da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun FAQ aggiornato, 1 se FAQ aggiornata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(FaqEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapFaqEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO RUOLO");
		}
	}
	
	

	private MapSqlParameterSource mapFaqEntityParameters(FaqEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_faq", entity.getIdFaq());
		params.addValue("titolo", entity.getTitolo());
		params.addValue("contenuto", entity.getContenuto());
		params.addValue("data_ins", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_ins", entity.getAttoreUpd(), Types.VARCHAR);
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		return params;
	}
	
}
