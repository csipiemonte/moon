/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.UtenteModuloAbilitatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteModuloEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

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
	
	public List<UtenteModuloAbilitatoEntity> findUtenteModuloAbilitatoByIdModulo(Long idModulo);

}
