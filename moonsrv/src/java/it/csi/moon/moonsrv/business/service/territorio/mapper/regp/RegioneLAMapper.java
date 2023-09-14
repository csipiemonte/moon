/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio.mapper.regp;

import it.csi.moon.commons.dto.extra.territorio.regp.RegioneLA;
import it.csi.territorio.svista.limamm.ente.cxfclient.Regione;

public class RegioneLAMapper {
	
	private static final String CLASS_NAME = "RegioneLAMapper";
	
	/**
	 * Remap un oggetto di Limamm via Outer servizi in oggetto MoonExtra
	 * @param Regione - regione della toponomastica regionale Limiti Amministrative
	 * @return oggetto MoonExtra RegioneLA compilato (o null se errore)
	 */
	public static RegioneLA remap(Regione regione) {
		if(regione==null)
			return null;
		
		RegioneLA result = new RegioneLA();
		result.setId(regione.getId());
		result.setCodIstat(regione.getCodIstat());
		result.setSigla(regione.getSigla());
		result.setNome(regione.getNome());
		return result;
	}
	
}
