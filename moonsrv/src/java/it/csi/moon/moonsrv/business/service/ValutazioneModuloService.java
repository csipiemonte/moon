/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.ValutazioneModuloSintesi;
import it.csi.moon.commons.entity.ValutazioneModuloEntity;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Metodi di business relativi alle valutazioni utenti dei moduli
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ValutazioneModuloService {
	void insertValutazioneModulo(ValutazioneModuloEntity entity) throws BusinessException;
	List<ValutazioneModuloSintesi> getValutazioneModuloSintesi(Long idModulo) throws BusinessException;
}
