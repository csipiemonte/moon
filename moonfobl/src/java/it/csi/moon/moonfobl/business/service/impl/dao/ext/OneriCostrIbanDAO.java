/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.ext;

import it.csi.moon.moonfobl.business.service.impl.dto.ext.OneriCostrIbanEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai dati relativi al codice Iban
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public interface OneriCostrIbanDAO {
	
	public OneriCostrIbanEntity findByCodiceIstat(String codiceIstat) throws ItemNotFoundDAOException, DAOException;
	
	public int insert(OneriCostrIbanEntity entity) throws DAOException;

	public int update(OneriCostrIbanEntity entity) throws DAOException;

}
