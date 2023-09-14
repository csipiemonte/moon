/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.csi.moon.commons.dto.BuildInfo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.dto.moonfobl.MessaggioBacheca;
import it.csi.moon.moonfobl.dto.moonfobl.UserChangeRequest;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

public interface BackendService {
	
	public String getMessage();

	//
	public UserInfo getCurrentUser(HttpServletRequest request);
	public UserInfo aggiornaCurrentUser(HttpServletRequest httpRequest, UserChangeRequest userChangeRequest);
	
	//
	public String localLogout(HttpServletRequest request);

	public String getVersion() throws BusinessException;
	public BuildInfo getBuildInfo() throws BusinessException;

	public List<MessaggioBacheca> getElencoMessaggiBacheca(UserInfo user);
	
	public String testWww(String protocollo, String url ) throws BusinessException, IOException;
}
