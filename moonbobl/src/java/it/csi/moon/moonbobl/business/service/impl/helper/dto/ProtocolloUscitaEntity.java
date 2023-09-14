/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.List;

/**
 * Entity per i dati di protocollo in uscita
 * <br>
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class ProtocolloUscitaEntity {

	private String numeroProtocollo;
	private String dataProtocollo;
	
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	public String getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	

		
}
