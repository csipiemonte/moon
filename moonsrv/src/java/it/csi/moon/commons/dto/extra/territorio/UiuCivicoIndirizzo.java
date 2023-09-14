/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.territorio;

/**
 * UiuCivicoIndirizzo della toponomastica del comune di Torino, risulatato di ricerca Catastali
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class UiuCivicoIndirizzo {
	
	  private Integer idUiu = null;
	  private Integer idCivico = null;
	  private String indirizzoCompleto = null;
	
	public UiuCivicoIndirizzo() {
		super();
	}
	public UiuCivicoIndirizzo(Integer idUiu, Integer idCivico, String indirizzoCompleto) {
		super();
		this.idUiu = idUiu;
		this.idCivico = idCivico;
		this.indirizzoCompleto = indirizzoCompleto;
	}

	public Integer getIdUiu() {
		return idUiu;
	}
	public void setIdUiu(Integer idUiu) {
		this.idUiu = idUiu;
	}
	public Integer getIdCivico() {
		return idCivico;
	}
	public void setIdCivico(Integer idCivico) {
		this.idCivico = idCivico;
	}
	public String getIndirizzoCompleto() {
		return indirizzoCompleto;
	}
	public void setIndirizzoCompleto(String indirizzoCompleto) {
		this.indirizzoCompleto = indirizzoCompleto;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idUiu == null) ? 0 : idUiu.hashCode());
		result = prime * result + ((idCivico == null) ? 0 : idCivico.hashCode());
		result = prime * result + ((indirizzoCompleto == null) ? 0 : indirizzoCompleto.hashCode());
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
		UiuCivicoIndirizzo other = (UiuCivicoIndirizzo) obj;
		if (idUiu == null) {
			if (other.idUiu != null)
				return false;
		} else if (!idUiu.equals(other.idUiu))
			return false;
		if (idCivico == null) {
			if (other.idCivico != null)
				return false;
		} else if (!idCivico.equals(other.idCivico))
			return false;
		if (indirizzoCompleto == null) {
			if (other.indirizzoCompleto != null)
				return false;
		} else if (!indirizzoCompleto.equals(other.indirizzoCompleto))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "UiuCivicoIndirizzo [idUiu=" + idUiu + ", idCivico=" + idCivico + ", indirizzoCompleto=" + indirizzoCompleto + "]";
	}
	
}
