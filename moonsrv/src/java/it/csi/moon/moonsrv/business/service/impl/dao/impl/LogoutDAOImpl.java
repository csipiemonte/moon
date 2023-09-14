/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.LogoutEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.LogoutDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle URL di logout del FO (adesso solo Shib)
 * <br>
 * <br>Tabella moon_fo_d_logout
 * <br>PK: idLogout
 * <br>AK: idPortale,livAuth (0)
 * 
 * @see LogoutEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 01/09/2020 - versione iniziale
 */

@Component
public class LogoutDAOImpl extends JdbcTemplateDAO implements LogoutDAO {
	
	private static final String CLASS_NAME = "LogoutDAOImpl";
	
	private static final String FROM_MOON_FO_D_LOGOUT = " FROM moon_fo_d_logout";

	private static final String FIND_SELECT_FIELDS = "SELECT id_logout, id_portale, liv_auth, url, data_upd, attore_upd";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
			FROM_MOON_FO_D_LOGOUT +
			" WHERE id_logout = :id_logout";
	
	private static final  String FIND_BY_PORTALE_LIV  = FIND_SELECT_FIELDS +
			FROM_MOON_FO_D_LOGOUT +
			" WHERE id_portale = :id_portale" +
			" AND liv_auth = :liv_auth";
	
	private static final  String FIND = FIND_SELECT_FIELDS +
			FROM_MOON_FO_D_LOGOUT;
	
	private static final  String FIND_BY_PORTALE = FIND_SELECT_FIELDS +
			FROM_MOON_FO_D_LOGOUT +
			" WHERE id_portale = :id_portale";

	
	/**
	 * Restituisce il logout identificato per {@code idLogout}
	 * 
	 * @param {@code idLogout} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il logout ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public LogoutEntity findById(Long idLogout) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_logout", idLogout);
			return (LogoutEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(LogoutEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce il logout identificato per {@code idPortale}
	 * 
	 * @param idPortale
	 * @param livAuth
	 * 
	 * @return il logout ricercato, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public LogoutEntity findByPortaleLivAuth(Long idPortale, Integer livAuth) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_portale", idPortale);
			params.addValue("liv_auth", livAuth);
			return (LogoutEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_PORTALE_LIV, params, BeanPropertyRowMapper.newInstance(LogoutEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByPortaleLivAuth] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByPortaleLivAuth] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	/**
	 * Restituisce tutte le righe di logout presenti nel sistema
	 * 
	 * @return la lista di tutte le aree.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public List<LogoutEntity> find() throws DAOException {
		List<LogoutEntity> result = null;
		result = (List<LogoutEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND, BeanPropertyRowMapper.newInstance(LogoutEntity.class));
		return result;
	}

	/**
	 * Restituisce logout presenti nel sistema selezionate per id portale
	 * 
	 * @return la lista degli possibile logout selezionate per id portale.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public List<LogoutEntity> findByPortale(Long idPortale) throws DAOException {
		List<LogoutEntity> result = null;
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_portale", idPortale);
		result = (List<LogoutEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_PORTALE, params, BeanPropertyRowMapper.newInstance(LogoutEntity.class));
		return result;
	}
	
}
