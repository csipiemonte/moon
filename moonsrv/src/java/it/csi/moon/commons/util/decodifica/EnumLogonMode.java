/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

public enum EnumLogonMode {

	NO_AUTH,
	LOGIN_PIN,
	CF_DOCUMENTO,
	GIA_AUTH,
	CF_CI,
	LOGIN_PWD,
	GOOGLE,
	;

	public static EnumLogonMode byName(String strLogonMode) {
		if (strLogonMode==null) return null;
		for (EnumLogonMode mode : values()) {
			if (mode.name().equalsIgnoreCase(strLogonMode)) {
				return mode;
			}
		}
		return null;
	}
	
	public boolean hasName(String logonModeName) {
		return name().equalsIgnoreCase(logonModeName)?true:false;
	}
	
}
