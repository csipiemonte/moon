/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.GsonBuilder;

public class JsonHelper {

	private static GsonBuilder gb = new GsonBuilder();
	public static String getJsonFromObject(Object obj)  {
		
		try {
			return gb.create().toJson(obj);
		} catch (Exception e) {
			return ("JSON NOT GENERATED !!!!!!");
		}
	}
	
	public static String getJsonFromList(List list) {
		ObjectMapper objectMapper = new ObjectMapper();
		// Set pretty printing of json
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		// 1. Convert Array to JSON
		try {			
			return objectMapper.writeValueAsString(list);

		} catch (Exception e) {
			return ("JSON NOT GENERATED !!!!!!");
		}
	}
	
	
	
}
