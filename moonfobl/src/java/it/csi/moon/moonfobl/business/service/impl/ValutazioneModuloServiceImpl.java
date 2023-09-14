/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ValutazioneModuloEntity;
import it.csi.moon.moonfobl.business.service.ValutazioneModuloService;
import it.csi.moon.moonfobl.business.service.impl.dao.ValutazioneModuloDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class ValutazioneModuloServiceImpl implements ValutazioneModuloService {

	private static final String CLASS_NAME = "ValutazioneModuloServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	ValutazioneModuloDAO valutazioneModuloDAO;
	
	@Override
	public void insertValutazioneModulo(ValutazioneModuloEntity entity) throws BusinessException {
		try {
			entity.setDataIns(new Date());
			valutazioneModuloDAO.insert(entity);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::insertValutazioneModulo] Errore generico servizio insertValutazioneModulo",ex);
			throw new ServiceException("Errore generico servizio insert ValutazioneModulo");
		}
	}

}
