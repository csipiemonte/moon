/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.territorio.mapper;

import java.util.function.Predicate;

import it.csi.apimint.toponomastica.v1.dto.UiuLight;
import it.csi.moon.commons.dto.extra.territorio.PianoNUI;

/**
 * Contruttore di oggetto JSON MoonExtra Territorio PianoNUI 
 *  da it.csi.apimint.toponomastica.v1.dto.UiuLight        Toponomastica via RS
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 08/04/2020 - versione initiale
 */
public class PianoNUIMapper {
	
	private static final String SEPARATORE_NUI = ", NUI: ";
	private static final String CLASS_NAME = "PianoNUIMapper";

	/**
	 * Remap un oggetto Toponomastica via Outer servizi REST in oggetto MoonExtra
	 * @param UiuLight - uiuLight della toponomastica di Torino
	 * @return oggetto MoonExtra PianoNUI compilato (o vuoto se errore)
	 */
	public static PianoNUI remap(UiuLight uiuLight) {
		if(uiuLight==null)
			return null;
		
		PianoNUI result = new PianoNUI();
		result.setCodice(uiuLight.getIdUiu());
		result.setNome(makeNameByPianoNui(uiuLight));  //TODO da verificare
		
		return result;
	}

	private static String makeNameByPianoNui(UiuLight uiuLight) {
		return makeNameByPianoNui(uiuLight.getDescrizionePiano(),uiuLight.getNui());
	}
	public static String makeNameByPianoNui(String descPiano, Integer nui) {
		return descPiano + SEPARATORE_NUI + nui;
	}

	public static Predicate<PianoNUI> isNUI(Integer nui) {
		String searchSuffix = SEPARATORE_NUI + nui;
        return p -> p.getNome()!=null && p.getNome().endsWith(searchSuffix);
    }
	
}
