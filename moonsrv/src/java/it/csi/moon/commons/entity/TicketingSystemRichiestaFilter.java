/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Optional;

public class TicketingSystemRichiestaFilter extends PagedFilter {
	
	private Long idRichiesta;
	private String codiceRichiesta;
	private String uuidRichiesta;
	private String stato;
	private Integer tipoDoc;
	private Long idIstanza;
	private Long idAllegato;
	private Long idFile;
	private Long idModulo;
	private Long idArea;
	private Long idEnte;
	private Integer idTicketingSystem;
	private String uuidTicketingSystem;
	private String codiceEsito;
	
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
	public Optional<Integer> getIdTicketingSystem() {
		return Optional.ofNullable(idTicketingSystem);
	}
	public void setIdTicketingSystem(Integer idTicketingSystem) {
		this.idTicketingSystem = idTicketingSystem;
	}
	public Optional<String> getUuidTicketingSystem() {
		return Optional.ofNullable(uuidTicketingSystem);
	}
	public void setUuidTicketingSystem(String uuidTicketingSystem) {
		this.uuidTicketingSystem = uuidTicketingSystem;
	}
	public Optional<String> getCodiceEsito() {
		return Optional.ofNullable(codiceEsito);
	}
	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}
	
	@Override
	public String toString() {
		return "TicketingSystemRichiestaFilter [idRichiesta=" + idRichiesta + ", codiceRichiesta=" + codiceRichiesta
				+ ", uuidRichiesta=" + uuidRichiesta + ", stato=" + stato + ", tipoDoc=" + tipoDoc 
				+ ", idIstanza=" + idIstanza + ", idAllegato=" + idAllegato + ", idFile=" + idFile
				+ ", idModulo=" + idModulo + ", idArea=" + idArea + ", idEnte=" + idEnte + ", idTicketingSystem="
				+ idTicketingSystem + ", uuidTicketingSystem=" + uuidTicketingSystem + ", codiceEsito=" + codiceEsito
				+ "]";
	}
	
}
