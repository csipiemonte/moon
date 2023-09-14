/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Stato;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.moonfobl.business.be.StatiApi;
import it.csi.moon.moonfobl.business.service.StatiService;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class StatiApiImpl extends MoonBaseApiImpl implements StatiApi {

	private static final String CLASS_NAME = "IstanzeApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	StatiService statiService;
	
	@Override
	public Response getStati(String conPresenzaIstanzeQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		try {

			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getIstanze] BEGIN");
			}
			
			Boolean conPresenzaIstanze = validaBoolean(conPresenzaIstanzeQP);
			
			
			UserInfo user = retrieveUserInfo(httpRequest);
			String nomePortale = retrievePortalName(httpRequest);

			// Filter
//			IstanzeFilter filter = new IstanzeFilter();
			ModuliFilter filter = new ModuliFilter();
			filter.setConPresenzaIstanzeUser(Boolean.TRUE.equals(conPresenzaIstanze)?user.getIdentificativoUtente():null);
			filter.setNomePortale((nomePortale.equals("localhost")) ? "*" : nomePortale);
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
			filter.setIdAmbito(user.getIdAmbito());
			filter.setIdVisibilitaAmbito(filter.getIdAmbito().isPresent()?null:ModuliFilter.VISIBILITA_AMBITO_PUBLIC);
			
			List<Stato> elenco = statiService.getElencoStati(filter);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			LOG.warn("[" + CLASS_NAME + "::getIstanze] Errore UnprocessableEntityException", uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio getIstanze", ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		}
		
		
		
	}

}
