/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.extra.istat;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class Comune implements Comparable<Comune> {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Integer codice = null;
  private String nome = null;

  public Comune() {
		super();
  }
  public Comune(Integer codice, String nome) {
	super();
	this.codice = codice;
	this.nome = nome;
  }
  
  
  /**
   * il codice identificativo del comune
   **/
  @ApiModelProperty(value = "il codice identificativo del comune")
  @JsonProperty("codice")
  public Integer getCodice() {
    return codice;
  }
  public void setCodice(Integer codice) {
    this.codice = codice;
  }

  /**
   * il nome comune del comune
   **/
  @ApiModelProperty(value = "il nome comune del comune")
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
    Comune comune = (Comune) o;
    return Objects.equals(codice, comune.codice) &&
        Objects.equals(nome, comune.nome);
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
  
	@Override
	public int compareTo(Comune c) {
	    if (getNome() == null || c.getNome() == null) {
	    	return 0;
		}
		return getNome().compareTo(c.getNome());
	}

}

