/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;



public enum LogonModeEnum {

	NO_AUTH,
	IDP_SHIBBOLETH,
	LOGIN_PWD,
	LOGIN_PIN,
	GOOGLE_ID,
	CF_DOCUMENTO, 
	API_IDP_SHIB,
	;

	public static LogonModeEnum byName(String strLogonMode) {
		if (strLogonMode==null) return null;
		for (LogonModeEnum mode : values()) {
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
