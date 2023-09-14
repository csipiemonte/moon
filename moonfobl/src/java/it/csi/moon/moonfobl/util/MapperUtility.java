/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperUtility<T> {

	private static ObjectMapper mapper = new ObjectMapper();
	private Class<T> tipo;
	
	
	public static String getJsonFromObj(Object obj) throws Throwable {
		return mapper.writeValueAsString(obj);				
	}
		
	public MapperUtility(Class<T> tipo) {this.tipo = tipo;}
		
	public T getObjFromJson(String json) throws Throwable {
		return mapper.readValue(json, tipo);
	}
	
}
