/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class Struttura03 {
	private String denominazione;
	private String autorizzazione;
	private String tipologia;
	private Integer numero31012020;
	private Integer capacita;
	
	
	@ApiModelProperty(value = "denominazione del servizio")
	@JsonProperty("denominazione") 	
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	
	@ApiModelProperty(value = "autorizzazione")
	@JsonProperty("autorizzazione") 	
	public String getAutorizzazione() {
		return autorizzazione;
	}
	public void setAutorizzazione(String autorizzazione) {
		this.autorizzazione = autorizzazione;
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
	
	@ApiModelProperty(value = "capacita del servizio")
	@JsonProperty("capacita") 	
	public Integer getCapacita() {
		return capacita;
	}
	public void setCapacita(Integer capacita) {
		this.capacita = capacita;
	}
	
	
	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Struttura03 oggetto = (Struttura03) o;
	    return Objects.equals(denominazione, oggetto.denominazione) &&
	        Objects.equals(autorizzazione, oggetto.autorizzazione) &&
	        Objects.equals(tipologia, oggetto.tipologia) &&
	        Objects.equals(numero31012020, oggetto.numero31012020) &&
	        Objects.equals(capacita, oggetto.capacita) ;

	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(denominazione, autorizzazione, tipologia, numero31012020, capacita);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Ed03 {\n");
	    
	    sb.append("    denominazione: ").append(toIndentedString(denominazione)).append("\n");
	    sb.append("    autorizzazione: ").append(toIndentedString(autorizzazione)).append("\n");
	    sb.append("    tipologia: ").append(toIndentedString(tipologia)).append("\n");
	    sb.append("    numero31012020: ").append(toIndentedString(numero31012020)).append("\n");
	    sb.append("    capacita: ").append(toIndentedString(capacita)).append("\n");
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
