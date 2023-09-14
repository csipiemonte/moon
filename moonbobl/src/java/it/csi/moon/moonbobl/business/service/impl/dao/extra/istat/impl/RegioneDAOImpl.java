/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.impl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.RegioneDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonbobl.dto.extra.istat.Regione;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle regioni
 * 
 * @see Regione
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
@Qualifier("moon")
public class RegioneDAOImpl extends JdbcTemplateDAO implements RegioneDAO {

	private final static String CLASS_NAME = "RegioneDAOImpl";

	private static final  String FIND_BY_ID  = "SELECT codice_regione AS codice, nome_regione AS nome" +
			" FROM moon_fo_c_regione" +
			" WHERE codice_regione = :codice_regione";
	
	private static final String ELENCO = "SELECT codice_regione AS codice, nome_regione AS nome" +
			" FROM moon_fo_c_regione" +
			" WHERE flag_attivo = 'S'";

	/**
	 * Cache locale delle regioni
	 */
	private List<Regione> listaRegioniCache;
	private LocalTime lastResetCache = LocalTime.now();
	private static final Duration DURATION_CACHE = Duration.ofMinutes(4*60);
	
	
	/**
	 * Ottiene tutte le regioni presenti nel sistema da Database
	 * Server per inizializzare la cache
	 * 
	 * @return la lista di tutte le regioni.
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	private List<Regione> findAllForce() throws DAOException {
		log.debug("[" + CLASS_NAME + "::findAllForse] BEGIN END");
		lastResetCache = LocalTime.now();
		listaRegioniCache = (List<Regione>)getCustomJdbcTemplate().query(ELENCO, BeanPropertyRowMapper.newInstance(Regione.class));
		return listaRegioniCache;
	}

	/**
	 * Ottiene tutte le regioni presenti nel sistema (da cache locale)
	 * 
	 * @return la lista di tutte le regioni.
	 * 
	 * @throws DAOException se si verificano altri errori. Null se lista vuota.
	 */
	@Override
	public List<Regione> findAll() {
		log.debug("[" + CLASS_NAME + "::findAll] BEGIN END");
		validateCacheAndInitIfNecessari();
		return listaRegioniCache;
	}


	/**
	 * Ottiene la regione identificato per {@code codice}
	 * 
	 * @param {@code codice} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return la regione ricercata, se trovata.
	 * 
	 * @throws ItemNotFoundDAOException se la resource non e' stata trovata.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public Regione findByPKForce(Integer codice) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByPKForce] IN codice = " + codice);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_regione", String.format("%02d" , codice));
			return (Regione)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(Regione.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByPKForce] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByPKForce] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Ottiene la regione identificato per {@code codice}
	 * 
	 * @param {@code codice} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return la regione ricercata, se trovata. Null se non trovata.
	 */
	@Override
	public Regione findByPK(Integer codice) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByPK] IN codice = " + codice);
			validateCacheAndInitIfNecessari();
			for (Regione regione : listaRegioniCache) {
				if (regione.getCodice().equals(codice)) {
					return regione;
				}
			}
			log.error("[" + CLASS_NAME + "::findByPK] Elemento non trovato codice = " + codice);
			throw new ItemNotFoundDAOException();
		} catch (ItemNotFoundDAOException nfe) {
			throw nfe;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByPKForce] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	
    //
    //
    public void forceResetCache() {
    	listaRegioniCache.clear();
    	lastResetCache = LocalTime.now();
    	log.info("[" + CLASS_NAME + "::forceResetModuliCache] BEGIN END");
    }
    public void validateCache() {
    	if (Duration.between(this.lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetCache();
    	}
    }
    //
	private void validateCacheAndInitIfNecessari() {
		if (listaRegioniCache==null) {
			init();
		}
		validateCache();
		if (listaRegioniCache.isEmpty()) {
			init();
		}
	}
	/**
	 * Inizializa la cache
	 */
	private void init() {
		try {
			log.debug("[" + CLASS_NAME + "::init] BEGIN END");
			// Nel caso delle regioni carico subito tutte le Regioni
			listaRegioniCache = findAllForce();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::init] Errore database findAllForce() : "+e.getMessage(),e);
			//e.printStackTrace();
		}
	}
}
