/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.extra.edilizia.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.edilizia.PraticaEdilizia;
import it.csi.moon.moonfobl.business.be.extra.edilizia.PraticheApi;
import it.csi.moon.moonfobl.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dirittostudio.PraticheDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dto.PraticaEdiliziaEntity;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class PraticheApiImpl extends MoonBaseApiImpl implements PraticheApi {

	private static final String CLASS_NAME = "PraticheApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	PraticheDAO praticheDAO;
	
	@Autowired
	MoonsrvDAO moonsrvDAO;

	@Override
	public Response getProgressivi(String anno, Integer numRegistro,
			SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			
			List<String> ris = praticheDAO.findProgressiviPerRegistroAnno(anno, numRegistro);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getProgressivi] Errore UnprocessableEntityException" + 
					" - anno="+anno+ " - registro="+numRegistro);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getProgressivi] Errore generico servizio getProgressivi"+ 
					" - anno="+anno+ " - registro="+numRegistro, ex);
			throw new ServiceException("Errore generico servizio elenco progressivi");
		}
	}

	@Override
	public Response getPratica(String anno, Integer numRegistro, String progressivo,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ServiceException {
		try {		
			PraticaEdiliziaEntity ris = praticheDAO.findPraticaByAnnoRegProg(anno, numRegistro, progressivo);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getPratica] Errore UnprocessableEntityException" + 
						" - anno="+anno+ " - registro="+numRegistro+ " - progressivo="+progressivo);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getPratica] Errore generico servizio getPratica"+ 
						" - anno="+anno+ " - registro="+numRegistro+ " - progressivo="+progressivo, ex );
			throw new ServiceException("Errore getPratica");
		}
	}

	@Override
	public Response getElencoPratiche(String anno, Integer numRegistro, String progressivo,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ServiceException {
		try {		
			List<PraticaEdiliziaEntity> ris = praticheDAO.findByAnnoRegProg(anno, numRegistro, progressivo);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getPratica] Errore UnprocessableEntityException" + 
						" - anno="+anno+ " - registro="+numRegistro+ " - progressivo="+progressivo);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getPratica] Errore generico servizio getPratica"+ 
						" - anno="+anno+ " - registro="+numRegistro+ " - progressivo="+progressivo, ex );
			throw new ServiceException("Errore getPratica");
		}
	}	
	
	@Override
	public Response getElencoPraticheV2(String anno, Integer numRegistro, String progressivo,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ServiceException {
		try {		
			List<PraticaEdilizia> ris = moonsrvDAO.getElencoPratiche(anno, numRegistro.toString(), progressivo);
			
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getElencoPraticheV2] Errore UnprocessableEntityException" + 
						" - anno="+anno+ " - registro="+numRegistro+ " - progressivo="+progressivo);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoPraticheV2] Errore generico servizio getElencoPraticheV2"+ 
						" - anno="+anno+ " - registro="+numRegistro+ " - progressivo="+progressivo, ex );
			throw new ServiceException("Errore getPratica");
		}
	}

}
