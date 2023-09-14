/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

public class RicevutaRichiesta {

	public String data;
	public String numeroIstanza;
	
	public RicevutaRichiesta() {
		super();
	}

	@Override
	public String toString() {
		return "RicevutaRichiesta [data=" + data + ", numeroIstanza=" + numeroIstanza + "]";
	}
	
}
