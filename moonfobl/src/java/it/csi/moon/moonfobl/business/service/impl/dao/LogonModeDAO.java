/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;


import it.csi.moon.moonfobl.business.service.impl.dto.LogonModeEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'identificazione del logon mode
 * <br>
 * <br>Tabella : moon_ml_d_logon_mode
 * 
 * @see LogonModeEntity
 * 
 * @author Danilo
 *
 */
public interface LogonModeDAO {
		
	public LogonModeEntity findLogonMode(String codiceModulo,String nomePortale) throws ItemNotFoundDAOException, DAOException;

}
