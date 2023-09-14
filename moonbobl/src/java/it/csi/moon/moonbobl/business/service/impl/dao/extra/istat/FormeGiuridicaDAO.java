/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.istat;


import java.util.List;

import it.csi.moon.moonbobl.dto.extra.istat.FormeGiuridica;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle Forme Giuridiche
 * 
 * @see FormeGiuridiche
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
public interface FormeGiuridicaDAO {

	/**
	 *
	 * @return
	 * @throws DAOException 
	 */
	public List<FormeGiuridica> find() throws DAOException;
	
	/**
	 * 
	 * @param codice
	 * @return
	 */
	public FormeGiuridica findById(Integer idFormeGiuridica) throws ItemNotFoundDAOException, DAOException;

}
