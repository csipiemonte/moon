/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * Ambito di un modulo (moon_fo_d_ambito)
 * 
 * @author FZ
 *
 * @since 1.0.0
 */
public class Ambito {
	
	private Integer idAmbito = null;
	private String codiceAmbito = null;
	private String nomeAmbito = null;
	private Integer idVisibilitaAmbito = null;
	private String color = null;
	private Date dataUpd;
	private String attoreUpd;
	 
	@ApiModelProperty(value = "identificativo dell'ambito")
	public Integer getIdAmbito() {
		return idAmbito;
	}
	public void setIdAmbito(Integer idAmbito) {
		this.idAmbito = idAmbito;
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
	
	@ApiModelProperty(value = "Visibilita dell'ambito")
	public Integer getIdVisibilitaAmbito() {
		return idVisibilitaAmbito;
	}
	public void setIdVisibilitaAmbito(Integer idVisibilitaAmbito) {
		this.idVisibilitaAmbito = idVisibilitaAmbito;
	}
	
	@ApiModelProperty(value = "Color dell'ambito")
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date modified) {
		this.dataUpd = modified;
	}
	  
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}
	  
	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Ambito obj = (Ambito) o;
	    return 
	    	Objects.equals(idAmbito, obj.idAmbito) &&
	    	Objects.equals(codiceAmbito, obj.codiceAmbito) &&
	    	Objects.equals(nomeAmbito, obj.nomeAmbito) &&
	    	Objects.equals(idVisibilitaAmbito, obj.idVisibilitaAmbito) &&
	    	Objects.equals(color, obj.color) &&
	    	Objects.equals(dataUpd, obj.dataUpd) &&
	    	Objects.equals(attoreUpd, obj.attoreUpd);
	}
	  
	@Override
	public int hashCode() {
		return Objects.hash(idAmbito, codiceAmbito, nomeAmbito, idVisibilitaAmbito, color, dataUpd, attoreUpd);
	}

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Ambito {\n");
	    
	    sb.append("    idAmbito: ").append(toIndentedString(idAmbito)).append("\n");
	    sb.append("    codiceAmbito: ").append(toIndentedString(codiceAmbito)).append("\n");
	    sb.append("    nomeAmbito: ").append(toIndentedString(nomeAmbito)).append("\n");
	    sb.append("    idVisibilitaAmbito: ").append(toIndentedString(idVisibilitaAmbito)).append("\n");
	    sb.append("    color: ").append(toIndentedString(color)).append("\n");
	    sb.append("    dataUpd: ").append(toIndentedString(dataUpd)).append("\n");
	    sb.append("    attoreUpd: ").append(toIndentedString(attoreUpd)).append("\n");
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
