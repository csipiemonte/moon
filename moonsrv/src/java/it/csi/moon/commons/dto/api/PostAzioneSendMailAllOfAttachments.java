/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Tipologie dei file da allegare nella email")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyEapServerCodegen")
public class PostAzioneSendMailAllOfAttachments   {
  

  private Boolean istanza;

  private Boolean allegati;

  private Boolean allegatiAzione;

  /**
   * Indica la necessita di allegare il PDF dell&#39;istanza MOOn alla email&#39;
   **/
  
  @ApiModelProperty(value = "Indica la necessita di allegare il PDF dell'istanza MOOn alla email'")
  @JsonProperty("istanza")
  public Boolean getIstanza() {
    return istanza;
  }
  public void setIstanza(Boolean istanza) {
    this.istanza = istanza;
  }

  /**
   * Indica la necessita di allegare gli allegati dell&#39;istanza MOOn alla email&#39;
   **/
  
  @ApiModelProperty(value = "Indica la necessita di allegare gli allegati dell'istanza MOOn alla email'")
  @JsonProperty("allegati")
  public Boolean getAllegati() {
    return allegati;
  }
  public void setAllegati(Boolean allegati) {
    this.allegati = allegati;
  }

  /**
   * Indica la necessita di allegare gli allegati dell&#39;attuale azione di workflow alla email&#39;
   **/
  
  @ApiModelProperty(value = "Indica la necessita di allegare gli allegati dell'attuale azione di workflow alla email'")
  @JsonProperty("allegatiAzione")
  public Boolean getAllegatiAzione() {
    return allegatiAzione;
  }
  public void setAllegatiAzione(Boolean allegatiAzione) {
    this.allegatiAzione = allegatiAzione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostAzioneSendMailAllOfAttachments postAzioneSendMailAllOfAttachments = (PostAzioneSendMailAllOfAttachments) o;
    return Objects.equals(istanza, postAzioneSendMailAllOfAttachments.istanza) &&
        Objects.equals(allegati, postAzioneSendMailAllOfAttachments.allegati) &&
        Objects.equals(allegatiAzione, postAzioneSendMailAllOfAttachments.allegatiAzione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(istanza, allegati, allegatiAzione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostAzioneSendMailAllOfAttachments {\n");
    
    sb.append("    istanza: ").append(toIndentedString(istanza)).append("\n");
    sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
    sb.append("    allegatiAzione: ").append(toIndentedString(allegatiAzione)).append("\n");
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

