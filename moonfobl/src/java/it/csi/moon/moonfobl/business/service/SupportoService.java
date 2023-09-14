/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import java.util.List;

import it.csi.moon.commons.dto.RichiestaSupporto;
import it.csi.moon.commons.entity.RichiestaSupportoFilter;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alle Richieste di Supporto
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 24/08/2020 - versione iniziale
 */
public interface SupportoService {
	List<RichiestaSupporto> getElencoRichiestaSupporto(RichiestaSupportoFilter filter) throws BusinessException;
	RichiestaSupporto getRichiestaSupportoById(Long idRichiestaSupporto) throws ItemNotFoundBusinessException, BusinessException;
	RichiestaSupporto createRichiestaSupporto(RichiestaSupporto richiesta) throws BusinessException;
}
