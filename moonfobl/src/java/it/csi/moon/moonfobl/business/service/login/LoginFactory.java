/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.login;

import org.apache.log4j.Logger;

import it.csi.moon.moonfobl.dto.moonfobl.LogonModeEnum;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class LoginFactory {

	private static final String CLASS_NAME = "LoginFactory";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
    public LoginFactory(){}
    
    public Login getLoginImpl (String logonMode) throws BusinessException {
    	Login result = null;
        switch (LogonModeEnum.byName(logonMode)) {
            case NO_AUTH:
                result = new LoginNoAuthImpl();
                break;
            case LOGIN_PWD:
                result = new LoginUsrPwdImpl();
                break;
            case GOOGLE_ID:
                result = new LoginGoogleImpl();
                break;
            case LOGIN_PIN:
                result = new LoginUsrPinImpl();
                break;
//            case CF_DOCUMENTO:
//                result = new LoginCfDocumentoImpl();
//                break;
            case IDP_SHIBBOLETH:
                result = new LoginIdpShibbolethImpl();
                break;
            default:
            	LOG.error("[" + CLASS_NAME + "::getLoginImpl] LogonMode sconosciuto : " + logonMode);
            	throw new BusinessException("MODULO CON MODALITA DI LOGON NON IMPLEMENTATA.");
        }
        return result;
    }
    
}
