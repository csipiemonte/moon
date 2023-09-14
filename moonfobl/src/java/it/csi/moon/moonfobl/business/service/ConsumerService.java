/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service;

import java.util.List;

import it.csi.moon.moonfobl.exceptions.business.BusinessException;


/**
 * @author Danilo
 * Metodi di business relativi al Consumer
 */

public interface ConsumerService {
	
	public <P> List<P> getParameters(String consumer, String codiceEnte) throws BusinessException;	
	
	public <P> P getParameter(String consumer, String codiceEnte, String type,String id) throws BusinessException;	
	
}

