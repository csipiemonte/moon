/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.ModuloExported;
import it.csi.moon.moonsrv.business.be.ModuliExpApi;
import it.csi.moon.moonsrv.business.service.ModuliExportService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ModuliExpApiImpl  implements ModuliExpApi {
	
	private static final String CLASS_NAME = "ModuliExpApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	ModuliExportService moduliExportService;
	
	@Override
	public Response expModuloByCd(String codicedModulo, String versione, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			ModuloExported modulo = moduliExportService.exportModuloByCd(codicedModulo, versione);
			return Response.ok(modulo).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::expModuloByCd] modulo non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::expModuloByCd] Errore servizio expModuloByCd", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::expModuloByCd] Errore generico servizio expModuloByCd",ex);
			throw new ServiceException("Errore generico servizio expModuloByCd");
		}
	}

}
