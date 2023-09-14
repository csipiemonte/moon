/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.CategorieApi;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.dto.moonfobl.Categoria;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class CategorieApiImpl implements CategorieApi {
	
	private final static String CLASS_NAME = "CategorieApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	@Override
	public Response getCategorie(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			
			List<Categoria> categorie = moonsrvDAO.getCategorie();
			
			 return Response.ok(categorie).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getCategorie] struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getCategorie] Errore servizio getCategorie",e);
			throw new ServiceException("Errore servizio getCampiModulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCategorie] Errore generico servizio getCategorie",ex);
			throw new ServiceException("Errore generico servizio getCategorie");
		} 
	
	}
	
}
