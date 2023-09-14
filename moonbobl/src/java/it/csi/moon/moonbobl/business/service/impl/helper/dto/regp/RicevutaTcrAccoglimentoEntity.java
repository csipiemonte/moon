/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto.regp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity per la creazione della ricevuta di cambio indirizzo
 * <br>
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class RicevutaTcrAccoglimentoEntity {

	private String tipologiaTemplate;
	private String nome;
	private String cognome;
	private String cf;
	private String indirizzo;
	private String citta;
	private String provincia;
	private String cap;
	
	private String classificazioneDOQUI;
	private String numAccertamento;
	private String NumProtIngr;
	
	private String dataScadenza;
	
	private List<String> motivazioni;
	
	private String testoLibero;
	private String numeroDetermina;
	private String dataDetermina;
	private String numeroImpegno;
	
	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getClassificazioneDOQUI() {
		return classificazioneDOQUI;
	}

	public void setClassificazioneDOQUI(String classificazione) {
		this.classificazioneDOQUI = classificazione;
	}



	public String getNumAccertamento() {
		return numAccertamento;
	}

	public void setNumAccertamento(String numAccertamento) {
		this.numAccertamento = numAccertamento;
	}

	public String getNumProtIngr() {
		return NumProtIngr;
	}

	public void setNumProtIngr(String numProtIngr) {
		NumProtIngr = numProtIngr;
	}



	private String annoPagamento;
    private String scadenza;
    
	/*private String operatore;
	private String funzionario;
	private String numeroPraticaNao;
	private String dataRegistrazione;
	private String telefono;
	private String fax;*/
	

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

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
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

	public String getAnnoPagamento() {
		return annoPagamento;
	}

	public void setAnnoPagamento(String annoPagamento) {
		this.annoPagamento = annoPagamento;
	}

	public String getScadenza() {
		return scadenza;
	}

	public void setScadenza(String scadenza) {
		this.scadenza = scadenza;
	}

	public List<String> getMotivazioni() {
		return motivazioni;
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

	public void setMotivazioni(List<String> motivazioni) {
		this.motivazioni = motivazioni;
	}

	public String getTipologiaTemplate() {
		return tipologiaTemplate;
	}

	public void setTipologiaTemplate(String tipologiaTemplate) {
		this.tipologiaTemplate = tipologiaTemplate;
	}
	
	/*@Override
	public String toString() {
		return "RicevutaCambioResidEntity [richiedente=" + richiedente + ", indirizzo=" + indirizzo
				+ ", numeroComponenti=" + numeroComponenti + ", dataPresentazione=" + dataPresentazione + ", operatore="
				+ operatore + ", funzionario=" + funzionario + ", numeroPraticaNao=" + numeroPraticaNao
				+ ", dataRegistrazione=" + dataRegistrazione + ", telefono=" + telefono + ", fax=" + fax + "]";*/
		
}
