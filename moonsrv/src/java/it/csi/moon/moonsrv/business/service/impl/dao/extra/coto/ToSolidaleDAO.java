/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto;

import java.util.List;

import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO di accesso all'elenco dei codici fiscali di esenzione ToSolidale
 * 
 * @author Luca
 * 
 * @since 1.0.0
 */
public interface ToSolidaleDAO {
	public List<String> find() throws DAOException;

	public String findByCf(String cf) throws DAOException;
}
