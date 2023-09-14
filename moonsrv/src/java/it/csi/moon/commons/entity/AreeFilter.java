/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Optional;

/**
 * Filter DTO usato dal AreaDAO per le ricerche delle aree
 * 
 * @see AreaDAO
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class AreeFilter {
	
	private Long idArea;
	private String codiceArea;
	private String nomeArea;
	private Long idEnte;

	public Optional<Long> getIdArea() {
		return Optional.ofNullable(idArea);
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Optional<String> getCodiceArea() {
		return Optional.ofNullable(codiceArea);
	}
	public void setCodiceArea(String codiceArea) {
		this.codiceArea = codiceArea;
	}
	public Optional<String> getNomeArea() {
		return Optional.ofNullable(nomeArea);
	}
	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
	}
	public Optional<Long> getIdEnte() {
		return Optional.ofNullable(idEnte);
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}

	
	@Override
	public String toString() {
		return "AreeFilter [idArea=" + idArea + ", codiceArea=" + codiceArea + ", nomeArea=" + nomeArea
				+ ", idEnte=" + idEnte + "]";
	}
	
}
