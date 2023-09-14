/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

public class CreaIuvResponse {

	private Long idRichiesta = null;
	private String codiceAvviso = null;
	private String iuv = null;
	private String urlRedirect;
	
	public CreaIuvResponse() {
		super();
	}
	public CreaIuvResponse(Long idRichiesta, String codiceAvviso, String iuv, String urlRedirect) {
		super();
		this.idRichiesta = idRichiesta;
		this.codiceAvviso = codiceAvviso;
		this.iuv = iuv;
		this.urlRedirect = urlRedirect;
	}
	
	public Long getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public String getCodiceAvviso() {
		return codiceAvviso;
	}
	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getUrlRedirect() {
		return urlRedirect;
	}
	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CreaIuvResponse {\n");
		sb.append("    idRichiesta: ").append(toIndentedString(idRichiesta)).append("\n");
		sb.append("    codiceAvviso: ").append(toIndentedString(codiceAvviso)).append("\n");
		sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
		sb.append("    urlRedirect: ").append(toIndentedString(urlRedirect)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
