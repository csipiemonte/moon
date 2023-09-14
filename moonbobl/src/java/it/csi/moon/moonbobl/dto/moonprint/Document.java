/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

import java.util.ArrayList;
import java.util.List;

public class Document {
	public String title;
	public Metadata metadata;
	public List<Section> sections;
	
	public Document() {
		super();
		this.metadata = new Metadata();
		this.sections = new ArrayList<Section>();
	}

	@Override
	public String toString() {
		return "Document [title=" + title + ", metadata=" + metadata + ", sections=" + sections + "]";
	}
	
}
