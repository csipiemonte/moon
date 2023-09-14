/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.EnteAreaRuoloFlatDTO;
import it.csi.moon.commons.entity.RuoloEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai ruoli
 * 
 * @see RuoloEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface RuoloDAO {
	public RuoloEntity findById(Integer idRuolo) throws ItemNotFoundDAOException, DAOException;
	public List<RuoloEntity> find() throws DAOException;
	public Integer insert(RuoloEntity entity) throws DAOException;
	public int delete(RuoloEntity entity) throws DAOException;
	public int delete(Integer idRuolo) throws DAOException;
	public int update(RuoloEntity entity) throws DAOException;
	
	// Direct Ruolo Access
	public List<EnteAreaRuoloFlatDTO> findEntiAreaRuoliAttiviByIdUtente(Long idUtente) throws DAOException;
}

