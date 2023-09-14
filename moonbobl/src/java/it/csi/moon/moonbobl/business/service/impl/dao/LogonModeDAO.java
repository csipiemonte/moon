/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.LogonMode;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai Moduli da Modulistica
 * <br>
 * <br>Tabella moon_ml_d_logon_mode
 * <br>PK: idLogonMode
 * <br>AK1: codiceLogonMode
 * 
 * @see LogonMode
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface LogonModeDAO {
	
	public LogonMode findById(Long idPortale) throws ItemNotFoundDAOException,DAOException;
	public LogonMode findByCd(String codicePortale) throws ItemNotFoundDAOException,DAOException;
	public List<LogonMode> find() throws DAOException;
	
}
