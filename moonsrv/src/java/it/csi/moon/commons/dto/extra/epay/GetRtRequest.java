/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.epay;

public class GetRtRequest {

	private String iuv;
	private String codiceFiscale;
	private String identificativoPagamento;
	private String codiceFiscaleEnte;
	private String formatoRT;
	
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getIdentificativoPagamento() {
		return identificativoPagamento;
	}
	public void setIdentificativoPagamento(String identificativoPagamento) {
		this.identificativoPagamento = identificativoPagamento;
	}
	public String getCodiceFiscaleEnte() {
		return codiceFiscaleEnte;
	}
	public void setCodiceFiscaleEnte(String codiceFiscaleEnte) {
		this.codiceFiscaleEnte = codiceFiscaleEnte;
	}
	public String getFormatoRT() {
		return formatoRT;
	}
	public void setFormatoRT(String formatoRT) {
		this.formatoRT = formatoRT;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class GetRtRequest {\n");
	    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
	    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
	    sb.append("    identificativoPagamento: ").append(toIndentedString(identificativoPagamento)).append("\n");
	    sb.append("    codiceFiscaleEnte: ").append(toIndentedString(codiceFiscaleEnte)).append("\n");
	    sb.append("    formatoRT: ").append(toIndentedString(formatoRT)).append("\n");
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
