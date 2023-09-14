/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso agli utenti
 * 
 * @see UtenteEntity
 * 
 * @author Mario
 */
public interface UtenteDAO {
	public UtenteEntity findById(Long idUtente) throws ItemNotFoundDAOException, DAOException;
	public UtenteEntity findByIdentificativoUtente(String identificativoUtente) throws ItemNotFoundDAOException, DAOException;
	public UtenteEntity findByUsrPwd(String user,String pwd) throws ItemNotFoundDAOException, DAOException;
	public List<UtenteEntity> find() throws DAOException;
	public Long insert(UtenteEntity entity) throws DAOException;
	public int delete(UtenteEntity entity) throws DAOException;
	public int delete(Long idUtente) throws DAOException;
	public int update(UtenteEntity entity) throws DAOException;
	public boolean isUtenteAbilitato(Long idUtente,String codiceModulo) throws ItemNotFoundDAOException, DAOException;
}
