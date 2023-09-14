/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.extra.dirittostudio.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.istat.Comune;
import it.csi.moon.moonfobl.business.be.extra.dirittostudio.ScuoleApi;
import it.csi.moon.moonfobl.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dirittostudio.ScuoleDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dto.ScuolaEntity;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class ScuoleApiImpl extends MoonBaseApiImpl implements ScuoleApi {

	private static final String CLASS_NAME = "ScuoleApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ScuoleDAO scuoleDAO;

	@Override
	public Response getScuole(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			List<ScuolaEntity> ris = scuoleDAO.find();
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getScuole] Errore UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getScuole] Errore generico servizio getScuole", ex);
			throw new ServiceException("Errore generico servizio elenco getScuole");
		}
	}

	@Override
	public Response getScuoleCodiceIstatOrdineScuola(String codiceIstat, Integer idOrdineScuola,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws ServiceException {
		try {			
			codiceIstat = this.validaCodiceIstatComuneRequired(codiceIstat);			
			List<ScuolaEntity> ris = scuoleDAO.findByCodiceIstatIdOrdine(codiceIstat, idOrdineScuola);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getScuoleCodiceIstatOrdineScuola] Errore UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getScuole] Errore generico servizio getScuole", ex);
			throw new ServiceException("Errore getScuoleCodiceIstatOrdineScuola servizio elenco getScuole");
		}
	}

	@Override
	public Response getComuniCodiceIstatProvincia(String codiceIstat, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {			
			codiceIstat = this.validaCodiceIstatProvinciaRequired(codiceIstat);			
			List<Comune> ris = scuoleDAO.findByCodiceIstatProvincia(codiceIstat);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getScuoleCodiceIstatOrdineScuola] Errore UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getScuole] Errore generico servizio getScuole", ex);
			throw new ServiceException("Errore getScuoleCodiceIstatOrdineScuola servizio elenco getScuole");
		}
	}

	@Override
	public Response getScuoleV2(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws ServiceException {
		try {
			List<ScuolaEntity> ris = scuoleDAO.findV2();
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getScuole] Errore UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getScuole] Errore generico servizio getScuoleV2", ex);
			throw new ServiceException("Errore generico servizio elenco getScuoleV2");
		}
	}


	@Override
	public Response getScuoleV2ComuniCodiceIstatProvincia(String codiceIstat, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getScuoleV2CodiceIstatOrdineScuolaAnno(String codiceIstat, Integer idOrdineScuola,
			String annoScolastico, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws ServiceException {
				
		codiceIstat = this.validaCodiceIstatComuneRequired(codiceIstat);
		
		try {
			List<ScuolaEntity> ris = scuoleDAO.findByCodiceIstatIdOrdineAnnoScolasticoV2(codiceIstat, idOrdineScuola,annoScolastico);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getScuoleV2CodiceIstatOrdineScuolaAnno] Errore UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getScuole] Errore generico servizio getScuole", ex);
			throw new ServiceException("Errore getScuoleV2CodiceIstatOrdineScuolaAnno servizio elenco getScuole");
		}
	}

	@Override
	public Response getScuoleV2CodiceIstatInOrdineScuolaAnno(String codiceIstat, Integer idOrdineScuola,
			String annoScolastico, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws ServiceException {
		
		try {
			
			List<String> codiciIstat = this.validaCodiciIstatComuneRequired(codiceIstat);
			
			List<ScuolaEntity> ris = scuoleDAO.findByCodiceIstatInIdOrdineAnnoScolasticoV2(codiciIstat, idOrdineScuola,annoScolastico);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getScuoleV2CodiceIstatInOrdineScuolaAnno] Errore UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getScuole] Errore generico servizio getScuole", ex);
			throw new ServiceException("Errore getScuoleV2CodiceIstatInOrdineScuolaAnno servizio elenco getScuole");
		}
	}

	@Override
	public Response getScuoleV2CodiceIstatNotInOrdineScuolaAnno(String codiceIstat, Integer idOrdineScuola,
			String annoScolastico, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws ServiceException {

		try {
						
		    List<String> codiciIstat = this.validaCodiciIstatComuneRequired(codiceIstat);
			
			List<ScuolaEntity> ris = scuoleDAO.findByCodiceIstatNotInIdOrdineAnnoScolasticoV2(codiciIstat, idOrdineScuola,annoScolastico);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getScuoleV2CodiceIstatNotInOrdineScuolaAnno] Errore UnprocessableEntityException");
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getScuole] Errore generico servizio getScuole", ex);
			throw new ServiceException("Errore getScuoleV2CodiceIstatNotInOrdineScuolaAnno servizio elenco getScuole");
		}
	}




	

}
