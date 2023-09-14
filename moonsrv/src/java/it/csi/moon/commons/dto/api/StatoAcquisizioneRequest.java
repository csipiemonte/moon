/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Dati relativi alla comunicazione del cambio di stato ( obbligatori codice e descrizione dell'esito dell'azione )")@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyEapServerCodegen")
public class StatoAcquisizioneRequest   {
  

  private String codice;

  private String descrizione;

  private String identificativo;

  private Date data;

  private String numeroProtocollo;

  private Date dataProtocollo;

  private String codiceFiscaleOperatore;

  private List<ProtocolloDocumento> protocolliDocumento = new ArrayList<ProtocolloDocumento>();

  private String datiAzione;

  private List<FruitoreAllegatoAzione> allegatiAzione = new ArrayList<FruitoreAllegatoAzione>();

  private List<PostAzione> postAzioni = new ArrayList<PostAzione>();

  /**
   * Codice dell&#39;esito ( es: IMPOK IMPKO IMPOKW )
   **/
  
  @ApiModelProperty(example = "IMPOKW", required = true, value = "Codice dell'esito ( es: IMPOK IMPKO IMPOKW )")
  @JsonProperty("codice")
  @NotNull
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   * Descrizione dell&#39;esito
   **/
  
  @ApiModelProperty(example = "Importazione OK con warning", required = true, value = "Descrizione dell'esito")
  @JsonProperty("descrizione")
  @NotNull
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * Identificativo gestione back office ( es. numero pratica )
   **/
  
  @ApiModelProperty(example = "B20200000452", value = "Identificativo gestione back office ( es. numero pratica )")
  @JsonProperty("identificativo")
  public String getIdentificativo() {
    return identificativo;
  }
  public void setIdentificativo(String identificativo) {
    this.identificativo = identificativo;
  }

  /**
   * Data gestione back office ( es. data pratica )
   **/
  
  @ApiModelProperty(value = "Data gestione back office ( es. data pratica )")
  @JsonProperty("data")
  public Date getData() {
    return data;
  }
  public void setData(Date data) {
    this.data = data;
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
   * Codice fiscale dell&#39;operatore responsabile della gestione back office
   **/
  
  @ApiModelProperty(value = "Codice fiscale dell'operatore responsabile della gestione back office")
  @JsonProperty("codiceFiscaleOperatore")
  public String getCodiceFiscaleOperatore() {
    return codiceFiscaleOperatore;
  }
  public void setCodiceFiscaleOperatore(String codiceFiscaleOperatore) {
    this.codiceFiscaleOperatore = codiceFiscaleOperatore;
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
   * Dati associati all&#39;azione di business convertito in un campo string
   **/
  
  @ApiModelProperty(value = "Dati associati all'azione di business convertito in un campo string")
  @JsonProperty("datiAzione")
  public String getDatiAzione() {
    return datiAzione;
  }
  public void setDatiAzione(String datiAzione) {
    this.datiAzione = datiAzione;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("allegatiAzione")
  public List<FruitoreAllegatoAzione> getAllegatiAzione() {
    return allegatiAzione;
  }
  public void setAllegatiAzione(List<FruitoreAllegatoAzione> allegatiAzione) {
    this.allegatiAzione = allegatiAzione;
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
    StatoAcquisizioneRequest acquisizione = (StatoAcquisizioneRequest) o;
    return Objects.equals(codice, acquisizione.codice) &&
        Objects.equals(descrizione, acquisizione.descrizione) &&
        Objects.equals(identificativo, acquisizione.identificativo) &&
        Objects.equals(data, acquisizione.data) &&
        Objects.equals(numeroProtocollo, acquisizione.numeroProtocollo) &&
        Objects.equals(dataProtocollo, acquisizione.dataProtocollo) &&
        Objects.equals(codiceFiscaleOperatore, acquisizione.codiceFiscaleOperatore) &&
        Objects.equals(protocolliDocumento, acquisizione.protocolliDocumento) &&
        Objects.equals(datiAzione, acquisizione.datiAzione) &&
        Objects.equals(allegatiAzione, acquisizione.allegatiAzione) &&
        Objects.equals(postAzioni, acquisizione.postAzioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, identificativo, data, numeroProtocollo, dataProtocollo, codiceFiscaleOperatore, protocolliDocumento, datiAzione, allegatiAzione, postAzioni);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Acquisizione {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    identificativo: ").append(toIndentedString(identificativo)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    numeroProtocollo: ").append(toIndentedString(numeroProtocollo)).append("\n");
    sb.append("    dataProtocollo: ").append(toIndentedString(dataProtocollo)).append("\n");
    sb.append("    codiceFiscaleOperatore: ").append(toIndentedString(codiceFiscaleOperatore)).append("\n");
    sb.append("    protocolliDocumento: ").append(toIndentedString(protocolliDocumento)).append("\n");
    sb.append("    datiAzione: ").append(toIndentedString(datiAzione)).append("\n");
    sb.append("    allegatiAzione: ").append(toIndentedString(allegatiAzione)).append("\n");
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