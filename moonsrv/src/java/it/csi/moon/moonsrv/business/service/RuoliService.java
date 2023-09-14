/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Ruolo;
import it.csi.moon.commons.dto.Utente;

/**
 * Metodi di business relativi alle ruoli
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface RuoliService {
	List<Ruolo> getElencoRuoli();
	Ruolo getRuoloById(Integer idRuolo);
	Ruolo createRuolo(Ruolo body);
	Ruolo updateRuolo(Integer idRuolo, Ruolo body);
	
	//
	// Utenti
	//
	List<Utente> getUtentiByIdRuolo(Integer idRuolo);
}
