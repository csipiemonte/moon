/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.csi.moon.moonfobl.business.service.impl.helper.dto.coto.dem.CambioResidenzaEntity;
import it.csi.moon.moonfobl.business.service.impl.helper.dto.coto.dem.ComponentiFamigliaEntity;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;



/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class CambioResidenzaHelper extends DatiIstanzaHelper {
	
	private static final String CLASS_NAME = "CambioResidenzaHelper";
	
	public static CambioResidenzaEntity parse(String datiIstanza) throws BusinessException {
		CambioResidenzaEntity result = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode dataNode = (ObjectNode)istanzaNode.get("data");
			
			result = new CambioResidenzaEntity();
			
			if (dataNode.has("email") && dataNode.get("email") != null)
			{
				result.setEmail(dataNode.get("email").asText());
			}
			else if (dataNode.has("emailRecapito") && dataNode.get("emailRecapito") != null) 
			{
				result.setEmail(dataNode.get("emailRecapito").asText());
			}
			JsonNode richiedente = dataNode.get("richiedente");
			String cognome = null;
			String nome = null;
			String codiceFiscale = null;
			if (richiedente!=null) {
				cognome = richiedente.get("cognome")!=null?richiedente.get("cognome").asText():"";
				nome = richiedente.get("nome")!=null?richiedente.get("nome").asText():"";
				codiceFiscale = richiedente.get("codiceFiscale")!=null?richiedente.get("codiceFiscale").asText():"";
			} else {
				// DEM_001
				cognome = dataNode.get("cognome")!=null?dataNode.get("cognome").asText():"";
				nome = dataNode.get("nome")!=null?dataNode.get("nome").asText():"";
				codiceFiscale = dataNode.get("codiceFiscale")!=null?dataNode.get("codiceFiscale").asText():"";
			}
			result.setNome(nome.toUpperCase());
			result.setCognome(cognome.toUpperCase());
			result.setRichiedente(nome.toUpperCase() + " " + cognome.toUpperCase());
			result.setCodiceFiscale(codiceFiscale.toUpperCase());
			
			if (dataNode.has("componente") && dataNode.get("componente") != null)
			{
				ArrayNode elencoDati = (ArrayNode)dataNode.get("componente");
				Iterator<JsonNode> it = elencoDati.iterator();
				int i=1;
				while (it.hasNext()) {
					JsonNode elemento = it.next();
					if (elemento.has("cf") && !elemento.get("cf").asText().equals("")) {
						ComponentiFamigliaEntity componente = new ComponentiFamigliaEntity();
						if (elemento.has("numeroPatente") && elemento.get("numeroPatente") != null)
						{
							componente.setNumeroPatente(elemento.get("numeroPatente")!=null?elemento.get("numeroPatente").asText():"");
						}
//						if (elemento.has("veicoli") && elemento.get("veicoli") != null )
//						{
//							ArrayNode elencoVeicoli= (ArrayNode)elemento.get("veicoli");
//							Iterator<JsonNode> itv = elencoVeicoli.iterator();
//							int iv=1;
//							while (itv.hasNext()) {
//								JsonNode veicolo = itv.next();
//								componente.getTarghe().add(veicolo.has("targa") ? veicolo.get("targa").asText() : "");
//								iv++;
//							}
//							
//						}
						i++;
						result.getComponenti().add(componente);
					}
				}
				result.setNumeroComponenti(i); // qui ci sono i componenti aggiuntivi al dichiarante quindi e' giusto i
			}
			else if (dataNode.has("dgNucleofamiliare") && dataNode.get("dgNucleofamiliare") != null	&& dataNode.get("dgNucleofamiliare").isArray()) {
				// DEM_001
				ArrayNode elencoDati = (ArrayNode)dataNode.get("dgNucleofamiliare");
				Iterator<JsonNode> it = elencoDati.iterator();
//				int i=1;
				while (it.hasNext()) {
					JsonNode elemento = it.next();
					if (elemento.has("dgNucleofamiliareCodiceFiscale") && !elemento.get("dgNucleofamiliareCodiceFiscale").asText().equals("")) {
						ComponentiFamigliaEntity componente = new ComponentiFamigliaEntity();
						if (elemento.has("numeroPatente") && elemento.get("numeroPatente") != null)
						{
							componente.setNumeroPatente(elemento.get("numeroPatente")!=null?elemento.get("numeroPatente").asText():"");
						}
						if (elemento.has("targhe") && elemento.get("targhe") != null)
						{
							componente.getTarghe().add(elemento.get("targhe")!=null?elemento.get("targhe").asText():"");
						}
//						i++;
						result.getComponenti().add(componente);
					}
				}
				result.setNumeroComponenti(result.getComponenti().size()); // qui ci sono gia tutti componenti della famiglia quindi non usare i che e' già incrementato di 1
			}
			String indirizzo = null;
			if (dataNode.has("nuovoindirizzo") && dataNode.get("nuovoindirizzo") != null)
			{
				JsonNode nuovoindirizzo = dataNode.get("nuovoindirizzo");
				indirizzo = nuovoindirizzo.has("civicocompleto") && nuovoindirizzo.get("civicocompleto").has("nome") ?nuovoindirizzo.get("civicocompleto").get("nome").asText() : "";
				String civicocompletoflagUiu = nuovoindirizzo.has("civicocompleto") && nuovoindirizzo.get("civicocompleto").has("flagUiu") ? nuovoindirizzo.get("civicocompleto").get("flagUiu").asText() : "";
				if (civicocompletoflagUiu.equals("S")) {
					String pianoNUI =  nuovoindirizzo.has("pianoNui") ? nuovoindirizzo.get("pianoNui").get("nome").asText() : "";
					indirizzo += ", "+pianoNUI;
				}
				if (civicocompletoflagUiu.equals("N")) {
					String piano = nuovoindirizzo.has("piano") ? nuovoindirizzo.get("piano").get("nome").asText() : "";
					indirizzo += ", "+piano;
				}
			} else if (dataNode.get("indNuovoPanelComune")!=null) {
				// DEM_001
				indirizzo = dataNode.has("indNuovoCivico") ? dataNode.get("indNuovoCivico").get("nome").asText() : "";
				String civicocompletoflagUiu = dataNode.has("indNuovoCivico") && dataNode.get("indNuovoCivico").has("flagUiu") ? dataNode.get("indNuovoCivico").get("flagUiu").asText() : "";
				if (civicocompletoflagUiu.equals("S")) {
					String pianoNUI =  dataNode.has("indNuovoPianoNui") ? dataNode.get("indNuovoPianoNui").get("nome").asText() : "";
					indirizzo += ", "+pianoNUI;
				}
				if (civicocompletoflagUiu.equals("N")) {
					String piano = dataNode.has("indNuovoPiano") ? dataNode.get("indNuovoPiano").get("nome").asText() : "";
					indirizzo += ", "+piano;
				}
			}
			result.setIndirizzo(indirizzo);
			return result;
		} catch (ParseException e) {
			LOG.error("[" + CLASS_NAME + "::parse] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::parse] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
		}
	}


//	public static RicevutaCambioResidEntity parseAzione(String datiAzione) throws BusinessException {
//		RicevutaCambioResidEntity result = null;
//		try {
//			
//			LOG.debug("[" + CLASS_NAME + "::parse] Inizio parse azione genera_ricevuta ");
//			JsonNode istanzaNode = readIstanzaData(datiAzione);
//			JsonNode dataNode = (ObjectNode)istanzaNode.get("data");
//			
//			result = new RicevutaCambioResidEntity();
//
//			result.setRichiedente(dataNode.has("richiedente")?dataNode.get("richiedente").asText():""); 
//			String numeroComponentiStr = dataNode.has("numeroComponenti")?dataNode.get("numeroComponenti").asText():"";
//			if (numeroComponentiStr != null )
//			{
//				int nc =  Integer.parseInt(numeroComponentiStr);
//				result.setNumeroComponenti(nc);
//			}
//			result.setIndirizzo(dataNode.has("indirizzo")?dataNode.get("indirizzo").asText():"");	
//			
//			result.setDataPresentazione(dataNode.has("dataPresentazione")?dataNode.get("dataPresentazione").asText():"");
//
//			result.setOperatore(dataNode.has("operatore")?dataNode.get("operatore").get("nome").asText():"");
//			String funzionario = dataNode.has("funzionario")?dataNode.get("funzionario").get("nome").asText():"";
//			String numeroPraticaNao = dataNode.has("numeroPraticaNao")?dataNode.get("numeroPraticaNao").asText():"";
//			String dataRegistrazione = dataNode.has("dataRegistrazione")? (dataNode.get("dataRegistrazione") != null ? parseDate(dataNode.get("dataRegistrazione")): "") : "";
//			String telefono = dataNode.has("telefono")?dataNode.get("telefono").asText():"";
//			String fax = dataNode.has("fax")?dataNode.get("fax").asText():"";
//			
//			result.setFunzionario(funzionario);
//			result.setNumeroPraticaNao(numeroPraticaNao);
//			result.setDataRegistrazione(dataRegistrazione);
//			result.setFax(fax);
//			result.setTelefono(telefono);
//			
//			return result;
//		} catch (ParseException e) {
//			LOG.error("[" + CLASS_NAME + "::parse] ParseException " + datiAzione, e);
//			throw new BusinessException();
//		} catch (Exception e) {
//			LOG.error("[" + CLASS_NAME + "::parse] Exception " + datiAzione, e);
//			throw new BusinessException();
//		} finally {
//			LOG.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
//		}
//	}
//	
	private static String parseDate(JsonNode jsonNode) throws ParseException {
		if (jsonNode==null) return null;
		try {
			SimpleDateFormat sdf_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
			String date10 = jsonNode.asText().substring(0,10);
			Date dateD = sdf_yyyy_MM_dd.parse(date10);
			
			SimpleDateFormat outpuFormat = new SimpleDateFormat("dd-MM-yyyy");
			String dateString = outpuFormat.format(dateD);
			return dateString;
		} catch (ParseException e) {
			LOG.error("[" + CLASS_NAME + "::parseDate] ParseException sdf_dd_MM_yyyy with "+jsonNode);
			throw (e);
		}
	}

}
