/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;


/**
 * Error restituita dai servizi REST
 * 
 * @author Francesco
 *
 * @since 1.0.0
 */
public class Error   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
  private String code = null;
  private String messaggioCittadino = null;
  private String errorMessage = null;
  private String fields = null;

  /**
   **/
  
  @ApiModelProperty(value = "")

  // nome originario nello yaml: code 
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")

  // nome originario nello yaml: messaggioCittadino 
  public String getMessaggioCittadino() {
    return messaggioCittadino;
  }
  public void setMessaggioCittadino(String messaggioCittadino) {
    this.messaggioCittadino = messaggioCittadino;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")

  // nome originario nello yaml: errorMessage 
  public String getErrorMessage() {
    return errorMessage;
  }
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")

  // nome originario nello yaml: fields 
  public String getFields() {
    return fields;
  }
  public void setFields(String fields) {
    this.fields = fields;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Error error = (Error) o;
    return Objects.equals(code, error.code) &&
        Objects.equals(messaggioCittadino, error.messaggioCittadino) &&
        Objects.equals(errorMessage, error.errorMessage) &&
        Objects.equals(fields, error.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, messaggioCittadino, errorMessage, fields);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    messaggioCittadino: ").append(toIndentedString(messaggioCittadino)).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
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

