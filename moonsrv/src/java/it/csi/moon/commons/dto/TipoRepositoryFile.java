/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Objects;

import it.csi.moon.commons.entity.TipoRepositoryFileEntity;

public class TipoRepositoryFile {
	
	private String codice = null;
	private String descrizione = null;
	private Integer tipoIngUsc = null;
	
	public TipoRepositoryFile() {
	}
	
	public TipoRepositoryFile(TipoRepositoryFileEntity entity) {
		this.codice = entity.getCodiceTipoRepositoryFile();
		this.descrizione = entity.getDescrizioneTipoRepositoryFile();
		this.tipoIngUsc = entity.getTipoIngUsc();
	}

	public String getCodice() {
		return codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Integer getTipoIngUsc() {
		return tipoIngUsc;
	}
	public void setTipoIngUsc(Integer tipoIngUsc) {
		this.tipoIngUsc = tipoIngUsc;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    TipoRepositoryFile obj = (TipoRepositoryFile) o;
	    return 
	    	Objects.equals(codice, obj.codice) &&
	    	Objects.equals(descrizione, obj.descrizione) &&
	    	Objects.equals(tipoIngUsc, obj.tipoIngUsc);
	}
	  
	@Override
	public int hashCode() {
		return Objects.hash(codice, descrizione, tipoIngUsc);
	}

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class TipoRepositoryFile {\n");
	    
	    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
	    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
	    sb.append("    tipoIngUsc: ").append(toIndentedString(tipoIngUsc)).append("\n");
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
