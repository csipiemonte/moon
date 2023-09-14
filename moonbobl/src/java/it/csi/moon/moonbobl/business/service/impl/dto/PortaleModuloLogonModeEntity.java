/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

/**
 * Entity della tabella di accesso a modulistica
 * <br>
 * <br>Tabella moon_ml_r_portale_modulo_logon_mode
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class PortaleModuloLogonModeEntity {

	private Long idPortale;
	private Long idModulo;
	private Integer idLogonMode;
	private String filtro;
	
	public Long getIdPortale() {
		return idPortale;
	}
	public void setIdPortale(Long idPortale) {
		this.idPortale = idPortale;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Integer getIdLogonMode() {
		return idLogonMode;
	}
	public void setIdLogonMode(Integer idLogonMode) {
		this.idLogonMode = idLogonMode;
	}
	public String getFiltro() {
		return filtro;
	}
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	
	@Override
	public String toString() {
		return "PortaleModuloLogonModeEntity [idPortale=" + idPortale + ", idModulo=" + idModulo + ", idLogonMode="
				+ idLogonMode + ", filtro=" + filtro + "]";
	}
	
}
