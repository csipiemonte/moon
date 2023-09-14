/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class Struttura36 {
	private String denominazione;
	private String tipologia;
	private Integer numero31012020;
	private Integer numeroSezioniFunzionanti;
	
	
	@ApiModelProperty(value = "denominazione del servizio")
	@JsonProperty("denominazione") 	
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	

	@ApiModelProperty(value = "tipologia del servizio")
	@JsonProperty("tipologia") 	
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	
	@ApiModelProperty(value = "frequentanti al 31012020")
	@JsonProperty("numero31012020") 	
	public Integer getNumero31012020() {
		return numero31012020;
	}
	public void setNumero31012020(Integer numero31012020) {
		this.numero31012020 = numero31012020;
	}
	
	@ApiModelProperty(value = "numero sezioni funzionanti")
	@JsonProperty("numeroSezioniFunzionanti") 	
	public Integer getNumeroSezioniFunzionanti() {
		return numeroSezioniFunzionanti;
	}
	public void setNumeroSezioniFunzionanti(Integer numeroSezioniFunzionanti) {
		this.numeroSezioniFunzionanti = numeroSezioniFunzionanti;
	}
	
	
	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Struttura36 oggetto = (Struttura36) o;
	    return Objects.equals(denominazione, oggetto.denominazione) &&
	        Objects.equals(tipologia, oggetto.tipologia) &&
	        Objects.equals(numero31012020, oggetto.numero31012020) &&
	        Objects.equals(numeroSezioniFunzionanti, oggetto.numeroSezioniFunzionanti) ;

	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(denominazione, tipologia, numero31012020, numeroSezioniFunzionanti);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Ed03 {\n");
	    
	    sb.append("    denominazione: ").append(toIndentedString(denominazione)).append("\n");
	    sb.append("    tipologia: ").append(toIndentedString(tipologia)).append("\n");
	    sb.append("    numero31012020: ").append(toIndentedString(numero31012020)).append("\n");
	    sb.append("    numeroSezioniFunzionanti: ").append(toIndentedString(numeroSezioniFunzionanti)).append("\n");
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
