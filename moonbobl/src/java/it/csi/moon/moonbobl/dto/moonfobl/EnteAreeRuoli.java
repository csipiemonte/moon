/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.List;

/*
 * EnteAreeRuoli per la login
 * 
 */
public class EnteAreeRuoli {
	
	private Long idEnte;
	private String codiceEnte;
	private String nomeEnte;
	private String descrizioneEnte;
	private Integer idTipoEnte;
	private List<AreaRuolo> areeRuoli;
	
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public String getNomeEnte() {
		return nomeEnte;
	}
	public void setNomeEnte(String nomeEnte) {
		this.nomeEnte = nomeEnte;
	}
	public String getDescrizioneEnte() {
		return descrizioneEnte;
	}
	public void setDescrizioneEnte(String descrizioneEnte) {
		this.descrizioneEnte = descrizioneEnte;
	}
	public Integer getIdTipoEnte() {
		return idTipoEnte;
	}
	public void setIdTipoEnte(Integer idTipoEnte) {
		this.idTipoEnte = idTipoEnte;
	}
	public List<AreaRuolo> getAreeRuoli() {
		return areeRuoli;
	}
	public void setAreeRuoli(List<AreaRuolo> areeRuoli) {
		this.areeRuoli = areeRuoli;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areeRuoli == null) ? 0 : areeRuoli.hashCode());
		result = prime * result + ((codiceEnte == null) ? 0 : codiceEnte.hashCode());
		result = prime * result + ((descrizioneEnte == null) ? 0 : descrizioneEnte.hashCode());
		result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
		result = prime * result + ((idTipoEnte == null) ? 0 : idTipoEnte.hashCode());
		result = prime * result + ((nomeEnte == null) ? 0 : nomeEnte.hashCode());
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
		EnteAreeRuoli other = (EnteAreeRuoli) obj;
		if (areeRuoli == null) {
			if (other.areeRuoli != null)
				return false;
		} else if (!areeRuoli.equals(other.areeRuoli))
			return false;
		if (codiceEnte == null) {
			if (other.codiceEnte != null)
				return false;
		} else if (!codiceEnte.equals(other.codiceEnte))
			return false;
		if (descrizioneEnte == null) {
			if (other.descrizioneEnte != null)
				return false;
		} else if (!descrizioneEnte.equals(other.descrizioneEnte))
			return false;
		if (idEnte == null) {
			if (other.idEnte != null)
				return false;
		} else if (!idEnte.equals(other.idEnte))
			return false;
		if (idTipoEnte == null) {
			if (other.idTipoEnte != null)
				return false;
		} else if (!idTipoEnte.equals(other.idTipoEnte))
			return false;
		if (nomeEnte == null) {
			if (other.nomeEnte != null)
				return false;
		} else if (!nomeEnte.equals(other.nomeEnte))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "EnteAreeRuoli [idEnte=" + idEnte + ", codiceEnte=" + codiceEnte + ", nomeEnte=" + nomeEnte
				+ ", descrizioneEnte=" + descrizioneEnte + ", idTipoEnte=" + idTipoEnte + ", areeRuoli=" + areeRuoli
				+ "]";
	}

}
