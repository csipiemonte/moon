/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class RilevazioneServiziInfanzia {
	private Provincia provincia = null;
	private Comune comune = null;
	private String presenzaStruttures = null;
	private Boolean dichiarazioneResponsabilita = null;
	private Boolean salvaInBozza = null;
	private Boolean submit = null;
	private List<Struttura03> listStruttura03 = null; 
	private List<Struttura36> listStruttura36 = null;
	private String noteServizi02 = null;
	private String noteServizi36 = null;
	
	@ApiModelProperty(value = "Provincia")
	@JsonProperty("provincia") 	
	public Provincia getProvincia() {
		return provincia;
	}
	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}
	
	@ApiModelProperty(value = "Crovincia")
	@JsonProperty("comune")
	public Comune getComune() {
		return comune;
	}
	public void setComune(Comune comune) {
		this.comune = comune;
	}
	
	@ApiModelProperty(value = "Sono presenti servizi")
	@JsonProperty("presenzaStruttures")
	public String getPresenzaStruttures() {
		return presenzaStruttures;
	}
	public void setPresenzaStruttures(String presenzaStruttures) {
		this.presenzaStruttures = presenzaStruttures;
	}
	
	@ApiModelProperty(value = "Dichiarazione di responsabilita")
	@JsonProperty("dichiarazioneResponsabilita")
	public Boolean getDichiarazioneResponsabilita() {
		return dichiarazioneResponsabilita;
	}
	public void setDichiarazioneResponsabilita(Boolean dichiarazioneResponsabilita) {
		this.dichiarazioneResponsabilita = dichiarazioneResponsabilita;
	}
	
	@ApiModelProperty(value = "salva In Bozza")
	@JsonProperty("salvaInBozza")
	public Boolean getSalvaInBozza() {
		return salvaInBozza;
	}
	public void setSalvaInBozza(Boolean salvaInBozza) {
		this.salvaInBozza = salvaInBozza;
	}
	
	@ApiModelProperty(value = "submit")
	@JsonProperty("submit")
	public Boolean getSubmit() {
		return submit;
	}
	public void setSubmit(Boolean submit) {
		this.submit = submit;
	}
	
	@ApiModelProperty(value = "Strutture 0-2")
	@JsonProperty("ed03")
	public List<Struttura03> getListStruttura03() {
		return listStruttura03;
	}
	public void setListStruttura03(List<Struttura03> listStruttura03) {
		this.listStruttura03 = listStruttura03;
	}
	
	@ApiModelProperty(value = "Strutture 3-6")
	@JsonProperty("eg36")
	public List<Struttura36> getListStruttura36() {
		return listStruttura36;
	}
	public void setListStruttura36(List<Struttura36> listStruttura36) {
		this.listStruttura36 = listStruttura36;
	}

	@ApiModelProperty(value = "noteServizi 0-2")
	@JsonProperty("noteServizi02")
	 public String getNoteServizi02() {
		return noteServizi02;
	}
	public void setNoteServizi02(String noteServizi02) {
		this.noteServizi02 = noteServizi02;
	}
	
	@ApiModelProperty(value = "noteServizi 3-6")
	@JsonProperty("noteServizi36")
	public String getNoteServizi36() {
		return noteServizi36;
	}
	public void setNoteServizi36(String noteServizi36) {
		this.noteServizi36 = noteServizi36;
	}
	
	
	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    RilevazioneServiziInfanzia oggetto = (RilevazioneServiziInfanzia) o;
	    return Objects.equals(provincia, oggetto.provincia) &&
	        Objects.equals(comune, oggetto.comune) &&
	        Objects.equals(presenzaStruttures, oggetto.presenzaStruttures) &&
	        Objects.equals(dichiarazioneResponsabilita, oggetto.dichiarazioneResponsabilita) &&
	        Objects.equals(salvaInBozza, oggetto.salvaInBozza) &&
	        Objects.equals(submit, oggetto.submit) &&
	        Objects.equals(listStruttura03, oggetto.listStruttura03) &&
	        Objects.equals(listStruttura36, oggetto.listStruttura36)
	        ;

	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(provincia, comune, presenzaStruttures, dichiarazioneResponsabilita, salvaInBozza, submit, listStruttura03, listStruttura36);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class RilevazioneServiziInfanzia {\n");
	    
	    sb.append("    provincia: ").append(toIndentedString(provincia)).append("\n");
	    sb.append("    comune: ").append(toIndentedString(comune)).append("\n");
	    sb.append("    presenzaStruttures: ").append(toIndentedString(presenzaStruttures)).append("\n");
	    sb.append("    dichiarazioneResponsabilita: ").append(toIndentedString(dichiarazioneResponsabilita)).append("\n");
	    sb.append("    salvaInBozza: ").append(toIndentedString(salvaInBozza)).append("\n");
	    sb.append("    submit: ").append(toIndentedString(submit)).append("\n");
	    sb.append("    ed03: ").append(toIndentedString(listStruttura03)).append("\n");
	    sb.append("    eg36: ").append(toIndentedString(listStruttura36)).append("\n");
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
