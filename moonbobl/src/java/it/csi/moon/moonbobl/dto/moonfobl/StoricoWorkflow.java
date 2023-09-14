/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Date;

public class StoricoWorkflow {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [implicit-camel-case]

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
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contieneDati == null) ? 0 : contieneDati.hashCode());
		result = prime * result + ((contieneOutput == null) ? 0 : contieneOutput.hashCode());
		result = prime * result + ((dataFine == null) ? 0 : dataFine.hashCode());
		result = prime * result + ((dataInizio == null) ? 0 : dataInizio.hashCode());
		result = prime * result + ((datiAzione == null) ? 0 : datiAzione.hashCode());
		result = prime * result + ((descDestinatario == null) ? 0 : descDestinatario.hashCode());
		result = prime * result + ((idAzione == null) ? 0 : idAzione.hashCode());
		result = prime * result + ((idFileRendering == null) ? 0 : idFileRendering.hashCode());
		result = prime * result + ((idIstanza == null) ? 0 : idIstanza.hashCode());
		result = prime * result + ((idProcesso == null) ? 0 : idProcesso.hashCode());
		result = prime * result + ((idStatoWfArrivo == null) ? 0 : idStatoWfArrivo.hashCode());
		result = prime * result + ((idStatoWfPartenza == null) ? 0 : idStatoWfPartenza.hashCode());
		result = prime * result + ((idStoricoWorkflow == null) ? 0 : idStoricoWorkflow.hashCode());
		result = prime * result + ((nomeAzione == null) ? 0 : nomeAzione.hashCode());
		result = prime * result + ((nomeStatoWfArrivo == null) ? 0 : nomeStatoWfArrivo.hashCode());
		result = prime * result + ((nomeStatoWfPartenza == null) ? 0 : nomeStatoWfPartenza.hashCode());
		result = prime * result + ((strutturaDatiAzione == null) ? 0 : strutturaDatiAzione.hashCode());
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
		StoricoWorkflow other = (StoricoWorkflow) obj;
		if (contieneDati == null) {
			if (other.contieneDati != null)
				return false;
		} else if (!contieneDati.equals(other.contieneDati))
			return false;
		if (contieneOutput == null) {
			if (other.contieneOutput != null)
				return false;
		} else if (!contieneOutput.equals(other.contieneOutput))
			return false;
		if (dataFine == null) {
			if (other.dataFine != null)
				return false;
		} else if (!dataFine.equals(other.dataFine))
			return false;
		if (dataInizio == null) {
			if (other.dataInizio != null)
				return false;
		} else if (!dataInizio.equals(other.dataInizio))
			return false;
		if (datiAzione == null) {
			if (other.datiAzione != null)
				return false;
		} else if (!datiAzione.equals(other.datiAzione))
			return false;
		if (descDestinatario == null) {
			if (other.descDestinatario != null)
				return false;
		} else if (!descDestinatario.equals(other.descDestinatario))
			return false;
		if (idAzione == null) {
			if (other.idAzione != null)
				return false;
		} else if (!idAzione.equals(other.idAzione))
			return false;
		if (idFileRendering == null) {
			if (other.idFileRendering != null)
				return false;
		} else if (!idFileRendering.equals(other.idFileRendering))
			return false;
		if (idIstanza == null) {
			if (other.idIstanza != null)
				return false;
		} else if (!idIstanza.equals(other.idIstanza))
			return false;
		if (idProcesso == null) {
			if (other.idProcesso != null)
				return false;
		} else if (!idProcesso.equals(other.idProcesso))
			return false;
		if (idStatoWfArrivo == null) {
			if (other.idStatoWfArrivo != null)
				return false;
		} else if (!idStatoWfArrivo.equals(other.idStatoWfArrivo))
			return false;
		if (idStatoWfPartenza == null) {
			if (other.idStatoWfPartenza != null)
				return false;
		} else if (!idStatoWfPartenza.equals(other.idStatoWfPartenza))
			return false;
		if (idStoricoWorkflow == null) {
			if (other.idStoricoWorkflow != null)
				return false;
		} else if (!idStoricoWorkflow.equals(other.idStoricoWorkflow))
			return false;
		if (nomeAzione == null) {
			if (other.nomeAzione != null)
				return false;
		} else if (!nomeAzione.equals(other.nomeAzione))
			return false;
		if (nomeStatoWfArrivo == null) {
			if (other.nomeStatoWfArrivo != null)
				return false;
		} else if (!nomeStatoWfArrivo.equals(other.nomeStatoWfArrivo))
			return false;
		if (nomeStatoWfPartenza == null) {
			if (other.nomeStatoWfPartenza != null)
				return false;
		} else if (!nomeStatoWfPartenza.equals(other.nomeStatoWfPartenza))
			return false;
		if (strutturaDatiAzione == null) {
			if (other.strutturaDatiAzione != null)
				return false;
		} else if (!strutturaDatiAzione.equals(other.strutturaDatiAzione))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StoricoWorkflow [idStoricoWorkflow=" + idStoricoWorkflow + ", idIstanza=" + idIstanza + ", idProcesso="
				+ idProcesso + ", idStatoWfPartenza=" + idStatoWfPartenza + ", idStatoWfArrivo=" + idStatoWfArrivo
				+ ", nomeStatoWfPartenza=" + nomeStatoWfPartenza + ", nomeStatoWfArrivo=" + nomeStatoWfArrivo
				+ ", idAzione=" + idAzione + ", nomeAzione=" + nomeAzione + ", datiAzione=" + datiAzione
				+ ", descDestinatario=" + descDestinatario + ", dataInizio=" + dataInizio + ", dataFine=" + dataFine
				+ ", contieneDati=" + contieneDati + ", contieneOutput=" + contieneOutput + ", strutturaDatiAzione="
				+ strutturaDatiAzione + ", idFileRendering=" + idFileRendering + "]";
	}

}
