/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.Ruolo;
import it.csi.moon.moonbobl.dto.moonfobl.Utente;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alle ruoli
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface RuoliService {
	List<Ruolo> getElencoRuoli() throws BusinessException;
	Ruolo getRuoloById(Integer idRuolo) throws ItemNotFoundBusinessException, BusinessException;
	Ruolo createRuolo(Ruolo body) throws BusinessException;
	Ruolo updateRuolo(Integer idRuolo, Ruolo body) throws BusinessException;
	
	//
	// Utenti
	//
	List<Utente> getUtentiByIdRuolo(Integer idRuolo) throws BusinessException;
}
