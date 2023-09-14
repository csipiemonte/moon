/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

public class CfiCompanyEntity {
	private String cfi;
	private String companyName;
	
	
	public CfiCompanyEntity() {
	}

	public CfiCompanyEntity(String cfi, String companyName) {
		super();
		this.cfi = cfi;
		this.companyName = companyName;
	}

	public String getCfi() {
		return cfi;
	}

	public void setCfi(String cfi) {
		this.cfi = cfi;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "CfiCompany [cfi=" + cfi + ", companyName=" + companyName + "]";
	}
}
