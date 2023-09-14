/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.impl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.ProvinciaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.RegioneDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonbobl.dto.extra.istat.Provincia;
import it.csi.moon.moonbobl.dto.extra.istat.Regione;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle province
 * <br>Cache Locale di 4 ore, dove viene caricate per regioni
 * 
 * @see Provincia
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
@Qualifier("moon")
public class ProvinciaDAOImpl extends JdbcTemplateDAO implements ProvinciaDAO {

	private final static String CLASS_NAME = "ProvinciaDAOImpl";

//	private static final  String FIND_BY_ID  = "SELECT codice_provincia AS codice, nome_provincia AS nome, sigla_provincia AS sigla" +
//			" FROM moon_fo_c_provincia" +
//			" WHERE codice_provincia = :codice_provincia";
	
	private static final String ELENCO_BY_REGIONE = "SELECT codice_provincia AS codice, nome_provincia AS nome, sigla_provincia AS sigla" +
			" FROM moon_fo_c_provincia" +
			" WHERE codice_regione = :codice_regione" +
			" AND flag_attivo = 'S'";
	
//	private static final String ELENCO = "SELECT codice_provincia AS codice, nome_provincia AS nome, sigla_provincia AS sigla" +
//	" FROM moon_fo_c_provincia" +
//	" WHERE flag_attivo = 'S'";


	/**
	 * Cache locale delle province
	 */
	private static final int NUMERO_DI_REGIONI_ITALIANE = 20; // 20 Regioni italiane // TODO to be dinamic  SELECT count(*)
	private static final Duration DURATION_CACHE = Duration.ofMinutes(4*60);
	private LinkedHashMap<Integer, List<Provincia>> mappaProvincePerRegione; // MAX 20 elements
	private LocalTime lastResetCache = LocalTime.now();
	
	
	@Autowired
	@Qualifier("moon")
	RegioneDAO regioneDAO;



	/**
	 *
	 * @return
	 * @throws DAOException 
	 */
	@Override
	public List<Provincia> findAll() throws DAOException {
		validateCacheAndInitIfNecessari(); // per primo del log.debug, inizializza la mappa se nulla !
		log.debug("[" + CLASS_NAME + "::findAll] BEGIN mappaProvincePerRegione.size()=" + mappaProvincePerRegione.size() + " / " + NUMERO_DI_REGIONI_ITALIANE);
		if ( mappaProvincePerRegione.size() < NUMERO_DI_REGIONI_ITALIANE ) {
			initAllRegioniMancante();
		}
		List<Provincia> result = mappaProvincePerRegione.values().stream().flatMap(List::stream).collect(Collectors.toList());
		log.debug("[" + CLASS_NAME + "::findAll] END result.size()=" + result.size());
		return result;
	}
	
	private void initAllRegioniMancante() throws DAOException {
		log.debug("[" + CLASS_NAME + "::initAllRegioniMancante] BEGIN mappaProvincePerRegione.size()=" + mappaProvincePerRegione.size() + " / " + NUMERO_DI_REGIONI_ITALIANE);
		// Basta richiamarli tutti !
		regioneDAO.findAll().stream().forEach(r -> listByCodiceRegione(r.getCodice()));
		log.debug("[" + CLASS_NAME + "::initAllRegioniMancante] END mappaProvincePerRegione.size()=" + mappaProvincePerRegione.size() + " / " + NUMERO_DI_REGIONI_ITALIANE);
	}


	/**
	 *
	 * @param codiceRegione
	 * @return
	 */
	@Override
	public List<Provincia> listByCodiceRegione(Integer codiceRegione) {
		log.debug("[" + CLASS_NAME + "::listByCodiceRegione] IN codiceRegione = " + codiceRegione);
		if (codiceRegione==null)
			return null;
		validateCacheAndInitIfNecessari();
		List<Provincia> res = mappaProvincePerRegione.get(codiceRegione);
		if ( res == null ) {
			res = initByCodiceRegione(codiceRegione);
		}
		return res;
	}


	private List<Provincia> initByCodiceRegione(Integer codiceRegione) {
		try {
			log.debug("[" + CLASS_NAME + "::initByCodiceRegione] IN codiceRegione = " + codiceRegione);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_regione", String.format("%02d" , codiceRegione));
			List<Provincia> result = (List<Provincia>) getCustomNamedParameterJdbcTemplateImpl().query(ELENCO_BY_REGIONE, params, BeanPropertyRowMapper.newInstance(Provincia.class));
			mappaProvincePerRegione.put(codiceRegione, result);
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::initByCodiceRegione] Errore database initByCodiceRegione("+codiceRegione+") : " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 
	 * @param codiceProvincia
	 * @return
	 * @throws DAOException 
	 */
	@Override
	public Provincia findByPK(Integer codiceProvincia) throws ItemNotFoundDAOException, DAOException {
		log.debug("[" + CLASS_NAME + "::findByPK] IN codiceProvincia = " + codiceProvincia);
		if (codiceProvincia==null)
			return null;
		validateCacheAndInitIfNecessari();
		for (Integer codRegione : mappaProvincePerRegione.keySet()) {
			for (Provincia provincia : mappaProvincePerRegione.get(codRegione)) {
				if (provincia.getCodice().equals(codiceProvincia)) {
					return provincia;
				}
			}
		}
		// Se arriva qui, potrebbe essere che la provincia ricercata non è in una delle regione gia in cache
		// Quindi controllo solo le regioni mancante (aggiungendole in cache e solo fino ad trovare la provincia ricercata)
		for (Integer codRegione : regioneDAO.findAll().stream().filter(isMancanteInMappaProvincePerRegione()).map(r -> r.getCodice()).collect(Collectors.toList()) ) {
			for (Provincia provincia : listByCodiceRegione(codRegione)) {
				if (provincia.getCodice().equals(codiceProvincia)) {
					return provincia;
				}
			}
		}
		log.error("[" + CLASS_NAME + "::findByPK] Provincia non trovata ! codiceProvincia = " + codiceProvincia);
		throw new ItemNotFoundDAOException();
	}
	private Predicate<Regione> isMancanteInMappaProvincePerRegione() {
		return r -> mappaProvincePerRegione.get(r.getCodice()) == null;
	}

	@Override
	public Provincia findByPKidx(Integer codiceRegione, Integer codiceProvincia) throws ItemNotFoundDAOException, DAOException {
		log.debug("[" + CLASS_NAME + "::findByPKidx] IN codiceRegione = " + codiceRegione + "   codiceProvincia = " + codiceProvincia);
		if (codiceRegione==null || codiceProvincia==null)
			return null;
		// validateCacheAndInitIfNecessari presente in listByCodiceRegione()
		for (Provincia provincia : listByCodiceRegione(codiceRegione)) {
			if (provincia.getCodice().equals(codiceProvincia)) {
				return provincia;
			}
		}
		log.error("[" + CLASS_NAME + "::findByPKidx] Provincia non trovata ! codiceRegione = " + codiceRegione + "   codiceProvincia = " + codiceProvincia);
		throw new ItemNotFoundDAOException();
	}


	@Override
	public Provincia findBySiglia(String siglaProvincia) throws ItemNotFoundDAOException, DAOException {
		log.debug("[" + CLASS_NAME + "::findBySiglia] IN siglaProvincia = " + siglaProvincia);
		if (siglaProvincia==null)
			return null;
		validateCacheAndInitIfNecessari();
		for (Entry<Integer, List<Provincia>> listaProvinceOfOneRegione : mappaProvincePerRegione.entrySet()) {
			for (Provincia provincia : listaProvinceOfOneRegione.getValue()) {
				if(siglaProvincia.equals(provincia.getSigla())) {
					return provincia;
				}
			}
	    }
		// Se arriva qui, potrebbe essere che la provincia ricercata non è in una delle regione gia in cache
		// Quindi controllo solo le regioni mancante (aggiungendole in cache e solo fino ad trovare la provincia ricercata)
		for (Integer codRegione : regioneDAO.findAll().stream().filter(isMancanteInMappaProvincePerRegione()).map(r -> r.getCodice()).collect(Collectors.toList()) ) {
			for (Provincia provincia : listByCodiceRegione(codRegione)) {
				if(siglaProvincia.equals(provincia.getSigla())) {
					return provincia;
				}
			}
		}
		log.error("[" + CLASS_NAME + "::findBySiglia] Provincia non trovata ! siglaProvincia = " + siglaProvincia);
		throw new ItemNotFoundDAOException();
	}

	@Override
	public Integer getCodiceRegione(Provincia provincia) {
		validateCacheAndInitIfNecessari();
		for (Entry<Integer, List<Provincia>> listaProvinceOfOneRegione : mappaProvincePerRegione.entrySet()) {
			for (Provincia p : listaProvinceOfOneRegione.getValue()) {
				if(provincia.equals(p)) {
					return listaProvinceOfOneRegione.getKey();
				}
			}
	    }
		return null;
	}
	
	
	
    //
    // Cache Management
	// 
    public void forceResetCache() {
    	mappaProvincePerRegione.clear();
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
		if (mappaProvincePerRegione==null) {
			init();
		}
		validateCache();
	}
	/**
	 * Inizializa la cache
	 */
	private void init() {
		mappaProvincePerRegione = new LinkedHashMap<Integer, List<Provincia>>();
		lastResetCache = LocalTime.now();
	}
}
