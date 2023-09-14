/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.login;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.DatiAggiuntivi;
import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.util.EnvProperties;
import it.csi.moon.moonfobl.util.LoggerAccessor;

//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@Component
public class LoginGoogleImpl extends BaseLoginImpl implements Login {

	private static final String CLASS_NAME = "LoginGoogleImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
//	@Autowired
//	UtenteDAO utenteDAO;
	
	private DatiAggiuntivi makeDatiAggiuntivi(JsonNode socialU) {
		DatiAggiuntivi datiAggiuntivi = new DatiAggiuntivi();
		datiAggiuntivi.setDataOraLogin(new Date());
		datiAggiuntivi.setProvider("google");
		return datiAggiuntivi;
	}
	
	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			 throws BusinessException, BusinessException, UnauthorizedException {
	
		LoginResponse result = null;
		try {
			LOG.info("[" + CLASS_NAME + "::login] BEGIN");
			LOG.info("[" + CLASS_NAME + "::login] LoginRequest " + loginRequest);
			
			// login
			result = _login(loginRequest,  httpRequest);
			//result.setDatiAggiuntivi(makeDatiAggiuntivi(loginRequest.getSocialUser());
			
			// IdMoonToken
			final String idMoonToken = jwtTokenUtil.generateToken(loginRequest, result);
			logDebugJwtToken(idMoonToken);
			result.setIdMoonToken(idMoonToken);
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::login] BusinessException - Errore invocazione DAO - ", e);
			throw new BusinessException("Errore accesso");
	//		throw new BusinessException("NON RISULTI RESIDENTE A TORINO, SE DI RECENTE HAI FATTO UNA PRATICA DI IMMIGRAZIONE RIVOLGITI AL CALL CENTER");
		}
		return result;
	}

	private LoginResponse _login(LoginRequest loginRequest, HttpServletRequest httpRequest)  throws BusinessException, UnauthorizedException {
		LoginResponse result = null;
		UtenteEntity utenteEntity = null;
		try {
			LOG.info("[" + CLASS_NAME + "::_login]");
			String socialUser = loginRequest.getSocialUser();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode su = mapper.readValue(socialUser, JsonNode.class);
						
			result = new LoginResponse(_login_GIA_AUTH(loginRequest, httpRequest, su));
			LOG.info("[" + CLASS_NAME + "::_login]- stampo result: " + result);
			result.setPhotoUrl(su.get("photoUrl").asText());
			result.setDatiAggiuntivi(makeDatiAggiuntivi(su));

			String urlLogoutKey = EnvProperties.LOGOUT_URL_PREFIX+loginRequest.getNomePortale()+"."+loginRequest.getProvider() ;
			LOG.debug("[" + CLASS_NAME + "::urlLogoutKey ] " + urlLogoutKey);
			result.setUrlLogout(EnvProperties.readFromFile(urlLogoutKey));
						
			result = completaLoginResponseModulo(result, loginRequest); // QUI con contoTerzi
			LOG.info("[" + CLASS_NAME + "::_login] LoginOK result="+result);
			return result;
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::_login_] MOONFOBL ", daoe);
			throw new BusinessException();
		} catch (JsonParseException e) {
			LOG.error("[" + CLASS_NAME + "::_login_GIA_AUTH] " + e.toString(), e);
			throw new BusinessException();
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::_login_GIA_AUTH] " + e.toString(), e);
			throw new BusinessException();
		}
	}

	private UserInfo _login_GIA_AUTH(LoginRequest loginRequest, HttpServletRequest httpRequest, JsonNode su) throws BusinessException, UnauthorizedException, IOException {
		HttpURLConnection con= null;
		try {
			
				String authTokenGoogle = su.get("authToken").asText();
				URL myurl = new URL("https://www.googleapis.com/oauth2/v1/userinfo?alt=json");
				con = (HttpURLConnection) myurl.openConnection();
				LOG.info("[" + CLASS_NAME + "::_login_GIA_AUTH - stampo conn: " + con);
				con.setRequestMethod("GET");
				con.setRequestProperty("Authorization", "Bearer " + authTokenGoogle);
				LOG.info("[" + CLASS_NAME + "::_login_GIA_AUTH - stampo authTokenGoogle: " + authTokenGoogle);
				con.getInputStream().readAllBytes();
		

			if ( con.getResponseCode()!=200  ) {
				// il marcatore deve sempre essere presente altrimenti e' una 
				// condizione di errore (escluse le pagine home e di servizio)
				LOG.error(
						"[" + CLASS_NAME + "::_login_GIA_AUTH] MOONFOBL-00011 - Tentativo di accesso a verifica token Google da " + httpRequest.getRemoteAddr() + " a " + httpRequest.getRequestURI() + "- Response code: " + "https://www.googleapis.com/oauth2/v1/userinfo?alt=json");
				throw new UnauthorizedException (
						"Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza", "MOONFOBL-00011");
			}

			UserInfo userInfo = buildUserInfo(su, loginRequest, httpRequest);
			//userInfo = completaUserInfoOfIsOperatore(userInfo, loginRequest, httpRequest);
			//validaPresenzaJwtIfRequired(userInfo);

			return userInfo;
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}
	
	private UserInfo buildUserInfo(JsonNode su, LoginRequest loginRequest, HttpServletRequest httpRequest) throws DAOException {
		if (LOG.isDebugEnabled()) {
			//LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN identita:" + identita);
			LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN provider:" + loginRequest.getProvider());
			LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN portalName:" + loginRequest.getNomePortale());
			//LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN jwt:" + jwt);
		}
		//LOG.debug("[" + CLASS_NAME + "::buildUserInfo] userInfo from identita..." );
		UserInfo userInfo = new UserInfo();
		userInfo.setNome(su.get("firstName").asText());
		userInfo.setCognome(su.get("lastName").asText()); 
		userInfo.setIdentificativoUtente(buildIdentificativoUtenteSocial(loginRequest.getProvider(), su.get("id").asText()));
		userInfo.setMail(su.get("email").asText());
		// ENTE - Nel caso di installazione MONO_ENTE deve essere valorizzato id_ente nei file di properties (buidtime)
		Ente ente = retrieveEnte(loginRequest.getNomePortale(), httpRequestUtils.getCodiceEnteRequestParam(httpRequest)); // Ente per il modulo
		userInfo.setEnte(ente);
		userInfo.setMultiEntePortale(isPortaleMultiEnte(loginRequest.getNomePortale()));
		userInfo.setPortalName(loginRequest.getNomePortale());
		//userInfo.setRuolo("--");
		//userInfo.setIdIride(identita.toString());
		return userInfo;
	}

	private String buildIdentificativoUtenteSocial(String provider, String idUtenteProvider) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::buildIdentificativoUtenteSocial] IN provider:" + provider);
			LOG.debug("[" + CLASS_NAME + "::buildIdentificativoUtenteSocial] IN idUtenteProvider:" + idUtenteProvider);
		}
		if (StringUtils.isEmpty(provider)) {
			LOG.error("[" + CLASS_NAME + "::buildIdentificativoUtenteSocial] provider isEmpty.");
			throw new BusinessException("Errore IdentificativoUtenteSocial provider isEmpty");
		}
		if (StringUtils.isEmpty(idUtenteProvider)) {
			LOG.error("[" + CLASS_NAME + "::buildIdentificativoUtenteSocial] idUtenteProvider isEmpty.");
			throw new BusinessException("Errore IdentificativoUtenteSocial idUtenteProvider isEmpty");
		}
		// TODO DecodificaProvider per mapping provider possibile e prefisso IdentificativoUtente MOOn
		// Per il momento GOOGLE : G-
		return "G-" + idUtenteProvider;
	}
}
