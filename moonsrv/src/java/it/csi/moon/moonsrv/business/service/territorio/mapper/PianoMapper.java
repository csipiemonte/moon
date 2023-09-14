/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.territorio.mapper;

import it.csi.apimint.toponomastica.v1.dto.UiuLight;
import it.csi.moon.commons.dto.extra.territorio.Piano;

/**
 * Contruttore di oggetto JSON MoonExtra Territorio Piano 
 *  da it.csi.apimint.toponomastica.v1.dto.UiuLight        Toponomastica via RS
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 26/08/2020 - versione initiale
 */
public class PianoMapper {
	
	private static final String CLASS_NAME = "PianoMapper";

	/**
	 * Remap un oggetto Toponomastica via Outer servizi REST in oggetto MoonExtra
	 * @param UiuLight - uiuLight della toponomastica di Torino
	 * @return oggetto MoonExtra Piano compilato (o vuoto se errore)
	 */
	public static Piano remap(UiuLight uiuLight) {
		if(uiuLight==null)
			return null;
		
		Piano result = new Piano();
		result.setCodice(uiuLight.getPiano());
		result.setNome(uiuLight.getDescrizionePiano());
		
		return result;
	}
	
}
