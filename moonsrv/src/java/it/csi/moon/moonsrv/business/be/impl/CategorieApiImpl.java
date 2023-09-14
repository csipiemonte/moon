/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Categoria;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.moonsrv.business.be.CategorieApi;
import it.csi.moon.moonsrv.business.service.CategorieService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class CategorieApiImpl implements CategorieApi {
	
	private static final String CLASS_NAME = "CategorieApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	CategorieService categorieService;
	
	@Override
	public Response getCategorie(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getCategorie] BEGIN");
			List<Categoria> elenco = categorieService.getElencoCategorie();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getCategorie] Errore servizio getCategorie",e);
			throw new ServiceException("Errore servizio elenco categorie");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCategorie] Errore generico servizio getCategorie",ex);
			throw new ServiceException("Errore generico servizio elenco categorie");
		}  
	}
	
	@Override
	public Response getCategoriaById(Integer idCategoria, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Categoria categoria = categorieService.getCategoriaById(idCategoria);
			return Response.ok(categoria).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getCategoriaById] Categoria non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getCategoriaById] Errore servizio getCategoriaById",e);
			throw new ServiceException("Errore servizio get categoria");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getCategoriaById] Errore generico servizio getCategoriaById",ex);
			throw new ServiceException("Errore generico servizio get categoria");
		} 
	}
	
	@Override
	public Response createCategoria(Categoria body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::createCategoria] IN Categoria body: "+body);
			Categoria categoria = categorieService.createCategoria(body);
			return Response.ok(categoria).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::createCategoria] Errore servizio createCategoria",e);
			throw new ServiceException("Errore servizio crea categoria");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createCategoria] Errore generico servizio createCategoria",ex);
			throw new ServiceException("Errore generico servizio crea categoria");
		} 
	}
	
	@Override
	public Response putCategoria(Integer idCategoria, Categoria body, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::putCategoria] IN idCategoria:"+idCategoria+"\nCategoria body: "+body);
			Categoria categoria = categorieService.updateCategoria(idCategoria, body);
			return Response.ok(categoria).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::putCategoria] Errore servizio putCategoria",e);
			throw new ServiceException("Errore servizio aggiorna categoria");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::putCategoria] Errore generico servizio putCategoria",ex);
			throw new ServiceException("Errore generico servizio aggiorna categoria");
		}
	}
	
	
	//
	// Moduli
	//
	@Override
	public Response getModuliByIdCategoria(Integer idCategoria, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getModuliByIdCategoria] BEGIN");
			List<Modulo> elenco = categorieService.getModuliByIdCategoria(idCategoria);
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getModuliByIdCategoria] Errore servizio getModuliByIdCategoria",e);
			throw new ServiceException("Errore servizio elenco moduli per idCategoria");
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getModuliByIdCategoria] Errore generico servizio getModuliByIdCategoria",ex);
			throw new ServiceException("Errore generico servizio elenco moduli per idCategoria");
		} 
	}
	
}
