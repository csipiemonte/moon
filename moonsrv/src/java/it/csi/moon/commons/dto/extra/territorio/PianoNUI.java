/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.territorio;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * IUI : Piano + NUI  della toponomastica del comune di Torino
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class PianoNUI {
	
	private Integer codice = null;
	private String nome = null;
	
	public PianoNUI() {
		super();
	}
	public PianoNUI(Integer codice, String nome) {
		super();
		this.codice = codice;
		this.nome = nome;
	}

	@ApiModelProperty(value = "codice identificativo del UIU (pianoNUI)")
	@JsonProperty("codice") 
	public Integer getCodice() {
		return codice;
	}
	public void setCodice(Integer codice) {
		this.codice = codice;
	}
	
	 @ApiModelProperty(value = "nome del UIU (pianoNUI)")
	 @JsonProperty("nome") 
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
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
		PianoNUI other = (PianoNUI) obj;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PianoNUI [codice=" + codice + ", nome=" + nome + "]";
	}
	
}
