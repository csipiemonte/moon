/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RapportoFineConcessioneEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;


/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class RapportoFineConcessioneHelper extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "RapportoFineConcessioneHelper";

	
	public static RapportoFineConcessioneEntity parse(String datiIstanza) throws BusinessException {
		RapportoFineConcessioneEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			result = new RapportoFineConcessioneEntity();
			
			JsonNode concessione= data.get("concessione");
			if(concessione.has("titolare")) {	
				String titolare = concessione.get("titolare").getTextValue();
				result.setTitolare(titolare);
			}
			if(concessione.has("email")) {	
				String email = concessione.get("email").getTextValue();
				result.setEmail(email);
			}
			if(concessione.has("codiceUtenza")) {	
				String cur = concessione.get("codiceUtenza").get("numero").getTextValue();
				result.setCur(cur);
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
	
}
