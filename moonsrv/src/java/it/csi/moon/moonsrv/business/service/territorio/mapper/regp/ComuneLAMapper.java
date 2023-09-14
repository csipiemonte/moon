/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio.mapper.regp;

import it.csi.moon.commons.dto.extra.territorio.regp.ComuneLA;
import it.csi.territorio.svista.limamm.ente.cxfclient.Comune;

public class ComuneLAMapper {
	
	private static final String CLASS_NAME = "ComuneLAMapper";
	
	/**
	 * Remap un oggetto di Limamm via Outer servizi in oggetto MoonExtra
	 * @param Comune - comune della toponomastica regionale Limiti Amministrative
	 * @return oggetto MoonExtra ComuneLA compilato (o null se errore)
	 */
	public static ComuneLA remap(Comune comune) {
		if(comune==null)
			return null;
		
		ComuneLA result = new ComuneLA();
		result.setId(comune.getId());
		result.setCodIstat(comune.getCodIstat());
		result.setCodCatasto(comune.getCodCatasto());
		result.setCap(comune.getCap());
		result.setNome(comune.getNome());
		result.setIdProvincia(comune.getIdProvincia());
		result.setAslDiRiferimento(AslLAMapper.remap(comune.getAslDiRiferimento()));
		return result;
	}
	
}
