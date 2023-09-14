/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica il tipo della firma crittografica
 * ritornato da Dosign
 * TIPO_MARCA_TEMPORALE (0)
 * TIPOFIRMA_SEMPLICE (1)
 * TIPOFIRMA_MULTIPLA_PARALLELA (2)
 * TIPOFIRMA_MULTIPLA_CONTROFIRMA (3)
 * TIPOFIRMA_MULTIPLA_CATENA (4)
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 15/10/2021 - versione iniziale
 */
public enum DecodificaTipoFirma implements DecodificaMOON {

	TIPO_MARCA_TEMPORALE(0),
	TIPOFIRMA_SEMPLICE(1),
	TIPOFIRMA_MULTIPLA_PARALLELA(2),
	TIPOFIRMA_MULTIPLA_CONTROFIRMA(3),
	TIPOFIRMA_MULTIPLA_CATENA(4),
	;	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaTipoFirma(Integer id) {
		this.id = id;
	}
	
	@Override
	public String getCodice() {
		return String.valueOf(id);
	}
	
	public Integer getId() {
		return id;
	}
	
	
	public static DecodificaTipoFirma byId(Integer id) {
		for(DecodificaTipoFirma d : values()) {
			if(d.id.equals(id)) {
				return d;
			}
		}
		return null;
	}
	
	
}
