/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import it.csi.moon.moonfobl.business.service.impl.dto.ext.CognoneNomeCodFiscEntity;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.OneriCostrIbanEntity;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class DatiIstanzaHelper_CONT_COMUNE extends DatiIstanzaHelper {
	
	private static final String CLASS_NAME = "DatiIstanzaHelper_CONT_COMUNE";
	
	public DatiIstanzaHelper_CONT_COMUNE(String datiIstanza) throws BusinessException {
		initDataNode(datiIstanza);
	}

	public OneriCostrIbanEntity readComune() throws BusinessException {
		OneriCostrIbanEntity result = null;
		try {			
			result = new OneriCostrIbanEntity();
			result.setCodIstat(dataNode.get("comune")!=null?String.format("%06d", dataNode.get("comune").get("codice").asInt()):null);
			result.setNomeComune(dataNode.get("comune")!=null?dataNode.get("comune").get("nome").asText():null);
			result.setIban(dataNode.get("iban")!=null?dataNode.get("iban").asText():null);

			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::readComune] Exception ", e);
			throw new BusinessException();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readComune] OUT result:" + result);
		}
	}

	public List<CognoneNomeCodFiscEntity> readOperatori() throws BusinessException {
		List<CognoneNomeCodFiscEntity> result = null;
		try {			
			result = new ArrayList<>();
			if ( dataNode.get("operatori")!=null ) {
				for (JsonNode ope : dataNode.get("operatori")) {
					CognoneNomeCodFiscEntity entity = new CognoneNomeCodFiscEntity();
					entity.setCognome(ope.get("cognome")!=null?ope.get("cognome").asText():null);
					entity.setNome(ope.get("nome")!=null?ope.get("nome").asText():null);
					entity.setCodicefiscale(ope.get("codiceFiscale")!=null?ope.get("codiceFiscale").asText():null);
					if (LOG.isDebugEnabled())  {
						LOG.debug("[" + CLASS_NAME + "::readOperatori] ADD " + entity);
					}
					result.add(entity);
				}
			}
			LOG.debug("[" + CLASS_NAME + "::readOperatori] OUT result.size(): " + result.size());
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::readOperatori] Exception ", e);
			throw new BusinessException();
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readOperatori] OUT result:" + result);
		}
	}

}
