/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

public class MyDocsRichiestaEntity {

	public enum Stato {EL, TX, OK, KO};
	public enum Esito {PUB, ERR}
	
	private Long idRichiesta;
	private Date dataRichiesta;
	private String stato;
	private Integer tipoDoc;
	private Long idIstanza;
	private Long idAllegatoIstanza;
	private Long idFile;
	private Long nomeFile;
	private Long idModulo;
	private Long idArea;
	private Long idEnte;
	private Long idStoricoWorkflow;
	private Long idAmbitoMydocs;
	private Long idTipologiaMydocs;
	private String uuidMydocs;
	private String note;
	private String codiceEsito;
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
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public Long getIdAmbitoMydocs() {
		return idAmbitoMydocs;
	}
	public void setIdAmbitoMydocs(Long idAmbitoMydocs) {
		this.idAmbitoMydocs = idAmbitoMydocs;
	}
	public Long getIdTipologiaMydocs() {
		return idTipologiaMydocs;
	}
	public void setIdTipologiaMydocs(Long idTipologiaMydocs) {
		this.idTipologiaMydocs = idTipologiaMydocs;
	}
	public String getUuidMydocs() {
		return uuidMydocs;
	}
	public void setUuidMydocs(String uuidMydocs) {
		this.uuidMydocs = uuidMydocs;
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
	public Long getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(Long nomeFile) {
		this.nomeFile = nomeFile;
	}
	
	@Override
	public String toString() {
		return "ProtocolloRichiestaEntity [idRichiesta=" + idRichiesta + ", dataRichiesta=" + dataRichiesta
				+ ", stato=" + stato
				+ ", tipoDoc=" + tipoDoc + ", idIstanza=" + idIstanza
				+ ", idAllegatoIstanza=" + idAllegatoIstanza + ", idFile=" + idFile + ", nomeFile=" + nomeFile 
				+ ", idModulo=" + idModulo + ", idArea=" + idArea
				+ ", idEnte=" + idEnte + ", idStoricoWorkflow=" + idStoricoWorkflow
				+ ", idAmbitoMydocs=" + idAmbitoMydocs + ", idTipologiaMydocs=" + idTipologiaMydocs
				+ ", uuidMydocs=" + uuidMydocs 
				+ ", note=" + note + ", codiceEsito=" + codiceEsito + ", descEsito=" + descEsito
				+ ", dataUpd=" + dataUpd + ", idStoricoWorkflow=" + idStoricoWorkflow + "]";
	}

	
}
