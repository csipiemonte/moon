/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.impl;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.ToSolidaleDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO di accesso all'elenco dei codici fiscali di esenzione ToSolidale
 * 
 * @author Luca
 * 
 * @since 1.0.0
 */

@Component
public class ToSolidaleDAOImpl extends JdbcTemplateDAO implements ToSolidaleDAO {
	private static final String CLASS_NAME = "ToSolidaleDAOImpl";	
	private static final String FIND = "SELECT codice_fiscale from moon_ext_tosolidale_taxi";
	private static final  String FIND_BY_CF = "SELECT idfamiglia"+
			" FROM moon_ext_tosolidale_taxi " +
			" WHERE codice_fiscale=:codice_fiscale";


	@Override
	public List<String> find() throws DAOException {
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {			
			LOG.debug("[" + CLASS_NAME + "::find]");
			return  (List<String>) getCustomNamedParameterJdbcTemplateImpl().queryForList(FIND, params, String.class);
				
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::find] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	@Override
	public String findByCf(String cf) throws DAOException {
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			
			params.addValue("codice_fiscale", cf);
			LOG.debug("[" + CLASS_NAME + "::findByCF] CF: "+cf);
			LOG.debug("[" + CLASS_NAME + "::findByCF] params: "+params);
			return  getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CF, params, String.class);
				
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::find] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
}
