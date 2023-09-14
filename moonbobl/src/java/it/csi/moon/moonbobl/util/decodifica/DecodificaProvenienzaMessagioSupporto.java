/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util.decodifica;

import it.csi.moon.moonbobl.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica della provenienza di un messaggio di supporto (moon_fo_t_messaggio_supporto.provenienza)
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public enum DecodificaProvenienzaMessagioSupporto implements DecodificaMOON {

	/** FO */
	FO("FO"),
	/** BO */
	BO("BO"),
	;
	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final String codice;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaProvenienzaMessagioSupporto(String codice) {
		this.codice = codice;
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
	public static DecodificaProvenienzaMessagioSupporto byCodice(String codice) {
		for(DecodificaProvenienzaMessagioSupporto d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}

}
