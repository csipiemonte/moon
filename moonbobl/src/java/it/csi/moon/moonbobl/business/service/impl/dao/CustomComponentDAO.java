/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.CustomComponentEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle componenti custom formio
 * 
 * @see CustomComponentEntity
 * 
 * @author Danilo
 *
 */
public interface CustomComponentDAO {
	public CustomComponentEntity findById(String idComponent) throws ItemNotFoundDAOException, DAOException;
	public List<CustomComponentEntity> find() throws DAOException;

}
