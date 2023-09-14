/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * AreaRuolo abilitato e attivi in un ente per un utente
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class AreaRuolo {

	private Long idArea = null;
	private String codiceArea = null;
	private String nomeArea = null;
	private Integer idRuolo = null;
	private String codiceRuolo = null;
	private String nomeRuolo = null;
	// UAR
	private Date dataUpdAbilitazione = null;
	private String attoreUpdAbilitazione = null;
	
	public AreaRuolo() {	
	}

	public AreaRuolo(Long idArea, String codiceArea, String nomeArea, Integer idRuolo, String codiceRuolo, String nomeRuolo) {
		super();
		this.idArea = idArea;
		this.codiceArea = codiceArea;
		this.nomeArea = nomeArea;
		this.idRuolo = idRuolo;
		this.codiceRuolo = codiceRuolo;
		this.nomeRuolo = nomeRuolo;
	}

	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public String getCodiceArea() {
		return codiceArea;
	}
	public void setCodiceArea(String codiceArea) {
		this.codiceArea = codiceArea;
	}
	public String getNomeArea() {
		return nomeArea;
	}
	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
	}
	public Integer getIdRuolo() {
		return idRuolo;
	}
	public void setIdRuolo(Integer idRuolo) {
		this.idRuolo = idRuolo;
	}
	public String getCodiceRuolo() {
		return codiceRuolo;
	}
	public void setCodiceRuolo(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}
	public String getNomeRuolo() {
		return nomeRuolo;
	}
	public void setNomeRuolo(String nomeRuolo) {
		this.nomeRuolo = nomeRuolo;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataUpdAbilitazione() {
		return dataUpdAbilitazione;
	}
	public void setDataUpdAbilitazione(Date dataUpdAbilitazione) {
		this.dataUpdAbilitazione = dataUpdAbilitazione;
	}
	public String getAttoreUpdAbilitazione() {
		return attoreUpdAbilitazione;
	}
	public void setAttoreUpdAbilitazione(String attoreUpdAbilitazione) {
		this.attoreUpdAbilitazione = attoreUpdAbilitazione;
	}
	
	@Override
	public String toString() {
		return "AreaRuolo [idArea=" + idArea + ", codiceArea=" + codiceArea + ", nomeArea=" + nomeArea + ", idRuolo="
			+ idRuolo + ", codiceRuolo=" + codiceRuolo + ", nomeRuolo=" + nomeRuolo
			+ ", dataUpdAbilitazione=" + dataUpdAbilitazione + ", attoreUpdAbilitazione=" + attoreUpdAbilitazione + "]";
	}
	
}
