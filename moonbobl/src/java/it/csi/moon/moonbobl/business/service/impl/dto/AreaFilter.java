/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Optional;

import it.csi.moon.moonbobl.business.service.impl.dao.AreaDAO;

/**
 * Filter DTO usato dal AreaDAO per le ricerche delle aree
 * 
 * @see AreaDAO
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class AreaFilter {

	private Long idArea;
	private Long idEnte;
	private String codiceArea;
	private String nomeArea;
	
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

	@Override
	public String toString() {
		return "AreaFilter [idArea=" + idArea + ", idEnte=" + idEnte + ", codiceArea=" + codiceArea + ", nomeArea="
				+ nomeArea + "]";
	}
	
}
