/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;


/**
 * MoonprintDocument encapsulazione del oggetto da fornire a MoonPrint per la stampa PDF
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class MoonprintDocumentoRicevuta {

	private String template = "default";
	private DocumentoRicevuta document;
	
	public MoonprintDocumentoRicevuta() {
		super();
		this.document = new DocumentoRicevuta();
	}
	public MoonprintDocumentoRicevuta(String template, DocumentoRicevuta document) {
		super();
		this.template = template;
		this.document = document;
	}
	
	public MoonprintDocumentoRicevuta(DocumentoRicevuta document) {
		super();
		this.document = document;
	}
	
	public DocumentoRicevuta getDocument() {
		return document;
	}
	public void setDocument(DocumentoRicevuta document) {
		this.document = document;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	
	@Override
	public String toString() {
		return "MoonprintDocument [template=" + template + ", document=" + document + "]";
	}
	
}
