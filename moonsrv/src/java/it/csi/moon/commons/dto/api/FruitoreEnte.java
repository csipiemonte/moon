/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

public class FruitoreEnte {

	private String codice = null;
	private String nome = null;
	private FruitoreEnte entePadre = null;


	public FruitoreEnte() {

	}

	public FruitoreEnte(String codice, String nome, FruitoreEnte entePadre) {
		super();
		this.codice = codice;
		this.nome = nome;
		this.entePadre = entePadre;
	}


	public String getCodice() {
		return codice;
	}


	public void setCodice(String codice) {
		this.codice = codice;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public FruitoreEnte getEntePadre() {
		return entePadre;
	}


	public void setEntePadre(FruitoreEnte entePadre) {
		this.entePadre = entePadre;
	}


	@Override
	public String toString() {
		return "FruitoreEnte [codice=" + codice + ", nome=" + nome + ", entePadre=" + entePadre + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result + ((entePadre == null) ? 0 : entePadre.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FruitoreEnte other = (FruitoreEnte) obj;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (entePadre == null) {
			if (other.entePadre != null)
				return false;
		} else if (!entePadre.equals(other.entePadre))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}



}
