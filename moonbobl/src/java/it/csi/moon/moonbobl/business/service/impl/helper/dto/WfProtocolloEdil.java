/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class WfProtocolloEdil {
	private String numeroProtocollo;
	private String dataProtocollo;
	private String submit = null;
	
	/*
	 * {"data":{"numeroProtocollo":"2020-11-3213131","dateTime":"2020-05-18T00:00:00+02:00","submit":true}}
	 */

	@ApiModelProperty(value = "numeroProtocollo")
	@JsonProperty("numeroProtocollo") 	
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	
	@ApiModelProperty(value = "dataProtocollo")
	@JsonProperty("dataProtocollo")	 
	public String getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	
	@ApiModelProperty(value = "submit")
	@JsonProperty("submit")	 
	public String getSubmit() {
		return submit;
	}
	public void setSubmit(String submit) {
		this.submit = submit;
	}
	
	 @Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    WfProtocolloEdil oggetto = (WfProtocolloEdil) o;
	    return Objects.equals(numeroProtocollo, oggetto.numeroProtocollo) &&
	        Objects.equals(dataProtocollo, oggetto.dataProtocollo);

	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(numeroProtocollo, dataProtocollo);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class WfProtocolloEdil {\n");
	    
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
