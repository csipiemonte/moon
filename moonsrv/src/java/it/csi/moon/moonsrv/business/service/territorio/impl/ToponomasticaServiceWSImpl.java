/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio.impl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.territorio.Civico;
import it.csi.moon.commons.dto.extra.territorio.Piano;
import it.csi.moon.commons.dto.extra.territorio.PianoNUI;
import it.csi.moon.commons.dto.extra.territorio.UiuCivicoIndirizzo;
import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.ToponomasticaDAO;
import it.csi.moon.moonsrv.business.service.territorio.ToponomasticaService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business della Toponomastica di Torino via WebService
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
@Qualifier("WS")
public class ToponomasticaServiceWSImpl implements ToponomasticaService {
	
	private static final String CLASS_NAME = "ToponomasticaServiceWSImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	@Qualifier("mock")
	private ToponomasticaDAO toponomasticaDAO = null;

	private static final Duration DURATION_CACHE = Duration.ofMinutes(60);
	private List<Via> vieCache = new ArrayList<>();
	private LocalTime lastResetCache = LocalTime.now();
	
	@Override
	public List<Via> getVie() throws BusinessException {
		try {
			initCache();
			if(vieCache.isEmpty()) {
				vieCache = toponomasticaDAO.getVie();
			}
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getVie] Errore generico servizio getVie", ex);
			throw new ServiceException("Errore generico servizio elenco getVie");
		}
		return vieCache;
	}
	
	@Override
	public List<Via> getVie(String filter) throws BusinessException {
		List<Via> result = null;
		try {
			result = getVie();
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getVie] Errore generico servizio getVie", ex);
			throw new ServiceException("Errore generico servizio elenco getVie");
		}
		return result;
	}
	
	@Override
	public List<Via> getVie(Integer limit, Integer skip, String filter) throws BusinessException {
		List<Via> result = null;
		try {
			result = toponomasticaDAO.getVie(limit, skip);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getVie] Errore generico servizio getVie", ex);
			throw new ServiceException("Errore generico servizio elenco getVie");
		}
		return result;
	}

	
	@Override
	public Via getViaById(Integer codiceVia) throws BusinessException {
		Via result = null;
		try {
			result = toponomasticaDAO.getViaByPK(codiceVia);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getViaById] Errore generico servizio getViaById", ex);
			throw new ServiceException("Errore generico servizio elenco getViaById");
		}
		return result;
	}
	
	/**
	 * NotImplementedException
	 * Ricerca per solo Descrizione Via Completa corrispondente a viaLight.getSedime().getDescrizione()
	 * Non includere il sedime nel parametro di ricerca
	 * http://tst-applogic.comune.torino.it/ctsapi-jb/v1/vielight
	 * 
	 * es.: usare VEROLENGO , (non usare VAI VEROLENGO)
	 * es.: usare GUIDO RENI, (non usare VIA GUIDO RENI, ne meno solo RENI)
	 */
	@Override
	public Via findViaByDesc(String descVia) throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::findViaByDesc] NotImplementedException");
		throw new ServiceException("NotImplementedException " + CLASS_NAME + ".findViaByDesc");
	}
	
	/**
	 * NotImplementedException
	 * Ricerca per solo Descrizione Via Completa corrispondente a viaLight.getSedime().getDescrizione()
	 * Non includere il sedime nel parametro di ricerca
	 * http://tst-applogic.comune.torino.it/ctsapi-jb/v1/vielight
	 * 
	 * es.: usare VEROLENGO , (non usare VAI VEROLENGO)
	 * es.: usare GUIDO RENI, (non usare VIA GUIDO RENI, ne meno solo RENI)
	 */
	@Override
	public List<Via> findVieByDesc(String descVia) throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::findVieByDesc] NotImplementedException");
		throw new ServiceException("NotImplementedException " + CLASS_NAME + ".findVieByDesc");
	}
	
	
    //
    // Integer - Numero Radici
	@Override
	public List<Integer> getNumeriRadiceByVia(Integer codiceVia) throws BusinessException {
			LOG.error("[" + CLASS_NAME + "::getNumeriRadiceByVia] NotImplementedException");
			throw new ServiceException("NotImplementedException " + CLASS_NAME + ".getNumeriRadiceByVia");
	}
	
	
    //
	// Entity Civico
	@Override
	public List<Civico> getCiviciByVia(Integer codiceVia, String tipologieCivico, List<Integer> stati) throws BusinessException {
		List<Civico> result = null;
		try {
			LOG.warn("[" + CLASS_NAME + "::getCiviciByVia] NotImplementedWarning filter by tipologieCivico on WS");
			result = toponomasticaDAO.findCiviciByCodiceVia(codiceVia);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getCiviciByVia] Errore generico servizio getCiviciByVia", ex);
			throw new ServiceException("Errore generico servizio elenco getCiviciByVia");
		}
		return result;
	}

	@Override
	public List<Civico> getCiviciByViaNumero(Integer codiceVia, Integer numero, String tipologieCivico, List<Integer> stati) throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::getCiviciByViaNumero] NotImplementedException");
		throw new ServiceException("NotImplementedException " + CLASS_NAME + ".getCiviciByViaNumero");
	}
	
	@Override
	public Civico getCivicoById(Integer codiceVia, Integer codiceCivico) throws BusinessException {
		Civico result = null;
		try {
			result = toponomasticaDAO.findCivicoByPK(codiceVia, codiceCivico);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Errore generico servizio getCivicoById", ex);
			throw new ServiceException("Errore generico servizio getCivicoById");
		}
		return result;
	}

	@Override
	public it.csi.apimint.toponomastica.v1.dto.Civico getCivicoById(Integer codiceCivico) throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::getCivicoById] NotImplementedException");
		throw new ServiceException("NotImplementedException " + CLASS_NAME + ".getCivicoById");
	}
	
	
    //
	// Entity PianoNUI
	@Override
	public List<PianoNUI> getPianiNuiByCivico(Integer codiceVia, Integer codiceCivico) throws BusinessException {
		List<PianoNUI> result = null;
		try {
			result = toponomasticaDAO.findPianiNUIByCivico(codiceVia,codiceCivico);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getPianiNuiByCivico] Errore generico servizio getPianiNuiByCivico", ex);
			throw new ServiceException("Errore generico servizio elenco getPianiNuiByCivico");
		}
		return result;
	}

	public PianoNUI getPianoNuiById(Integer codiceVia, Integer codiceCivico, Integer uiu) throws BusinessException {
		PianoNUI result = null;
		try {
			result = getPianiNuiByCivico(codiceVia, codiceCivico).stream()
					.filter(u -> uiu.equals(u.getCodice()))
					.findAny()
					.orElse(null);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getPianoNuiById] Errore generico servizio getPianoNuiById", ex);
			throw new ServiceException("Errore generico servizio getPianoNuiById");
		}
		return result;
	}
	
	
    //
	// Entity Piano
	@Override
	public List<Piano> getPiani() throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::getPiani] NotImplementedException");
		throw new ServiceException("NotImplementedException " + CLASS_NAME + ".getPiani");
	}

	@Override
	public Piano getPianoById(String codicePiano) throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::getPianoById] NotImplementedException");
		throw new ServiceException("NotImplementedException " + CLASS_NAME + ".getPianoById");
	}

	@Override
	public List<Piano> getPianiByCivico(Integer codiceVia, Integer codiceCivico) throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::getPianoById] NotImplementedException");
		throw new ServiceException("NotImplementedException " + CLASS_NAME + ".getPianoById");
//		List<Piano> result = null;
//		try {
//			result = toponomasticaDAO.findPianiNUIByCivico(codiceVia,codiceCivico).stream()
//					.map(PianoMapper::remap)
//					.filter(distinctByKey(Piano::getCodice))
//					.collect(Collectors.toList());
//		} catch (Throwable ex) {
//			LOG.error("[" + CLASS_NAME + "::getPianiNuiByCivico] Errore generico servizio getPianiNuiByCivico", ex);
//			throw new ServiceException("Errore generico servizio elenco getPianiNuiByCivico");
//		}
//		return result;
	}
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
    //
    // Gestione della Cache delle Vie
	//
    public void forceResetCache() {
    	vieCache.clear();
    	lastResetCache = LocalTime.now();
    	LOG.info("[" + CLASS_NAME + "::forceResetCache] BEGIN END");
    }
    public void initCache() {
    	if (Duration.between(this.lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetCache();
    	}
    }

	
	
	//
	// Catasto
	@Override
	public List<UiuCivicoIndirizzo> getUiuCivicoIndirizzoByTriplettaCatastale(String foglio, String numero, String subalterno) throws BusinessException {
		LOG.error("[" + CLASS_NAME + "::getUiuCivicoIndirizzoByTriplettaCatastale] NotImplementedException");
		throw new ServiceException("NotImplementedException " + CLASS_NAME + ".getUiuCivicoIndirizzoByTriplettaCatastale");
	}

}
