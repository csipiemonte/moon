/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.commercio;

import java.util.List;

import it.csi.moon.commons.dto.extra.commercio.Concessione;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle concessioni
 * 
 * @author Alberto
 * 
 * @since 1.0.0
 */
public interface ConcessioniDAO {

	/**
	 * Recupera elenco delle concessioni
	 * @param cf del concessionario
	 */
	public List<Concessione> findByCF(String cf) throws DAOException;
	
	/**
	 * Recupera una concessione
	 * @param pk della concessione
	 * @return singola concessione
	 */
	public Concessione findById(Long id) throws ItemNotFoundDAOException, DAOException;
	
}
