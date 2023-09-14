/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.wf.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.apimint.cosmo.v1.dto.AggiornaEventoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.AggiornaEventoFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.AvviaProcessoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.AvviaProcessoFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.CreaDocumentiFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaEventoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaEventoFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.CreaNotificaFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaNotificaFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.CreaPraticaFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaPraticaFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.EliminaEventoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.EsitoCreazioneDocumentiFruitore;
import it.csi.apimint.cosmo.v1.dto.FileUploadResult;
import it.csi.apimint.cosmo.v1.dto.GetPraticaFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.InviaSegnaleFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.InviaSegnaleFruitoreResponse;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimintTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.wf.CosmoDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;

/**
 * Implementazione DAO COSMO via servizi REST via API Manager Outer
 * 
 * @author Laurent Pissard
 */
@Qualifier("apimint")
@Component
public class CosmoApimintDAOImpl extends ApimintTemplateImpl implements CosmoDAO {
	
	private static final String CLASS_NAME = "CosmoApimintDAOImpl";
	
	private static final String WF_COSMO_APIMINT_URL = EnvProperties.readFromFile(EnvProperties.WF_COSMO_APIMINT_ENDPOINT);
	private static final Map<Integer,String> COSMO_MAP_STATUS_CODE_MESSAGE;
	static {
        Map<Integer, String> map = new HashMap<>();
		map.put(401, "401 - Unauthorized - il fruitore non e' stato autenticato correttamente oppure non e' stato registrato come fruitore abilitato");
		map.put(403, "403 - Forbidden - il fruitore non e' autorizzato alla specifica operazione richiesta");
		map.put(404, "404 - NotFound - la risorsa richiesta non esiste");
		map.put(409, "409 - Conflict - una risorsa corrispondente agli identificativi univoci");
		map.put(500, "500 - InternalServerError - si e' verificato un errore interno.");
        COSMO_MAP_STATUS_CODE_MESSAGE = Collections.unmodifiableMap(map);
    }
	
    public CosmoApimintDAOImpl() {
    	super();
    	setFAIL_ON_UNKNOWN_PROPERTIES(false);
    }
    
//	protected String getPathExtra() {
//		return apimintCosmoPath;
//	}
	protected String getBaseApimintUrl() {
		return WF_COSMO_APIMINT_URL;
	}
	


	//
	// Documenti
	@Override
	public EsitoCreazioneDocumentiFruitore creaDocumento(CreaDocumentiFruitoreRequest documento) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::creaDocumento] BEGIN");
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);
        	String url = "/documenti";
        	ResponseUUID<EsitoCreazioneDocumentiFruitore> response = postJson(url, EsitoCreazioneDocumentiFruitore.class, documento);
        	LOG.debug("[" + CLASS_NAME + "::creaDocumento] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::creaDocumento] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaDocumento] Errore generico servizio creaDocumento", e);
			throw new DAOException("Errore generico servizio creaDocumento");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::creaDocumento] END in " + sec + " milliseconds.");
        }
	}
	
	@Override
	public byte[] getContenuto(String idPratica) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::getContenuto] BEGIN");
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);        	
        	String url = "/documenti/"+idPratica+"/contenuto";
        	byte[] response = getJsonWithBinaryRedirect(url);
        	LOG.debug("[" + CLASS_NAME + "::getContenuto] lenght :: " + response.length);
    	    return response;
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getContenuto] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getContenuto] Errore generico servizio getContenuto", e);
			throw new DAOException("Errore generico servizio getContenuto");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::getContenuto] END in " + sec + " milliseconds.");
        }
	}

	//
	// Pratiche
	@Override
	public GetPraticaFruitoreResponse getPratica(String idPratica) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::getPratica] BEGIN IN idPratica = " + idPratica);
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);        	
        	String url = "/pratiche/" + idPratica;
        	ResponseUUID<GetPraticaFruitoreResponse> response = getJson(url, GetPraticaFruitoreResponse.class);
        	LOG.debug("[" + CLASS_NAME + "::getPratica] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::getPratica] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getPratica] Errore generico servizio getPratica", e);
			throw new DAOException("Errore generico servizio getPratica");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::getPratica] END in " + sec + " milliseconds.");
        }
	}

	@Override
	public CreaPraticaFruitoreResponse creaPratica(CreaPraticaFruitoreRequest pratica) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::creaPratica] BEGIN");
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);        	
        	String url = "/pratiche";
        	ResponseUUID<CreaPraticaFruitoreResponse> response = postJson(url, CreaPraticaFruitoreResponse.class, pratica);
        	LOG.debug("[" + CLASS_NAME + "::creaPratica] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::creaPratica] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaPratica] Errore generico servizio creaPratica", e);
			throw new DAOException("Errore generico servizio creaPratica");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::creaPratica] END in " + sec + " milliseconds.");
        }
	}


	//
	// Eventi
	@Override
	public CreaEventoFruitoreResponse creaEvento(CreaEventoFruitoreRequest evento) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::creaEvento] BEGIN");
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);        	
        	String url = "/pratiche";
        	ResponseUUID<CreaEventoFruitoreResponse> response = postJson(url, CreaEventoFruitoreResponse.class, evento);
        	LOG.debug("[" + CLASS_NAME + "::creaEvento] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::creaEvento] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaEvento] Errore generico servizio creaEvento", e);
			throw new DAOException("Errore generico servizio creaEvento");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::creaEvento] END in " + sec + " milliseconds.");
        }
	}


	@Override
	public AggiornaEventoFruitoreResponse aggiornaEvento(String idEvento, AggiornaEventoFruitoreRequest evento) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::aggiornaEvento] BEGIN");
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);        	
        	String url = "/evento/" + idEvento;
        	ResponseUUID<AggiornaEventoFruitoreResponse> response = putJson(url, AggiornaEventoFruitoreResponse.class, evento);
        	LOG.debug("[" + CLASS_NAME + "::aggiornaEvento] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::aggiornaEvento] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::aggiornaEvento] Errore generico servizio aggiornaEvento", e);
			throw new DAOException("Errore generico servizio aggiornaEvento");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::aggiornaEvento] END in " + sec + " milliseconds.");
        }
	}


	@Override
	public void eliminaEvento(String idEvento, EliminaEventoFruitoreRequest evento) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::eliminaEvento] BEGIN");
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);        	
        	String url = "/evento/" + idEvento;
        	ResponseUUID<AggiornaEventoFruitoreResponse> response = putJson(url, AggiornaEventoFruitoreResponse.class, evento);
        	LOG.debug("[" + CLASS_NAME + "::eliminaEvento] 200 :: " + response.getResponse());
//    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::eliminaEvento] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::eliminaEvento] Errore generico servizio eliminaEvento", e);
			throw new DAOException("Errore generico servizio eliminaEvento");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::eliminaEvento] END in " + sec + " milliseconds.");
        }
	}


	//
	// Processi
	@Override
	public AvviaProcessoFruitoreResponse avviaProcesso(AvviaProcessoFruitoreRequest processo) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::avviaProcesso] BEGIN");
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);        	
        	String url = "/processo";
        	ResponseUUID<AvviaProcessoFruitoreResponse> response = postJson(url, AvviaProcessoFruitoreResponse.class, processo);
        	LOG.debug("[" + CLASS_NAME + "::avviaProcesso] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::avviaProcesso] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::avviaProcesso] Errore generico servizio avviaProcesso", e);
			throw new DAOException("Errore generico servizio avviaProcesso");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::avviaProcesso] END in " + sec + " milliseconds.");
        }
	}
	
	@Override
	public InviaSegnaleFruitoreResponse inviaSegnale(InviaSegnaleFruitoreRequest segnale, String idPratica) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::inviaSegnale] BEGIN");
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);  
        	String url = "/pratiche/"+idPratica+"/segnala";                	
        	ResponseUUID<InviaSegnaleFruitoreResponse> response = postJson(url, InviaSegnaleFruitoreResponse.class, segnale);
        	LOG.debug("[" + CLASS_NAME + "::inviaSegnale] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::inviaSegnale] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::inviaSegnale] Errore generico servizio inviaSegnale", e);
			throw new DAOException("Errore generico servizio inviaSegnale");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::inviaSegnale] END in " + sec + " milliseconds.");
        }
	}



	//
	// Notifiche
	@Override
	public CreaNotificaFruitoreResponse creaNotifica(CreaNotificaFruitoreRequest notifica) throws DAOException {
        long start = System.currentTimeMillis();
        try {
        	LOG.debug("[" + CLASS_NAME + "::creaNotifica] BEGIN");
        	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);        	
        	String url = "/pratiche";
        	ResponseUUID<CreaNotificaFruitoreResponse> response = postJson(url, CreaNotificaFruitoreResponse.class, notifica);
        	LOG.debug("[" + CLASS_NAME + "::creaNotifica] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::creaNotifica] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaNotifica] Errore generico servizio creaNotifica", e);
			throw new DAOException("Errore generico servizio creaNotifica");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::creaNotifica] END in " + sec + " milliseconds.");
        }
	}

	
	//
	// File
	@Override
	public FileUploadResult fileUpload(byte[] bytes, MediaType mediaType, String fileName) throws DAOException {
        long start = System.currentTimeMillis();
    	LOG.debug("[" + CLASS_NAME + "::fileUpload] BEGIN");
    	setMapStatusCodeMessage(COSMO_MAP_STATUS_CODE_MESSAGE);        	
    	String url = "/file/upload";
		MultipartFormDataOutput bodyMultipart = new MultipartFormDataOutput();
        try( InputStream bis = new ByteArrayInputStream(bytes) ) {
			bodyMultipart.addFormData("payload", bis, MediaType.APPLICATION_OCTET_STREAM_TYPE, fileName);
        	ResponseUUID<FileUploadResult> response = postJsonMultipartForm(url, FileUploadResult.class, bodyMultipart);
        	LOG.debug("[" + CLASS_NAME + "::fileUpload] 200 :: " + response.getResponse());
    	    return response.getResponse();
        } catch(DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::fileUpload] DAOException " + daoe.getMessage());
			throw daoe;
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::fileUpload] Errore generico servizio fileUpload", e);
			throw new DAOException("Errore generico servizio fileUpload");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::fileUpload] END in " + sec + " milliseconds.");
        }
    }

}
