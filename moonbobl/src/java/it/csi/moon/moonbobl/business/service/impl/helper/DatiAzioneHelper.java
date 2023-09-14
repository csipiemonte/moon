/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.MyDocsActionEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

/**
 * Helper per operazioni sul json data delle azioni
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class DatiAzioneHelper extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "DatiAzioneHelper";
	
	public static MyDocsActionEntity parseMyDocsAttachmentsAzione(String datiAzione) throws BusinessException {
		MyDocsActionEntity result = null;
		try {
			
			log.debug("[" + CLASS_NAME + "::parse] Inizio parse azione mydocs ");
			JsonNode istanzaNode = readIstanzaData(datiAzione);
			JsonNode dataNode = (ObjectNode)istanzaNode.get("data");
			
			result = new MyDocsActionEntity();

			List<String> elencoFormIoFileNames = new ArrayList<String>();
			if (dataNode.has("allegati_mydocs") &&  dataNode.get("allegati_mydocs") != null && dataNode.get("allegati_mydocs").isArray()) {		
				ArrayNode elenco = (ArrayNode)dataNode.get("allegati_mydocs");
				Iterator<JsonNode>it = elenco.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();						
					if (elemento.has("allegato_mydocs") &&  elemento.get("allegato_mydocs") != null && elemento.get("allegato_mydocs").isArray()) {		
						ArrayNode allegato = (ArrayNode)elemento.get("allegato_mydocs");
						Iterator<JsonNode>it2 = allegato.iterator();
						while (it2.hasNext()) {
							JsonNode file = it2.next();							
							if (file != null && file.get("name") != null) {
								elencoFormIoFileNames.add(file.get("name").getTextValue());
							}
						}	
					}
				}
			} // fine allegati
			result.setFormIoFileNames(elencoFormIoFileNames);
			
			return result;
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parse] ParseException " + datiAzione, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parse] Exception " + datiAzione, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
		}
	}
	
	
	
}
