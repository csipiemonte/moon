/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Formater in String di template HTML presenti nella definizione dei moduli FormIo
 * Usato principalmente da PrintIstanzaMapper_ESTA_RAGA per la generazione del oggetto JSON per MOOnPrint
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class HTMLFormat {
	
    private int curPos;
    
	private String htmlFormat;
	private String strFormat;
	private JsonNode nodeData;
	
	public HTMLFormat(String htmlFormat, JsonNode nodeData) {
		super();
		this.htmlFormat = htmlFormat;
		this.nodeData = nodeData;
		this.curPos = 0;

		strFormat = removeHTMLTags(htmlFormat);
	}
	
	public HTMLFormat(String htmlFormat, JsonNode nodeData, boolean html) {
		super();
		this.htmlFormat = htmlFormat;
		this.nodeData = nodeData;
		this.curPos = 0;

		if (html) {
			strFormat = htmlFormat;
		} else {
			strFormat = removeHTMLTags(htmlFormat);
		}
	}
	
	private String removeHTMLTags(String htmlFormat2) {
		return htmlFormat.replaceAll("\\<.*?\\>", "");
//		return Jsoup.parse(htmlFormat);
	}

	public String format() {
		String result="";

		String key;
		int posEND;
		int pos = strFormat.indexOf("{{", curPos);
//		System.out.println("format() curPos="+curPos+"  pos="+pos);
		if (pos>=0) {
			result += strFormat.substring(curPos, pos);
			// extractKey
			posEND = strFormat.indexOf("}}", pos+2);
//			System.out.println("format() posEND="+posEND);
			if (posEND>pos) {
				key = strFormat.substring(pos+2,posEND).trim().replace("item.", "");
//				System.out.println("format() key="+key);
//				result += ("#" + key + "#");
				result += getItemValue(key);
				curPos = posEND+2;
//				System.out.println("format() return result="+result+" format()");
				return result + format();
			} else {
				result += strFormat.substring(curPos);
				return result;
			}
		} else {
			result += strFormat.substring(curPos);
			return result;
		}
	}

	private String getItemValue(String key) {
		if (nodeData.get(key)==null) {
			return null;
		}
		if (nodeData.get(key).isNumber()) {
			return nodeData.get(key).asText(); //.getNumberValue().toString();
		} else if (nodeData.get(key).isTextual()) {
			return nodeData.get(key).asText();
		}
		return nodeData.get(key).asText();
	}
	
}
