/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyEapServerCodegen")
public class PostAzioneSendMailAllOf   {
  

  private String to;

  private String cc;

  private String bcc;

  private String subject;

  private String text;

  private PostAzioneSendMailAllOfAttachments attachments;

  /**
   * Indirizzo email del destinatario dell&#39;email&#39;
   **/
  
  @ApiModelProperty(example = "nome.cognome@csi.it", value = "Indirizzo email del destinatario dell'email'")
  @JsonProperty("to")
  public String getTo() {
    return to;
  }
  public void setTo(String to) {
    this.to = to;
  }

  /**
   * Indirizzo email del destinatario in copia dell&#39;email&#39;
   **/
  
  @ApiModelProperty(example = "nome.cognome@csi.it", value = "Indirizzo email del destinatario in copia dell'email'")
  @JsonProperty("cc")
  public String getCc() {
    return cc;
  }
  public void setCc(String cc) {
    this.cc = cc;
  }

  /**
   * Indirizzo email del destinatario in copia nascosta dell&#39;email&#39;
   **/
  
  @ApiModelProperty(example = "nome.cognome@csi.it", value = "Indirizzo email del destinatario in copia nascosta dell'email'")
  @JsonProperty("bcc")
  public String getBcc() {
    return bcc;
  }
  public void setBcc(String bcc) {
    this.bcc = bcc;
  }

  /**
   * Subject dell&#39;email
   **/
  
  @ApiModelProperty(example = "Richiesta di completamento della sua pratica", value = "Subject dell'email")
  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Indirizzo email del destinatario dell&#39;email&#39;
   **/
  
  @ApiModelProperty(example = "Si richiede di compilare il modulo per completare la sua pratica legati all'istanza da lei presentata. Si allegata sia il Nuovo Modulo da compilare che la Notice di compilazione.", value = "Indirizzo email del destinatario dell'email'")
  @JsonProperty("text")
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("attachments")
  public PostAzioneSendMailAllOfAttachments getAttachments() {
    return attachments;
  }
  public void setAttachments(PostAzioneSendMailAllOfAttachments attachments) {
    this.attachments = attachments;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostAzioneSendMailAllOf postAzioneSendMailAllOf = (PostAzioneSendMailAllOf) o;
    return Objects.equals(to, postAzioneSendMailAllOf.to) &&
        Objects.equals(cc, postAzioneSendMailAllOf.cc) &&
        Objects.equals(bcc, postAzioneSendMailAllOf.bcc) &&
        Objects.equals(subject, postAzioneSendMailAllOf.subject) &&
        Objects.equals(text, postAzioneSendMailAllOf.text) &&
        Objects.equals(attachments, postAzioneSendMailAllOf.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(to, cc, bcc, subject, text, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostAzioneSendMailAllOf {\n");
    
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    cc: ").append(toIndentedString(cc)).append("\n");
    sb.append("    bcc: ").append(toIndentedString(bcc)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
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

