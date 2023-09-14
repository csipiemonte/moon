/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Objects;

public class Workflow   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  

	private Long idWorkflow = null;
	private Long idProcesso = null;
	private Integer idStatoWfPartenza = null;
	private Integer idStatoWfArrivo = null;
	private String campoCondizione = null;
	private String valoreCondizione = null;
	private Long idAzione = null;
	private String nomeAzione = null;
	private String codiceAzione = null;
	private String emailDestinatario = null;
	private Long idUtenteDestinatario = null;
	private Long idTipoUtenteDestinatario = null;
	private Long idGruppoUtentiDestinatari = null;
	private Boolean isAnnullabile = false;
	private Boolean isAutomatico = false;
	private Long idDatiAzione = null;
	private Boolean isAzioneConDati = false;
	private Long idCondition = null;
	//
	private Stato statoPartenza;
	private Stato statoArrivo;

  public Long getIdWorkflow() {
		return idWorkflow;
	}

	public void setIdWorkflow(Long idWorkflow) {
		this.idWorkflow = idWorkflow;
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

	public String getCampoCondizione() {
		return campoCondizione;
	}

	public void setCampoCondizione(String campoCondizione) {
		this.campoCondizione = campoCondizione;
	}

	public String getValoreCondizione() {
		return valoreCondizione;
	}

	public void setValoreCondizione(String valoreCondizione) {
		this.valoreCondizione = valoreCondizione;
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

	public String getCodiceAzione() {
		return codiceAzione;
	}

	public void setCodiceAzione(String codiceAzione) {
		this.codiceAzione = codiceAzione;
	}

	public String getEmailDestinatario() {
		return emailDestinatario;
	}

	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}

	public Long getIdUtenteDestinatario() {
		return idUtenteDestinatario;
	}

	public void setIdUtenteDestinatario(Long idUtenteDestinatario) {
		this.idUtenteDestinatario = idUtenteDestinatario;
	}

	public Long getIdTipoUtenteDestinatario() {
		return idTipoUtenteDestinatario;
	}

	public void setIdTipoUtenteDestinatario(Long idTipoUtenteDestinatario) {
		this.idTipoUtenteDestinatario = idTipoUtenteDestinatario;
	}

	public Long getIdGruppoUtentiDestinatari() {
		return idGruppoUtentiDestinatari;
	}

	public void setIdGruppoUtentiDestinatari(Long idGruppoUtentiDestinatari) {
		this.idGruppoUtentiDestinatari = idGruppoUtentiDestinatari;
	}

	public Boolean getIsAnnullabile() {
		return isAnnullabile;
	}

	public void setIsAnnullabile(Boolean isAnnullabile) {
		this.isAnnullabile = isAnnullabile;
	}

	public Boolean getIsAutomatico() {
		return isAutomatico;
	}

	public void setIsAutomatico(Boolean isAutomatico) {
		this.isAutomatico = isAutomatico;
	}

public Long getIdDatiAzione() {
		return idDatiAzione;
	}

	public void setIdDatiAzione(Long idDatiAzione) {
		this.idDatiAzione = idDatiAzione;
	}

public Boolean getIsAzioneConDati() {
		return isAzioneConDati;
	}

	public void setIsAzioneConDati(Boolean isAzioneConDati) {
		this.isAzioneConDati = isAzioneConDati;
	}
	public Long getIdCondition() {
		return idCondition;
	}
	public void setIdCondition(Long idCondition) {
		this.idCondition = idCondition;
	}
	//
	public Stato getStatoPartenza() {
		return statoPartenza;
	}
	public void setStatoPartenza(Stato statoPartenza) {
		this.statoPartenza = statoPartenza;
	}
	public Stato getStatoArrivo() {
		return statoArrivo;
	}
	public void setStatoArrivo(Stato statoArrivo) {
		this.statoArrivo = statoArrivo;
	}

@Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Workflow workflow = (Workflow) o;
    return Objects.equals(idProcesso, workflow.idProcesso) &&
        Objects.equals(idStatoWfPartenza, workflow.idStatoWfPartenza) &&
        Objects.equals(idStatoWfArrivo, workflow.idStatoWfArrivo) &&
        Objects.equals(idAzione, workflow.idAzione);
  }


  @Override
  public int hashCode() {
    return Objects.hash(idWorkflow, idProcesso, idStatoWfPartenza, idStatoWfArrivo, campoCondizione, valoreCondizione, idAzione,  
    		emailDestinatario, idUtenteDestinatario, idTipoUtenteDestinatario, idGruppoUtentiDestinatari, isAnnullabile, isAutomatico);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Workflow {\n");
    
    sb.append("    idWorkflow: ").append(toIndentedString(idWorkflow)).append("\n");
    sb.append("    idProcesso: ").append(toIndentedString(idProcesso)).append("\n");
    sb.append("    idStatoWfPartenza: ").append(toIndentedString(idStatoWfPartenza)).append("\n");
    sb.append("    idStatoWfArrivo: ").append(toIndentedString(idStatoWfArrivo)).append("\n");
    sb.append("    campoCondizione: ").append(toIndentedString(campoCondizione)).append("\n");
    sb.append("    valoreCondizione: ").append(toIndentedString(valoreCondizione)).append("\n");
    sb.append("    idAzione: ").append(toIndentedString(idAzione)).append("\n");
    sb.append("    emailDestinatario: ").append(toIndentedString(emailDestinatario)).append("\n");
    sb.append("    idUtenteDestinatario: ").append(toIndentedString(idUtenteDestinatario)).append("\n");
    sb.append("    idTipoUtenteDestinatario: ").append(toIndentedString(idTipoUtenteDestinatario)).append("\n");
    sb.append("    idGruppoUtentiDestinatari: ").append(toIndentedString(idGruppoUtentiDestinatari)).append("\n");
    sb.append("    isAnnullabile: ").append(toIndentedString(isAnnullabile)).append("\n");
    sb.append("    isAutomatico: ").append(toIndentedString(isAutomatico)).append("\n");
    sb.append("    idCondition: ").append(toIndentedString(idCondition)).append("\n");
    sb.append("    statoPartenza: ").append(toIndentedString(statoPartenza)).append("\n");
    sb.append("    statoArrivo: ").append(toIndentedString(statoArrivo)).append("\n");
    sb.append("    idCondition: ").append(toIndentedString(idCondition)).append("\n");
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

