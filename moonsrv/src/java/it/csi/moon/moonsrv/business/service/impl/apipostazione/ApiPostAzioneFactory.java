/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.apipostazione;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class ApiPostAzioneFactory {

	private static final String CLASS_NAME = "ApiPostAzioneFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	public ApiPostAzione getApiPostAzione(String codiceAzione, Istanza istanza, Long idStoricoWorkflow) {
		ApiPostAzione result = null;
    	try {
    		switch (istanza.getModulo().getCodiceModulo()) {
    			default :
    				result = new ApiPostAzioneDefault(codiceAzione,istanza, idStoricoWorkflow);
    				break;
    		}
    	} catch (Exception e) {
    		LOG.error("[" + CLASS_NAME + "::getApiPostAzione] Exception ", e);
		} finally {
			LOG.debug("[" + CLASS_NAME + "::getApiPostAzione] OUT ApiPostAzione = " + result);
		}
    	return result;
	}
	
}
