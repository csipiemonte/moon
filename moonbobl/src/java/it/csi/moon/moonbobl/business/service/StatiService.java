/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.StatiFilter;
import it.csi.moon.moonbobl.dto.moonfobl.Stato;

/**
 * Metodi di business relativi agli stati
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface StatiService {
	
	List<Stato> getElencoStati();
	List<Stato> getElencoStati(StatiFilter filter);
	Stato getStatoById(Integer idStato);
	Stato getStatoByCd(String codiceStato);
	Stato createStato(Stato stato);
	Stato updateStato(Integer idStato, Stato stato);
	void deleteStatoById(Integer idStato);

}
