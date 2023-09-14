/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

/**
 * Entity della tabelladi relazione portale modulo
 * <br>
 * <br>Tabella moon_fo_r_portale_modulo
 * <br>PK: idPortale idModulo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ProcessoModuloEntity {

	private Long idModulo;
	private Long idProcesso;
	private Integer ordine;
	
	public ProcessoModuloEntity() {
		super();
	}
	
	public ProcessoModuloEntity(Long idModulo, Long idProcesso) {
		super();
		this.idModulo = idModulo;
		this.idProcesso = idProcesso;
		this.ordine = 1;
	}

	public Long getIdProcesso() {
		return idProcesso;
	}
	public void setIdPortale(Long idProcesso) {
		this.idProcesso = idProcesso;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Integer getOrdine() {
		return ordine;
	}
	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}

	@Override
	public String toString() {
		return "ProcessoModuloEntity [idModulo=" + idModulo + ", idProcesso=" + idProcesso + ", ordine=" + ordine + "]";
	}

}
