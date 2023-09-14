/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dto;

import java.util.List;

import it.csi.apimint.demografia.v1.dto.Soggetto;

public class ApimintResponseAnprFamiglia {

	private List<Soggetto> famiglia;

	public List<Soggetto> getFamiglia() {
		return famiglia;
	}
	public void setFamiglia(List<Soggetto> soggetti) {
		this.famiglia = soggetti;
	}
	
	@Override
	public String toString() {
		return "ApimintResponseAnprFamiglia [famiglia=" + famiglia + "]";
	}

}
