/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;


import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.entity.ModuloClassEntity;
import it.csi.moon.commons.util.decodifica.DecodificaModuloClass;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloClassDAO;
import it.csi.moon.moonsrv.business.service.impl.protocollo.Protocollo;
import it.csi.moon.moonsrv.business.service.mapper.moonprint.PrintIstanzaMapper;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

//@Component
public class ClassLoaderModulo extends ClassLoader {
	
	private static final String CLASS_NAME = "ClassLoaderModulo";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final ClassLoader CLASS_LOADER_PADRE = ClassLoaderModulo.class.getClassLoader();
		
	@Autowired
	ModuloClassDAO moduloclassDao;

	public ClassLoaderModulo() {
		super(CLASS_LOADER_PADRE);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	public PrintIstanzaMapper findInitByIdModulo(Long idModulo) throws BusinessException {
		return findClassByIdModuloTipologia(idModulo, DecodificaModuloClass.INIT.getId());
	}
	
	public  PrintIstanzaMapper  findPrintMapperByIdModulo(Long idModulo) throws BusinessException {
		return findClassByIdModuloTipologia(idModulo, DecodificaModuloClass.MAPPER_MOONPRINT.getId());
	}

	public PrintIstanzaMapper findInitByIdModuloNullable(Long idModulo) {
		try {
			return findClassByIdModuloTipologia(idModulo, DecodificaModuloClass.INIT.getId());
		} catch (Exception e) {
			return null;
		}
	}
	
	public PrintIstanzaMapper findPrintMapperByIdModuloNullable(Long idModulo) {
		try {
			return (PrintIstanzaMapper) findClassByIdModuloTipologia(idModulo, DecodificaModuloClass.MAPPER_MOONPRINT.getId());
		} catch (Exception e) {
			return null;
		}
	}

	public Protocollo findProtocolloByIdModuloNullable(Long idModulo) {
		try {
			return (Protocollo) findClassByIdModuloTipologia(idModulo, DecodificaModuloClass.PROTOCOLLO_MANAGER.getId());
		} catch (Exception e) {
			return null;
		}
	}
	
	private <T> T findClassByIdModuloTipologia(Long idModulo, int tipologia) throws BusinessException {
		try {
			LOG.info("[" + CLASS_NAME + "::findClassByIdModuloTipologia] BEGIN ");
			ModuloClassEntity moduloClassEntity =  moduloclassDao.findClassbyIdModuloTipologia(idModulo, tipologia);
			byte[] bytesClass = moduloClassEntity.getContenuto();				
			Class moduloClass = defineClass(moduloClassEntity.getNomeClass(),bytesClass,0,bytesClass.length);
			return (T) moduloClass.getDeclaredConstructor().newInstance();
		} catch (ItemNotFoundDAOException intf) {
			LOG.warn("[" + CLASS_NAME + "::findClassByIdModuloTipologia] ItemNotFoundDAOException with idModulo="+idModulo+" tipologia= "+tipologia);
			throw new BusinessException(intf);
		} catch (DAOException dao) {
			LOG.warn("[" + CLASS_NAME + "::findClassByIdModuloTipologia] DAOException with idModulo="+idModulo+" tipologia= "+tipologia);
			throw new BusinessException(dao);
		} catch (ClassFormatError e) {
			LOG.error("[" + CLASS_NAME + "::findClassByIdModuloTipologia] ClassFormatError with idModulo="+idModulo+" tipologia= "+tipologia, e);
			throw new BusinessException();
        } catch (InstantiationException e) {
			LOG.error("[" + CLASS_NAME + "::findClassByIdModuloTipologia] InstantiationException with idModulo="+idModulo+" tipologia= "+tipologia, e);
			throw new BusinessException();
		} catch (IllegalAccessException e) {
			LOG.error("[" + CLASS_NAME + "::findClassByIdModuloTipologia] IllegalAccessException with idModulo="+idModulo+" tipologia= "+tipologia, e);
			throw new BusinessException();
		} catch (IllegalArgumentException e) {
			LOG.error("[" + CLASS_NAME + "::findClassByIdModuloTipologia] IllegalArgumentException with idModulo="+idModulo+" tipologia= "+tipologia, e);
			throw new BusinessException();
		} catch (InvocationTargetException e) {
			LOG.error("[" + CLASS_NAME + "::findClassByIdModuloTipologia] InvocationTargetException with idModulo="+idModulo+" tipologia= "+tipologia, e);
			throw new BusinessException();
		} catch (NoSuchMethodException e) {
			LOG.error("[" + CLASS_NAME + "::findClassByIdModuloTipologia] NoSuchMethodException with idModulo="+idModulo+" tipologia= "+tipologia, e);
			throw new BusinessException();
		} catch (SecurityException e) {
			LOG.error("[" + CLASS_NAME + "::findClassByIdModuloTipologia] SecurityException with idModulo="+idModulo+" tipologia= "+tipologia, e);
			throw new BusinessException();
		}
	}
 
}
