/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import it.csi.moon.moonfobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso alla tipologia di codice istanza
 * 
 * 
 * @author Alberto Deiro
 *
 * @since 1.0.0
 */
public interface CodiceIstanzaDAO {
	public String findById(Integer id) throws DAOException;
}
