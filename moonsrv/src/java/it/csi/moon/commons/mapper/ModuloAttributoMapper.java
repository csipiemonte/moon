/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.ModuloAttributo;
import it.csi.moon.commons.entity.ModuloAttributoEntity;

/**
 * Contruttore di oggetto JSON Modulo per i moduli 
 *  da ModuloAttributoEntity {@code entity}
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloAttributoMapper {
	
	public static ModuloAttributo remap(ModuloAttributoEntity entity) {
		
		return new ModuloAttributo(entity.getNomeAttributo(), entity.getValore());
		
	}
	
}
