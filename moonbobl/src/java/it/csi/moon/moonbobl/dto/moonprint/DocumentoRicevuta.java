/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

public class DocumentoRicevuta {
	
	public String title;
	public RicevutaRichiesta richiesta;
	public RicevutaAccettazione accettazione;
	public RicevutaAnagrafica anagrafica;
	public HeaderFooter header;
	public HeaderFooter footer;
	
	public DocumentoRicevuta() {
		super();
		this.richiesta = new RicevutaRichiesta();
		this.accettazione = new RicevutaAccettazione();
		this.anagrafica = new RicevutaAnagrafica();
		this.header = new HeaderFooter();
		this.footer = new HeaderFooter();
	}

	@Override
	public String toString() {
		return "DocumentoRicevuta [title=" + title + ", richiesta=" + richiesta + ", accettazione=" + accettazione
				+ ", anagrafica=" + anagrafica + ", header=" + header + ", footer=" + footer + "]";
	}

}
