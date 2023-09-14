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
import it.csi.moon.moonbobl.business.service.impl.helper.dto.ComunicazioneEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.dem.RicevutaCambioResidEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;


/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class ComunicazioneHelper extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "ComunicazioneHelper";

	
	public static ComunicazioneEntity parse(String datiIstanza) throws BusinessException {
		ComunicazioneEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			result = new ComunicazioneEntity();
			
			if (data.has("email") && data.get("email") != null)
			{
				result.setEmail(data.get("email").getTextValue());
			}
			else if (data.has("emailRecapito") && data.get("emailRecapito") != null) 
			{
				result.setEmail(data.get("emailRecapito").getTextValue());
			}
			JsonNode richiedente = data.get("richiedente");
			String cognome = null;
			String nome = null;
			String codiceFiscale = null;
			if (richiedente!=null) {
				cognome = richiedente.get("cognome")!=null?richiedente.get("cognome").getTextValue():"";
				nome = richiedente.get("nome")!=null?richiedente.get("nome").getTextValue():"";
				codiceFiscale = richiedente.get("codiceFiscale")!=null?richiedente.get("codiceFiscale").getTextValue():"";
			} else {
				// DEM_001
				cognome = data.get("cognome")!=null?data.get("cognome").getTextValue():"";
				nome = data.get("nome")!=null?data.get("nome").getTextValue():"";
				codiceFiscale = data.get("codiceFiscale")!=null?data.get("codiceFiscale").getTextValue():"";
			}
			result.setNome(nome.toUpperCase());
			result.setCognome(cognome.toUpperCase());
			result.setRichiedente(nome.toUpperCase() + " " + cognome.toUpperCase());
			result.setCodiceFiscale(codiceFiscale.toUpperCase());
		
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


	public static RicevutaCambioResidEntity parseAzione(String datiAzione) throws BusinessException {
		RicevutaCambioResidEntity result = null;
		try {
			
			log.debug("[" + CLASS_NAME + "::parse] Inizio parse azione genera_ricevuta ");
			JsonNode istanzaNode = readIstanzaData(datiAzione);
			JsonNode dataNode = (ObjectNode)istanzaNode.get("data");
			
			result = new RicevutaCambioResidEntity();

			result.setRichiedente(dataNode.has("richiedente")?dataNode.get("richiedente").getTextValue():""); 
			String numeroComponentiStr = dataNode.has("numeroComponenti")?dataNode.get("numeroComponenti").getTextValue():"";
			if (numeroComponentiStr != null )
			{
				int nc =  Integer.parseInt(numeroComponentiStr);
				result.setNumeroComponenti(nc);
			}
			result.setIndirizzo(dataNode.has("indirizzo")?dataNode.get("indirizzo").getTextValue():"");	
			
			result.setDataPresentazione(dataNode.has("dataPresentazione")?dataNode.get("dataPresentazione").getTextValue():"");

			result.setOperatore(dataNode.has("operatore")?dataNode.get("operatore").get("nome").getTextValue():"");
			String funzionario = dataNode.has("funzionario")?dataNode.get("funzionario").get("nome").getTextValue():"";
			String numeroPraticaNao = dataNode.has("numeroPraticaNao")?dataNode.get("numeroPraticaNao").getTextValue():"";
			String dataRegistrazione = dataNode.has("dataRegistrazione")? (dataNode.get("dataRegistrazione") != null ? parseDate(dataNode.get("dataRegistrazione")): "") : "";
			String telefono = dataNode.has("telefono")?dataNode.get("telefono").getTextValue():"";
			String fax = dataNode.has("fax")?dataNode.get("fax").getTextValue():"";
			
			result.setFunzionario(funzionario);
			result.setNumeroPraticaNao(numeroPraticaNao);
			result.setDataRegistrazione(dataRegistrazione);
			result.setFax(fax);
			result.setTelefono(telefono);
			
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
			result.setTesto(dataNode.has("testo")?dataNode.get("testo").getTextValue():""); 

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
