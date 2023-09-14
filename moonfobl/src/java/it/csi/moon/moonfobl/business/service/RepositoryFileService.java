/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

/**
 * Metodi di business relativi alla gestione del repository dei files
 * 
 * @author Danilo
 * 
 */
public interface RepositoryFileService {
		
	public RepositoryFileEntity getRepositoryFile(Long idFile) throws BusinessException;

	public RepositoryFileEntity getRepositoryFileByName(String nameFile) throws BusinessException;

	public List<Documento> findByIdIstanza(Long idIstanza) throws BusinessException;

	public List<Documento> findProtocollatiByIdIstanza(Long idIstanza) throws BusinessException;
	
	public List<Documento> findEmessiDaUfficioByIdIstanza(Long idIstanza) throws BusinessException;

	public RepositoryFileEntity findByFormIoNameFile(String formIoName)throws BusinessException;

}
