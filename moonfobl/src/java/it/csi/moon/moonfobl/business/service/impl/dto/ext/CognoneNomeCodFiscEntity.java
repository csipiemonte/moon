/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dto.ext;

public class CognoneNomeCodFiscEntity {

	private String cognome;
	private String nome;
	private String codicefiscale;
	
	public CognoneNomeCodFiscEntity() {
		super();
	}
	public CognoneNomeCodFiscEntity(String cognome, String nome, String codicefiscale) {
		super();
		this.cognome = cognome;
		this.nome = nome;
		this.codicefiscale = codicefiscale;
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

	public String getCodicefiscale() {
		return codicefiscale;
	}

	public void setCodicefiscale(String codicefiscale) {
		this.codicefiscale = codicefiscale;
	}

	@Override
	public String toString() {
		return "CognoneNomeCodFiscEntity [cognome=" + cognome + ", nome=" + nome + ", codicefiscale=" + codicefiscale
				+ "]";
	}
	
}
