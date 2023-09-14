/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import it.csi.moon.commons.util.decodifica.DecodificaTipoStruttura;

/**
 * Entity della tabella della struttura effettiva del modulo gestito da FormIO 
 * <br>Un solo record presente per ogni modulo
 * <br>
 * <br>Tabella moon_io_d_modulostruttura
 * <br>PK: idModuloStruttura
 * <br>AK: idVersioneModulo
 * 
 * @see ModuloEntity
 * @see DecodificaTipoStruttura
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloStrutturaEntity {
	
	private Long idModuloStruttura;
	private Long idVersioneModulo;
	private Long idModulo;
	private String struttura;
	private String tipoStruttura; // FRM, WIZ
	
	public Long getIdModuloStruttura() {
		return idModuloStruttura;
	}
	public void setIdModuloStruttura(Long idModuloStruttura) {
		this.idModuloStruttura = idModuloStruttura;
	}
	public Long getIdVersioneModulo() {
		return idVersioneModulo;
	}
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public String getStruttura() {
		return struttura;
	}
	public void setStruttura(String struttura) {
		this.struttura = struttura;
	}
	public String getTipoStruttura() {
		return tipoStruttura;
	}
	public void setTipoStruttura(String tipoStruttura) {
		this.tipoStruttura = tipoStruttura;
	}

}
