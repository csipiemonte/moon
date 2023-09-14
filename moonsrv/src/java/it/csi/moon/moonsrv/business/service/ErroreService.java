/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.ErroreEntity;

public interface ErroreService {

	public void salva(ErroreEntity errore);
	
	public String retrieveUser(UserInfo userInfo);
}
