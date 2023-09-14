/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.ModuloAttributoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloAttributo;

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
		
		ModuloAttributo obj = new ModuloAttributo();
		obj.setIdAttributo(entity.getIdAttributo());
		obj.setNome(entity.getNomeAttributo());
		obj.setValore(entity.getValore());
		obj.setDataUpd(entity.getDataUpd());
		obj.setAttoreUpd(entity.getAttoreUpd());
		return obj;
	}
	
}
