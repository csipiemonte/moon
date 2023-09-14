/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto;

import java.util.List;

import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO di accesso all'elenco dei codici fiscali di esenzione SISE
 * 
 * @author Danilo
 * 
 * @since 1.0.0
 */

public interface EsenteSISEDAO {
	
	public List<String> find() throws DAOException;

}


