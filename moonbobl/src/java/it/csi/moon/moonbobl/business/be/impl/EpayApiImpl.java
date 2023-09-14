/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.EpayApi;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.dto.moonfobl.VerificaPagamento;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class EpayApiImpl implements EpayApi {
	
	private final static String CLASS_NAME = "EpayApiImpl";
	private Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	@Override
	public Response getVerificaPagamento( String idEpay,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			VerificaPagamento response = moonsrvDAO.getVerificaPagamento(idEpay);
			return Response.ok(response).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getVerificaPagamento] Errore servizio getVerificaPagamento", e);
			throw new ServiceException("Errore servizio getVerificaPagamento");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getVerificaPagamento] Errore generico servizio getVerificaPagamento", ex);
			throw new ServiceException("Errore generico servizio getVerificaPagamento");
		}
	}
	
}
