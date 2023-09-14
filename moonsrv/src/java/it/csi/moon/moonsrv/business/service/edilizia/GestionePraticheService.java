/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.edilizia;

import java.util.List;

import it.csi.moon.commons.dto.extra.edilizia.PraticaEdilizia;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business Pratiche Edilizie
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public interface GestionePraticheService {
	 
//	public List<PraticaEdilizia> getElencoPratiche(String anno, Integer registro, String progressivo) throws BusinessException;
	
	public PraticaEdilizia getPratica(Integer registro, Integer progressivo, Integer anno) throws BusinessException, ItemNotFoundBusinessException;
	
	public List<PraticaEdilizia> getPratiche(Integer registro, Integer progressivo, Integer anno) throws BusinessException, ItemNotFoundBusinessException;
	
	public String getPraticaJson(Integer registro, Integer progressivo, Integer anno) throws BusinessException, ItemNotFoundBusinessException;
}
