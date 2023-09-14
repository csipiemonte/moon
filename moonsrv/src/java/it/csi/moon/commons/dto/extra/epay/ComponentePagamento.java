/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.epay;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.csi.moon.commons.BigDecimal2Serializer;

public class ComponentePagamento {

	private int progressivo;
	@JsonSerialize(using = BigDecimal2Serializer.class)
	private BigDecimal importo;
	private String causale;
	private String datiSpecificiRiscossione;//OLD NOT USED da PPAY
	private String annoAccertamento;
	private String numeroAccertamento;
	
	public ComponentePagamento() {
		super();
	}
	
	public ComponentePagamento(int progressivo, BigDecimal importo, String causale, String datiSpecificiRiscossione,
			String annoAccertamento, String numeroAccertamento) {
		super();
		this.progressivo = progressivo;
		this.importo = importo;
		this.causale = causale;
		this.datiSpecificiRiscossione = datiSpecificiRiscossione;
		this.annoAccertamento = annoAccertamento;
		this.numeroAccertamento = numeroAccertamento;
	}
	
	public int getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public String getDatiSpecificiRiscossione() {
		return datiSpecificiRiscossione;
	}
	/**
	 * OLD NOT USED da PPAY
	 * @param datiSpecificiRiscossione
	 */
	public void setDatiSpecificiRiscossione(String datiSpecificiRiscossione) {
		this.datiSpecificiRiscossione = datiSpecificiRiscossione;
	}
	public String getAnnoAccertamento() {
		return annoAccertamento;
	}
	public void setAnnoAccertamento(String annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}
	public String getNumeroAccertamento() {
		return numeroAccertamento;
	}
	public void setNumeroAccertamento(String numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class ComponentePagamento {\n");
	    sb.append("    progressivo: ").append(toIndentedString(progressivo)).append("\n");
	    sb.append("    importo: ").append(toIndentedString(nf(importo))).append("\n");
	    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
	    sb.append("    datiSpecificiRiscossione: ").append(toIndentedString(datiSpecificiRiscossione)).append("\n");
	    sb.append("    annoAccertamento: ").append(toIndentedString(annoAccertamento)).append("\n");
	    sb.append("    numeroAccertamento: ").append(toIndentedString(numeroAccertamento)).append("\n");
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
		return value==null?"null":new DecimalFormat("##0.00").format(value);
	}

}
