/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.processo;

import org.apache.log4j.Logger;

import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.Constants;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class ProcessoServiceDelegateFactory {

	private static final String CLASS_NAME = "ProcessoServiceDelegateFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	public ProcessoServiceDelegate getDelegate(String codiceProcesso) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getDelegate] IN codiceProcesso=" + codiceProcesso );
		switch(codiceProcesso) {
			case Constants.CODICE_PROCESSO_COTO_RESID:
				return new ProcessoDelegate_COTO_RESID();
			case Constants.CODICE_PROCESSO_COTO_RIN:
				return new ProcessoDelegate_COTO_RIN();
			case Constants.CODICE_PROCESSO_ASL_AUTOR:
				return new ProcessoDelegate_ASL_AUTOR();
			case Constants.CODICE_PROCESSO_COSMO:
				return new ProcessoDelegate_COSMO();				
			default:
				return new ProcessoDefaultDelegate();
		}
	}

}
