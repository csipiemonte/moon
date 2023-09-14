/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.istat;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class FormeGiuridica   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Integer codice = null;
  private String nome = null;

  public FormeGiuridica() {
		super();
  }
  public FormeGiuridica(Integer codice, String nome) {
	super();
	this.codice = codice;
	this.nome = nome;
  }
  
  
  /**
   * il codice identificativo delle forme giuridiche
   **/
  @ApiModelProperty(value = "il codice identificativo delle forme giuridiche")
  @JsonProperty("codice")
  public Integer getCodice() {
    return codice;
  }
  public void setCodice(Integer codice) {
    this.codice = codice;
  }

  /**
   * il nome comune delle forme giuridiche
   **/
  @ApiModelProperty(value = "il nome delle forme giuridiche")
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
    FormeGiuridica obj = (FormeGiuridica) o;
    return Objects.equals(codice, obj.codice) &&
        Objects.equals(nome, obj.nome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, nome);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FormeGiuridica {\n");
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

