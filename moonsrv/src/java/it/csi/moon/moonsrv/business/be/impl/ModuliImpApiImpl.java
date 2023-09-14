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
import it.csi.moon.commons.dto.ModuloImportParams;
import it.csi.moon.commons.dto.ModuloImportParams.EnumModuloImportModalita;
import it.csi.moon.moonsrv.business.be.ModuliImpApi;
import it.csi.moon.moonsrv.business.service.ModuliImportService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class ModuliImpApiImpl extends MoonBaseApiImpl implements ModuliImpApi {
	
	private static final String CLASS_NAME = "ModuliImpApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	ModuliImportService moduliImportService;
	
	@Override
	public Response impModulo(String modalita, 
    	String codiceModuloTarget, 
    	ModuloExported modulo,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			modalita = validaStringCodeRequired(modalita);
			//
			ModuloImportParams params = new ModuloImportParams();
			params.setModalita(EnumModuloImportModalita.byCodice(modalita));
			params.setCodiceModuloTarget(codiceModuloTarget);
			String result = moduliImportService.importModulo(modulo, params);
			return Response.ok(result).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::impModulo] modulo non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::impModulo] Errore servizio expModuloByCd",e);
			throw new ServiceException("Errore servizio get modulo");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::impModulo] Errore generico servizio expModuloByCd",ex);
			throw new ServiceException("Errore generico servizio expModuloByCd");
		}
	}

}
