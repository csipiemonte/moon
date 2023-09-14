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

import it.csi.moon.moonbobl.business.service.CosmoService;
import it.csi.moon.moonbobl.business.service.impl.dao.CosmoLogPraticaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.CosmoLogServizioDAO;
import it.csi.moon.moonbobl.business.service.mapper.LogPraticaCosmoMapper;
import it.csi.moon.moonbobl.business.service.mapper.LogServizioCosmoMapper;
import it.csi.moon.moonbobl.dto.moonfobl.LogPraticaCosmo;
import it.csi.moon.moonbobl.dto.moonfobl.LogServizioCosmo;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Metodi di business del Notificatore
 * 
 * @author Laurent
 */

@Component
public class CosmoServiceImpl implements CosmoService {
	
	private static final String CLASS_NAME = "CosmoServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	CosmoLogPraticaDAO cosmoLogPraticaDAO;
	
	@Autowired
	CosmoLogServizioDAO cosmoLogServizioDAO;

	@Override
	public List<LogPraticaCosmo> getElencoLogPraticaByIdIstanza(Long idIstanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getElencoLogPraticaByIdIstanza] IN idIstanza = " + idIstanza);
			//
			List<LogPraticaCosmo> result = cosmoLogPraticaDAO.findByIdIstanza(idIstanza).stream()
				.map(LogPraticaCosmoMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (DAOException dao) {
			LOG.warn("[" + CLASS_NAME + "::getElencoLogPraticaByIdIstanza] Errore generico servizio getElencoLogPraticaByIdIstanza for idIstanza=" + idIstanza);
			throw new ServiceException(dao);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoLogPraticaByIdIstanza] Errore generico servizio getElencoLogPraticaByIdIstanza", ex);
			throw new ServiceException("Errore generico servizio LogPraticaCosmo");
		} 
	}
	
	@Override
	public List<LogServizioCosmo> getElencoLogServizioByIdIstanza(Long idIstanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getElencoLogServizioByIdIstanza] IN idIstanza = " + idIstanza);
			//
			List<LogServizioCosmo> result = cosmoLogServizioDAO.findByIdIstanza(idIstanza).stream()
				.map(LogServizioCosmoMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (DAOException dao) {
			LOG.warn("[" + CLASS_NAME + "::getElencoLogServizioByIdIstanza] Errore generico servizio getElencoLogServizioByIdIstanza for idIstanza=" + idIstanza);
			throw new ServiceException(dao);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoLogServizioByIdIstanza] Errore generico servizio getElencoLogServizioByIdIstanza", ex);
			throw new ServiceException("Errore generico servizio LogServizioCosmo");
		} 
	}
}
