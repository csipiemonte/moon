/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.login;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.util.decodifica.DecodificaPortale;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.dto.moonfobl.LogonModeEnum;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * Usato per :
 *  - CMTO_STRAT          su  4-CMTO moon-cittametropolitanatorino.patrim.csi.it
 *  
 * @author laurent
 *
 */
@Component
public class LoginNoAuthImpl extends BaseLoginImpl implements Login {

	private static final String CLASS_NAME = "LoginNoAuthImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	AuditService auditService;
	
	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders,  HttpServletRequest httpServletRequest) throws BusinessException {
		try {
			validaLoginRequest(loginRequest);

			// login
			LoginResponse result = _login(loginRequest/*, httpServletRequest*/);
	
			// IdMoonToken
			final String idMoonToken = jwtTokenUtil.generateToken(loginRequest, result);
			logDebugJwtToken(idMoonToken);
			result.setIdMoonToken(idMoonToken);
			
//			auditService.insertAuditLogin(httpServletRequest.getRemoteAddr(), result, nomePortale);
			
			return result;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::login] BusinessException - Errore invocazione DAO - ", e);
			throw new BusinessException("Errore accesso");
	//		throw new BusinessException("NON RISULTI RESIDENTE A TORINO, SE DI RECENTE HAI FATTO UNA PRATICA DI IMMIGRAZIONE RIVOLGITI AL CALL CENTER");
		}
	}

	
	private LoginResponse _login(LoginRequest loginRequest /*, HttpServletRequest httpServletRequest, ModuloEntity moduloE, MapModuloAttributi attributi*/) throws BusinessException {
		LoginResponse result = null;
		
//		String logonMode = dao.finLogonMode(loginRequest.getCodiceModulo(),nomePortale);   /// Tabella : codiceModulo,nomePortale , logonMode   8 righe
//      SE logonMode <> "NO_AUTH"  THEN  throw new LoginBusinessException("Modulo non pubblicato su quel Portale");
//      result = new LoginResponse(_login(loginRequest));  // idendificare idEnte in base al portale e ritornare UserInfo con idEnte  e generare LoginResponse
		
		
//      parametrizzazione attraverso tabella moon_ml_d_logon_mode		
				
		checkLogin(loginRequest.getNomePortale(),LogonModeEnum.NO_AUTH.name(),loginRequest);	
		
		result = new LoginResponse(_login(loginRequest.getNomePortale()));
		
//		4-CMTO moon-cittametropolitanatorino.patrim.csi.it
//		if (DecodificaPortale.CMTO_4.hasNomePortale(nomePortale)) {
//			// CMTO       CMTO_STRAT
//			result = new LoginResponse(_login(nomePortale));
//			if (! ( Constants.CODICE_MODULO_CMTO_STRAT.equals(loginRequest.getCodiceModulo()) || 
//					Constants.CODICE_MODULO_CMTO_PTGM.equals(loginRequest.getCodiceModulo()) || 
//					Constants.CODICE_MODULO_CMTO_PRO.equals(loginRequest.getCodiceModulo()) || 
//					Constants.CODICE_MODULO_CMTO_RA.equals(loginRequest.getCodiceModulo())
//					)) {
//				LOG.error("[" + CLASS_NAME + "::_login] LoginBusinessException : Modulo non pubblicato su quel Portale CMTO_4 : "+loginRequest.getCodiceModulo());
//				throw new LoginBusinessException("Modulo non pubblicato su quel Portale");
//			}
//		}
		result = completaLoginResponseModuloNoContoTerziIfRequested(result, loginRequest);
		LOG.info("[" + CLASS_NAME + "::_login] LoginOK result="+result);
		return result;
	}

	
	private UserInfo _login(String nomePortale) throws BusinessException {
		final String loginEXPECTED = "cmto"; // lasciare vuoto "" per tutti enti
		final String pinEXPECTED = "cmto"; // lasciare vuoto "" per tutti enti
//		EnteEntity ente = enteDAO.findByCodice(Constants.CODICE_ENTE_CMTO);  // DecodificaPortale.byNomePortale(nomePortale).getCodiceEnte();  
//		return new UserInfo(loginEXPECTED, loginEXPECTED, "", ente.getIdEnte());
		
		return new UserInfo(loginEXPECTED, loginEXPECTED, "", DecodificaPortale.byNomePortale(nomePortale).getIdEnte());
	}

	
	private void validaLoginRequest(LoginRequest loginRequest) throws BusinessException {

	}

}
