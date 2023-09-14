/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.TipoCodiceIstanza;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alle decodifiche della codifica delle istanze di un modulo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface TipiCodiceIstanzaService {
	List<TipoCodiceIstanza> getElencoTipoCodiceIstanza() throws BusinessException;
	TipoCodiceIstanza getTipoCodiceIstanzaById(Integer idTipoCodiceIstanza) throws ItemNotFoundBusinessException, BusinessException;
	TipoCodiceIstanza getTipoCodiceIstanzaByCodice(String descCodice) throws ItemNotFoundBusinessException, BusinessException;
}
