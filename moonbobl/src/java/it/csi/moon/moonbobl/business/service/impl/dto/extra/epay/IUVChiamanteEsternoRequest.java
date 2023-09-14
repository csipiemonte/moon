/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto.extra.epay;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import it.csi.moon.moonbobl.business.BigDecimal2Serializer;



public class IUVChiamanteEsternoRequest {

	private String codiceFiscaleEnte;
	private String causale;
	private String tipoPagamento;
	@JsonSerialize(using = BigDecimal2Serializer.class)
	private BigDecimal importo;
	private String nome;
	private String cognome;
	private String ragioneSociale;
	private String email;
	private String codiceFiscalePartitaIVAPagatore;
	private String identificativoPagamento;
	private List<ComponentePagamento> componentiPagamento;

	public String getCodiceFiscaleEnte() {
		return codiceFiscaleEnte;
	}
	public void setCodiceFiscaleEnte(String codiceFiscaleEnte) {
		this.codiceFiscaleEnte = codiceFiscaleEnte;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public String getTipoPagamento() {
		return tipoPagamento;
	}
	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCodiceFiscalePartitaIVAPagatore() {
		return codiceFiscalePartitaIVAPagatore;
	}
	public void setCodiceFiscalePartitaIVAPagatore(String codiceFiscalePartitaIVAPagatore) {
		this.codiceFiscalePartitaIVAPagatore = codiceFiscalePartitaIVAPagatore;
	}
	public String getIdentificativoPagamento() {
		return identificativoPagamento;
	}
	public void setIdentificativoPagamento(String identificativoPagamento) {
		this.identificativoPagamento = identificativoPagamento;
	}
	public List<ComponentePagamento> getComponentiPagamento() {
		return componentiPagamento;
	}
	public void setComponentiPagamento(List<ComponentePagamento> componentiPagamento) {
		this.componentiPagamento = componentiPagamento;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class IUVChiamanteEsternoRequest {\n");
	    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
	    sb.append("    codiceFiscaleEnte: ").append(toIndentedString(codiceFiscaleEnte)).append("\n");
	    sb.append("    tipoPagamento: ").append(toIndentedString(tipoPagamento)).append("\n");
	    sb.append("    importo: ").append(toIndentedString(nf(importo))).append("\n");
	    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
	    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
	    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
	    sb.append("    email: ").append(toIndentedString(email)).append("\n");
	    sb.append("    codiceFiscalePartitaIVAPagatore: ").append(toIndentedString(codiceFiscalePartitaIVAPagatore)).append("\n");
	    sb.append("    identificativoPagamento: ").append(toIndentedString(identificativoPagamento)).append("\n");
	    sb.append("    componentiPagamento: ").append(toIndentedString(componentiPagamento)).append("\n");
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
	
	private String nf(BigDecimal value) {
		return value==null?"null":new DecimalFormat(".##").format(value);
	}
	
}
