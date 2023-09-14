/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.PortaleEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.PortaleDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai Portali del FO
 * <br>
 * <br>Tabella moon_fo_d_portale
 * <br>PK: idPortale
 * <br>AK1: codicePortale
 * <br>AK2: nomePortale (il piu usato vista che ci arriva nel HttpRequest)
 * 
 * @see PortaleEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

@Component
public class PortaleDAOImpl extends JdbcTemplateDAO implements PortaleDAO {
	
	private static final String CLASS_NAME = "PortaleDAOImpl";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_portale, codice_portale, nome_portale, descrizione_portale, fl_attivo, data_upd, attore_upd";

	private static final  String FIND_BY_ID  = FIND_SELECT_FIELDS +
			" FROM moon_fo_d_portale" +
			" WHERE id_portale = :id_portale";
	
	private static final  String FIND_BY_CD  = FIND_SELECT_FIELDS +
			" FROM moon_fo_d_portale" +
			" WHERE codice_portale = :codice_portale";
	
	private static final  String FIND_BY_NAME  = FIND_SELECT_FIELDS +
			" FROM moon_fo_d_portale" +
			" WHERE nome_portale = :nome_portale";
	
	private static final  String FIND = FIND_SELECT_FIELDS +
			" FROM moon_fo_d_portale" +
			" WHERE fl_attivo='S'";
	
	
	/**
	 * Restituisce il portale identificata per {@code idPortale}
	 * 
	 * @param {@code idPortale} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il portale ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public PortaleEntity findById(Long idPortale) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_portale", idPortale);
			return (PortaleEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(PortaleEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Restituisce il portale identificata per {@code codicePortale}
	 * 
	 * @param codicePortale
	 * 
	 * @return il portale ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public PortaleEntity findByCd(String codicePortale) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_portale", codicePortale);
			return (PortaleEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(PortaleEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	/**
	 * Restituisce il portale identificata per {@code nomePortale}
	 * 
	 * @param nomePortale
	 * 
	 * @return il portale ricercato, se trovato.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */	
	@Override
	public PortaleEntity findByNome(String nomePortale) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("nome_portale", nomePortale);
			return (PortaleEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_NAME, params, BeanPropertyRowMapper.newInstance(PortaleEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
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
	public List<PortaleEntity> find() throws DAOException {
		List<PortaleEntity> result = null;
		result = (List<PortaleEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND, BeanPropertyRowMapper.newInstance(PortaleEntity.class));
		return result;
	}

}
