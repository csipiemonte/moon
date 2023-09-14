/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.istat;


import java.util.List;

import it.csi.moon.commons.dto.extra.istat.Ateco;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai codice ISTAT Ateco 2007 - Classificazione delle attività economiche Ateco 2007
 * 
 * @see Ateco
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
public interface AtecoDAO {

	/**
	 *
	 * @return
	 * @throws DAOException 
	 */
	public List<Ateco> findFinale() throws DAOException;

	/**
	 * 
	 * @param codiceComune
	 * @return
	 */
	public Ateco findByCodice(String codiceAteco) throws ItemNotFoundDAOException, DAOException;

}
