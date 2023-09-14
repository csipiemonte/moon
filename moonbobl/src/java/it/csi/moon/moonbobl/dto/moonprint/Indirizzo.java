/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

public class Indirizzo {
	
	public String comune;
	public String indirizzo;
	public String indirizzoCompleto;
	
	public Indirizzo() {
		super();
	}

	@Override
	public String toString() {
		return "Indirizzo [comune=" + comune + ", indirizzo=" + indirizzoCompleto + "]";
	}

}
