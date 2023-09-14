/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idValutazione == null) ? 0 : idValutazione.hashCode());
		result = prime * result + ((descValutazione == null) ? 0 : descValutazione.hashCode());
		result = prime * result + ((numeroIstanze == null) ? 0 : numeroIstanze.hashCode());
		result = prime * result + ((percent == null) ? 0 : percent.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValutazioneModuloSintesi other = (ValutazioneModuloSintesi) obj;
		if (idValutazione == null) {
			if (other.idValutazione != null)
				return false;
		} else if (!idValutazione.equals(other.idValutazione))
			return false;
		if (descValutazione == null) {
			if (other.descValutazione != null)
				return false;
		} else if (!descValutazione.equals(other.descValutazione))
			return false;
		if (numeroIstanze == null) {
			if (other.numeroIstanze != null)
				return false;
		} else if (!numeroIstanze.equals(other.numeroIstanze))
			return false;
		if (percent == null) {
			if (other.percent != null)
				return false;
		} else if (!percent.equals(other.percent))
			return false;
		return true;
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
