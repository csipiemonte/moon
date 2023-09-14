/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.GeneralEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;



/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class GeneralHelper extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "GeneralHelper";

	
	public static GeneralEntity parse(String datiIstanza) throws BusinessException {
		GeneralEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			result = new GeneralEntity();
			
			String cognome = "";
			String nome = "";
			String codiceFiscale = "";
			String email = "";
			
			if (data.has("cognome") && data.get("cognome") != null)
			{
				cognome = data.get("cognome").getTextValue();
			}
			if (data.has("nome") && data.get("nome") != null)
			{
				nome = data.get("nome").getTextValue();
			}
			if (data.has("codiceFiscale") && data.get("codiceFiscale") != null)
			{
				codiceFiscale = data.get("codiceFiscale").getTextValue();
			}
			
			if (data.has("email") && data.get("email") != null)
			{
				email = data.get("email").getTextValue();
			}
			
			if (data.has("richiedente")) 
			{
				JsonNode richiedente = data.get("richiedente");
				if (richiedente.has("cognome") && richiedente.get("cognome") != null)
				{
					cognome = richiedente.get("cognome").getTextValue();
				}
				if (richiedente.has("nome") && richiedente.get("nome") != null)
				{
					nome = richiedente.get("nome").getTextValue();
				}
				if (richiedente.has("codiceFiscale") && richiedente.get("codiceFiscale") != null)
				{
					codiceFiscale = richiedente.get("codiceFiscale").getTextValue();
				}
				if (richiedente.has("email") && richiedente.get("email") != null)
				{
					email = richiedente.get("email").getTextValue();
				}
				
				if (richiedente.has("contatto")) 
				{
					JsonNode contatto = richiedente.get("contatto");
					if (email.equals("") && contatto.has("email") && contatto.get("email") != null)
					{
						email = contatto.get("email").getTextValue();
					}
				}
			}

			result.setNome(nome.toUpperCase());
			result.setCognome(cognome.toUpperCase());
			result.setRichiedente(nome.toUpperCase() + " " + cognome.toUpperCase());
			result.setCodiceFiscale(codiceFiscale.toUpperCase());
			result.setEmail(email);
			
			if (data.has("pec") && data.get("pec") != null)
			{
				result.setPec(data.get("pec").getTextValue());
			}

			return result;
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parse] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parse] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
		}
	}


	private static String parseDate(JsonNode jsonNode) throws ParseException {
		if (jsonNode==null) return null;
		try {
			SimpleDateFormat sdf_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
			String date10 = jsonNode.getTextValue().substring(0,10);
			Date dateD = sdf_yyyy_MM_dd.parse(date10);
			
			SimpleDateFormat outpuFormat = new SimpleDateFormat("dd-MM-yyyy");
			String dateString = outpuFormat.format(dateD);
			return dateString;
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parseDate] ParseException sdf_dd_MM_yyyy with "+jsonNode);
			throw (e);
		}
	}

	public static NotificaActionEntity parseAzioneNotifica(String datiAzione) throws BusinessException {
		NotificaActionEntity result = null;
		try {
			
			log.debug("[" + CLASS_NAME + "::parse] Inizio parse azione genera_ricevuta ");
			JsonNode istanzaNode = readIstanzaData(datiAzione);
			JsonNode dataNode = (ObjectNode)istanzaNode.get("data");
			
			result = new NotificaActionEntity();

			result.setNome(dataNode.has("nome")?dataNode.get("nome").getTextValue():""); 
			result.setCognome(dataNode.has("cognome")?dataNode.get("cognome").getTextValue():""); 
			result.setCodiceFiscale(dataNode.has("codiceFiscale")?dataNode.get("codiceFiscale").getTextValue():"");
			result.setEmail(dataNode.has("email")?dataNode.get("email").getTextValue():""); 
			result.setPec(dataNode.has("pec")?dataNode.get("pec").getTextValue():""); 
			result.setTesto(dataNode.has("testo")?dataNode.get("testo").getTextValue():""); 
			
			List<String> emailCc = new ArrayList<String>();
			if (dataNode.has("emailCc") &&  dataNode.get("emailCc") != null && dataNode.get("emailCc").isArray()){		
				ArrayNode elenco = (ArrayNode)dataNode.get("emailCc");
				Iterator<JsonNode>it = elenco.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();						
					emailCc.add(elemento.get("email").getTextValue());				
				}
			}
			result.setEmailCc(emailCc);
			
			List<String> elencoFormIoFileNames = new ArrayList<String>();
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
	
	public static NotificaActionEntity parseAzioneInvioDocumentazione(String datiAzione) throws BusinessException {
		NotificaActionEntity result = null;
		try {
			
			log.debug("[" + CLASS_NAME + "::parse] Inizio parse azione invio docuentazione ");
			JsonNode istanzaNode = readIstanzaData(datiAzione);
			JsonNode dataNode = (ObjectNode)istanzaNode.get("data");
			
			result = new NotificaActionEntity();

			result.setEmail(dataNode.has("emailUfficio")?dataNode.get("emailUfficio").getTextValue():""); 
			
			List<String> elencoFormIoFileNames = new ArrayList<String>();
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
