/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm;

import it.csi.apimint.nextcrm.v1.dto.NewTicket;
import it.csi.apimint.nextcrm.v1.dto.Ticket;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

public interface NextcrmApimintDAO {

	public Ticket creaTicket(NewTicket newTicket) throws DAOException;
	
}
