/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.FruitoreEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per acquisire i dati del Fruitore 
 * 
 * @see FruitoreEntity
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public interface FruitoreDAO {
	
	public FruitoreEntity getFruitoreByCodice(String codice) throws ItemNotFoundDAOException, DAOException;
	public FruitoreEntity getFruitoreById(Integer idFruitore) throws ItemNotFoundDAOException, DAOException;
	public Integer countByIdModulo(Long idModulo, Integer idFruitore) throws DAOException;
	public Integer countByIdEnte(Long idEnte, Integer idFruitore) throws DAOException;	
	public List<FruitoreEntity> find() throws DAOException;
	public Integer insert(FruitoreEntity entity) throws DAOException;
	int delete(FruitoreEntity entity) throws DAOException;
	int delete(Integer idEnte) throws DAOException;
	int update(FruitoreEntity entity) throws DAOException;	
	
}
