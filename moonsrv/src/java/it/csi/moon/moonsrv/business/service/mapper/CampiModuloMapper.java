/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Estrazione dei Campi di un Modulo
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 07/09/2020 - versione iniziale
 */
public class CampiModuloMapper {
	
	private static final String CLASS_NAME = "CampiModuloMapper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private String gridKey = null;
	private String gridFullKey = null;
	private boolean onlyFirstLevel = false;
	private String typeFilter = null;
	
	public String getGridKey() {
		return gridKey;
	}
	public void setGridKey(String gridKey) {
		this.gridKey = gridKey;
	}
	public String getGridFullKey() {
		return gridFullKey;
	}
	public void setGridFullKey(String gridFullKey) {
		this.gridFullKey = gridFullKey;
	}
	
/*
    private static final Set<String> SKIP_NODES = Collections.unmodifiableSet(new HashSet<String> (
    		Arrays.asList("collapsible", "tag", "tags", "breadcrumb", "clearOnHide", "labelPosition", "breadcrumb", "properties", "validate",
    			"customPrivate", "custom", "pattern", "minLength", "maxLength", "required", "strictDateValidation", "spellcheck",
    			"placeholder", "inputMask", "autofocus", "lockKey", "customClass", "refreshOn", "redrawOn", "errorLabel", "tooltip", 
    			"tabindex", "dbIndex", "customDefaultValue", "calculateValue", "validateOn", "overlay", "style",
    			"left", "top", "width", "height", "allowCalculateOverride", "encrypted", "alwaysEnabled", "showCharCount", "showWordCount",
    			"allowMultipleMasks", "mask", "tree", "className", "attr", "attrs", "value", "state", 
    			"id", "size", "leftIcon", "rightIcon", "block", "action", "disableOnInvalid", 
    			"storage", "dir", "fileNameTemplate", "image", "webcam", "fileTypes", "filePattern", "fileMinSize", "fileMaxSize", "customConditional",
    			"logic", "imageSize", "privateDownload", "uploadOnly", 
    			"nextPage", "shortcut", "fieldSet", "optionsLabelPosition", "inline", "name",
    			"breadcrumbClickable", "buttonSettings", "previous", "cancel", "next",
    			"case",
    			// Select
    			"resource", "headers", "disableLimit", "dataSrc", "valueProperty", "filter", "authenticate", "template", "clearOnRefresh", 
    			"limit", "lazyLoad", "searchEnabled", "searchField", "minSearch", "readOnlyValue", "selectFields", "searchThreshold", "json", 
    			"fuseOptions", "include", "threshold", "customOptions", 
    			"data", "values", "url", "dataType", "selectThreshold", "selectValues", 
    			// DataGrid
    			"disableAddingRemovingRows", "reorder", "addAnother", "addAnotherPosition", "defaultOpen", "layoutFixed", 
    			"inDataGrid", 
    			// Day
    			"hideInputLabels", "inputsLabelPosition", "useLocaleSettings", "fields", "dayFirst", "maxDate", "minDate", 
    			"hide", "month", "year", 
    			// Email
    			"kickbox", "enabled", "path", 
    			"content", "path",
    			// Textarea
    			"editor", "autoExpand", "rows", "wysiwyg", 
    			// Forse da usare :
//    			"dataGridLabel", 
    			"disabled", "persistent", "unique", "protected", "defaultValue", "suffix", "prefix", "inputFormat", "description", 
    			"widget", "attributes", 
    			// Usati :
    			"conditional", "hideLabel", "tableView", "theme", "multiple", 
    			"type", "label", "key" , "title", "input", "hidden", "inputType",
    			// Columns
    			"columns", "delimiter","requireDecimal","hideOnChildrenHidden","offset","push","pull",
    			"autoAdjust"
            	)));
*/

//    private static final Set<String> KNOWED_NODES = Collections.unmodifiableSet(new HashSet<String> (
//    		Arrays.asList("components" , ""
//    			)));

//    private class ConditionalNodeException extends Exception {
//
//    	private static final long serialVersionUID = 1L;
//    	private static final String  DEFAULT_MSG = "Errore su layer business, operazione non eseguita";
//    	public ConditionalNodeException() {
//    		super(DEFAULT_MSG);
//    	}
//    	public ConditionalNodeException(String msg) {
//    		super(DEFAULT_MSG + " - "+ msg);
//    	}
//    	public ConditionalNodeException(Throwable thr) {
//    		super(DEFAULT_MSG + " - "+thr.getMessage());
//    	}
//    };

    
    /**
     * estrai la lista dei campi della struttura del modulo
     * 
     * @param strutturaEntity
     * 
     * @return la lista dei campi
     * 
     * @throws Exception 
     */
	public List<CampoModulo> getCampi(String content, boolean pOnlyFirstLevel, String type) throws Exception {

		List<CampoModulo> result = new ArrayList<>();
		onlyFirstLevel = pOnlyFirstLevel;
		typeFilter = type;
		
		// Lettura delle due strutture variabile
		JsonNode moduloJsonNode = readJsonNode(content);
		
		 // renderNode(moduloJsonNode.get("components"), FIRST_LEVEL, istanzaJsonNode.get("data"));
		result = parse(moduloJsonNode.get("components"),null,result,"");
		return result;
	}

	
	public List<CampoModulo> parse(JsonNode element, JsonNode parent, List<CampoModulo> campi, String keyPrefix) throws Exception {
		try {
			if (LOG.isDebugEnabled() && !StringUtils.isEmpty(keyPrefix)) {
				LOG.debug("["+CLASS_NAME+"::parse] keyPrefix: " + keyPrefix);
			}
			if (element.isArray()) {
				for (Iterator<JsonNode> it = element.iterator();it.hasNext(); ) {
					campi = parse(it.next(), element, campi, keyPrefix);
				}
			} else if (element.isObject()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("["+CLASS_NAME+"::parse] Type: " + ( element.has("type") ? element.get("type").asText():"NO TYPE") + " Label: " + ( element.has("label") ? element.get("label").asText():"NO LABEL") + " Key: " + ( element.has("key") ? element.get("key").asText():"NO KEY"));
				}
				switch (element.has("type")  ? element.get("type").asText(): "NO TYPE") {
			//
			// Basic
				case "textfield":
				case "textarea":
				case "number":
				case "password":
				case "checkbox":
				case "selectboxes":
				case "select":
				case "radio":
				case "button":
				case "myrating":
					LOG.debug("["+CLASS_NAME+"::parse] Added.");
					campi = addCampo(campi, buildCampoModulo(element.get("label").asText(), element.get("key").asText(), element.get("type").asText(), keyPrefix));
					break;
				case "email":
				case "url":
				case "phoneNumber":
				case "tags":
				case "address":
				case "datetime":
				case "time":
				case "day":
				case "currency":
				case "survey":
				case "signature":
					LOG.debug("["+CLASS_NAME+"::parse] Added.");
					campi = addCampo(campi, buildCampoModulo(element.get("label").asText(), element.get("key").asText(), element.get("type").asText(), keyPrefix));
					break;
			//
			// Layout
				case "htmlelement":
				case "containt":
					break;
				case "columns":	
					// Element verrano listali per colona : campoTextFieldCol1Sx1, campoTextFieldCol1Sx2, campoTextFieldCol2Dx1, campoTextFieldCol2Dx2
					for (JsonNode c: element.get("columns")) {
						campi = parse (c, element, campi, keyPrefix);
					} 
					break;
				case "hidden":
					LOG.debug("["+CLASS_NAME+"::parse] Added.");
					campi = addCampo(campi, buildCampoModulo(element.get("label").asText(), element.get("key").asText(), element.get("type").asText(), keyPrefix));
					break;
				case "container":
					String prefisso = addPrefix(keyPrefix, element.get("key").asText());
					LOG.debug("["+CLASS_NAME+"::parse] container ... using prefisso: "+prefisso);
					campi = parse (element.get("components"), null, campi, prefisso);
//					for (JsonNode c: element.get("components")) {
//						campi = parse (c, element, campi, prefisso); 
//					} 
					break;
				case "file":
					LOG.debug("["+CLASS_NAME+"::parse] Added.");
					campi = addCampo(campi, buildCampoModulo(element.get("label").asText(), element.get("key").asText(), element.get("type").asText(), keyPrefix));
					break;
				case "datagrid":
				case "editgrid":
					LOG.debug("["+CLASS_NAME+"::parse] grid ...");
					if (onlyFirstLevel) {
						campi = addCampo(campi, buildCampoModulo(element.get("label").asText(), element.get("key").asText(), element.get("type").asText(), keyPrefix));
					} else {
						setGridKey(element.get("key").asText());
						setGridFullKey(addPrefix(keyPrefix,element.get("key").asText()));
						for (Iterator<JsonNode> it = element.iterator();it.hasNext(); ) {
							campi = parse(it.next(), element, campi, keyPrefix);
						}
						setGridKey(null);
						setGridFullKey(null);
					}
					break;
				case "NO TYPE":
					//LOG.debug("["+CLASS_NAME+"::parse] Elemento senza type");
					break;
				default:
					LOG.debug("["+CLASS_NAME+"::parse] Elemento non riconosciuto => " + element.get("type"));// es. panel
				}	
			}
			
			
			if ( (element.has("components") && element.has("type") && (! element.get("type").asText().contains("grid"))) ||
					element.has("components") && (parent.get("type") != null && parent.get("type").asText().equalsIgnoreCase("columns"))
					) {
				 for (JsonNode c: element.get("components")) {
					 campi = parse (c, element, campi, keyPrefix); 
				   }
				
			}
			
			return campi;
		
		} catch (Exception ex) {
			LOG.error("["+CLASS_NAME+"::parse] Error parsing element: " + element.asText());
			LOG.error("["+CLASS_NAME+"::parse] Eccezione in parsing; " + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Aggiungi all'elenco de rispetta i criteri del filtro per type
	 * @param campi
	 * @param nuovoCampo
	 * @return
	 */
	private List<CampoModulo> addCampo(List<CampoModulo> campi, CampoModulo nuovoCampo) {
		if (StringUtils.isEmpty(typeFilter) || typeFilter.equals(nuovoCampo.getType())) {
			campi.add(nuovoCampo);
		}
		return campi;
	}
	
	private CampoModulo buildCampoModulo(String label, String key, String type, String keyPrefix) {
		return new CampoModulo(label,key,type,addPrefix(keyPrefix,key),getGridKey(),getGridFullKey());
	}


	private String addPrefix(String keyPrefix, String keyPrefixToAdd) {
		return (StrUtils.isEmpty(keyPrefix)?"":keyPrefix+".")+keyPrefixToAdd;
	}


	/**
	 * Legge la struttura del modulo
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static JsonNode readJsonNode(String content) throws JsonParseException, JsonMappingException, IOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::readJsonNode] IN content: "+content);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode result = objectMapper.readValue(content, JsonNode.class);
			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readJsonNode] ERROR " + e.getMessage());
		    throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readJsonNode] END");
		}
	}

}
