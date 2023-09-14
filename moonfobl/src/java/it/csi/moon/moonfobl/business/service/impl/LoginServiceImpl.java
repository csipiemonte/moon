/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.commons.util.decodifica.DecodificaPortale;
import it.csi.moon.moonfobl.business.service.LoginService;
import it.csi.moon.moonfobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.LogonModeDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.LogonModeEntity;
import it.csi.moon.moonfobl.business.service.login.Login;
import it.csi.moon.moonfobl.business.service.login.LoginFactory;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.dto.moonfobl.LogonMode;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.LoginBusinessException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.util.EnvProperties;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class LoginServiceImpl implements LoginService {

	private static final String CLASS_NAME = "LoginServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	LogonModeDAO logonModeDAO;
	@Autowired
	EnteDAO enteDAO;
	
//	@Override
//	public LogonMode getLogonMode(String codiceModulo, String nomePortale) throws BusinessException {
//		LogonMode result = null;
//		try {
//			LOG.debug("[" + CLASS_NAME + "::getLogonMode] IN codiceModulo="+codiceModulo+"  nomePortale="+nomePortale);
//			result = DecodificaLogonModePortale.byPortale(DecodificaPortale.byNomePortale(nomePortale)).getLogonMode();
//			LOG.debug("[" + CLASS_NAME + "::getLogonMode] init result="+result);
//
//			LogonModeEntity logonMode = logonModeDAO.findLogonMode(codiceModulo,nomePortale);
//			result.setCodiceModulo(codiceModulo);
//			result.setLogonMode(logonMode.getLogonMode());
//
//			if (LogonModeEnum.IDP_SHIBBOLETH.name().equals(result.getLogonMode())) {
//				String envUrlLogout = EnvProperties.readFromFile(EnvProperties.LOGOUT_URL_PREFIX+nomePortale);
//				if (!StrUtils.isEmpty(envUrlLogout) && !envUrlLogout.startsWith("@")) {
//					result.setUrlLogout(EnvProperties.readFromFile(EnvProperties.LOGOUT_URL_PREFIX+nomePortale));
//				}
//			}
//			
//			return result;
//				
//		} catch (Exception e) {
//			LOG.error("[" + CLASS_NAME + "::getLogonMode] Exception ", e);
//			throw new BusinessException("Errore accesso");
//		} finally {
//			LOG.debug("[" + CLASS_NAME + "::getLogonMode] END result="+result);
//		}
//	}


	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders, HttpServletRequest httpServletRequest) throws BusinessException, UnauthorizedException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::login] IN loginRequest:"+loginRequest);
			}
//			LogonMode logonMode = getLogonMode(loginRequest.getCodiceModulo(), nomePortale);
//	    	LogonModeEnum enumLogonMode = LogonModeEnum.byName(logonMode.getLogonMode());
//			loginRequest.setLogonMode(enumLogonMode); // servira per la costr del JWT nelle Class di Login

			Login loginImpl = new LoginFactory().getLoginImpl(loginRequest.getLogonMode());
			return loginImpl.login(loginRequest, httpHeaders, httpServletRequest); //, moduloE, attributi);
			
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::login] BusinessException " + be.getMessage());
			throw be;
		} catch (UnauthorizedException ue) {
			LOG.warn("[" + CLASS_NAME + "::login] UnauthorizedException " + ue.getMessage());
			throw ue;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::login] Exception - Errore invocazione DAO - ", e);
			throw new BusinessException("Errore accesso");
		} 
	}

	@Override
	public LogonMode getDirectLogonMode(String codiceModulo, String codiceEnte, String nomePortale) throws LoginBusinessException, BusinessException {
		LogonMode result = new LogonMode();
		try {
			LOG.debug("[" + CLASS_NAME + "::getDirectLogonMode] IN codiceModulo="+codiceModulo+"  codiceEnte="+codiceEnte+"  nomePortale="+nomePortale);
			LogonModeEntity logonMode = logonModeDAO.findLogonMode(codiceModulo,nomePortale);
			result.setCodiceModulo(codiceModulo);
			result.setLogonMode(logonMode.getLogonMode());
			result.setIdEnte(DecodificaPortale.byNomePortale(nomePortale).getIdEnte());
			if (result.getIdEnte()<0) {
				result.setIdEnte(4L); // per Cittafacile => 4 - CSI per Modulo AVVISO_1_2022
			}
			result.setLogo(enteDAO.findById(result.getIdEnte()).getLogo());
			result.setTitoloHeader(getTitoloHeader(result.getIdEnte()));
//			if (LogonModeEnum.GIA_AUTH.name().equals(result.getLogonMode())) {
				String envUrlLogout = EnvProperties.readFromFile(EnvProperties.LOGOUT_URL_PREFIX+nomePortale);
				if (!StrUtils.isEmpty(envUrlLogout) && !envUrlLogout.startsWith("@")) {
					result.setUrlLogout(EnvProperties.readFromFile(EnvProperties.LOGOUT_URL_PREFIX+nomePortale));
				}
//			}
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getDirectLogonMode] Exception ", e);
			throw new BusinessException("Errore accesso");
		} finally {
			LOG.debug("[" + CLASS_NAME + "::getDirectLogonMode] END result="+result);
		}
	}
	
	private String getTitoloHeader(Long idEnte) {
		String result = "Modulistica Oline";
		switch (idEnte.intValue()) {
			case 1: result = "Modulistica Online della Regione Piemonte"; break;
			case 2: result = "Modulistica Oline del Comune di Torino"; break;
			case 3: result = "Modulistica Oline della Citta' Metropolitana Torino"; break;
			default: result = "Modulistica Oline"; break;
		}
		return result;
	}
	
}
