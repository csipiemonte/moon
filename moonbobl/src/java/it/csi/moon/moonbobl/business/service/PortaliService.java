/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.Portale;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alle portali
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface PortaliService {
	
	List<Portale> getElencoPortali() throws BusinessException;
	Portale getPortaleById(Long idPortale) throws ItemNotFoundBusinessException, BusinessException;
	Portale getPortaleByCd(String codicePortale) throws ItemNotFoundBusinessException, BusinessException;
	Portale getPortaleByNome(String nomePortale) throws ItemNotFoundBusinessException, BusinessException;
	Portale createPortale(Portale body) throws BusinessException;
	Portale updatePortale(Long idPortale, Portale body) throws BusinessException;
	
	//
	// Moduli
	//
	List<Modulo> getModuliByIdPortale(Long idPortale) throws BusinessException;
}
