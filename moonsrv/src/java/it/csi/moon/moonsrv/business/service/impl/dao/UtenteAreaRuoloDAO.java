/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.UtenteAreaRuoloEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso ai ruoli utenti
 * 
 * @see UtenteAreaRuoloEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface UtenteAreaRuoloDAO {
	public List<UtenteAreaRuoloEntity> findByIdUtente(Long idUtente) throws DAOException;
	public List<UtenteAreaRuoloEntity> findByIdRuolo(Integer idRuolo) throws DAOException;
	public List<UtenteAreaRuoloEntity> findByIdAreaIdRuolo(Long idArea, Integer idRuolo) throws DAOException;
	public List<UtenteAreaRuoloEntity> find() throws DAOException;
	public int insert(UtenteAreaRuoloEntity entity) throws DAOException;
	public int delete(Long idUtente, Long idArea, Integer idRuolo) throws DAOException;
	public int delete(UtenteAreaRuoloEntity entity) throws DAOException;
	public int deleteAllByIdUtente(Long idUtente) throws DAOException;
	public int deleteAllByIdRuolo(Integer idRuolo) throws DAOException;	
}
