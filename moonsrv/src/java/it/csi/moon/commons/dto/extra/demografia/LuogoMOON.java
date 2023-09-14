/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.demografia;

/**
 * LuogoMOON
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class LuogoMOON {

	Nazione nazione;
	String descrizioneLuogo;
	
	public LuogoMOON() {
		super();
	}
	
	
	public Nazione getNazione() {
		return nazione;
	}
	public void setNazione(Nazione nazione) {
		this.nazione = nazione;
	}
	public String getDescrizioneLuogo() {
		return descrizioneLuogo;
	}
	public void setDescrizioneLuogo(String descrizioneLuogo) {
		this.descrizioneLuogo = descrizioneLuogo;
	}

}
