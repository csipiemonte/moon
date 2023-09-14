/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.Area;
import it.csi.moon.commons.entity.AreaEntity;

/**
 * Contruttore di oggetto JSON area
 *  da areaEntity {@code entity}
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 09/12/2021 - versione iniziale
 */
public class AreaMapper {
	
	public static Area buildFromId(Long idArea) {
		Area area = new Area();
		area.setIdArea(idArea);
		return area;
	}
	public static Area buildFromId(Long idEnte, Long idArea) {
		Area area = new Area();
		area.setIdArea(idArea);
		area.setIdEnte(idEnte);
		return area;
	}
	
	public static Area buildFromEntity(AreaEntity entity) {
		
		Area area = new Area();
		
		area.setIdArea(entity.getIdArea());
		area.setIdEnte(entity.getIdEnte());
		area.setCodiceArea(entity.getCodiceArea());
		area.setNomeArea(entity.getNomeArea());
		area.setDataUpd(entity.getDataUpd());
		area.setAttoreUpd(entity.getAttoreUpd());
		
		return area;
	}

}
