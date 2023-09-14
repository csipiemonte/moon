/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import it.csi.moon.moonfobl.business.service.impl.dto.ext.UtenteAreaRuoloEntity;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per la gestione della relazione utente area ruolo
 * 
 * @see UtenteAreaRuoloEntity
 * 
 * @author Danilo
 */
public interface UtenteAreaRuoloDAO {
	
	public UtenteAreaRuoloEntity findByUtenteAreaRuolo(Long idUtente, Long idArea, Integer integer) throws ItemNotFoundDAOException, DAOException;	
	public int insert(UtenteAreaRuoloEntity entity) throws DAOException;

}
