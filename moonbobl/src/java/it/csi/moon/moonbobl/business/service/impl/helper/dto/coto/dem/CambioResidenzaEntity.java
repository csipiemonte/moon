/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.dem;

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
public class CambioResidenzaEntity {

	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String richiedente;
	private String indirizzo;
	private int numeroComponenti;
	private String dataPresentazione;
	private List<ComponentiFamigliaEntity> componenti = null;
	private String email;

	
	public CambioResidenzaEntity() {
		super();
		componenti = new ArrayList<ComponentiFamigliaEntity>();
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

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public int getNumeroComponenti() {
		return numeroComponenti;
	}

	public void setNumeroComponenti(int numeroComponenti) {
		this.numeroComponenti = numeroComponenti;
	}

	public String getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(String dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public List<ComponentiFamigliaEntity> getComponenti() {
		return componenti;
	}

	public void setComponenti(List<ComponentiFamigliaEntity> componenti) {
		this.componenti = componenti;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "CambioResidenzaEntity [richiedente=" + richiedente + ", indirizzo=" + indirizzo  + "]";
	}
		
}
