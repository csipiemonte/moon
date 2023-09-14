/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.wf.impl;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class CosmoDelegateFactory {

	private static final String CLASS_NAME = "CosmoDelegateFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	public CosmoDelegate getDelegate(Istanza istanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getDelegate] IN codiceModulo=" + istanza.getModulo().getCodiceModulo() );
		switch(istanza.getModulo().getCodiceModulo()) {
//			case Constants.CODICE_MODULO_COTO_RESID:
//				return new CosmoDelegate_COTO_RESID();
//			case Constants.CODICE_MODULO_COTO_RIN:
//				return new CosmoDelegate_COTO_RIN();
			default:
				return new CosmoDefaultDelegate(istanza);
		}
	}

}
