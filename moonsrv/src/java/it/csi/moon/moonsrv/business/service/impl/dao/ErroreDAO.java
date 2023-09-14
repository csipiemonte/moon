/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;
import it.csi.moon.commons.entity.ErroreEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

public interface ErroreDAO {

	public ErroreEntity findById(String uuid) throws ItemNotFoundDAOException, DAOException;
	public void insert(ErroreEntity entity) throws DAOException;

}
