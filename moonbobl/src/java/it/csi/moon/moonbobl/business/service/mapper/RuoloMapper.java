/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.RuoloEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Ruolo;

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
		ruolo.setCodiceRuolo(entity.getCodiceRuolo());
		ruolo.setNomeRuolo(entity.getNomeRuolo());
		ruolo.setDescrizioneRuolo(entity.getDescrizioneRuolo());
		ruolo.setFlagAttivo("S".equals(entity.getFlAttivo()));
		ruolo.setDataUpd(entity.getDataUpd());
		ruolo.setAttoreUpd(entity.getAttoreUpd());
		
		return ruolo;	
	}
}
