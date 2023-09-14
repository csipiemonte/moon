/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * Tipo utente (moon_fo_d_tipo_utente)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class TipoUtente   {
  
  private String codice = null;
  private String descrizione = null;

  /**
   * codice dello del tipo utente
   **/
  @ApiModelProperty(value = "codice del tipo utente")
  // nome originario nello yaml: name 
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }


  /**
   * nome dello dello stato
   **/
  @ApiModelProperty(value = "descrizione del tipo utente")
  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoUtente t = (TipoUtente) o;
    return Objects.equals(codice, t.descrizione) &&
    	Objects.equals(descrizione, t.descrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoUtente {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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

