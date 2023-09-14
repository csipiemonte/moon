/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

public class ProtocolloRichiestaEntity {

	public enum Stato {EL, TX, OK, KO}
	public enum TipoInOut {IN(1), OUT(2);
		private Integer id;
		TipoInOut(Integer id) {this.id=id;}
		public Integer getId() { return id; }
	}
	public enum TipoDoc {
		ISTANZA(1),
		ISTANZA_ALLEGATO(2),
		XML_RESOCONTO(3), 
		INTEGRAZIONE(4), 
		INTEGRAZIONE_ALLEGATO(5),
		RICEVUTA(6), 
		ADDITIONAL_ALLEGATO(7),
		;
		private Integer id;
		TipoDoc(Integer id) {this.id=id;}
		public Integer getId() { return id; }
		public boolean isCorrectId(Integer idTipoDocToCompare) {
			if (idTipoDocToCompare==null) return false;
			if (getId().equals(idTipoDocToCompare))
				return true;
			return false;
		}
		public boolean isCorrect(ProtocolloRichiestaEntity richiestaToCompare) {
			if (richiestaToCompare==null) return false;
			return isCorrectId(richiestaToCompare.getTipoDoc());
		}
	}
	public enum Protocollatore {
		STARDAS(1),
		MAGGIOLI_SOAP(2),
		;
		private Integer id;
		Protocollatore(Integer id) {this.id=id;}
		public Integer getId() { return id; }
	}
	
	private Long idRichiesta;
	private Date dataRichiesta;
	private String codiceRichiesta;
	private String uuidRichiesta;
	private String stato;
	private Integer tipoIngUsc;
	private Integer tipoDoc;
	private Long idIstanza;
	private Long idAllegatoIstanza;
	private Long idFile;
	private Long idModulo;
	private Long idArea;
	private Long idEnte;
	private Integer idProtocollatore;
	private String uuidProtocollatore;
	private String note;
	private String codiceEsito;
	private String descEsito;
	private Date dataUpd;
	private Long idStoricoWorkflow;
	
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
	public Integer getTipoIngUsc() {
		return tipoIngUsc;
	}
	public void setTipoIngUsc(Integer tipoIngUsc) {
		this.tipoIngUsc = tipoIngUsc;
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
	public Integer getIdProtocollatore() {
		return idProtocollatore;
	}
	public void setIdProtocollatore(Integer idProtocollatore) {
		this.idProtocollatore = idProtocollatore;
	}
	public String getUuidProtocollatore() {
		return uuidProtocollatore;
	}
	public void setUuidProtocollatore(String uuidProtocollatore) {
		this.uuidProtocollatore = uuidProtocollatore;
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
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	
	@Override
	public String toString() {
		return "ProtocolloRichiestaEntity [idRichiesta=" + idRichiesta + ", dataRichiesta=" + dataRichiesta
				+ ", codiceRichiesta=" + codiceRichiesta + ", uuidRichiesta=" + uuidRichiesta + ", stato=" + stato
				+ ", tipoIngUsc=" + tipoIngUsc + ", tipoDoc=" + tipoDoc + ", idIstanza=" + idIstanza
				+ ", idAllegatoIstanza=" + idAllegatoIstanza + ", idFile=" + idFile 
				+ ", idModulo=" + idModulo + ", idArea=" + idArea
				+ ", idEnte=" + idEnte + ", idProtocollatore=" + idProtocollatore + ", uuidProtocollatore="
				+ uuidProtocollatore + ", note=" + note + ", codiceEsito=" + codiceEsito + ", descEsito=" + descEsito
				+ ", dataUpd=" + dataUpd + ", idStoricoWorkflow=" + idStoricoWorkflow + "]";
	}
	
}
