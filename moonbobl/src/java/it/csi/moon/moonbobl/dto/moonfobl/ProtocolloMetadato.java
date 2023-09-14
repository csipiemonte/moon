/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

/**
 * Metadato per la protocollazione (moon_pr_d_parametri)
 * <br>
 * <br>Tabella moon_pr_d_metadato
 * <br>PK: idMetadato
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 12/05/2022 - versione iniziale
 */
public class ProtocolloMetadato {

	private Long idMetadato;
	private String nomeMetadato;
	private String defaultValue;
	private Integer ordine;
	
	public Long getIdMetadato() {
		return idMetadato;
	}
	public void setIdMetadato(Long idMetadato) {
		this.idMetadato = idMetadato;
	}
	public String getNomeMetadato() {
		return nomeMetadato;
	}
	public void setNomeMetadato(String nomeMetadato) {
		this.nomeMetadato = nomeMetadato;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Integer getOrdine() {
		return ordine;
	}
	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}
	
	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class ProtocolloMetadato {\n");
	    
	    sb.append("    idMetadato: ").append(toIndentedString(idMetadato)).append("\n");
	    sb.append("    nomeMetadato: ").append(toIndentedString(nomeMetadato)).append("\n");
	    sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
	    sb.append("    ordine: ").append(toIndentedString(ordine)).append("\n");
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
