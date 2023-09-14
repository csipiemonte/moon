/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.initializer;

import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.moonsrv.business.service.dto.IstanzaInitCompletedParams;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Interface DatiIstanzaInitializer
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface DatiIstanzaInitializer {

	public String getDatiIstanza(IstanzaInitCompletedParams completedParams, ModuloVersionatoEntity modulo) throws BusinessException;
	
}
