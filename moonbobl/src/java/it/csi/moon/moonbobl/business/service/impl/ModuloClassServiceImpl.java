/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.ModuloClassService;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloClassDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloClassEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.ModuloClassHelper;
import it.csi.moon.moonbobl.business.service.mapper.ModuloClassMapper;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloClass;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.LoggerAccessor;



@Component
public class ModuloClassServiceImpl implements ModuloClassService {

	private final static String CLASS_NAME = "ModuloClassServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ModuloClassDAO moduloClassDao ;
	@Autowired
	MoonsrvDAO moonsrvDao;
	
	@Override
	public List<ModuloClass> getFileClassByIdModulo(Long idModulo) {
		try {
			return moduloClassDao.findDBClassbyIdModulo(idModulo)
					.stream()
					.map(ModuloClassMapper::buildFromModuloClassEntity)
					.collect(Collectors.toList());
				
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getFileClassByIdModulo] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero moduli class");
 		}
	}

	@Override
	public ModuloClass getFileClassByIdModuloTipologia(Long idModulo, int idTipologia) throws ItemNotFoundBusinessException, BusinessException {
		try {
			ModuloClassEntity classEntity = moduloClassDao.findDBClassbyIdModuloTipologia(idModulo,idTipologia); 
			ModuloClass ris = ModuloClassMapper.buildFromModuloClassEntity(classEntity);
			return ris;
		} catch (ItemNotFoundDAOException nfe) {
			log.warn("[" + CLASS_NAME + "::getFileClassByIdModuloTipologia] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException("Errore recupero elenco modulo class");
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getFileClassByIdModuloTipologia] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco modulo class");
 		}
	}

	@Override
	public ModuloClass uploadFileClass(Long idModulo, int idTipologia, byte[] bytes) {
		try {
			// call moonsrv
			ModuloClass ris = moonsrvDao.uploadModuloClass(idModulo, idTipologia, bytes);
			return ris;
		} catch (DAOException d) {
			log.error("[" + CLASS_NAME + "::uploadFileClass] Errore invocazione DAO", d);
			throw new BusinessException(d);
		}		
	}

	@Override
	public void delete(Long idModulo, int idTipologia) {
		try {
			// call moonsrv
			moonsrvDao.deleteModuloClass(idModulo, idTipologia);
		} catch (DAOException d) {
			log.error("[" + CLASS_NAME + "::delete] Errore invocazione DAO", d);
			throw new BusinessException(d);
		}
	}

}
