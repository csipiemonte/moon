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

public class Regione implements Comparable<Regione> {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Integer codice = null;
  private String nome = null;
  private List<Provincia> province = new ArrayList<Provincia>();

  public Regione() {
		super();
  }
  public Regione(Integer codice, String nome) {
	super();
	this.codice = codice;
	this.nome = nome;
  }
  
  /**
   * il codice identificativo della regione
   **/
  @ApiModelProperty(value = "il codice identificativo della regione")
  @JsonProperty("codice") 
  public Integer getCodice() {
    return codice;
  }
  public void setCodice(Integer codice) {
    this.codice = codice;
  }

  /**
   * il nome comune della regione
   **/
  @ApiModelProperty(value = "il nome comune della regione")
  @JsonProperty("nome") 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("province") 
  public List<Provincia> getProvince() {
    return province;
  }
  public void setProvince(List<Provincia> province) {
    this.province = province;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Regione regione = (Regione) o;
    return Objects.equals(codice, regione.codice) &&
        Objects.equals(nome, regione.nome) &&
        Objects.equals(province, regione.province);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, nome, province);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Regione {\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    province: ").append(toIndentedString(province)).append("\n");
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
	public int compareTo(Regione r) {
	    if (getNome() == null || r.getNome() == null) {
	    	return 0;
		}
		return getNome().compareTo(r.getNome());
	}
}

