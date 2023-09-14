/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import javax.servlet.http.HttpServletRequest;

import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaInitParams;
import it.csi.moon.moonbobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;

public interface AuditService {

	public void insertAuditLogin(String ipAdress, UserInfo userInfo);
	public void insertAuditLogout(String ipAdress, UserInfo userInfo, String result);
	public void insertAuditANPRGetFamigliaByCF(IstanzaInitParams initParams, String keyOperazione);
	
	public String retrieveUser(UserInfo userInfo);
	public String retrieveUser(IstanzaInitParams initParams);
	
	// audit metodi bo
	public void insertSearchIstanze(HttpServletRequest req, String keyOperation);
	public void getPdf(String ipAddress,UserInfo userInfo,Long idIstanza);
	public void getIstanza(String ipAddress,UserInfo userInfo,Long idIstanza, String nomePortale);
	public void saveIstanza(String ipAddress,UserInfo userInfo,Long idIstanza);
	// metodo generico
	public void traceOperazione(AuditEntity auditEntity);
	
	public void insertAuditLoginIdpShibboleth(String ipAdress, LoginResponse loginResponse, String portalName, String provider);
	public void insertAuditLoginLocal(String ipAdress, LoginResponse loginResponse, String portalName, String loginMode);
}
