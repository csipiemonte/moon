/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import it.csi.moon.moonbobl.business.be.LoginApi;
import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.LoginService;
import it.csi.moon.moonbobl.business.service.login.LoginIdpShibbolethImpl;
import it.csi.moon.moonbobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonbobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonbobl.dto.moonfobl.LogonModeEnum;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonbobl.util.HttpRequestUtils;
import it.csi.moon.moonbobl.util.LoggerAccessor;


@Component
public class LoginApiImpl extends MoonBaseApiImpl implements LoginApi{

	private final static String CLASS_NAME = "LoginApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	private final static int MAX_COOKIE_LENGTH = 4096;
	private final static int MAX_COOKIE_AGE = 10;
	
	@Autowired 
	AuditService auditService;
	@Autowired
	public LoginService loginService;
	
	@Override
	public Response loginIdpShibboleth(String provider, String simulatePortale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			log.debug("[" + CLASS_NAME + "::loginIdpShibboleth] IN provider: " + provider);
			log.debug("[" + CLASS_NAME + "::loginIdpShibboleth] IN simulatePortale: " + simulatePortale);
			log.debug("[" + CLASS_NAME + "::loginIdpShibboleth] IN ServerName: " + httpRequest.getServerName());
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setLogonMode(LogonModeEnum.IDP_SHIBBOLETH.name()); // usato sopratutto se su usa lo stratto loginService con la factory
			//validaLoginRequest(loginRequest);
			nomePortale = validaStringPortaleName(httpRequest.getServerName(), simulatePortale);
			log.debug("[" + CLASS_NAME + "::loginIdpShibboleth] nomePortale= "  + nomePortale);
			loginRequest.setNomePortale(nomePortale);
			loginRequest.setProvider(provider);
			loginResponse = new LoginIdpShibbolethImpl().login(loginRequest, httpHeaders, httpRequest);
			//loginResponse.setCognome(loginResponse.getCognome());
			//loginResponse.setNome(loginResponse.getNome());
			//loginResponse.setIdIride(loginResponse.getIdIride());
			//
			Cookie cookieIdMoonToken = new Cookie("idMoonToken", loginResponse.getIdMoonToken());
			cookieIdMoonToken.setPath("/moonbobl");
			cookieIdMoonToken.setMaxAge(60);
			log.debug("[" + CLASS_NAME + "::loginIdpShibboleth] Valore cookieIdMoonToken: " + cookieIdMoonToken.getValue());
			httpResponse.addCookie(cookieIdMoonToken);
			//
			loginResponse.setIdMoonToken(null);
			Cookie cookieLoginResponse = new Cookie("loginResponse", buildCookie(loginResponse));
			cookieLoginResponse.setPath("/moonbobl");
			cookieLoginResponse.setMaxAge(60);
			log.debug("[" + CLASS_NAME + "::loginIdpShibboleth] Valore cookieLoginResponse: " + cookieLoginResponse.getValue());
			httpResponse.addCookie(cookieLoginResponse);
			//
			URI urlToRedirectURI = new URI(httpRequest.getScheme(),httpRequest.getServerName(),"moonbobl");
			log.debug("[" + CLASS_NAME + "::loginIdpShibboleth] URL Redirect : " + urlToRedirectURI.toString() ); 						
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonbobl" + getQueryString(httpRequest));		
			log.debug("[" + CLASS_NAME + "::loginIdpShibboleth] URL Redirect : " + urlToRedirect.toString() );
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
			if (devmode) {
				return Response.ok(loginResponse).build();
			} else {
				return Response.temporaryRedirect(urlToRedirect).build();
			}
		} catch (ResourceNotFoundException notFoundEx) {
			log.error("[" + CLASS_NAME + "::loginIdpShibboleth] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException unauthorizedEx) {
			log.error("[" + CLASS_NAME + "::loginIdpShibboleth] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonbobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				log.fatal("[" + CLASS_NAME + "::loginIdpShibboleth] UnauthorizedException + URISyntaxException", e);
			}
			throw unauthorizedEx;
		} catch (ForbiddenException forbidenEx) {
			log.error("[" + CLASS_NAME + "::loginIdpShibboleth] ForbiddenException");
			throw forbidenEx;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::loginIdpShibboleth] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::loginIdpShibboleth] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
			try {
				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
			} catch (Exception e) {
				log.error("[" + CLASS_NAME + "::login] Errore servizio Audit", e);
			}
		}
	}
	
	@Override
	public Response loginIdpUserPwd(String simulatePortale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		try {
			log.debug("[" + CLASS_NAME + "::loginIdpUserPwd] IN simulatePortale: " + simulatePortale);
			log.debug("[" + CLASS_NAME + "::loginIdpUserPwd] IN ServerName: " + httpRequest.getServerName());
			Cookie cookie = new Cookie("auth-mode", "user-pwd");
			cookie.setPath("/moonbobl");
			cookie.setMaxAge(MAX_COOKIE_AGE);
			log.debug("[" + CLASS_NAME + "::loginIdpUserPwd] Valore cookie: " + cookie.getValue());
			URI urlToRedirectURI = new URI(httpRequest.getScheme(),httpRequest.getServerName(),"moonbobl");
			log.debug("[" + CLASS_NAME + "::loginIdpUserPwd] URL Redirect : " + urlToRedirectURI.toString() ); 						
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonbobl" + getQueryString(httpRequest));		
			log.debug("[" + CLASS_NAME + "::loginIdpUserPwd] URL Redirect : " + urlToRedirect.toString() );
			httpResponse.addCookie(cookie);
			if (devmode) {
				return Response.ok().build();
			} else {
				return Response.temporaryRedirect(urlToRedirect).build();
			}
		} catch (ResourceNotFoundException notFoundEx) {
			log.error("[" + CLASS_NAME + "::loginIdpUserPwd] ResourceNotFoundException");
			throw notFoundEx;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::loginIdpUserPwd] Errore: " + ex.getMessage());
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonbobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				log.fatal("[" + CLASS_NAME + "::loginIdpUserPwd] UnauthorizedException + URISyntaxException", e);
			}
			throw new ServiceException();			
		} 
	}
	
	@Override
	public Response postLoginRequest(String simulatePortale, LoginRequest loginRequest, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			log.debug("[" + CLASS_NAME + "::postLoginRequest] IN loginIdpShibboleth: " + loginRequest);
			log.debug("[" + CLASS_NAME + "::postLoginRequest] IN loginIdpShibboleth: " + simulatePortale);
			log.debug("[" + CLASS_NAME + "::postLoginRequest] IN loginIdpShibboleth: " + httpRequest.getServerName());
			//validaLoginRequest(loginRequest);
			nomePortale = validaStringPortaleName(httpRequest.getServerName(), simulatePortale);
			log.debug("[" + CLASS_NAME + "::postLoginRequest] nomePortale= "  + nomePortale);
			loginRequest.setNomePortale(nomePortale);
			//
			loginResponse = loginService.login(loginRequest, httpHeaders, httpRequest);
			// ObjectMapper mapper = new ObjectMapper();
			// String strJSON = mapper.writeValueAsString(loginResponse);
			// log.debug("*******"  + strJSON);
			// Cookie cookie = new Cookie("loginResponse", strJSON);
			// cookie.setPath("/");
			// HttpRequestUtils httpRequestUtils = new  HttpRequestUtils();
						
			// URI utlToRedirect = new URI("https:////tst-" + httpRequestUtils.getPortalName(httpRequest) + "/moonfobl");
			
			log.debug("*******"  + httpRequest.getServerName());
			log.debug("*******"  + httpRequest.getRemoteHost());
			log.debug("*******"  + httpRequest.getRemotePort());
			log.debug("*******"  + httpRequest.getProtocol());
			log.debug("******* PORTALE"  + nomePortale);
			// httpResponse.addCookie(cookie);
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
			
			//return Response.status(Response.Status.FOUND).location(utlToRedirect).build();
			// return Response.temporaryRedirect(utlToRedirect).build();
			 return Response.ok(loginResponse).build();
		} catch (ResourceNotFoundException notFoundEx) {
			log.error("[" + CLASS_NAME + "::postLoginRequest] ResourceNotFoundException");
			throw notFoundEx;						
		} catch (ForbiddenException forbidenEx) {
			log.error("[" + CLASS_NAME + "::postLoginRequest] ForbiddenException");
			throw forbidenEx;
		} catch (UnauthorizedException unauthorizedEx) {
			log.error("[" + CLASS_NAME + "::postLoginRequest] UnauthorizedException");
			throw unauthorizedEx;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postLoginRequest] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postLoginRequest] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
			try {
				String loginMode = loginRequest.getLogonMode();
				auditService.insertAuditLoginLocal(httpRequest.getRemoteAddr(), loginResponse, nomePortale, loginMode);
			} catch (Exception e) {
				log.error("[" + CLASS_NAME + "::postLoginRequest] Errore servizio Audit", e);
			}
		}
	}
	
	protected String buildCookie(LoginResponse loginResponse) throws Throwable {
		LoginResponse cookie = (LoginResponse)loginResponse.clone();
		// Eliminazione dei dati inutile nel cookie (per non superare la limit di dimenzione)
		cookie.setIdIride(null);
		cookie.setDatiAggiuntivi(null);
		return encodeCookieBase64(cookie);
	}

	private String encodeCookieBase64(LoginResponse loginResponse) throws Throwable {
		String result = encodedBase64(loginResponse);
		if (result.length() > MAX_COOKIE_LENGTH) {
			throw new Throwable("Lunghezza massima cookie superata");
		}
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::encodedBase64] Encode loginResponse=" +  result);
		}	
		return result;
	}
	
	private String encodedBase64(LoginResponse loginResponse) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		String strJSON = mapper.writeValueAsString(loginResponse);
		String result =   Base64.getUrlEncoder().encodeToString(strJSON.getBytes(Charset.forName("UTF-8")));
		log.debug("[" + CLASS_NAME + "::encodedBase64] result = " +  result);
		return result;
	}
		
	private String getQueryString(HttpServletRequest request) {
		if (request.getQueryString() != null && request.getQueryString().length() > 0) {
			log.debug("[" + CLASS_NAME + "::getQueryString] QueryString="+request.getQueryString());
			return "?" + request.getQueryString();			
		} else {
			return "";
		}
	}

}
