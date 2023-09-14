/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import it.csi.moon.moonfobl.business.service.impl.dto.ext.UtenteModuloEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per la gestione della relazione utente modulo
 * 
 * @see UtenteModuloEntity
 * 
 * @author Danilo
 */
public interface UtenteModuloDAO {
	public UtenteModuloEntity findByUtenteModulo(Long idUtente, Long idModulo) throws ItemNotFoundDAOException, DAOException;	
	public int insert(UtenteModuloEntity entity) throws DAOException;

}
