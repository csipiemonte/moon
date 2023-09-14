/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.Map;

import javax.management.AttributeList;

import it.csi.moon.commons.dto.BuildInfo;
import it.csi.moon.commons.dto.SysInfo;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Metodi di business generico
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface BackendService {

	public String getMessage();

	public String getVersion() throws BusinessException;
	public BuildInfo getBuildInfo() throws BusinessException;
	public AttributeList getAttributeList() throws BusinessException;
	public SysInfo getSysInfo(String fields) throws BusinessException;
	public Map<String,String> getProp() throws BusinessException;
	public Map<String,String> getEnv() throws BusinessException;
	
}
