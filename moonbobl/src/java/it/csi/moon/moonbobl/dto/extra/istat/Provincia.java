/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.extra.istat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class Provincia implements Comparable<Provincia> {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Integer codice = null;
  private String nome = null;
  private String sigla = null;
  private List<Comune> comuni = new ArrayList<Comune>();

  public Provincia() {
		super();
  }
  public Provincia(Integer codice, String nome, String sigla) {
	super();
	this.codice = codice;
	this.nome = nome;
  }
  
  /**
   * il codice identificativo della provincia
   **/
  @ApiModelProperty(value = "il codice identificativo della provincia")
  @JsonProperty("codice") 
  public Integer getCodice() {
    return codice;
  }
  public void setCodice(Integer codice) {
    this.codice = codice;
  }

  /**
   * il nome comune della provincia
   **/
  @ApiModelProperty(value = "il nome della provincia")
  @JsonProperty("nome") 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * il nome comune della provincia
   **/
  @ApiModelProperty(value = "la sigla della provincia")
  @JsonProperty("sigla") 
  public String getSigla() {
    return sigla;
  }
  public void setSigla(String sigla) {
    this.sigla = sigla;
  }
  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("comuni") 
  public List<Comune> getComuni() {
    return comuni;
  }
  public void setComuni(List<Comune> comuni) {
    this.comuni = comuni;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Provincia provincia = (Provincia) o;
    return Objects.equals(codice, provincia.codice) &&
        Objects.equals(nome, provincia.nome) &&
        Objects.equals(sigla, provincia.sigla) &&
        Objects.equals(comuni, provincia.comuni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, nome, sigla, comuni);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Provincia {\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    sigla: ").append(toIndentedString(sigla)).append("\n");
    sb.append("    comuni: ").append(toIndentedString(comuni)).append("\n");
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
	public int compareTo(Provincia p) {
	    if (getNome() == null || p.getNome() == null) {
	    	return 0;
		}
		return getNome().compareTo(p.getNome());
	}

}

