/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.Processo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;

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
	Processo getProcessoById(Long idProcesso, String fields) throws ItemNotFoundBusinessException, BusinessException;
	Processo getProcessoByCd(String codiceProcesso) throws ItemNotFoundBusinessException, BusinessException;
	Processo getProcessoByNome(String nomeProcesso) throws ItemNotFoundBusinessException, BusinessException;
	Processo createProcesso(UserInfo user, Processo processo) throws BusinessException;
	Processo updateProcesso(UserInfo user, Processo processo) throws BusinessException;
	
	//
	// Moduli
	//
	Processo getProcessoByIdModulo(Long idModulo) throws ItemNotFoundBusinessException, BusinessException;
	List<Modulo> getModuliByIdProcesso(Long idProcesso) throws BusinessException;
	
	//
	// Image
	byte[] getByteArrayImageById(Long idProcesso) throws BusinessException;
}
