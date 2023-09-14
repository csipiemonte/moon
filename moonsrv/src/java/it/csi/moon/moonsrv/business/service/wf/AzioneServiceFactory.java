/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.wf;

import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.business.service.wf.impl.regp.Azione_TCR;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.Constants;
import it.csi.moon.moonsrv.util.LoggerAccessor;



public class AzioneServiceFactory {

	private static final String CLASS_NAME = "AzioneServiceFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	public AzioneService getService(String codiceModulo) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getService] IN codiceModulo=" + codiceModulo );
		switch(codiceModulo) {
			case Constants.CODICE_MODULO_RP_TASSE_OSS_DIS:
				return new Azione_TCR();	
			default:
				return new Azione_Default();
		}
	}

}
