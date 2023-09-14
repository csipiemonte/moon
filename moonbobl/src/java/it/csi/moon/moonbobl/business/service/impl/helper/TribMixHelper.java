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

import it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.TributiGeneralEntity;
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
public class TribMixHelper extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "TribMixHelper";

	
	public static TributiGeneralEntity parse(String datiIstanza) throws BusinessException {
		TributiGeneralEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			result = new TributiGeneralEntity();
			String tipoPersona = "";
			if(data.has("contribuente")) {
				JsonNode contribuente= data.get("contribuente");
				tipoPersona = contribuente.get("tipoPersona").getTextValue();
				result.setTipoContribuente(tipoPersona);
				if (tipoPersona.equals("personaFisica")) {
					if(contribuente.has("personaFisica")) {
						JsonNode personaFisica= contribuente.get("personaFisica");
						result.setNome(personaFisica.get("nome").getTextValue());
						result.setCognome(personaFisica.get("cognome").getTextValue());
						result.setCodiceFiscale(personaFisica.get("codiceFiscale").getTextValue());
					}
				}
				else {
					if(contribuente.has("personaGiuridica")) {
						JsonNode personaGiuridica= contribuente.get("personaGiuridica");
						result.setRagioneSociale(personaGiuridica.get("ragionesociale").getTextValue());
						result.setCodiceFiscale(personaGiuridica.get("codiceFiscale").getTextValue());
					}
				}
			}
			String email = "";
			if (data.has("email") && data.get("email") != null)
			{
				email = data.get("email").getTextValue();
			}
			result.setEmail(email);
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
	
	public static TributiGeneralEntity parseAzione(String datiIstanza) throws BusinessException {

		TributiGeneralEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parseAzioneAccogliRespingi] IN datiIstanza=" + datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode) istanzaNode.get("data");

			result = new TributiGeneralEntity();
			if (data.has("nome") && data.get("nome") != null) {
				result.setNome(data.get("nome").getTextValue());
			}
			if (data.has("cognome") && data.get("cognome") != null) {
				result.setCognome(data.get("cognome").getTextValue());
			}
			if (data.has("ragioneSociale") && data.get("ragioneSociale") != null) {
				result.setRagioneSociale(data.get("ragioneSociale").getTextValue());
			}
			if (data.has("email") && data.get("email") != null) {
				result.setEmail(data.get("email").getTextValue());
			}
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
			if (data.has("testoRisposta") && data.get("testoRisposta") != null) {
				result.setTestoRisposta(data.get("testoRisposta").getTextValue());
			}
			
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
