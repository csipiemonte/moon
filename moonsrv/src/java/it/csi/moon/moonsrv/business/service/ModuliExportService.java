/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service;

import it.csi.moon.commons.dto.ModuloExported;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi all'export di moduli
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ModuliExportService {

	public ModuloExported exportModuloByCd(String codiceModulo, String versione) throws ItemNotFoundBusinessException, BusinessException;
	
}
