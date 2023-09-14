/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.RepositoryFile;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.RepositoryFileFilter;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alla gestione del repository dei files
 * 
 * @author Danilo
 * 
 */
public interface RepositoryFileService {
	
	public List<RepositoryFile> getElencoRepositoryFile(RepositoryFileFilter filter, String fields) throws BusinessException;
	public RepositoryFile getRepositoryFileById(Long idFile, String fields) throws ItemNotFoundBusinessException, BusinessException;
	
	//
	// RepositoryFileEntity
	public RepositoryFileEntity getRepositoryFileEntity(Long idFile) throws ItemNotFoundBusinessException, BusinessException;
	public List<RepositoryFileEntity> getElencoRepositoryFileEntity(RepositoryFileFilter filter) throws BusinessException;
	public int updateUuidIndex(RepositoryFileEntity r) throws BusinessException;
	public int update(RepositoryFileEntity r) throws BusinessException;
	
	public byte[] getContenutoRepositoryFile(Long idFile) throws BusinessException;

}
