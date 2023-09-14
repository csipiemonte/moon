/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StoricoWorkflow   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
	
	private Long idStoricoWorkflow = null;
	private Long idIstanza = null;
	private Long idProcesso = null;
	private Integer idStatoWfPartenza = null;
	private Integer idStatoWfArrivo = null;
	private String nomeStatoWfPartenza = null;
	private String nomeStatoWfArrivo = null;
	private Long idAzione = null;
	private String nomeAzione = null;
	private String datiAzione = null;
	private String descDestinatario = null;
	private Date dataInizio = null;
	private Date dataFine = null;
	private Boolean contieneDati = false;
	private Boolean contieneOutput = false;
	private String strutturaDatiAzione = null;
	private Long idFileRendering;
	private String attoreUpd = null;

	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Long getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}
	public Integer getIdStatoWfPartenza() {
		return idStatoWfPartenza;
	}
	public void setIdStatoWfPartenza(Integer idStatoWfPartenza) {
		this.idStatoWfPartenza = idStatoWfPartenza;
	}
	public Integer getIdStatoWfArrivo() {
		return idStatoWfArrivo;
	}
	public void setIdStatoWfArrivo(Integer idStatoWfArrivo) {
		this.idStatoWfArrivo = idStatoWfArrivo;
	}
	public String getNomeStatoWfPartenza() {
		return nomeStatoWfPartenza;
	}
	public void setNomeStatoWfPartenza(String nomeStatoWfPartenza) {
		this.nomeStatoWfPartenza = nomeStatoWfPartenza;
	}
	public String getNomeStatoWfArrivo() {
		return nomeStatoWfArrivo;
	}
	public void setNomeStatoWfArrivo(String nomeStatoWfArrivo) {
		this.nomeStatoWfArrivo = nomeStatoWfArrivo;
	}
	public Long getIdAzione() {
		return idAzione;
	}
	public void setIdAzione(Long idAzione) {
		this.idAzione = idAzione;
	}
	public String getNomeAzione() {
		return nomeAzione;
	}
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}
	public String getDatiAzione() {
		return datiAzione;
	}
	public void setDatiAzione(String datiAzione) {
		this.datiAzione = datiAzione;
	}
	public String getDescDestinatario() {
		return descDestinatario;
	}
	public void setDescDestinatario(String descDestinatario) {
		this.descDestinatario = descDestinatario;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	public Boolean getContieneDati() {
		return contieneDati;
	}
	public void setContieneDati(Boolean contieneDati) {
		this.contieneDati = contieneDati;
	}
	public Boolean getContieneOutput() {
		return contieneOutput;
	}
	public void setContieneOutput(Boolean contieneOutput) {
		this.contieneOutput = contieneOutput;
	}
	public String getStrutturaDatiAzione() {
		return strutturaDatiAzione;
	}
	public void setStrutturaDatiAzione(String strutturaDatiAzione) {
		this.strutturaDatiAzione = strutturaDatiAzione;
	}
	public Long getIdFileRendering() {
		return idFileRendering;
	}
	public void setIdFileRendering(Long idFileRendering) {
		this.idFileRendering = idFileRendering;
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
    StoricoWorkflow storicoWorkflow = (StoricoWorkflow) o;
    return 
        Objects.equals(idStoricoWorkflow, storicoWorkflow.idStoricoWorkflow) &&  
    	Objects.equals(idIstanza, storicoWorkflow.idIstanza) && 
    	Objects.equals(idProcesso, storicoWorkflow.idProcesso) && 
        Objects.equals(idStatoWfPartenza, storicoWorkflow.idStatoWfPartenza) &&
        Objects.equals(idStatoWfArrivo, storicoWorkflow.idStatoWfArrivo) &&
        Objects.equals(nomeStatoWfPartenza, storicoWorkflow.nomeStatoWfPartenza) &&
        Objects.equals(nomeStatoWfArrivo, storicoWorkflow.nomeStatoWfArrivo) &&
        Objects.equals(idAzione, storicoWorkflow.idAzione) &&
        Objects.equals(nomeAzione, storicoWorkflow.nomeAzione) &&
		Objects.equals(datiAzione, storicoWorkflow.datiAzione) &&
		Objects.equals(descDestinatario, storicoWorkflow.descDestinatario) &&
		Objects.equals(dataInizio, storicoWorkflow.dataInizio) &&
		Objects.equals(dataFine, storicoWorkflow.dataFine) &&
		Objects.equals(contieneDati, storicoWorkflow.contieneDati) &&
		Objects.equals(contieneOutput, storicoWorkflow.contieneOutput) &&
		Objects.equals(strutturaDatiAzione, storicoWorkflow.strutturaDatiAzione) &&
		Objects.equals(idFileRendering, storicoWorkflow.idFileRendering) &&
		Objects.equals(attoreUpd, storicoWorkflow.attoreUpd);
  }


  @Override
  public int hashCode() {
    return Objects.hash(idStoricoWorkflow, idIstanza, idProcesso, idStatoWfPartenza, idStatoWfArrivo, nomeStatoWfPartenza, nomeStatoWfArrivo, idAzione, 
    		nomeAzione, datiAzione, descDestinatario, dataInizio, dataFine, contieneDati, contieneOutput, strutturaDatiAzione, idFileRendering, attoreUpd);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoricoWorkflow {\n");
    
    sb.append("    idStoricoWorkflow: ").append(toIndentedString(idStoricoWorkflow)).append("\n");
    sb.append("    idIstanza: ").append(toIndentedString(idIstanza)).append("\n");
    sb.append("    idProcesso: ").append(toIndentedString(idProcesso)).append("\n");
    sb.append("    idStatoWfPartenza: ").append(toIndentedString(idStatoWfPartenza)).append("\n");
    sb.append("    idStatoWfArrivo: ").append(toIndentedString(idStatoWfArrivo)).append("\n");
    sb.append("    nomeStatoWfPartenza: ").append(toIndentedString(nomeStatoWfPartenza)).append("\n");
    sb.append("    nomeStatoWfArrivo: ").append(toIndentedString(nomeStatoWfArrivo)).append("\n");
    sb.append("    idAzione: ").append(toIndentedString(idAzione)).append("\n");
    sb.append("    nomeAzione: ").append(toIndentedString(nomeAzione)).append("\n");
    sb.append("    datiAzione: ").append(toIndentedString(datiAzione)).append("\n");
    sb.append("    descDestinatario: ").append(toIndentedString(descDestinatario)).append("\n");
    sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
    sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
    sb.append("    contieneDati: ").append(toIndentedString(contieneDati)).append("\n");
    sb.append("    contieneOutput: ").append(toIndentedString(contieneOutput)).append("\n");
    sb.append("    strutturaDatiAzione: ").append(toIndentedString(strutturaDatiAzione)).append("\n");
    sb.append("    idFileRendering: ").append(toIndentedString(idFileRendering)).append("\n");
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

