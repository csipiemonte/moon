/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Area;
import it.csi.moon.commons.entity.AreeFilter;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi degli enti
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface AreeService {
	List<Area> getElencoAree(AreeFilter filter) throws BusinessException;
	Area getAreaById(Long idArea) throws ItemNotFoundBusinessException, BusinessException;
	Area getAreaByIdEnteCodice(Long idEnte, String codiceArea) throws BusinessException;
	Area createArea(Area body) throws BusinessException;
	Area updateArea(Long idArea, Area body) throws BusinessException;
}
