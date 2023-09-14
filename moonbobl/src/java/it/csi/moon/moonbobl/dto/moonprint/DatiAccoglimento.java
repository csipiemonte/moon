/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatiAccoglimento {
	public String numeroAccertamento;
	public String numProtocolloIngresso;
 	public String numProtocolloUscita;
	public String annoPagamento;
	public String dataScadenza;
	public String classificazioneDOQUI;
	
	public String testoLibero;
    public String numeroDetermina;
    public String dataDetermina;
    public String numeroImpegno;
    
	public List<String> motivi;
	
	
	public String getClassificazioneDOQUI() {
		return classificazioneDOQUI;
	}
	public void setClassificazioneDOQUI(String classificazioneDOQUI) {
		this.classificazioneDOQUI = classificazioneDOQUI;
	}
	public String getNumeroAccertamento() {
		return numeroAccertamento;
	}
	public void setNumeroAccertamento(String numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	public String getNumProtocolloIngresso() {
		return numProtocolloIngresso;
	}
	public void setNumProtocolloIngresso(String numProtocolloIngresso) {
		this.numProtocolloIngresso = numProtocolloIngresso;
	}
	public String getAnnoPagamento() {
		return annoPagamento;
	}
	public void setAnnoPagamento(String annoPagamento) {
		this.annoPagamento = annoPagamento;
	}
	public String getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public String getTestoLibero() {
		return testoLibero;
	}
	public void setTestoLibero(String testoLibero) {
		this.testoLibero = testoLibero;
	}
	public String getNumeroDetermina() {
		return numeroDetermina;
	}
	public void setNumeroDetermina(String numeroDetermina) {
		this.numeroDetermina = numeroDetermina;
	}
	public String getDataDetermina() {
		return dataDetermina;
	}
	public void setDataDetermina(String dataDetermina) {
		this.dataDetermina = dataDetermina;
	}
	public String getNumeroImpegno() {
		return numeroImpegno;
	}
	public void setNumeroImpegno(String numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}
}
