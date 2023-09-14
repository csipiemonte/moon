/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.ProtocolloParametro;
import it.csi.moon.commons.entity.ProtocolloParametroEntity;

/**
 * Contruttore di oggetto JSON ProtocolloParametro
 *  da ProtocolloPatrametroEntity {@code entity}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ProtocolloParametroMapper {
	
	public static ProtocolloParametro buildFromEntity(ProtocolloParametroEntity entity) {
		
		ProtocolloParametro result = new ProtocolloParametro();
		
		result.setIdParametro(entity.getIdParametro());
		result.setEnte(EnteMapper.buildFromId(entity.getIdEnte()));
		result.setArea(AreaMapper.buildFromId(entity.getIdArea()));
		result.setIdModulo(entity.getIdModulo());
		result.setNomeAttributo(entity.getNomeAttributo());
		result.setValore(entity.getValore());
		result.setDataUpd(entity.getDataUpd());
		result.setAttoreUpd(entity.getAttoreUpd());
		
		return result;
	}
	
}
