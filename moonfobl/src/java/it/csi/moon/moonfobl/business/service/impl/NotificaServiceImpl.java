/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.NotificaEntity;
import it.csi.moon.moonfobl.business.service.NotificaService;
import it.csi.moon.moonfobl.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class NotificaServiceImpl implements NotificaService {
	
	private static final String CLASS_NAME = "AllegatoServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	NotificaDAO notificaDAO;
	
	@Override
	public Long insertNotifica(NotificaEntity notifica) throws BusinessException {
		try {
			return notificaDAO.insert(notifica);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertNotifica] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore nell aggiornamento dell allegato");
		}
	}

}
