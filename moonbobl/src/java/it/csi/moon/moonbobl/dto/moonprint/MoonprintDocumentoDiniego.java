/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

public class MoonprintDocumentoDiniego {
	private String template = "default";
	private DocumentoDiniego document;
	
	public MoonprintDocumentoDiniego() {
		super();
		this.document = new DocumentoDiniego();
	}
	public MoonprintDocumentoDiniego(String template, DocumentoDiniego document) {
		super();
		this.template = template;
		this.document = document;
	}
	
	public MoonprintDocumentoDiniego(DocumentoDiniego document) {
		super();
		this.document = document;
	}
	
	public DocumentoDiniego getDocument() {
		return document;
	}
	public void setDocument(DocumentoDiniego document) {
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
