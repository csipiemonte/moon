/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica del TipoStruttura
 *   Used in  moon_io_d_modulostruttura.tipo_struttura
 *   
 * @author Laurent Pissard
 * @version 1.0.0 - 24/12/2019 - versione iniziale
 *
 */
public enum DecodificaTipoStruttura implements DecodificaMOON {

	/** FRM - Form */
	FRM("FRM", "Form"),
	/** WIZ - Wizard */
	WIZ("WIZ", "Wizard"),
	;

	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private String codice;
	private String descrizione;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipoStruttura(String codice, String descrizione) {
		this.codice = codice;
		this.descrizione = descrizione;
	}
	
	@Override
	public String getCodice() {
		return codice;
	}

	
	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaTipoStruttura byCodice(String codice) {
		for(DecodificaTipoStruttura d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}

}
