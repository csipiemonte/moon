/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.mapper.DocumentoMapper;
import it.csi.moon.moonfobl.business.service.RepositoryFileService;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

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
	MoonsrvDAO moonsrvDAO;
	
	@Override
	public RepositoryFileEntity getRepositoryFile(Long idFile) throws BusinessException {
		
		LOG.debug("[" + CLASS_NAME + "::getRepositoryFile] IN idFile: "+idFile);		
		RepositoryFileEntity result = null;
		
		try {
			result = repositoryFileDAO.findById(idFile);
			
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFile] ricevuta non trovata", e);
			throw new BusinessException("Errore recupero ricevuta pdf per idFile=" + idFile);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFile] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero ricevuta pdf per idFile=" + idFile);
		}
		
		return result;
	}
	
	@Override
	public RepositoryFileEntity getRepositoryFileByName(String nameFile) throws BusinessException {
		
		LOG.debug("[" + CLASS_NAME + "::getRepositoryFileByName] IN idFile: "+nameFile);		
		RepositoryFileEntity result = null;
		
		try {
			result = repositoryFileDAO.findByFormioNameFile(nameFile);
			
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileByName] ricevuta non trovata", e);
			throw new BusinessException("Errore recupero ricevuta pdf per idFile=" + nameFile);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getRepositoryFileByName] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero ricevuta pdf per idFile=" + nameFile);
		}
		
		return result;
	}
	
	@Override
	public List<Documento> findByIdIstanza(Long idIstanza) throws BusinessException {
		
		LOG.debug("[" + CLASS_NAME + "::findByIdIstanza] IN idIstanza: "+idIstanza);		
		List<Documento> result = null;
		
		try {
			result = repositoryFileDAO.findByIdIstanza(idIstanza).stream()
					.map(DocumentoMapper::buildFromEntity)
					.collect(Collectors.toList());
			
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanza] ricevuta non trovata", e);
			throw new BusinessException("Errore findByIdIstanza per idIstanza=" + idIstanza);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanza] Errore invocazione DAO", e);
			throw new BusinessException("Errore findByIdIstanza per idIstanza=" + idIstanza);
		}
		
		return result;
	}
	
	@Override
	public List<Documento> findProtocollatiByIdIstanza(Long idIstanza) throws BusinessException {
		
		LOG.debug("[" + CLASS_NAME + "::findProtocollatiByIdIstanza] IN idIstanza: "+idIstanza);		
		List<Documento> result = null;
		
		try {
			result = repositoryFileDAO.findProtocollatiByIdIstanza(idIstanza).stream()
					.map(DocumentoMapper::buildFromEntity)
					.collect(Collectors.toList());
			
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::findProtocollatiByIdIstanza] documenti non trovati", e);
			throw new BusinessException("Errore findProtocollatiByIdIstanza per idIstanza=" + idIstanza);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::findProtocollatiByIdIstanza] Errore invocazione DAO", e);
			throw new BusinessException("Errore findProtocollatiByIdIstanza per idIstanza=" + idIstanza);
		}
		
		return result;
	}
	
	@Override
	public List<Documento> findEmessiDaUfficioByIdIstanza(Long idIstanza) throws BusinessException {
		
		LOG.debug("[" + CLASS_NAME + "::findEmessiDaUfficioByIdIstanza] IN idIstanza: "+idIstanza);		
		List<Documento> result = null;
		
		try {
			result = repositoryFileDAO.findEmessiDaUfficioByIdIstanza(idIstanza).stream()
					.map(DocumentoMapper::buildFromEntity)
					.collect(Collectors.toList());
			
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::findEmessiDaUfficioByIdIstanza] documenti non trovati", e);
			throw new BusinessException("Errore findEmessiDaUfficioByIdIstanza per idIstanza=" + idIstanza);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::findProtocollatiByIdIstanza] Errore invocazione DAO", e);
			throw new BusinessException("Errore findEmessiDaUfficioByIdIstanza per idIstanza=" + idIstanza);
		}
		
		return result;
	}

	@Override
	public RepositoryFileEntity findByFormIoNameFile(String formIoName) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::findByFormIoNameFile] IN formIoNameFile: "+formIoName);		
		RepositoryFileEntity result = null;
		
		try {
			result = repositoryFileDAO.findByFormioNameFile(formIoName);
			if( result.getContenuto() == null && result.getUuidIndex() != null ) {
				//chiedo a index
				result.setContenuto(moonsrvDAO.getContenutoIndexByUid(result.getUuidIndex()));
			}
			
		} catch (ItemNotFoundDAOException e) {
			LOG.error("[" + CLASS_NAME + "::findByFormIoNameFile] ricevuta non trovata", e);
			throw new BusinessException("Errore recupero ricevuta pdf per formIoNameFile=" + formIoName);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::findByFormIoNameFile] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero ricevuta pdf per formIoNameFile=" + formIoName);
		}
		
		return result;
	}
	
	
}

