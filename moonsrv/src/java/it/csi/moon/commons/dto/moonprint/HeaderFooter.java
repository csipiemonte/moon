/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.moonprint;

public class HeaderFooter {
	public String left;
	public String center;
	public String right;
	
	public HeaderFooter() {
		super();
	}

	public HeaderFooter(String left, String center, String right) {
		super();
		this.left = left;
		this.center = center;
		this.right = right;
	}

	@Override
	public String toString() {
		return "HeaderFooter [left=" + left + ", center=" + center + ", right=" + right + "]";
	}

}
