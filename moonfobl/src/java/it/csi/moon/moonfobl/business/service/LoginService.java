/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.dto.moonfobl.LogonMode;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.LoginBusinessException;

/**
 * Metodi di business relativi alle FAQ
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface LoginService {
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders, HttpServletRequest httpServletRequest) throws BusinessException;
	public LogonMode getDirectLogonMode(String codiceModulo, String codiceEnte, String nomePortale) throws LoginBusinessException, BusinessException;
}
