/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.ModuloClass;
import it.csi.moon.commons.entity.ModuloClassEntity;
import it.csi.moon.commons.mapper.ModuloClassMapper;
import it.csi.moon.moonsrv.business.service.ModuloClassService;
import it.csi.moon.moonsrv.business.service.helper.ModuloClassHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloClassDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;



@Component
public class ModuloClassServiceImpl implements ModuloClassService {

	private static final String CLASS_NAME = "ModuloClassServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ModuloClassDAO moduloClassDao;
	
	@Override
	public List<ModuloClass> getFileClassByIdModulo(Long idModulo) {
		try {
			return moduloClassDao.findDBClassbyIdModulo(idModulo)
					.stream()
					.map(ModuloClassMapper::buildFromModuloClassEntity)
					.collect(Collectors.toList());
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getFileClassByIdModulo] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero moduli class");
 		}
	}

	@Override
	public ModuloClass getFileClassByIdModuloTipologia(Long idModulo, int idTipologia) {
		try {
			ModuloClassEntity classEntity = moduloClassDao.findDBClassbyIdModuloTipologia(idModulo,idTipologia); 
			ModuloClass ris = ModuloClassMapper.buildFromModuloClassEntity(classEntity);
			return ris;	
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getFileClassByIdModuloTipologia] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco modulo class");
 		}
	}

	@Override
	public ModuloClass uploadModuloClass(Long idModulo, int idTipologia, byte[] bytes) {
		try {
			ModuloClassEntity classEntity = new ModuloClassHelper().verify(idModulo, idTipologia, bytes);
			ModuloClassEntity classEntityDB = null;
			try {
				classEntityDB = moduloClassDao.findDBClassbyIdModuloTipologia(idModulo,idTipologia); 
				classEntity.setId(classEntityDB.getId());
				moduloClassDao.updateFileClass(classEntity);
			} catch (ItemNotFoundDAOException e) {
				moduloClassDao.insert(classEntity);
			}
			ModuloClass ris = ModuloClassMapper.buildFromModuloClassEntity(classEntity);
			return ris;		
		} catch (BusinessException be) {
			throw be;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::uploadFileClass] Errore invocazione DAO", e);
			throw new BusinessException("Errore salvataggio modulo class","MOONSRV-20703");
 		}
	}

	@Override
	public void delete(Long idModulo, int idTipologia) {
		try {
			moduloClassDao.deleteByIdModuloTipologia(idModulo,idTipologia); 
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::uploadFileClass] Errore invocazione DAO", e);
			throw new BusinessException("Errore cancellazione modulo class","MOONSRV-20704");
 		}
	}
	
}
