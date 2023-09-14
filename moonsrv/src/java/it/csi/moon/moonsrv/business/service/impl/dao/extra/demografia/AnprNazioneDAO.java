/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.demografia;


import java.util.ArrayList;

import it.csi.moon.commons.dto.extra.demografia.Nazione;
import it.csi.moon.moonsrv.exceptions.business.DAOException;


/**
 *
 * @author Laurent Pissard
 */
public interface AnprNazioneDAO {

	/**
	 *
	 * @return
	 */
	public ArrayList<Nazione> findAll() throws DAOException;
	
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
	public Nazione findByNome(String nomeNazione) throws DAOException;

}
