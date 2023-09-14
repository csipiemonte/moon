/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;


public class DocumentoAccoglimento {
	public String title;
	public RicevutaRichiesta richiesta;
	public DatiAnagrafica anagrafica;
	public DatiAccoglimento datiAccoglimento;
	public HeaderFooter header;
	public HeaderFooter footer;
	
	public DocumentoAccoglimento() {
		super();
		this.title="";
		this.richiesta = new RicevutaRichiesta();
		this.anagrafica = new DatiAnagrafica();
		this.datiAccoglimento = new DatiAccoglimento();
		this.header = new HeaderFooter();
		this.footer = new HeaderFooter();
	}
}
