/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.extra.territorio;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class Civico implements Comparable<Civico> {
	
	private Integer codice = null;
	private String nome = null;
	private String flagUiu = null; // uiuCompletate
	
	public Civico() {
		super();
	}
	public Civico(Integer codice, String nome, String flagUiu) {
		super();
		this.codice = codice;
		this.nome = nome;
		this.flagUiu = flagUiu;
	}

	@ApiModelProperty(value = "codice identificativo del civico")
	@JsonProperty("codice") 
	public Integer getCodice() {
		return codice;
	}
	public void setCodice(Integer codice) {
		this.codice = codice;
	}
	
	@ApiModelProperty(value = "nome del civico")
	@JsonProperty("nome") 
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@ApiModelProperty(value = "flag di UIU completata")
	@JsonProperty("flagUiu") 
	public String getFlagUiu() {
		return flagUiu;
	}
	public void setFlagUiu(String flagUiu) {
		this.flagUiu = flagUiu;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((flagUiu == null) ? 0 : flagUiu.hashCode());
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
		Civico other = (Civico) obj;
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
		if (flagUiu == null) {
			if (other.flagUiu != null)
				return false;
		} else if (!flagUiu.equals(other.flagUiu))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Civico [codice=" + codice + ", nome=" + nome + ", flagUiu=" + flagUiu + "]";
	}
	
	@Override
	public int compareTo(Civico c) {
        return this.getNome().compareTo(c.getNome());
    }
	
}
