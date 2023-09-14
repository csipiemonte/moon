/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Funzione;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alle funzioni
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface FunzioniService {
	List<Funzione> getElencoFunzioni() throws BusinessException;
	Funzione getFunzioneById(Integer idFunzione) throws ItemNotFoundBusinessException, BusinessException;
	Funzione createFunzione(Funzione body) throws BusinessException;
	Funzione updateFunzione(Integer idFunzione, Funzione body) throws BusinessException;
}
