/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

public class ModuloClass {
	private Long idModulo;
	private int tipologia;
	private String nomeClasse;
	
	public ModuloClass() {
		this.idModulo = null;
		this.tipologia = 0;
		this.nomeClasse = null;
	}

	public ModuloClass(Long idModulo, int tipologia, String nomeClasse) {
		this.idModulo = idModulo;
		this.tipologia = tipologia;
		this.nomeClasse = nomeClasse;
	}
	
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public int getTipologia() {
		return tipologia;
	}
	public void setTipologia(int tipologia) {
		this.tipologia = tipologia;
	}
	public String getNomeClasse() {
		return nomeClasse;
	}
	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}
	
	
	
}
