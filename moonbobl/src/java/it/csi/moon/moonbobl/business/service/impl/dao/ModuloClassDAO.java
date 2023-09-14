/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.ModuloClassEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;



public interface ModuloClassDAO {

	//public ModuloClassEntity findClassbyIdModuloTipologia(Long idModulo, int tipologia) throws DAOException;
	public ModuloClassEntity findDBClassbyIdModuloTipologia(Long idModulo, int tipologia) throws ItemNotFoundDAOException, DAOException;
	public List<ModuloClassEntity> findDBClassbyIdModulo(Long idModulo) throws DAOException;
	public Long insertFileClass(ModuloClassEntity entity) throws DAOException;
	public int updateFileClass(ModuloClassEntity entity) throws DAOException;
	
}
