/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.protocollo;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonsrv.exceptions.business.BusinessException;

public abstract class BaseProtocollo {

	public BaseProtocollo() {
		super();
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	public ProtocolloParams readParams() throws BusinessException {
		return null;
	}

}
