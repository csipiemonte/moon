/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

public enum VersioneStatoEnum {

	ALL,
	CURRENT,
	LAST,
	;

	public static VersioneStatoEnum byName(String type) {
		if (type==null) return null;
		for (VersioneStatoEnum mode : values()) {
			if (mode.name().equalsIgnoreCase(type)) {
				return mode;
			}
		}
		return null;
	}
	
	public boolean hasName(String type) {
		return name().equalsIgnoreCase(type)?true:false;
	}
	
}
