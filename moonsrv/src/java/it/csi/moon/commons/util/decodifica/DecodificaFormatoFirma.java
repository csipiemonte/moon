/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.util.helper.DecodificaMOONValoriHelper;

/**
 * Decodifica il codice della firma crittografica
 * ritornato da Dosign
 * (1): Firma digitale enveloped.
 * (2): Firma digitale detached.
 * (3): Marca temporale InfoCert.
 * (4): Marca temporale detached.
 * (5): Time stamped data.
 * (6): Firma digitale PAdES.
 * (7): Firma digitale XAdES.
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 15/10/2021 - versione iniziale
 */
public enum DecodificaFormatoFirma implements DecodificaMOON {

	FIRMA_ENVELOPED(1),
	FIRMA_DETACHED(2),
	MARCA_TEMPORALE_INFOCERT(3),
	MARCA_TEMPORALE_DETACHED(4),
	TIME_STAMPED_DATA(5),
	FIRMA_DIGITALE_PADES(6),
	FIRMA_DIGITALE_XADES(7),
	;	
	/** I valori possibili per la decodifica */
	public static final String VALORI_POSSIBILI;
	private final Integer id;
	
	
	static {
		VALORI_POSSIBILI = DecodificaMOONValoriHelper.getValoriPossibili(values());
	}
	
	private DecodificaFormatoFirma(Integer id) {
		this.id = id;
	}
	
	@Override
	public String getCodice() {
		return String.valueOf(id);
	}
	
	public Integer getId() {
		return id;
	}
	
	
	public static DecodificaFormatoFirma byId(Integer id) {
		for(DecodificaFormatoFirma d : values()) {
			if(d.id.equals(id)) {
				return d;
			}
		}
		return null;
	}
	
	
}
