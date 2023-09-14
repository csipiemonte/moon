/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.csi.moon.moonfobl.business.service.impl.helper.dto.IntegrazioneActionEntity;
import it.csi.moon.moonfobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;



/**
 * Helper per operazioni sul json data delle azioni
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class DatiAzioneHelper extends DatiIstanzaHelper {
	
	private static final String CLASS_NAME = "DatiAzioneHelper";
	
	public static NotificaActionEntity parseAzioneNotifica(String datiAzione) throws BusinessException {
		NotificaActionEntity result = null;
		try {
			
			LOG.debug("[" + CLASS_NAME + "::parse] Inizio parse azione genera_ricevuta ");
			JsonNode istanzaNode = readIstanzaData(datiAzione);
			JsonNode dataNode = (ObjectNode)istanzaNode.get("data");
			
			result = new NotificaActionEntity();

			result.setNome(dataNode.has("nome")?dataNode.get("nome").asText():""); 
			result.setCognome(dataNode.has("cognome")?dataNode.get("cognome").asText():""); 
			result.setCodiceFiscale(dataNode.has("codiceFiscale")?dataNode.get("codiceFiscale").asText():"");
			result.setEmail(dataNode.has("email")?dataNode.get("email").asText():""); 
			result.setTesto(dataNode.has("testo")?dataNode.get("testo").asText():""); 

			List<String> elencoFormIoFileNames = new ArrayList<>();
			if (dataNode.has("allegati") &&  dataNode.get("allegati") != null && dataNode.get("allegati").isArray()) {		
				ArrayNode elenco = (ArrayNode)dataNode.get("allegati");
				Iterator<JsonNode>it = elenco.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();						
					if (elemento.has("allegato") &&  elemento.get("allegato") != null && elemento.get("allegato").isArray()) {		
						ArrayNode allegato = (ArrayNode)elemento.get("allegato");
						Iterator<JsonNode>it2 = allegato.iterator();
						while (it2.hasNext()) {
							JsonNode file = it2.next();							
							if (file != null && file.get("name") != null) {
								elencoFormIoFileNames.add(file.get("name").asText());
							}
						}	
					}
				}
			} // fine allegati
			result.setFormIoFileNames(elencoFormIoFileNames);
			
			return result;
		} catch (ParseException e) {
			LOG.error("[" + CLASS_NAME + "::parse] ParseException " + datiAzione, e);
			throw new BusinessException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::parse] Exception " + datiAzione, e);
			throw new BusinessException();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
		}
	}
	
	public static IntegrazioneActionEntity parseAzioneNotificaIntegrazione(String datiAzione) throws BusinessException {
		
		IntegrazioneActionEntity result = null;
		try {
			
			LOG.debug("[" + CLASS_NAME + "::parse] Inizio parse azione integrazione ");
			JsonNode istanzaNode = readIstanzaData(datiAzione);
			JsonNode dataNode = (ObjectNode)istanzaNode.get("data");
			
			result = new IntegrazioneActionEntity();

			result.setNome(dataNode.has("nome")?dataNode.get("nome").asText():""); 
			result.setCognome(dataNode.has("cognome")?dataNode.get("cognome").asText():""); 
			result.setCodiceFiscale(dataNode.has("codiceFiscale")?dataNode.get("codiceFiscale").asText():"");
			result.setEmail(dataNode.has("email")?dataNode.get("email").asText():""); 
			result.setTesto(dataNode.has("testo")?dataNode.get("testo").asText():""); 
			result.setTesto(dataNode.has("testoRisposta")?dataNode.get("testoRisposta").asText():""); 
			
			List<String> elencoEmailCc = new ArrayList<>();
			if (dataNode.has("emailCc") && dataNode.get("emailCc") != null && dataNode.get("emailCc").isArray()) {
				ArrayNode elenco = (ArrayNode) dataNode.get("emailCc");
				Iterator<JsonNode> it = elenco.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();
					if (elemento.has("email") && elemento.get("email") != null) {
						elencoEmailCc.add(elemento.get("email").asText());
					}
				}
			} 
			result.setEmailCc(elencoEmailCc);			

			List<String> elencoFormIoFileNames = new ArrayList<>();
			if (dataNode.has("allegati") && dataNode.get("allegati") != null && dataNode.get("allegati").isArray()) {
				ArrayNode elenco = (ArrayNode) dataNode.get("allegati");
				Iterator<JsonNode> it = elenco.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();
					if (elemento.has("allegato") && elemento.get("allegato") != null
							&& elemento.get("allegato").isArray()) {
						ArrayNode allegato = (ArrayNode) elemento.get("allegato");
						Iterator<JsonNode> it2 = allegato.iterator();
						while (it2.hasNext()) {
							JsonNode file = it2.next();

							if (file != null && file.get("name") != null) {

								elencoFormIoFileNames.add(file.get("name").asText());

							}
						}
					}
				}
			} 
			result.setFormIoFileNames(elencoFormIoFileNames);
			
			List<String> elencoIntegrFormIoFileNames = new ArrayList<>();
			if (dataNode.has("allegatiRisposta") && dataNode.get("allegatiRisposta") != null && dataNode.get("allegatiRisposta").isArray()) {
				ArrayNode elenco = (ArrayNode) dataNode.get("allegatiRisposta");
				Iterator<JsonNode> it = elenco.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();
					if (elemento.has("allegatoRisposta") && elemento.get("allegatoRisposta") != null
							&& elemento.get("allegatoRisposta").isArray()) {
						ArrayNode allegato = (ArrayNode) elemento.get("allegatoRisposta");
						Iterator<JsonNode> it2 = allegato.iterator();
						while (it2.hasNext()) {
							JsonNode file = it2.next();

							if (file != null && file.get("name") != null) {

								elencoIntegrFormIoFileNames.add(file.get("name").asText());

							}
						}
					}
				}
			} 
			result.setIntegrazioneFormIoFileNames(elencoIntegrFormIoFileNames);
			
			return result;
		} catch (ParseException e) {
			LOG.error("[" + CLASS_NAME + "::parse] ParseException " + datiAzione, e);
			throw new BusinessException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::parse] Exception " + datiAzione, e);
			throw new BusinessException();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
		}
	}
	
}
