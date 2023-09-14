/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.demografia;


import java.util.List;

import it.csi.moon.commons.dto.extra.demografia.Nazione;
import it.csi.moon.moonfobl.exceptions.business.DAOException;


/**
 *
 * @author Laurent Pissard
 */
public interface NazioneDAO {

	/**
	 *
	 * @return
	 */
	public List<Nazione> findAll(String uso, String eu) throws DAOException;
	
	public List<Nazione> findAll() throws DAOException;

	
	/**
	 * 
	 * @param codice
	 * @return
	 */
	public Nazione findByPK(Integer codice) throws DAOException;

	/**
	 * 
	 * @param nomeNazione
	 * @return
	 */
	public List<Nazione> findByNome(String nomeNazione) throws DAOException;

}
