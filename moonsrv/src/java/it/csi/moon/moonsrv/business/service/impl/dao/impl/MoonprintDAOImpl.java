/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.moonprint.Document;
import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.commons.util.JsonHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.MoonprintDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.component.MoonprintTemplateImpl;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

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
	
	private static final String CLASS_NAME = "MoonprintDAOImpl";
	private static final int TIMEOUT = 20000;
	
	@Override
	public String pingMoonprint() throws DAOException {
		return "MOONPRINT: " + getMoonprint("/ping");
	}

	@Override
	public byte[] printPdf(MoonprintDocument moonprintDocument) throws DAOException {
		try {
			if (LOG.isDebugEnabled()) {
				 LOG.debug("[" + CLASS_NAME + "::printPdf] IN template = " + moonprintDocument.getTemplate());
				 LOG.debug("[" + CLASS_NAME + "::printPdf] IN document = " + moonprintDocument.getDocument());
				 try {
				 LOG.debug("[" + CLASS_NAME + "::printPdf] IN  " + new ObjectMapper().writeValueAsString(moonprintDocument));
				 } catch (Exception e) {}
			}
			String URL = getEndpoint() + getPathExtra() + "/pdf"; // TODO moonprintDocument.getTemplate()
			
			ResteasyClient client = new ResteasyClientBuilder()
				.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
				.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
				.establishConnectionTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
				.socketTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
				.build();
//			ResteasyWebTarget webTarget = client.target(URL)
//				.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()))
//				.queryParam("template", moonprintDocument.getTemplate())
//				.queryParam("doc", "{docTemplate}")
//		        .resolveTemplate("docTemplate", doc2string(moonprintDocument.getDocument()));
//		    Invocation.Builder invocationBuilder = webTarget.request();
//		    InputStream inputStream = invocationBuilder.get(InputStream.class);
		    
		    
		    
			client.getConfiguration();  // puo andare NullPointerException resteasy.client.jaxrs.internal.ClientWebTarget
			ResteasyWebTarget webTarget = client.target(URL)
				.register(new BasicAuthentication(getAuthUsr(), getAuthPwd()))
				.queryParam("template", moonprintDocument.getTemplate());
	        Builder builder = webTarget.request().accept("application/pdf");
	        Response response = builder.post(Entity.json(moonprintDocument.getDocument()));
	        InputStream inputStream = response.readEntity(InputStream.class);
		    
		    
//		    invocationBuilder.header("Content-Type", MediaType.TEXT_PLAIN);
//		    invocationBuilder.header("Content-Type", MediaType.APPLICATION_JSON);
//		    invocationBuilder.accept( MediaType.APPLICATION_OCTET_STREAM_TYPE );
		    // invocationBuilder.accept(MediaType.TEXT_PLAIN);
		    //invocationBuilder.accept("application/pdf" );
//		    invocationBuilder.header("clientId", clientId);
            
//		    Response response = invocationBuilder.get(); // richiama moonprint.pdfApi.getPdfMock()
//		    LOG.debug("[" + CLASS_NAME + "::printPdf]  - response " + generateString4MoonPrint(moonprintDocument));
//		    Response response = invocationBuilder.post(Entity.text((generateString4MoonPrint(moonprintDocument)) ) );
//		    Response response = invocationBuilder.post(Entity.json(moonprintDocument.getDocument()));
		    
//	        String contentDisposition = response.getHeaderString("Content-Disposition");
//	        Long contentLength = Long.valueOf(response.getHeaderString("Content-Length"));
//	        LOG.info("[" + CLASS_NAME + "::printPdf] contentDisposition=" + contentDisposition);
//		    LOG.info("[" + CLASS_NAME + "::printPdf] contentLength=" + contentLength);
//		    
//	        InputStream inputStream = response.readEntity(InputStream.class);

//		    Response response = invocationBuilder.get();
//		    LOG.info("[" + CLASS_NAME + "::printPdf] status     =" + response.getStatus());
//		    LOG.info("[" + CLASS_NAME + "::printPdf] statusInfo =" + response.getStatusInfo());
//		    LOG.info("[" + CLASS_NAME + "::printPdf] length     =" + response.getLength());
//		    LOG.info("[" + CLASS_NAME + "::printPdf] mediaType  =" + response.getMediaType());
//		    InputStream inputStream = response.readEntity(InputStream.class);
//            response.close();  // You should close connections!
            
		    byte[] bytes = inputStream.readAllBytes(); // IOUtils.toByteArray(inputStream);
		    if (LOG.isDebugEnabled()) {
		    	LOG.debug("[" + CLASS_NAME + "::printPdf] bytes=" + bytes);
		    }
		    LOG.info("[" + CLASS_NAME + "::printPdf] bytes.length=" + (bytes!=null?bytes.length:"null"));
		    
		    return bytes;
		} catch (IOException ioe) {
			LOG.error("[" + CLASS_NAME + "::printPdf] IOException: ", ioe);
			throw new DAOException("ERRORE IOException in printPdf");
		}
	}

//	private String generateString4MoonPrint(MoonprintDocument document) {
//		String documentJson = doc2string(document.getDocument()); 
//		LOG.debug("[" + CLASS_NAME + "::generateString4MoonPrint] documentJson =" + documentJson);
//		LOG.debug("[" + CLASS_NAME + "::generateString4MoonPrint] template =" + document.getTemplate());
//		return document.getTemplate() + "|" + documentJson;
//	}
	private String doc2string(Document doc) {
		// document in json => json String
		String result = JsonHelper.getJsonFromObject(doc).toString();
	    if (LOG.isDebugEnabled()) {
	    	LOG.debug("[" + CLASS_NAME + "::doc2string] result =" + result);
	    }
		return result;
	}
}
