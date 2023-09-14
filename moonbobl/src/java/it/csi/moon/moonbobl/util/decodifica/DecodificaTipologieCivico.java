/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica delle possibilita di filtro delle tipologie del civico della Toponomastica di Torino
 * <br> 1      SOLO_RESIDENZIALE          => RESIDENZIALE
 * <br> 3      SOLO_MISTO                 => MISTO
 * <br> 4      SOLO_NON_RESIDENZIALE      => NON_RESIDENZIALE
 * <br> 1,3    RESIDENZIALE               => RESIDENZIALE & MISTO
 * <br> 3,4    NON_RESIDENZIALE           => MISTO & NON_RESIDENZIALE
 * <br> 1,3,4  null
 * 
 * @author Laurent Pissard
 * @version 1.0.0 - 25/06/2020 - versione iniziale
 */
public enum DecodificaTipologieCivico implements DecodificaMOON {

	/** 1	Numero civico residenziale */
	SOLO_RESIDENZIALE("1"),
//	/** 2	Numero civico bloccato */
//	BLOCCATO("2"),
	/** 3	Numero civico misto */
	SOLO_MISTO("3"),
	/** 4	Numero civico non residenziale */
	SOLO_NON_RESIDENZIALE("4"),
//	/** 9	Numero civico Virtuale */
//	VIRTUALE("9"),
	/** 1,3	Residenziale */
	RESIDENZIALE("1,3"),
	/** 3,4	Non Residenziale */
	NON_RESIDENZIALE("3,4"),
	;

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private String tipologie = null;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipologieCivico(String tipologie) {
		this.tipologie = tipologie;
	}
	
	@Override
	public String getCodice() {
		return tipologie;
	}
	
	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaTipologieCivico byCodice(String codice) {
		for(DecodificaTipologieCivico d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}

}
