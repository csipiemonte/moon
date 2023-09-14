/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.Area;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alle aree
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface AreeService {
	List<Area> getElencoArea() throws BusinessException;
	List<Area> getElencoAreaByIdEnte(Long idEnte) throws BusinessException;
	Area getAreaById(Long idArea) throws ItemNotFoundBusinessException, BusinessException;
	Area createArea(Area body) throws BusinessException;
	Area updateArea(Long idArea, Area body) throws BusinessException;
}
