/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonprint.business.be.PdfApi;
import it.csi.moon.moonprint.business.service.PdfService;
import it.csi.moon.moonprint.exceptions.service.ServiceException;
import it.csi.moon.moonprint.util.LoggerAccessor;


@Component
public class PdfApiImpl implements PdfApi {
	
	private final static String CLASS_NAME = "PdfApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	PdfService pdfService;
	
	@Override
	public Response getPdfMock(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME +  "::"+"getPdf] BEGIN");
		}
		try {
			byte[] bytes = pdfService.getPdfMock();
			// System.out.println("[" + CLASS_NAME + "::getPdf] bytes = "+bytes);
			
	        return Response.ok(bytes).build();
//	        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
//	        		.header("Pragma", "no-cache")
//	        		.header("Expires", "0")
//	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=response.pdf")
//	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
//	        		.build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getPdf] - Errore servizio getInitIstanzaByIdModulo",e);
			throw new ServiceException("Errore servizio generatePdf");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME +  "::"+"getPdf] END");
			}
		}
	}
	
	
	@Override
	public Response generatePdf(String body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME +  "::"+"generatePdf] BEGIN");
			LOG.debug("[" + CLASS_NAME +  "::"+"generatePdf] IN body = " + body);
		}
		try {
			byte[] bytes = pdfService.getPdf(body);
			// System.out.println("[" + CLASS_NAME + "::generatePdf] bytes = "+bytes);

			return Response.ok(bytes)
	        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
	        		.header("Pragma", "no-cache")
	        		.header("Expires", "0")
	        		//.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+getFileName(body))
	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
	        		.build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::generatePdf] - Errore servizio generatePdf",e);
			throw new ServiceException("Errore servizio generatePdf");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME +  "::"+"generatePdf] END");
			}
		}
	}

//	@Override
//	public Response generatePdf( String body, String template, 
//		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//		if (log.isDebugEnabled()) {
//			log.debug("[" + CLASS_NAME +  "::"+"generatePdf] BEGIN");
//			log.debug("[" + CLASS_NAME +  "::"+"generatePdf] IN template = " + template);
//			log.debug("[" + CLASS_NAME +  "::"+"generatePdf] IN body = " + body);
//		}
//		try {
//			byte[] bytes = pdfService.getPdf(template, body);
//			System.out.println("[" + CLASS_NAME + "::generatePdf] bytes = "+bytes);
//
//			return Response.ok(bytes)
//	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
//	        		.build();
//		} catch (Exception e) {
//			log.error("[" + CLASS_NAME + "::generatePdf] - Errore servizio generatePdf",e);
//			throw new ServiceException("Errore servizio generatePdf");
//		} finally {
//			if (log.isDebugEnabled()) {
//				log.debug("[" + CLASS_NAME +  "::"+"generatePdf] END");
//			}
//		}
//	}
	
	@Override
	public Response getGeneratePdf( String template, String doc, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME +  "::"+"getGeneratePdf] BEGIN");
			LOG.debug("[" + CLASS_NAME +  "::"+"getGeneratePdf] IN template = " + template);
			LOG.debug("[" + CLASS_NAME +  "::"+"getGeneratePdf] IN body = " + doc);
		}
		try {
			byte[] bytes = pdfService.getPdf(template, doc);
			// System.out.println("[" + CLASS_NAME + "::getGeneratePdf] bytes = "+bytes);

			return Response.ok(bytes)
	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
	        		.build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getGeneratePdf] - Errore servizio generatePdf",e);
			throw new ServiceException("Errore servizio getGeneratePdf");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME +  "::"+"getGeneratePdf] END");
			}
		}
	}
	
	public Response postGeneratePdf( String template, String body,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME +  "::"+"postGeneratePdf] BEGIN");
			LOG.debug("[" + CLASS_NAME +  "::"+"postGeneratePdf] IN template = " + template);
			LOG.debug("[" + CLASS_NAME +  "::"+"postGeneratePdf] IN body = " + body);
		}
		try {
			byte[] bytes = pdfService.getPdf(template, body);
			// System.out.println("[" + CLASS_NAME + "::getGeneratePdf] bytes = "+bytes);

			return Response.ok(bytes)
	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
	        		.build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getGeneratePdf] - Errore servizio generatePdf",e);
			throw new ServiceException("Errore servizio getGeneratePdf");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME +  "::"+"getGeneratePdf] END");
			}
		}
	}
	
}
