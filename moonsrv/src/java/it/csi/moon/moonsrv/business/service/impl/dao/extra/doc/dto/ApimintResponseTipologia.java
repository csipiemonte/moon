/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.dto;

import it.csi.apimint.mydocs.be.v1.dto.TipologiaResponse;

public class ApimintResponseTipologia {

	private TipologiaResponse tipologia;
	
	public TipologiaResponse getTipologia() {
		return tipologia;
	}

	public void setTipologia(TipologiaResponse tipologia) {
		this.tipologia = tipologia;
	}

	@Override
	public String toString() {
		return "ApimintResponseTipologia [tipologia=" + tipologia + "]";
	}

}
