/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Optional;

import it.csi.moon.moonbobl.business.service.impl.dao.StatoDAO;

/**
 * Filter DTO usato dal StatoDAO per le ricerche degli stati
 * 
 * @see StatoDAO
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class StatiFilter {

	private Long idModulo;
	
	public Optional<Long> getIdModulo() {
		return Optional.ofNullable(idModulo);
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}


	@Override
	public String toString() {
		return "StatiFilter [idModulo=" + idModulo + "]";
	}
	
}
