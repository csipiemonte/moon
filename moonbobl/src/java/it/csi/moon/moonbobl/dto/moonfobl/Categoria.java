/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * Categoria di un modulo (moon_fo_d_categoria)
 * 
 * @author FZ
 *
 * @since 1.0.0
 */
public class Categoria {
	
	private Integer idCategoria = null;
	private String descrizione = null;
	private String codiceAmbito = null;
	private String nomeAmbito = null;
	private String color = null;
		
	@ApiModelProperty(value = "identificativo della categoria")
	public Integer getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}
	
	@ApiModelProperty(value = "Descrizione della categoria")
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@ApiModelProperty(value = "Codice dell'ambito")
	public String getCodiceAmbito() {
		return codiceAmbito;
	}
	public void setCodiceAmbito(String codiceAmbito) {
		this.codiceAmbito = codiceAmbito;
	}
	
	@ApiModelProperty(value = "Nome dell'ambito")
	public String getNomeAmbito() {
		return nomeAmbito;
	}
	public void setNomeAmbito(String nomeAmbito) {
		this.nomeAmbito = nomeAmbito;
	}
	
	@ApiModelProperty(value = "Color dell'ambito")
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Categoria obj = (Categoria) o;
	    return 
	    	Objects.equals(idCategoria, obj.idCategoria) &&
	    	Objects.equals(descrizione, obj.descrizione) &&
	    	Objects.equals(codiceAmbito, obj.codiceAmbito) &&
	    	Objects.equals(nomeAmbito, obj.nomeAmbito) &&
	    	Objects.equals(color, obj.color);
	  }


	  @Override
	  public int hashCode() {
	    return Objects.hash(idCategoria, descrizione);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Categoria {\n");
	    
	    sb.append("    idCategoria: ").append(toIndentedString(idCategoria)).append("\n");
	    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
	    sb.append("    codiceAmbito: ").append(toIndentedString(codiceAmbito)).append("\n");
	    sb.append("    nomeAmbito: ").append(toIndentedString(nomeAmbito)).append("\n");
	    sb.append("    color: ").append(toIndentedString(color)).append("\n");
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
