/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.moon.moonbobl.business.service.RepositoryFileService;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Metodi di business relativi all'accesso al repository dei files
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
@Component
public class RepositoryFileServiceImpl implements RepositoryFileService {
	
	private final static String CLASS_NAME = "RepositoryFileServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	@Override
	public RepositoryFileEntity getRepositoryFile(Long idFile) throws BusinessException {
		
		log.debug("[" + CLASS_NAME + "::getRepositoryFile] IN idFile: "+idFile);		
		RepositoryFileEntity result = null;
		
		try {
			result = repositoryFileDAO.findById(idFile);
			
		} catch (ItemNotFoundDAOException e) {
			log.error("[" + CLASS_NAME + "::getRepositoryFile] ricevuta non trovata", e);
			throw new BusinessException("Errore recupero ricevuta pdf per idFile=" + idFile);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getRepositoryFile] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero ricevuta pdf per idFile=" + idFile);
		}
		
		return result;
	}
	
	@Override
	public RepositoryFileEntity findByFormIoNameFile(String formIoName) throws BusinessException {
		log.debug("[" + CLASS_NAME + "::findByFormIoNameFile] IN formIoNameFile: "+formIoName);		
		RepositoryFileEntity result = null;
		try {
			result = repositoryFileDAO.findByFormioNameFile(formIoName);
			if( result.getContenuto() == null && result.getUuidIndex() != null ) {
				//chiedo a index
				result.setContenuto(moonsrvDAO.getContenutoIndexByUid(result.getUuidIndex()));
			}
		} catch (ItemNotFoundDAOException e) {
			log.error("[" + CLASS_NAME + "::findByFormIoNameFile] ricevuta non trovata", e);
			throw new BusinessException("Errore recupero ricevuta pdf per formIoNameFile=" + formIoName);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::findByFormIoNameFile] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero ricevuta pdf per formIoNameFile=" + formIoName);
		}
		
		return result;
	}
}

