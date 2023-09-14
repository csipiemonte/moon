/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.notificatore;

import it.csi.apirest.notify.mb.v1.dto.Message;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
* DAO Notificatore Preferences via servizi REST 
* 
* @author Danilo Mosca
* 
* @since 1.0.0
*/
public interface NotificatoreMbDAO {
	public String invia(String endpoint, String serviceToken, Message message) throws DAOException;
}
