/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.doc.dto;

import java.util.List;

import it.csi.apimint.mydocs.be.v1.dto.AmbitoResponse;

public class ApimintResponseAmbiti {

	private List<AmbitoResponse> ambiti;

	public List<AmbitoResponse> getAmbiti() {
		return ambiti;
	}
	public void setAmbiti(List<AmbitoResponse> ambiti) {
		this.ambiti = ambiti;
	}
	
	@Override
	public String toString() {
		return "ApimintResponseAmbiti [ambiti=" + ambiti + "]";
	}

}
