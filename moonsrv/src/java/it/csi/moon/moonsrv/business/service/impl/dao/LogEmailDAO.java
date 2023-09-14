/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.LogEmailEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

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
	public List<LogEmailEntity> findByIdIstanza(Long idIstanza) throws DAOException;
	public int insert(LogEmailEntity logEmail) throws DAOException;
}
