/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.Ruolo;
import it.csi.moon.commons.entity.RuoloEntity;

/**
 * Contruttore di oggetto JSON Ruolo
 *  da RuoloEntity {@code entity}
 * 
 * @author Mario
 */

public class RuoloMapper {
	
	public static Ruolo buildFromEntity (RuoloEntity entity) {
		
		Ruolo ruolo = new Ruolo();
		ruolo.setIdRuolo(entity.getIdRuolo());
		ruolo.setCodice(entity.getCodiceRuolo());
		ruolo.setNome(entity.getNomeRuolo());
		ruolo.setDescrizione(entity.getDescrizioneRuolo());
		ruolo.setFlagAttivo("S".equals(entity.getFlAttivo()));
		ruolo.setDataUpd(entity.getDataUpd());
		ruolo.setAttoreUpd(entity.getAttoreUpd());
		
		return ruolo;	
	}
}
