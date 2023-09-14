/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;
/*
 * Rappresenta allegato inviato al servizio upload
 * 
 * 
 */

import java.util.Arrays;

public class AllegatoEntity extends AllegatoLazyEntity {
	
	private byte[] contenuto;
	
	public AllegatoEntity() {
		super();
	}
	
	public byte[] getContenuto() {
		return contenuto;
	}
	public void setContenuto(byte[] contenuto) {
		this.contenuto = contenuto;
	}

	@Override
	public String toString() {
		return "AllegatoEntity [" + super.toString()
				+ ", contenuto=" + ((contenuto==null)?"null":(Arrays.toString(contenuto).substring(0, 10)+"... len="+contenuto.length))
				+ "]";
	}

	public String toStringFULL() {
		return "AllegatoEntity [" + super.toString()
				+ ", contenuto=" + contenuto
				+ "]";
	}
	
	public boolean isValid() {
		return (contenuto != null && contenuto.length > 0) && super.isValid();
	}
}
