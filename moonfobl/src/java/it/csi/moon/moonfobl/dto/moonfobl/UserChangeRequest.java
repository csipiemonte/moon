/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

import it.csi.moon.commons.dto.Ente;

public class UserChangeRequest {

	private Ente ente = null;

	public Ente getEnte() {
		return ente;
	}
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	
	@Override
	public String toString() {
		return "UserChangeRequest [ente=" + ente + "]";
	}

}
