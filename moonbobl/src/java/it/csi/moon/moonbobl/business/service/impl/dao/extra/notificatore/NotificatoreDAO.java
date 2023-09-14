/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.notificatore;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.Message;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

/**
*
* @author 
*/
public interface NotificatoreDAO {

	public String inviaNotifica(Message message) throws DAOException;
	
}
