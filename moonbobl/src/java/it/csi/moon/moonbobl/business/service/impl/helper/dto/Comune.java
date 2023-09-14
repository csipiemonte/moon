/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class Comune {
	private Integer codice;
	private String nome;
	

	@ApiModelProperty(value = "codice del comune")
	@JsonProperty("codice") 	
	public Integer getCodice() {
		return codice;
	}
	public void setCodice(Integer codice) {
		this.codice = codice;
	}
	
	@ApiModelProperty(value = "nome del comune")
	@JsonProperty("nome") 	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	 @Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Comune oggetto = (Comune) o;
	    return Objects.equals(codice, oggetto.codice) &&
	        Objects.equals(nome, oggetto.nome);

	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(codice, nome);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Comune {\n");
	    
	    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
	    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }

	  /**
	   * Convert the given object to string with each line indented by 4 spaces
	   * (except the first line).
	   */
	  private String toIndentedString(Object o) {
	    if (o == null) {
	      return "null";
	    }
	    return o.toString().replace("\n", "\n    ");
	  }
}
