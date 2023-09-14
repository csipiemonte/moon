/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

public enum DecodificaModuloClass  implements DecodificaMOON {
	INIT(1),
	MAPPER_MOONPRINT(2),
	PROTOCOLLO_MANAGER(3),
	;

	public static final String VALORI_POSSIBILI;
	private final Integer id;
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaModuloClass(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}


	@Override
	public String getCodice() {
		return id.toString();
	}

	/**
	 * Ottiene la decodifica a partire dal codice.
	 * 
	 * @param codice il codice da cercare
	 * @return la codifica corrispondente al codice, se presente
	 */
	public static DecodificaModuloClass byCodice(String codice) {
		for(DecodificaModuloClass d : values()) {
			if(d.getCodice().equals(codice)) {
				return d;
			}
		}
		return null;
	}
	
	public static DecodificaModuloClass byId(Integer id) {
		for(DecodificaModuloClass d : values()) {
			if(d.id.equals(id)) {
				return d;
			}
		}
		return null;
	}

}
