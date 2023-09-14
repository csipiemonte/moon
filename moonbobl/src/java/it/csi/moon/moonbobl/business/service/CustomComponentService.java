/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.CustomComponentEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

/**
 * @author Danilo
 * servizio di recupero custom component form.io
 */
public interface CustomComponentService {		
	public CustomComponentEntity findById(String idComponent) throws BusinessException;
	public List<CustomComponentEntity> getCustomComponents() throws BusinessException;

}