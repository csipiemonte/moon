/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import it.csi.moon.commons.entity.ValutazioneModuloEntity;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

/**
 * Metodi di business relativi alle valutazioni utenti dei moduli
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ValutazioneModuloService {
	void insertValutazioneModulo(ValutazioneModuloEntity entity) throws BusinessException;
}
