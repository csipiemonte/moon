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
import it.csi.moon.commons.util.decodifica.DecodificaPortale;
import it.csi.moon.moonfobl.business.service.impl.dao.AmbitoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.dto.moonfobl.LogonModeEnum;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.service.ForbiddenException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.util.LoggerAccessor;


/**
 * Usato per :
 *  - RIL_TERMOSCANNER     su  6-REGP moon.patrim.csi.it || 8-REGP_TEST moon-internet.patrim.csi.it
 *  
 * @author laurent
 *
 */

@Component
public class LoginUsrPinImpl extends BaseLoginImpl implements Login {
	
	private static final String CLASS_NAME = "LoginUsrPinImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
 
//	@Autowired
//    UtenteDAO utenteDAO;
	@Autowired
	AmbitoDAO ambitoDAO;

	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders,  HttpServletRequest httpServletRequest) throws BusinessException {
		try {
			validaLoginRequest(loginRequest);
			
			// login
			LoginResponse result = _login(loginRequest,  httpServletRequest);
	
			// IdMoonToken
			String idMoonToken = jwtTokenUtil.generateToken(loginRequest, result);
			logDebugJwtToken(idMoonToken);
			result.setIdMoonToken(idMoonToken);
			
			return result;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::login] DAOException - Errore invocazione DAO - ", be);
			throw be;
		}
	}
	
	private LoginResponse _login(LoginRequest loginRequest,  HttpServletRequest httpServletRequest/*, ModuloEntity moduloE, MapModuloAttributi attributi*/) throws ResourceNotFoundException,ForbiddenException,BusinessException, BusinessException {
		LoginResponse result = null;
//		6-REGP moon.patrim.csi.it OR 8-REGP_TEST moon-internet.patrim.csi.it
		
		checkLogin(loginRequest.getNomePortale(),LogonModeEnum.LOGIN_PIN.name(),loginRequest);	
		
		try {
			result = new LoginResponse(_login_PIN(loginRequest,httpServletRequest));
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::_login] DAOException - Errore invocazione DAO - ", e);
			throw new BusinessException("Errore accesso");
		}
//		parametrizzazione da tabella moon_ml_d_logon_mode
		
//		if (DecodificaPortale.REGP_6.hasNomePortale(nomePortale) || DecodificaPortale.REGP_TEST_8.hasNomePortale(nomePortale)) {
//			// REGP          RIL_TERMOSCANNER
//			result = new LoginResponse(_login_REGP(loginRequest,nomePortale));
//			if (!Constants.CODICE_MODULO_RIL_TERMOSCANNER.equals(loginRequest.getCodiceModulo())) {
//				LOG.error("[" + CLASS_NAME + "::_login] BusinessException : Modulo non pubblicato su quel Portale REGP_6 || REGP_TEST_8 : "+loginRequest.getCodiceModulo());
//				throw new BusinessException("Modulo non pubblicato su quel Portale");
//			}
//		}
		

				
		result = completaLoginResponseModuloNoContoTerziIfRequested(result, loginRequest);
		LOG.info("[" + CLASS_NAME + "::_login] LoginOK result="+result);
		return result;
	}


	// usare moon_fo_t_utente. usernamne e password   e    
	// verificare utente abilitato al modulo   moon_fo_r_utente_modulo 
	// Capire la necessita di introdure un ruolo ?!  tipo  FO_SIMPL
	// INSERT 1 riga in moon_fo_t_utente         : 1 Utente (tipoUtente 4-CIT) scuola/a12345z  cf:scuola  cognome:scuola  nome:""
	// INSERT 1 riga in moon_fo_r_utente_modulo  : abilitarlo al modulo TERM_SCANNER
	// IMPORT ritornare UserInfo con idEnte del Portale collegato      dao o DecodificaPortale.byNomePortale(nomePortale).getIdEnte());
	@Deprecated
	private UserInfo _login_REGP(LoginRequest loginRequest, String nomePortale, HttpServletRequest httpServletRequest) throws ResourceNotFoundException,ForbiddenException, BusinessException, DAOException {
		LOG.info("[" + CLASS_NAME + "::_login_REGP] BEGIN");
//		final String loginEXPECTED = "scuola";
//		final String pinEXPECTED = "a12345z";
//		if(!loginRequest.getLogin().trim().equalsIgnoreCase(loginEXPECTED) || !loginRequest.getPin().trim().equalsIgnoreCase(pinEXPECTED)) {
//			LOG.error("[" + CLASS_NAME + "::_login_REGP] BusinessException 3 NoMatchPIN login=" + loginRequest.getLogin() + "  pinInserted:"+loginRequest.getPin()+" expected:"+pinEXPECTED+ "  " + BusinessException.ErrType.CREDENZIALI_NON_VALIDE.toString());
//			throw new BusinessException(BusinessException.ErrType.CREDENZIALI_NON_VALIDE);
//		}
//		LOG.debug("[" + CLASS_NAME + "::_login_REGP] LoginOK ");
//		EnteEntity ente = enteDAO.findByCodice(Constants.CODICE_ENTE_REGP);
//		return new UserInfo(loginEXPECTED, loginEXPECTED, "", ente.getIdEnte());
		
		// 1.ricerca Utente fittizio e verifica Abilitazione al modulo
		UtenteEntity utente;
		try {
			utente = utenteDAO.findByUsrPwd(loginRequest.getLogin(),loginRequest.getPin());	
		} catch (ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::_login_REGP] MOONFOBL-00013 - Login Password non corretto login["+loginRequest.getLogin()+"]   for " + httpServletRequest.getRemoteAddr() + " for " + httpServletRequest.getRequestURI());
			throw new UnauthorizedException ("Login Password non corretto", "MOONFOBL-00013");
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::_login_REGP] MOONFOBL-00014 - Login Password non corretto login["+loginRequest.getLogin()+"]   for " + httpServletRequest.getRemoteAddr() + " for " + httpServletRequest.getRequestURI(), daoe);
			throw new UnauthorizedException ("Login Password non corretto generico", "MOONFOBL-00014");
		}
					
//		// 2. verif abilitazioe al modulo
		if (!utenteDAO.isUtenteAbilitato(utente.getIdUtente(),loginRequest.getCodiceModulo())) throw new ForbiddenException();
		
		return new UserInfo(utente.getIdentificativoUtente(), utente.getCognome(), utente.getNome(), DecodificaPortale.byNomePortale(nomePortale).getIdEnte());
	
	}
	
	
	private UserInfo _login_PIN(LoginRequest loginRequest, HttpServletRequest httpServletRequest)
			throws ResourceNotFoundException, ForbiddenException, BusinessException, DAOException {
		LOG.info("[" + CLASS_NAME + "::_login_PIN] BEGIN");

		UtenteEntity utente;
		try {
			utente = utenteDAO.findByUsrPwd(loginRequest.getLogin(),loginRequest.getPin());	
		} catch (ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::_login_PIN] MOONFOBL-00013 - Login Password non corretto login["+loginRequest.getLogin()+"]   for " + httpServletRequest.getRemoteAddr() + " for " + httpServletRequest.getRequestURI());
			throw new UnauthorizedException ("Login Password non corretto", "MOONFOBL-00013");
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::_login_PIN] MOONFOBL-00014 - Login Password non corretto login["+loginRequest.getLogin()+"]   for " + httpServletRequest.getRemoteAddr() + " for " + httpServletRequest.getRequestURI(), daoe);
			throw new UnauthorizedException ("Login Password non corretto generico", "MOONFOBL-00014");
		}

		if (!utenteDAO.isUtenteAbilitato(utente.getIdUtente(), loginRequest.getCodiceModulo()))
			throw new ForbiddenException();
		UserInfo userInfo = new UserInfo(utente.getIdentificativoUtente(), utente.getCognome(), utente.getNome(),
				DecodificaPortale.byNomePortale(loginRequest.getNomePortale()).getIdEnte());
		
		String portalName = loginRequest.getNomePortale();
		Ente ente = retrieveEnte(portalName, httpRequestUtils.getCodiceEnteRequestParam(httpServletRequest)); // Ente per il modulo
		userInfo.setEnte(ente);
		userInfo.setMultiEntePortale(isPortaleMultiEnte(portalName));
		userInfo.setPortalName(portalName);
		String codiceAmbiente = httpRequestUtils.getCodiceAmbitoRequestParam(httpServletRequest);
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
		if (StringUtils.isEmpty(loginRequest.getPin())) {
			throw new BusinessException("[" + CLASS_NAME + "::validaLoginRequest] Pin obbligatorio.");
		}
	}

}
