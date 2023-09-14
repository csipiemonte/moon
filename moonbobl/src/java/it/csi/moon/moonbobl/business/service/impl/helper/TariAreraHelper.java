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

import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.tari.TariAreraEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RapportoFineConcessioneEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RicevutaTcrDiniegoEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;


/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class TariAreraHelper extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "TariAreraHelper";

	
	public static TariAreraEntity parse(String datiIstanza) throws BusinessException {
		TariAreraEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			result = new TariAreraEntity();
			String tipoPersona = "";
			if(data.has("dichiarante")) {
				JsonNode dichiarante= data.get("dichiarante");
				tipoPersona = dichiarante.get("tipoPersona").getTextValue();
				result.setTipoContribuente(tipoPersona);
				if (tipoPersona.equals("personaFisica")) {
					if(dichiarante.has("personaFisica")) {
						JsonNode personaFisica= dichiarante.get("personaFisica");
						result.setNome(personaFisica.get("nome").getTextValue());
						result.setCognome(personaFisica.get("cognome").getTextValue());
						result.setCodiceUtente(personaFisica.get("cf").getTextValue());
					}
				}
				else {
					if(dichiarante.has("personaGiuridica")) {
						JsonNode personaGiuridica= dichiarante.get("personaGiuridica");
						result.setRagioneSociale(personaGiuridica.get("ragionesociale").getTextValue());
						result.setCodiceUtente(personaGiuridica.get("cf").getTextValue());
					}
				}
			}
			else {
				tipoPersona = "personaFisica";
				result.setTipoContribuente(tipoPersona);
			}
			if(data.has("richiedente")) {
				JsonNode richiedente= data.get("richiedente");
				String email = richiedente.get("email").getTextValue();
				result.setEmail(email);
			}
			String indirizzoCompleto = "";
			String codiceUtenza = "";
			if(data.has("dichiarazioni")) {	
				JsonNode dichiarazioni = data.get("dichiarazioni");
				if(dichiarazioni.has("tipoLocali")) {	
					JsonNode tipoLocali = dichiarazioni.get("tipoLocali");
					JsonNode locale = null;			
					
					if ( (tipoLocali.has("attivitaPrevalente") && tipoLocali.get("attivitaPrevalente").asBoolean()) || 
						 (tipoLocali.has("abitazione") && tipoLocali.get("abitazione").asBoolean()) ) {
						if ( tipoLocali.has("attivitaPrevalente") && tipoLocali.get("attivitaPrevalente").asBoolean()) {
							locale = data.get("attivitaPrevalente");
						}
						if (tipoLocali.has("abitazione") && tipoLocali.get("abitazione").asBoolean() ) {
							locale = data.get("abitazione");
						}
					}
					else {
						if (tipoLocali.has("accessori") && tipoLocali.get("accessori").asBoolean()) {
							locale = data.get("accessori");
						}
						else {
							if (tipoLocali.has("cantina") && tipoLocali.get("cantina").asBoolean()) {
								locale = data.get("cantina");
							}
							else {
								if (tipoLocali.has("soffitta") && tipoLocali.get("soffitta").asBoolean()) {
									locale = data.get("soffitta");
								}
								else {
									if (tipoLocali.has("box") && tipoLocali.get("box").asBoolean()) {
										locale = data.get("box");
									}
								}
							}
						}
					}	
					
					if (locale != null) {
						JsonNode indirizzo = null;
						if (locale.has("abitazionePrincipale")) {
							JsonNode abitazionePrincipale = locale.get("abitazionePrincipale");
							indirizzo = abitazionePrincipale.get("indirizzo");
						}
						else {
							JsonNode immobile = locale.get("immobile");
							indirizzo = immobile.get("indirizzo");
						}
						if (indirizzo != null) {
							if (indirizzo.has("civicoNonInElenco") && indirizzo.get("civicoNonInElenco").asBoolean()) {
							
								String nomeVia = (indirizzo.has("via") && indirizzo.get("via").has("nome")) ? 
										indirizzo.get("via").get("nome").asText() : "";
								String civico = (indirizzo.has("civicoManuale")) ? 
										indirizzo.get("civicoManuale").asText() : "";
								indirizzoCompleto = nomeVia + " " + civico;
							}
							else {
								String nomeVia = (indirizzo.has("civicocompleto") && indirizzo.get("civicocompleto").has("nome")) ? 
										indirizzo.get("civicocompleto").get("nome").asText() : "";
								String pianoNui = (indirizzo.has("pianoNui") && indirizzo.get("pianoNui").has("nome")) ? 
										indirizzo.get("pianoNui").get("nome").asText() : "";
								String piano = (indirizzo.has("piano") && indirizzo.get("piano").has("nome")) ? 
										indirizzo.get("piano").get("nome").asText() : "";
								indirizzoCompleto = nomeVia + " " + pianoNui + piano;
							}
						}
					}
				}
				codiceUtenza = indirizzoCompleto;
				result.setCodiceUtenza(codiceUtenza);
			}
			else {
				// caso modulo da sportello
				JsonNode indirizzo = data.get("indirizzo");
				if (indirizzo.has("civicoNonInElenco") &&  indirizzo.get("civicoNonInElenco").asBoolean()) {
				
					String nomeVia = (indirizzo.has("via") && indirizzo.get("via").has("nome")) ? 
							indirizzo.get("via").get("nome").getTextValue() : "";
					String civico = (indirizzo.has("civicoManuale")) ? 
							indirizzo.get("civicoManuale").getTextValue() : "";
					indirizzoCompleto = nomeVia + " " + civico;
				}
				else {
					String nomeVia = (indirizzo.has("civicocompleto") && indirizzo.get("civicocompleto").has("nome")) ? 
							indirizzo.get("civicocompleto").get("nome").getTextValue() : "";
					String pianoNui = (indirizzo.has("pianoNui") && indirizzo.get("pianoNui").has("nome")) ? 
							indirizzo.get("pianoNui").get("nome").getTextValue() : "";
					String piano = (indirizzo.has("piano") && indirizzo.get("piano").has("nome")) ? 
							indirizzo.get("piano").get("nome").getTextValue() : "";
					indirizzoCompleto = nomeVia + " " + pianoNui + piano;
				}
				codiceUtenza = indirizzoCompleto;
				result.setCodiceUtenza(codiceUtenza);
			}
			
			if(data.has("tipologiaRichiesta")) {
				result.setTipologiaRichiesta(data.get("tipologiaRichiesta").getTextValue());
			}
			if(data.has("tipologiaUtenza")) {
				result.setTipologiaUtenza(data.get("tipologiaUtenza").getTextValue());
			}
			if(data.has("dataRicezione")) {
				result.setDataRicezione(data.get("dataRicezione").getTextValue());
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
	
	public static TariAreraEntity parseAzioneAccogliRespingi(String datiIstanza) throws BusinessException {

		TariAreraEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parseAzioneAccogliRespingi] IN datiIstanza=" + datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode) istanzaNode.get("data");

			result = new TariAreraEntity();
			if (data.has("codiceUtente")) {
				result.setCodiceUtente(data.get("codiceUtente").getTextValue());
			}
			if (data.has("nome") && data.get("nome") != null) {
				result.setNome(data.get("nome").getTextValue());
			}
			if (data.has("cognome") && data.get("cognome") != null) {
				result.setCognome(data.get("cognome").getTextValue());
			}
			if (data.has("ragioneSociale") && data.get("ragioneSociale") != null) {
				result.setRagioneSociale(data.get("ragioneSociale").getTextValue());
			}
			if (data.has("tipoContribuente") && data.get("tipoContribuente") != null) {
				result.setTipoContribuente(data.get("tipoContribuente").getTextValue());
			}
			if (data.has("email") && data.get("email") != null) {
				result.setEmail(data.get("email").getTextValue());
			}
			if (data.has("codiceUtenza") && data.get("codiceUtenza") != null) {
				result.setCodiceUtenza(data.get("codiceUtenza").getTextValue());
			}
			if (data.has("tipologiaUtenza") && data.get("tipologiaUtenza") != null) {
				result.setTipologiaUtenza(data.get("tipologiaUtenza").getTextValue());
			}
			if (data.has("tipologiaRichiesta") && data.get("tipologiaRichiesta") != null) {
				result.setTipologiaRichiesta(data.get("tipologiaRichiesta").getTextValue());
			}
			if (data.has("dataRicezione") && data.get("dataRicezione") != null) {
				result.setDataRicezione(data.get("dataRicezione").getTextValue());
			}
			if (data.has("dataChiusura") && data.get("dataChiusura") != null) {
				result.setDataChiusura(data.get("dataChiusura").getTextValue());
			}
			if (data.has("causaDelMancatoRispettoDelloStandardDiQualita") && data.get("causaDelMancatoRispettoDelloStandardDiQualita") != null) {
				result.setCausaDelMancatoRispettoDelloStandardDiQualita(data.get("causaDelMancatoRispettoDelloStandardDiQualita").getTextValue());
			}
			if (data.has("causaDelMancatoObbligoDiRisposta") && data.get("causaDelMancatoObbligoDiRisposta") != null) {
				result.setCausaDelMancatoObbligoDiRisposta(data.get("causaDelMancatoObbligoDiRisposta").getTextValue());
			}
			if (data.has("testoRisposta") && data.get("testoRisposta") != null) {
				result.setTestoRisposta(data.get("testoRisposta").getTextValue());
			}
			if (data.has("noteDiLavorazione") && data.get("noteDiLavorazione") != null) {
				result.setNoteDiLavorazione(data.get("noteDiLavorazione").getTextValue());
			}
			
			List<String> emailCc = new ArrayList<String>();
			if (data.has("emailCc") &&  data.get("emailCc") != null && data.get("emailCc").isArray()){		
				ArrayNode elenco = (ArrayNode)data.get("emailCc");
				Iterator<JsonNode>it = elenco.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();						
					emailCc.add(elemento.get("email").getTextValue());				
				}
			}
			result.setEmailCc(emailCc);
			
			List<String> emailCcn = new ArrayList<String>();
			if (data.has("emailCcn") &&  data.get("emailCcn") != null && data.get("emailCcn").isArray()){		
				ArrayNode elenco = (ArrayNode)data.get("emailCcn");
				Iterator<JsonNode>it = elenco.iterator();
				while (it.hasNext()) {
					JsonNode elemento = it.next();						
					emailCc.add(elemento.get("email").getTextValue());				
				}
			}
			result.setEmailCcn(emailCcn);
			
			List<String> elencoFormIoFileNames = new ArrayList<String>();
			if (data.has("allegati") &&  data.get("allegati") != null && data.get("allegati").isArray()) {		
				ArrayNode elenco = (ArrayNode)data.get("allegati");
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
			log.error("[" + CLASS_NAME + "::parseAzioneAccogliRespingi] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parseAzioneAccogliRespingi] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parseAzioneAccogliRespingi] OUT result:" + result);
		}
	}
}
