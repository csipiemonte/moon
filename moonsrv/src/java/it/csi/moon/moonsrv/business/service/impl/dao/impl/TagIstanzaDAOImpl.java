/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.Date;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.TagIstanzaDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso a tag_istanza
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

@Component
public class TagIstanzaDAOImpl extends JdbcTemplateDAO implements TagIstanzaDAO {
	
	private static final String CLASS_NAME = "TagIstanzaDAOImpl";
	
	private static final  String UPDATE_ESITO = "UPDATE moon_fo_r_tag_istanza" +
			" SET esito=:esito, data_upd=:data_upd, attore_upd=:attore_upd" +
			" WHERE id_tag = :id_tag AND id_istanza = :id_istanza";	
	
	/**
	 * Aggiorna l'esito di un record di relazione tag/istanza  
	 * 
	 * @param {@code idTag}{@code idIstanza} la chiave del record di relazione tag/istanza da aggiornare. Cannot be {@code null}
	 * @param {@code esito} il valore dell esito da aggiornare.
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun record aggiornato, 1 se record aggiornato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int updateEsito(Long idTag, Long idIstanza, String esito) throws DAOException {
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateEsito] IN idTag: "+idTag);
				LOG.debug("[" + CLASS_NAME + "::updateEsito] IN idIstanza: "+idIstanza);
				LOG.debug("[" + CLASS_NAME + "::updateEsito] IN esito: "+esito);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_tag", idTag);
			params.addValue("id_istanza", idIstanza);
			params.addValue("esito", esito);
			params.addValue("data_upd", new Date(), Types.TIMESTAMP);
			params.addValue("attore_upd", "ADMIN");
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ESITO, params);
			LOG.debug("[" + CLASS_NAME + "::updateEsito] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateEsito] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO");
		}
	}

}
