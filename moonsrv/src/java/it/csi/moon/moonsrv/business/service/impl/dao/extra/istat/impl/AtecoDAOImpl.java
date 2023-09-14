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

import it.csi.moon.commons.dto.extra.istat.Ateco;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.istat.AtecoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai codice ISTAT Ateco 2007 - Classificazione delle attività economiche Ateco 2007
 * 
 * @see Ateco
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
@Qualifier("moon")
public class AtecoDAOImpl extends JdbcTemplateDAO implements AtecoDAO {

	private static final String CLASS_NAME = "AtecoDAOImpl";
	
	private static final String ELENCO_FINALE = "SELECT to_number(replace(codice,'.','')) as codice, concat(codice,' - ',descrizione) AS nome\n" + 
			" FROM moon_fo_c_ateco \n" + 
			" WHERE codice is not null\n" + 
			" AND (data_fine is null or data_fine>now())\n" + 
			" ORDER BY sezione,divisione,codice";
	
	private static final String FIND_BY_CODICE = "SELECT to_number(replace(codice,'.','')) as codice, concat(codice,' - ',descrizione) AS nome\n" + 
			" FROM moon_fo_c_ateco \n" + 
			" WHERE codice = :codice\n" + 
			" AND (data_fine is null or data_fine>now())";
	
	/**
	 * Cache locale dei comuni per provincia
	 */
	private static final Duration DURATION_CACHE = Duration.ofMinutes(4*60L);
	private List<Ateco> cacheAtecoFinale = null;
	private LocalTime lastResetCache = LocalTime.now();
	
	/**
	 *
	 * @return
	 * @throws DAOException
	 */
	@Override
	public List<Ateco> findFinale() throws DAOException {
		validateCache();
		if (cacheAtecoFinale==null) {
			cacheAtecoFinale = (List<Ateco>) getCustomNamedParameterJdbcTemplateImpl().query(ELENCO_FINALE, BeanPropertyRowMapper.newInstance(Ateco.class));
		}
		LOG.debug("[" + CLASS_NAME + "::findFinale] BEGIN cacheAtecoFinale.size()=" + ((cacheAtecoFinale!=null)?cacheAtecoFinale.size():"null"));
		return new ArrayList<>(cacheAtecoFinale);
	}

	


	/**
	 * 
	 * @param codiceComune
	 * @return
	 */
	@Override
	public Ateco findByCodice(String codiceAteco) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodice] IN codiceAteco = " + codiceAteco);
			if (codiceAteco==null) return null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice", codiceAteco);
			return (Ateco)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CODICE, params, BeanPropertyRowMapper.newInstance(Ateco.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] CodiceAteco non trovato ! codiceAteco = " + codiceAteco);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}	
	
	
    //
    // Cache Management
	// 
    public void forceResetCache() {
    	cacheAtecoFinale = null;
    	lastResetCache = LocalTime.now();
    	LOG.info("[" + CLASS_NAME + "::forceResetCache] BEGIN END");
    }
    public void validateCache() {
    	if (cacheAtecoFinale!=null && cacheAtecoFinale.size()>0 && 
    		Duration.between(this.lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetCache();
    	}
    }

}
