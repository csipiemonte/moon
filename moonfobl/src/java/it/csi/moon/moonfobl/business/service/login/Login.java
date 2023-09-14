/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.login;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;

public interface Login {

	public <T extends LoginRequest> LoginResponse login(T loginRequest, HttpHeaders httpHeaders, HttpServletRequest httpServletRequest/*, ModuloEntity moduloE, MapModuloAttributi attributi*/) throws BusinessException, UnauthorizedException;
	
}
