/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.UserInfo;

public interface AuditService {

	public void insertAuditLogin(String ipAdress, UserInfo userInfo);
	public void insertAuditLogout(String ipAdress, UserInfo userInfo, String result);
	public void insertAuditANPRGetFamigliaByCF(IstanzaInitParams initParams, String keyOperazione);
	public void insertAuditANPRGetFamigliaByCF(String utente, String ipAdress, String keyOperazione);
	
	public String retrieveUser(UserInfo userInfo);
	public String retrieveUser(IstanzaInitParams initParams);
	
	public void insertAuditAPICambioStato(String ipAdress, String user, String params);
	public void insertAuditAPINotifica(String ipAdress, String user, String params);	
	public void insertAuditAPIIstanze(String ipAdress, String user, String params);
	public void insertAuditAPIIstanzePaginate(String ipAdress, String user, String params);
	public void insertAuditAPIDettaglioIstanza(String ipAdress, String user, String params);
	public void insertAuditAPIDettaglioIstanzaPdf(String ipAdress, String user, String params);
	public void insertAuditAPIReport(String ipAdress, String user, String params);
	public void insertAuditAPICercaAllegato(String ipAdress, String user, String params);
	public void insertAuditAPIModuloVersione(String ipAdress, String user, String params);
		
}
