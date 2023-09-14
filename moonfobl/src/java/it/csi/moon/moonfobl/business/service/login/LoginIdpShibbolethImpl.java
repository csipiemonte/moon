/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.login;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.DatiAggiuntivi;
import it.csi.moon.commons.dto.DatiAggiuntiviHeaders;
import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.AmbitoEntity;
import it.csi.moon.commons.util.HeadersUtils;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.impl.dao.AmbitoDAO;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.util.EnvProperties;
import it.csi.moon.moonfobl.util.IrideIdentitaDigitale;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * Login con IDP Shiboleth
 * 
 * @author laurent
 *
 */
@Component
public class LoginIdpShibbolethImpl extends BaseLoginImpl implements Login {

	private static final String CLASS_NAME = "LoginIdpShibbolethImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

    private static final Set<String> HEADER_SHIB_ID_STARTWITH = Collections.unmodifiableSet(new HashSet<>(
        	Arrays.asList( 
        		"",
       	)));
    
	@Autowired
	AuditService auditService;
	@Autowired
	AmbitoDAO ambitoDAO;
	
	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws BusinessException, BusinessException, UnauthorizedException {
		try {
			LOG.info("[" + CLASS_NAME + "::login] BEGIN");
			LOG.info("[" + CLASS_NAME + "::login] LoginRequest " + loginRequest);
			validaLoginRequest(loginRequest);
			
			// login
			LoginResponse result = _login(loginRequest, httpRequest);
			result.setDatiAggiuntivi(makeDatiAggiuntivi(HeadersUtils.readFromHeaders(httpHeaders)));
			
			// IdMoonToken
			final String idMoonToken = jwtTokenUtil.generateToken(loginRequest, result);
			logDebugJwtToken(idMoonToken);
			result.setIdMoonToken(idMoonToken);

			LOG.info("[" + CLASS_NAME + "::login] LoginOK result="+result);
			return result;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::login] BusinessException ", be);
			throw be;
	//		throw new BusinessException("NON RISULTI RESIDENTE A TORINO, SE DI RECENTE HAI FATTO UNA PRATICA DI IMMIGRAZIONE RIVOLGITI AL CALL CENTER");
		}
	}

	
	private DatiAggiuntivi makeDatiAggiuntivi(DatiAggiuntiviHeaders daHeaders) {
		DatiAggiuntivi datiAggiuntivi = new DatiAggiuntivi();
		datiAggiuntivi.setDataOraLogin(new Date());
		datiAggiuntivi.setProvider(daHeaders.getShibIdentitaProvider());
		datiAggiuntivi.setHeaders(daHeaders);
		return datiAggiuntivi;
	}


	private LoginResponse _login(LoginRequest loginRequest,  HttpServletRequest httpRequest) throws BusinessException, BusinessException, UnauthorizedException {
		LoginResponse result = null;
//		COTO_EXTRACOM_10 moon-extracom.patrim.csi.it
		
//	    checkLogin(nomePortale, LogonModeEnum.IDP_SHIBBOLETH.name(), loginRequest);	
						
		result = new LoginResponse(_login_GIA_AUTH(loginRequest,  httpRequest));
//		result.setProvider(loginRequest.getProvider());
		String urlLogoutKey = EnvProperties.LOGOUT_URL_PREFIX+loginRequest.getNomePortale()+"."+loginRequest.getProvider() ;
		LOG.debug("[" + CLASS_NAME + "::urlLogoutKey ] " + urlLogoutKey);
		
		//result.setUrlLogout(EnvProperties.readFromFile(EnvProperties.LOGOUT_URL_PREFIX+loginRequest.getNomePortale()+"."+loginRequest.getProvider()));
		result.setUrlLogout(EnvProperties.readFromFile(urlLogoutKey));
		result.setIdIride(httpRequestUtils.getShibToken(httpRequest, devmode));
//		if (DecodificaPortale.COTO_EXTRACOM_10.hasNomePortale(nomePortale)||DecodificaPortale.TOFACILE_BO_3.hasNomePortale(nomePortale)
//				|| DecodificaPortale.TOFACILE_BO_TEST_9.hasNomePortale(nomePortale)) {
//			// COTO          TO_SOLIDALE
//			result = new LoginResponse(_login_COTO(loginRequest, nomePortale, httpServletRequest));
//			if (!Constants.CODICE_MODULO_TO_SOLIDALE.equals(loginRequest.getCodiceModulo())) {
//				LOG.error("[" + CLASS_NAME + "::_login] BusinessException : Modulo non pubblicato sul Portale COTO_EXTRACOM_10 || TOFACILE_BO_3 || TOFACILE_BO_TEST_9: "+loginRequest.getCodiceModulo());
//				throw new BusinessException("Modulo non pubblicato sul Portale");
//			}
//		}

		result = completaLoginResponseModulo(result, loginRequest); // QUI con contoTerzi
		LOG.info("[" + CLASS_NAME + "::_login] LoginOK result="+result);
		return result;
	}

	
	private UserInfo _login_GIA_AUTH(LoginRequest loginRequest, HttpServletRequest httpRequest) throws BusinessException, UnauthorizedException {
		try {
			String marker = httpRequestUtils.getShibToken(httpRequest, devmode);
			IrideIdentitaDigitale identita = null;
			String cf, cognome, nome;
			if (marker == null) {
				// Seconda soluzione SPID
				cf = httpRequestUtils.retrieveCodiceFiscale(httpRequest);
				if (StringUtils.isBlank(cf)) {
					// il marcatore deve sempre essere presente altrimenti e' una 
					// condizione di errore (escluse le pagine home e di servizio)
					LOG.error(
						"[" + CLASS_NAME + "::_login_GIA_AUTH] MOONFOBL-00011 - Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza from " + httpRequest.getRemoteAddr() + " for " + httpRequest.getRequestURI());
		            throw new UnauthorizedException (
						"Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza", "MOONFOBL-00011");
				}
				cognome = httpRequestUtils.retrieveCognome(httpRequest);
				nome = httpRequestUtils.retrieveNome(httpRequest);
			} else {
				logHeadersOnUsersForDebug(marker, httpRequest);
				identita = new IrideIdentitaDigitale(marker);
				cf = identita.getCodFiscale();
				cognome = identita.getCognome();
				nome = identita.getNome();
				LOG.debug("[" + CLASS_NAME + "::_login_GIA_AUTH] IRIDE:" + identita);
			}
			if (identita==null) {
				LOG.error("[" + CLASS_NAME + "::_login_GIA_AUTH] identita null.");
				throw new BusinessException();
			}
			String shibCognome = httpRequestUtils.retrieveCognome(httpRequest);
			cognome = shibCognome!=null?shibCognome.toUpperCase():cognome;
			String shibNome = httpRequestUtils.retrieveNome(httpRequest);
			nome = shibNome!=null?shibNome.toUpperCase():nome;
			UserInfo userInfo = buildUserInfo(identita.toString(), cf, cognome, nome, loginRequest.getNomePortale(), httpRequest, httpRequestUtils.retrieveMail(httpRequest));
			userInfo = completaUserInfoOfIsOperatore(userInfo, loginRequest, httpRequest);
//				validaPresenzaJwtIfRequired(userInfo);

			return userInfo;
		} catch (Exception e) { // MalformedIdTokenException e) {
			LOG.error("[" + CLASS_NAME + "::_login_GIA_AUTH] " + e.toString(), e);
			throw new BusinessException();
		}
	}


	private UserInfo buildUserInfo(String identitaIrideStr, String cf, String cognome, String nome, String portalName, HttpServletRequest httpRequest, String mail) throws DAOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN identitaIride:" + identitaIrideStr);
			LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN portalName:" + portalName);
		}
		LOG.debug("[" + CLASS_NAME + "::buildUserInfo] userInfo from identita..." );
		UserInfo userInfo = new UserInfo();
		userInfo.setIdentificativoUtente(cf);
		userInfo.setNome(nome);
		userInfo.setCognome(cognome);
		userInfo.setCodFiscDichIstanza(cf);
		userInfo.setMail(mail);
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
		userInfo.setIdIride(identitaIrideStr);
		//
		String jwt = httpRequestUtils.retrieveJWT(httpRequest, devmode);
		userInfo.setJwt(jwt);
		return userInfo;
	}

	private void validaLoginRequest(LoginRequest loginRequest) throws BusinessException {
		Set<String> validationErrors = new HashSet<>();
		if (StringUtils.isEmpty(loginRequest.getNomePortale())) {
			validationErrors.add("nomePortale");
		}
		if (StringUtils.isEmpty(loginRequest.getProvider())) {
			validationErrors.add("provider");
		}
		if (!validationErrors.isEmpty()) {
			LOG.error("[" + CLASS_NAME + "::validaLoginRequest] Elementi obbligatori mancanti nell'oggetto LoginRequest: " + String.join(", ", validationErrors));
			throw new BusinessException("Invalid LoginRequest");
		}
	}
	

	private void logHeadersOnUsersForDebug(String marker, HttpServletRequest httpreq) {
		boolean isToLog = false;
		Iterator<String> cfIterator = HEADER_SHIB_ID_STARTWITH.iterator();
    	while(!isToLog && cfIterator.hasNext()) { 
			if (!isToLog && marker.startsWith(cfIterator.next())) {
				isToLog = true;
			}
    	}
    	if (isToLog) {
    		LOG.info("[" + CLASS_NAME + "::logHeadersOnUsersForDebug] ====================================================================================");
    		LOG.info("[" + CLASS_NAME + "::logHeadersOnUsersForDebug] marker: " + marker);
    		httpRequestUtils.logInfoAllHeaders(httpreq);
    		LOG.info("[" + CLASS_NAME + "::logHeadersOnUsersForDebug] ====================================================================================");
    	}
	}
	
}
