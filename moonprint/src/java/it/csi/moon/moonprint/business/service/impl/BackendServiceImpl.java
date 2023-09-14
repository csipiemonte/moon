/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.business.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonprint.business.service.BackendService;

@Component
@Qualifier("provaprovaprova")
public class BackendServiceImpl  implements BackendService{

	public BackendServiceImpl() {
		
		// TODO Auto-generated constructor stub
	}

	public String getMessage(){
		return "moonprint backend service OK. [BackendServiceImpl.getMessage()]";
	}

}
