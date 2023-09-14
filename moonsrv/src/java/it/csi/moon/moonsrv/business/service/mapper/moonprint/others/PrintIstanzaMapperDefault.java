/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.mapper.moonprint.others;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.commons.entity.ModuloStrutturaEntity;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.IstanzaDataReader;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.MoonprintDocumentWriter;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.PrintIstanzaMapper;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Contruttore di oggetto JSON per MOOnPrint
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class PrintIstanzaMapperDefault implements PrintIstanzaMapper {

	private static final String CLASS_NAME = "PrintIstanzaMapperDefault";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	private static final int FIRST_LEVEL = 1;
	public static final String EURO = "\u20AC";
	public static final String NO_DATA_FOUND_TEXT = "Dato obbligatorio non presente";

	private static MoonprintDocumentWriter localWriter = new MoonprintDocumentWriter();
	private static IstanzaDataReader reader = new IstanzaDataReader();

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
	 * Remap l'istanza con il suo modulo  in un oggetto per MOOnPrint
	 * 
	 * @param istanza
	 * @param strutturaEntity
	 * 
	 * @return MoonprintDocument che contiene Module.Document di MOOnPrint
	 * 
	 * @throws Exception 
	 */
	public MoonprintDocument remap(Istanza istanza, ModuloStrutturaEntity strutturaEntity) throws Exception {
		localWriter.clean();
		localWriter.setTitle(istanza.getModulo().getDescrizioneModulo());

		// Metadata
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		final DateFormat dt = new SimpleDateFormat("HH:mm");

		String dataOra = df.format(istanza.getCreated()) + " alle ore " + dt.format(istanza.getCreated());
		localWriter.setMetadataDataPresentazione(dataOra);
		//localWriter.setMetadataDataPresentazione(df.format(istanza.getCreated()));
		localWriter.setMetadataNumeroIstanza(istanza.getCodiceIstanza());
		localWriter.setMetadataQrContent(istanza.getCodiceIstanza());
		localWriter.setMetadataHeader("%%title%%", "", "Richiesta il: %%dataPresentazione%%");
		localWriter.setMetadataFooter("Istanza n.: %%numeroIstanza%%", null, "");

		// Lettura delle due strutture variabile
		JsonNode istanzaJsonNode = readIstanzaData(istanza);
		JsonNode moduloJsonNode = readStrutturaModulo(strutturaEntity);
		//		System.out.println("istanza="+istanza);
		//		System.out.println("istanzaJsonNode="+istanzaJsonNode);

		// renderNode(moduloJsonNode.get("components"), FIRST_LEVEL, istanzaJsonNode.get("data"));
		parse(moduloJsonNode.get("components"), istanzaJsonNode.get("data"),null,null);
		return localWriter.write();
	}

	private static boolean hasCustomConditional(JsonNode element) {
		if (element.has("customConditional")) {
			String condizione = element.get("customConditional").asText();
			return !condizione.isBlank();
		} else 
			return false;
	}

	private static boolean isValidConditional(JsonNode element) {

		if (element.has("conditional")) {

			JsonNode cond = element.get("conditional");

			String key = cond.get("when").asText();
			String eq = cond.get("eq").asText();
			boolean show = cond.get("show").asBoolean();
			// String json = cond.get("json").asText();

			if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(eq) && show) {
				return true;
			} else {
				return false;
			}

		} else
			return false;
	}

	private static boolean isVisibleByConditional(JsonNode element, JsonNode dati) {

		// if (element.has("conditional")) {
		if (isValidConditional(element)) {
			JsonNode cond = element.get("conditional");
			String key = cond.get("when").asText();
			//String val = hasChildNode(dati, key) ? getChildNode(dati, key).toString() : "";
			String val =  hasChildNode(dati, key) ? (getChildNode(dati, key).isTextual() ? getChildNode(dati, key).asText() : getChildNode(dati, key).toString() ) :"";
			String eq = cond.get("eq").asText();
			boolean show = cond.get("show").asBoolean();
			if (StringUtils.isNotBlank(eq)) {
				if (eq.equals(val)) {
					return show;
				} else {
					return !show;
				}
			} else {
				return false;
			}
		} else if(hasCustomConditional(element)) {
			if(element.has("key")/*&& !(element.has("type") && element.get("type").asText().toUpperCase().equals("FILE"))*/) {
				String key=  element.get("key").asText();
				return (hasChildNode(dati, key) && getChildNode(dati, key)!=null &&  !getChildNode(dati, key).toString().equals("{}")? true:false);
			}
			return true;
		}
		else
			return true;
	}

	private static boolean isInData(JsonNode node, JsonNode dataNode) {
		if(node.has("key")) {
			String key=  node.get("key").asText();

			return (hasChildNode(dataNode, key) && dataNode.get(key)!=null &&  !dataNode.get(key).toString().equals("{}")? true:false);
		}
		return true;
	}

	private static boolean showLabel(JsonNode element) {
		if (element.has("hideLabel")) {
			return !element.get("hideLabel").asBoolean();
		} else {
			return true;
		}
	}

	private static String getPrefix(JsonNode element) {
		String prefix = "";
		if (element.has("prefix")) {
			prefix = element.get("prefix").asText();
		}
		return prefix;
	}

	private static String getCurrency(JsonNode element) {
		String prefix = "";
		if (element.has("currency")) {
			prefix = element.get("currency").asText();
		}
		return prefix;
	}

	private static boolean isEuroCurrency(JsonNode element, String value) {
		boolean isCurrency = false;
		String prefix = getPrefix(element);
		String decimalLimit = "";

		if (element.has("decimalLimit")) {
			decimalLimit = element.get("decimalLimit").toString();
		}
		if (prefix.equals(EURO) && StringUtils.isNotBlank(decimalLimit))
			isCurrency = true;
		return isCurrency;
	}

	private static String getEuroCurrencyValue(JsonNode element, String value) {
		NumberFormat nf = NumberFormat.getInstance(new Locale("it", "IT"));
		nf.setMinimumFractionDigits(element.get("decimalLimit").asInt());
		nf.setMaximumFractionDigits(element.get("decimalLimit").asInt());
		Double importo = Double.valueOf(value);
		return nf.format(importo);
	}

	private static String getCurrencyValue(JsonNode element, String value) {
		String currency = getCurrency(element);
		if (StringUtils.isNotBlank(currency)) {
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			Currency cur = Currency.getInstance(currency);
			nf.setCurrency(cur);
			Double importo = Double.valueOf(value);
			return nf.format(importo);
		} else {
			return value;
		}
	}

	private static String getLabel(JsonNode element) {
		if (element.has("label") && showLabel(element) && StringUtils.isNotBlank(element.get("label").asText())) {
			return element.get("label").asText();
		} else {
			if (element.has("content") && StringUtils.isNotBlank(element.get("content").asText())) {
				return element.get("content").asText().replaceAll("<[^>]*>", "");
			} else {
				return "";
			}
		}
	}

	private static String getNameTemplate(JsonNode element) {
		if (element.has("template")) {
			String template = element.get("template").asText().replaceAll("<[^>]*>", "").replaceAll("[{}]", "")
					.trim();
			String[] items = StringUtils.split(template, "\\.");
			//TODO da testare bene MARTEDì
			if (items.length == 1) {
				return items[0].replace(" ","").equals("item")? "singoloItem":"label";
			}
			else if (items.length == 2) {
				return items[1];
			} else if (items.length == 3){
				String labelLeft = items[1].replace("item","").trim();
				return "{labelLeft:"+ labelLeft+", labelRight:"+items[2]+"}";
			}
		}
		return "label";
	}

	private static String getContent(JsonNode element) {
		if (element.has("content") && StringUtils.isNotBlank(element.get("content").asText())) {
			return element.get("content").asText().replaceAll("<[^>]*>", "").replace("&nbsp;", "");
		} else {
			return "";
		}
	}

	private static String getHtml(JsonNode element) {
		if (element.has("html") && StringUtils.isNotBlank(element.get("html").asText())) {
			return element.get("html").asText().replaceAll("<[^>]*>", "").replace("&nbsp;", "");
		} else {
			return "";
		}
	}

	private static boolean isRequired(JsonNode element) {
		if (element.has("validate")) {
			return element.get("validate").get("required").asBoolean();
		} else {
			return false;
		}
	}

	private static void write(String type, JsonNode element, JsonNode dati) {
		if (!(element.get("hidden") != null && element.get("hidden").asBoolean())) {
			if (isVisibleByConditional(element, dati)) {
				String label = getLabel(element);
				String value = generateValueToPrint(element, dati);
				//TODO verificare e approvare && or ||
				if (StringUtils.isNotBlank(label) && StringUtils.isNotBlank(value)) {
					localWriter.addItem(label, value);
				}				
				//localWriter.addItem(getLabel(element), generateValueToPrint(element, dati));
			}
		}
	}

	private static JsonNode getChildNode(JsonNode target, String key) {
		if (key.contains(".")) {
			String[] fullPath = key.split("\\.");
			String[] cutPath = Arrays.copyOfRange(fullPath, 1, fullPath.length);
			key = String.join(".", cutPath);
			return getChildNode(target.get(fullPath[0]), key);
		} else {
			return target.get(key);
		}
	}

	private static boolean hasChildNode(JsonNode target, String key) {
		if (key.contains(".")) {
			String[] fullPath = key.split("\\.");
			String[] path = Arrays.copyOfRange(fullPath, 1, fullPath.length);
			key = String.join(".", path);
			return (target != null) ? hasChildNode(target.get(fullPath[0]), key) : false;
		} else {
			return (target != null) ? (target.get(key) != null ? true : false) : false;
		}
	}

	private static JsonNode getdNodeWithPrefix(JsonNode element, JsonNode parent, String keyPrefix) {
		if (keyPrefix != null && (!FormIoLayoutType.isLayoutType(element))) {
			((ObjectNode) element).put("keyPrefix", keyPrefix);
		}
		return element;
	}

	//	private static boolean isValidElement(JsonNode element) {
	//
	//		return element.has("key") &&  element.has("type");
	//
	//	}	
	private static boolean isPropertyHiddenPdf(JsonNode element) {
		if(element.has("properties")) {
			if (element.get("properties").isObject() && element.get("properties").has("hiddenPdf")) {
				return element.get("properties").get("hiddenPdf").asBoolean();
			}
		}
		return false;
	}		

	public static void parse(JsonNode element, JsonNode dati, JsonNode parent, String keyPrefix) throws Exception {
		boolean esegui=true;
		if(hasCustomConditional(element)) {
			if(element.has("type") && element.get("type").asText().equals("panel"))
				esegui=isVisibleByConditional(element, dati);
			/*else {
				esegui=!isEmptyInData(element, dati);
			}*/
		}
		if(esegui)
			try {
				if (element.isArray()) {
					//				for (Iterator<JsonNode> it = element.iterator(); it.hasNext();) {
					//					parse(it.next(), dati, element);
					//				}
					ArrayNode arrayNode = (ArrayNode) element;
					for (int i = 0; i < arrayNode.size(); i++) {
						JsonNode arrayElement = arrayNode.get(i);						         
						parse(arrayElement, dati, element,keyPrefix);
					}
				} else if (element.isObject()) {
					LOG.debug("[" + CLASS_NAME + "::parse] : " + "Type: "
							+ (element.has("type") ? element.get("type").asText() : "NO TYPE") + " Label: "
							+ (element.has("label") ? element.get("label").asText() : "NO LABEL") + " Key: "
							+ (element.has("key") ? element.get("key").asText() : "NO KEY"));
					String type = element.has("type") ? element.get("type").asText() : "NO TYPE";
					//TEST
					String elemkey = element.has("key") ? element.get("key").asText() : "NO KEY";
					if(elemkey.equals("SCUOLAATTUALE_CODICE")){
						LOG.debug("[" + CLASS_NAME + "::parse] : something");
					}
					switch (FormIoType.byNode(element)) {
					case PANEL:
						if (!(element.get("hidden") != null && element.get("hidden").asBoolean())) {
							String parentType = parent.has("type") ? parent.get("type").asText() : "NO TYPE";
							switch (FormIoType.byNode(parent)) {
							case PANEL:	
								localWriter.addSubSection(element.get("title").asText(), "");
								break;
							case EDITGRID:
								localWriter.addSubSection(element.get("title").asText(), "");
								break;
							case DATAGRID:
								localWriter.addSubSection(element.get("title").asText(), "");
								break;
							case NO_TYPE:	
								localWriter.addSection(element.get("title").asText(), "");
								break;
							default:
								localWriter.addSection(element.get("title").asText(), "");
							}
						}
						break;
					case TEXTAREA:
					case DATETIME:
					case URL:
					case PHONENUMBER:
					case EMAIL:
					case DAY:
					case CHECKBOX:
					case RADIO:
					case SELECT:
					case TEXTFIELD:
					case TIME:
						element = getdNodeWithPrefix(element, parent, keyPrefix);
						write(type, element, dati);
						break;
					case COLUMNS:
						for (JsonNode c : element.get("columns")) {
							parse(c, dati, element,keyPrefix);
						}
						break;
					case TABLE:
						for (JsonNode c : element.get("rows")) {						
							if (c.isArray()) {
								ArrayNode arrayNode = (ArrayNode) c;
								for (int i = 0; i < arrayNode.size(); i++) {
									JsonNode arrayElement = arrayNode.get(i);						         
									parse(arrayElement, dati, element,keyPrefix);
								}
							}
						}
						break;	
					case EDITGRID:
					case DATAGRID:
						if (hasChildNode(dati,element.get("key").asText())){
							JsonNode datiTabellaNode = getChildNode(dati,element.get("key").asText());
							if (datiTabellaNode != null && datiTabellaNode.isArray()) {
								ArrayNode datiTabella = (ArrayNode) getChildNode(dati,element.get("key").asText());
								int index = 1;						
								for (JsonNode rigaDatiTabella : datiTabellaNode) {
									localWriter.addSubSection(""+index++, "");
									for (JsonNode c : element.get("components")) {															
										parse(c, rigaDatiTabella, element,keyPrefix);
									}
								}						
								localWriter.addSubSection("", "");
							}
						}
						break;
					case SELECTBOXES:
						if (!(element.get("hidden") != null && element.get("hidden").asBoolean())) {
							if (isVisibleByConditional(element, dati)) {
								String key = element.get("key").asText();
								if (hasChildNode(dati, key)) {
									JsonNode nodeBoxes = getChildNode(dati, key);
									if (element.has("values") && element.get("values").isArray()) {
										Iterator<JsonNode> lst = element.get("values").iterator();
										StringBuilder sb = new StringBuilder();
										while (lst.hasNext()) {
											JsonNode val = lst.next();
											boolean hasValue = (val.get("value") != null)
													? (StringUtils.isNotBlank(val.get("value").asText())
															? nodeBoxes.get(val.get("value").asText()).asBoolean()
																	: false)
															: false;
															localWriter.addItem(val.get("label").asText(), hasValue ? "SI" : "NO");
										}
									} else {
										if (isRequired(element)) {
											localWriter.addItem(element.get("label").asText(), NO_DATA_FOUND_TEXT);
										} else {
											localWriter.addItem(element.get("label").asText(), "");
										}
									}
								}
							}
						}
						break;
					case CURRENCY:
						if (!(element.get("hidden") != null && element.get("hidden").asBoolean())) {
							if (isVisibleByConditional(element, dati)) {
								localWriter.addItem(getLabel(element).concat(" ").concat(getCurrency(element)),
										generateValueToPrint(element, dati));
							}
						}
						break;
					case NUMBER:
						if (!(element.get("hidden") != null && element.get("hidden").asBoolean())) {
							if (isVisibleByConditional(element, dati)) {
								localWriter.addItem(getLabel(element).concat(" ").concat(getPrefix(element)),
										generateValueToPrint(element, dati));
							}
						}
						break;
					case HTMLELEMENT:
						if (!(element.get("hidden") != null && element.get("hidden").asBoolean())) {
							if (isVisibleByConditional(element, dati)) {
								localWriter.addItem(getContent(element), "");
							}
						}
						break;
					case CONTENT:
						if (!(element.get("hidden") != null && element.get("hidden").asBoolean())) {
							if (isVisibleByConditional(element, dati)) {							
								if (!isPropertyHiddenPdf(element)) {
									localWriter.addSubSection("", "");
									localWriter.addItem(getHtml(element), "");
								}
							}
						}
						break;
					case FILE:
						if (!(element.get("hidden") != null && element.get("hidden").asBoolean())) {
							if (isVisibleByConditional(element, dati)) {
								localWriter.addItem(getLabel(element), generateValueToPrint(element, dati));
							}
						}
						break;
					case CONTAINER:
						if (!(element.get("hidden") != null && element.get("hidden").asBoolean())) {
							if (isVisibleByConditional(element, dati) && showLabel(element)) {
								localWriter.addSection(element.get("label").asText(), "");
							}
							keyPrefix =  element.get("key").asText() + '.';
							//						for (JsonNode c : element.get("components")) {
							//							parse(c, dati, element,keyPrefix);
							//						}
						}
						break;
					case TABS:
						LOG.debug("[" + CLASS_NAME + "::parse] :tabs");
						break;					
					case NO_TYPE:
						LOG.debug("[" + CLASS_NAME + "::parse] : no type");
						break;
					default:
						LOG.debug("[" + CLASS_NAME + "::parse] : Elemento non riconosciuto => " + element.get("type"));
					}
				}
				if ((element.has("components") && element.has("type")
						&& (!element.get("type").asText().equals("datagrid")) && (!element.get("type").asText().equals("editgrid")) && (!element.get("type").asText().equals("table"))
						/*&& (!element.get("type").asText().equals("container"))*/)
						|| element.has("components") && (parent.get("type") != null && (parent.get("type").asText().equalsIgnoreCase("columns")) || parent.get("type").asText().equalsIgnoreCase("table"))
						|| (parent != null && parent.has("type") && parent.get("type").asText().equalsIgnoreCase("tabs"))) {
					for (JsonNode c : element.get("components")) {
						parse(c, dati, element,keyPrefix);
					}
				}
				return;
			} catch (Exception ex) {
				LOG.error("[" + CLASS_NAME + "::parse] :Error parsing element key: "+ (element.has("key") ? element.get("key").asText() : "NO KEY") +" type: "+(element.has("type") ? element.get("type").asText() : "NO TYPE"));			
				LOG.error("[" + CLASS_NAME + "::parse] :Eccezione in parsing;"+ ex.getMessage());
				throw (ex);
			}
	}

	private static String getValueToPrint(String key, JsonNode el, JsonNode dati) {
		if (hasChildNode(dati, key)) {
			return getChildNode(dati, key).asText();
		} else {
			//			if (isRequired(el)) {
			//				return NO_DATA_FOUND_TEXT;
			//			} else {
			//				return "";
			//			}
			return getValueNoChildNode(el);
		}
	}

	private static String getValueNoChildNode(JsonNode el) {
		//		if (isRequired(el)) {
		//			return NO_DATA_FOUND_TEXT;
		//		} else {
		//			return "";
		//		}
		return "";
	}

	private static String generateValueToPrint(JsonNode el, JsonNode dati) {
		if (!el.has("key"))
			return "NO KEY FOR ELEMENT" + el.asText();

		//		String key = el.get("key").asText();
		String key = "";		
		if (el.has("keyPrefix") && StringUtils.isNotBlank(el.get("keyPrefix").asText()))
		{
			key = el.get("keyPrefix").asText()+el.get("key").asText();
		}
		else {			
			key = el.get("key").asText();
		}
		if (el.has("type")) {
			switch (FormIoType.byNode(el)) {
			case TEXTAREA:
			case DATETIME:
			case URL:
			case PHONENUMBER:
			case EMAIL:
			case DAY:
			case TEXTFIELD:
			case TIME:
				return getValueToPrint(key, el, dati);
			case CURRENCY:
				if (hasChildNode(dati, key)) {
					String value = getChildNode(dati, key).toString();
					return getCurrencyValue(el, value);
				} else {
					//					if (isRequired(el)) {
					//						return NO_DATA_FOUND_TEXT;
					//					} else {
					//						return "";
					//					}
					return getValueNoChildNode(el);
				}
			case NUMBER:
				if (hasChildNode(dati, key)) {
					String value = getChildNode(dati, key).toString();

					if (isEuroCurrency(el, value))
						return getEuroCurrencyValue(el, value);
					return getChildNode(dati, key).toString();
				} else {
					//					if (isRequired(el)) {
					//						return NO_DATA_FOUND_TEXT;
					//					} else {
					//						return "";
					//					}
					return getValueNoChildNode(el);
				}
			case CHECKBOX:
				if (hasChildNode(dati, key)) {
					if (getChildNode(dati, key).isBoolean()) {
						return getChildNode(dati, key).asBoolean() ? "SI" : "NO";
					} else {
						return getChildNode(dati, key).asText();
					}

				} else {
					//					if (isRequired(el)) {
					//						return NO_DATA_FOUND_TEXT;
					//					} else {
					//						return "";
					//					}
					return getValueNoChildNode(el);
				}
			case RADIO:
				if (hasChildNode(dati, key)) {
					String value = "";
					String label = "";
					if (getChildNode(dati, key).isBoolean()) {
						return getChildNode(dati, key).asBoolean() ? "SI" : "NO";
					} else {
						value = getChildNode(dati, key).asText();
						if (el.has("values") && el.get("values").isArray()) {
							Iterator<JsonNode> lst = el.get("values").iterator();
							StringBuilder sb = new StringBuilder();
							while (lst.hasNext()) {
								JsonNode val = lst.next();
								if (val.get("value").asText().equals(value)) {
									// label = val.get("label").asText();
									return val.get("label").asText();
								}
							}
						} else {
							return value;
						}
					}
				} else {
					//					if (isRequired(el)) {
					//						return NO_DATA_FOUND_TEXT;
					//					} else {
					//						return "";
					//					}
					return getValueNoChildNode(el);
				}
				break;
			case SELECT:
				if (hasChildNode(dati, key)) {
					String value = "";
					String label = "";
					String itemName = getNameTemplate(el);
					//TODO da testare bene MARTEDì
					if(itemName.equals("singoloItem")) {
						return getChildNode(dati, key).asText();
					}
					//					if ( getChildNode(dati,key).has("nome") &&  getChildNode(dati,key).get("nome").isTextual()) {
					else if (getChildNode(dati, key).has(itemName) && getChildNode(dati, key).get(itemName).isTextual()) {
						return getChildNode(dati, key).get(itemName).asText();
					} else {
						if (getChildNode(dati, key) != null) {
							if (getChildNode(dati, key).isTextual()) {
								value = getChildNode(dati, key).asText();
							} else
								value = getChildNode(dati, key).toString();
							if (getChildNode(dati, key).isObject()) {
								if (el.has("data") && el.get("data").has("json")
										&& el.get("data").get("json").isArray()) {
									Iterator<JsonNode> lst = el.get("data").get("json").iterator();
									StringBuilder sb = new StringBuilder();
									JsonParser parser = new JsonParser();	
									JsonObject itemObj = (JsonObject) parser.parse(itemName);
									JsonObject valObj = (JsonObject) parser.parse(value);
									while (lst.hasNext()) {
										JsonNode val = lst.next();
										String keyCode= itemObj.get("labelLeft").getAsString();
										String keyLabel= itemObj.get("labelRight").getAsString();

										if (val.get(keyCode).asText().equals(valObj.get(keyCode).getAsString()) &&
												val.get(keyLabel).asText().equals(valObj.get(keyLabel).getAsString())
												) {
											label = val.get(keyLabel).asText();
										}
									}
									return label;
								} else {
									//									if (isRequired(el)) {
									//										return NO_DATA_FOUND_TEXT;
									//									} else {
									//										return "";
									//									}
									return getValueNoChildNode(el);
								}
							} else {
								if (StringUtils.isNotBlank(value)) {
									if (el.has("data") && el.get("data").has("values")
											&& el.get("data").get("values").isArray() 
											&& ((Iterator<JsonNode>)el.get("data").get("values").iterator()).hasNext()) {
										Iterator<JsonNode> lst = el.get("data").get("values").iterator();
										StringBuilder sb = new StringBuilder();
										while (lst.hasNext()) {
											JsonNode val = lst.next();
											if (val.get("value").asText().equals(value)) {
												//											return label = val.get("label").asText();																																	
												return label = (val.get(itemName) != null)
														? val.get(itemName).asText()
																: "";
											}
										}
									}
									else if (el.has("data") && el.get("data").has("json")
											&& el.get("data").get("json").isArray() 
											&& ((Iterator<JsonNode>)el.get("data").get("json").iterator()).hasNext()) {
										itemName=itemName.replace(" -", "");										
										StringBuilder sb = new StringBuilder();
										JsonParser parser = new JsonParser();	
										JsonObject itemObj = (JsonObject) parser.parse(itemName);
										//JsonObject valObj = (JsonObject) parser.parse(value);
										if (value.startsWith("[")) {
											Iterator<JsonNode> valLst = getChildNode(dati, key).iterator();
											while (valLst.hasNext()) {
												JsonNode valIter = valLst.next();
												if (valIter.isTextual()) {
													value = valIter.asText();
												} else
													value = valIter.toString();
												JsonObject valObj = (JsonObject) parser.parse(value);
												Iterator<JsonNode> lst = el.get("data").get("json").iterator();
												while (lst.hasNext()) {
													JsonNode val = lst.next();
													String keyCode= itemObj.get("labelLeft").getAsString();
													String keyLabel= itemObj.get("labelRight").getAsString();
													if (val.get(keyCode).asText().equals(valObj.get(keyCode).getAsString()) &&
															val.get(keyLabel).asText().equals(valObj.get(keyLabel).getAsString())
															) {
														label += val.get(keyLabel).asText();
													}
												}
												if (valLst.hasNext())
													label += "; ";
											}
										}
										return label;
									}
									else {
										//											if (isRequired(el)) {
										//												return NO_DATA_FOUND_TEXT;
										//											} else {
										//												return "";
										//											}
										return getValueNoChildNode(el);
									}

								} else {
									return value;
								}
							}
						} else 
							/*	if (isRequired(el)) {
							return NO_DATA_FOUND_TEXT;
						} else {
							return "";
						}
							 */
							return getValueNoChildNode(el);
					}
				} else return getValueNoChildNode(el);
				break;
			case FILE:
				if (hasChildNode(dati, key)) {
					JsonNode node = getChildNode(dati, key);
					if (node.isArray()) {
						Iterator<JsonNode> it = node.iterator();
						StringBuilder sb = new StringBuilder();
						while (it.hasNext()) {
							JsonNode file = it.next();
							sb.append(file.get("originalName").asText()).append("\n");
						}
						return sb.toString();
					}
				}else return getValueNoChildNode(el);
				break;
			default:
				return "NO DATA FOUND FOR KEY " + key + " type " + el.get("type").asText();
			}
		}
		return "NO TYPE FOR KEY " + key;
	}



	/**
	 * Legge i dati dell istanza in nell oggetto istanzaJsonNode
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	private static JsonNode readIstanzaData(Istanza istanza) throws Exception {
		try {
			LOG.debug("[" + CLASS_NAME + "::readIstanzaData] IN istanza: "+istanza);
			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JsonNode result = objectMapper.readValue(istanza.getData().toString(), JsonNode.class);
			return result;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::readIstanzaData] ERROR "+e.getMessage());
			throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readIstanzaData] END");
		}
	}


	/**
	 * Legge la struttura del modulo
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static JsonNode readStrutturaModulo(ModuloStrutturaEntity strutturaEntity) throws JsonParseException, JsonMappingException, IOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::readStrutturaModulo] IN strutturaEntity: "+strutturaEntity);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode result = objectMapper.readValue(strutturaEntity.getStruttura(), JsonNode.class);
			return result;
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::readStrutturaModulo] ERROR "+e.getMessage());
			throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readStrutturaModulo] END");
		}
	}


	public static void renderNode(JsonNode root, int level, JsonNode dataNode) {
		//		System.out.println("--------------------------------");
		//		System.out.println(root);
		//		System.out.println("json=" + root);
		//		System.out.println("json.isText=" + root.isTextual());
		//		System.out.println("json.isArray=" + root.isArray());
		//		System.out.println("json.isNull=" + root.isNull());
		//		System.out.println("json=" + root.asText());
		//		System.out.println("--------------------------------");
		if (root.isObject()) {
			renderObjectNode(root, level, dataNode);
		} else if(root.isArray()) {
			ArrayNode arrayNode = (ArrayNode) root;
			for (int i = 0; i < arrayNode.size(); i++) {
				JsonNode arrayElement = arrayNode.get(i);
				renderNode(arrayElement, level, dataNode);
			}
		} else {
			//System.out.println("["+CLASS_NAME+"::renderNode] *** NOT IMPLEMENTED NEW JsonNode root = " + root);
			LOG.debug("["+CLASS_NAME+"::renderNode] *** NOT IMPLEMENTED NEW JsonNode root = " + root);
		}
	}

	private static enum FormIoType {
		PANEL,
		TEXTFIELD,
		RADIO,
		CHECKBOX,
		FILE,
		SELECT,
		NUMBER,
		DATAGRID,
		EDITGRID,
		DAY,
		TEXTAREA,
		PHONENUMBER,
		EMAIL,
		HTMLELEMENT,
		BUTTON,
		DATETIME,
		URL,
		CURRENCY,
		CONTENT,
		SELECTBOXES,
		COLUMNS,
		CONTAINER,
		TABLE,
		TABS,
		TIME,
		NO_TYPE;


		public static FormIoType byNode(JsonNode node) {
			if (node.isObject()) {
				for (FormIoType t : values()) {
					if (node.has("type")) {
						if (node.get("type").asText().toUpperCase().equals(t.name())) {
							return t;
						}
					}
				}
			} 
			//			else {
			//				System.out.println("ERROR NEW Enum FormIoType :: node is not an Object  ! " + node);
			//			}
			return NO_TYPE;
		}
	}

	private static enum FormIoLayoutType {
		PANEL,
		DATAGRID,
		EDITGRID,
		COLUMNS,
		CONTAINER,
		TABLE,
		TABS;

		public static boolean isLayoutType(JsonNode node) {
			if (node.isObject()) {
				for (FormIoLayoutType t : values()) {
					if (node.has("type")) {
						if (node.get("type").asText().toUpperCase().equals(t.name())) {
							return true;
						}
					}
				}
			} 
			return false;
		}
	}



	private static void renderObjectNode(JsonNode node, int level, JsonNode dataNode) {
		if (node==null || node.get("type")==null) {
			return;
		}
		if (node.get("hidden")!=null && node.get("hidden").asBoolean()) {
			LOG.debug("#HIDE#" + node.get("type")+"\t\t label:" + node.get("label")+" \t\tkey=" + node.get("key"));
			return;
		}
		if (!validateNode(node, dataNode)) {
			LOG.debug("#UNVALID#" + node.get("type")+"\t\t label:" + node.get("label")+" \t\tkey=" + node.get("key"));
			return;
		}
		switch( FormIoType.byNode(node) ) {
		case PANEL:
			LOG.debug("");
			LOG.debug("NEW SECTION  title  ::  " + node.get("title"));
			LOG.debug("=======================================================================================");
			if (level==FIRST_LEVEL) {
				localWriter.addSection(node.get("title").asText(),"");
			} else {
				localWriter.addSubSection(node.get("title").asText(),"");
			}
			renderNode(node.get("components"), level+1, dataNode);
			break;
		case TEXTFIELD:
		case NUMBER:
		case CHECKBOX:
		case RADIO:
		case DAY:
		case TIME:
		case TEXTAREA:
		case PHONENUMBER:
		case EMAIL:
		case HTMLELEMENT:
			LOG.debug(FormIoType.byNode(node).name() + "\t\t label:" + node.get("label")+"\t\t ***key***=" + node.get("key")+"  \t\t\tvalue=" + reader.getIstanzaDataOf(node, dataNode));
			//TODO verificare e approvare
			//if (isInData(node,dataNode))
			localWriter.addItem(node.get("label").asText(), reader.getIstanzaDataOf(node, dataNode));
			break;
		case FILE:
			LOG.debug(FormIoType.byNode(node).name() + "\t\t label:" + node.get("label")+"\t\t ***key***=" + node.get("key")+"  \t\t\tvalue=" + reader.getIstanzaDataOfFile(node, dataNode));
			localWriter.addItem(node.get("label").asText(), reader.getIstanzaDataOfFile(node, dataNode));
			break;
		case EDITGRID:
		case DATAGRID:
			LOG.debug(FormIoType.byNode(node).name() + "\t\t label:" + node.get("label")+"\t\t ***key***=" + node.get("key"));
			renderDatagrid(node, level, dataNode);
			break;
		case SELECT:
			LOG.debug(FormIoType.byNode(node).name() + "\t\t label:" + node.get("label")+"\t\t ***key***=" + node.get("key")+"  \t\t\tvalue=" + reader.getIstanzaDataOfSelect(node, dataNode));
			break;
		case BUTTON:
			break;
		case COLUMNS:
			renderColumns(node, level, dataNode);
			break;
		default:
			LOG.debug("type  =" + node.get("type"));
			LOG.debug("inputType=" + node.get("inputType"));
			LOG.debug("label =" + node.get("label"));
			LOG.debug("key   =" + node.get("key"));
			LOG.debug("title =" + node.get("title"));
			LOG.debug("input =" + node.get("input"));
			LOG.debug("hidden=" + node.get("hidden"));
		}
	}


	private static void renderColumns(JsonNode nodeColumns, int level, JsonNode dataNode) {
		//		System.out.println("=======================================================================================");
		//		System.out.println("*** renderColumns BEGIN *** \t\t label:" + nodeColumns.get("label")+"\t\t ***key***=" + nodeColumns.get("key"));
		//		System.out.println("=======================================================================================");
		//		System.out.println("type  =" + nodeColumns.get("type"));
		//		System.out.println("inputType=" + nodeColumns.get("inputType"));
		//		System.out.println("label =" + nodeColumns.get("label"));
		//		System.out.println("key   =" + nodeColumns.get("key"));
		//		System.out.println("title =" + nodeColumns.get("title"));
		//		System.out.println("input =" + nodeColumns.get("input"));
		//		System.out.println("hidden=" + nodeColumns.get("hidden"));
		//		System.out.println("conditional=" + nodeColumns.get("conditional"));

		if("true".equals(nodeColumns.get("hidden").asText())) {
			return;
		}

		ArrayNode columns = (ArrayNode) nodeColumns.get("columns");
		for (int j = 0; j < columns.size(); j++) {
			JsonNode columnNode = columns.get(j);	
			ArrayNode columnElements = (ArrayNode) columnNode.get("components");
			for (int i = 0; i < columnElements.size(); i++) {
				JsonNode element = columnElements.get(i);
				renderObjectNode(element, level, dataNode);
			}
		}
		//		System.out.println("==========================");
		//		System.out.println("*** renderColumns END ***");
		//		System.out.println("==========================");	
	}




	private static void renderDatagrid(JsonNode nodeRootDatagrid, int level, JsonNode dataNode) {
		//		System.out.println("=======================================================================================");
		//		System.out.println("*** renderDatagrid BEGIN *** \t\t label:" + nodeRootDatagrid.get("label")+"\t\t ***key***=" + nodeRootDatagrid.get("key"));
		//		System.out.println("=======================================================================================");
		//		System.out.println("type  =" + nodeRootDatagrid.get("type"));
		//		System.out.println("inputType=" + nodeRootDatagrid.get("inputType"));
		//		System.out.println("label =" + nodeRootDatagrid.get("label"));
		//		System.out.println("key   =" + nodeRootDatagrid.get("key"));
		//		System.out.println("title =" + nodeRootDatagrid.get("title"));
		//		System.out.println("input =" + nodeRootDatagrid.get("input"));
		//		System.out.println("hidden=" + nodeRootDatagrid.get("hidden"));
		//		System.out.println("conditional=" + nodeRootDatagrid.get("conditional"));

		if(nodeRootDatagrid.get("hidden")!=null && "true".equals(nodeRootDatagrid.get("hidden").asText())) {
			return;
		}
		//		System.out.println("------------------------");
		ArrayNode columnsDatagrid = (ArrayNode) nodeRootDatagrid.get("components");
		if(columnsDatagrid.isArray()) {
			// Recupero dati inseriti nell'istanze (se presente)
			ArrayNode righeDatiInserite = reader.getIstanzaDataArrayOf(nodeRootDatagrid, dataNode);
			for (int i = 0; i < righeDatiInserite.size(); i++) {
				JsonNode rigaDatiInserite = righeDatiInserite.get(i);
				// Render Rows of istanza data => creare solo la subsection vuota
				//	        	System.out.println("Render Rows of istanza data => in subsection ...");
				System.out.println("rigaDatiInserite="+rigaDatiInserite);
				localWriter.addSubSection("", "");
				for (int j = 0; j < columnsDatagrid.size(); j++) {
					// 
					JsonNode column = columnsDatagrid.get(j);	            
					//					System.out.println("type  =" + column.get("type"));
					//					System.out.println("inputType=" + column.get("inputType"));
					//					System.out.println("label =" + column.get("label"));
					//					System.out.println("key   =" + column.get("key"));
					//					System.out.println("title =" + column.get("title"));
					//					System.out.println("input =" + column.get("input"));
					//					System.out.println("hidden=" + column.get("hidden"));
					//					System.out.println("conditional=" + column.get("conditional"));
					// Render Column => in rows
					//					System.out.println("Render Column  " + column.get("label") + "  => in rows ...");
					renderObjectNode(column, level, rigaDatiInserite);
					//					System.out.println("------------------------");
				}
			} // Loop sui dati inserite, presente nell'istanza
		}
		//		System.out.println("==========================");
		//		System.out.println("*** renderDatagrid END ***");
		//		System.out.println("==========================");		
	}


	private static boolean validateNode(JsonNode node, JsonNode dataNode) {
		if (node == null || node.get("conditional")==null || node.get("conditional").get("eq")==null || node.get("conditional").get("eq").asText().isEmpty()) {
			return true;
		}
		return validateConditional(node.get("conditional"), dataNode);
	}

	private static boolean validateConditional(JsonNode conditionalNode, JsonNode dataNode) {
		//		System.out.println("validateConditional ::  eq=" + conditionalNode.get("eq") + "  when="+conditionalNode.get("when") + "  show="+conditionalNode.get("show"));
		String key = conditionalNode.get("when").asText();
		String val = reader.getIstanzaData(key, dataNode);
		//		System.out.println("validateConditional ::  "+key+"="+val);
		boolean show = conditionalNode.get("show").asBoolean();
		if (conditionalNode.get("eq")!=null) {
			if (!(show && conditionalNode.get("eq").asText().equals(val))) {
				//				System.out.println("UNVALIDATE !!");
				return false;
				//				throw new ConditionalNodeException("UNVALIDATE");
			} else {
				//				System.out.println("Validate.");
			}

		} else {
			System.out.println("*** NOT IMPLEMENTED NEW conditionalNode :: " + conditionalNode);
		}
		return true;
	}

	public static MoonprintDocument remapEstaRaga2(Istanza istanza, ModuloStrutturaEntity strutturaEntity) throws Exception {
		final String DEFAULT_VALUE = "";
		MoonprintDocumentWriter localWriter = new MoonprintDocumentWriter();
		// TODO Auto-generated method stub
		localWriter.setTitle(istanza.getModulo().getOggettoModulo());

		// Metadata
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		localWriter.setMetadataDataPresentazione(df.format(istanza.getCreated()));
		localWriter.setMetadataNumeroIstanza(istanza.getCodiceIstanza());
		localWriter.setMetadataQrContent(istanza.getCodiceIstanza());
		localWriter.setMetadataHeader("%%title%%", "", "Inviata il: %%dataPresentazione%%");
		localWriter.setMetadataFooter("Istanza n.: %%numeroIstanza%%", null, null);

		// Lettura delle due strutture variabile
		JsonNode istanzaJsonNode = readIstanzaData(istanza);
		JsonNode data = istanzaJsonNode.get("data");
		localWriter.addSection("Dati compilante", "");
		/* COMPILANTE */
		localWriter.addSubSection("Compilante", "");
		localWriter.addItem("Nome", data.has("nome") ? data.get("nome").asText() : DEFAULT_VALUE);
		localWriter.addItem("Cognome", data.has("cognome") ? data.get("cognome").asText() : DEFAULT_VALUE);
		localWriter.addItem("Codice Fiscale", data.has("codiceFiscale") ? data.get("codiceFiscale").asText() : DEFAULT_VALUE );
		localWriter.addItem("Email", data.has("email") ? data.get("email").asText() : DEFAULT_VALUE);
		localWriter.addItem("Cellulare", data.has("cellulare2") ? data.get("cellulare2").asText() : DEFAULT_VALUE);
		localWriter.addSubSection("Altri dati di contatto", "");
		localWriter.addItem("Email", data.has("email1") ? data.get("email1").asText() : DEFAULT_VALUE);
		localWriter.addItem("Cellulare", data.has("cellulare1") ? data.get("cellulare1").asText()  : DEFAULT_VALUE);
		// Sezione Dati Bambino
		/*
		 * Se bambinoNonPresenteInElenco true leggo i dati da
		 * nome1, cognome1, codicefiscale1
		 */
		localWriter.addSection("Dati bambino", "");
		localWriter.addSubSection("Dati Anagrafici", "");
		if ( (data.has("trovatoInAnpr") && data.get("trovatoInAnpr") != null && data.get("trovatoInAnpr").asText().equalsIgnoreCase("N") ) ||
				(data.has("bambinoNonPresenteInElenco") && data.get("bambinoNonPresenteInElenco").asBoolean()) ) {
			localWriter.addItem("Nome", data.has("nomeBambino") ? data.get("nomeBambino").asText() : DEFAULT_VALUE);
			localWriter.addItem("Cognome", data.has("cognomeBambino") ? data.get("cognomeBambino").asText() : DEFAULT_VALUE);
			localWriter.addItem("Codice fiscale", data.has("codiceFiscaleBambino") ? data.get("codiceFiscaleBambino").asText() : DEFAULT_VALUE);
			localWriter.addItem("Sesso", data.has("sessoBambino") ? data.get("sessoBambino").asText() : DEFAULT_VALUE);
			//fixme: aggiungere sesso e verificare data per soggetto non presente 
			// Data di nascita
			if (data.has("dataNascitaBambino")) {
				Date dn = new SimpleDateFormat("yyyy-MM-dd").parse(data.get("dataNascitaBambino").asText());	
				localWriter.addItem("Data di nascita", new SimpleDateFormat("dd/MM/yyyy").format(dn));
			}
			if (data.has("bambinoResidenteATorino") && data.get("bambinoResidenteATorino").asText().equals("SI"))
			{
				localWriter.addItem("Residente a", "TORINO");
			}
			else {
				localWriter.addItem("Residente a", data.has("comuneResidenzaBambino") ? data.get("comuneResidenzaBambino").asText() : DEFAULT_VALUE);
			}
		} else {
			// lo recupero dalla selezione stato famiglia dgNucleofamiliare
			if (data.has("dgNucleofamiliare") &&  data.get("dgNucleofamiliare") != null && data.get("dgNucleofamiliare").isArray()) {		
				ArrayNode nucleo = (ArrayNode)data.get("dgNucleofamiliare");
				Iterator<JsonNode>it = nucleo.iterator();
				while (it.hasNext()) {
					JsonNode componente = it.next();
					if ( componente.has("selezionato") && componente.get("selezionato").asBoolean()) {
						localWriter.addItem("Nome", componente.has("dgNucleofamiliareNome") ? componente.get("dgNucleofamiliareNome").asText() : DEFAULT_VALUE);
						localWriter.addItem("Cognome", componente.has("dgNucleofamiliareCognome") ? componente.get("dgNucleofamiliareCognome").asText() : DEFAULT_VALUE);
						localWriter.addItem("Codice fiscale", componente.has("dgNucleofamiliareCodiceFiscale") ? componente.get("dgNucleofamiliareCodiceFiscale").asText() : DEFAULT_VALUE);
						localWriter.addItem("Sesso", componente.has("dgNucleofamiliareSesso") ? componente.get("dgNucleofamiliareSesso").asText() : DEFAULT_VALUE);
						// dgNucleofamiliareSesso
						// Data di nascita
						localWriter.addItem("Data di nascita", componente.has("dgNucleofamiliaredataNascita") ? componente.get("dgNucleofamiliaredataNascita").asText() : DEFAULT_VALUE);
						localWriter.addItem("Residente a", "TORINO");
						break;
					}
				}

			}
		}
		String relazione = "";
		if (data.has("relazione")) {
			String valore = data.get("relazione").asText();
			if (valore.equals("genitore"))
				relazione = "genitore";
			if (valore.equals("personaTutrice"))
				relazione = "persona tutrice o operatore del servizio sociale";
			if (valore.equals("personaAffidataria"))
				relazione = "persona affidataria";
		}
		localWriter.addItem("Relazione di parentela col bambino", relazione );
		/* Dati Bambino */
		localWriter.addSubSection("Scuola primaria", "");
		localWriter.addItem("Nome della scuola frequentata", data.has("nomeDellaScuolaFrequentata") ? data.get("nomeDellaScuolaFrequentata").asText() : DEFAULT_VALUE);
		localWriter.addItem("Classe", data.has("classe") ? data.get("classe").asText() : DEFAULT_VALUE);
		localWriter.addSubSection("Disabilità", "");
		localWriter.addItem("Il bambino ha disabilità certificate", data.has("radio1") ? data.get("radio1").asText() : DEFAULT_VALUE);
		/* Dati Famiglia */
		localWriter.addSection("Dati famiglia", "");
		localWriter.addSubSection("Situazione lavorativa", "");
		localWriter.addItem("Nel nucleo è presente un solo genitore ?", data.has("nelNucleoEPresenteUnSoloGenitore") ? data.get("nelNucleoEPresenteUnSoloGenitore").asText().toUpperCase() : DEFAULT_VALUE);

		// sono mutuamente esclusive
		if(	data.get("ilGenitorePresenteNelNucleoLavora") != null) {
			localWriter.addItem("Il genitore/adulto di riferimento lavora e non puo' occuparsi del bambino ?", data.has("ilGenitorePresenteNelNucleoLavora") ? data.get("ilGenitorePresenteNelNucleoLavora").asText().toUpperCase() : DEFAULT_VALUE);
		}
		if(	data.get("iGenitoriPresentiNelNucleoLavoranoEntrambi") != null) {
			localWriter.addItem("Entrambi i genitori/adulti di riferimento lavorano e non possono occuparsi del bambino? ", data.get("iGenitoriPresentiNelNucleoLavoranoEntrambi").asText().toUpperCase());
		}	
		localWriter.addSubSection("Attestazioni relative al nucleo familiare", "");
		localWriter.addItem("Assistenza economica del comune di Torino", data.get("assistenzaComuneTorino").asBoolean() ? "SI":"NO");
		localWriter.addItem("Reddito di cittadinanza", data.get("redditoDiCittadinanza").asBoolean() ? "SI":"NO"  );
		localWriter.addItem("Progetti specifici dei Servizi Sociali di inserimento scolastico ed educativo", data.get("progettiServiziSociali").asBoolean() ? "SI":"NO");
		localWriter.addItem("Esenzione mensa nell’anno scolastico 2019/2020", data.get("esenzioneMensa").asBoolean() ? "SI":"NO");

		localWriter.addSubSection("ISEE", "");
		localWriter.addItem("Valore indicatore ISEE MINORENNI 2020", data.has("ValoreISEEMinorenni2020") ? data.get("ValoreISEEMinorenni2020").get("nome").asText() : DEFAULT_VALUE);

		if (data.has("ValoreISEEMinorenni2020") && !data.get("ValoreISEEMinorenni2020").get("codice").asText().equals("nessunaIsee"))
		{
			if (data.has("dataCompilazioneIsee") && data.get("dataCompilazioneIsee") != null &&  !data.get("dataCompilazioneIsee").asText().equals("")) {
				Date d = new SimpleDateFormat("yyyy-MM-dd").parse(data.get("dataCompilazioneIsee").asText());	
				localWriter.addItem("Data di rilascio attestazione ISEE", new SimpleDateFormat("dd/MM/yyyy").format(d));			
			}
		}

		localWriter.addSection(true,"Scelta settimane e strutture", "");
		localWriter.addSubSection("Scelta settimane", "");		
		//localWriter.addItem("Periodi per cui si richiede la preiscrizione ", "");
		localWriter.addItem("dal 13 al 24 luglio", data.get("selectBoxes1").get("p13luglio24luglio").asBoolean() ? "SI":"NO");
		localWriter.addItem("dal 27 luglio al 7 agosto", data.get("selectBoxes1").get("p27luglio7agosto").asBoolean() ? "SI":"NO");
		localWriter.addSubSection("Scelta strutture", "");

		localWriter.addItem("Prima scelta", data.has("primaScelta") ? data.get("primaScelta").get("nome").asText() : DEFAULT_VALUE);
		localWriter.addItem("Seconda scelta", data.has("secondaScelta") ?  data.get("secondaScelta").get("nome").asText()  : DEFAULT_VALUE);

		// Privacy
		localWriter.addSection("Dichiarazioni", "");
		if (data.has("dichiarazionePrivacy") && data.get("dichiarazionePrivacy") != null)
		{
			localWriter.addSubSection("Informativa dati personale", "");
			localWriter.addItem("Dichiaro di aver preso visione dell'informativa sul trattamento dei dati personali", data.get("dichiarazionePrivacy").asBoolean() ? "SI": "NO");
		}
		if (data.has("dichiarazioneFalseDichiarazioni") && data.get("dichiarazioneFalseDichiarazioni") != null)
		{
			localWriter.addSubSection("Dichiarazioni sostitutive", "");
			localWriter.addItem("Dichiaro di aver letto ed essere consapevole delle sanzioni penali in caso di dichiarazioni false e della conseguente decadenza dai benefici eventualmente conseguiti (ai sensi degli artt. 75 3 76 DPR 445/2000) ", data.get("dichiarazioneFalseDichiarazioni").asBoolean() ? "SI": "NO");
		}

		if (data.has("dichiarazioneResponsabilitaGenitoriale") && data.get("dichiarazioneResponsabilitaGenitoriale") != null)
		{
			localWriter.addSubSection("Responsabilita genitoriale", "");
			localWriter.addItem("Dichiaro che, ai sensi del DLGS 154/2013 (art. 316 co. 1 e art. 337 ter co. 3), la richiesta di iscrizione è condivisa dai genitori o, nel caso di responsabilita' genitoriale esclusiva, dichiaro di essere l'unico genitore con responsabilita' genitoriale", data.get("dichiarazioneResponsabilitaGenitoriale").asBoolean() ? "SI": "NO");
		}

		return localWriter.write();
	}


	public static MoonprintDocument remapEstaRaga(Istanza istanza, ModuloStrutturaEntity strutturaEntity) throws Exception {
		final String DEFAULT_VALUE = "";
		MoonprintDocumentWriter localWriter = new MoonprintDocumentWriter();
		// TODO Auto-generated method stub
		localWriter.setTitle(istanza.getModulo().getOggettoModulo());

		// Metadata
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		localWriter.setMetadataDataPresentazione(df.format(istanza.getCreated()));
		localWriter.setMetadataNumeroIstanza(istanza.getCodiceIstanza());
		localWriter.setMetadataQrContent(istanza.getCodiceIstanza());
		localWriter.setMetadataHeader("%%title%%", "", "Inviata il: %%dataPresentazione%%");
		localWriter.setMetadataFooter("Istanza n.: %%numeroIstanza%%", null, null);

		// Lettura delle due strutture variabile
		JsonNode istanzaJsonNode = readIstanzaData(istanza);
		JsonNode data = istanzaJsonNode.get("data");
		localWriter.addSection("Dati compilante", "");
		/* COMPILANTE */
		localWriter.addSubSection("Compilante", "");
		localWriter.addItem("Nome", data.has("nome") ? data.get("nome").asText() : DEFAULT_VALUE);
		localWriter.addItem("Cognome", data.has("cognome") ? data.get("cognome").asText() : DEFAULT_VALUE);
		localWriter.addItem("Codice Fiscale", data.has("codiceFiscale") ? data.get("codiceFiscale").asText() : DEFAULT_VALUE );
		localWriter.addItem("Email", data.has("email") ? data.get("email").asText() : DEFAULT_VALUE);
		localWriter.addItem("Cellulare", data.has("cellulare2") ? data.get("cellulare2").asText() : DEFAULT_VALUE);
		localWriter.addSubSection("Altri dati di contatto", "");
		localWriter.addItem("Email", data.has("email1") ? data.get("email1").asText() : DEFAULT_VALUE);
		localWriter.addItem("Cellulare", data.has("cellulare1") ? data.get("cellulare1").asText()  : DEFAULT_VALUE);
		// Sezione Dati Bambino
		/*
		 * Se bambinoNonPresenteInElenco true leggo i dati da
		 * nome1, cognome1, codicefiscale1
		 */
		localWriter.addSection("Dati bambino", "");
		localWriter.addSubSection("Dati Anagrafici", "");
		if ( (data.has("trovatoInAnpr") && data.get("trovatoInAnpr") != null && data.get("trovatoInAnpr").asText().equalsIgnoreCase("N") ) ||
				(data.has("bambinoNonPresenteInElenco") && data.get("bambinoNonPresenteInElenco").asBoolean()) ) {
			localWriter.addItem("Nome", data.has("nomeBambino") ? data.get("nomeBambino").asText() : DEFAULT_VALUE);
			localWriter.addItem("Cognome", data.has("cognomeBambino") ? data.get("cognomeBambino").asText() : DEFAULT_VALUE);
			localWriter.addItem("Codice fiscale", data.has("codiceFiscaleBambino") ? data.get("codiceFiscaleBambino").asText() : DEFAULT_VALUE);
			localWriter.addItem("Sesso", data.has("sessoBambino") ? data.get("sessoBambino").asText() : DEFAULT_VALUE);
			//fixme: aggiungere sesso e verificare data per soggetto non presente 
			// Data di nascita
			if (data.has("dataNascitaBambino")) {
				Date dn = new SimpleDateFormat("yyyy-MM-dd").parse(data.get("dataNascitaBambino").asText());	
				localWriter.addItem("Data di nascita", new SimpleDateFormat("dd/MM/yyyy").format(dn));
			}
			if (data.has("bambinoResidenteATorino") && data.get("bambinoResidenteATorino").asText().equals("SI"))
			{
				localWriter.addItem("Residente a", "TORINO");
			}
			else {
				localWriter.addItem("Residente a", data.has("comuneResidenzaBambino") ? data.get("comuneResidenzaBambino").asText() : DEFAULT_VALUE);
			}
		} else {
			// lo recupero dalla selezione stato famiglia dgNucleofamiliare
			if (data.has("dgNucleofamiliare") &&  data.get("dgNucleofamiliare") != null && data.get("dgNucleofamiliare").isArray()) {		
				ArrayNode nucleo = (ArrayNode)data.get("dgNucleofamiliare");
				Iterator<JsonNode>it = nucleo.iterator();
				while (it.hasNext()) {
					JsonNode componente = it.next();
					if ( componente.has("selezionato") && componente.get("selezionato").asBoolean()) {
						localWriter.addItem("Nome", componente.has("dgNucleofamiliareNome") ? componente.get("dgNucleofamiliareNome").asText() : DEFAULT_VALUE);
						localWriter.addItem("Cognome", componente.has("dgNucleofamiliareCognome") ? componente.get("dgNucleofamiliareCognome").asText() : DEFAULT_VALUE);
						localWriter.addItem("Codice fiscale", componente.has("dgNucleofamiliareCodiceFiscale") ? componente.get("dgNucleofamiliareCodiceFiscale").asText() : DEFAULT_VALUE);
						localWriter.addItem("Sesso", componente.has("dgNucleofamiliareSesso") ? componente.get("dgNucleofamiliareSesso").asText() : DEFAULT_VALUE);
						// dgNucleofamiliareSesso
						// Data di nascita
						localWriter.addItem("Data di nascita", componente.has("dgNucleofamiliaredataNascita") ? componente.get("dgNucleofamiliaredataNascita").asText() : DEFAULT_VALUE);
						localWriter.addItem("Residente a", "TORINO");
						break;
					}
				}

			}
		}
		String relazione = "";
		if (data.has("relazione")) {
			String valore = data.get("relazione").asText();
			if (valore.equals("genitore"))
				relazione = "genitore";
			if (valore.equals("personaTutrice"))
				relazione = "persona tutrice o operatore del servizio sociale";
			if (valore.equals("personaAffidataria"))
				relazione = "persona affidataria";
		}
		localWriter.addItem("Relazione di parentela col bambino", relazione );

		/* Dati Bambino */

		localWriter.addSubSection("Scuola primaria", "");
		localWriter.addItem("Nome della scuola frequentata", data.has("nomeDellaScuolaFrequentata") ? data.get("nomeDellaScuolaFrequentata").asText() : DEFAULT_VALUE);
		localWriter.addItem("Classe", data.has("classe") ? data.get("classe").asText() : DEFAULT_VALUE);
		localWriter.addSubSection("Disabilità", "");
		localWriter.addItem("Il bambino ha disabilità certificate", data.has("radio1") ? data.get("radio1").asText() : DEFAULT_VALUE);
		/* Dati Famiglia */
		localWriter.addSection("Dati famiglia", "");
		localWriter.addSubSection("Situazione lavorativa", "");
		localWriter.addItem("Nel nucleo è presente un solo genitore ?", data.has("nelNucleoEPresenteUnSoloGenitore") ? data.get("nelNucleoEPresenteUnSoloGenitore").asText().toUpperCase() : DEFAULT_VALUE);

		// sono mutuamente esclusive
		if(	data.get("ilGenitorePresenteNelNucleoLavora") != null) {
			localWriter.addItem("Il genitore/adulto di riferimento lavora e non puo' occuparsi del bambino ?", data.has("ilGenitorePresenteNelNucleoLavora") ? data.get("ilGenitorePresenteNelNucleoLavora").asText().toUpperCase() : DEFAULT_VALUE);
		}
		if(	data.get("iGenitoriPresentiNelNucleoLavoranoEntrambi") != null) {
			localWriter.addItem("Entrambi i genitori/adulti di riferimento lavorano e non possono occuparsi del bambino? ", data.get("iGenitoriPresentiNelNucleoLavoranoEntrambi").asText().toUpperCase());
		}	


		localWriter.addSubSection("Attestazioni relative al nucleo familiare", "");
		localWriter.addItem("Assistenza economica del comune di Torino", data.get("assistenzaComuneTorino").asBoolean() ? "SI":"NO");
		localWriter.addItem("Reddito di cittadinanza", data.get("redditoDiCittadinanza").asBoolean() ? "SI":"NO"  );
		localWriter.addItem("Progetti specifici dei Servizi Sociali di inserimento scolastico ed educativo", data.get("progettiServiziSociali").asBoolean() ? "SI":"NO");
		localWriter.addItem("Esenzione mensa nell’anno scolastico 2019/2020", data.get("esenzioneMensa").asBoolean() ? "SI":"NO");


		localWriter.addSubSection("ISEE", "");
		localWriter.addItem("Valore indicatore ISEE MINORENNI 2020", data.has("ValoreISEEMinorenni2020") ? data.get("ValoreISEEMinorenni2020").get("nome").asText() : DEFAULT_VALUE);

		if (data.has("ValoreISEEMinorenni2020") && !data.get("ValoreISEEMinorenni2020").get("codice").asText().equals("nessunaIsee"))
		{
			if (data.has("dataCompilazioneIsee") && data.get("dataCompilazioneIsee") != null &&  !data.get("dataCompilazioneIsee").asText().equals("")) {
				Date d = new SimpleDateFormat("yyyy-MM-dd").parse(data.get("dataCompilazioneIsee").asText());	
				localWriter.addItem("Data di rilascio attestazione ISEE", new SimpleDateFormat("dd/MM/yyyy").format(d));			
			}
		}

		localWriter.addSection(true,"Scelta settimane e strutture", "");
		localWriter.addSubSection("Scelta settimane", "");		
		//localWriter.addItem("Periodi per cui si richiede la preiscrizione ", "");
		localWriter.addItem("dal 29 giugno al 10 luglio", data.get("selectBoxes1").get("p29giugno10luglio").asBoolean() ? "SI":"NO");
		localWriter.addItem("dal 13 al 24 luglio", data.get("selectBoxes1").get("p13luglio24luglio").asBoolean() ? "SI":"NO");
		localWriter.addItem("dal 27 luglio al 7 agosto", data.get("selectBoxes1").get("p27luglio7agosto").asBoolean() ? "SI":"NO");
		localWriter.addSubSection("Scelta strutture", "");

		localWriter.addItem("Prima scelta", data.has("primaScelta") ? data.get("primaScelta").get("nome").asText() : DEFAULT_VALUE);
		localWriter.addItem("Seconda scelta", data.has("secondaScelta") ?  data.get("secondaScelta").get("nome").asText()  : DEFAULT_VALUE);

		// Privacy
		localWriter.addSection("Dichiarazioni", "");
		if (data.has("dichiarazionePrivacy") && data.get("dichiarazionePrivacy") != null)
		{
			localWriter.addSubSection("Informativa dati personale", "");
			localWriter.addItem("Dichiaro di aver preso visione dell'informativa sul trattamento dei dati personali", data.get("dichiarazionePrivacy").asBoolean() ? "SI": "NO");
		}
		if (data.has("dichiarazioneFalseDichiarazioni") && data.get("dichiarazioneFalseDichiarazioni") != null)
		{
			localWriter.addSubSection("Dichiarazioni sostitutive", "");
			localWriter.addItem("Dichiaro di aver letto ed essere consapevole delle sanzioni penali in caso di dichiarazioni false e della conseguente decadenza dai benefici eventualmente conseguiti (ai sensi degli artt. 75 3 76 DPR 445/2000) ", data.get("dichiarazioneFalseDichiarazioni").asBoolean() ? "SI": "NO");
		}

		if (data.has("dichiarazioneResponsabilitaGenitoriale") && data.get("dichiarazioneResponsabilitaGenitoriale") != null)
		{
			localWriter.addSubSection("Responsabilita genitoriale", "");
			localWriter.addItem("Dichiaro che, ai sensi del DLGS 154/2013 (art. 316 co. 1 e art. 337 ter co. 3), la richiesta di iscrizione è condivisa dai genitori o, nel caso di responsabilita' genitoriale esclusiva, dichiaro di essere l'unico genitore con responsabilita' genitoriale", data.get("dichiarazioneResponsabilitaGenitoriale").asBoolean() ? "SI": "NO");
		}
		return localWriter.write();
	}

}
