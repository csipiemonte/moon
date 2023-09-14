/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

public class UlterioreDatiMoonToken {

	private String idFamigliaConvivenzaANPR;
	private String chiaveUnivocita;
	
	/**
	 * Costruttore per TO_SOLIDALE
	 * @param idFamigliaConvivenzaANPR
	 * @param chiaveUnivocita
	 */
	public UlterioreDatiMoonToken(String idFamigliaConvivenzaANPR, String chiaveUnivocita) {
		super();
		this.idFamigliaConvivenzaANPR = idFamigliaConvivenzaANPR;
		this.chiaveUnivocita = chiaveUnivocita;
	}
	
	public String getIdFamigliaConvivenzaANPR() {
		return idFamigliaConvivenzaANPR;
	}
	public void setIdFamigliaConvivenzaANPR(String idFamigliaConvivenzaANPR) {
		this.idFamigliaConvivenzaANPR = idFamigliaConvivenzaANPR;
	}

	public String getChiaveUnivocita() {
		return chiaveUnivocita;
	}
	public void setChiaveUnivocita(String chiaveUnivocita) {
		this.chiaveUnivocita = chiaveUnivocita;
	}
	
}
