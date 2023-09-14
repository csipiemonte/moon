/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.CodiceIstanzaDAO;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alla tipologia di codice istanza
 * 
 * 
 * @author Alberto Deiro
 *
 * @since 1.0.0
 */
@Component
public class CodiceIstanzaDAOImpl extends JdbcTemplateDAO implements CodiceIstanzaDAO {
	private final static String CLASS_NAME= "CodiceIstanzaDAOImpl";
	
	private static final  String FIND_BY_ID = "SELECT desc_codice " +
		" FROM moon_io_d_tipocodiceistanza " +
		" WHERE id_tipo_codice_istanza = :id_tipo_codice_istanza";

	
	@Override
	public String findById(Integer id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_tipo_codice_istanza", id);
			String descCodiceIstanza = "";
			descCodiceIstanza = (String) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, String.class);
					
			return descCodiceIstanza;
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

}
