/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dto;

public class TplProprietarioEntity {

	Integer idProprietario;
	String cfProprietario;
	
	public final Integer getIdProprietario() {
		return idProprietario;
	}
	public final String getCfProprietario() {
		return cfProprietario;
	}
	public final void setIdProprietario(Integer idProprietario) {
		this.idProprietario = idProprietario;
	}
	public final void setCfProprietario(String cfProprietario) {
		this.cfProprietario = cfProprietario;
	}
	
	@Override
	public String toString() {
		return "TplProprietarioEntity [idProprietario=" + idProprietario + ", cfProprietario=" + cfProprietario + "]";
	}
	
}
