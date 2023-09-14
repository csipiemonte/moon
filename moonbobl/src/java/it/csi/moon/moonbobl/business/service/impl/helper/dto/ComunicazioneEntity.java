/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity per la creazione della ricevuta di cambio indirizzo
 * <br>
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class ComunicazioneEntity {

	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String richiedente;
	private String email;

	
	public ComunicazioneEntity() {
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


	public String getRichiedente() {
		return richiedente;
	}


	public void setRichiedente(String richiedente) {
		this.richiedente = richiedente;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return "ComunicazioneEntity [nome=" + nome + ", cognome=" + cognome + ", codiceFiscale=" + codiceFiscale
				+ ", richiedente=" + richiedente + ", email=" + email + "]";
	}


}
