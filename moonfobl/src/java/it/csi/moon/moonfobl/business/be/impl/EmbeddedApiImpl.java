/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.util.JsonHelper;
import it.csi.moon.moonfobl.business.be.EmbeddedApi;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.EmbeddedService;
import it.csi.moon.moonfobl.business.service.helper.JwtIdentitaUtil;
import it.csi.moon.moonfobl.dto.moonfobl.EmbeddedNavigator;
import it.csi.moon.moonfobl.dto.moonfobl.EmbeddedOptions;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.EnvProperties;
import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;
import it.csi.moon.moonfobl.util.decodifica.DecodificaEmbeddedService;

@Component
public class EmbeddedApiImpl extends MoonBaseApiImpl implements EmbeddedApi {

	private static final String CLASS_NAME = "EmbeddedApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	ObjectMapper objectMapper = new ObjectMapper();
	
	private static final String LOCAL_CONTEXT = ":17000/moonfowcl/local/embedded";

	@Autowired
	public EmbeddedService embeddedService;
	@Autowired
	AuditService auditService;
    @Autowired
    JwtIdentitaUtil jwtTokenUtil;
	
	
	
	@Override
	public Response getViewIstanzaNav(String codiceIstanzaPP, String moonId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getViewIstanzaNav] IN codiceIstanzaPP: " + codiceIstanzaPP);
			String codiceIstanza = validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);	
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
			
			LOG.debug("[" + CLASS_NAME + "::getViewIstanzaNav] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());				
			
			EmbeddedNavigator embeddedNav = embeddedService.gotoViewIstanza(user, codiceIstanza);
			embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);				
			
			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();		
						
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanzaNav] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanzaNav] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getViewIstanza] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanzaNav] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanzaNav] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanzaNav] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanzaNav] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {

		}
	}

	@Override
	public Response getViewIstanza(String codiceIstanzaPP, String moonId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getViewIstanza] IN codiceIstanzaPP: " + codiceIstanzaPP);
			String codiceIstanza = validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
			EmbeddedNavigator embeddedNav = embeddedService.gotoViewIstanza(user, codiceIstanza);
			loginResponse.setEmbeddedNavigator(embeddedNav);
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl" + getQueryString(httpRequest));
			httpResponse.addCookie(buildCookie("loginResponse", pruneAndEncodeLoginResponse(loginResponse)));
			LOG.debug("[" + CLASS_NAME + "::getViewIstanza] URL to redirect obtained from request: " + urlToRedirect);
			LOG.debug("[" + CLASS_NAME + "::getViewIstanza] URL to redirect obtained from token : " + getEndPointUrl(loginResponse.getIdMoonToken()));
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
//			if (devmode) {
//				return Response.ok(loginResponse).build();
//			} else
//			return Response.temporaryRedirect(urlToRedirect).build();			
			
			return Response.ok(loginResponse).build();
						
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getViewIstanza] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//TODO

//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}


	private String getQueryString(HttpServletRequest request) {
		if (request.getQueryString() != null && request.getQueryString().length() > 0) {
			LOG.debug("[" + CLASS_NAME + "::getQueryString] QueryString=" + request.getQueryString());
			return "?" + request.getQueryString();
		} else {
			return "";
		}
	}

	@Override
	public Response getEditIstanza(String codiceIstanzaPP, String moonId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getEditIstanza] IN codiceIstanzaPP: " + codiceIstanzaPP);
			String codiceIstanza = validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
			EmbeddedNavigator embeddedNav = embeddedService.gotoEditIstanza(user, codiceIstanza);
			loginResponse.setEmbeddedNavigator(embeddedNav);
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
//			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, moonId);
//			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_EMBEDDED_PARAMS, objectMapper.writeValueAsString(embeddedNav));
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl" + getQueryString(httpRequest));
			httpResponse.addCookie(buildCookie("loginResponse", pruneAndEncodeLoginResponse(loginResponse)));
			LOG.debug("[" + CLASS_NAME + "::getEditIstanza] URL Redirect : " + urlToRedirect.toString());
			LOG.debug("[" + CLASS_NAME + "::getEditIstanza] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
			
//			if (devmode) {
//				return Response.ok(loginResponse).build();
//			} else
//				return Response.temporaryRedirect(urlToRedirect).build();
			return Response.ok(loginResponse).build();
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getEditIstanza] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}
	
	@Override
	public Response getEditIstanzaNav(String codiceIstanzaPP, String moonId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getEditIstanzaNav] IN codiceIstanzaPP: " + codiceIstanzaPP);
			String codiceIstanza = validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
			LOG.debug("[" + CLASS_NAME + "::getEditIstanzaNav] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());				
			
			EmbeddedNavigator embeddedNav = embeddedService.gotoEditIstanza(user, codiceIstanza);
			embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);				

			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();
			
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanzaNav] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanzaNav] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getEditIstanzaNav] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanzaNav] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanzaNav] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanzaNav] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanzaNav] Errore generico servizio login", ex);
			throw new ServiceException();
		} 
	}	

	@Override
//	public Response getNewIstanza(String codiceModuloQP, String versioneModuloPP, 
	public Response getNewIstanza(String codiceModuloPP, String moonId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		// TODO Auto-generated method stub
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getNewIstanza] IN codiceModuloPP: " + codiceModuloPP);

			String codiceModulo = validaStringCodeRequired(codiceModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
			EmbeddedNavigator embeddedNav = embeddedService.gotoNewIstanza(user, codiceModulo);
			loginResponse.setEmbeddedNavigator(embeddedNav);
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
//			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, moonId);
//			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_EMBEDDED_PARAMS, objectMapper.writeValueAsString(embeddedNav));
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl" + getQueryString(httpRequest));
			httpResponse.addCookie(buildCookie("loginResponse", pruneAndEncodeLoginResponse(loginResponse)));
			LOG.debug("[" + CLASS_NAME + "::getNewIstanza] URL Redirect : " + urlToRedirect.toString());
			LOG.debug("[" + CLASS_NAME + "::getNewIstanza] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
//			if (devmode) {
//				return Response.ok(loginResponse).build();
//			} else
//				return Response.temporaryRedirect(urlToRedirect).build();
			
			return Response.ok(loginResponse).build();
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getNewIstanza] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}
	
	@Override
	public Response getNewIstanzaNav(String codiceModuloPP, String moonId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		// TODO Auto-generated method stub
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getNewIstanzaNav] IN codiceModuloPP: " + codiceModuloPP);

			String codiceModulo = validaStringCodeRequired(codiceModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
			LOG.debug("[" + CLASS_NAME + "::getNewIstanzaNav] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());				
			
			EmbeddedNavigator embeddedNav = embeddedService.gotoNewIstanza(user, codiceModulo);
			embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);				
			
			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanzaNav] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanzaNav] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getNewIstanzaNav] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanzaNav] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanzaNav] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanzaNav] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanzaNav] Errore generico servizio login", ex);
			throw new ServiceException();
		} 
	}

	@Override
	public Response getIstanza(String codiceIstanzaPP, String moonId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanza] IN codiceIstanzaPP: " + codiceIstanzaPP);
			String codiceIstanza = validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
			EmbeddedNavigator embeddedNav = embeddedService.gotoIstanza(user, codiceIstanza);
			loginResponse.setEmbeddedNavigator(embeddedNav);
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl" + getQueryString(httpRequest));
			httpResponse.addCookie(buildCookie("loginResponse", pruneAndEncodeLoginResponse(loginResponse)));
			LOG.debug("[" + CLASS_NAME + "::getIstanza] URL Redirect : " + urlToRedirect.toString());
			LOG.debug("[" + CLASS_NAME + "::getIstanza] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
//			if (devmode) {
//				return Response.ok(loginResponse).build();
//			} else
//				return Response.temporaryRedirect(urlToRedirect).build();
			
			return Response.ok(loginResponse).build();
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getIstanza] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}
	
	
	@Override
	public Response getIstanzaNav(String codiceIstanzaPP, String moonId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzaNav] IN codiceIstanzaPP: " + codiceIstanzaPP);
			String codiceIstanza = validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
			LOG.debug("[" + CLASS_NAME + "::getIstanzaNav] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());				
			
			EmbeddedNavigator embeddedNav = embeddedService.gotoIstanza(user, codiceIstanza);
			embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);				
			
			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();

		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaNav] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaNav] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getIstanzaNav] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaNav] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaNav] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaNav] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzaNav] Errore generico servizio login", ex);
			throw new ServiceException();
		}
	}

	@Override
	public Response getIstanze(String moonId, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanze] ");

			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
			EmbeddedNavigator embeddedNav = new EmbeddedNavigator(DecodificaEmbeddedService.ISTANZE);
			loginResponse.setEmbeddedNavigator(embeddedNav);
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
			URI urlToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl" + getQueryString(httpRequest));
			httpResponse.addCookie(buildCookie("loginResponse", pruneAndEncodeLoginResponse(loginResponse)));
			LOG.debug("[" + CLASS_NAME + "::getIstanza] URL Redirect : " + urlToRedirect.toString());
			LOG.debug("[" + CLASS_NAME + "::getIstanza] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
//			if (devmode) {
//				return Response.ok(loginResponse).build();
//			} else
//				return Response.temporaryRedirect(urlToRedirect).build();
			
			return Response.ok(loginResponse).build();
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getIstanze] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}
	
	
	@Override
	public Response getIstanzeNav(String moonId, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanzeNav] ");

			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
			LOG.debug("[" + CLASS_NAME + "::getIstanzeNav] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());				
			
			EmbeddedNavigator embeddedNav = new EmbeddedNavigator(DecodificaEmbeddedService.ISTANZE);
			embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);				
			
			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();

		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeNav] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeNav] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getIstanzeNav] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeNav] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeNav] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeNav] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanzeNav] Errore generico servizio login", ex);
			throw new ServiceException();
		} 
	}

	/***********************************************
	 * POST
	 ***********************************************/

	@Override
	public Response postViewIstanza(String codiceIstanzaPP, EmbeddedOptions options, String moonId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {

			LOG.debug("[" + CLASS_NAME + "::postViewIstanza] IN codiceIstanzaPP: " + codiceIstanzaPP);
			String codiceIstanza = validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);	
			loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
			
			LOG.debug("[" + CLASS_NAME + "::getViewIstanzaNav] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());	
			
			
			EmbeddedNavigator embeddedNav = embeddedService.gotoViewIstanza(user, codiceIstanza);
			embeddedNav.setOptions(options);
			embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);	
			
			LOG.debug("inet servername : "+InetAddress.getLocalHost().getHostName());
			LOG.debug("request servername : "+httpRequest.getServerName());
//			LOG.debug("request servername hostname : "+Runtime.getRuntime().exec("hostname")); // commented for sonar or add path of command
			
			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();
			
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getViewIstanza] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//TODO

//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}

	@Override
	public Response postEditIstanza(String codiceIstanzaPP, EmbeddedOptions options, String moonId, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
	     	LOG.debug("[" + CLASS_NAME + "::getEditIstanza] IN codiceIstanzaPP: " + codiceIstanzaPP);
			String codiceIstanza = validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
            loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
            
            LOG.debug("[" + CLASS_NAME + "::getEditIstanza] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
			
			EmbeddedNavigator embeddedNav = embeddedService.gotoEditIstanza(user, codiceIstanza);
	        embeddedNav.setOptions(options);
			embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);	
				
			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();	
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getEditIstanza] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getEditIstanza] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}

	@Override
//	public Response getNewIstanza(String codiceModuloQP, String versioneModuloPP, 
	public Response postNewIstanza(String codiceModuloPP, EmbeddedOptions options, String moonId, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		// TODO Auto-generated method stub
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getNewIstanza] IN codiceModuloPP: " + codiceModuloPP);

			String codiceModulo = validaStringCodeRequired(codiceModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
            loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
            
            LOG.debug("[" + CLASS_NAME + "::getNewIstanza] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
						          
			EmbeddedNavigator embeddedNav = embeddedService.gotoNewIstanza(user, codiceModulo);
			embeddedNav.setOptions(options);
			loginResponse.setEmbeddedNavigator(embeddedNav);
		    embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);	
				
			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();         
		        
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getNewIstanza] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getNewIstanza] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}

	@Override
	public Response postIstanza(String codiceIstanzaPP,  EmbeddedOptions options, String moonId,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanza] IN codiceIstanzaPP: " + codiceIstanzaPP);
			String codiceIstanza = validaStringCodeRequired(codiceIstanzaPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
            loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
            
            LOG.debug("[" + CLASS_NAME + "::getIstanza] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
			
			EmbeddedNavigator embeddedNav = embeddedService.gotoIstanza(user, codiceIstanza);
					
			embeddedNav.setOptions(options);
			loginResponse.setEmbeddedNavigator(embeddedNav);
		    embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);	
				
			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();   
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getIstanza] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}

	@Override
	public Response postIstanze(EmbeddedOptions options, String moonId, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ResourceNotFoundException, UnprocessableEntityException, ServiceException {
		LoginResponse loginResponse = null;
		String nomePortale = null;
		try {
			LOG.debug("[" + CLASS_NAME + "::getIstanze] ");

			UserInfo user = retrieveUserInfo(httpRequest);
			loginResponse = new LoginResponse(user);
            loginResponse.setIdMoonToken(new HttpRequestUtils().getMoonToken(httpRequest, devmode));
            
            LOG.debug("[" + CLASS_NAME + "::getIstanze] id moonToken: " + loginResponse.getIdMoonToken());
			httpResponse.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, loginResponse.getIdMoonToken());
		       				
			EmbeddedNavigator embeddedNav = new EmbeddedNavigator(DecodificaEmbeddedService.ISTANZE);
			
			embeddedNav.setOptions(options);
			loginResponse.setEmbeddedNavigator(embeddedNav);
		    embeddedNav.setToken(loginResponse.getIdMoonToken());
			
			String embeddeString = JsonHelper.getJsonFromObject(embeddedNav);	
				
			return Response.ok(getEndPointUrl(loginResponse.getIdMoonToken()) + "?embedded="+embeddeString).build();     
			
		} catch (ResourceNotFoundException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] ResourceNotFoundException");
			throw notFoundEx;
		} catch (UnauthorizedException ue) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] UnauthorizedException");
			URI urlErrToRedirect;
			try {
				urlErrToRedirect = new URI("https://" + httpRequest.getServerName() + "/moonfobl/unauthorized.html");
				return Response.temporaryRedirect(urlErrToRedirect).build();
			} catch (URISyntaxException e) {
				LOG.fatal("[" + CLASS_NAME + "::getIstanze] UnauthorizedException + URISyntaxException", e);
			}
			throw ue;
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] UnprocessableEntityException");
			throw uee;
		} catch (ForbiddenException fe) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] ForbiddenException");
			throw fe;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio login", ex);
			throw new ServiceException();
		} finally {
//			try {
//				String shibProvider = new HttpRequestUtils().getShibIdentProvider(httpRequest, devmode);
//				auditService.insertAuditLoginIdpShibboleth(httpRequest.getRemoteAddr(), loginResponse, nomePortale, shibProvider);
//			} catch (Exception e) {
//				LOG.error("[" + CLASS_NAME + "::getViewIstanza] Errore servizio Audit", e);
//			}
		}
	}
	
	private String getNomePortaleFromToken(String token) {
		String nomePortale = jwtTokenUtil.getPortaleNameFromToken(token);
		if (EnvProperties.readFromFile(EnvProperties.TARGET_LINE).startsWith("tst-")) {
			nomePortale = "tst-" + nomePortale;
		}
		if (EnvProperties.readFromFile(EnvProperties.TARGET_LINE).startsWith("ts-")) {
			nomePortale = "ts-" + nomePortale;
		}
		return nomePortale;
	}
	
	private String getEndPointUrl(String token) {		
		if (devmode) {
			return "http://localhost" + LOCAL_CONTEXT;
		} else {
			return "https://" + getNomePortaleFromToken(token) + "/moonfobl";
		}	          	      
	}

}
