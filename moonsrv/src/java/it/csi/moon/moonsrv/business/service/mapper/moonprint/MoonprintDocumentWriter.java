/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.moonprint;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.moonprint.Document;
import it.csi.moon.commons.dto.moonprint.HeaderFooter;
import it.csi.moon.commons.dto.moonprint.Item;
import it.csi.moon.commons.dto.moonprint.Metadata;
import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.commons.dto.moonprint.Section;
import it.csi.moon.commons.dto.moonprint.Subsection;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Writer per produrre l'oggetto MoonprintModule.Document per la componente MOONPRINT
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class MoonprintDocumentWriter {
	
	private static final String CLASS_NAME = "MoonprintDocumentWriter";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private  Document document;
	private  Section currentSection;
	private  Subsection currentSubsection;
	private  String  template = "default";
	
	public MoonprintDocumentWriter() {
		super();
		document = new Document();
		currentSection = null;
		currentSubsection = null;
	}
	public MoonprintDocumentWriter(String template) {
		super();
		document = new Document();
		currentSection = null;
		currentSubsection = null;
		this.template = template;
	}
	
	public void clean() {
		document = new Document();
		currentSection = null;
		currentSubsection = null;
	}

	public MoonprintDocument write() {
		MoonprintDocument result = new MoonprintDocument(document);
		result.setTemplate(this.template);
		return result;
	}
	

	//
	public void setTitle(String title) {
		document.title = title==null?"":title;
	}
	
	//
	// Metadata
	//
	private  Metadata getMetadata() {
		if (document.metadata == null) {
			document.metadata = new Metadata();
		}
		return document.metadata;
	}
	public void setMetadataDataPresentazione(String dataPresentazione) {
		getMetadata().dataPresentazione = dataPresentazione;
	}
	public void setMetadataQrContent(String qrContent) {
		getMetadata().qrContent = qrContent;
	}
	public void setMetadataNumeroIstanza(String numeroIstanza) {
		getMetadata().numeroIstanza = numeroIstanza;
	}
	public void setMetadataHeader(String left, String center, String right) {
		getMetadata().header = new HeaderFooter(left, center, right);
//		getMetadata().header.left = left;
//		getMetadata().header.center = center;
//		getMetadata().header.right = right;
	}
	public void setMetadataFooter(String left, String center, String right) {
		getMetadata().footer = new HeaderFooter();
		getMetadata().footer.left = left;
		getMetadata().footer.center = center;
		getMetadata().footer.right = right;
	}
	
	//
	// Sections
	//
	private  List<Section> getSections() {
		if (document.sections==null) {
			document.sections = new ArrayList<Section>();
		}
		return document.sections;
	}
	private  void addNewCurrentSection(Section s) {
		getSections().add(s);
		currentSection = s;
		currentSubsection = null;
	}
	public  void addSection(String title, String subtitle) {
		Section s = new Section();
		s.title = title;
		s.subtitle = subtitle;
		
		addNewCurrentSection(s);
	}
	
	
	public  void addSection(boolean pageBreakBefore,  String title, String subtitle) {
		Section s = new Section();
		s.title = title;
		s.subtitle = subtitle;
		s.pageBreakBefore=pageBreakBefore;
		addNewCurrentSection(s);
	}
	
	
	private  Section getCurrentSection() {
		if (currentSection==null) {
			addSection("","");
		}
		return currentSection;
	}

	//
	// SubSections
	//
	private  List<Subsection> getSubSections() {
		if (getCurrentSection().subsections==null) {
			currentSection.subsections = new ArrayList<Subsection>();
		}
		return currentSection.subsections;
	}
	private  void addNewCurrentSubSection(Subsection ss) {
		getSubSections().add(ss);
		currentSubsection = ss;
	}
	public void addSubSection(String title, String subtitle) {
		Subsection ss = new Subsection();
		ss.title = title;
		ss.subtitle = subtitle;
		addNewCurrentSubSection(ss);
	}
	/**
	 * 
	 * @param title
	 * @param subtitle
	 * @param pre for newline use "\r\n" (all other tags as <b>bold</b> or as <i>italic</i> must be added before )
	 * @param post for newline use "\r\n"(all other tags as <b>bold</b> or as <i>italic</i> must be added before )
	 */
	public void addSubSectionTextToHtml(String title, String subtitle, String pre, String post) {
		Subsection ss = new Subsection();
		ss.title = title;
		ss.subtitle = subtitle;
		ss.pre=pre.replace("\r\n", "<br />");
		ss.post=post.replace("\r\n", "<br />");
		addNewCurrentSubSection(ss);
	}
	
	/**
	 * 
	 * @param title
	 * @param subtitle
	 * @param pre 
	 * @param post 
	 */
	public void addSubSection(String title, String subtitle, String pre, String post) {
		Subsection ss = new Subsection();
		ss.title = title;
		ss.subtitle = subtitle;
		ss.pre=pre;
		ss.post=post;
		addNewCurrentSubSection(ss);
	}
	
	//
	// Items
	//
	private List<Item> getItems() {
		if (currentSubsection==null) 
			addSubSection("", "");
		if (currentSubsection.items==null) {
			currentSubsection.items = new ArrayList<Item>();
		}
		return currentSubsection.items;
	}
	public void addItem(String label, String value) {
		Item i = new Item();
		i.label = label;
		i.value = value;
		getItems().add(i);
	}
}
