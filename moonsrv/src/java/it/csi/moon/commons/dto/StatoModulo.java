/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * Stato di un modulo (moon_io_d_statomodulo)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class StatoModulo   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
  private String codice = null;
  private String descrizione = null;
  private Date dataInizioValidita = null;
  private Date dataFineValidita = null;

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
   * nome dello dello stato
   **/
  @ApiModelProperty(value = "descrizione dello stato del modulo")
  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }
  
  /**
   * data inizio validita dello stato
   **/
  @ApiModelProperty(value = "data inizio validita dello stato del modulo")
  // data inizio validita originario nello yaml: dataInizioValidita 
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
  public Date getDataInizioValidita() {
	return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita) {
	this.dataInizioValidita = dataInizioValidita;
  }
  
  /**
   * data fine validita dello stato
   **/
  @ApiModelProperty(value = "data fine validita dello stato del modulo")
  // data inizio validita originario nello yaml: dataFineValidita 
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
  public Date getDataFineValidita() {
	return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita) {
	this.dataFineValidita = dataFineValidita;
  }
  
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatoModulo s = (StatoModulo) o;
    return Objects.equals(codice, s.codice) &&
    	Objects.equals(descrizione, s.descrizione) &&
    	Objects.equals(dataInizioValidita, s.dataInizioValidita) &&
    	Objects.equals(dataFineValidita, s.dataFineValidita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, dataInizioValidita, dataFineValidita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatoModulo {\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    dataInizioValidita: ").append(toIndentedString(dataInizioValidita)).append("\n");
    sb.append("    dataFineValidita: ").append(toIndentedString(dataFineValidita)).append("\n");
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

