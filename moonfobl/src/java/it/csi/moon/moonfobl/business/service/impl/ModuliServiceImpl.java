/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 *
 */
package it.csi.moon.moonfobl.business.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.StatoModulo;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.mapper.StatoModuloMapper;
import it.csi.moon.moonfobl.business.service.ModuliService;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.dto.moonfobl.ModuloVersioneStato;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.business.TooManyItemFoundBusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * Metodi di business relativi ai moduli
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ModuliServiceImpl implements ModuliService {
	
	private static final String CLASS_NAME = "ModuliServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;

	@Override
	public List<Modulo> getElencoModuli(ModuliFilter filter) throws BusinessException {
		try {
			return moonsrvDAO.getModuli(filter);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoModuli] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco moduli");
 		}
	}

	@Override
	public Modulo getModuloById(Long idModulo, Long idVersioneModulo, String fields) throws ItemNotFoundBusinessException, BusinessException {
		try {
			return moonsrvDAO.getModuloById(idModulo, idVersioneModulo, fields);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore invocazione DAO", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca modulo per id");
		}
	}
	
//	@Override
//	public Modulo getModuloById(Long idModulo, Long idVersioneModulo) throws ItemNotFoundBusinessException, BusinessException {
//		try {
//			return moonsrvDAO.getModuloById(idModulo, idVersioneModulo);
//		} catch (ItemNotFoundDAOException notFoundEx) {
//			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore invocazione DAO", notFoundEx);
//			throw new ItemNotFoundBusinessException();
//		} catch (DAOException e) {
//			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore invocazione DAO", e);
//			throw new BusinessException("Errore ricerca modulo per id");
//		}
//	}

	@Override
	public StatoModulo getStatoModuloById(Long idModulo, Long idVersioneModulo) throws ItemNotFoundBusinessException, BusinessException {
		try {
			return StatoModuloMapper.buildFromModuloCronologiaStatiEntity(moduloDAO.findCurrentCronologia(idVersioneModulo));
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore invocazione DAO", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloById] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca modulo per id");
		}
	}

	@Override
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws ItemNotFoundBusinessException, BusinessException {
		try {
			return moonsrvDAO.getModuloByCodice(codiceModulo, versione);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] Errore invocazione DAO", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloByCodice] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca modulo per id");
		}
	}

	@Override
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws ItemNotFoundBusinessException, TooManyItemFoundBusinessException, BusinessException {
		try {
			return moonsrvDAO.getModuloPubblicatoByCodice(codiceModulo);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca modulo pubblicato per codice");
		}
	}


	@Override
	public Long getIdModuloByCodice(String codiceModulo) throws ItemNotFoundBusinessException, BusinessException {
		try {
			return moduloDAO.findByCodice(codiceModulo).getIdModulo();
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getIdModuloByCodice] Errore invocazione DAO", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getIdModuloByCodice] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca IdModulo per codice");
		}
	}
	
	@Override
	public List<ModuloVersioneStato> getVersioniModuloById(Long idModulo, String stato) throws ItemNotFoundBusinessException, BusinessException {
		try {
			return moduloDAO.findVersioniModuloById(idModulo, stato);
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getVersioniModuloById] Errore invocazione DAO", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getVersioniModuloById] Errore invocazione DAO", e);
			throw new BusinessException("Errore getVersioniModuloById");
		}
	}
	
	
	
}
