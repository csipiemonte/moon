/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrIbanEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ext.CognoneNomeCodFiscEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class DatiIstanzaHelper_CONT_COMUNE extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "DatiIstanzaHelper_CONT_COMUNE";
	
	public DatiIstanzaHelper_CONT_COMUNE(String datiIstanza) throws BusinessException {
		initDataNode(datiIstanza);
	}

	public OneriCostrIbanEntity readComune() throws BusinessException {
		OneriCostrIbanEntity result = null;
		try {			
			result = new OneriCostrIbanEntity();
			result.setCodIstat(dataNode.get("comune")!=null?String.format("%06d", dataNode.get("comune").get("codice").getIntValue()):null);
			result.setNomeComune(dataNode.get("comune")!=null?dataNode.get("comune").get("nome").getTextValue():null);
			result.setIban(dataNode.get("iban")!=null?dataNode.get("iban").getTextValue():null);

			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::readComune] Exception ", e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::readComune] OUT result:" + result);
		}
	}

	public List<CognoneNomeCodFiscEntity> readOperatori() throws BusinessException {
		List<CognoneNomeCodFiscEntity> result = null;
		try {			
			result = new ArrayList<CognoneNomeCodFiscEntity>();
			if ( dataNode.get("operatori")!=null ) {
				for (JsonNode ope : dataNode.get("operatori")) {
					CognoneNomeCodFiscEntity entity = new CognoneNomeCodFiscEntity();
					entity.setCognome(ope.get("cognome")!=null?ope.get("cognome").getTextValue():null);
					entity.setNome(ope.get("nome")!=null?ope.get("nome").getTextValue():null);
					entity.setCodicefiscale(ope.get("codiceFiscale")!=null?ope.get("codiceFiscale").getTextValue():null);
					if (log.isDebugEnabled())  {
						log.debug("[" + CLASS_NAME + "::readOperatori] ADD " + entity);
					}
					result.add(entity);
				}
			}
			log.debug("[" + CLASS_NAME + "::readOperatori] OUT result.size(): " + result.size());
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::readOperatori] Exception ", e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::readOperatori] OUT result:" + result);
		}
	}

}
