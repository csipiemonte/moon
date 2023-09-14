/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.impl;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.apimint.mydocs.be.v1.dto.AmbitoResponse;
import it.csi.apimint.mydocs.be.v1.dto.DocumentiResponse;
import it.csi.apimint.mydocs.be.v1.dto.Documento;
import it.csi.apimint.mydocs.be.v1.dto.FiltroDocumento;
import it.csi.apimint.mydocs.be.v1.dto.Tipologia;
import it.csi.apimint.mydocs.be.v1.dto.TipologiaResponse;
import it.csi.moon.commons.dto.Ambito;
import it.csi.moon.moonsrv.business.service.impl.dao.component.ApimintTemplateImpl;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.MyDocsDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.dto.ApimintResponseAmbiti;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.dto.ApimintResponseAmbito;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.dto.ApimintResponseTipologia;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.dto.ApimintResponseTipologie;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.dto.ApimintResponseUuid;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.ResponseUUID;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

@Qualifier("apimint")
@Component
public class MyDocsApimintDAOImpl extends ApimintTemplateImpl implements MyDocsDAO  {

	private static final String CLASS_NAME = "MyDocsApimintDAOImpl";
	private static final String CONSUMER = "moon";
    
    public MyDocsApimintDAOImpl() {
    	super();
    	setFAIL_ON_UNKNOWN_PROPERTIES(false); // per il YAML di merda che hanno pubblicato di questi servizi
    }
    
	protected String getPathExtra() {
		return "/mydocsbeApplApiRest/v1";
	}
	
	protected MultivaluedMap<String, Object> getHeadersExtra() throws DAOException {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
		headers.add("Content-Type", "application/json");
		headers.add("Consumer", CONSUMER);
		headers.add("Ente", getEnte());
		return headers;
	}

	private String ente;
	public String getEnte() {
		return ente;
	}
	public void setEnte(String ente) {
		this.ente = ente;
	}

	//
	// PING return 200
	// Non necessita headers: ente, consumer
	@Override
	public String ping() throws DAOException {
		final String methodName = "ping";
        long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::"+ methodName + "] BEGIN");
			int statusCode = getJsonStatus("/ping");
    	    return Integer.toString(statusCode);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName + "] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::" + methodName + "] END in " + sec + " milliseconds.");
        }
	}

	//
	// AMBITI
	//
	@Override
	public List<AmbitoResponse> listAmbiti(String ente) throws DAOException {
		assert ente!=null : "ente non presente.";
		final String methodName = "listAmbiti";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::"+ methodName +"] BEGIN");
			setEnte(ente);
			ResponseUUID<ApimintResponseAmbiti> response = getJson("/ambiti", ApimintResponseAmbiti.class);
    	    return response.getResponse().getAmbiti();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start);
	            LOG.info("[" + CLASS_NAME + "::" + methodName + "] END in " + sec + " milliseconds.");
			}
		}
	}

	@Override
	public AmbitoResponse getAmbitoById(String ente, Long idAmbito) throws DAOException {
		assert ente!=null : "ente non presente.";
		final String methodName = "getAmbitoById";
        long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::"+ methodName +"] BEGIN");
			setEnte(ente);
			ResponseUUID<ApimintResponseAmbito> response = getJson("/ambiti/" + idAmbito, ApimintResponseAmbito.class);
    	    return response.getResponse().getAmbito();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::" + methodName + "] END in " + sec + " milliseconds.");
        }
	}
	
	@Override
	public AmbitoResponse insertAmbito(String ente, Ambito ambito) throws DAOException {
		assert ente!=null : "ente non presente.";
		final String methodName = "insertAmbito";
        long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::"+ methodName +"] BEGIN");
			setEnte(ente);
			ResponseUUID<ApimintResponseAmbito> response = postJson("/ambiti", ApimintResponseAmbito.class, ambito);
    	    return response.getResponse().getAmbito();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::" + methodName + "] END in " + sec + " milliseconds.");
        }	
	}
	
	
	//
	// TIPOLOGIE
	//
	@Override
	public List<TipologiaResponse> listTipologie(String ente) throws DAOException {
		assert ente!=null : "ente non presente.";
		final String methodName = "listTipologie";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::"+ methodName +"] BEGIN");
			setEnte(ente);
			ResponseUUID<ApimintResponseTipologie> response = getJson("/tipologie", ApimintResponseTipologie.class);
    	    return response.getResponse().getTipologie();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start);
	            LOG.info("[" + CLASS_NAME + "::" + methodName + "] END in " + sec + " milliseconds.");
			}
		}
	}

	@Override
	public TipologiaResponse getTipologiaById(String ente, Long idTipologia) throws DAOException {
		assert ente!=null : "ente non presente.";
		final String methodName = "getTipologiaById";
        long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::"+ methodName +"] BEGIN");
			setEnte(ente);
			ResponseUUID<ApimintResponseTipologia> response = getJson("/tipologie/" + idTipologia, ApimintResponseTipologia.class);
    	    return response.getResponse().getTipologia();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::" + methodName + "] END in " + sec + " milliseconds.");
        }
	}
	
	@Override
	public TipologiaResponse insertTipologia(String ente, Tipologia tipologia) throws DAOException {
		assert ente!=null : "ente non presente.";
		final String methodName = "insertTipologia";
        long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::"+ methodName +"] BEGIN");
			setEnte(ente);
			ResponseUUID<ApimintResponseTipologia> response = postJson("/tipologie", ApimintResponseTipologia.class, tipologia);
    	    return response.getResponse().getTipologia();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::" + methodName + "] END in " + sec + " milliseconds.");
        }	
	}
	
	
	//
	// DOCUMENTI
	//
	public DocumentiResponse findDocumenti(String ente, FiltroDocumento filtro) throws DAOException {
		assert ente!=null : "ente non presente.";
		final String methodName = "findDocumenti";
        long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::"+ methodName +"] BEGIN");
			setEnte(ente);
			ResponseUUID<DocumentiResponse> response = postJson("/documenti/paginati/list", DocumentiResponse.class, filtro);
    	    return response.getResponse();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore generico servizio " + methodName, e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::" + methodName + "] END in " + sec + " milliseconds.");
        }
	}
	public String insertDocumento(String ente, Documento documento) throws DAOException {
		assert ente!=null : "ente non presente.";
		final String methodName = "insertDocumento";
        long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::"+ methodName +"] BEGIN");
			setEnte(ente);
			ResponseUUID<ApimintResponseUuid> response = postJson("/documenti", ApimintResponseUuid.class, documento);
    	    return response.getResponse().getUuid();
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + methodName +"] Errore with ente: " + ente + "\ndocumento: " + documento + " headersExtra:" + map2String(getHeadersExtra()), e);
			throw new DAOException("Errore generico servizio " + methodName);
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start);
            LOG.info("[" + CLASS_NAME + "::" + methodName + "] END in " + sec + " milliseconds.");
        }	
	}


	
}
