/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

public class EPayPagoPAParams {

	private String codiceFiscale;

	public EPayPagoPAParams() {
		super();
	}
	public EPayPagoPAParams(String codiceFiscale) {
		super();
		this.codiceFiscale = codiceFiscale;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@Override
	public String toString() {
		return "EPayPagoPAParams [codiceFiscale=" + codiceFiscale + "]";
	}
	
	//
	// INNER BUILDER
	public static class Builder {
		private String codiceFiscale;

		public Builder codiceFiscale(String codiceFiscale) {
			this.codiceFiscale = codiceFiscale;
			return this;
		}
		public EPayPagoPAParams build() {
			return new EPayPagoPAParams(this);
		}
	}
	public EPayPagoPAParams(Builder builder) {
		setCodiceFiscale(builder.codiceFiscale);
	}
}
