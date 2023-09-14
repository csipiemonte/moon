/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.ModuloAttributoEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;



/**
 * DAO per l'accesso ai attributi di un modulo
 * 
 * @see ModuloAttributoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ModuloAttributiDAO {
	public List<ModuloAttributoEntity> findByIdModulo(Long idModulo) throws DAOException;
	public ModuloAttributoEntity findById(Long idAttributo) throws DAOException;
	public Long insert(ModuloAttributoEntity attributo) throws DAOException;
	public int update(ModuloAttributoEntity attributo) throws DAOException;
	public int delete(Long idAttributo) throws DAOException;
	public int deleteAllByIdModulo(Long idModulo) throws DAOException;
}
