/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Categoria;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.entity.CategoriaEntity;
import it.csi.moon.commons.mapper.CategoriaMapper;
import it.csi.moon.moonsrv.business.service.CategorieService;
import it.csi.moon.moonsrv.business.service.impl.dao.CategoriaDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
/**
 * Metodi di business relativi alle categorie
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class CategorieServiceImpl  implements CategorieService {
	
	private static final String CLASS_NAME = "CategorieServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	CategoriaDAO categoriaDAO;
	
	@Override
	public List<Categoria> getElencoCategorie() throws BusinessException {
		try {
			List<Categoria> result = categoriaDAO.find().stream()
					.map(CategoriaMapper::buildFromCategoriaEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoCategorie] Errore generico servizio getElencoCategorie",ex);
			throw new ServiceException("Errore generico servizio elenco categorie");
		} 
	}

	@Override
	public Categoria getCategoriaById(Integer idCategoria) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getCategoriaById] BEGIN IN idCategoria="+idCategoria);
		}
		Categoria result = null;
		try {
			CategoriaEntity entity = categoriaDAO.findById(idCategoria);
			result = CategoriaMapper.buildFromCategoriaEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getCategoriaById] Errore invocazione DAO");
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getCategoriaById] Errore invocazione DAO - ", e);
			throw new BusinessException(e);
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getCategoriaById] END result="+result);
			}
		}
	}

	@Override
	public Categoria createCategoria(Categoria body) throws BusinessException {
		return null;
	}

	@Override
	public Categoria updateCategoria(Integer idCategoria, Categoria body) throws BusinessException {
		return null;
	}

	
	//
	// Moduli
	//
	@Override
	public List<Modulo> getModuliByIdCategoria(Integer idCategoria) throws BusinessException {
		return null;
	}


}