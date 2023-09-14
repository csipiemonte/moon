/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.CategoriaEntity;
import it.csi.moon.commons.entity.CategoriaModuloEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

public interface CategoriaDAO {
	
	public CategoriaEntity findById(long idCategoria) throws DAOException;
	public CategoriaEntity findByNome(String nomeCategoria) throws DAOException;
	public List<CategoriaEntity> find() throws DAOException;
	public List<CategoriaEntity> findByIdModulo(long idModulo) throws DAOException;
	
	public int insertCategoriaModulo(CategoriaModuloEntity categoriaModuloEntity) throws DAOException;
	public int deleteCategoriaByIdModulo(Long idModulo) throws DAOException;

}
