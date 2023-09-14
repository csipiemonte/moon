/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

import java.util.ArrayList;
import java.util.List;

public class Subsection {
	public String title;
	public String subtitle;
	public String pre;
	public String post;
	public List<Item> items;

	public Subsection() {
		super();
		this.items = new ArrayList<Item>();
	}

	@Override
	public String toString() {
		return "Subsection [title=" + title + ", subtitle=" + subtitle + ", pre=" + pre + ", post=" + post + ", items="
				+ items + "]";
	}
	
}
