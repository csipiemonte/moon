/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import it.csi.moon.commons.util.StrUtils;

/**
 * Decodifica dei Tipi Documento di Riconoscimento in alcune modalità di Login
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 02/12/2020 - versione iniziale
 */
public enum EnumTipoDocumentoRiconoscimento {
	
	CARTA_IDENTITA(1), 
	PERMESSO_SOGGIORNO(2),
	;
	
	private int codice;
	private EnumTipoDocumentoRiconoscimento(int codice) {
		this.codice = codice;
	}
	public int getCodice() {
		return codice;
	}

	public static EnumTipoDocumentoRiconoscimento byId(int codice) {
		for(EnumTipoDocumentoRiconoscimento e : values()) {
			if(e.getCodice() == codice) {
				return e;
			}
		}
		return null;
	}
	
	public boolean isCorrectId(String codice) {
		if (StrUtils.isEmpty(codice)) return false;
		if (String.valueOf(getCodice()).equals(codice))
			return true;
		return false;
	}
}
