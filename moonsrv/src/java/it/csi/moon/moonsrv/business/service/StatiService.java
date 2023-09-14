/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Stato;

/**
 * Metodi di business relativi agli stati
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface StatiService {
	
	List<Stato> getElencoStati();
	Stato getStatoById(Integer idStato);
	Stato getStatoByCd(String codiceStato);
	Stato createStato(Stato stato);
	Stato updateStato(Integer idStato, Stato stato);
	void deleteStatoById(Integer idStato);

}
