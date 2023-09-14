/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import it.csi.moon.moonbobl.business.service.impl.dto.UtenteAreaRuoloEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per la gestione della relazione utente area ruolo
 * 
 * @see UtenteAreaRuoloEntity
 * 
 * @author Danilo
 */
public interface UtenteAreaRuoloDAO {
	
	public UtenteAreaRuoloEntity findByUtenteAreaRuolo(Long idUtente, Long idArea, Integer integer) throws ItemNotFoundDAOException, DAOException;	
	public int insert(UtenteAreaRuoloEntity areaRuolo) throws DAOException;
	public int delete(UtenteAreaRuoloEntity entity) throws DAOException;

}
