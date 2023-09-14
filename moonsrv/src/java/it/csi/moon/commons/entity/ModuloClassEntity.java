/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

public class ModuloClassEntity {

	private Long id;
	private Long idModulo;
	private byte[] contenuto;
	private String nomeClass;
	private int tipologia;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public byte[] getContenuto() {
		return contenuto;
	}
	public void setContenuto(byte[] contenuto) {
		this.contenuto = contenuto;
	}
	public String getNomeClass() {
		return nomeClass;
	}
	public void setNomeClass(String nomeClasse) {
		this.nomeClass = nomeClasse;
	}
	public int getTipologia() {
		return tipologia;
	}
	public void setTipologia(int tipologia) {
		this.tipologia = tipologia;
	}
	
}
