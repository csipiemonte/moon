/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.commercio.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.commercio.Concessione;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.commercio.ConcessioniDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle concessioni
 * 
 * @see Concessioni
 * 
 * @author Alberto
 * 
 * @since 1.0.0
 */

@Component
public class ConcessioniDAOImpl extends JdbcTemplateDAO implements ConcessioniDAO {
	private static final String CLASS_NAME= "CodiceIstanzaDAOImpl";
	
	private static final  String FIND_BY_ID = "SELECT id_concessione, cf_concessionario, concessionario, " +
			"autorizzazione, mercato, posteggio, settore, giorno " + 
			"FROM moon_ext_concessioni_coto " +
			"WHERE id_concessione = :id_concessione";

	private static final  String FIND = "SELECT id_concessione, cf_concessionario, concessionario, " +
			"autorizzazione, mercato, posteggio, settore, giorno " + 
			"FROM moon_ext_concessioni_coto " +
			"WHERE cf_concessionario = :cfconcessionario";
	
	@Override
	public Concessione findById(Long id) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_concessione", id);
			Concessione concessione = (Concessione) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, Concessione.class);
					
			return concessione;
			
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	
	@Override
	public List<Concessione> findByCF(String cf) throws DAOException {
		List<Concessione> result = null;
		
		StringBuilder sb = new StringBuilder(FIND);
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("cfconcessionario", cf);
		
		LOG.debug("[" + CLASS_NAME + "::findByCF] params: "+params);
		result = (List<Concessione>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(Concessione.class));
		
		return result;

	}
}