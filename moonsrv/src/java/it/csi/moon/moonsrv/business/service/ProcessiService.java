/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.Processo;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi ai processi
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ProcessiService {
	
	List<Processo> getElencoProcessi() throws BusinessException;
	Processo getProcessoById(Long idProcesso) throws ItemNotFoundBusinessException, BusinessException;
	Processo getProcessoByCd(String codiceProcesso) throws ItemNotFoundBusinessException, BusinessException;
	Processo getProcessoByNome(String nomeProcesso) throws ItemNotFoundBusinessException, BusinessException;
	Processo createProcesso(Processo body) throws BusinessException;
	Processo updateProcesso(Long idProcesso, Processo body) throws BusinessException;
	
	//
	// Moduli
	//
	Processo getProcessoByIdModulo(Long idModulo) throws ItemNotFoundBusinessException, BusinessException;
	List<Modulo> getModuliByIdProcesso(Long idProcesso) throws BusinessException;
}
