/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.text.ParseException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import it.csi.moon.moonbobl.exceptions.business.BusinessException;


/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ProtocolloUscitaHelper extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "ProtocolloUscitaHelper";

	
	public static String parse(String datiIstanza) throws BusinessException {
		String numeroProtocolloUscita = null;
		try {
			log.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			numeroProtocolloUscita = data.get("numeroProtocollo").getTextValue();
					
			return numeroProtocolloUscita;
			
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parse] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parse] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parse] OUT result:" + numeroProtocolloUscita);
		}
	}
	
	public static String parseNumeroProtocolloUscita(String datiIstanza) throws BusinessException {
		String numeroProtocolloUscita = null;
		try {
			log.debug("[" + CLASS_NAME + "::parseNumeroProtocolloUscita] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			numeroProtocolloUscita = data.get("numeroProtocollo").getTextValue();
					
			return numeroProtocolloUscita;
			
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parseNumeroProtocolloUscita] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parseNumeroProtocolloUscita] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parseNumeroProtocolloUscita] OUT result:" + numeroProtocolloUscita);
		}
	}
	
	public static String parseDataProtocolloUscita(String datiIstanza) throws BusinessException {
		String dataProtocolloUscita = null;
		try {
			log.debug("[" + CLASS_NAME + "::parseDataProtocolloUscita] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			dataProtocolloUscita = data.get("dataProtocollo").getTextValue();
					
			return dataProtocolloUscita;
			
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parseDataProtocolloUscita] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parseDataProtocolloUscita] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parseDataProtocolloUscita] OUT result:" + dataProtocolloUscita);
		}
	}
	
}
