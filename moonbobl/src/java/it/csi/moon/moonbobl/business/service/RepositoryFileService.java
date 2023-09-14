/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

/**
 * Metodi di business relativi alla gestione del repository dei files
 * 
 * @author Danilo
 * 
 */
public interface RepositoryFileService {
		
	public RepositoryFileEntity getRepositoryFile(Long idFile) throws BusinessException;

	public RepositoryFileEntity findByFormIoNameFile(String formIoName)throws BusinessException;

}
