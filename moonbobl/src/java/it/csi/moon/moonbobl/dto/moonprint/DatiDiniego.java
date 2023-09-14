/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonprint;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatiDiniego {
	
	public String classificazioneDOQUI;
	public String numeroAccertamento;
	public String numProtocolloIngresso;
 	public String numProtocolloUscita;
	public String annoPagamento;
	public String dataScadenza;
	public List<String> motivazioni;
	


	public DatiDiniego() {
		super();
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

	public List<String> getMotivazioni() {
		return motivazioni;
	}

	public void setMotivazioni(List<String> motivazioni) {
		this.motivazioni = motivazioni;
	} 
	
	public String getClassificazioneDOQUI() {
		return classificazioneDOQUI;
	}

	public void setClassificazioneDOQUI(String classificazioneDOQUI) {
		this.classificazioneDOQUI = classificazioneDOQUI;
	}

}
