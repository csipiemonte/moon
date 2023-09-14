/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util.decodifica;

/**
 * Decodifica degli Attributi dei Tipi Documento della LoginRequest
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 30/03/2020 - versione iniziale
 */
public enum DecodificaLoginRequestTipoDocumento implements DecodificaMOON {
	
	CARTA_IDENTITA(1), 
	PERMESSO_SOGGIORNO(2),
	;
	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	
	private DecodificaLoginRequestTipoDocumento(Integer id2) { 
		id=id2;
	}
	
	
	@Override
	public String getCodice() {
		return String.valueOf(id);
	}
	
	public final Integer getId() { 
		return id;
	}

	public static DecodificaLoginRequestTipoDocumento byId(Integer tipoDocumento) {
		for (DecodificaLoginRequestTipoDocumento e : DecodificaLoginRequestTipoDocumento.values()) {
			if (e.getId().equals(tipoDocumento)) {
				return e;
			}
		}
		return null;
	}
	
}
