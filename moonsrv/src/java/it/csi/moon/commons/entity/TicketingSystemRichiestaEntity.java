/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

public class TicketingSystemRichiestaEntity {

	public enum Stato {EL, TX, OK, KO}
	public enum TipoDoc {
		ISTANZA(1),
		ISTANZA_ALLEGATO(2),
		;
		private Integer id;
		TipoDoc(Integer id) {this.id=id;}
		public Integer getId() { return id; }
	}
	public enum TicketingSystem {
		NEXTCRM(1),
		R2U(2),
		OTRS(3),
		;
		private Integer id;
		TicketingSystem(Integer id) {this.id=id;}
		public Integer getId() { return id; }
	}
	
	private Long idRichiesta;
	private Date dataRichiesta;
	private String codiceRichiesta;
	private String uuidRichiesta;
	private String stato;
	private Integer tipoDoc;
	private Long idIstanza;
	private Long idAllegatoIstanza;
	private Long idFile;
	private Long idModulo;
	private Long idArea;
	private Long idEnte;
	private Integer idTicketingSystem; // FROM enum TicketingSystem.getId()
	private String uuidTicketingSystem;
	private String note;
	private String codiceEsito; // 201
	private String descEsito;
	private Date dataUpd;
	
	public Long getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public Date getDataRichiesta() {
		return dataRichiesta;
	}
	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}
	public String getCodiceRichiesta() {
		return codiceRichiesta;
	}
	public void setCodiceRichiesta(String codiceRichiesta) {
		this.codiceRichiesta = codiceRichiesta;
	}
	public String getUuidRichiesta() {
		return uuidRichiesta;
	}
	public void setUuidRichiesta(String uuidRichiesta) {
		this.uuidRichiesta = uuidRichiesta;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public Integer getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(Integer tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public Long getIdAllegatoIstanza() {
		return idAllegatoIstanza;
	}
	public void setIdAllegatoIstanza(Long idAllegatoIstanza) {
		this.idAllegatoIstanza = idAllegatoIstanza;
	}
	public Long getIdFile() {
		return idFile;
	}
	public void setIdFile(Long idFile) {
		this.idFile = idFile;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public Integer getIdTicketingSystem() {
		return idTicketingSystem;
	}
	public void setIdTicketingSystem(Integer idTicketingSystem) {
		this.idTicketingSystem = idTicketingSystem;
	}
	public String getUuidTicketingSystem() {
		return uuidTicketingSystem;
	}
	public void setUuidTicketingSystem(String uuidTicketingSystem) {
		this.uuidTicketingSystem = uuidTicketingSystem;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCodiceEsito() {
		return codiceEsito;
	}
	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}
	public String getDescEsito() {
		return descEsito;
	}
	public void setDescEsito(String descEsito) {
		this.descEsito = descEsito;
	}
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}
	
	@Override
	public String toString() {
		return "TicketingSystemRichiestaEntity [idRichiesta=" + idRichiesta + ", dataRichiesta=" + dataRichiesta
				+ ", codiceRichiesta=" + codiceRichiesta + ", uuidRichiesta=" + uuidRichiesta + ", stato=" + stato
				+ ", tipoDoc=" + tipoDoc + ", idIstanza=" + idIstanza
				+ ", idAllegatoIstanza=" + idAllegatoIstanza + ", idFile=" + idFile 
				+ ", idModulo=" + idModulo + ", idArea=" + idArea + ", idEnte=" + idEnte
				+ ", idTicketingSystem=" + idTicketingSystem + ", uuidTicketingSystem=" + uuidTicketingSystem 
				+ ", note=" + note + ", codiceEsito=" + codiceEsito + ", descEsito=" + descEsito
				+ ", dataUpd=" + dataUpd + "]";
	}
	
}
