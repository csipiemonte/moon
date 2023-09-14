/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.extra.demografia;

import java.util.Date;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * DocumentoRiconoscimento (Fonte: Demografia ANPR)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class DocumentoRiconoscimento   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
    
  private String numero = null;
  private Date dataRilascio = null;
  private Date dataScadenza = null;
  private String enteRilascio = null;

	public DocumentoRiconoscimento() {
		super();
	}
	
	@ApiModelProperty(value = "numero del documento")
	@JsonProperty("numero") 
	public String getNumero() {
	    return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	  /**
	   * dataRilascio
	   **/
	  @ApiModelProperty(value = "dataRilascio")
	  @JsonProperty("dataRilascio") 
    public Date getDataRilascio() {
		return dataRilascio;
	}
	public void setDataRilascio(Date dataRilascio) {
		this.dataRilascio = dataRilascio;
	}

	  /**
	   * dataScadenza
	   **/
	  @ApiModelProperty(value = "dataScadenza")
	  @JsonProperty("dataScadenza") 
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	  /**
	   * enteRilascio
	   **/
	  @ApiModelProperty(value = "enteRilascio")
	  @JsonProperty("enteRilascio") 
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public String getEnteRilascio() {
		return enteRilascio;
	}
	public void setEnteRilascio(String enteRilascio) {
		this.enteRilascio = enteRilascio;
	}

	  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentoRiconoscimento documento = (DocumentoRiconoscimento) o;
    return Objects.equals(numero, documento.numero) &&
        Objects.equals(dataRilascio, documento.dataRilascio) &&
        Objects.equals(dataScadenza, documento.dataScadenza) &&
        Objects.equals(enteRilascio, documento.enteRilascio) ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(numero, dataRilascio, dataScadenza, enteRilascio);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentoRiconoscimento {\n");
    
    sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
    sb.append("    dataRilascio: ").append(toIndentedString(dataRilascio)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    enteRilascio: ").append(toIndentedString(enteRilascio)).append("\n");
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

