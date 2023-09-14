/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

import java.util.ArrayList;
import java.util.List;

public class RicevutaAnagrafica {
	
	public CognomeNomePatVei richiedente;
	public List<CognomeNomePatVei> nucleoFamiliare;
	public Indirizzo nuovoIndirizzo;
	public String comuneProvenienza;
	public String fraseIntestazione;
	
	public RicevutaAnagrafica() {
		super();
		this.richiedente = new CognomeNomePatVei();
		this.nucleoFamiliare = new ArrayList<CognomeNomePatVei>();
		this.nuovoIndirizzo = new Indirizzo();
		this.comuneProvenienza = "";
		this.fraseIntestazione = "";
	}

	@Override
	public String toString() {
		return "RicevutaAnagrafica [richiedente=" + richiedente + ", nucleoFamiliare=" + nucleoFamiliare
				+ ", nuovoIndirizzo=" + nuovoIndirizzo + "]";
	}
	
}
