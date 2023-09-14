/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Optional;

import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;

/**
 * Filter DTO usato dal EnteDAO per le ricerche degli enti
 * 
 * @see EnteDAO
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class EntiFilter {
	
	private Long idEnte;
	private String codiceEnte;
	private String nomeEnte;
	private String descrizioneEnte;
	private String flAttivo;
	private Integer idTipoEnte;
	private String logo;
	private String nomePortale;
	private Long idPortale;
	private String codiceIpa;
	private String utenteAbilitato;

	public Optional<Long> getIdEnte() {
		return Optional.ofNullable(idEnte);
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public Optional<String> getCodiceEnte() {
		return Optional.ofNullable(codiceEnte);
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public Optional<String> getNomeEnte() {
		return Optional.ofNullable(nomeEnte);
	}
	public void setNomeEnte(String nomeEnte) {
		this.nomeEnte = nomeEnte;
	}
	public Optional<String> getDescrizioneEnte() {
		return Optional.ofNullable(descrizioneEnte);
	}
	public void setDescrizioneEnte(String descrizioneEnte) {
		this.descrizioneEnte = descrizioneEnte;
	}
	public Optional<String> getFlAttivo() {
		return Optional.ofNullable(flAttivo);
	}
	public void setFlAttivo(String flAttivo) {
		this.flAttivo = flAttivo;
	}
	public Optional<Integer> getIdTipoEnte() {
		return Optional.ofNullable(idTipoEnte);
	}
	public void setIdTipoEnte(Integer idTipoEnte) {
		this.idTipoEnte = idTipoEnte;
	}
	public Optional<String> getLogo() {
		return Optional.ofNullable(logo);
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Optional<String> getNomePortale() {
		return Optional.ofNullable(nomePortale);
	}
	public void setNomePortale(String nomePortale) {
		this.nomePortale = nomePortale;
	}
	public Optional<Long> getIdPortale() {
		return Optional.ofNullable(idPortale);
	}
	public void setIdPortale(Long idPortale) {
		this.idPortale = idPortale;
	}
	public Optional<String> getCodiceIpa() {
		return Optional.ofNullable(codiceIpa);
	}
	public void setCodiceIpa(String codiceIpa) {
		this.codiceIpa = codiceIpa;
	}
	public Optional<String> getUtenteAbilitato() {
		return Optional.ofNullable(utenteAbilitato);
	}
	public void setUtenteAbilitato(String utenteAbilitato) {
		this.utenteAbilitato = utenteAbilitato;
	}
	
	@Override
	public String toString() {
		return "EntiFilter [idEnte=" + idEnte + ", codiceEnte=" + codiceEnte + ", nomeEnte=" + nomeEnte
				+ ", descrizioneEnte=" + descrizioneEnte + ", flAttivo=" + flAttivo + ", idTipoEnte=" + idTipoEnte
				+ ", logo=" + logo + ", nomePortale=" + nomePortale + ", idPortale=" + idPortale + ", codiceIpa="
				+ codiceIpa + ", utenteAbilitato=" + utenteAbilitato + "]";
	}
	
}
