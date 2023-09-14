/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrIbanEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso ai dati relativi al codice Iban
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public interface OneriCostrIbanDAO {
	
	public OneriCostrIbanEntity findByCodiceIstat(String codiceIstat) throws DAOException;
	
	public int insert(OneriCostrIbanEntity entity) throws DAOException;

	public int update(OneriCostrIbanEntity entity) throws DAOException;

;
}
