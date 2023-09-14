/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper.dto;

import java.util.List;

/**
 * Entity per l'azione di integrazione
 * <br>
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class IntegrazioneActionEntity {

	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String email;
	private List<String> emailCc;	
	private String testo;
	private List<String> formIoFileNames;
	private List<String> integrazioneFormIoFileNames;
			
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public List<String> getFormIoFileNames() {
		return formIoFileNames;
	}

	public void setFormIoFileNames(List<String> formIoFileNames) {
		this.formIoFileNames = formIoFileNames;
	}
	
	public List<String> getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(List<String> emailCc) {
		this.emailCc = emailCc;
	}

	public List<String> getIntegrazioneFormIoFileNames() {
		return integrazioneFormIoFileNames;
	}

	public void setIntegrazioneFormIoFileNames(List<String> integrazioneFormIoFileNames) {
		this.integrazioneFormIoFileNames = integrazioneFormIoFileNames;
	}

	@Override
	public String toString() {
		return "IntegrazioneActionEntity [nome=" + nome + ", cognome=" + cognome + ", codiceFiscale=" + codiceFiscale
				+ ", email=" + email + ", emailCc=" + emailCc + ", testo=" + testo + ", formIoFileNames="
				+ formIoFileNames + ", integrazioneFormIoFileNames=" + integrazioneFormIoFileNames + "]";
	}
		
}
