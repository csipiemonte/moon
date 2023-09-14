/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.CategoriaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.CategoriaModuloEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

public interface CategoriaDAO {
	
	public CategoriaEntity findById(long idCategoria) throws DAOException;
	public List<CategoriaEntity> find() throws DAOException;
	public List<CategoriaEntity> findByIdModulo(long idModulo) throws DAOException;
	
	public int insertCategoriaModulo(CategoriaModuloEntity categoriaModuloEntity) throws DAOException;
	public int deleteCategoriaModuloByIdModulo(Long idModulo) throws DAOException;

}
