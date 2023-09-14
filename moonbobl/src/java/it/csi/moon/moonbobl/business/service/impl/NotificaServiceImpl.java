/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.business.service.NotificaService;
import it.csi.moon.moonbobl.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.NotificaEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

@Component
public class NotificaServiceImpl implements NotificaService {
	
	private final static String CLASS_NAME = "AllegatoServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	NotificaDAO notificaDAO;
	
	@Override
	public Long insertNotifica(NotificaEntity notifica) throws BusinessException {
		try {
			return notificaDAO.insert(notifica);
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insertNotifica] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore nell aggiornamento dell allegato");
		}
	}

}
