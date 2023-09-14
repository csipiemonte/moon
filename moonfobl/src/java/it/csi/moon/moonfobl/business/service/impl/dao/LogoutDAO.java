/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.LogoutEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle URL di logout del FO (adesso solo Shib)
 * <br>
 * <br>Tabella moon_fo_d_logout
 * <br>PK: idLogout
 * <br>AK: idPortale,livAuth (0)
 * 
 * @see LogoutEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 31/08/2020 - versione iniziale
 */

public interface LogoutDAO {
	
	public LogoutEntity findById(Long idLogout) throws ItemNotFoundDAOException,DAOException;
	public LogoutEntity findByPortaleLivAuth(Long idPortale, Integer livAuth) throws ItemNotFoundDAOException,DAOException;	
	public List<LogoutEntity> find() throws DAOException;
	public List<LogoutEntity> findByPortale(Long idPortale) throws DAOException;	
	
}
