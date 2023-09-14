/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio.mapper.regp;

import java.util.ArrayList;
import java.util.List;

import it.csi.moon.commons.dto.extra.territorio.regp.AslLA;
import it.csi.territorio.svista.limamm.ente.cxfclient.ArrayOfAsl;
import it.csi.territorio.svista.limamm.ente.cxfclient.Asl;

public class AslLAMapper {
	
	private static final String CLASS_NAME = "ComuneLAMapper";
	
	/**
	 * Remap un oggetto di Limamm via Outer servizi in oggetto MoonExtra
	 * @param Asl - asl della toponomastica regionale Limiti Amministrative
	 * @return oggetto MoonExtra AslLA compilato (o null se errore)
	 */
	public static AslLA remap(Asl asl) {
		if(asl==null)
			return null;
		
		return new AslLA(asl.getCodAsl(), asl.getNome(), asl.getNomeBreve());
	}

	/**
	 * Remap un oggetto di Limamm via Outer servizi in oggetto MoonExtra
	 * @param ArrayOfAsl - aslDiRiferimento della toponomastica regionale Limiti Amministrative
	 * @return oggetto MoonExtra List<AslLA> compilato (o null se errore)
	 */
	public static List<AslLA> remap(ArrayOfAsl aslDiRiferimento) {
		if(aslDiRiferimento==null)
			return null;
		
		List<AslLA> result = new ArrayList<>();
		for (Asl asl : aslDiRiferimento.getItem()) {
			if(asl!=null)
				result.add(remap(asl));
		}
		return result;
	}
	
}
