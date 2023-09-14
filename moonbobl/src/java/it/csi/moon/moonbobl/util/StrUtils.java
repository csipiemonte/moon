/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StrUtils {

	public static boolean isEmpty(final CharSequence cs) {
	   return cs == null || cs.length() == 0;
	}
	
	public static String join(CharSequence deli, CharSequence... ele) {
	   return Stream.of(ele)
			    .filter(s -> !isEmpty(s))
			    .collect(Collectors.joining(" "));
	}
	
	
	public static String pruneUpper(final CharSequence cs) {
		if (isEmpty(cs)) {
			return null;
		}
		String result = cs.toString().trim().toUpperCase();
		return result.length()==0?null:result;
	}
	
	public static String firstLetterUpper(String str) {
		if (isEmpty(str)) {
			return null;
		}
	    String result = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	    return result;
	}

	/**
	 * TRUE se la String vale S or s, FALSE altimenti (mai null)
	 * @param flag da controllare di tipo S/N/null
	 * @return Boolean TRUE or FALSE (mai Null)
	 */
	public static Boolean isTRUE(String flag) {
		// TODO Auto-generated method stub
		return "S".equalsIgnoreCase(flag);
	}

	/**
	 * toUpperCase OR null if Empty
	 * @param str
	 * @return
	 */
	public static String toUpperCase(String str) {
		if (isEmpty(str)) {
			return null;
		}
		return str.toUpperCase();
	}

	/**
	 * toLowerCase OR null if Empty
	 * @param str
	 * @return
	 */
	public static String toLowerCase(String str) {
		if (isEmpty(str)) {
			return null;
		}
		return str.toLowerCase();
	}

}
