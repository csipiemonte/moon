/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util;

import com.google.gson.GsonBuilder;

public class JsonHelper {

	private static GsonBuilder gb = new GsonBuilder();
	
	public static String getJsonFromObject(Object obj) {
		
		
		return gb.create().toJson(obj);
	}
	
}
