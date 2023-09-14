/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.moonprint;

import java.util.ArrayList;
import java.util.List;

public class Section {
	public String title;
	public String subtitle;
	public boolean pageBreakBefore;
	public boolean pageBreakAfter;
	public List<Subsection> subsections;

	
	public Section() {
		super();
		this.subsections = new ArrayList<>();
	}


	@Override
	public String toString() {
		return "Section [title=" + title + ", subtitle=" + subtitle + ", pageBreakBefore=" + pageBreakBefore
				+ ", pageBreakAfter=" + pageBreakAfter + ", subsections=" + subsections + "]";
	}
	
	
}
