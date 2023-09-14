/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.security.Principal;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.ReportVerificaFirma;
import it.csi.moon.commons.util.decodifica.DecodificaFormatoFirma;
import it.csi.moon.moonsrv.business.be.FirmaDigitaleApi;
import it.csi.moon.moonsrv.business.service.FirmaDigitaleService;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class FirmaDigitaleApiImpl implements FirmaDigitaleApi {

	private static final String CLASS_NAME = "FirmaDigitaleAPIImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	FirmaDigitaleService firmaDigitaleService;

	@Override
	public Response testResouces(SecurityContext securityContext) {
		LOG.debug("[" + CLASS_NAME + "::testResouces] BEGIN END");
		Principal principal = null;
		if (securityContext != null) {
			principal = securityContext.getUserPrincipal();
			LOG.debug("[" + CLASS_NAME + "::ping] init principal=" + principal);
		}
		String response = firmaDigitaleService.testResources() ? "OK" : "KO";
		return Response.ok(response).build();

	}

	@Override
	public Response verificaFirmaByIdAllegato(Long idAllegato) {
		try {
			LOG.debug("[" + CLASS_NAME + "::verificaFirmaByIdAllegato] BEGIN");
			ReportVerificaFirma response = firmaDigitaleService.checkDocumentSignatureByIdAllegato(idAllegato);
			return Response.ok(response).build();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::verificaFirmaByIdAllegato] END");
		}
	}

	@Override
	public Response verificaFirmaByIdFile(Long idFile) {
		try {
			LOG.debug("[" + CLASS_NAME + "::verificaFirmaByIdFile] BEGIN");
			ReportVerificaFirma response = firmaDigitaleService.checkDocumentSignatureByIdFile(idFile);
			return Response.ok(response).build();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::verificaFirmaByIdFile] END");
		}
	}

	@Override
	public Response verificaFirmaByContenuto(MultipartFormDataInput file) throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::verificaFirmaByContenuto] BEGIN");
			ReportVerificaFirma response = firmaDigitaleService.checkDocumentSignatureByFile(file);
			return Response.ok(response).build();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::verificaFirmaByContenuto] END");
		}
	}

	@Override
	public boolean checkFirmaPadesByIdAllegato(Long idAllegato) {
		LOG.debug("[" + CLASS_NAME + "::checkFirmaPadesByIdAllegato] BEGIN END");
		ReportVerificaFirma response = firmaDigitaleService.checkDocumentSignatureByIdAllegato(idAllegato);
		return response.isFirmato() && (response.getFormatoFirma() == DecodificaFormatoFirma.FIRMA_DIGITALE_PADES);
	}

	@Override
	public boolean checkFirmaPadesByIdFile(Long idFile) {
		LOG.debug("[" + CLASS_NAME + "::checkFirmaPadesByIdFile] BEGIN END");
		ReportVerificaFirma response = firmaDigitaleService.checkDocumentSignatureByIdFile(idFile);
		return response.isFirmato() && (response.getFormatoFirma() == DecodificaFormatoFirma.FIRMA_DIGITALE_PADES);
	}

	@Override
	public boolean checkFirmaPades(MultipartFormDataInput file) {
		ReportVerificaFirma response = firmaDigitaleService.checkDocumentSignatureByFile(file);
		return response.isFirmato() && (response.getFormatoFirma() == DecodificaFormatoFirma.FIRMA_DIGITALE_PADES);
	}

	// Tika
	@Override
	public Response retrieveContentType(MultipartFormDataInput file) {
		String result = firmaDigitaleService.retrieveContentType(file);
		return Response.ok(result).build();
	}
	
	
}
