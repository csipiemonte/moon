/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

public class AllegatiSummary {

	private int numeroAllegati;
	private Long dimensioneTotale = null;
	
	public int getNumeroAllegati() {
		return numeroAllegati;
	}
	public void setNumeroAllegati(int numeroAllegati) {
		this.numeroAllegati = numeroAllegati;
	}
	public Long getDimensioneTotale() {
		return dimensioneTotale;
	}
	public void setDimensioneTotale(Long dimensioneTotale) {
		this.dimensioneTotale = dimensioneTotale;
	}
	
	@Override
	public String toString() {
		return "AllegatoSummary [numeroAllegati=" + numeroAllegati + ", dimensioneTotale=" + dimensioneTotale + "]";
	}
	
}
