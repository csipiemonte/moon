/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.RuoloFunzioniEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso ai ruoli delle funzioni
 * 
 * @see RuoloFunzioniEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface RuoloFunzioniDAO {
	public List<RuoloFunzioniEntity> findByIdRuolo(Integer idRuolo) throws DAOException;
	public List<RuoloFunzioniEntity> find() throws DAOException;
	public int insert(RuoloFunzioniEntity entity) throws DAOException;
	public int insertList(List<RuoloFunzioniEntity> entity) throws DAOException;
	public int delete(Integer idRuolo, Integer idFunzione) throws DAOException;
	public int delete(RuoloFunzioniEntity entity) throws DAOException;
	public int deleteAllByIdRuolo(Integer idRuolo) throws DAOException;
}
