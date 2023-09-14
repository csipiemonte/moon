/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.entity.EntiFilter;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai enti
 * <br>
 * <br>Tabella principale : moon_fo_d_ente
 * 
 * @see EnteEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/06/2020 - versione iniziale
 */
public interface EnteDAO {
	
	public EnteEntity findById(Long idEnte) throws ItemNotFoundDAOException, DAOException;
	public EnteEntity findByCodice(String codice) throws ItemNotFoundDAOException, DAOException;
	public List<EnteEntity> find(EntiFilter filter) throws DAOException;
	
	public Long insert(EnteEntity entity) throws DAOException;
	int delete(EnteEntity entity) throws DAOException;
	int delete(Long idEnte) throws DAOException;
	int update(EnteEntity entity) throws DAOException;
	
}
