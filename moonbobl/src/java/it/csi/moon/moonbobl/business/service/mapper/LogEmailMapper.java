/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import it.csi.moon.moonbobl.business.service.impl.dto.LogEmailEntity;
import it.csi.moon.moonbobl.dto.moonfobl.LogEmail;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoLogEmail;

/**
 * Costruttore di oggetto JSON LogEmail
 *  da LogEmailEntity {@code entity}
 * 
 * @author Laurent
 */

public class LogEmailMapper {
	
	public static LogEmail buildFromEntity (LogEmailEntity entity) {
		
		LogEmail logEmail = new LogEmail();
		logEmail.setIdLogEmail(entity.getIdLogEmail());
		logEmail.setDataLogEmail(entity.getDataLogEmail());
		logEmail.setTipologia(DecodificaTipoLogEmail.byId(entity.getIdTipologia()).getTipoLogEmail());
		logEmail.setIdEnte(entity.getIdEnte());
		logEmail.setIdModulo(entity.getIdModulo());
		logEmail.setIdIstanza(entity.getIdIstanza());
		logEmail.setEmailDestinatario(entity.getEmailDestinatario());
		logEmail.setTipoEmail(entity.getTipoEmail());
		logEmail.setEsito(entity.getEsito());
		
		return logEmail;	
	}
}
