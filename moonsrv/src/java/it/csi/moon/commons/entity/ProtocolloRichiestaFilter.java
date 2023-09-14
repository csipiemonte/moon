/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Optional;

public class ProtocolloRichiestaFilter extends PagedFilter {
	
	private Long idRichiesta;
	private String codiceRichiesta;
	private String uuidRichiesta;
	private String stato;
	private Integer tipoIngUsc;
	private Integer tipoDoc;
	private Long idIstanza;
	private Long idAllegato;
	private Long idFile;
	private Long idModulo;
	private Long idArea;
	private Long idEnte;
	private Integer idProtocollatore;
	private String uuidProtocollatore;
	private String codiceEsito;
	private Long idStoricoWorkflow;
	
	public Optional<Long> getIdRichiesta() {
		return Optional.ofNullable(idRichiesta);
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public Optional<String> getCodiceRichiesta() {
		return Optional.ofNullable(codiceRichiesta);
	}
	public void setCodiceRichiesta(String codiceRichiesta) {
		this.codiceRichiesta = codiceRichiesta;
	}
	public Optional<String> getUuidRichiesta() {
		return Optional.ofNullable(uuidRichiesta);
	}
	public void setUuidRichiesta(String uuidRichiesta) {
		this.uuidRichiesta = uuidRichiesta;
	}
	public Optional<String> getStato() {
		return Optional.ofNullable(stato);
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public Optional<Integer> getTipoIngUsc() {
		return Optional.ofNullable(tipoIngUsc);
	}
	public void setTipoIngUsc(Integer tipoIngUsc) {
		this.tipoIngUsc = tipoIngUsc;
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
	public Optional<Integer> getIdProtocollatore() {
		return Optional.ofNullable(idProtocollatore);
	}
	public void setIdProtocollatore(Integer idProtocollatore) {
		this.idProtocollatore = idProtocollatore;
	}
	public Optional<String> getUuidProtocollatore() {
		return Optional.ofNullable(uuidProtocollatore);
	}
	public void setUuidProtocollatore(String uuidProtocollatore) {
		this.uuidProtocollatore = uuidProtocollatore;
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
		return "ProtocolloRichiestaFilter [idRichiesta=" + idRichiesta + ", codiceRichiesta=" + codiceRichiesta
				+ ", uuidRichiesta=" + uuidRichiesta + ", stato=" + stato + ", tipoIngUsc=" + tipoIngUsc + ", tipoDoc="
				+ tipoDoc + ", idIstanza=" + idIstanza + ", idAllegato=" + idAllegato + ", idFile=" + idFile
				+ ", idModulo=" + idModulo + ", idArea=" + idArea + ", idEnte=" + idEnte + ", idProtocollatore="
				+ idProtocollatore + ", uuidProtocollatore=" + uuidProtocollatore + ", codiceEsito=" + codiceEsito
				+ ", idStoricoWorkflow=" + idStoricoWorkflow + "]";
	}
	
}
