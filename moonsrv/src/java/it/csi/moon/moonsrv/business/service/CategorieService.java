/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Categoria;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alle categorie
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface CategorieService {
	List<Categoria> getElencoCategorie() throws BusinessException;
	Categoria getCategoriaById(Integer idCategoria) throws ItemNotFoundBusinessException, BusinessException;
	Categoria createCategoria(Categoria body) throws BusinessException;
	Categoria updateCategoria(Integer idCategoria, Categoria body) throws BusinessException;
	
	//
	// Moduli
	//
	List<Modulo> getModuliByIdCategoria(Integer idCategoria) throws BusinessException;
}
