/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.moonprint;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import it.csi.moon.moonsrv.business.service.mapper.HTMLFormat;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Reader per renderizzare il valore o codice salvato nell'istanza in String stampabile
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaDataReader {

	private static final String CLASS_NAME = "IstanzaDataReader";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	//
	//
	public static String getIstanzaDataOf(JsonNode node, JsonNode dataNode) {
		if (node==null || node.get("key")==null) {
			return null;
		}
		return getIstanzaData(node.get("key").asText(), dataNode);
	}
	public static String getIstanzaData(String fieldName, JsonNode dataNode) {
		if (dataNode.get(fieldName)==null) {
			return "";
		}
		return dataNode.get(fieldName).asText();
	}

	
	//
	// SELECT
	//
	public static String getIstanzaDataOfSelect(JsonNode node, JsonNode dataNode) {
		try {
			String result = null;
			if (node==null || node.get("key")==null) {
				return null;
			}
			JsonNode itemData = dataNode.get(node.get("key").asText());
			if (itemData==null || itemData.isNull() || "{}".equals(itemData.toString())) {
				return ""; // NON Valorizzato
			}
	//		System.out.println("itemData="+itemData);
	//		System.out.println("label =" + node.get("label"));
	//		System.out.println("key   =" + node.get("key"));
	//		System.out.println("dataSrc=" + node.get("dataSrc"));
	//		System.out.println("data=" + node.get("data"));
	//		System.out.println("template=" + node.get("template"));
	//		System.out.println("selectValues=" + node.get("selectValues"));
	//		System.out.println("selectValues=" + node.get("valueProperty")); // Chiave di salvataggio, se null tutto l'oggetto json
			
			switch (node.get("dataSrc").asText()) {
				case "values" :
					result = getLabelSelectValuesJsonDataFormIoNode(node, itemData);
					break;
				case "json" :
					result = getLabelSelectValuesJsonDataFormIoNode(node, itemData);
					break;
				case "url" :
					if (node.get("selectValues")==null || StringUtils.isEmpty(node.get("selectValues").asText())) {
						// SE viene salvato tutto l'oggetto json nell istanza
						result = new HTMLFormat(node.get("template").asText(), itemData).format();
						// System.out.println("VALUE = " + result );
					} else {
						// SE viene salvato solo il codice nell istanza
						System.out.println("*** NOT IMPLEMENTED for SELECT.url.selectValues=" + node.get("selectValues") + " ***");
					}
					break;
				default :
					System.out.println("*** NOT IMPLEMENTED for SELECT.dataSrc=" + node.get("dataSrc") + " *****");
					break;
			}
			return result;
		} catch (Exception e) {
			LOG.error("["+CLASS_NAME+"::getIstanzaDataOfSelect] Exception SELECT.values key=" + node.get("key") + " - Il valore presente in istanze dataNode=" + dataNode+ "  msg="+e.getMessage());
			return null;
		}
	}
	
	@Deprecated // Generalizzato in getLabelSelectValuesJsonDataFormIoNode
	public static String getLabelValuesDataFormIoNode(JsonNode node, JsonNode itemData) {
		try {
			if (node.get("data")==null || node.get("data").get("values")==null) {
				System.out.println("ERROR SELECT.values key=" + node.get("key") + " - Impossibile ricercare il valore del select in un array vuoto for node.data="+node.get("data"));
				return null;
			}
			JsonNode valuesNode = node.get("data").get("values"); // "data.values" per prendere i valori possibile nel modulo
			if (valuesNode.isArray()) {
				for (JsonNode valNode : valuesNode) {
					if (itemData.asText().equals(valNode.get("value").asText())) {
						return valNode.get("label").asText();
					}
				}
			}
			LOG.warn("ERROR SELECT.values key=" + node.get("key") + " - Il valore presente in istanze " + itemData+ "  NON e' stato trovato nelle values del form valuesNode="+valuesNode);
			return null;
		} catch (Exception e) {
			LOG.error("["+CLASS_NAME+"::getLabelValuesDataFormIoNode] Exception SELECT.values key=" + node.get("key") + " - Il valore presente in istanze " + itemData+ "  msg="+e.getMessage());
			return null;
		}
	}
	
	public static String getLabelSelectValuesJsonDataFormIoNode(JsonNode node, JsonNode itemData) {
		try {
			String dataSrc = node.get("dataSrc").asText(); // For "values" OR "json" :: data.json OR data.values
			if (node.get("data")==null || node.get("data").get(dataSrc)==null) {
				System.out.println("ERROR SELECT.json key=" + node.get("key") + " - Impossibile ricercare il valore del select in un array vuoto for node.data="+node.get("data"));
				return null;
			}
			String keySaved = (node.get("valueProperty")!=null)?node.get("valueProperty").asText():null;
			if (StringUtils.isEmpty(keySaved)) {
				// Tutto l'item json è stato salvato nell'istanza, ne prendo il template
				return new HTMLFormat(node.get("template").asText(), itemData).format();
			} else {
				// Dal codice salvato in itemData, ricerco l'item.keySaved corrispondente nell'array data.json del modulo
				JsonNode valuesNode = node.get("data").get(dataSrc); // "data.json" per prendere i valori possibile nel modulo.
				if (valuesNode.isArray()) {
					String strItemData = itemData.asText();
					for (JsonNode valNode : valuesNode) {
						JsonNode nodeKeyToCompare = valNode.get(keySaved); // Tipizzato (itemData lui arriva sempre String)
						if (strItemData.equals(String.valueOf(nodeKeyToCompare))) {
							return new HTMLFormat(node.get("template").asText(), valNode).format();
						}
					}
				}
				LOG.warn("ERROR SELECT."+dataSrc+" key=" + node.get("key") + " - Il valore presente in istanze " + itemData+ "  NON e' stato trovato nelle values del form valuesNode="+valuesNode);
			}
			LOG.warn("ERROR SELECT."+dataSrc+" key=" + node.get("key") + " - Il valore presente in istanze " + itemData+ "  NON e' stato trovato nei dati presente nel form node.get(\"data\")="+node.get("data"));
			return null;
		} catch (Exception e) {
			LOG.error("["+CLASS_NAME+"::getLabelSelectValuesJsonDataFormIoNode] Exception SELECT.values key=" + node.get("key") + " - Il valore presente in istanze " + itemData+ "  msg="+e.getMessage());
			return null;
		}
	}
	
	
	//
	// DATAGRID
	//
	public static ArrayNode getIstanzaDataArrayOf(JsonNode node, JsonNode dataNode) {
		if (node==null || node.get("key")==null) {
			return null;
		}
		return getIstanzaDataArray(node.get("key").asText(), dataNode);
	}
	private static ArrayNode getIstanzaDataArray(String fieldName, JsonNode dataNode) {
		try {
			ArrayNode result = null;
			JsonNode nodeData = dataNode.get(fieldName);
			if(nodeData!=null && nodeData.isArray()) {
		        result = (ArrayNode) nodeData;
			}
			return result;
		} catch (Exception e) {
			LOG.error("["+CLASS_NAME+"::getIstanzaDataArray] Exception for key=" + fieldName + " - Il valore presente in istanze dataNode=" + dataNode + "  msg="+e.getMessage());
			return null;
		}
	}
	
	
	//
	// FILE
	//
	public static String getIstanzaDataOfFile(JsonNode node, JsonNode dataNode) {
		if (node==null || node.get("key")==null) {
			return null;
		}
		return getIstanzaDataFile(node.get("key").asText(), dataNode);
	}
	public static String getIstanzaDataFile(String fieldName, JsonNode dataNode) {
		try {
			String result = null;
			List<String> resultList = new ArrayList<>();
			ArrayNode fileInseriti = getIstanzaDataArray(fieldName, dataNode);
			if (fileInseriti!=null) {
	        	for (int i = 0; i < fileInseriti.size(); i++) {
		            JsonNode fileInserito = fileInseriti.get(i);
		            if (fileInserito!=null) {
		            	JsonNode originalName = fileInserito.get("originalName");
		            	if (originalName!=null) {
		            		resultList.add(originalName.asText());
		            	}
		            }
	        	}
	        	result = StringUtils.join(resultList, " ; ");
			}
			return result;
		} catch (Exception e) {
			LOG.error("["+CLASS_NAME+"::getIstanzaDataFile] Exception for key=" + fieldName + " - Il valore presente in istanze dataNode=" + dataNode + "  msg="+e.getMessage());
			return null;
		}
	}
}
