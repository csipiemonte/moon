/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import it.csi.moon.commons.util.decodifica.DecodificaFormatoFirma;
import it.csi.moon.commons.util.decodifica.DecodificaTipoFirma;

public class ReportVerificaFirma {
	
	private boolean firmato;
	private DecodificaFormatoFirma formatoFirma;
	private DecodificaTipoFirma tipoFirma;
	
	public boolean isFirmato() {
		return firmato;
	}
	public void setFirmato(boolean firmato) {
		this.firmato = firmato;
	}
	public DecodificaFormatoFirma getFormatoFirma() {
		return formatoFirma;
	}
	public void setFormatoFirma(DecodificaFormatoFirma formatoFirma) {
		this.formatoFirma = formatoFirma;
	}
	public DecodificaTipoFirma getTipoFirma() {
		return tipoFirma;
	}
	public void setTipoFirma(DecodificaTipoFirma tipoFirma) {
		this.tipoFirma = tipoFirma;
	}
	
	@Override
	public String toString() {
		return "ReportVerificaFirma [firmato=" + firmato + ", formatoFirma=" + formatoFirma + ", tipoFirma=" + tipoFirma
				+ "]";
	}

}
