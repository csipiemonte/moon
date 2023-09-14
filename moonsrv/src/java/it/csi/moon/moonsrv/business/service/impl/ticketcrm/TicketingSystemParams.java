/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.ticketcrm;

import it.csi.moon.commons.entity.IstanzaPdfEntity;

/**
 * Parametri per la ticketizzazione su CRM
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class TicketingSystemParams   {

	IstanzaPdfEntity istanzaPdf;
	
	public IstanzaPdfEntity getIstanzaPdf() {
		return istanzaPdf;
	}
	public void setIstanzaPdf(IstanzaPdfEntity istanzaPdfE) {
		this.istanzaPdf = istanzaPdfE;
	}
	
	public String toStringFull() {
		return "IstanzaCrmParams [istanzaPdf=" + istanzaPdf.toStringFULL()
			+ "]";
	}
	@Override
	public String toString() {
		return "IstanzaCrmParams [istanzaPdf=" + istanzaPdf
			+ "]";
	}
}
