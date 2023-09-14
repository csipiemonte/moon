/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.helper;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class DatiIstanzaHelper {
	
	private static final String OF_FULL_KEY = " of fullKey: ";
	private static final String OF_LENGTH = " of length ";
	private static final String CLASS_NAME = "DatiIstanzaHelper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	protected BaseJsonNode dataNode = null;
	
	public JsonNode initDataNode(Istanza istanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::initDataNode] IN istanza: "+istanza);
			if (istanza==null || istanza.getData()==null) {
				throw new BusinessException("InputParamIstanzaDataNull");
			}
			return initDataNode(istanza.getData().toString());
		} finally {
			LOG.debug("[" + CLASS_NAME + "::initDataNode] END");
		}
	}
	public JsonNode initDataNode(String datiIstanza) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::initDataNode] dato da acquisire : nome del comune");
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			dataNode = (ObjectNode)istanzaNode.get("data");
			return dataNode;
		} catch (ParseException e) {
			LOG.error("[" + CLASS_NAME + "::initDataNode] ParseException datiIstanza=" + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::initDataNode] parse for datiIstanza=" + datiIstanza, e);
			throw new BusinessException();
		}
	}
	
	/**
	 * Aggiorna il field 'submit' nel json delle submission FormIo a 'false'
	 * 
	 * @param datiIstanza
	 * @return datiIstanza aggiornata con element "submit":false
	 */
	public String updateSubmitFalse(String datiIstanza) throws Exception {
		String result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::updateSubmitFalse] IN datiIstanza: "+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			((ObjectNode)istanzaNode.get("data")).put("submit", false);	
			result = istanzaNode.toString();
			return result;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::updateSubmitFalse] OUT result:"+result);
		}
	}


	/**
	 * Legge i dati dell istanza in nell oggetto istanza
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	public static JsonNode readIstanzaData(Istanza istanza) throws Exception {
		try {
			LOG.debug("[" + CLASS_NAME + "::readIstanzaData] IN istanza: "+istanza);
			if (istanza==null || istanza.getData()==null) {
				throw new BusinessException("InputParamIstanzaDataNull");
			}
			return readIstanzaData(istanza.getData().toString());
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readIstanzaData] END");
		}
	}
	/**
	 * Legge i dati dell istanza in nell oggetto istanzaJsonNode
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	protected static JsonNode readIstanzaData(String datiIstanza) throws Exception {
		try {
			LOG.debug("[" + CLASS_NAME + "::readIstanzaData(str)] IN datiIstanza: " + datiIstanza);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			return objectMapper.readValue(datiIstanza, JsonNode.class);
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readIstanzaData(str)] ERROR read: " + datiIstanza, e);
		    throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readIstanzaData(str)] END");
		}
	}
	
	//
	//
	public String extractedTextValueFromDataNodeByKey(String fullKey) {
		JsonNode dataTextNode = extractedJsonNodeFromDataNodeByKey(fullKey);
		return dataTextNode!=null?dataTextNode.asText():"";
	}
	public String extractedTextValueFromDataNodeByKey(String fullKey, String formioType) {
		JsonNode dataTextNode = extractedJsonNodeFromDataNodeByKey(fullKey);
		if (dataTextNode==null)
			return null;
		if ("Number".equalsIgnoreCase(formioType))
			return dataTextNode.asText(); //getNumberValue().toString();
		else if ("Boolean".equalsIgnoreCase(formioType))
			return dataTextNode.asBoolean()?"S":"N";
		else
			return dataTextNode.asText();
	}
	public boolean extractedBooleanValueFromDataNodeByKey(String fullKey) {
		JsonNode dataBooleanNode = extractedJsonNodeFromDataNodeByKey(fullKey);
		return dataBooleanNode!=null?dataBooleanNode.asBoolean():false;
	}
	
	public JsonNode extractedJsonNodeFromDataNodeByKey(String fullKey) {
		if (fullKey == null)
			return null;
		JsonNode data = dataNode;
		String key = fullKey;
		// Gestione anidamento oggetti nel JSON, se fullKey contiene .
		if (fullKey.contains(".")) {
			String[] keysArray = fullKey.split("\\.");
			LOG.debug("[" + CLASS_NAME + "::extractedJsonNodeFromDataNodeByKey] fullKey.split('.') : " + keysArray + OF_LENGTH + keysArray.length);
			int i = 0;
			while (i<(keysArray.length-1)) {
				LOG.debug("[" + CLASS_NAME + "::extractedJsonNodeFromDataNodeByKey] ricerca data node " + i + " - " + keysArray[i] + OF_FULL_KEY + fullKey);
				data = data.get(keysArray[i]);
				if (data == null) {
					LOG.warn("[" + CLASS_NAME + "::extractedJsonNodeFromDataNodeByKey] data.get("+keysArray[i]+") null (magari campo opzionale!).");
					return null;
				}
				i++;
			}
			key = keysArray[i];
		}
		// Da QUI data punta sull'oggetto foglia richiesto
		return _extractedJsonNodeFromDataNodeByKey(data, key, fullKey);
	}
	
	private JsonNode _extractedJsonNodeFromDataNodeByKey(JsonNode data, String key, String fullKey) {
		JsonNode result = null;
		if (data != null && data.has(key)) {
			if (data.get(key).isArray()) {
				LOG.warn("[" + CLASS_NAME + "::_extractedJsonNodeFromDataNodeByKey] Key: " + key + OF_FULL_KEY + fullKey + " is not Object but Array in istanza dataNode\n" + dataNode);
			} else {
				LOG.debug("[" + CLASS_NAME + "::_extractedJsonNodeFromDataNodeByKey] data=" + data);
			}
			try {
				result = data.get(key);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::_extractedJsonNodeFromDataNodeByKey] Key: " + key + OF_FULL_KEY + fullKey + " is not String quindi non usare .asText()\n" + dataNode);
			}
		} else {
			LOG.warn("[" + CLASS_NAME + "::_extractedJsonNodeFromDataNodeByKey] Key: " + key + OF_FULL_KEY + fullKey + " not found in ["+data+"] del istanza dataNode\n" + dataNode);
		}
		return result;
	}
	
	
	/**
	 * Ritorna i valori "name" in dataIstanza di un FileUpload identificato da un campoModulo
	 * Used in AssociaAllegatiAdIstanzaTask
	 * @param campo
	 * @return
	 */
	public List<String> extractFormIoFileNameByCampo(CampoModulo campo) {
		if (campo == null)
			return null;
		List<String> result = new ArrayList<>();
		JsonNode data = dataNode;
		// Gestion Presenza di grid
		if (!StrUtils.isEmpty(campo.getGridFullKey())) {
			String gridFullKey = campo.getGridFullKey();
			String gridKey = gridFullKey;
			if (gridFullKey.contains(".")) {
				String[] gridKeysArray = gridFullKey.split("\\.");
				LOG.debug("[" + CLASS_NAME + "::extractFormIoFileNameByCampo] gridFullKey.split('.') : " + gridKeysArray + OF_LENGTH + gridKeysArray.length);
				int i = 0;
				while (i<(gridKeysArray.length-1)) {
					LOG.debug("[" + CLASS_NAME + "::extractFormIoFileNameByCampo] ricerca data node " + i + " - " + gridKeysArray[i] + " of gridFullKey: " + gridFullKey);
					data = data.get(gridKeysArray[i]);
					if (data == null) {
						LOG.warn("[" + CLASS_NAME + "::extractFormIoFileNameByCampo] data.get("+gridKeysArray[i]+") null (magari campo opzionale!).");
						return null;
					}
					i++;
				}
				gridKey = gridKeysArray[i];
			}
			if (data.get(gridKey)!=null && data.get(gridKey).isArray()) { // Il GRID e' sempre un array
				for (int i = 0; i < data.get(gridKey).size(); i++) {
					LOG.debug("[" + CLASS_NAME + "::extractFormIoFileNameByCampo] LOOP GRID "+i);
					JsonNode localData = data.get(gridKey).get(i); //.get(campo.getKey());
					if (localData != null ) {
						result = _extractFormIoFileNameByCampo(campo, result, localData);
					}
				}
			} else {
				LOG.warn("[" + CLASS_NAME + "::extractFormIoFileNameByCampo] gridKey: " + gridKey + " of gridFullKey: " + gridFullKey + " is not Array nel istanza dataNode\n" + dataNode);
			}
		} else {
			result = _extractFormIoFileNameByCampo(campo, result, data);
		}
		
		return result;
	}
	
	private List<String> _extractFormIoFileNameByCampo(CampoModulo campo, List<String> result, JsonNode data) {
		// Gestione anidamento oggetti nel JSON, se fullKey contiene .
		String fullKey = campo.getFullKey();
		String key = fullKey;
		if (fullKey.contains(".")) {
			String[] keysArray = fullKey.split("\\.");
			LOG.debug("[" + CLASS_NAME + "::_extractFormIoFileNameByCampo] fullKey.split('.') : " + keysArray + OF_LENGTH + keysArray.length);
			int i = 0;
			while (i<(keysArray.length-1)) {
				LOG.debug("[" + CLASS_NAME + "::_extractFormIoFileNameByCampo] ricerca data node " + i + " - " + keysArray[i] + OF_FULL_KEY + fullKey);
				data = data.get(keysArray[i]);
				if (data == null) {
					LOG.warn("[" + CLASS_NAME + "::_extractFormIoFileNameByCampo] data.get("+keysArray[i]+") null (magari campo opzionale!).");
					return result;
				}
				i++;
			}
			key = keysArray[i];
		}
		// Da QUI data punta sull'oggetto foglia richiesto
		if (data != null && data.get(key) != null) {
			if (data.get(key).isArray()) { // Il componente FileUpload e' sempre un array
				for (int i = 0; i < data.get(key).size(); i++) {
					if (data.get(key).get(i) != null && data.get(key).get(i).get("name") != null) {
						result.add(data.get(key).get(i).get("name").asText());
					} else {
						LOG.warn("[" + CLASS_NAME + "::_extractFormIoFileNameByCampo] Key: " + key+ OF_FULL_KEY + fullKey
								+ " impossibile estrarre get(0).get('name') nel istanza per dataNode.get(key)=\n"
								+ dataNode.get(key));
					}
				}
			} else {
				LOG.warn("[" + CLASS_NAME + "::_extractFormIoFileNameByCampo] Key: " + key + OF_FULL_KEY + fullKey + " is not Array nel istanza dataNode\n" + dataNode);
			}
		} else {
			LOG.warn("[" + CLASS_NAME + "::_extractFormIoFileNameByCampo] Key: " + key + OF_FULL_KEY + fullKey + " not found in ["+data+"] del istanza dataNode\n" + dataNode);
		}
		return result;
	}
	
	
	public String azzeraDataIstanzaFile(String datiIstanza, List<CampoModulo> campiFile) {
		String result = datiIstanza;
		try {
			LOG.debug("[" + CLASS_NAME + "::azzeraDataIstanzaFile] IN datiIstanza: "+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			for (CampoModulo campo : campiFile) {
				LOG.debug("[" + CLASS_NAME + "::azzeraDataIstanzaFile] campo " + campo);
				_azzeraDataIstanzaFileByCampo(campo, istanzaNode);
			}
			result = istanzaNode.toString();
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::azzeraDataIstanzaFile] " ,e);
			throw new BusinessException();
		} 
		finally { 
			LOG.debug("[" + CLASS_NAME + "::azzeraDataIstanzaFile] OUT result:" + result);
		}
	}
	
	private void _azzeraDataIstanzaFileByCampo(CampoModulo campo, JsonNode data) {
		int i = 0;
		data = data.get("data");
		// Gestion Presenza di grid
		if (!StrUtils.isEmpty(campo.getGridFullKey())) {
			String gridFullKey = campo.getGridFullKey();
			if (gridFullKey.contains(".")) {
				String[] gridKeysArray = gridFullKey.split("\\.");
				i=0;
				while (i<(gridKeysArray.length)) {
					data = data.get(gridKeysArray[i]);
					i++;
				}
				if (data !=null && data.isArray()) { // Il GRID e' sempre un array
					for (int j = 0; j < data.size(); j++) {
						JsonNode localData = data.get(j); 
						_extractFormIoFileNameByCampoNoGrid(campo,localData);
					}
				}
			}
		} else { // ramo di campo senza grid
			_extractFormIoFileNameByCampoNoGrid(campo,data);
		}
}


	
	//arrivati nel nodo finale interessato rimuove tutto
	private void _extractFormIoFileNameByCampoNoGrid(CampoModulo campo, JsonNode data) {
		String fullKey = campo.getFullKey();
		JsonNode localData = data;
		int i=0;
		if(fullKey.contains(".")) {
			String[] fullKeysArray = fullKey.split("\\.");
			while (i<(fullKeysArray.length-1)) {
				localData = localData.get(fullKeysArray[i]);
				i++;
			}
			((ArrayNode)localData.get(fullKeysArray[i])).removeAll();
		}else {
			((ArrayNode)localData.get(fullKey)).removeAll();
			}
	}
	
	public List<AllegatoLazyEntity> verificaMatchFullKeyAllegati(List<AllegatoLazyEntity> allegati, List<CampoModulo> campiFile) {
		List<AllegatoLazyEntity> result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::verificaMatchFullKeyAllegati] " );
			result = new ArrayList<>();
			for(AllegatoLazyEntity a : allegati) {
				for(CampoModulo cf : campiFile) {
					if(a.getFullKey().equals(cf.getFullKey())) {
						result.add(a);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::verificaMatchFullKeyAllegati] " , e);
			throw new BusinessException();
		}
		return result;
	}

	// USED only in TRIB_DICH_IMU STARDAS Protocollazione
	public String extractedFirstTextValueFromArrayNodeByKey(String arrayFullKey, String fullKey) {
		if (arrayFullKey == null)
			return null;
		JsonNode data = dataNode;
		String key = arrayFullKey;
		// Gestione anidamento oggetti nel JSON, se fullKey contiene .
		if (arrayFullKey.contains(".")) {
			String[] keysArray = arrayFullKey.split("\\.");
			LOG.debug("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] fullKey.split('.') : " + keysArray + OF_LENGTH + keysArray.length);
			int i = 0;
			while (i<(keysArray.length-1)) {
				LOG.debug("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] ricerca data node " + i + " - " + keysArray[i] + OF_FULL_KEY + fullKey);
				data = data.get(keysArray[i]);
				if (data == null) {
					LOG.warn("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] data.get("+keysArray[i]+") null (magari campo opzionale!).");
					return null;
				}
				i++;
			}
			key = keysArray[i];
		}
		// Da QUI data punta sull'oggetto foglia richiesto che deve essere un array
		if (data != null && data.get(key) != null) {
			if (data.get(key).isArray()) {
				// prendo solo il primo elemento dell'array
				data = data.get(key).get(0);
				String internalKey = fullKey;
				// Gestione anidamento oggetti nel JSON, se fullKey contiene .
				if (fullKey.contains(".")) {
					String[] keysArray = fullKey.split("\\.");
					LOG.debug("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] fullKey.split('.') : " + keysArray + OF_LENGTH + keysArray.length);
					int i = 0;
					while (i<(keysArray.length-1)) {
						LOG.debug("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] ricerca data node " + i + " - " + keysArray[i] + OF_FULL_KEY + fullKey);
						data = data.get(keysArray[i]);
						if (data == null) {
							LOG.warn("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] data.get("+keysArray[i]+") null (magari campo opzionale!).");
							return null;
						}
						i++;
					}
					internalKey = keysArray[i];
				}
				
				if (data != null && data.get(internalKey) != null) {
					if (data.get(internalKey).isArray()) {
						LOG.warn("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] Key: " + key + OF_FULL_KEY + fullKey + " is not Object but Array in istanza dataNode\n" + dataNode);
					} else {
						LOG.debug("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] data=" + data);
						try {
							return data.get(internalKey)!=null?data.get(internalKey).asText():"";
							// Magari dopo gestire altre tipologie di campo oltre che String : Magari gestire Integer, ...
						} catch (Exception e) {
							LOG.error("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] Key: " + key + OF_FULL_KEY + fullKey + " is not String quindi non usare .asText()\n" + dataNode);
						}
					}
				} else {
					LOG.warn("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] Key: " + key + OF_FULL_KEY + fullKey + " not found in ["+data+"] del istanza dataNode\n" + dataNode);
				}
				
			} else {
				LOG.warn("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] Key: " + key + OF_FULL_KEY + arrayFullKey + " is not Array in istanza dataNode\n" + dataNode);
			}
		} else {
			LOG.warn("[" + CLASS_NAME + "::extractedFirstTextValueFromArrayNodeByKey] Key: " + key + OF_FULL_KEY + arrayFullKey + " not found in ["+data+"] del istanza dataNode\n" + dataNode);
		}
		return null;
	}

}
