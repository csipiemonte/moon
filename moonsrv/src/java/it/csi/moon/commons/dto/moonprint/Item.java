/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.moonprint;

public class Item {
	public String label;
	public String value;
	
	
	public Item() {
		super();
	}

	public Item(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Item [label=" + label + ", value=" + value + "]";
	}
	
}
