/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.LogEmail;
import it.csi.moon.commons.entity.LogEmailEntity;
import it.csi.moon.commons.util.decodifica.DecodificaTipoLogEmail;

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
