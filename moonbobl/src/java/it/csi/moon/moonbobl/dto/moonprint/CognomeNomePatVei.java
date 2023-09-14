/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

public class CognomeNomePatVei {
	
	public String cognome;
	public String nome;
	public String numeroPatente;
	public String targaVeicolo;
	
	public CognomeNomePatVei() {
		super();
	}

	@Override
	public String toString() {
		return "CognomeNomePatVei [cognome=" + cognome + ", nome=" + nome + ", numeroPatente=" + numeroPatente
				+ ", targaVeicolo=" + targaVeicolo + "]";
	}
	
}
