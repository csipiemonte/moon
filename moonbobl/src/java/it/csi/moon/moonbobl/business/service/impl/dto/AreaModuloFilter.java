/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Optional;


/**
 * Filter DTO usato dal AreaModuloDAO per le ricerche delle collocazione di un modulo nell'area di un ente
 * 
 * @see AreaModuloDAO
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class AreaModuloFilter extends PagedFilter {

	private Long idArea;
	private Long idModulo;
	private Long idEnte;
	
	public Optional<Long> getIdArea() {
		return Optional.ofNullable(idArea);
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Optional<Long> getIdModulo() {
		return Optional.ofNullable(idModulo);
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Optional<Long> getIdEnte() {
		return Optional.ofNullable(idEnte);
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	
	@Override
	public String toString() {
		return "IstanzeFilter [idArea=" + idArea + ", idModulo=" + idModulo + ", idEnte=" + idEnte + "]";
	}
}
