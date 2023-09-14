/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import it.csi.moon.commons.entity.UtenteModuloEntity;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per la gestione della relazione utente modulo
 * 
 * @see UtenteModuloEntity
 * 
 * @author Danilo
 */
public interface UtenteModuloDAO {
	public UtenteModuloEntity findByIdUtenteModulo(Long idUtente, Long idModulo) throws ItemNotFoundDAOException, DAOException;
	public UtenteModuloEntity findByCfUtenteModulo(String identificativoUtente, Long idModulo) throws ItemNotFoundDAOException, DAOException;
	public int insert(UtenteModuloEntity entity) throws DAOException;
	public int delete(UtenteModuloEntity entity) throws DAOException;
	public int deleteAllByIdModulo(Long idModulo) throws DAOException;

}
