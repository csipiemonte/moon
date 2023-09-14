/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

/**
 * Valutazione modulo (moon_fo_t_valutazione_modulo)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Valutazione {
	
	private Boolean trovaModulo; // 3 // 1,2
	private Boolean compilaModulo; // 3 // 1,2
	private Boolean inviaIstanza; // 3 // 1,2
	private Boolean trovaIstanza; // 3 // 1,2
	private Boolean comeProcedere; // 3 // 1,2
	private Boolean aspettoGrafico; // 2 // 3,4
	private Boolean visualizzaResponsive; // 2 // 3,4
	private Boolean proceduraCompilaInvio; // 2 // 3,4
	private Boolean sezioneAiuto; // 2 // 3,4
	private String dettaglio; //  // 1,2,3,4,5


	public Boolean getTrovaModulo() {
		return trovaModulo;
	}
	public void setTrovaModulo(Boolean trovaModulo) {
		this.trovaModulo = trovaModulo;
	}
	public Boolean getCompilaModulo() {
		return compilaModulo;
	}
	public void setCompilaModulo(Boolean compilaModulo) {
		this.compilaModulo = compilaModulo;
	}
	public Boolean getInviaIstanza() {
		return inviaIstanza;
	}
	public void setInviaIstanza(Boolean inviaIstanza) {
		this.inviaIstanza = inviaIstanza;
	}
	public Boolean getTrovaIstanza() {
		return trovaIstanza;
	}
	public void setTrovaIstanza(Boolean trovaIstanza) {
		this.trovaIstanza = trovaIstanza;
	}
	public Boolean getComeProcedere() {
		return comeProcedere;
	}
	public void setComeProcedere(Boolean comeProcedere) {
		this.comeProcedere = comeProcedere;
	}
	public Boolean getAspettoGrafico() {
		return aspettoGrafico;
	}
	public void setAspettoGrafico(Boolean aspettoGrafico) {
		this.aspettoGrafico = aspettoGrafico;
	}
	public Boolean getVisualizzaResponsive() {
		return visualizzaResponsive;
	}
	public void setVisualizzaResponsive(Boolean visualizzaResponsive) {
		this.visualizzaResponsive = visualizzaResponsive;
	}
	public Boolean getProceduraCompilaInvio() {
		return proceduraCompilaInvio;
	}
	public void setProceduraCompilaInvio(Boolean proceduraCompilaInvio) {
		this.proceduraCompilaInvio = proceduraCompilaInvio;
	}
	public Boolean getSezioneAiuto() {
		return sezioneAiuto;
	}
	public void setSezioneAiuto(Boolean sezioneAiuto) {
		this.sezioneAiuto = sezioneAiuto;
	}
	public String getDettaglio() {
		return dettaglio;
	}
	public void setDettaglio(String dettaglio) {
		this.dettaglio = dettaglio;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aspettoGrafico == null) ? 0 : aspettoGrafico.hashCode());
		result = prime * result + ((comeProcedere == null) ? 0 : comeProcedere.hashCode());
		result = prime * result + ((compilaModulo == null) ? 0 : compilaModulo.hashCode());
		result = prime * result + ((inviaIstanza == null) ? 0 : inviaIstanza.hashCode());
		result = prime * result + ((proceduraCompilaInvio == null) ? 0 : proceduraCompilaInvio.hashCode());
		result = prime * result + ((sezioneAiuto == null) ? 0 : sezioneAiuto.hashCode());
		result = prime * result + ((trovaIstanza == null) ? 0 : trovaIstanza.hashCode());
		result = prime * result + ((trovaModulo == null) ? 0 : trovaModulo.hashCode());
		result = prime * result + ((visualizzaResponsive == null) ? 0 : visualizzaResponsive.hashCode());
		result = prime * result + ((dettaglio == null) ? 0 : dettaglio.hashCode());
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
		Valutazione other = (Valutazione) obj;
		if (aspettoGrafico == null) {
			if (other.aspettoGrafico != null)
				return false;
		} else if (!aspettoGrafico.equals(other.aspettoGrafico))
			return false;
		if (comeProcedere == null) {
			if (other.comeProcedere != null)
				return false;
		} else if (!comeProcedere.equals(other.comeProcedere))
			return false;
		if (compilaModulo == null) {
			if (other.compilaModulo != null)
				return false;
		} else if (!compilaModulo.equals(other.compilaModulo))
			return false;
		if (inviaIstanza == null) {
			if (other.inviaIstanza != null)
				return false;
		} else if (!inviaIstanza.equals(other.inviaIstanza))
			return false;
		if (proceduraCompilaInvio == null) {
			if (other.proceduraCompilaInvio != null)
				return false;
		} else if (!proceduraCompilaInvio.equals(other.proceduraCompilaInvio))
			return false;
		if (sezioneAiuto == null) {
			if (other.sezioneAiuto != null)
				return false;
		} else if (!sezioneAiuto.equals(other.sezioneAiuto))
			return false;
		if (trovaIstanza == null) {
			if (other.trovaIstanza != null)
				return false;
		} else if (!trovaIstanza.equals(other.trovaIstanza))
			return false;
		if (trovaModulo == null) {
			if (other.trovaModulo != null)
				return false;
		} else if (!trovaModulo.equals(other.trovaModulo))
			return false;
		if (visualizzaResponsive == null) {
			if (other.visualizzaResponsive != null)
				return false;
		} else if (!visualizzaResponsive.equals(other.visualizzaResponsive))
			return false;
		if (dettaglio == null) {
			if (other.dettaglio != null)
				return false;
		} else if (!dettaglio.equals(other.dettaglio))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Valutazione {\n");
	    sb.append("    trovaModulo: ").append(toIndentedString(trovaModulo)).append("\n");
	    sb.append("    compilaModulo: ").append(toIndentedString(compilaModulo)).append("\n");
	    sb.append("    inviaIstanza: ").append(toIndentedString(inviaIstanza)).append("\n");
	    sb.append("    trovaIstanza: ").append(toIndentedString(trovaIstanza)).append("\n");
	    sb.append("    comeProcedere: ").append(toIndentedString(comeProcedere)).append("\n");
	    sb.append("    aspettoGrafico: ").append(toIndentedString(aspettoGrafico)).append("\n");
	    sb.append("    visualizzaResponsive: ").append(toIndentedString(visualizzaResponsive)).append("\n");
	    sb.append("    proceduraCompilaInvio: ").append(toIndentedString(proceduraCompilaInvio)).append("\n");
	    sb.append("    sezioneAiuto: ").append(toIndentedString(sezioneAiuto)).append("\n");
	    sb.append("    dettaglio: ").append(toIndentedString(dettaglio)).append("\n");
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
