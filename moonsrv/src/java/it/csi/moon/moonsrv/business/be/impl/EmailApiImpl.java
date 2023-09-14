/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.moonsrv.business.be.EmailApi;
import it.csi.moon.moonsrv.business.service.mail.EmailService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class EmailApiImpl extends MoonBaseApiImpl implements EmailApi {
	
	private static final String CLASS_NAME = "EmailApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	EmailService emailService;
	
    @Override
    public Response inviaMessaggio( EmailRequest body, 
        	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
    	try {
			LOG.debug("[" + CLASS_NAME + "::inviaMessaggio] BEGIN IN\n"+body);
			
			emailService.sendMail(body);
			return Response.ok().build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggio] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggio] Exception ", ex);
			throw new ServiceException("Errore generico inviaMessaggio");
		}
	}
	
    public Response inviaMessaggioWithAttach( EmailRequest body, 
        	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) {
    	try {
			LOG.debug("[" + CLASS_NAME + "::inviaMessaggioWithAttach] BEGIN IN\n"+body);
			
			emailService.sendMailWithAttach(body);
			return Response.ok().build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggio] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggio] Exception ", ex);
			throw new ServiceException("Errore generico inviaMessaggio");
		}
	}
    
    @Override
    public Response inviaMessaggioHtml( EmailRequest body, 
        	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
    	try {
			LOG.debug("[" + CLASS_NAME + "::inviaMessaggioHtml] BEGIN IN\n"+body);
			
			emailService.sendMailHtmlAndText(body);
			return Response.ok().build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggioHtml] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggioHtml] Exception ", ex);
			throw new ServiceException("Errore generico inviaMessaggioHtml");
		}
	}
	
    @Override
    public Response inviaMessaggioHtmlWithAttach( EmailRequest body, 
        	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
    	try {
			LOG.debug("[" + CLASS_NAME + "::inviaMessaggioHtmlWithAttach] BEGIN IN\n"+body);
			
			emailService.sendMailHtmlAndTextWithAttach(body);
			return Response.ok().build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggioHtmlWithAttach] BusinessException ", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::inviaMessaggioHtmlWithAttach] Exception ", ex);
			throw new ServiceException("Errore generico inviaMessaggioHtmlWithAttach");
		}
	}
}
