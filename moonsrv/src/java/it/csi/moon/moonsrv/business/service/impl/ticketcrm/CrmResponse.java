/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.ticketcrm;

public class CrmResponse {
	String ticketUUIDPrincipale;
	boolean creatoAdesso;
	//
	public CrmResponse(String ticketUUIDPrincipale, boolean creatoAdesso) {
		super();
		this.ticketUUIDPrincipale = ticketUUIDPrincipale;
		this.creatoAdesso = creatoAdesso;
	}
	// GET
	public String getTicketUUIDPrincipale() {
		return ticketUUIDPrincipale;
	}
	public boolean iscreatoAdesso() {
		return creatoAdesso;
	}
}
