/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto.regp;

import java.util.ArrayList;
import java.util.List;

public class RicevutaTcrDiniegoEntity {

	private String tipologiaTemplate;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String indirizzo;
	private String citta;
	private String provincia;
	private String cap;
	private String classificazioneDOQUI;
	private String numProtIngr;
	private String numAccertamento;
	private String annoPagamento;
	private String dataScadenza;
	private List<String> motivazioni;
	
	
	public RicevutaTcrDiniegoEntity() {
		super();
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
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCitta() {
		return citta;
	}
	public void setCitta(String citta) {
		this.citta = citta;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getNumProtIngr() {
		return numProtIngr;
	}
	public void setNumProtIngr(String numProtIngr) {
		this.numProtIngr = numProtIngr;
	}
	public String getNumAccertamento() {
		return numAccertamento;
	}
	public void setNumAccertamento(String numAccertamento) {
		this.numAccertamento = numAccertamento;
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


	public String getTipologiaTemplate() {
		return tipologiaTemplate;
	}


	public void setTipologiaTemplate(String tipologiaTemplate) {
		this.tipologiaTemplate = tipologiaTemplate;
	}


	public String getClassificazioneDOQUI() {
		return classificazioneDOQUI;
	}


	public void setClassificazioneDOQUI(String classificazioneDOQUI) {
		this.classificazioneDOQUI = classificazioneDOQUI;
	}

	
}
