/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.extra.territorio;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class PianoNUI {
	
	private Integer codice = null;
	private String nome = null;
	
	
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
	
	
}
