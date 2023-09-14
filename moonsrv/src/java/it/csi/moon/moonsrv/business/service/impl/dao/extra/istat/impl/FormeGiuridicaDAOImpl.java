/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.istat.impl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.istat.FormeGiuridica;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.istat.FormeGiuridicaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle Forme Giuridiche
 * 
 * @see FormeGiuridiche
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
@Qualifier("moon")
public class FormeGiuridicaDAOImpl extends JdbcTemplateDAO implements FormeGiuridicaDAO {

	private static final String CLASS_NAME = "FormeGiuridicaDAOImpl";
	
	private static final String ELENCO = "select id_forma_giuridica as codice, CONCAT(cod_forma_giuridica,' - ',descr_forma_giuridica) as nome" + 
			" FROM moon_c_forme_giuridiche " +
			" ORDER BY cod_forma_giuridica";
	
	private static final String FIND_BY_ID = "select id_forma_giuridica as codice, CONCAT(cod_forma_giuridica,' - ',descr_forma_giuridica) as nome" + 
			" FROM moon_c_forme_giuridiche " +
			" WHERE id_forma_giuridica=:id_forma_giuridica";
	
	/**
	 * Cache locale dei comuni per provincia
	 */
	private static final Duration DURATION_CACHE = Duration.ofMinutes(4*60L);
	private List<FormeGiuridica> cache = null;
	private LocalTime lastResetCache = LocalTime.now();
	
	/**
	 *
	 * @return
	 * @throws DAOException
	 */
	@Override
	public List<FormeGiuridica> find() throws DAOException {
		validateCache();
		if (cache==null) {
			cache = (List<FormeGiuridica>) getCustomNamedParameterJdbcTemplateImpl().query(ELENCO, BeanPropertyRowMapper.newInstance(FormeGiuridica.class));
		}
		LOG.debug("[" + CLASS_NAME + "::find] BEGIN cache.size()=" + ((cache!=null)?cache.size():"null"));
		return new ArrayList<>(cache);
	}

	


	/**
	 * 
	 * @param codiceComune
	 * @return
	 */
	@Override
	public FormeGiuridica findById(Integer idFormeGiuridica) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findById] IN idFormeGiuridica = " + idFormeGiuridica);
			if (idFormeGiuridica==null) return null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_forma_giuridica", idFormeGiuridica);
			return (FormeGiuridica)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(FormeGiuridica.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] FormeGiuridica non trovato ! idFormeGiuridica = " + idFormeGiuridica);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}	
	
	
    //
    // Cache Management
	// 
    public void forceResetCache() {
    	cache = null;
    	lastResetCache = LocalTime.now();
    	LOG.info("[" + CLASS_NAME + "::forceResetCache] BEGIN END");
    }
    public void validateCache() {
    	if (cache!=null && cache.size()>0 &&
    		Duration.between(this.lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetCache();
    	}
    }

}
