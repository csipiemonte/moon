/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.LoginService;
import it.csi.moon.moonbobl.business.service.login.Login;
import it.csi.moon.moonbobl.business.service.login.LoginFactory;
import it.csi.moon.moonbobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonbobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class LoginServiceImpl implements LoginService {

	private final static String CLASS_NAME = "LoginServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	
	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders, HttpServletRequest httpServletRequest) throws BusinessException, UnauthorizedException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::login] IN loginRequest:"+loginRequest);
			}
//			LogonMode logonMode = getLogonMode(loginRequest.getCodiceModulo(), nomePortale);
//	    	LogonModeEnum enumLogonMode = LogonModeEnum.byName(logonMode.getLogonMode());
//			loginRequest.setLogonMode(enumLogonMode); // servira per la costr del JWT nelle Class di Login

			Login loginImpl = new LoginFactory().getLoginImpl(loginRequest.getLogonMode());
			return loginImpl.login(loginRequest, httpHeaders, httpServletRequest); //, moduloE, attributi);
			
		} catch (BusinessException be) {
			log.warn("[" + CLASS_NAME + "::login] BusinessException " + be.getMessage());
			throw be;
		} catch (UnauthorizedException ue) {
			log.warn("[" + CLASS_NAME + "::login] UnauthorizedException " + ue.getMessage());
			throw ue;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::login] Exception - Errore invocazione DAO - ", e);
			throw new BusinessException("Errore accesso");
		} 
	}	
}
