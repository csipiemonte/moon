/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Objects;

public class Azione   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
	
	private Long idAzione = null;
	private String codiceAzione = null;
	private String nomeAzione = null;
	private String descAzione = null;
	//
	private Long idWorkflow = null;
	private Long idIstanza = null;
	private String datiAzione = null;
	private Long idStoricoWorkflow = null;
	private String descDestinatario = null;
	private String codEsitoAzione = null;
	private String descEsitoAzione = null;

	//
	public Long getIdAzione() {
		return idAzione;
	}
	public void setIdAzione(Long idAzione) {
		this.idAzione = idAzione;
	}
	public String getCodiceAzione() {
		return codiceAzione;
	}
	public void setCodiceAzione(String codiceAzione) {
		this.codiceAzione = codiceAzione;
	}
	public String getNomeAzione() {
		return nomeAzione;
	}
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}
	public String getDescAzione() {
		return descAzione;
	}
	public void setDescAzione(String descAzione) {
		this.descAzione = descAzione;
	}

	//
	public Long getIdWorkflow() {
		return idWorkflow;
	}
	public void setIdWorkflow(Long idWorkflow) {
		this.idWorkflow = idWorkflow;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public String getDatiAzione() {
		return datiAzione;
	}
	public void setDatiAzione(String datiAzione) {
		this.datiAzione = datiAzione;
	}
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public String getDescDestinatario() {
		return descDestinatario;
	}
	public void setDescDestinatario(String descDestinatario) {
		this.descDestinatario = descDestinatario;
	}
	public String getCodEsitoAzione() {
		return codEsitoAzione;
	}
	public void setCodEsitoAzione(String codEsitoAzione) {
		this.codEsitoAzione = codEsitoAzione;
	}
	public String getDescEsitoAzione() {
		return descEsitoAzione;
	}
	public void setDescEsitoAzione(String descEsitoAzione) {
		this.descEsitoAzione = descEsitoAzione;
	}


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Azione obj = (Azione) o;
    return 
    	Objects.equals(idIstanza, obj.idIstanza) &&
    	Objects.equals(idWorkflow, obj.idWorkflow) &&
        Objects.equals(idAzione, obj.idAzione) &&
        Objects.equals(codiceAzione, obj.codiceAzione) ;
  }


  @Override
  public int hashCode() {
    return Objects.hash(idWorkflow, idAzione, nomeAzione, datiAzione, descDestinatario);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Azione {\n");
    sb.append("    idWorkflow: ").append(toIndentedString(idWorkflow)).append("\n");
    sb.append("    idAzione: ").append(toIndentedString(idAzione)).append("\n");
    sb.append("    codiceAzione: ").append(toIndentedString(codiceAzione)).append("\n");
    sb.append("    nomeAzione: ").append(toIndentedString(nomeAzione)).append("\n");
    sb.append("    datiAzione: ").append(toIndentedString(datiAzione)).append("\n");
    sb.append("    descDestinatario: ").append(toIndentedString(descDestinatario)).append("\n");
    sb.append("    idStoricoWorkflow: ").append(toIndentedString(idStoricoWorkflow)).append("\n");
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

