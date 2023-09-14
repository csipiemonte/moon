/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.StatoModulo;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.moonfobl.dto.moonfobl.ModuloVersioneStato;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.TooManyItemFoundBusinessException;

/**
 * @author franc
 * Metodi di business relativi ai moduli
 */
public interface ModuliService {
	public List<Modulo> getElencoModuli(ModuliFilter filter) throws BusinessException;	
//	public Modulo getModuloById(Long idModulo, Long idVersioneModulo) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo getModuloById(Long idModulo, Long idVersioneModulo, String fields) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws ItemNotFoundBusinessException, BusinessException;
	public StatoModulo getStatoModuloById(Long idModulo, Long idVersioneModulo) throws ItemNotFoundBusinessException, BusinessException;
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws ItemNotFoundBusinessException, TooManyItemFoundBusinessException, BusinessException;
	public Long getIdModuloByCodice(String codiceModulo) throws ItemNotFoundBusinessException, BusinessException;
	public List<ModuloVersioneStato> getVersioniModuloById(Long idModulo, String stato) throws ItemNotFoundBusinessException, BusinessException;
}
