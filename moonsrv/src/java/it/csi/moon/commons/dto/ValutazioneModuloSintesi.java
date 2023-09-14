/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

/**
 * ValutazioneModuloSintesi
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ValutazioneModuloSintesi {
	
	private Integer idValutazione;
	private String descValutazione;
	private Integer numeroIstanze;
	private Float percent;

	public Integer getIdValutazione() {
		return idValutazione;
	}
	public void setIdValutazione(Integer idValutazione) {
		this.idValutazione = idValutazione;
	}
	public String getDescValutazione() {
		return descValutazione;
	}
	public void setDescValutazione(String descValutazione) {
		this.descValutazione = descValutazione;
	}
	public Integer getNumeroIstanze() {
		return numeroIstanze;
	}
	public void setNumeroIstanze(Integer numeroIstanze) {
		this.numeroIstanze = numeroIstanze;
	}
	public Float getPercent() {
		return percent;
	}
	public void setPercent(Float percent) {
		this.percent = percent;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class ValutazioneModuloSintesi {\n");
	    sb.append("    idValutazione: ").append(toIndentedString(idValutazione)).append("\n");
	    sb.append("    descValutazione: ").append(toIndentedString(descValutazione)).append("\n");
	    sb.append("    numeroIstanze: ").append(toIndentedString(numeroIstanze)).append("\n");
	    sb.append("    percent: ").append(toIndentedString(percent)).append("\n");
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
