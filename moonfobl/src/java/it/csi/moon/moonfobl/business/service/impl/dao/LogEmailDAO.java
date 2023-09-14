/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import it.csi.moon.commons.entity.LogEmailEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso ai log email
 * <br>
 * <br>Tabella principale : moon_fo_t_log_email
 * 
 * @see LogEmailEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/06/2020 - versione iniziale
 */
public interface LogEmailDAO {
	
	public int insert(LogEmailEntity logEmail) throws DAOException;

}
