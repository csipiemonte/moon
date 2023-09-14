/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

public class RicevutaAccettazione {
	
	public String data;
	public String modulo;
	public String funzionarioResponsabile;
	public String ufficialeAnagrafe;
	public String rif;
	
	public RicevutaAccettazione() {
		super();
	}

	@Override
	public String toString() {
		return "RicevutaAccettazione [data=" + data + ", modulo=" + modulo + ", funzionarioResponsabile="
				+ funzionarioResponsabile + ", ufficialeAnagrafe=" + ufficialeAnagrafe + ", rif=" + rif + "]";
	}
	
}
