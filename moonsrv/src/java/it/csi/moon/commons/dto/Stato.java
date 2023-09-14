/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * Stato di un istanza (moon_wf_d_stato)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Stato   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
  private Integer idStato = null;
  private String codice = null;
  private String nome = null;
  private String descrizione = null;
  private Integer idTabFo = null;

  /**
   * l&#39;identificativo dello stato
   **/
  
  @ApiModelProperty(value = "identificativo dello stato")


  // nome originario nello yaml: idStato 
  public Integer getIdStato() {
    return idStato;
  }
  public void setIdStato(Integer idStato) {
    this.idStato = idStato;
  }

  /**
   * codice dello stato
   **/
  
  @ApiModelProperty(value = "codice dello stato")

  // nome originario nello yaml: codice 
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }
  
  /**
   * nome dello stato
   **/
  
  @ApiModelProperty(value = "nome dello stato")

  // nome originario nello yaml: name 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }


  /**
   * descrizione dello stato
   **/
  
  @ApiModelProperty(value = "descrizione dello dello stato")

  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * IdTabFo dello stato
   **/
  
  @ApiModelProperty(value = "idTabFo")

  // nome originario nello yaml: idTabFo 
  public Integer getIdTabFo() {
    return idTabFo;
  }
  public void setIdTabFo(Integer idTabFo) {
    this.idTabFo = idTabFo;
  }
  
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Stato s = (Stato) o;
    return Objects.equals(idStato, s.idStato) &&
        Objects.equals(codice, s.codice) &&
    	Objects.equals(descrizione, s.descrizione) &&
        Objects.equals(nome, s.nome) &&
        Objects.equals(idTabFo, s.idTabFo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idStato, codice, nome, descrizione, idTabFo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Stato {\n");
    
    sb.append("    idStato: ").append(toIndentedString(idStato)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    idTabFo: ").append(toIndentedString(idTabFo)).append("\n");
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

