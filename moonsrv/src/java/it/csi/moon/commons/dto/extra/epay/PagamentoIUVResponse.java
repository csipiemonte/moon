/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.epay;

public class PagamentoIUVResponse {

	String identificativoPagamento;
	String iuv;
	String codiceAvviso;
	String codiceEsito;
	String descrizioneEsito;
	String urlWisp;
	
	public String getIdentificativoPagamento() {
		return identificativoPagamento;
	}
	public void setIdentificativoPagamento(String identificativoPagamento) {
		this.identificativoPagamento = identificativoPagamento;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCodiceAvviso() {
		return codiceAvviso;
	}
	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}
	public String getCodiceEsito() {
		return codiceEsito;
	}
	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}
	public String getDescrizioneEsito() {
		return descrizioneEsito;
	}
	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}
	public String getUrlWisp() {
		return urlWisp;
	}
	public void setUrlWisp(String urlWisp) {
		this.urlWisp = urlWisp;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class PagamentoIUVResponse {\n");
	    
	    sb.append("    identificativoPagamento: ").append(toIndentedString(identificativoPagamento)).append("\n");
	    sb.append("    urlWisp: ").append(toIndentedString(urlWisp)).append("\n");
	    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
	    sb.append("    codiceAvviso: ").append(toIndentedString(codiceAvviso)).append("\n");
	    sb.append("    codiceEsito: ").append(toIndentedString(codiceEsito)).append("\n");
	    sb.append("    descrizioneEsito: ").append(toIndentedString(descrizioneEsito)).append("\n");
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
