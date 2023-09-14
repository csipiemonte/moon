/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.territorio.mapper;

import it.csi.apimint.toponomastica.v1.dto.CivicoLight;
import it.csi.moon.commons.dto.extra.territorio.Civico;

/**
 * Contruttore di oggetto JSON MoonExtra Territorio Civico 
 *  da it.csi.apimint.toponomastica.v1.dto.CivicoLight        Toponomastica via RS
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 08/04/2020 - versione initiale
 */
public class CivicoMapper {
	
	private static final String CLASS_NAME = "CivicoMapper";

	/**
	 * Remap un oggetto ANPR via Outer servizi REST in oggetto MoonExtra
	 * @param CivicoLight - civicoLight della toponomastica di Torino
	 * @return oggetto MoonExtra Civico compilato (o vuoto se errore)
	 */
	public static Civico remapCivocoLight(CivicoLight civicoLight) {
		if(civicoLight==null)
			return null;
		
		Civico result = new Civico();
		result.setCodice(civicoLight.getIdCivico());
		result.setNome(civicoLight.getIndirizzo());
		result.setFlagUiu(civicoLight.isUiuCompletate()?"S":"N");
		
		return result;
	}


}
