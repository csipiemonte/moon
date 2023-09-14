/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.epay;

public class GetRtResponse {

	private String identificativoPagamento;
	private String codiceEsito;
	private String descrizioneEsito;
	private String descrizioneStatoPagamento;
	private String iuvOriginario;
	private String iuvEffettivo;
	private String ricevutaPdf;
	private String rtXml;
	
	public String getIdentificativoPagamento() {
		return identificativoPagamento;
	}
	public void setIdentificativoPagamento(String identificativoPagamento) {
		this.identificativoPagamento = identificativoPagamento;
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
	public String getDescrizioneStatoPagamento() {
		return descrizioneStatoPagamento;
	}
	public void setDescrizioneStatoPagamento(String descrizioneStatoPagamento) {
		this.descrizioneStatoPagamento = descrizioneStatoPagamento;
	}
	public String getIuvOriginario() {
		return iuvOriginario;
	}
	public void setIuvOriginario(String iuvOriginario) {
		this.iuvOriginario = iuvOriginario;
	}
	public String getIuvEffettivo() {
		return iuvEffettivo;
	}
	public void setIuvEffettivo(String iuvEffettivo) {
		this.iuvEffettivo = iuvEffettivo;
	}
	public String getRicevutaPdf() {
		return ricevutaPdf;
	}
	public void setRicevutaPdf(String ricevutaPdf) {
		this.ricevutaPdf = ricevutaPdf;
	}
	public String getRtXml() {
		return rtXml;
	}
	public void setRtXml(String rtXml) {
		this.rtXml = rtXml;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class GetRtResponse {\n");
	    sb.append("    identificativoPagamento: ").append(toIndentedString(identificativoPagamento)).append("\n");
	    sb.append("    codiceEsito: ").append(toIndentedString(codiceEsito)).append("\n");
	    sb.append("    descrizioneEsito: ").append(toIndentedString(descrizioneEsito)).append("\n");
	    sb.append("    descrizioneStatoPagamento: ").append(toIndentedString(descrizioneStatoPagamento)).append("\n");
	    sb.append("    iuvOriginario: ").append(toIndentedString(iuvOriginario)).append("\n");
	    sb.append("    iuvEffettivo: ").append(toIndentedString(iuvEffettivo)).append("\n");
	    sb.append("    ricevutaPdf: ").append(toIndentedString(ricevutaPdf)).append("\n");
	    sb.append("    rtXml: ").append(toIndentedString(rtXml)).append("\n");
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
