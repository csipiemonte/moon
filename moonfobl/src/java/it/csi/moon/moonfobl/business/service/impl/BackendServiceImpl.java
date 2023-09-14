/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.BuildInfo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.mapper.EnteMapper;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.BackendService;
import it.csi.moon.moonfobl.business.service.helper.JwtIdentitaUtil;
import it.csi.moon.moonfobl.business.service.impl.dao.DummyDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.LogoutDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.PortaleDAO;
import it.csi.moon.moonfobl.dto.moonfobl.MessaggioBacheca;
import it.csi.moon.moonfobl.dto.moonfobl.UserChangeRequest;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.EnvProperties;
import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
@Qualifier("provaprovaprova")
public class BackendServiceImpl  implements BackendService{
	
	private static final String CLASS_NAME = "BackendServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	DummyDAO dummyDAO;
	@Autowired
	EnteDAO enteDAO;
	@Autowired
	PortaleDAO portaleDAO;
	@Autowired
	LogoutDAO logoutDAO;
	@Autowired
	AuditService auditService;
    @Autowired
    JwtIdentitaUtil jwtTokenUtil;
	
	public String getMessage(){
		return dummyDAO.pingDB();
	}

	//
	@Override
	public UserInfo getCurrentUser(HttpServletRequest httpRequest) {
		try {
			if (LOG.isDebugEnabled() ) {
				LOG.debug("[" + CLASS_NAME + "::getCurrentUser] \nhttpRequest=" + httpRequest);
			}
			HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
			String marker = httpRequestUtils.getMoonToken(httpRequest);
			UserInfo userInfo = jwtTokenUtil.retreiveUserInfoFromToken(marker);
			if (LOG.isDebugEnabled() ) {
				LOG.debug("[" + CLASS_NAME + "::getCurrentUser] \nuserInfo=" + userInfo);
			}
			return userInfo;
		} catch (Exception e) {
			throw new BusinessException();
		}
	}

	@Override
	public UserInfo aggiornaCurrentUser(HttpServletRequest httpRequest, UserChangeRequest userChangeRequest) {
		if (LOG.isDebugEnabled() ) {
			LOG.debug("[" + CLASS_NAME + "::aggiornaCurrentUser] \nhttpRequest=" + httpRequest + "\nuserChangeRequest=" + userChangeRequest);
		}

		try {
			HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
			String oldIdMoonToken = httpRequestUtils.getMoonToken(httpRequest); //, devmode);
			UserInfo userInfo = jwtTokenUtil.retreiveUserInfoFromToken(oldIdMoonToken);
			// Codice simile a quello presente alla login in IrideIdAdapterFilter
			EnteEntity enteE = enteDAO.findById(userChangeRequest.getEnte().getIdEnte());
			userInfo.setEnte(EnteMapper.buildFromEntity(enteE));
			String newIdMoonToken = jwtTokenUtil.writeIdEnte(oldIdMoonToken,userChangeRequest.getEnte().getIdEnte());
			userInfo.setIdMoonToken(newIdMoonToken);
			if (LOG.isDebugEnabled() ) {
				LOG.debug("[" + CLASS_NAME + "::aggiornaCurrentUser] \nresult=" + userInfo);
			}
			return userInfo;
		} catch (Exception e) {
			throw new BusinessException();
		}
	}
	
	
	//
	@Override
	public String localLogout(HttpServletRequest httpRequest) {
		UserInfo user = getCurrentUser(httpRequest);
		HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
		String portalName = httpRequestUtils.getPortalName(httpRequest);
		//
		// Ricerca della response : URL di logout del portale
//		String result = EnvProperties.readFromFile(EnvProperties.LOGOUT_URL_PREFIX+portalName);

		try {
			auditService.insertAuditLogout(httpRequest.getRemoteAddr(), user);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::localLogout] Errore servizio Audit ",e );
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::localLogout] logoutURL for " + portalName);
		}
		
		// TEST via DB
//		Integer livAuth = 0; // Default 0
//		Long idPortale = portaleDAO.findByNome(portalName).getIdPortale();
//		LogoutEntity logoutUrl = logoutDAO.findByPortaleLivAuth(idPortale, livAuth);
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("[" + CLASS_NAME + "::localLogout] TEST logoutURL for " + logoutUrl);
//		}
		
		return "OK";
	}


	@Override
	public String getVersion() throws BusinessException {
		return EnvProperties.readFromFile(EnvProperties.VERSION);
	}
	
	@Override
	public BuildInfo getBuildInfo() throws BusinessException {
		return new BuildInfo(EnvProperties.readFromFile(EnvProperties.VERSION),
				EnvProperties.readFromFile(EnvProperties.BUILD_TIME),
				EnvProperties.readFromFile(EnvProperties.TARGET_LINE));
	}

	@Override
	public List<MessaggioBacheca> getElencoMessaggiBacheca(UserInfo user) {
		List<MessaggioBacheca> messaggiBacheca = new ArrayList<>();
		messaggiBacheca.add(new MessaggioBacheca("INFO","Sono presenti istanze che richiedono integrazione"));
		return messaggiBacheca;
	}
	
	@Override
	public String testWww(String protocollo, String url ) throws BusinessException, IOException {
		
		HttpURLConnection con= null;
		try {
			URL myurl = new URL(protocollo+"://"+url);
			con = (HttpURLConnection) myurl.openConnection();
	        con.setRequestMethod("GET");
	        con.getInputStream().readAllBytes();
			
			return con.getResponseMessage();
			
		}  catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::testWww] " + e.toString(), e);
			throw new BusinessException();
		}  finally {
	     	if (con != null) {
	     		con.disconnect();
	     	}
		}		
	}
	
}
