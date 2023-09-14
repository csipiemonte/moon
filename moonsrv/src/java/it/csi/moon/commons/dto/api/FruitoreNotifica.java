/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Dati relativi ad una notifica su un'istanza")@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyEapServerCodegen")
public class FruitoreNotifica   {
  

  private String codiceFiscaleOperatore;

  private String datiNotifica;

  private String numeroProtocollo;

  private Date dataProtocollo;

  private List<ProtocolloDocumento> protocolliDocumento = new ArrayList<ProtocolloDocumento>();

  private List<FruitoreAllegatoAzione> allegatiNotifica = new ArrayList<FruitoreAllegatoAzione>();

  private List<PostAzione> postAzioni = new ArrayList<PostAzione>();

  /**
   * Codice fiscale dell&#39;operatore responsabile della notifica lato back office
   **/
  
  @ApiModelProperty(value = "Codice fiscale dell'operatore responsabile della notifica lato back office")
  @JsonProperty("codiceFiscaleOperatore")
  public String getCodiceFiscaleOperatore() {
    return codiceFiscaleOperatore;
  }
  public void setCodiceFiscaleOperatore(String codiceFiscaleOperatore) {
    this.codiceFiscaleOperatore = codiceFiscaleOperatore;
  }

  /**
   * Dati associati alla notifica (convertito in un campo string)
   **/
  
  @ApiModelProperty(value = "Dati associati alla notifica (convertito in un campo string)")
  @JsonProperty("datiNotifica")
  public String getDatiNotifica() {
    return datiNotifica;
  }
  public void setDatiNotifica(String datiNotifica) {
    this.datiNotifica = datiNotifica;
  }

  /**
   * Numero protocollo assegnato all&#39;istanza
   **/
  
  @ApiModelProperty(example = "B20200000452", value = "Numero protocollo assegnato all'istanza")
  @JsonProperty("numeroProtocollo")
  public String getNumeroProtocollo() {
    return numeroProtocollo;
  }
  public void setNumeroProtocollo(String numeroProtocollo) {
    this.numeroProtocollo = numeroProtocollo;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("dataProtocollo")
  public Date getDataProtocollo() {
    return dataProtocollo;
  }
  public void setDataProtocollo(Date dataProtocollo) {
    this.dataProtocollo = dataProtocollo;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("protocolliDocumento")
  public List<ProtocolloDocumento> getProtocolliDocumento() {
    return protocolliDocumento;
  }
  public void setProtocolliDocumento(List<ProtocolloDocumento> protocolliDocumento) {
    this.protocolliDocumento = protocolliDocumento;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("allegatiNotifica")
  public List<FruitoreAllegatoAzione> getAllegatiNotifica() {
    return allegatiNotifica;
  }
  public void setAllegatiNotifica(List<FruitoreAllegatoAzione> allegatiNotifica) {
    this.allegatiNotifica = allegatiNotifica;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("postAzioni")
  public List<PostAzione> getPostAzioni() {
    return postAzioni;
  }
  public void setPostAzioni(List<PostAzione> postAzioni) {
    this.postAzioni = postAzioni;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FruitoreNotifica notifica = (FruitoreNotifica) o;
    return Objects.equals(codiceFiscaleOperatore, notifica.codiceFiscaleOperatore) &&
        Objects.equals(datiNotifica, notifica.datiNotifica) &&
        Objects.equals(numeroProtocollo, notifica.numeroProtocollo) &&
        Objects.equals(dataProtocollo, notifica.dataProtocollo) &&
        Objects.equals(protocolliDocumento, notifica.protocolliDocumento) &&
        Objects.equals(allegatiNotifica, notifica.allegatiNotifica) &&
        Objects.equals(postAzioni, notifica.postAzioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceFiscaleOperatore, datiNotifica, numeroProtocollo, dataProtocollo, protocolliDocumento, allegatiNotifica, postAzioni);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Notifica {\n");
    
    sb.append("    codiceFiscaleOperatore: ").append(toIndentedString(codiceFiscaleOperatore)).append("\n");
    sb.append("    datiNotifica: ").append(toIndentedString(datiNotifica)).append("\n");
    sb.append("    numeroProtocollo: ").append(toIndentedString(numeroProtocollo)).append("\n");
    sb.append("    dataProtocollo: ").append(toIndentedString(dataProtocollo)).append("\n");
    sb.append("    protocolliDocumento: ").append(toIndentedString(protocolliDocumento)).append("\n");
    sb.append("    allegatiNotifica: ").append(toIndentedString(allegatiNotifica)).append("\n");
    sb.append("    postAzioni: ").append(toIndentedString(postAzioni)).append("\n");
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



