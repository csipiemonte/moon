/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.moonfobl.util.LoggerAccessor;
import it.csi.moon.moonfobl.business.be.MoonPrintApi;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;


@Component
public class MoonPrintApiImpl implements MoonPrintApi {
	
	private static final String CLASS_NAME = "MoonPrintApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Override
	public Response getPdf(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.info("[" + CLASS_NAME + "::getPdf] BEGIN");
		
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("response.pdf");
			byte[] bytes = is.readAllBytes();
			LOG.info("[" + CLASS_NAME + "::getPdf] bytes = "+bytes);

	        return Response.ok(bytes)
	        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
	        		.header("Pragma", "no-cache")
	        		.header("Expires", "0")
	        		.header(HttpHeaders.CONTENT_TYPE, "application/pdf")
	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=response.pdf")
	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
	        		.build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getPdf] Errore servizio getInitIstanzaByIdModulo",e);
			throw new ServiceException("Errore servizio generatePdf");
		}
	}
	
	@Override
	public Response generatePdf(MoonprintDocument body, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.info("[" + CLASS_NAME + "::generatePdf] IN body = "+body);
		
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("response.pdf");
			byte[] bytes = is.readAllBytes();
			LOG.info("[" + CLASS_NAME + "::getPdf] bytes = "+bytes);
			return Response.ok(bytes)
	        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
	        		.header("Pragma", "no-cache")
	        		.header("Expires", "0")
	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=response.pdf")
	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
	        		.build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::generatePdf] Errore servizio getInitIstanzaByIdModulo",e);
			throw new ServiceException("Errore servizio generatePdf");
		}
	}

	@Override
	public Response generateBase64(MoonprintDocument body, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.info("[" + CLASS_NAME + "::generateBase64] IN body = "+body);
		
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("/response.pdf");
			byte[] bytes = is.readAllBytes();
			ResponseBuilder response = Response.ok(new ByteArrayInputStream(bytes))
	        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
	        		.header("Pragma", "no-cache")
	        		.header("Expires", "0")
	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=response.pdf")
	        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length);
			return response.build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::generatePdf] Errore servizio getInitIstanzaByIdModulo",e);
			throw new ServiceException("Errore servizio generatePdf");
		}
	}
	
}
