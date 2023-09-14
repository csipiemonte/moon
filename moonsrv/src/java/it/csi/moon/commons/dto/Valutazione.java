/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

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
