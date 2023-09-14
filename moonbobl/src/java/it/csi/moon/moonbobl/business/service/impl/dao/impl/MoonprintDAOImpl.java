/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Invocation;

import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.MoonprintDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.component.MoonprintTemplateImpl;
import it.csi.moon.moonbobl.dto.moonprint.Document;
import it.csi.moon.moonbobl.dto.moonprint.DocumentoAccoglimento;
import it.csi.moon.moonbobl.dto.moonprint.DocumentoDiniego;
import it.csi.moon.moonbobl.dto.moonprint.DocumentoRicevuta;
import it.csi.moon.moonbobl.dto.moonprint.MoonPrintDocumentoAccoglimento;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocument;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoDiniego;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoRicevuta;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.util.JsonHelper;



/**
 * DAO per l'accesso alla componente MOOnPrint per la stampa pdf
 * 
 * @see MoonprintDocument
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class MoonprintDAOImpl extends MoonprintTemplateImpl implements MoonprintDAO {
	
	private final static String CLASS_NAME = "MoonprintDAOImpl";

	@Override
	public String pingMoonprint() throws DAOException {
		return "MOONPRINT: " + getMoonprint("/ping");
	}

	@Override
	public byte[] printPdf(MoonprintDocument moonprintDocument) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				 log.debug("[" + CLASS_NAME + "::printPdf] IN template = " + moonprintDocument.getTemplate());
				 log.debug("[" + CLASS_NAME + "::printPdf] IN document = " + moonprintDocument.getDocument());
			}
			String URL = getEndpoint() + getPathExtra() + "/pdf"; // TODO moonprintDocument.getTemplate()
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget webTarget = client.target(URL)
				.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()))
				.queryParam("template", moonprintDocument.getTemplate())
				.queryParam("doc", "{docTemplate}")
		        .resolveTemplate("docTemplate", doc2string(moonprintDocument.getDocument()));
	        
		    Invocation.Builder invocationBuilder = webTarget.request();
		    InputStream inputStream = invocationBuilder.get(InputStream.class);
		    byte[] bytes = inputStream.readAllBytes(); // IOUtils.toByteArray(inputStream);
		    if (log.isDebugEnabled()) {
		    	log.debug("[" + CLASS_NAME + "::printPdf] bytes=" + bytes);
		    }
		    log.info("[" + CLASS_NAME + "::printPdf] bytes.length=" + (bytes!=null?bytes.length:"null"));
		    
		    return bytes;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new DAOException("ERRORE IOException in printPdf");
		}
	}

//	private String generateString4MoonPrint(MoonprintDocument document) {
//		String documentJson = doc2string(document.getDocument()); 
//		log.debug("[" + CLASS_NAME + "::generateString4MoonPrint] documentJson =" + documentJson);
//		log.debug("[" + CLASS_NAME + "::generateString4MoonPrint] template =" + document.getTemplate());
//		return document.getTemplate() + "|" + documentJson;
//	}
	private String doc2string(Document doc) {
		// document in json => json String
		String result = JsonHelper.getJsonFromObject(doc).toString() ; 
		log.debug("[" + CLASS_NAME + "::doc2string] result =" + result);
		return result;
	}

	
	
	@Override
	public byte[] printPdf(MoonprintDocumentoRicevuta moonprintDocument) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				 log.debug("[" + CLASS_NAME + "::printPdf] IN template = " + moonprintDocument.getTemplate());
				 log.debug("[" + CLASS_NAME + "::printPdf] IN document = " + moonprintDocument.getDocument());
			}
			String URL = getEndpoint() + getPathExtra() + "/pdf"; // TODO moonprintDocument.getTemplate()
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget webTarget = client.target(URL)
				.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()))
				.queryParam("template", moonprintDocument.getTemplate())
				.queryParam("doc", "{docTemplate}")
		        .resolveTemplate("docTemplate", doc2string(moonprintDocument.getDocument()));
	        
		    Invocation.Builder invocationBuilder = webTarget.request();
		    InputStream inputStream = invocationBuilder.get(InputStream.class);
		    byte[] bytes = inputStream.readAllBytes(); // IOUtils.toByteArray(inputStream);
		    if (log.isDebugEnabled()) {
		    	log.debug("[" + CLASS_NAME + "::printPdf] bytes=" + bytes);
		    }
		    log.info("[" + CLASS_NAME + "::printPdf] bytes.length=" + (bytes!=null?bytes.length:"null"));
		    
		    return bytes;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new DAOException("ERRORE IOException in printPdf");
		}
	}
	
	private String doc2string(DocumentoRicevuta doc) {
		// document in json => json String
		String result = JsonHelper.getJsonFromObject(doc).toString() ; 
		log.debug("[" + CLASS_NAME + "::doc2string] result =" + result);
		return result;
	}
	
	@Override
	public byte[] printPdf(MoonprintDocumentoDiniego moonprintDocument) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				 log.debug("[" + CLASS_NAME + "::printPdf] IN template = " + moonprintDocument.getTemplate());
				 log.debug("[" + CLASS_NAME + "::printPdf] IN document = " + moonprintDocument.getDocument());
			}
			String URL = getEndpoint() + getPathExtra() + "/pdf"; // TODO moonprintDocument.getTemplate()
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget webTarget = client.target(URL)
				.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()))
				.queryParam("template", moonprintDocument.getTemplate())
				.queryParam("doc", "{docTemplate}")
		        .resolveTemplate("docTemplate", doc2string(moonprintDocument.getDocument()));
	        
		    Invocation.Builder invocationBuilder = webTarget.request();
		    InputStream inputStream = invocationBuilder.get(InputStream.class);
		    byte[] bytes = inputStream.readAllBytes(); // IOUtils.toByteArray(inputStream);
		    if (log.isDebugEnabled()) {
		    	log.debug("[" + CLASS_NAME + "::printPdf] bytes=" + bytes);
		    }
		    log.info("[" + CLASS_NAME + "::printPdf] bytes.length=" + (bytes!=null?bytes.length:"null"));
		    
		    return bytes;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new DAOException("ERRORE IOException in printPdf");
		}
	}
	private String doc2string(DocumentoDiniego doc) {
		// document in json => json String
		String result = JsonHelper.getJsonFromObject(doc).toString() ; 
		log.debug("[" + CLASS_NAME + "::doc2string] result =" + result);
		return result;
	}
	@Override
	public byte[] printPdf(MoonPrintDocumentoAccoglimento moonprintDocument) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				 log.debug("[" + CLASS_NAME + "::printPdf] IN template = " + moonprintDocument.getTemplate());
				 log.debug("[" + CLASS_NAME + "::printPdf] IN document = " + moonprintDocument.getDocument());
			}
			// TODO moonprintDocument.getTemplate()
			String URL = getEndpoint() + getPathExtra() + "/pdf"; 
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget webTarget = client.target(URL)
				.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()))
				.queryParam("template", moonprintDocument.getTemplate())
				.queryParam("doc", "{docTemplate}")
		        .resolveTemplate("docTemplate", doc2string(moonprintDocument.getDocument()));
	        
		    Invocation.Builder invocationBuilder = webTarget.request();
		    InputStream inputStream = invocationBuilder.get(InputStream.class);
		    byte[] bytes = inputStream.readAllBytes(); // IOUtils.toByteArray(inputStream);
		    if (log.isDebugEnabled()) {
		    	log.debug("[" + CLASS_NAME + "::printPdf] bytes=" + bytes);
		    }
		    log.info("[" + CLASS_NAME + "::printPdf] bytes.length=" + (bytes!=null?bytes.length:"null"));
		    
		    return bytes;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new DAOException("ERRORE IOException in printPdf");
		}
	}
	private String doc2string(DocumentoAccoglimento doc) {
		// document in json => json String
		String result = JsonHelper.getJsonFromObject(doc).toString() ; 
		log.debug("[" + CLASS_NAME + "::doc2string] result =" + result);
		return result;
	}
	
}
