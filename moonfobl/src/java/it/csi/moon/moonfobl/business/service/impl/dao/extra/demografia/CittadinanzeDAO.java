/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia;


import java.util.List;

import it.csi.moon.commons.dto.extra.demografia.Cittadinanza;
import it.csi.moon.moonfobl.exceptions.business.DAOException;


/**
 *
 * @author Laurent Pissard
 */
public interface CittadinanzeDAO {

	/**
	 *
	 * @return
	 */
	public List<Cittadinanza> findAll() throws DAOException;
	public List<Cittadinanza> findAll(int limit, int skip) throws DAOException;
	
	/**
	 * 
	 * @param codice
	 * @return
	 */
	public Cittadinanza findByPK(Integer codice) throws DAOException;

	/**
	 * 
	 * @param descMaschile
	 * @return
	 */
	public Cittadinanza findByDesc(String descMaschile) throws DAOException;



}
