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

import it.csi.moon.commons.dto.LogEmail;
import it.csi.moon.commons.mapper.LogEmailMapper;
import it.csi.moon.moonsrv.business.service.LogEmailService;
import it.csi.moon.moonsrv.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi alle utenti
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class LogEmailServiceImpl  implements LogEmailService {
	
	private static final String CLASS_NAME = "LogEmailServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	LogEmailDAO logEmailDAO;

	@Override
	public List<LogEmail> getElencoLogEmailByIdIstanza(Long idIstanza) {
		try {
			List<LogEmail> result = logEmailDAO.findByIdIstanza(idIstanza).stream()
				.map(LogEmailMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (DAOException dao) {
			LOG.error("[" + CLASS_NAME + "::getElencoLogEmailByIdIstanza] Errore generico servizio getElencoLogEmailByIdIstanza" + dao.getMessage());
			throw new ServiceException(dao);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoLogEmailByIdIstanza] Errore generico servizio getElencoLogEmailByIdIstanza", ex);
			throw new ServiceException("Errore generico servizio LogEmail");
		} 
	}

}
