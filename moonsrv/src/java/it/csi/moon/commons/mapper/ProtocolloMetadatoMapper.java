/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.ProtocolloMetadato;
import it.csi.moon.commons.entity.ProtocolloMetadatoEntity;

/**
 * Contruttore di oggetto JSON ProtocolloMetadato
 *  da ProtocolloMetadatoEntity {@code entity}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ProtocolloMetadatoMapper {
	
	public static ProtocolloMetadato buildFromEntity(ProtocolloMetadatoEntity entity) {
		
		ProtocolloMetadato result = new ProtocolloMetadato();
		
		result.setIdMetadato(entity.getIdMetadato());
		result.setNomeMetadato(entity.getNomeMetadato());
		result.setDefaultValue(entity.getDefaultValue());
		result.setOrdine(entity.getOrdine());
		
		return result;
	}
	
}
