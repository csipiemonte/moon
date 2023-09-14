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

import it.csi.moon.commons.entity.NotificaEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;



/**
 * DAO per l'accesso alla notifica di invio email
 * <br>
 * <br>Tabella moon_fo_t_notifica
 * <br>PK: idNotifica
 *
 * @see NotificaEntity
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */


@Component
public class NotificaDAOImpl extends JdbcTemplateDAO implements NotificaDAO {
	
	private static final String CLASS_NAME = "NotificaDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_notifica, id_istanza, cf_destinatario, email_destinatario, oggetto_notifica, testo_notifica"+
			"data_invio,esito_invio,flag_letta,flag_archiviata";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
			" FROM moon_fo_t_notifica" +
			" WHERE id_notifica = :id_notifica";
	
	private static final  String FIND_BY_ID_ISTANZA  = FIND_SELECT_FIELDS +
			" FROM moon_fot_notifica" +
			" WHERE id_istanza = :id_istanza";
	
	private static final String INSERT = "INSERT INTO moon_fo_t_notifica(" + 
			" id_notifica, id_istanza, cf_destinatario, email_destinatario, oggetto_notifica, testo_notifica, data_invio, esito_invio, flag_letta, flag_archiviata)" + 
			" VALUES (:id_notifica, :id_istanza, :cf_destinatario, :email_destinatario, :oggetto_notifica, :testo_notifica, :data_invio, :esito_invio, :flag_letta, :flag_archiviata)";

	private static final  String UPDATE = "UPDATE moon_fo_t_notifica" +
			" SET  id_notifica=:id_notifica, id_istanza=:id_istanza, cf_destinatario=:cf_destinatario , email_destinatario=:email_destinatario, oggetto_notifica=:oggetto_notifica, testo_notifica=:testo_notifica, data_invio=:data_invio, esito_invio=:esito_invio, flag_letta:=flag_letta, 	flag_archiviata:=flag_archiviata" +
			" WHERE id_notifica = :id_notifica";

	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_notifica_id_notifica_seq')";	
	
	/**
	 * Restituisce la notifica identificata per {@code idNotifica}
	 * 
	 * @param {@code idNotifica} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return la notifica ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public NotificaEntity findById(Long idNotifica) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_notifica", idNotifica);
			return (NotificaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(NotificaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce le notifiche individuate per {@code idIstanza}
	 * 
	 * @param idistanza
	 * 
	 * @return la lista delle notifiche per istanza
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public  List<NotificaEntity> findByIdIstanza(Long idIstanza) throws DAOException {
		
			List<NotificaEntity> result = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			result = (List<NotificaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, BeanPropertyRowMapper.newInstance(NotificaEntity.class));
			return result;	
	}


	/**
	 * Inserisce una notifica nel sistema
	 * 
	 * @param {@code entity} la notifica da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idnNtifica} generata durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Long insert(NotificaEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idNotifica = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdNotifica(idNotifica);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idNotifica;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}

	/**
	 * Aggiorna una notifica nel sistema sulla base della sua chiave primaria {@code idNotifica} 
	 * 
	 * @param {@code entity} notifica da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessuna notifica aggiornata, 1 se notifica aggiornata).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(NotificaEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO NOTIFICA");
		}
	}


	// id_notifica, id_istanza, cf_destinatario, email_destinatario, oggetto_notifica, testo_notifica, data_invio,esito_invio,flag_letta,flag_archiviata";
    private MapSqlParameterSource mapEntityParameters(NotificaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_notifica", entity.getIdNotifica());
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("cf_destinatario", entity.getCfDestinatario());
    	params.addValue("email_destinatario" , entity.getEmailDestinatario());
    	params.addValue("oggetto_notifica", entity.getOggettoNotifica());
    	params.addValue("testo_notifica", entity.getTestoNotifica());
    	params.addValue("data_invio", entity.getDataInvio(),Types.TIMESTAMP);
    	params.addValue("esito_invio" , entity.getEsitoInvio());
    	params.addValue("flag_letta", entity.getFlagLetta());
    	params.addValue("flag_archiviata", entity.getFlagArchiviata());
  
    	return params;
    }

    
    //
    // moon_fo_r_allegato_notifica
    //
	@Override
	public Long insertAllegato(Long idNotifica, Long idFile) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insertAllegato] IN idNotifica="+idNotifica+"  idFile="+idFile);
			MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("id_notifica", idNotifica);
	    	params.addValue("id_file", idFile);
			final String INSERT_ALLEGATO = "INSERT INTO moon_fo_r_allegato_notifica (id_notifica,id_file) VALUES (:id_notifica,:id_file);";
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_ALLEGATO , params);
			LOG.debug("[" + CLASS_NAME + "::insertAllegato] Record inseriti: " + numRecord);
			return idNotifica;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertAllegato] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}

	
}