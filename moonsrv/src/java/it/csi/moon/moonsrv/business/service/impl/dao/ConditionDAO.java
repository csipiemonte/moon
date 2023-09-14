/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import it.csi.moon.commons.entity.ConditionEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle condition
 * 
 * @see ConditionEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ConditionDAO {
	
	public ConditionEntity findById(Long id) throws ItemNotFoundDAOException, DAOException;
	public ConditionEntity findByCodice(String codice) throws ItemNotFoundDAOException, DAOException;
	
	public Long insert(ConditionEntity condition) throws DAOException;
	public int delete(Long id) throws DAOException;
	public int update(ConditionEntity condition) throws DAOException;

}
