/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.Azione;

/**
 * Metodi di business relativi agli stati
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface AzioniService {
	
	List<Azione> getElencoAzioni();
	Azione getAzioneById(Long idAzione);
	Azione getAzioneByCd(String codiceAzione);
	Azione createAzione(Azione azione);
	Azione updateAzione(Long idAzione, Azione azione);
	void deleteAzioneById(Long idAzione);

}
