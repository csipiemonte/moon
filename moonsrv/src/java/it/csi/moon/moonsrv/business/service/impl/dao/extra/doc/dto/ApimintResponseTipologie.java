/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.dto;

import java.util.List;

import it.csi.apimint.mydocs.be.v1.dto.TipologiaResponse;

public class ApimintResponseTipologie {

	private List<TipologiaResponse> tipologie;

	public List<TipologiaResponse> getTipologie() {
		return tipologie;
	}
	public void setAmbiti(List<TipologiaResponse> tipologie) {
		this.tipologie = tipologie;
	}
	
	@Override
	public String toString() {
		return "ApimintResponseTipologie [tipologie=" + tipologie + "]";
	}

}
