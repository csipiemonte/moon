/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util.decodifica;

/**
 * Decodifica Embedded Service
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 03/06/2022 - versione iniziale
 */
public enum DecodificaEmbeddedService implements DecodificaMOON {
	
	/** ISTANZA_VIEW */
	ISTANZA_VIEW("VIEW"),
	/** ISTANZA_EDIT */
	ISTANZA_EDIT("EDIT"),
	/** ISTANZA_NEW */
	ISTANZA_NEW("NEW"),
	/** ISTANZA_NEW */
	ISTANZA("ISTANZA"),
	/** ISTANZE*/
	ISTANZE("ISTANZE")	
	;
	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private String codiceService = null;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	
	private DecodificaEmbeddedService(String codiceService) {
		this.codiceService = codiceService;
	}
	
	
	@Override
	public String getCodice() {
		return String.valueOf(codiceService);
	}


	public static DecodificaEmbeddedService byId(String codiceService) {
		for (DecodificaEmbeddedService e : DecodificaEmbeddedService.values()) {
			if (e.getCodice().equals(codiceService)) {
				return e;
			}
		}
		return null;
	}
	
}
