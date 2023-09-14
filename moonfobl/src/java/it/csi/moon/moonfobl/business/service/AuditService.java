/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;

public interface AuditService {

//	public void insertAuditLogin(String ipAdress, UserInfo userInfo);
//	public void insertAuditLogin(String ipAdress, LoginResponse loginResponse, String portalName);
	public void insertAuditLoginIdpShibboleth(String ipAdress, LoginResponse loginResponse, String portalName, String provider);
	public void insertAuditLogout(String ipAdress, UserInfo userInfo/*, String result*/);
	public void insertAuditANPRGetFamigliaByCF(IstanzaInitParams initParams, String keyOperazione);
	
	public String retrieveUser(UserInfo userInfo);
	public String retrieveUser(IstanzaInitParams initParams);
	
	public void insertSearchIstanze(String ipAddress, UserInfo userInfo, String keyOperation);
	public void getPdf(String ipAddress, UserInfo userInfo, Long idIstanza);
	public void getNotifica(String ipAddress, UserInfo userInfo, Long idIstanza);
	public void getIstanza(String ipAddress, UserInfo userInfo, Long idIstanza);
	public void getIstanza(String ipAddress, UserInfo userInfo, String codiceIstanza);
	public void saveIstanza(String ipAddress, UserInfo userInfo, Long idIstanza);
	public void deleteIstanza(String ipAddress, UserInfo userInfo, Long idIstanza);
	public void getDocumentoNotifica(String ipAddress, UserInfo userInfo, Long idIstanza);
	public void getDocumentoByFormioNameFile(String ipAddress, UserInfo userInfo, String formioNameFile);
	public void getDocumentoByIdFile(String ipAddress, UserInfo userInfo, Long idFile);
	public void duplicaIstanza(String ipAddress, UserInfo userInfo, Long idIstanza);
	
	public void insertAuditLoginLocal(String ipAdress, LoginResponse loginResponse, String portalName, String loginMode);
}
