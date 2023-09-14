/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.impl;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.apimint.toponomastica.v1.dto.CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse;
import it.csi.apimint.toponomastica.v1.dto.Civico;
import it.csi.apimint.toponomastica.v1.dto.CivicoLight;
import it.csi.apimint.toponomastica.v1.dto.Piano;
import it.csi.apimint.toponomastica.v1.dto.UiuLight;
import it.csi.apimint.toponomastica.v1.dto.ViaLight;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApiRestTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.ToponomasticaApiRestDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;

/**
* Implementazione DAO Toponomastica di Torino via servizi REST via API Manager Outer
* 
* @author Francesco Zucaro
* @author Laurent Pissard
* 
* @since 1.0.0
*/
@Qualifier("applogic")
@Component
public class ToponomasticaApiRestDAOImpl extends ApiRestTemplateImpl implements ToponomasticaApiRestDAO {
	
	private static final String CLASS_NAME = "ToponomasticaApiRestDAOImpl";
	
    public ToponomasticaApiRestDAOImpl() {
    	super();
    	setEndpoint(EnvProperties.readFromFile(EnvProperties.TOPO_APPLOGIC_ENDPOINT));
    }
    
    /*
     * Setter Getter dei Parametri specifici a Toponomastica
     */
	public static String getAppId() {
		return EnvProperties.readFromFile(EnvProperties.APIMINT_TOPONOMASTICA_APP_ID);
	}
	@Override
	public String getUsername() {
		return EnvProperties.readFromFile(EnvProperties.TOPO_APPLOGIC_USERNAME);
	}
	@Override
	public String getPassword() {
		return EnvProperties.readFromFile(EnvProperties.TOPO_APPLOGIC_PASSWORD);
	}
	
	//
	// Entity Via
	@Override
	public List<ViaLight> elencaVie() throws DAOException {
		final String methodName = "elencaVie";
		long start = System.currentTimeMillis();
		List<ViaLight> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
			String url = "/vielight";
			ResponseUUID<ViaLight[]> response = getJson(url, ViaLight[].class);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + response.getResponse());
			}
			result = List.of(response.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio cercaElencoVie", e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(CLASS_NAME + "." + methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END result.size()=" + ((result!=null)?result.size():"null"));
		}
		return result;
	}

    
    //
    // Numero Radici
	@Override
	public List<Integer> elencaNumeriRadiceDiUnaVia(Integer idVia) throws DAOException {
		final String methodName = "elencaNumeriRadiceDiUnaVia";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
			String url = "/vie/" + idVia + "/civicilight/numeriRadice" ;
			ResponseUUID<Integer[]> result = getJson(url, Integer[].class);
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + result.getResponse());
			return List.of(result.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info("elencoNumeriRadiceByIdvia() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
	}

	
    //
	// Entity Civico
	/** elencaCiviciDiUnaViaLight /vie/{idvia}/civicilight */
	public List<CivicoLight> elencaCiviciDiUnaVia(Integer idVia) throws DAOException {
		final String methodName = "elencaCiviciDiUnaVia";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
			String url = "/vie/" + idVia + "/civicilight";
			ResponseUUID<CivicoLight[]> result = getJson(url, CivicoLight[].class);
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + result.getResponse());
			return List.of(result.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(methodName +"() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
	}
	
	@Override
	public List<CivicoLight> elencaCiviciDiUnaViaNumero(Integer idVia, Integer numero) throws DAOException {
		final String methodName = "elencaCiviciDiUnaViaNumero";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
			String url = "/vie/" + idVia + "/civicilight/numero/" + numero ;
			ResponseUUID<CivicoLight[]> result = getJson(url, CivicoLight[].class);
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + result.getResponse());
			return List.of(result.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
	}

	@Override
	public Civico findCivicoById(Integer codiceCivico) throws DAOException {
		final String methodName = "findCivicoById";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN codiceCivico="+codiceCivico);
			String url = "/civici/" + codiceCivico ;
			ResponseUUID<Civico[]> result = getJson(url, Civico[].class);
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + result.getResponse());
			return List.of(result.getResponse()).get(0); // Prendo il primo
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName + " with codiceCivico="+codiceCivico, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info(methodName + "() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
	}
	
	
	@Override
	public List<UiuLight> elencaUiuLightDiUnCivico(Integer idCivico) throws DAOException {
		final String methodName = "elencaUiuLightDiUnCivico";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
			String url = "/civici/" + idCivico + "/uiulight";
			ResponseUUID<UiuLight[]> result = getJson(url, UiuLight[].class);
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + result.getResponse());
			return List.of(result.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info("elencaUiuLightDiUnCivico() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
	}

	@Override
	public List<Piano> elencaPiani() throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::elencaPiani] BEGIN");
			String url = "/uiu/piani";
			ResponseUUID<Piano[]> result = getJson(url, Piano[].class);
			LOG.debug("[" + CLASS_NAME + "::elencaPiani] 200 :: " + result.getResponse());
			return List.of(result.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::elencaPiani] Errore generico servizio elencaPiani", e);
			throw new DAOException("Errore generico servizio elencaPiani");
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info("elencaPiani() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::elencaPiani] END");
		}
	}


	@Override
	public List<CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse> cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale(
			String foglio, String numero, String subalterno) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale] BEGIN");
			String url = "/civici/foglio/" + foglio + "/numero/" + numero + "/subalterno/" + subalterno + "/indirizzo";
			ResponseUUID<CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse[]> result = getJson(url, CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse[].class);
			LOG.debug("[" + CLASS_NAME + "::cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale] 200 :: " + result.getResponse());
			return List.of(result.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::elencaPiani] Errore generico servizio cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale", e);
			throw new DAOException("Errore generico servizio cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale");
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info("cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale] END");
		}
	}
	
	
	@Override
	protected MultivaluedMap<String, Object> getHeadersExtra() {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
		if (!StringUtils.isEmpty(getAppId())) {
			headers.add("app-id",  getAppId());
		}
// Sopostato gestione BasicAuth in ApiRestTemplateImpl su RestEasy
//		if (!StringUtils.isEmpty(getUsername()) && !StringUtils.isEmpty(getPassword())) {
//			String secret = getUsername() + ":" + getPassword();
//			String encoded = new String(Base64.encodeBase64(secret.getBytes()));
//			headers.add("Authorization", "Basic " + encoded);
//		}
		return headers;
	}

}
