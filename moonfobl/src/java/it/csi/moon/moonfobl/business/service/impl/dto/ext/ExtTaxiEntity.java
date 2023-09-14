/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dto.ext;

import java.util.Date;

public class ExtTaxiEntity {
	private Integer	id;
	private String codice_fiscale;
	private String idfamiglia;
	private String nome;
	private String cognome;
	private String data_nascita;
	private String email;
	private String cellulare;
	private String categoria;
	private String codice_buono;
	private String codice_fiscale_richiedente;
	private Date data_ins;
	private Date data_export;
	
	public ExtTaxiEntity() {
		super();
	}

	public ExtTaxiEntity(int id, String codice_fiscale, String idfamiglia, String nome, String cognome,
			String data_nascita, String email, String cellulare, String categoria, String codice_buono,
			String codice_fiscale_richiedente, Date data_ins, Date data_export) {
		super();
		this.id = id;
		this.codice_fiscale = codice_fiscale;
		this.idfamiglia = idfamiglia;
		this.nome = nome;
		this.cognome = cognome;
		this.data_nascita = data_nascita;
		this.email = email;
		this.cellulare = cellulare;
		this.categoria = categoria;
		this.codice_buono = codice_buono;
		this.codice_fiscale_richiedente = codice_fiscale_richiedente;
		this.data_ins = data_ins;
		this.data_export = data_export;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodice_fiscale() {
		return codice_fiscale;
	}

	public void setCodice_fiscale(String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}

	public String getIdfamiglia() {
		return idfamiglia;
	}

	public void setIdfamiglia(String idfamiglia) {
		this.idfamiglia = idfamiglia;
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

	public String getData_nascita() {
		return data_nascita;
	}

	public void setData_nascita(String data_nascita) {
		this.data_nascita = data_nascita;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCellulare() {
		return cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCodice_buono() {
		return codice_buono;
	}

	public void setCodice_buono(String codice_buono) {
		this.codice_buono = codice_buono;
	}

	public String getCodice_fiscale_richiedente() {
		return codice_fiscale_richiedente;
	}

	public void setCodice_fiscale_richiedente(String codice_fiscale_richiedente) {
		this.codice_fiscale_richiedente = codice_fiscale_richiedente;
	}

	public Date getData_ins() {
		return data_ins;
	}

	public void setData_ins(Date data_ins) {
		this.data_ins = data_ins;
	}

	public Date getData_export() {
		return data_export;
	}

	public void setData_export(Date data_export) {
		this.data_export = data_export;
	}
	
	
}
