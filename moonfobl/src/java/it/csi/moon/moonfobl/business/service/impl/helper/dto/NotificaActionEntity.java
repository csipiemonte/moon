/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper.dto;

import java.util.List;

/**
 * Entity per la creazione della ricevuta di cambio indirizzo
 * <br>
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class NotificaActionEntity {

	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String email;
	private String email_cc;	
	private String testo;
	private List<String> formIoFileNames;
			
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

	public String getEmail_cc() {
		return email_cc;
	}

	public void setEmail_cc(String email_cc) {
		this.email_cc = email_cc;
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

	@Override
	public String toString() {
		return "NotificaEntity [nome=" + nome + ", cognome=" + cognome	+ ", email=" + email  + "]";
	}
		
}
