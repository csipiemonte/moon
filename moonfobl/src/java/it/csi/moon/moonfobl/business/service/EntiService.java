/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.entity.EntiFilter;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi degli enti
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface EntiService {
	List<Ente> getElencoEnti(EntiFilter filter) throws BusinessException;
	Ente getEnteById(Long idEnte) throws ItemNotFoundBusinessException, BusinessException;
	Ente getEnteByCodice(String codiceEnte) throws ItemNotFoundBusinessException, BusinessException;
	Ente createEnte(Ente ente) throws BusinessException;
	Ente updateEnte(Long idRuolo, Ente ente) throws BusinessException;
}
