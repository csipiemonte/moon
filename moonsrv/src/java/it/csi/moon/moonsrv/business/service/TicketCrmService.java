/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ResponseOperazioneMassiva;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Metodi di business relativi all'apertura di Ticket CRM
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface TicketCrmService {
	public void creaTicketCrmIstanza(Long idIstanza) throws BusinessException;	
	public void creaTicketCrmIstanza(Istanza istanza) throws BusinessException;

	//
	public ResponseOperazioneMassiva creaTicketCrmIstanzaMassivo(Long idTag);

}
