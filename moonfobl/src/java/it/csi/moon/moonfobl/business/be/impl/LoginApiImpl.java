/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.util.HeadersUtils;
import it.csi.moon.moonfobl.business.be.LoginApi;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.LoginService;
import it.csi.moon.moonfobl.business.service.login.LoginGoogleImpl;
import it.csi.moon.moonfobl.business.service.login.LoginIdpShibbolethImpl;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.dto.moonfobl.LogonMode;
import it.csi.moon.moonfobl.dto.moonfobl.LogonModeEnum;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.LoginBusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.exceptions.service.LoginException;
import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class LoginApiImpl extends MoonBaseApiImpl implements LoginApi {
	
	private static final String CLASS_NAME = "LoginApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	public LoginService loginService;
	@Autowired 
	AuditService auditService;


	@Override
	public Response loginIdpShibboleth(String provider, String simulatePortale,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::loginIdpShibboleth] IN provider: " + provider);
			LOG.debug("[" + CLASS_NAME + "::loginIdpShibboleth] IN simulatePortale: " + simulatePortale);
			LOG.debug("[" + CLASS_NAME + "::loginIdpShibboleth] IN ServerName: " + httpRequest.getServerName());
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setLogonMode(LogonModeEnum.IDP_SHIBBOLETH.name()); // usato sopratutto se su usa lo stratto loginService con la factory
			nomePortale = validaStringPortaleName(httpRequest.getServerName(), simulatePortale);
			LOG.debug("******* PORTALE"  + nomePortale);
			loginRequest.setNomePortale(nomePortale);
			loginRequest.setProvider(provider);
			loginResponse = new LoginIdpShibbolethImpl().login(loginRequest, httpHeaders, httpRequest);
			loginResponse.setCognome(loginResponse.getCognome());
			loginResponse.setNome(loginResponse.getNome());
			loginResponse.setIdIride(loginResponse.getIdIride());

			URI urlToRedirectURI = new URI(httpRequest.getScheme(),httpRequest.getServerName(),"moonfobl");
			LOG.debug("[" + CLASS_NAME + "::loginIdpShibboleth] URL Redirect : " + urlToRedirectURI.toString() ); 						
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl" + getQueryString(httpRequest));		
			LOG.debug("[" + CLASS_NAME + "::loginIdpShibboleth] URL Redirect : " + urlToRedirect.toString() );
			httpResponse.addCookie(buildCookie("loginResponse", pruneAndEncodeLoginResponse(loginResponse)));
			LOG.debug("[" + CLASS_NAME + "::loginIdpShibboleth] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
			if (isWithoutRedirect(httpHeaders)) {
				return Response.ok(loginResponse).build();
			} else {
				return Response.temporaryRedirect(urlToRedirect).build();
			}
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::loginIdpShibboleth] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException unauthorizedEx) {
			LOG.error("[" + CLASS_NAME + "::loginIdpShibboleth] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::loginIdpShibboleth] UnauthorizedException + URISyntaxException", e);
			}
			throw unauthorizedEx;
		} catch (ForbiddenException forbidenEx) {
			LOG.error("[" + CLASS_NAME + "::loginIdpShibboleth] ForbiddenException");
			throw forbidenEx;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::loginIdpShibboleth] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::loginIdpShibboleth] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
			try {
				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::login] Errore servizio Audit", e);
			}
		}
	}
	
	private boolean isWithoutRedirect(HttpHeaders httpHeaders) {
		String noRedirect = HeadersUtils.retrieveHeaderNoRedirect(httpHeaders);
		return devmode || "S".equalsIgnoreCase(noRedirect);
	}

	@Override
	public Response loginIdpGoogle(String provider, String simulatePortale, LoginRequest loginRequest,
	    	 SecurityContext securityContext,  HttpHeaders httpHeaders,  HttpServletRequest httpRequest,  HttpServletResponse httpResponse) {
				
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::loginIdpGoogle] IN simulatePortale: " + simulatePortale);
			LOG.debug("[" + CLASS_NAME + "::loginIdpGoogle] IN ServerName: " + httpRequest.getServerName());
			nomePortale = validaStringPortaleName(httpRequest.getServerName(), simulatePortale);
			LOG.debug("******* PORTALE"  + nomePortale);
			loginRequest.setNomePortale(nomePortale);
			loginRequest.setProvider(provider);
			loginResponse = new LoginGoogleImpl().login(loginRequest, httpHeaders, httpRequest);
			loginResponse.setCognome(loginResponse.getCognome());
			loginResponse.setNome(loginResponse.getNome());
			LOG.debug("*******"  + httpRequest.getServerName());
			LOG.debug("*******"  + httpRequest.getRemoteHost());
			LOG.debug("*******"  + httpRequest.getRemotePort());
			LOG.debug("*******"  + httpRequest.getProtocol());
			LOG.debug("******* PORTALE"  + nomePortale);			
			LOG.debug("[" + CLASS_NAME + "::loginIdpGoogle] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());			
			return Response.ok(loginResponse).build();			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::loginIdpGoogle] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException unauthorizedEx) {
			LOG.error("[" + CLASS_NAME + "::loginIdpGoogle] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https:////" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::loginIdpGoogle] UnauthorizedException + URISyntaxException", e);
			}
			throw unauthorizedEx;
		} catch (ForbiddenException forbidenEx) {
			LOG.error("[" + CLASS_NAME + "::loginIdpGoogle] ForbiddenException");
			throw forbidenEx;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::loginIdpGoogle] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::loginIdpGoogle] Errore generico servizio login", ex);
			throw new ServiceException();
		}
		
	}
		
	private String getQueryString(HttpServletRequest request) {
		if (request.getQueryString() != null && request.getQueryString().length() > 0) {
			LOG.debug("[" + CLASS_NAME + "::getQueryString] QueryString="+request.getQueryString());
			return "?" + request.getQueryString();			
		} else {
			return "";
		}
	}

	@Override
	public Response postLoginRequest(String simulatePortale, LoginRequest loginRequest, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::postLoginRequest] IN loginIdpShibboleth: " + loginRequest);
			LOG.debug("[" + CLASS_NAME + "::postLoginRequest] IN loginIdpShibboleth: " + simulatePortale);
			LOG.debug("[" + CLASS_NAME + "::postLoginRequest] IN loginIdpShibboleth: " + httpRequest.getServerName());
			nomePortale = validaStringPortaleName(httpRequest.getServerName(), simulatePortale);
			//
		    loginRequest.setNomePortale(nomePortale);
			loginResponse = loginService.login(loginRequest, httpHeaders, httpRequest);
			// ObjectMapper mapper = new ObjectMapper();
			// String strJSON = mapper.writeValueAsString(loginResponse);
			// LOG.debug("*******"  + strJSON);
			// Cookie cookie = new Cookie("loginResponse", strJSON);
			// cookie.setPath("/");
//			cookie.setHttpOnly(true); // Compliant: this sensitive cookie is protected against theft (HttpOnly=true)
			// HttpRequestUtils httpRequestUtils = new  HttpRequestUtils();
						
			// URI utlToRedirect = new URI("https:////tst-" + httpRequestUtils.getPortalName(httpRequest) + "/moonfobl");
			
			LOG.debug("*******"  + httpRequest.getServerName());
			LOG.debug("*******"  + httpRequest.getRemoteHost());
			LOG.debug("*******"  + httpRequest.getRemotePort());
			LOG.debug("*******"  + httpRequest.getProtocol());
			LOG.debug("******* PORTALE"  + nomePortale);
			// httpResponse.addCookie(cookie);
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
			
			//return Response.status(Response.Status.FOUND).location(utlToRedirect).build();
			// return Response.temporaryRedirect(utlToRedirect).build();
			return Response.ok(loginResponse).build();
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::postLoginRequest] ResourceNotFoundException");
			throw notFoundEx;						
		} catch (ForbiddenException forbidenEx) {
			LOG.error("[" + CLASS_NAME + "::postLoginRequest] ForbiddenException");
			throw forbidenEx;
		} catch (UnauthorizedException unauthorizedEx) {
			LOG.error("[" + CLASS_NAME + "::postLoginRequest] UnauthorizedException");
			throw unauthorizedEx;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::postLoginRequest] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::postLoginRequest] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
			try {
				String loginMode = loginRequest.getLogonMode();
				auditService.insertAuditLoginLocal(httpRequest.getRemoteAddr(), loginResponse, nomePortale, loginMode);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::postLoginRequest] Errore servizio Audit", e);
			}
		}
	}

/*
 * Ritorna cookie per gestire autenticazione differente
 * 	
 */
	
	@Override
	public Response loginIdpUserPwd(String simulatePortale, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::loginIdpUserPwd] IN simulatePortale: " + simulatePortale);
			LOG.debug("[" + CLASS_NAME + "::loginIdpUserPwd] IN ServerName: " + httpRequest.getServerName());
			URI urlToRedirectURI = new URI(httpRequest.getScheme(),httpRequest.getServerName(),"moonfobl");
			LOG.debug("[" + CLASS_NAME + "::loginIdpUserPwd] URL Redirect : " + urlToRedirectURI.toString() ); 						
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl" + getQueryString(httpRequest));		
			LOG.debug("[" + CLASS_NAME + "::loginIdpUserPwd] URL Redirect : " + urlToRedirect.toString() );
			httpResponse.addCookie(buildCookie("auth-mode", "user-pwd"));
			if (isWithoutRedirect(httpHeaders)) {
				return Response.ok().build();
			} else {
				return Response.temporaryRedirect(urlToRedirect).build();
			}
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::loginIdpUserPwd] ResourceNotFoundException");
			throw notFoundEx;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::loginIdpUserPwd] Errore: " + ex.getMessage());
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::loginIdpUserPwd] UnauthorizedException + URISyntaxException", e);
				throw new ServiceException();
			}			
		} 
	}

	@Override
	public Response loginIdpMode(String loginMode, String simulatePortale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
	
		Cookie cookie=null;
		try {
			LOG.debug("[" + CLASS_NAME + "::loginIdpMode] IN simulatePortale: " + simulatePortale);
			LOG.debug("[" + CLASS_NAME + "::loginIdpMode] IN ServerName: " + httpRequest.getServerName());
			validaLoginMode(loginMode, cookie);
			URI urlToRedirectURI = new URI(httpRequest.getScheme(),httpRequest.getServerName(),"moonfobl");
			LOG.debug("[" + CLASS_NAME + "::loginIdpMode] URL Redirect : " + urlToRedirectURI.toString() );
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl" + getQueryString(httpRequest) );
			LOG.debug("[" + CLASS_NAME + "::loginIdpMode] URL Redirect : " + urlToRedirect.toString() );
			httpResponse.addCookie(buildCookie("auth-mode", loginMode));
			if (isWithoutRedirect(httpHeaders)) {
				return Response.ok().build();
			} else {
				return Response.temporaryRedirect(urlToRedirect).build();
			}
		} catch (ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::loginIdpMode] ItemNotFoundBusinessException loginMode=" + loginMode, notFoundEx);
			throw notFoundEx;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::loginIdpMode] Errore: " + ex.getMessage());
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::loginIdpMode] UnauthorizedException + URISyntaxException", e);
			}
			throw new ServiceException();			
		} 
		
		
	
	}

	protected void validaLoginMode(String loginMode, Cookie cookie) throws ItemNotFoundBusinessException {
		switch (loginMode) {
			case "google":
			case "user-pwd":
				break;
			default:
				throw new ItemNotFoundBusinessException("Unexpected loginMode");
		}
	}

	/**
	 * Prima chiamata per Accesso Diretto alla compilazione di un singolo modulo (LIKE Modulistica)
	 */
	@Override
	public Response getDirectLogonMode(String codiceModulo, String codiceEnte, String simulatePortale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getDirectLogonMode] IN codiceModulo: "+codiceModulo);
			LOG.debug("[" + CLASS_NAME + "::getDirectLogonMode] IN codiceEnte: "+codiceEnte);
			LOG.debug("[" + CLASS_NAME + "::getDirectLogonMode] IN simulatePortale: "+simulatePortale);
			LOG.debug("[" + CLASS_NAME + "::getDirectLogonMode] IN nomePortale: "+httpRequest.getServerName());
			codiceModulo = validaStringCodeRequired(codiceModulo);
			String nomePortale = validaStringPortaleName(httpRequest.getServerName(), simulatePortale);
			LOG.debug("[" + CLASS_NAME + "::getDirectLogonMode] nomePortale: " + nomePortale);
			
			LogonMode logonMode = loginService.getDirectLogonMode(codiceModulo, codiceEnte, nomePortale);
			
			URI urlToRedirectURI = new URI(httpRequest.getScheme(),httpRequest.getServerName(),"moonfobl");
			LOG.debug("[" + CLASS_NAME + "::getDirectLogonMode] URL Redirect : " + urlToRedirectURI.toString() );
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl" + getQueryString(httpRequest) );
			LOG.debug("[" + CLASS_NAME + "::getDirectLogonMode] URL Redirect : " + urlToRedirect.toString() );
			httpResponse.addCookie(buildCookie("direct-auth-mode", logonMode.getLogonMode()));
			if (isWithoutRedirect(httpHeaders)) {
				return Response.ok().build();
			} else {
				return Response.temporaryRedirect(urlToRedirect).build();
			}			
//			return Response.ok(logonMode).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getDirectLogonMode] Modulo non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (LoginBusinessException lbe) {
			LOG.error("[" + CLASS_NAME + "::getDirectLogonMode] Errore specifica login");
			throw new LoginException(lbe.getMessage(), lbe.getCode());
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::getDirectLogonMode] Errore servizio login",e);
			throw new ServiceException("Errore servizio login");
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getDirectLogonMode] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getDirectLogonMode] Errore generico servizio login",ex);
			throw new ServiceException("Errore generico servizio login");
		}
	}

}
