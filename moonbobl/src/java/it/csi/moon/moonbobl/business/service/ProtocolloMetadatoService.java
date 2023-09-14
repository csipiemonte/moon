/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.ProtocolloMetadato;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi ai metadati del protocollo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ProtocolloMetadatoService {
	List<ProtocolloMetadato> getElenco() throws BusinessException;
	ProtocolloMetadato getById(Long idMetadato) throws ItemNotFoundBusinessException, BusinessException;
	ProtocolloMetadato create(ProtocolloMetadato body) throws BusinessException;
	ProtocolloMetadato update(Long idMetadato, ProtocolloMetadato body) throws BusinessException;
}
