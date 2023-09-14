/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.ext;

import it.csi.moon.moonbobl.business.service.impl.dto.ext.DomandaBuoniEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso alle istanze direttamente su Database
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface DomandaBuoniDAO {
	public DomandaBuoniEntity findById(Integer id) throws DAOException;
	public Integer countByCodiceFiscali(String joinedCodiceFiscali) throws DAOException;
	
	public Integer insert(DomandaBuoniEntity entity) throws DAOException;

	public int update(DomandaBuoniEntity entity) throws DAOException;
}
