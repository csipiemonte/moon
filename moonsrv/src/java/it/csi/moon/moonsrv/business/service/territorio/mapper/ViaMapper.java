/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.territorio.mapper;

import it.csi.apimint.toponomastica.v1.dto.ViaLight;
import it.csi.moon.commons.dto.extra.territorio.Via;
import it.csi.moon.commons.util.StrUtils;


/**
 * Contruttore di oggetto JSON MoonExtra Territorio Via 
 *  da it.csi.apimint.toponomastica.v1.dto.ViaLight        Toponomastica via RS
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 07/04/2020 - versione initiale
 */
public class ViaMapper {
	
	private static final String CLASS_NAME = "ViaMapper";

	/**
	 * Remap un oggetto ANPR via Outer servizi REST in oggetto MoonExtra
	 * @param ViaLight - viaLight della toponomastica di Torino
	 * @return oggetto MoonExtra Via compilato (o vuoto se errore)
	 */
	public static Via remapViaLight(ViaLight viaLight) {
		if(viaLight==null)
			return null;
		
		Via result = new Via();
		result.setCodice(viaLight.getIdVia());
		result.setNome(StrUtils.join(" ", 
			(viaLight.getSedime()!=null)?viaLight.getSedime().getDescrizione():null,
			(viaLight.getSedime()!=null)?viaLight.getSedime().getPreposizione():null,
			viaLight.getDenominazionePrincipale()).trim()); // ci sono dei spazzi, come possibile !!
/* {"idVia":59301,"stato":1,"descStato":"Attiva","convenzionale":0,"sedime":{"idSedime":361,"descrizione":"VIA","descrizioneBreve":"V."},"denominazionePrincipale":"GIUSEPPE VERDI","denominazioneSecondaria":"VERDI GIUSEPPE","denominazioneBreve":"VERDI"}  */
		return result;
	}

}
