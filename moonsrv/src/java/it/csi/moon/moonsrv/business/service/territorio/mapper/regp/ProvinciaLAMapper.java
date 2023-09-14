/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio.mapper.regp;

import it.csi.moon.commons.dto.extra.territorio.regp.ProvinciaLA;
import it.csi.territorio.svista.limamm.ente.cxfclient.Provincia;

public class ProvinciaLAMapper {
	
	private static final String CLASS_NAME = "ProvinciaLAMapper";
	
	/**
	 * Remap un oggetto di Limamm via Outer servizi in oggetto MoonExtra
	 * @param Provincia - regione della toponomastica regionale Limiti Amministrative
	 * @return oggetto MoonExtra RegioneLA compilato (o null se errore)
	 */
	public static ProvinciaLA remap(Provincia provincia) {
		if(provincia==null)
			return null;
		
		ProvinciaLA result = new ProvinciaLA();
		result.setId(provincia.getId());
		result.setCodIstat(provincia.getCodIstat());
		result.setSigla(provincia.getSigla());
		result.setNome(provincia.getNome());
		result.setIdRegione(provincia.getIdRegione());
		return result;
	}
	
}
