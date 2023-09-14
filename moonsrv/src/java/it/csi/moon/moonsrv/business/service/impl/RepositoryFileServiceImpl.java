/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.RepositoryFile;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.RepositoryFileFilter;
import it.csi.moon.commons.mapper.RepositoryFileMapper;
import it.csi.moon.moonsrv.business.service.IndexService;
import it.csi.moon.moonsrv.business.service.RepositoryFileService;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi all'accesso al repository dei files
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
@Component
public class RepositoryFileServiceImpl implements RepositoryFileService {
	
	private static final String CLASS_NAME = "RepositoryFileServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	IndexService indexService;
	
	//
	// RepositoryFile
	@Override
	public List<RepositoryFile> getElencoRepositoryFile(RepositoryFileFilter filter, String fields) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getElencoRepositoryFile] IN idFile: ");
		try {
			if (StringUtils.isNotBlank(fields) && fields.contains("contenuto")) {
				return repositoryFileDAO.findByFilter(filter).stream()
					.map(RepositoryFileMapper::buildFromEntity)
					.collect(Collectors.toList());
			} else {
				return repositoryFileDAO.findLazyByFilter(filter).stream()
					.map(RepositoryFileMapper::buildFromEntity)
					.collect(Collectors.toList());
			}
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoRepositoryFile] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco repository file" );
		}
	}
	@Override
	public RepositoryFile getRepositoryFileById(Long idFile, String fields) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getRepositoryFileById] IN idFile=" + idFile + "  fields=" + fields);
		try {
			RepositoryFileEntity entity = null;
			if (StringUtils.isNotBlank(fields) && fields.contains("contenuto")) {
				entity = repositoryFileDAO.findById(idFile);
			} else {
				entity = (RepositoryFileEntity) repositoryFileDAO.findLazyById(idFile);
			}
			RepositoryFile result = RepositoryFileMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileById] repositoryFile non trovato " + idFile, e);
			throw new BusinessException("Errore recupero repositoryFile per idFile=" + idFile);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileById] Errore invocazione DAO " + idFile, e);
			throw new BusinessException("Errore recupero repositoryFile per idFile=" + idFile);
		}
	}


	//
	// RepositoryFileEntity
	@Override
	public RepositoryFileEntity getRepositoryFileEntity(Long idFile) throws ItemNotFoundBusinessException, BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getRepositoryFileEntity] IN idFile: "+idFile);		
		RepositoryFileEntity result = null;
		try {
			result = repositoryFileDAO.findById(idFile);
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileEntity] ricevuta non trovata", e);
			throw new ItemNotFoundBusinessException("Errore recupero RepositoryFileEntity per idFile=" + idFile);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileEntity] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero RepositoryFileEntity per idFile=" + idFile);
		}
		return result;
	}
	
	@Override
	public List<RepositoryFileEntity> getElencoRepositoryFileEntity(RepositoryFileFilter filter) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getElencoRepositoryFileEntity] IN idFile: ");
		List<RepositoryFileEntity> result = new ArrayList<>();
		try {
			result = repositoryFileDAO.findByFilter(filter);
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoRepositoryFileEntity] ricevuta non trovata", e);
			throw new BusinessException("Errore recupero repository file Entity" );
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoRepositoryFileEntity] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero repository file Entity" );
		}
		return result;
	}
	
	@Override
	public int updateUuidIndex(RepositoryFileEntity r) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::updateUuidIndex] IN idFile: "+r.getIdFile());		
		int result = 0;
		try {
			result = repositoryFileDAO.updateUuidIndex(r);
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::updateUuidIndex] repo file non trovata", e);
			throw new BusinessException("Errore updateUuidIndex repository file" );
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::updateUuidIndex] Errore invocazione DAO", e);
			throw new BusinessException("Errore updateUuidIndex repository file" );
		}
		return result;
	}
	
	@Override
	public int update(RepositoryFileEntity r) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::update] IN idFile: "+r.getIdFile());		
		int result = 0;
		
		try {
			result = repositoryFileDAO.update(r);
			
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::update] repo file non trovata", e);
			throw new BusinessException("Errore update repository file" );
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore invocazione DAO", e);
			throw new BusinessException("Errore update repository file" );
		}
		
		return result;
	}
	
	public byte[] getContenutoRepositoryFile(Long idFile) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::getContenutoRepositoryFile] IN idFile: "+idFile);		
		byte[] result = null;		
		try { 
			RepositoryFileEntity entity = repositoryFileDAO.findById(idFile);
			result = entity.getContenuto();
			if(result == null && entity.getUuidIndex() != null) {
				//chiedo a index
				result = indexService.getContentByUid(entity.getUuidIndex());
			}
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::update] repo file non trovata", e);
			throw new BusinessException("Errore update repository file" );
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore invocazione DAO", e);
			throw new BusinessException("Errore update repository file" );
		}
		return result;
	}
}

