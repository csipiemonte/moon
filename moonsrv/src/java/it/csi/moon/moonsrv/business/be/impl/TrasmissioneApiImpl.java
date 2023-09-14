/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.be.TrasmissioneApi;
import it.csi.moon.moonsrv.business.service.TrasmettiService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * API per trasmissione buoni taxi
 * 
 * @author Danilo
 *
 */

@Component
public class TrasmissioneApiImpl extends MoonBaseApiImpl implements TrasmissioneApi {

	private static final String CLASS_NAME = "TrasmissioneApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	TrasmettiService trasmettiService;

	@Override
	public Response trasmettiBuoniTaxi(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		
		Integer result = null;
		
		try {
			result = trasmettiService.trasmettiBuoniTaxi();
							
		} catch (BusinessException ex) {
			LOG.error("[" + CLASS_NAME + "::trasmettiBuoniTaxi] Errore servizio ", ex);
			throw new ServiceException("Errore servizio ", ex);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::trasmettiBuoniTaxi] Errore generico servizio ", ex);
			throw new ServiceException("Errore servizio ", ex);

		}
		return Response.ok(result).build();	

	}

	@Override
	public Response downloadBuoniTaxi(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		
		
		byte[] bytes = trasmettiService.getRowsBuoniTaxi();
		
		Calendar dataOdierna = Calendar.getInstance();
		DateFormat dF = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String timestampForFileName = dF.format(dataOdierna.getTime());
		String fileName = "codici_buoni_taxi" + "_" +timestampForFileName+".csv";	
	
		return Response.ok(bytes)
    		.header("Cache-Control", "no-cache, no-store, must-revalidate")
    		.header("Pragma", "no-cache")
    		.header("Expires", "0")
    		.header(HttpHeaders.CONTENT_TYPE, "text/csv")
    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;"+fileName)
    		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
    		.build();
	}
}
