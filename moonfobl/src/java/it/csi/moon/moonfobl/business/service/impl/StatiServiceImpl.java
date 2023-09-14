/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 *
 */
package it.csi.moon.moonfobl.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Stato;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.mapper.StatoMapper;
import it.csi.moon.moonfobl.business.service.StatiService;
import it.csi.moon.moonfobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
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
public class StatiServiceImpl implements StatiService {
	
	private static final String CLASS_NAME = "StatiServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	StatoDAO statoDAO;

	@Override
	public List<Stato> getElencoStati(ModuliFilter filter) throws BusinessException {
		try {
			return statoDAO.find(filter).stream().map(StatoMapper::buildFromEntity).collect(Collectors.toList());
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getElencoStati] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco moduli");
 		}
	}

		
}
