/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.epay.impl;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.moonsrv.business.service.epay.impl.coto.EpayDelegate_EDIL_DEF_COND_PPAY;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.Constants;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class EpayDelegateFactory {

	private static final String CLASS_NAME = "EpayDelegateFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	public EpayDelegate getDelegate(Istanza istanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getDelegate] IN codiceModulo=" + istanza.getModulo().getCodiceModulo() );
		switch(istanza.getModulo().getCodiceModulo().toUpperCase()) {
			case Constants.CODICE_MODULO_EDIL_DEF_COND:
			case Constants.CODICE_MODULO_EDIL_DEF_COND_PPAY:
				return new EpayDelegate_EDIL_DEF_COND_PPAY(istanza);
			default:
				return new EpayDefaultDelegate(istanza);
		}
	}

}
