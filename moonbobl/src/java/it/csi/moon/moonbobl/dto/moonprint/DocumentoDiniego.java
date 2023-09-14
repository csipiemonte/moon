/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;




public class DocumentoDiniego {
	public String title;
	public RicevutaRichiesta richiesta;
	public DatiAnagrafica anagrafica;
	public DatiDiniego datiDiniego;
	public HeaderFooter header;
	public HeaderFooter footer;
	
	public DocumentoDiniego() {
		super();
		this.title="";
		this.richiesta = new RicevutaRichiesta();
		this.anagrafica = new DatiAnagrafica();
		this.datiDiniego = new DatiDiniego();
		this.header = new HeaderFooter();
		this.footer = new HeaderFooter();
	}

	@Override
	public String toString() {
		return "DocumentoRicevuta [title=" + title + ", richiesta=" + richiesta 
				+ ", anagrafica=" + anagrafica+ ", DatiDiniego=" + datiDiniego + ", header=" + header + ", footer=" + footer + "]";
	}

}
