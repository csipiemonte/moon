/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.AreaEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Area;

/**
 * Contruttore di oggetto JSON Area
 *  da AreaEntity {@code entity}
 * 
 * @author Mario
 */

public class AreaMapper {
	
	public static Area buildFromEntity (AreaEntity entity) {
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
