/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.be.ReportsApi;
import it.csi.moon.moonsrv.business.service.ReportService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.exceptions.service.UnauthorizedException;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ReportsApiImpl extends MoonBaseApiImpl implements ReportsApi {

	private static final String CLASS_NAME = "ReportsApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ReportService reportService;

	@Override
	public Response getReport(String codiceModulo, String codiceEstrazione, Date dataDa, Date dataA,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getReport] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::getReport] IN codiceModulo: " + codiceModulo);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN codiceEstrazione: " + codiceEstrazione);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN dataDa: " + dataDa);
				LOG.debug("[" + CLASS_NAME + "::getReport] IN dsataA: " + dataA);
			}
			// validazione dati
			codiceModulo = validaStringCodeRequired(codiceModulo);		
			codiceEstrazione = validaStringCodeRequired(codiceEstrazione);		
			
			StreamingOutput stream = reportService.getStreamReportByCodiceModuloCodiceReport(codiceModulo, codiceEstrazione, dataDa, dataA);
			
	        return Response.ok(stream)
	        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
	        		.header("Pragma", "no-cache")
	        		.header("Expires", "0")
	        		.header(HttpHeaders.CONTENT_TYPE, "text/csv")
	        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+codiceModulo+".csv")
	        		.build();			

		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getReport] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (UnauthorizedBusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::getReport] Errore di sicurezza getReport", ex);
			throw new UnauthorizedException();			
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getReport] Errore servizio getReport", be);
			throw new ServiceException(be);	
		} catch (UnprocessableEntityException e) {
			LOG.error("[" + CLASS_NAME + "::getReport] Errore servizio getReport");
			throw e;			
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getReport] Errore generico servizio getReport", ex);
			throw new ServiceException("Errore generico servizio getReport");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getReport] END");
			}
		}
	}


	
}
