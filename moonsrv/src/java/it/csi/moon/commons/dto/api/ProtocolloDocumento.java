/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Protocollo in ingresso legato ad un documento")@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyEapServerCodegen")
public class ProtocolloDocumento   {
  

  private String idDocumento;

  private String numeroProtocollo;

  private Date dataProtocollo;

  /**
   * Identificativo univoco del documento
   **/
  
  @ApiModelProperty(value = "Identificativo univoco del documento")
  @JsonProperty("idDocumento")
  public String getIdDocumento() {
    return idDocumento;
  }
  public void setIdDocumento(String idDocumento) {
    this.idDocumento = idDocumento;
  }

  /**
   * Numero di protocollo del documento
   **/
  
  @ApiModelProperty(value = "Numero di protocollo del documento")
  @JsonProperty("numeroProtocollo")
  public String getNumeroProtocollo() {
    return numeroProtocollo;
  }
  public void setNumeroProtocollo(String numeroProtocollo) {
    this.numeroProtocollo = numeroProtocollo;
  }

  /**
   * Data di protocollo dell&#39;istanza (2022-10-29 00:00:00)
   **/
  
  @ApiModelProperty(value = "Data di protocollo dell'istanza (2022-10-29 00:00:00)")
  @JsonProperty("dataProtocollo")
  public Date getDataProtocollo() {
    return dataProtocollo;
  }
  public void setDataProtocollo(Date dataProtocollo) {
    this.dataProtocollo = dataProtocollo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProtocolloDocumento protocolloDocumento = (ProtocolloDocumento) o;
    return Objects.equals(idDocumento, protocolloDocumento.idDocumento) &&
        Objects.equals(numeroProtocollo, protocolloDocumento.numeroProtocollo) &&
        Objects.equals(dataProtocollo, protocolloDocumento.dataProtocollo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDocumento, numeroProtocollo, dataProtocollo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProtocolloDocumento {\n");
    
    sb.append("    idDocumento: ").append(toIndentedString(idDocumento)).append("\n");
    sb.append("    numeroProtocollo: ").append(toIndentedString(numeroProtocollo)).append("\n");
    sb.append("    dataProtocollo: ").append(toIndentedString(dataProtocollo)).append("\n");
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

