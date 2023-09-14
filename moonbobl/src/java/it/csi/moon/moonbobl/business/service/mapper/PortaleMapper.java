/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.PortaleEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Portale;

/**
 * Contruttore di oggetto JSON Ruolo
 *  da RuoloEntity {@code entity}
 * 
 * @author Mario
 */

public class PortaleMapper {
	
	public static Portale buildFromEntity (PortaleEntity entity) {
		
		Portale portale = new Portale();
		portale.setIdPortale(entity.getIdPortale());
		portale.setCodicePortale(entity.getCodicePortale());
		portale.setNomePortale(entity.getNomePortale());
		portale.setDescrizionePortale(entity.getDescrizionePortale());
		portale.setFlagAttivo("S".equals(entity.getFlagAttivo()));
		portale.setDataUpd(entity.getDataUpd());
		portale.setAttoreUpd(entity.getAttoreUpd());
		
		return portale;	
	}
}
