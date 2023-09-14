/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dto;

public class FineConcEntity {
	private String  ragione_sociale;
	private String	cognome;
	private String	nome;
	private String	codice_fiscale_titolare;
	private String	codice_fiscale_abilitato;
	private String	email;
	private String	telefono;
	private String	codice_utenza;
	private String	scadenza_concessione;

	public FineConcEntity() {
		super();
	}

	public FineConcEntity(String ragione_sociale, String cognome, String nome, String codice_fiscale_titolare, String codice_fiscale_abilitato,
			 String email, String telefono, String codice_utenza, String scadenza_concessione) {
		super(); 
		this.ragione_sociale = ragione_sociale;
		this.cognome = cognome;
		this.nome = nome;
		this.codice_fiscale_titolare = codice_fiscale_titolare;
		this.codice_fiscale_abilitato = codice_fiscale_abilitato;
		this.email = email;
		this.telefono = telefono;
		this.codice_utenza = codice_utenza;
		this.scadenza_concessione = scadenza_concessione;
	}

	public String getRagione_sociale() {
		return ragione_sociale;
	}

	public void setRagione_sociale(String ragione_sociale) {
		this.ragione_sociale = ragione_sociale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodice_fiscale_titolare() {
		return codice_fiscale_titolare;
	}

	public void setCodice_fiscale_titolare(String codice_fiscale_titolare) {
		this.codice_fiscale_titolare = codice_fiscale_titolare;
	}

	public String getCodice_fiscale_abilitato() {
		return codice_fiscale_abilitato;
	}

	public void setCodice_fiscale_abilitato(String codice_fiscale_abilitato) {
		this.codice_fiscale_abilitato = codice_fiscale_abilitato;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}	
	
	public String getCodice_utenza() {
		return codice_utenza;
	}

	public void setCodice_utenza(String codice_utenza) {
		this.codice_utenza = codice_utenza;
	}

	public String getScadenza_concessione() {
		return scadenza_concessione;
	}

	public void setScadenza_concessione(String scadenza_concessione) {
		this.scadenza_concessione = scadenza_concessione;
	}
}
