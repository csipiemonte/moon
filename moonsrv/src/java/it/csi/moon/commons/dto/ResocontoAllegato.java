/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

public class ResocontoAllegato {
	private String nomeAllegato;
	private String hashAllegato;
	
	
	public String getNomeAllegato() {
		return nomeAllegato;
	}
	
	public void setNomeAllegato(String nomeAllegato) {
		this.nomeAllegato = nomeAllegato;
	}
	
	public String getHashAllegato() {
		return hashAllegato;
	}
	
	public void setHashAllegato(String hashAllegato) {
		this.hashAllegato = hashAllegato;
	}
	
	public ResocontoAllegato(String nomeAllegato, String hashAllegato) {
		super();
		this.nomeAllegato = nomeAllegato;
		this.hashAllegato = hashAllegato;
	}
	
	public ResocontoAllegato() {
		super();
	}
	

}
