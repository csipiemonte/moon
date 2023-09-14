/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;


import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Allegato fornito dal fruitore associato ad una azione.")@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyEapServerCodegen")
public class FruitoreAllegatoAzione   {
  

  private String nomeFile;

  private String contentType;

  private String contenuto;

  private String refUrl;

  private String descrizione;

  private Integer lunghezza;

  private Boolean docFirmato;

  private Integer idTipologia;

  private Date data;

  private String tipo;

  private String numeroProtocollo;

  private Date dataProtocollo;

  /**
   * Nome del file allegato
   **/
  
  @ApiModelProperty(example = "NuovoModulo.pdf", required = true, value = "Nome del file allegato")
  @JsonProperty("nomeFile")
  @NotNull
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   * Content type del file allegato
   **/
  
  @ApiModelProperty(example = "application/pdf", required = true, value = "Content type del file allegato")
  @JsonProperty("contentType")
  @NotNull
  public String getContentType() {
    return contentType;
  }
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Contenuto binario del file allegato base64 encoded - campo obbligatorio in alternativa a refUrl
   **/
  
  @ApiModelProperty(example = "c0d1acc2-731a-420a-ad60-2fe13263b56d", value = "Contenuto binario del file allegato base64 encoded - campo obbligatorio in alternativa a refUrl")
  @JsonProperty("contenuto")
  public String getContenuto() {
    return contenuto;
  }
  public void setContenuto(String contenuto) {
    this.contenuto = contenuto;
  }

  /**
   * Url reference di download dell&#39;allegato - campo obbligatorio in alternativa a contenuto
   **/
  
  @ApiModelProperty(example = "http://esempio.csi.it/doc/allegato123.pdf", value = "Url reference di download dell'allegato - campo obbligatorio in alternativa a contenuto")
  @JsonProperty("refUrl")
  public String getRefUrl() {
    return refUrl;
  }
  public void setRefUrl(String refUrl) {
    this.refUrl = refUrl;
  }

  /**
   * Descrizione breve del file allegato
   **/
  
  @ApiModelProperty(example = "Notifica di integrazione", value = "Descrizione breve del file allegato")
  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * Dimensione allegato in bytes
   **/
  
  @ApiModelProperty(example = "102117", value = "Dimensione allegato in bytes")
  @JsonProperty("lunghezza")
  public Integer getLunghezza() {
    return lunghezza;
  }
  public void setLunghezza(Integer lunghezza) {
    this.lunghezza = lunghezza;
  }

  /**
   * Indica se il file è firmato
   **/
  
  @ApiModelProperty(example = "false", value = "Indica se il file è firmato")
  @JsonProperty("docFirmato")
  public Boolean getDocFirmato() {
    return docFirmato;
  }
  public void setDocFirmato(Boolean docFirmato) {
    this.docFirmato = docFirmato;
  }

  /**
   * Identificativo MOOn di classificazione del file allegato.
   **/
  
  @ApiModelProperty(example = "2", value = "Identificativo MOOn di classificazione del file allegato.")
  @JsonProperty("idTipologia")
  public Integer getIdTipologia() {
    return idTipologia;
  }
  public void setIdTipologia(Integer idTipologia) {
    this.idTipologia = idTipologia;
  }

  /**
   * Data inserimento allegato
   **/
  
  @ApiModelProperty(value = "Data inserimento allegato")
  @JsonProperty("data")
  public Date getData() {
    return data;
  }
  public void setData(Date data) {
    this.data = data;
  }

  /**
   * Identificativo di classificazione del file allegato del sistema fruitore
   **/
  
  @ApiModelProperty(example = "ALLEGATO_RICHIESTA_INTEGRAZIONE", value = "Identificativo di classificazione del file allegato del sistema fruitore")
  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   * Numero di protocollo in uscita del file allegato
   **/
  
  @ApiModelProperty(value = "Numero di protocollo in uscita del file allegato")
  @JsonProperty("numeroProtocollo")
  public String getNumeroProtocollo() {
    return numeroProtocollo;
  }
  public void setNumeroProtocollo(String numeroProtocollo) {
    this.numeroProtocollo = numeroProtocollo;
  }

  /**
   * Data di protocollo in uscita del file allegato (2022-10-29 00:00:00)
   **/
  
  @ApiModelProperty(value = "Data di protocollo in uscita del file allegato (2022-10-29 00:00:00)")
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
    FruitoreAllegatoAzione allegatoAzione = (FruitoreAllegatoAzione) o;
    return Objects.equals(nomeFile, allegatoAzione.nomeFile) &&
        Objects.equals(contentType, allegatoAzione.contentType) &&
        Objects.equals(contenuto, allegatoAzione.contenuto) &&
        Objects.equals(refUrl, allegatoAzione.refUrl) &&
        Objects.equals(descrizione, allegatoAzione.descrizione) &&
        Objects.equals(lunghezza, allegatoAzione.lunghezza) &&
        Objects.equals(docFirmato, allegatoAzione.docFirmato) &&
        Objects.equals(idTipologia, allegatoAzione.idTipologia) &&
        Objects.equals(data, allegatoAzione.data) &&
        Objects.equals(tipo, allegatoAzione.tipo) &&
        Objects.equals(numeroProtocollo, allegatoAzione.numeroProtocollo) &&
        Objects.equals(dataProtocollo, allegatoAzione.dataProtocollo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomeFile, contentType, contenuto, refUrl, descrizione, lunghezza, docFirmato, idTipologia, data, tipo, numeroProtocollo, dataProtocollo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllegatoAzione {\n");
    
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
    sb.append("    contenuto: ").append(toIndentedString(contenuto)).append("\n");
    sb.append("    refUrl: ").append(toIndentedString(refUrl)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    lunghezza: ").append(toIndentedString(lunghezza)).append("\n");
    sb.append("    docFirmato: ").append(toIndentedString(docFirmato)).append("\n");
    sb.append("    idTipologia: ").append(toIndentedString(idTipologia)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
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



