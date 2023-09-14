/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

public class MoonPrintDocumentoAccoglimento {
	private String template = "default";
	private DocumentoAccoglimento document;
	
	public MoonPrintDocumentoAccoglimento() {
		super();
		this.document = new DocumentoAccoglimento();
	}
	public MoonPrintDocumentoAccoglimento(String template, DocumentoAccoglimento document) {
		super();
		this.template = template;
		this.document = document;
	}
	
	public MoonPrintDocumentoAccoglimento(DocumentoAccoglimento document) {
		super();
		this.document = document;
	}
	
	public DocumentoAccoglimento getDocument() {
		return document;
	}
	public void setDocument(DocumentoAccoglimento document) {
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
