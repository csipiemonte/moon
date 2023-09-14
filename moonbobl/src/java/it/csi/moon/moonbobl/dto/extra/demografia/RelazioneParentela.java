/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.extra.demografia;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * RelazioneParentela (Fonte: Demografia ANPR)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class RelazioneParentela   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  private Integer codice = null;
  private String nome = null;

	public RelazioneParentela() {
		super();
	}
	public RelazioneParentela(Integer codice, String nome) {
		super();
		this.codice = codice;
		this.nome = nome;
	}
	
  /**
   * il codice identificativo della relazione di parentela
   **/
  @ApiModelProperty(value = "il codice identificativo della relazione di parentela (id_tipo_rels_parentela)")
  @JsonProperty("codice") 
  public Integer getCodice() {
    return codice;
  }
  public void setCodice(Integer codice) {
    this.codice = codice;
  }

  /**
   * il nome della cittadinanza
   **/
  @ApiModelProperty(value = "il nome della relazione di parentela")
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
    RelazioneParentela citt = (RelazioneParentela) o;
    return Objects.equals(codice, citt.codice) &&
        Objects.equals(nome, citt.nome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, nome);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelazioneParentela {\n");
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

