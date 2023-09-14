/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm;

import it.csi.apirest.otrs.v1.dto.TicketRequest;
import it.csi.apirest.otrs.v1.dto.TicketResponse;

public interface OtrsApiRestDAO {

	public TicketResponse createTicket(TicketRequest request);

}
