/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.territorio;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Piano toponomastica delle abitazione del comune di Torino
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Piano {
	
	private String codice = null;
	private String nome = null;
	
	public Piano() {
		super();
	}
	public Piano(String codice, String nome) {
		super();
		this.codice = codice;
		this.nome = nome;
	}

	@ApiModelProperty(value = "codice identificativo del piano")
	@JsonProperty("codice") 
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	
	 @ApiModelProperty(value = "descrizione del piano")
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
		Piano other = (Piano) obj;
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
		return "Piano [codice=" + codice + ", nome=" + nome + "]";
	}
	
}
