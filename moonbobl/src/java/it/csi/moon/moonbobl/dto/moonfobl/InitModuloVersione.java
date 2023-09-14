/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

public class InitModuloVersione {

	private Long idVersioneModulo = null;
	private String versione = null;
	private StatoModulo stato = null;
	
	public Long getIdVersioneModulo() {
		return idVersioneModulo;
	}
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}
	public String getVersione() {
		return versione;
	}
	public void setVersione(String versione) {
		this.versione = versione;
	}
	public StatoModulo getStato() {
		return stato;
	}
	public void setStato(StatoModulo stato) {
		this.stato = stato;
	}
		  
}
