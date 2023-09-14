/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.istat.impl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.istat.Comune;
import it.csi.moon.commons.dto.extra.istat.Provincia;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.istat.ComuneDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.istat.ProvinciaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai comuni italiani
 * 
 * @see Comune
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
@Qualifier("moon")
public class ComuneDAOImpl extends JdbcTemplateDAO implements ComuneDAO {

	private static final String CLASS_NAME = "ComuneDAOImpl";
	
	private static final String ELENCO_BY_PROVINCIA = "SELECT codice_comune AS codice, nome_comune AS nome" +
			" FROM moon_fo_c_comune" +
			" WHERE codice_provincia = :codice_provincia" +
			" AND flag_attivo = 'S'" +
			" ORDER BY nome_comune";
	
	private static final String FIND_EMAIL_BY_COMUNE = "SELECT email_asl " + 
			" FROM moon.moon_fo_c_comune_rp " +
			" WHERE codice_comune = :codice_comune";
	
	/**
	 * Cache locale dei comuni per provincia
	 */
	private static final Duration DURATION_CACHE = Duration.ofMinutes(4*60L);
	private static final int NUMERO_DI_PROVINCIE_ITALIANE = 106; // 106 Provincie italiane // TODO to be dinamic  SELECT count(*)
	private LinkedHashMap<Integer, List<Comune>> mappaComuniPerProvincia; // MAX 106 elements
	private LocalTime lastResetCache = LocalTime.now();
	
	
	@Autowired
	@Qualifier("moon")
	ProvinciaDAO provinciaDAO;


	/**
	 *
	 * @return
	 * @throws DAOException
	 */
	@Override
	public List<Comune> findAll() throws DAOException {
		validateCacheAndInitIfNecessari(); // per primo del LOG.debug, inizializza la mappa se nulla !
		LOG.debug("[" + CLASS_NAME + "::findAll] BEGIN mappaComuniPerProvincia.size()=" + mappaComuniPerProvincia.size() + " / " + NUMERO_DI_PROVINCIE_ITALIANE);
		if ( mappaComuniPerProvincia.size() < NUMERO_DI_PROVINCIE_ITALIANE ) {
			initAllProvincieMancante();
		}
		List<Comune> result = mappaComuniPerProvincia.values().stream().flatMap(List::stream).collect(Collectors.toList());
		LOG.debug("[" + CLASS_NAME + "::findAll] END result.size()=" + result.size());
		return result;
	}

	
	private void initAllProvincieMancante() throws DAOException {
		LOG.debug("[" + CLASS_NAME + "::initAllProvincieMancante] BEGIN mappaComuniPerProvincia.size()=" + mappaComuniPerProvincia.size() + " / " + NUMERO_DI_PROVINCIE_ITALIANE);
		// Basta richiamarli tutti !
		provinciaDAO.findAll().stream().forEach(r -> listByCodiceProvincia(r.getCodice()));
		LOG.debug("[" + CLASS_NAME + "::initAllProvincieMancante] END mappaComuniPerProvincia.size()=" + mappaComuniPerProvincia.size() + " / " + NUMERO_DI_PROVINCIE_ITALIANE);
	}


	/**
	 * 
	 * @param codiceComune
	 * @return
	 */
	@Override
	public Comune findByPK(Integer codiceComune) throws ItemNotFoundDAOException, DAOException {
		LOG.debug("[" + CLASS_NAME + "::findByPK] IN codiceComune = " + codiceComune);
		if (codiceComune==null)
			return null;
		validateCacheAndInitIfNecessari();
		for (Integer codProvincia : mappaComuniPerProvincia.keySet()) {
			for (Comune comune : mappaComuniPerProvincia.get(codProvincia)) {
				if (comune.getCodice().equals(codiceComune)) {
					return comune;
				}
			}
		}
//TOFIX forse meglio fare la query e caricare solo la provincia giusta
		// Se arriva qui, potrebbe essere che la provincia ricercata non è in una delle regione gia in cache
		// Quindi controllo solo le regioni mancante (aggiungendole in cache e solo fino ad trovare la provincia ricercata)
		for (Integer codProvincia : provinciaDAO.findAll().stream().filter(isMancanteInMappaComuniPerProvincia()).map(r -> r.getCodice()).collect(Collectors.toList()) ) {
			for (Comune comune : listByCodiceProvincia(codProvincia)) {
				if (comune.getCodice().equals(codiceComune)) {
					return comune;
				}
			}
		}
		LOG.error("[" + CLASS_NAME + "::findByPK] Comune non trovato ! codiceComune = " + codiceComune);
		throw new ItemNotFoundDAOException();
	}
	private Predicate<Provincia> isMancanteInMappaComuniPerProvincia() {
		return p -> mappaComuniPerProvincia.get(p.getCodice()) == null;
	}

	@Override
	public Comune findByPKidx(Integer codiceProvincia, Integer codiceComune) throws ItemNotFoundDAOException, DAOException {
		LOG.debug("[" + CLASS_NAME + "::findByPKidx] IN codiceProvincia = " + codiceProvincia + "   codiceComune = " + codiceComune);
		if (codiceProvincia==null || codiceComune==null)
			return null;
		// validateCacheAndInitIfNecessari presente in listByCodiceProvincia()
		for (Comune comune : listByCodiceProvincia(codiceProvincia)) {
			if (comune.getCodice().equals(codiceComune)) {
				return comune;
			}
		}
		LOG.error("[" + CLASS_NAME + "::findByPKidx] Comune non trovato ! codiceProvincia = " + codiceProvincia + "   codiceComune = " + codiceComune);
		throw new ItemNotFoundDAOException();
	}

	/**
	 *
	 * @param codiceProvincia
	 * @return
	 */
	@Override
	public List<Comune> listByCodiceProvincia(Integer codiceProvincia) throws DAOException {
		LOG.debug("[" + CLASS_NAME + "::listByCodiceProvincia] IN codiceProvincia = " + codiceProvincia);
		if (codiceProvincia==null)
			return null;
		validateCacheAndInitIfNecessari();
		List<Comune> res = mappaComuniPerProvincia.get(codiceProvincia);
		if ( res == null ) {
			res = initByCodiceProvincia(codiceProvincia);
		}
		return res;
	}

	
	private List<Comune> initByCodiceProvincia(Integer codiceProvincia) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::initByCodiceProvincia] IN codiceProvincia = " + codiceProvincia);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_provincia", String.format("%03d" , codiceProvincia));
			List<Comune> result = (List<Comune>) getCustomNamedParameterJdbcTemplateImpl().query(ELENCO_BY_PROVINCIA, params, BeanPropertyRowMapper.newInstance(Comune.class));
			mappaComuniPerProvincia.put(codiceProvincia, result);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::initByCodiceProvincia] Errore database initByCodiceProvincia("+codiceProvincia+") : " + e.getMessage(), e);
			throw new DAOException();
		}
	}


	/**
	 *
	 * @param nome
	 * @param codiceProvincia
	 * @return
	 */
	@Override
	public Comune findByNomeCodiceProvincia(String nome, Integer codiceProvincia) throws ItemNotFoundDAOException, DAOException {
		LOG.debug("[" + CLASS_NAME + "::findByNomeCodiceProvincia] IN nome = " + nome + "   codiceProvincia = " + codiceProvincia);
		if (nome==null || codiceProvincia==null)
			return null;
		// validateCacheAndInitIfNecessari presente in listByCodiceProvincia()
		for (Comune comune : listByCodiceProvincia(codiceProvincia)) {
			if (comune.getNome().equalsIgnoreCase(nome)) {
				return comune;
			}
		}
		LOG.error("[" + CLASS_NAME + "::findByNomeCodiceProvincia] Comune non trovato ! nome = " + nome + "   codiceProvincia = " + codiceProvincia);
		throw new ItemNotFoundDAOException();
	}
	
		
	/**
	 * 
	 * @param codiceComune
	 * @return
	 */
	@Override
	public String findEmailAslByCodiceComune(Integer codiceComune) throws ItemNotFoundDAOException, DAOException {
		LOG.debug("[" + CLASS_NAME +  "::"+"findEmailAslByCodiceComune] IN codiceComune = " + codiceComune);
		
		if (codiceComune==null)
			return null;

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("codice_comune", String.format("%06d" , codiceComune));
		String emailAsl = "";
		emailAsl = (String) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_EMAIL_BY_COMUNE, params, String.class);
		return emailAsl;

	}
	
	
    //
    // Cache Management
	// 
    public void forceResetCache() {
    	mappaComuniPerProvincia.clear();
    	lastResetCache = LocalTime.now();
    	LOG.info("[" + CLASS_NAME + "::forceResetCache] BEGIN END");
    }
    public void validateCache() {
    	if (Duration.between(this.lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetCache();
    	}
    }
    //
	private void validateCacheAndInitIfNecessari() {
		if (mappaComuniPerProvincia==null) {
			init();
		}
		validateCache();
	}
	/**
	 * Inizializa la cache
	 */
	private void init() {
		mappaComuniPerProvincia = new LinkedHashMap<>();
		lastResetCache = LocalTime.now();
	}
}
