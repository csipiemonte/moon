/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio.impl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.apimint.toponomastica.v1.dto.CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse;
import it.csi.apimint.toponomastica.v1.dto.CivicoLight;
import it.csi.apimint.toponomastica.v1.dto.UiuLight;
import it.csi.moon.commons.dto.extra.territorio.Civico;
import it.csi.moon.commons.dto.extra.territorio.Piano;
import it.csi.moon.commons.dto.extra.territorio.PianoNUI;
import it.csi.moon.commons.dto.extra.territorio.UiuCivicoIndirizzo;
import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.ToponomasticaApiRestDAO;
import it.csi.moon.moonsrv.business.service.territorio.ToponomasticaService;
import it.csi.moon.moonsrv.business.service.territorio.mapper.CivicoMapper;
import it.csi.moon.moonsrv.business.service.territorio.mapper.PianoMapper;
import it.csi.moon.moonsrv.business.service.territorio.mapper.PianoNUIMapper;
import it.csi.moon.moonsrv.business.service.territorio.mapper.ViaMapper;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.moon.moonsrv.util.territorio.coto.decodifica.EnumTipologiaCivico;

/**
 * Metodi di business della Toponomastica di Torino via nuovi servizi REST
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 06/04/2020 - Versione initiale
 */
@Component
@Qualifier("RS")
public class ToponomasticaServiceRSImpl implements ToponomasticaService {
	
	private static final String CLASS_NAME = "ToponomasticaServiceRSImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
    private static final Set<String> VIE_DA_ESCLUDERE_NOME = Collections.unmodifiableSet(new HashSet<>(
    	Arrays.asList("VIA DELLA CASA COMUNALE", "INDIRIZZO IMPRECISATO", "EVENTO OCCASIONALE", "RESIDENTE ESTERO", 
    			"PORTA NUOVA", "PORTA SUSA", "TEATRO REGIO", "STADIO DELLE ALPI", "PISTA CICLABILE ROBERT BADEN-POWELL",
    			"AREA PEDONALE VITTIME IMMIGRAZIONE",
    			"CORSO ALESSANDRIA VENARIA REALE",
    			"CORSO LOMBARDIA VENARIA REALE",
    			"CORSO PIEMONTE VENARIA REALE",
    			"PALAZZO DEL LAVORO",
    			"PALAZZO MADAMA",
    			"PIAZZA CASTELLO PALAZZO MADAMA",
    			"PIAZZA CASTELLO PALAZZO REALE",
    			"SENZA FISSA DIMORA",
    			"STRADA DRUENTO VENARIA REALE",
    			"SVINCOLO TANGENZIALE",
    			"SVINCOLO TANGENZIALE NICHELINO",
    			"SVINCOLO TANGENZIALE VENARIA REALE",
    			"VIA COLOMBO VENARIA REALE"
    		)));
    private static final Set<String> VIE_DA_ESCLUDERE_NOME_INIT = Collections.unmodifiableSet(new HashSet<>(
    	Arrays.asList("AEROPORTO", "AIUOLA", "BORGO", "CAVALCAVIA", "GIARDINO", "PONTE", "PARCO", "SOTTOPASSAGGIO"
    		)));
    
	@Autowired
//	@Qualifier("applogic") // diretto su applogic con basicAuth apimanager:apimanager
	@Qualifier("apimint")  // via APIM internet con token
	private ToponomasticaApiRestDAO toponomasticaApiRestDAO = null;
	
	private static final Duration DURATION_CACHE = Duration.ofMinutes(60);
	private List<Via> vieCache = new ArrayList<>();
	private List<Piano> pianiCache = new ArrayList<>();
	private LocalTime lastResetCache = LocalTime.now();

	//
	// Entity Via
	@Override
	public List<Via> getVie() throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getVie] BEGIN");
			initCache();
			if(vieCache.isEmpty()) {
				vieCache = toponomasticaApiRestDAO.elencaVie().stream()
					.map(ViaMapper::remapViaLight)
					.sorted()
//					.sorted((v1, v2)->v1.compareTo(v2))
					.collect(Collectors.toList());
			}
			if (vieCache!=null) {
				LOG.debug("[" + CLASS_NAME + "::getVie] END vieCache.size()="+vieCache.size());
			}
			return vieCache;
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getVie] DAOException " + daoe.getMessage());
	    	throw new BusinessException("getVie non trovata");
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getVie] Exception " + e.getMessage());
	    	throw new BusinessException("getVie non trovata generico");
		}
	}
	
	// Disabilita filter SE filter=false OR N
	@Override
	public List<Via> getVie(String filter) throws BusinessException {
		List<Via> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getVie] BEGIN filter="+filter);
			if (isFalse(filter)) {
				LOG.debug("[" + CLASS_NAME + "::getVie] NO filter");
				result = getVie().stream()
					.collect(Collectors.toList());
			} else {
				LOG.debug("[" + CLASS_NAME + "::getVie] DEFAULT filter isInVieDaEscludere()");
				result = getVie().stream()
					.filter(isInVieDaEscludere())
					.collect(Collectors.toList());
			}
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getVie] Errore generico servizio getVie", ex);
			throw new ServiceException("Errore generico servizio elenco getVie");
		}
	}
	
	private static Predicate<Via> isInVieDaEscludere() {
		return v -> !VIE_DA_ESCLUDERE_NOME.contains(v.getNome()) && !StringUtils.startsWithAny(v.getNome(), (String[]) VIE_DA_ESCLUDERE_NOME_INIT.toArray(new String[VIE_DA_ESCLUDERE_NOME_INIT.size()]));
	}
	private static Predicate<Via> applyFilter(String filter) {
		return v -> isFalse(filter)?false:true;
	}
	private static boolean isFalse(String value) {
		return "n".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
	}
	
	@Override
	public List<Via> getVie(Integer limit, Integer skip, String filter) throws BusinessException {
		List<Via> result = new ArrayList<>();
		try {
			LOG.debug("[" + CLASS_NAME + "::getVie] BEGIN IN limit="+limit+"  skip="+skip+"  filter="+filter);
			if (limit==0) {
				result = getVie(filter);
			} else {
				result = getVie(filter).stream()
					.skip(skip)
					.limit(limit)
					.collect(Collectors.toList());
			}
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getVie] (limit) BusinessException " + be.getMessage());
			throw be;
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getVie] (limit) Exception " + e.getMessage());
	    	throw new BusinessException("getVie (limit) non trovata generico");
		}
	}

	
	@Override
	public Via getViaById(Integer codiceVia) throws BusinessException {
		Via result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getViaById] BEGIN IN codiceVia="+codiceVia);
			result = getVie().stream()
				.filter(v -> codiceVia.equals(v.getCodice()))
				.findAny()
				.orElse(null);
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getViaById] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getViaById] Exception " + e.getMessage());
	    	throw new BusinessException("getViaById non trovata generico");
		}
	}
	
	/**
	 * Ricerca per solo Descrizione Via Completa corrispondente a viaLight.getSedime().getDescrizione()
	 * Non includere il sedime nel parametro di ricerca
	 * http://tst-applogic.comune.torino.it/ctsapi-jb/v1/vielight
	 * 
	 * es.: usare VEROLENGO , (non usare VAI VEROLENGO)
	 * es.: usare GUIDO RENI, (non usare VIA GUIDO RENI, ne meno solo RENI)
	 */
	@Override
	public Via findViaByDesc(String descVia) throws BusinessException {
		Via result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::findViaByDesc] BEGIN IN descVia="+descVia);
			String descViaTrimUpper = descVia.trim().toUpperCase();
			initCache();		
			
			
			// test comparazione indirizzo comune di torino
			result = toponomasticaApiRestDAO.elencaVie().stream()
				.filter(vl -> vl.getSedime()!=null && vl.getSedime().getDescrizione()!=null 
							&& (vl.getSedime().getDescrizione() + " "+vl.getDenominazionePrincipale()).equalsIgnoreCase(descViaTrimUpper))
				.map(ViaMapper::remapViaLight)
				.findAny()
				.orElse(null);
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::findViaByDesc] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::findViaByDesc] Exception " + e.getMessage());
	    	throw new BusinessException("findViaByDesc non trovata generico");
		}
	}
	
	/**
	 * Ricerca per solo Descrizione Via Completa corrispondente a viaLight.getSedime().getDescrizione()
	 * Non includere il sedime nel parametro di ricerca
	 * http://tst-applogic.comune.torino.it/ctsapi-jb/v1/vielight
	 * 
	 * es.: usare VEROLENGO , (non usare VAI VEROLENGO)
	 * es.: usare GUIDO RENI, (non usare VIA GUIDO RENI, ne meno solo RENI)
	 */
	@Override
	public List<Via> findVieByDesc(String descVia) throws BusinessException {
		List<Via> result = new ArrayList<>();
		try {
			LOG.debug("[" + CLASS_NAME + "::findVieByDesc] BEGIN IN descVia="+descVia);
			String descViaTrimUpper = descVia.trim().toUpperCase();
			initCache();				
			result = toponomasticaApiRestDAO.elencaVie().stream()
				.filter(vl -> vl.getSedime()!=null && vl.getSedime().getDescrizione()!=null 
							&& vl.getSedime().getDescrizione().contains(descViaTrimUpper))
				.map(ViaMapper::remapViaLight)
				.sorted()
				.collect(Collectors.toList());
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::findVieByDesc] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::findVieByDesc] Exception " + e.getMessage());
	    	throw new BusinessException("findVieByDesc non trovata generico");
		}
	}

	
    //
    // Integer - Numero Radici
	@Override
	public List<Integer> getNumeriRadiceByVia(Integer codiceVia) throws BusinessException {
		List<Integer> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getNumeriRadiceByVia] BEGIN IN codiceVia="+codiceVia);
			result = toponomasticaApiRestDAO.elencaNumeriRadiceDiUnaVia(codiceVia);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getNumeriRadiceByVia] Errore generico servizio getNumeriRadiceByVia", ex);
			throw new ServiceException("Errore generico servizio elenco getNumeriRadiceByVia");
		}
		return result;
	}
	
	
    //
	// Entity Civico
	@Override
	public List<Civico> getCiviciByVia(Integer codiceVia, String tipologieCivico, List<Integer> stati) throws BusinessException {
		List<Civico> result = new ArrayList<>();
		try {
			LOG.debug("[" + CLASS_NAME + "::getCiviciByVia] BEGIN IN codiceVia="+codiceVia+"   tipologieCivico="+tipologieCivico);
//			Predicate<CivicoLight> filtro = makePredicateTipologieCivico(tipologieCivico);
			List<Predicate<CivicoLight>> allPredicates = new ArrayList<>();
		    allPredicates.add(makePredicateTipologieCivico(tipologieCivico));
		    if ( stati!=null && stati.size()>0 ) {
		    	allPredicates.add(makePredicateStatiOfCivico(stati));
		    }
			result = toponomasticaApiRestDAO.elencaCiviciDiUnaVia(codiceVia).stream()
				.filter(allPredicates.stream().reduce(x->true, Predicate::and))
				.map(CivicoMapper::remapCivocoLight)
				.collect(Collectors.toList());
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getCiviciByVia] Errore generico servizio getCiviciByVia", ex);
			throw new ServiceException("Errore generico servizio elenco getCiviciByVia");
		}
		return result;
	}
	
	/**
	 * tipologieCivico (@see EnumTipologiaCivico), in combinazione:
	 * 1     RESIDENZIALE
	 * 3     MISTO
	 * 4     NON_RESIDENZIALE
	 * 1,3   RESIDENZIALE & MISTO
	 * 1,3,4 RESIDENZIALE, MISTO & NON_RESIDENZIALE
	 */
	@Override
	public List<Civico> getCiviciByViaNumero(Integer codiceVia, Integer numero, String tipologieCivico, List<Integer> stati) throws BusinessException {
		List<Civico> result = new ArrayList<>();
		try {
			LOG.debug("[" + CLASS_NAME + "::getCiviciByViaNumero] BEGIN IN codiceVia="+codiceVia+"   numero="+numero+"   tipologieCivico="+tipologieCivico);
//			Predicate<CivicoLight> filtro = makePredicateTipologieCivico(tipologieCivico);
			List<Predicate<CivicoLight>> allPredicates = new ArrayList<>();
		    allPredicates.add(makePredicateTipologieCivico(tipologieCivico));
		    if ( stati!=null && stati.size()>0 ) {
		    	allPredicates.add(makePredicateStatiOfCivico(stati));
		    }
		    
			result = toponomasticaApiRestDAO.elencaCiviciDiUnaViaNumero(codiceVia, numero).stream()
				.filter(allPredicates.stream().reduce(x->true, Predicate::and))
				.map(CivicoMapper::remapCivocoLight)
				.collect(Collectors.toList());
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getCiviciByViaNumero] Errore generico servizio getCiviciByViaNumero", ex);
			throw new ServiceException("Errore generico servizio elenco getCiviciByViaNumero");
		}
		return result;
	}

	private Predicate<CivicoLight> makePredicateTipologieCivico(String tipologieCivico) {
		Predicate<CivicoLight> filtro = civicoLight -> civicoLight.getTipologia()==null || 
				  (!EnumTipologiaCivico.VIRTUALE.getCodice().equals(civicoLight.getTipologia()) );                    // esclude sempre VIRTUALE

		if ( StringUtils.isNotEmpty(tipologieCivico) ) {
			final List listTipologie = Arrays.asList(tipologieCivico.split(","));
			filtro = civicoLight -> civicoLight.getTipologia()==null || (
					 (!EnumTipologiaCivico.VIRTUALE.getCodice().equals(civicoLight.getTipologia()))                   // esclude sempre VIRTUALE
					&& listTipologie.contains(civicoLight.getTipologia() ) );    // filtro su singolo solo se richiesto
		}
		return filtro;
	}

	private Predicate<CivicoLight> makePredicateStatiOfCivico(List<Integer> stati) {
//		EnumStatoCivico.SPPRESSO
//		EnumStatoCivico.ATTIVO
		return civicoLight -> civicoLight.getStato()==null || (
				( stati!=null && stati.size()>0 ) && stati.contains(civicoLight.getStato() ) );
	}
	
	@Override
	public Civico getCivicoById(Integer codiceVia, Integer codiceCivico) throws BusinessException {
		Civico result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getCivicoById] BEGIN IN codiceVia="+codiceVia+"   codiceCivico="+codiceCivico);
			result = getCiviciByVia(codiceVia, null, null).stream()
					.filter(c -> codiceCivico.equals(c.getCodice()))
					.findAny()
					.orElse(null);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Errore generico servizio getCivicoById", ex);
			throw new ServiceException("Errore generico servizio elenco getCivicoById");
		}
		return result;
	}
	@Override
	public it.csi.apimint.toponomastica.v1.dto.Civico getCivicoById(Integer codiceCivico) throws BusinessException {
		it.csi.apimint.toponomastica.v1.dto.Civico result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getCivicoById] BEGIN IN  codiceCivico="+codiceCivico);
			result  = toponomasticaApiRestDAO.findCivicoById(codiceCivico);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getCivicoById] Errore generico servizio getCivicoById", ex);
			throw new ServiceException("Errore generico servizio elenco getCivicoById");
		}
		return result;
	}
	
    //
	// Entity PianoNUI
	@Override
	public List<PianoNUI> getPianiNuiByCivico(Integer codiceVia, Integer codiceCivico) throws BusinessException {
		List<PianoNUI> result = new ArrayList<>();
		try {
			LOG.debug("[" + CLASS_NAME + "::getPianiNuiByCivico] BEGIN IN codiceVia="+codiceVia+"   codiceCivico="+codiceCivico);
			List<UiuLight> elencoUiuLight = toponomasticaApiRestDAO.elencaUiuLightDiUnCivico(codiceCivico);
			for (UiuLight uiuLight : elencoUiuLight) {
				result.add(PianoNUIMapper.remap(uiuLight));
			}
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getPianiNuiByCivico] Errore generico servizio getPianiNuiByCivico", ex);
			throw new ServiceException("Errore generico servizio elenco getPianiNuiByCivico");
		}
		return result;
	}

	@Override
	public PianoNUI getPianoNuiById(Integer codiceVia, Integer codiceCivico, Integer nui) throws BusinessException {
		PianoNUI result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getPianoNuiById] BEGIN IN codiceVia="+codiceVia+"   codiceCivico="+codiceCivico+"   nui="+nui);
			result = getPianiNuiByCivico(codiceVia, codiceCivico).stream()
					.filter(PianoNUIMapper.isNUI(nui))
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
		List<Piano> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getPiani] BEGIN");
			initCache();
			if(pianiCache.isEmpty()) {
				List<it.csi.apimint.toponomastica.v1.dto.Piano> piani = toponomasticaApiRestDAO.elencaPiani();
				pianiCache = piani.stream().map(p -> new Piano(p.getCodicePiano(), p.getDescrizione())).collect(Collectors.toList());
			}
			result = pianiCache;
			return result;
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getPiani] DAOException " + daoe.getMessage());
	    	throw new BusinessException("getPiani non trovata");
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getPiani] Exception " + e.getMessage());
	    	throw new BusinessException("getPiani non trovata generico");
		}
	}

	@Override
	public Piano getPianoById(String codicePiano) throws BusinessException {
		Piano result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getPianoById] BEGIN IN codicePiano="+codicePiano);
			result = getPiani().stream()
				.filter(p -> codicePiano.equals(p.getCodice()))
				.findAny()
				.orElse(null);
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPianoById] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getPianoById] Exception " + e.getMessage());
	    	throw new BusinessException("getPianoById non trovata generico");
		}
	}

	@Override
	public List<Piano> getPianiByCivico(Integer codiceVia, Integer codiceCivico) throws BusinessException {
		List<Piano> result = new ArrayList<>();
		try {
			LOG.debug("[" + CLASS_NAME + "::getPianiByCivico] BEGIN IN codiceVia="+codiceVia+"   codiceCivico="+codiceCivico);
			result = toponomasticaApiRestDAO.elencaUiuLightDiUnCivico(codiceCivico).stream()
				.map(PianoMapper::remap)
				.filter(distinctByKey(Piano::getCodice))
				.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getPianiByCivico] Errore generico servizio getPianiByCivico", ex);
			throw new ServiceException("Errore generico servizio elenco getPianiNuiByCivico");
		}
	}
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
	
	//
	// Catasto
	@Override
	public List<UiuCivicoIndirizzo> getUiuCivicoIndirizzoByTriplettaCatastale(String foglio, String numero, String subalterno) throws BusinessException {
		List<UiuCivicoIndirizzo> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getUiuCivicoIndirizzoByTriplettaCatastale] BEGIN");
			List<CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse> list = toponomasticaApiRestDAO.cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale(foglio, numero, subalterno);
			result = list.stream().map(o -> new UiuCivicoIndirizzo(o.getIdUiu(), o.getIdCivico(), o.getIndirizzoCompleto())).collect(Collectors.toList());
			return result;
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getUiuCivicoIndirizzoByTriplettaCatastale] DAOException " + daoe.getMessage());
	    	throw new BusinessException("getUiuCivicoIndirizzoByTriplettaCatastale non trovata");
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getUiuCivicoIndirizzoByTriplettaCatastale] Exception " + e.getMessage());
	    	throw new BusinessException("getUiuCivicoIndirizzoByTriplettaCatastale non trovata generico");
		}
	}
	
	
    //
    // Gestione della Cache delle Vie e Piano
	//
    public void forceResetCache() {
    	vieCache.clear();
    	pianiCache.clear();
    	lastResetCache = LocalTime.now();
    	LOG.info("[" + CLASS_NAME + "::forceResetCache] BEGIN END");
    }
    public void initCache() {
    	if (Duration.between(this.lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetCache();
    	}
    }


}
