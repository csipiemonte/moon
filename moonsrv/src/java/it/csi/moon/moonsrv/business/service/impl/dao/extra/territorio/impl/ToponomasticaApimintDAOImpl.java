/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.impl;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.apimint.toponomastica.v1.dto.CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse;
import it.csi.apimint.toponomastica.v1.dto.CivicoLight;
import it.csi.apimint.toponomastica.v1.dto.Piano;
import it.csi.apimint.toponomastica.v1.dto.UiuLight;
import it.csi.apimint.toponomastica.v1.dto.ViaLight;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimintTemplateImpl;
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
@Qualifier("apimint")
@Component
public class ToponomasticaApimintDAOImpl extends ApimintTemplateImpl implements ToponomasticaApiRestDAO {
	
	private static final String CLASS_NAME = "ToponomasticaApimintDAOImpl";

	private static final String APIMINT_EXTRA_PATH_TERRITORIO_TOPONOMASTICA_CTSAPICOTO_V1 = "/territorio/toponomastica/ctsapicoto/v1";

    public ToponomasticaApimintDAOImpl() {
    	super();
    	setEndpoint(EnvProperties.readFromFile(EnvProperties.APIMINT_ENDPOINT));
    }
	
    @Override
    public boolean isFAIL_ON_UNKNOWN_PROPERTIES() {
    	return false;
    }
    
	protected ResteasyClient getResteasyClient() {
		return new ResteasyClientBuilder().build();
	}

	protected String getPathExtra() {
		return APIMINT_EXTRA_PATH_TERRITORIO_TOPONOMASTICA_CTSAPICOTO_V1;
	}
	
    /*
     * Setter Getter dei Parametri specifici a Toponomastica
     */
	public static String getAppId() {
		return EnvProperties.readFromFile(EnvProperties.APIMINT_TOPONOMASTICA_APP_ID);
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
	public it.csi.apimint.toponomastica.v1.dto.Civico findCivicoById(Integer codiceCivico) throws DAOException {
		final String methodName = "findCivicoById";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN codiceCivico="+codiceCivico);
			String url = "/civici/" + codiceCivico ;
			ResponseUUID< it.csi.apimint.toponomastica.v1.dto.Civico[]> result = getJson(url, it.csi.apimint.toponomastica.v1.dto.Civico[].class);
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
		final String methodName = "cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] BEGIN");
			String url = "/civici/foglio/" + foglio + "/numero/" + numero + "/subalterno/" + subalterno + "/indirizzo";
			ResponseUUID<CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse[]> result = getJson(url, CercaIdUiuIdCivicoIndirizzoPianoNuiByDatiCatastaliResponse[].class);
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] 200 :: " + result.getResponse());
			return List.of(result.getResponse());
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			long end = System.currentTimeMillis();
			float sec = (end - start); 
			LOG.info("cercaIdUiuIdCivicoIndirizzoPianoNuiByTriplettaCatastale() in " + sec + " milliseconds.");
			LOG.debug("[" + CLASS_NAME + "::" + methodName +"] END");
		}
	}
	
	@Override
	protected MultivaluedMap<String, Object> getHeadersExtra() {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
		headers.add("Content-Type", "application/json");
		if (!StringUtils.isEmpty(getAppId())) {
			headers.add("app-id",  getAppId());
			headers.add("app_id",  getAppId());
		}
		return headers;
	}


}
