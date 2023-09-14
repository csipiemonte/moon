/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.EnteAreaModulo;
import it.csi.moon.commons.entity.AreaEntity;
import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.EnteEntity;

/**
 * Contruttore di oggetto JSON EnteAreaModulo
 *  da EnteEntity {@code ente}
 *  da AreaEntity {@code area}
 *  da AreaModuloEntity {@code areaModulo}
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 07/10/2020 - versione iniziale
 */
public class EnteAreaModuloMapper {
	
	public static EnteAreaModulo buildFromEntities(EnteEntity ente, AreaEntity area, AreaModuloEntity areaModulo) {
		
		EnteAreaModulo result = new EnteAreaModulo();
		
		result.setEnte(ente);
		result.setArea(area);
		result.setIdModulo(areaModulo.getIdModulo());
		result.setDataUpd(areaModulo.getDataUpd());
		result.setAttoreUpd(areaModulo.getAttoreUpd());
		
		return result;
	}

	public static Object buildFromEntity(AreaModuloEntity areaModulo) {
		
		EnteAreaModulo result = new EnteAreaModulo();
		
		result.setIdModulo(areaModulo.getIdModulo());
		result.setDataUpd(areaModulo.getDataUpd());
		result.setAttoreUpd(areaModulo.getAttoreUpd());
		
		return result;
	}

}
