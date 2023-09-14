/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Optional;

public class MyDocsRichiestaFilter extends PagedFilter {
	
	private Long idRichiesta;
	private String stato;
	private Integer tipoDoc;
	private Long idIstanza;
	private Long idAllegato;
	private Long idFile;
	private Long idModulo;
	private Long idArea;
	private Long idEnte;
	private Long idStoricoWorkflow;
	private String uuidMydocs;
	private String codiceEsito;
	
	public Optional<Long> getIdRichiesta() {
		return Optional.ofNullable(idRichiesta);
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public Optional<String> getStato() {
		return Optional.ofNullable(stato);
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public Optional<Integer> getTipoDoc() {
		return Optional.ofNullable(tipoDoc);
	}
	public void setTipoDoc(Integer tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public Optional<Long> getIdIstanza() {
		return Optional.ofNullable(idIstanza);
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Optional<Long> getIdAllegato() {
		return Optional.ofNullable(idAllegato);
	}
	public void setIdAllegato(Long idAllegato) {
		this.idAllegato = idAllegato;
	}
	public Optional<Long> getIdFile() {
		return Optional.ofNullable(idFile);
	}
	public void setIdFile(Long idFile) {
		this.idFile = idFile;
	}
	public Optional<Long> getIdModulo() {
		return Optional.ofNullable(idModulo);
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Optional<Long> getIdArea() {
		return Optional.ofNullable(idArea);
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Optional<Long> getIdEnte() {
		return Optional.ofNullable(idEnte);
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public Optional<String> getUuidMydocs() {
		return Optional.ofNullable(uuidMydocs);
	}
	public void setUuidMydocse(String uuidMydocs) {
		this.uuidMydocs = uuidMydocs;
	}
	public Optional<String> getCodiceEsito() {
		return Optional.ofNullable(codiceEsito);
	}
	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}
	public Optional<Long> getIdStoricoWorkflow() {
		return Optional.ofNullable(idStoricoWorkflow);
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	
	@Override
	public String toString() {
		return "MyDocsRichiestaFilter [idRichiesta=" + idRichiesta
				+ ", stato=" + stato + ", tipoDoc="
				+ tipoDoc + ", idIstanza=" + idIstanza + ", idAllegato=" + idAllegato + ", idFile=" + idFile
				+ ", idModulo=" + idModulo + ", idArea=" + idArea + ", idEnte=" + idEnte
				+ ", idStoricoWorkflow=" + idStoricoWorkflow
				+ ", uuidMydocs=" + uuidMydocs + ", codiceEsito=" + codiceEsito
				+ "]";
	}
	
}
