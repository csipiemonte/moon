/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.FunzioneEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle funzioni
 * 
 * @see FunzioneEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface FunzioneDAO {
	public FunzioneEntity findById(Integer idFunzione) throws ItemNotFoundDAOException, DAOException;
	public FunzioneEntity findByCd(String codiceFunzione) throws ItemNotFoundDAOException, DAOException;
	public List<FunzioneEntity> find() throws DAOException;
	public Integer insert(FunzioneEntity entity) throws DAOException;
	public int delete(FunzioneEntity entity) throws DAOException;
	public int delete(Integer idFunzione) throws DAOException;
	public int update(FunzioneEntity entity) throws DAOException;
}
