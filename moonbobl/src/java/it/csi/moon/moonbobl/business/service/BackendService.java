/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.csi.moon.moonbobl.dto.moonfobl.BuildInfo;
import it.csi.moon.moonbobl.dto.moonfobl.EnteAreeRuoli;
import it.csi.moon.moonbobl.dto.moonfobl.UserChangeRequest;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

public interface BackendService {

	public String getMessage();

	public it.csi.moon.moonbobl.dto.moonfobl.UserInfo getCurrentUser(HttpServletRequest req);

	public String localLogout(HttpServletRequest request);
	
	public List<EnteAreeRuoli> getEntiAreeRuoliAttivi(Long idUtente, String portalName) throws BusinessException;
	public List<EnteAreeRuoli> getEntiAreeRuoliAttivi(String identificativoUtente, Long idEnte) throws BusinessException;

	public String getVersion() throws BusinessException;
	public BuildInfo getBuildInfo() throws BusinessException;
	
	public UserInfo aggiornaCurrentUser(HttpServletRequest request, UserChangeRequest userChangeRequest);

	
	
}
