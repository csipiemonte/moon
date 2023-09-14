/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.LogonModeDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.PortaleEntity;
import it.csi.moon.moonbobl.dto.moonfobl.LogonMode;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai Moduli da Modulistica
 * <br>
 * <br>Tabella moon_ml_d_logon_mode
 * <br>PK: idLogonMode
 * <br>AK1: codiceLogonMode
 * 
 * @see LogonMode
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

@Component
public class LogonModeDAOImpl extends JdbcTemplateDAO implements LogonModeDAO {
	
	private final static String CLASS_NAME = "LogonModeDAOImpl";
	
	private static final String SELECT_FIELDS_ = "SELECT id_logon_mode, codice_logon_mode, desc_logon_mode";

	private static final  String FIND_BY_ID  = SELECT_FIELDS_ +
		" FROM moon_ml_d_logon_mode" +
		" WHERE id_logon_mode = :id_logon_mode";
	
	private static final  String FIND_BY_CD  = SELECT_FIELDS_ +
		" FROM moon_ml_d_logon_mode" +
		" WHERE codice_logon_mode = :codice_logon_mode";
	
	private static final  String FIND = SELECT_FIELDS_ +
		" FROM moon_ml_d_logon_mode";

	
	/**
	 * Restituisce il logonMode identificata per {@code idLogonMode}
	 * 
	 * @param {@code idLogonMode} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il LogonMode ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public LogonMode findById(Long idLogonMode) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_logon_mode", idLogonMode);
			return (LogonMode)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(LogonMode.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce il logonMode identificata per {@code codiceLogonMode}
	 * 
	 * @param codiceLogonMode
	 * 
	 * @return il logonMode ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public LogonMode findByCd(String codiceLogonMode) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_logon_mode", codiceLogonMode);
			return (LogonMode)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(LogonMode.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByCd] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByCd] Errore database: "+e.getMessage(),e);
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
	public List<LogonMode> find() throws DAOException {
		List<LogonMode> result = null;
		result = (List<LogonMode>) getCustomNamedParameterJdbcTemplateImpl().query(FIND, BeanPropertyRowMapper.newInstance(LogonMode.class));
		return result;
	}

}
