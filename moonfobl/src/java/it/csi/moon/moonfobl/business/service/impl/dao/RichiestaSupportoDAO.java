/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.sql.Types;
import java.util.List;

import it.csi.moon.commons.entity.RichiestaSupportoEntity;
import it.csi.moon.commons.entity.RichiestaSupportoFilter;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle Richieste di Supporto (Chat)
 * 
 * @see RichiestaSupportoEntity
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public interface RichiestaSupportoDAO {
	public RichiestaSupportoEntity findById(Long idRichiestaSupporto) throws ItemNotFoundDAOException, DAOException;
	public List<RichiestaSupportoEntity> find(RichiestaSupportoFilter filter) throws DAOException;
	
	public Long insert(RichiestaSupportoEntity entity) throws DAOException;
	public int delete(RichiestaSupportoEntity entity) throws DAOException;
	public int delete(Long idRichiestaSupporto) throws DAOException;
	public int update(RichiestaSupportoEntity entity) throws DAOException;
}

