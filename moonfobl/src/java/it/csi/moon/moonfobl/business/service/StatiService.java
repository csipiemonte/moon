/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Stato;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

/**
 * @author franc
 * Metodi di business relativi ai moduli
 */
public interface StatiService {
	public List<Stato> getElencoStati(ModuliFilter filter) throws BusinessException;	
}
