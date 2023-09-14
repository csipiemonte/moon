/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dto;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Parametri payload per la richiesta di inizializzazione di richiesta integrazione
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class IntegrazioneInitParams  {

	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String testo;
	private String email;
	
	private JsonNode allegati;
	private JsonNode emailCc;

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public JsonNode getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(JsonNode emailCc) {
		this.emailCc = emailCc;
	}

	public JsonNode getAllegati() {
		return allegati;
	}

	public void setAllegati(JsonNode allegati) {
		this.allegati = allegati;
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


}

