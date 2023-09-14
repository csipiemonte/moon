/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.login;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.BackendService;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.business.service.mapper.EnteMapper;
import it.csi.moon.moonbobl.business.service.mapper.TipoUtenteMapper;
import it.csi.moon.moonbobl.dto.moonfobl.Ente;
import it.csi.moon.moonbobl.dto.moonfobl.EnteAreeRuoli;
import it.csi.moon.moonbobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonbobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonbobl.util.HttpRequestUtils;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoUtente;



@Component
public class LoginUsrPwdImpl extends BaseLoginImpl implements Login {

	private final static String CLASS_NAME = "LoginUsrPwdImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	BackendService backendService;
	
	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders,  HttpServletRequest httpServletRequest) throws BusinessException,UnauthorizedException {
		try {
			validaLoginRequest(loginRequest);
			
			// login
			LoginResponse result = _login(loginRequest, httpServletRequest);
			result.setDatiAggiuntivi(makeDatiAggiuntivi(httpHeaders));
	
			// IdMoonToken
			String idMoonToken = jwtTokenUtil.generateToken(loginRequest, result);
			logDebugJwtToken(idMoonToken);
			result.setIdMoonToken(idMoonToken);
			
			return result;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::login] DAOException - Errore invocazione DAO - ", be);
			throw be;
		} catch (UnauthorizedException ue) {
			log.error("[" + CLASS_NAME + "::login] UnauthorizedException ", ue);
			throw ue;
		} catch (JsonProcessingException e) {
			log.error("[" + CLASS_NAME + "::login] JsonProcessingException - Errore generazione JWT - ", e);
			throw new BusinessException("Errore accesso JsonProcessingException");
		}
	}
	
	private LoginResponse _login(LoginRequest loginRequest, HttpServletRequest httpServletRequest/*, ModuloEntity moduloE, MapModuloAttributi attributi*/) throws BusinessException,UnauthorizedException {
		LoginResponse result = null;
		result = new LoginResponse(_login_utente(loginRequest, httpServletRequest));
		String contextPath = httpServletRequest.getContextPath();
		result.setUrlLogout("https://"+loginRequest.getNomePortale()+contextPath+"/#/auth/user-pwd");
		//TODO: da capire se serve
		//result = completaLoginResponseModuloNoContoTerziIfRequested(result, loginRequest);
		log.info("[" + CLASS_NAME + "::_login] LoginOK result="+result);
		return result;
	}

	
	private UserInfo _login_utente(LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws UnauthorizedException {
		try {
			log.info("[" + CLASS_NAME + "::_login_utente] BEGIN");
			UtenteEntity utente = utenteDAO.findByUsrPwd(loginRequest.getLogin(), loginRequest.getPassword());
			log.debug("[" + CLASS_NAME + "::_login_utente] LoginOK ");
			return buildUserInfo(utente, loginRequest.getNomePortale(), httpServletRequest);
		} catch (ItemNotFoundDAOException nfe) {
			log.error("[" + CLASS_NAME + "::_login_utente] MOONBOBL - Login Password non corretto login["+loginRequest.getLogin()+"]   for " + httpServletRequest.getRemoteAddr() + " for " + httpServletRequest.getRequestURI());
			throw new UnauthorizedException ("Login Password non corretto", "MOONBOBL");
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::_login_utente] MOONBOBL - Login Password non corretto login["+loginRequest.getLogin()+"]   for " + httpServletRequest.getRemoteAddr() + " for " + httpServletRequest.getRequestURI(), daoe);
			throw new UnauthorizedException ("Login Password non corretto generico", "MOONBOBL");
		}
	}

	
	private UserInfo buildUserInfo(UtenteEntity utente, String portalName, HttpServletRequest httpRequest) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::buildUserInfo] IN utente:" + utente);
			log.debug("[" + CLASS_NAME + "::buildUserInfo] IN portalName:" + portalName);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setNome(utente.getNome());
		userInfo.setCognome(utente.getCognome());
		userInfo.setIdentificativoUtente(utente.getIdentificativoUtente());
//		userInfo.setCodFiscDichIstanza(utente.getIdentificativoUtente());
		userInfo.setMail(utente.getEmail());
		//
		List<EnteAreeRuoli> entiAreeRuoli = backendService.getEntiAreeRuoliAttivi(utente.getIdUtente(), portalName);
		if ((entiAreeRuoli==null || entiAreeRuoli.isEmpty()) && !DecodificaTipoUtente.ADM.isCorrectType(utente)) {
			log.error("[" + CLASS_NAME + "::buildUserInfo] EntiAreaRuoliAttivi vuoto per identitaJwt.getCodFisc()=" + utente.getIdentificativoUtente() + "  idUtente="+utente.getIdUtente());
			//response.sendError(HttpServletResponse.SC_FORBIDDEN, "NoRolesForUserNonADM");
			throw new UnauthorizedException("NoRolesForUserNonADM");
		}
		userInfo.setEntiAreeRuoli(entiAreeRuoli);
		// ENTE - Nel caso di installazione MONO_ENTE deve essere valorizzato id_ente nei file di properties (buidtime)
		Ente ente = retrieveEnte(portalName, httpRequestUtils.getCodiceEnteRequestParam(httpRequest)); // Ente per il modulo
		if(ente == null) {
			ente = retrieveUnicoEnte(entiAreeRuoli);
		}
		userInfo.setEnte(ente);
		userInfo.setMultiEntePortale(isPortaleMultiEnte(portalName));
		userInfo.setPortalName(portalName);
		userInfo.setTipoUtente(TipoUtenteMapper.buildFromIdTipoUtente(utente.getIdTipoUtente()));

//		log.debug("[" + CLASS_NAME + "::buildUserInfo] result.getEntiAreeRuoli.size() = "+userInfo.getEntiAreeRuoli()!=null?userInfo.getEntiAreeRuoli().size():null);
//		log.debug("[" + CLASS_NAME + "::buildUserInfo] result.getEntiAreeRuoli = "+userInfo.getEntiAreeRuoli()!=null?userInfo.getEntiAreeRuoli():"");
		
		log.debug("[" + CLASS_NAME + "::buildUserInfo] result.getEntiAreeRuoli.size() = "+userInfo.getEntiAreeRuoli());
		
		return userInfo;
	}
	
	private Ente retrieveUnicoEnte(List<EnteAreeRuoli> entiAreeRuoli) {
		Ente result = null;
		List<Long> idEnti= entiAreeRuoli.stream()
			.mapToLong(ear -> ear.getIdEnte())
			.distinct()
			.boxed()
			.collect(Collectors.toList());
		if(idEnti.size() == 1) {
			result = EnteMapper.buildFromEntity(enteDAO.findById(idEnti.get(0)));
		}
		return result;
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
