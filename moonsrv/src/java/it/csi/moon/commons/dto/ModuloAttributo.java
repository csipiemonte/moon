/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

/**
 * Modulo attribut ( moon_io_d_moduloattributi )
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloAttributo {

	private String nome;
	private String valore;

	public ModuloAttributo() {
		super();
	}

	public ModuloAttributo(String nome, String valore) {
		super();
		this.nome = nome;
		this.valore = valore;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getValore() {
		return valore;
	}
	public void setValore(String valore) {
		this.valore = valore;
	}

	@Override
	public String toString() {
		return "ModuloAttributo [nome=" + nome + ", valore=" + valore + "]";
	}
}
