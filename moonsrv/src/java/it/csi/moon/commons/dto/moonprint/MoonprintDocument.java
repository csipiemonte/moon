/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.moonprint;

/**
 * MoonprintDocument encapsulazione del oggetto da fornire a MoonPrint per la stampa PDF
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class MoonprintDocument {

	public static final String TEMPLATE_DEFAULT = "default";
	public static final String TEMPLATE_BOZZA = "Bozza";
	
	private String template = TEMPLATE_DEFAULT;
	private Document document;
	
	public MoonprintDocument() {
		super();
		this.document = new Document();
	}
	public MoonprintDocument(String template, Document document) {
		super();
		this.template = template;
		this.document = document;
	}
	
	public MoonprintDocument(Document document) {
		super();
		this.document = document;
	}
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
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
