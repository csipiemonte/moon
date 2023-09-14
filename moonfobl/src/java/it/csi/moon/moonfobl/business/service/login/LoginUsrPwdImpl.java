/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.login;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.AmbitoEntity;
import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.AmbitoDAO;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * Usato per :
 *  - NOT USED
 *  
 * @author laurent
 *
 */
@Component
public class LoginUsrPwdImpl extends BaseLoginImpl implements Login {

	private static final String CLASS_NAME = "LoginUsrPwdImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

//	@Autowired
//	UtenteDAO utenteDAO;
	@Autowired
	AmbitoDAO ambitoDAO;
	
	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders,  HttpServletRequest httpServletRequest) throws BusinessException,UnauthorizedException {
		try {
			validaLoginRequest(loginRequest);
			
			// login
			LoginResponse result = _login(loginRequest, httpServletRequest);
	
			// IdMoonToken
			String idMoonToken = jwtTokenUtil.generateToken(loginRequest, result);
			logDebugJwtToken(idMoonToken);
			result.setIdMoonToken(idMoonToken);
			
			return result;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::login] DAOException - Errore invocazione DAO - ", be);
			throw be;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::login] UnauthorizedException ", ue);
			throw ue;
		}
	}
	
	private LoginResponse _login(LoginRequest loginRequest, HttpServletRequest httpServletRequest/*, ModuloEntity moduloE, MapModuloAttributi attributi*/) throws BusinessException,UnauthorizedException {
		LoginResponse result = null;
		result = new LoginResponse(_login_utente(loginRequest, httpServletRequest));
		String contextPath = httpServletRequest.getContextPath();
		result.setUrlLogout("https://"+loginRequest.getNomePortale()+contextPath+"/#/auth/user-pwd");
		result = completaLoginResponseModuloNoContoTerziIfRequested(result, loginRequest);
		LOG.info("[" + CLASS_NAME + "::_login] LoginOK result="+result);
		return result;
	}

	
	private UserInfo _login_utente(LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws UnauthorizedException {
		try {
			LOG.info("[" + CLASS_NAME + "::_login_utente] BEGIN");
			UtenteEntity utente = utenteDAO.findByUsrPwd(loginRequest.getLogin(), loginRequest.getPassword());
			LOG.debug("[" + CLASS_NAME + "::_login_utente] LoginOK ");
			UserInfo userInfo = buildUserInfo(utente, loginRequest.getNomePortale(), httpServletRequest);
			userInfo = completaUserInfoOfIsOperatore(userInfo, loginRequest, httpServletRequest);
			return userInfo;
		} catch (ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::_login_utente] MOONFOBL-00013 - Login Password non corretto login["+loginRequest.getLogin()+"]   for " + httpServletRequest.getRemoteAddr() + " for " + httpServletRequest.getRequestURI());
			throw new UnauthorizedException ("Login Password non corretto", "MOONFOBL-00013");
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::_login_utente] MOONFOBL-00014 - Login Password non corretto login["+loginRequest.getLogin()+"]   for " + httpServletRequest.getRemoteAddr() + " for " + httpServletRequest.getRequestURI(), daoe);
			throw new UnauthorizedException ("Login Password non corretto generico", "MOONFOBL-00014");
		}
	}

	
	private UserInfo buildUserInfo(UtenteEntity utente, String portalName, HttpServletRequest httpRequest) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN utente:" + utente);
			LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN portalName:" + portalName);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setNome(utente.getNome());
		userInfo.setCognome(utente.getCognome());
		userInfo.setIdentificativoUtente(utente.getIdentificativoUtente());
//		userInfo.setCodFiscDichIstanza(utente.getIdentificativoUtente());
		userInfo.setMail(utente.getEmail());
		// ENTE - Nel caso di installazione MONO_ENTE deve essere valorizzato id_ente nei file di properties (buidtime)
		Ente ente = retrieveEnte(portalName, httpRequestUtils.getCodiceEnteRequestParam(httpRequest)); // Ente per il modulo
		userInfo.setEnte(ente);
		userInfo.setMultiEntePortale(isPortaleMultiEnte(portalName));
		userInfo.setPortalName(portalName);
		String codiceAmbiente = httpRequestUtils.getCodiceAmbitoRequestParam(httpRequest);
		if (StringUtils.isNotBlank(codiceAmbiente)) {
			codiceAmbiente = codiceAmbiente.trim().toUpperCase();
			AmbitoEntity ambitoE = ambitoDAO.findByCodice(codiceAmbiente);
			userInfo.setIdAmbito(ambitoE.getIdAmbito());
			userInfo.setDescrizioneAmbito(ambitoE.getNomeAmbito());
		}
		return userInfo;
	}


	private void validaLoginRequest(LoginRequest loginRequest) throws BusinessException {
		if (StringUtils.isEmpty(loginRequest.getLogin())) {
			throw new BusinessException("[" + CLASS_NAME + "::validaLoginRequest] Login obbligatorio.");
		}
		if (StringUtils.isEmpty(loginRequest.getPassword())) {
			throw new BusinessException("[" + CLASS_NAME + "::validaLoginRequest] Password obbligatorio.");
		}
	}

}
