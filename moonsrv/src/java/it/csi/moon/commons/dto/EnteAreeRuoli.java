/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.io.Serializable;
import java.util.List;

/*
 * EnteAreeRuoli per la login
 * 
 */
public class EnteAreeRuoli implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	public String toString() {
		return "EnteAreeRuoli [idEnte=" + idEnte + ", codiceEnte=" + codiceEnte + ", nomeEnte=" + nomeEnte
				+ ", descrizioneEnte=" + descrizioneEnte + ", idTipoEnte=" + idTipoEnte + ", areeRuoli=" + areeRuoli
				+ "]";
	}

}
