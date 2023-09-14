/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

/**
 * Entity della tabella dei parametri (configurazione)
 * <br>
 * <br>Tabella moon_c_parametro
 * <br>PK: idParametro
 * <br>AK: codiceComponente,codiceParametro
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ParametroEntity {

	private Long idParametro = null;
	private String codiceComponente = null;
	private String codiceParametro = null;
	private String valore = null;
	
	public Long getIdParametro() {
		return idParametro;
	}
	public void setIdParametro(Long idParametro) {
		this.idParametro = idParametro;
	}
	public String getCodiceComponente() {
		return codiceComponente;
	}
	public void setCodiceComponente(String codiceComponente) {
		this.codiceComponente = codiceComponente;
	}
	public String getCodiceParametro() {
		return codiceParametro;
	}
	public void setCodiceParametro(String codiceParametro) {
		this.codiceParametro = codiceParametro;
	}
	public String getValore() {
		return valore;
	}
	public void setValore(String valore) {
		this.valore = valore;
	}
	
	@Override
	public String toString() {
		return "ParametroEntity [idParametro=" + idParametro + ", codiceComponente=" + codiceComponente
				+ ", codiceParametro=" + codiceParametro + ", valore=" + valore
				+ "]";
	}

}