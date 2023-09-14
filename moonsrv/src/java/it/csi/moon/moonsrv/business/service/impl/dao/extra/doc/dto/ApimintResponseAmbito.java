/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.dto;

import it.csi.apimint.mydocs.be.v1.dto.AmbitoResponse;

public class ApimintResponseAmbito {

	private AmbitoResponse ambito;
	
	public AmbitoResponse getAmbito() {
		return ambito;
	}

	public void setAmbito(AmbitoResponse ambito) {
		this.ambito = ambito;
	}

	@Override
	public String toString() {
		return "ApimintResponseAmbito [ambito=" + ambito + "]";
	}

}
