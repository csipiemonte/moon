/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

public enum ConsumerTypeEnum {

	TEXT,
	LINK,
	IMAGE
	;

	public static ConsumerTypeEnum byName(String type) {
		if (type==null) return null;
		for (ConsumerTypeEnum t : values()) {
			if (t.name().equalsIgnoreCase(type)) {
				return t;
			}
		}
		return null;
	}
	
	public boolean hasName(String type) {
		return name().equalsIgnoreCase(type)?true:false;
	}
	
}
