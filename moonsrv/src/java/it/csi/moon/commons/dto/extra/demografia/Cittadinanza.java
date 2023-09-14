/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.demografia;

import java.util.Objects;

/**
 * Cittadinanza (Fonte: Demografia ANPR)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Cittadinanza   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
  private Integer codice = null;
  private String nome = null;
  private String flagUE = null;  // S/N

  public Cittadinanza() {
	  super();
  }
  public Cittadinanza(int codice, String nome, String flagUE) {
	  this.codice = codice;
	  this.nome = nome;
	  this.flagUE = flagUE;
  }

  

/**
   * il codice identificativo della cittadinanza (id_nazione)
   **/
  


  // nome originario nello yaml: codice 
  public Integer getCodice() {
    return codice;
  }
  public void setCodice(Integer codice) {
    this.codice = codice;
  }

  /**
   * il nome della cittadinanza
   **/
  


  // nome originario nello yaml: nome 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * il flag di appartenenza all&#39;UE   S / N
   **/
  


  // nome originario nello yaml: flagUE 
  public String getFlagUE() {
    return flagUE;
  }
  public void setFlagUE(String flagUE) {
    this.flagUE = flagUE;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cittadinanza cittadinanza = (Cittadinanza) o;
    return Objects.equals(codice, cittadinanza.codice) &&
        Objects.equals(nome, cittadinanza.nome) &&
        Objects.equals(flagUE, cittadinanza.flagUE);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, nome, flagUE);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Cittadinanza {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    flagUE: ").append(toIndentedString(flagUE)).append("\n");
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

