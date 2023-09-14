/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.renderer.template.datamodel;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BozzaDataModel extends DataModel {
	public static class Item {
		@JsonProperty public String label;
		@JsonProperty public String value;
	}

	public static class Subsection {
		@JsonProperty public String title;
		@JsonProperty public String subtitle;
		@JsonProperty public String pre;
		@JsonProperty public String post;

		@JsonProperty
		@JsonDeserialize(as=ArrayList.class, contentAs=Item.class)
		public List<Item> items;
	}


	public static class Section {
		@JsonProperty public String title;
		@JsonProperty public String subtitle;
		@JsonProperty public boolean pageBreakBefore;
		@JsonProperty public boolean pageBreakAfter;

		@JsonProperty
		@JsonDeserialize(as=ArrayList.class, contentAs=Subsection.class)
		public List<Subsection> subsections;
	}

	public static class Document extends DataModel.Document {
//		@JsonProperty
//		public String title;

		@JsonProperty
		@JsonDeserialize(as=Metadata.class, contentAs=Metadata.class)
		public Metadata metadata;

		@JsonProperty
		@JsonDeserialize(as=ArrayList.class, contentAs=Section.class)
		public List<Section> sections;
	}

	public static class HeaderFooter {
		@JsonProperty public String left;
		@JsonProperty public String center;
		@JsonProperty public String right;
	}


	public static class Metadata {
		@JsonProperty
		public String qrContent;

		@JsonProperty
		public String dataPresentazione;

		@JsonProperty
		public String numeroIstanza;

		@JsonProperty
		@JsonDeserialize(as=HeaderFooter.class, contentAs=HeaderFooter.class)
		public HeaderFooter header;

		@JsonProperty
		@JsonDeserialize(as=HeaderFooter.class, contentAs=HeaderFooter.class)
		public HeaderFooter footer;

	}
}
