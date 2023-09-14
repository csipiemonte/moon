/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.ticketcrm;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Interface TicketingSystem
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface TicketingSystem {

	public String creaTicketIstanza(Istanza istanza, TicketingSystemParams params) throws BusinessException;
	
	public TicketingSystemParams readParams() throws BusinessException;

}
