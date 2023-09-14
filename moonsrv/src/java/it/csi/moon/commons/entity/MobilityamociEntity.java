/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

public class MobilityamociEntity {
	private Integer matricola;
	private String	cognome;
	private String	nome;
	private String	codice_fiscale;
	private String	cod_strut;
	private String	categoria;
	private String	comune_nascita;
	private String	provincia_nascita;
	private String	comune_residenza;
	private String	provincia_residenza;
	private String	indirizzo_residenza;
	private String	email;
	private String	data_nascita;
	private String	data_inizio;
	private String	data_fine;
	private String	desc_abbonamento_anno_precedente;
	private String	abbonamento_da;
	private String	abbonamento_a;
	private Integer	num_tessera_anno_precedente;
	
	
	
	public MobilityamociEntity() {
		super();
	}

	public MobilityamociEntity(Integer matricola, String cognome, String nome, String codice_fiscale, String cod_strut,
			String categoria, String comune_nascita, String provincia_nascita, String comune_residenza,
			String provincia_residenza, String indirizzo_residenza, String email, String data_nascita,
			String data_inizio, String data_fine, String desc_abbonamento_anno_precedente, String abbonamento_da,
			String abbonamento_a, Integer num_tessera_anno_precedente) {
		super();
		this.matricola = matricola;
		this.cognome = cognome;
		this.nome = nome;
		this.codice_fiscale = codice_fiscale;
		this.cod_strut = cod_strut;
		this.categoria = categoria;
		this.comune_nascita = comune_nascita;
		this.provincia_nascita = provincia_nascita;
		this.comune_residenza = comune_residenza;
		this.provincia_residenza = provincia_residenza;
		this.indirizzo_residenza = indirizzo_residenza;
		this.email = email;
		this.data_nascita = data_nascita;
		this.data_inizio = data_inizio;
		this.data_fine = data_fine;
		this.desc_abbonamento_anno_precedente = desc_abbonamento_anno_precedente;
		this.abbonamento_da = abbonamento_da;
		this.abbonamento_a = abbonamento_a;
		this.num_tessera_anno_precedente = num_tessera_anno_precedente;
	}

	public Integer getMatricola() {
		return matricola;
	}

	public void setMatricola(Integer matricola) {
		this.matricola = matricola;
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

	public String getCodice_fiscale() {
		return codice_fiscale;
	}

	public void setCodice_fiscale(String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}

	public String getCod_strut() {
		return cod_strut;
	}

	public void setCod_strut(String cod_strut) {
		this.cod_strut = cod_strut;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getComune_nascita() {
		return comune_nascita;
	}

	public void setComune_nascita(String comune_nascita) {
		this.comune_nascita = comune_nascita;
	}

	public String getProvincia_nascita() {
		return provincia_nascita;
	}

	public void setProvincia_nascita(String provincia_nascita) {
		this.provincia_nascita = provincia_nascita;
	}

	public String getComune_residenza() {
		return comune_residenza;
	}

	public void setComune_residenza(String comune_residenza) {
		this.comune_residenza = comune_residenza;
	}

	public String getProvincia_residenza() {
		return provincia_residenza;
	}

	public void setProvincia_residenza(String provincia_residenza) {
		this.provincia_residenza = provincia_residenza;
	}

	public String getIndirizzo_residenza() {
		return indirizzo_residenza;
	}

	public void setIndirizzo_residenza(String indirizzo_residenza) {
		this.indirizzo_residenza = indirizzo_residenza;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getData_nascita() {
		return data_nascita;
	}

	public void setData_nascita(String data_nascita) {
		this.data_nascita = data_nascita;
	}

	public String getData_inizio() {
		return data_inizio;
	}

	public void setData_inizio(String data_inizio) {
		this.data_inizio = data_inizio;
	}

	public String getData_fine() {
		return data_fine;
	}

	public void setData_fine(String data_fine) {
		this.data_fine = data_fine;
	}

	public String getDesc_abbonamento_anno_precedente() {
		return desc_abbonamento_anno_precedente;
	}

	public void setDesc_abbonamento_anno_precedente(String desc_abbonamento_anno_precedente) {
		this.desc_abbonamento_anno_precedente = desc_abbonamento_anno_precedente;
	}

	public String getAbbonamento_da() {
		return abbonamento_da;
	}

	public void setAbbonamento_da(String abbonamento_da) {
		this.abbonamento_da = abbonamento_da;
	}

	public String getAbbonamento_a() {
		return abbonamento_a;
	}

	public void setAbbonamento_a(String abbonamento_a) {
		this.abbonamento_a = abbonamento_a;
	}

	public Integer getNum_tessera_anno_precedente() {
		return num_tessera_anno_precedente;
	}

	public void setNum_tessera_anno_precedente(Integer num_tessera_anno_precedente) {
		this.num_tessera_anno_precedente = num_tessera_anno_precedente;
	}
	
	
}
