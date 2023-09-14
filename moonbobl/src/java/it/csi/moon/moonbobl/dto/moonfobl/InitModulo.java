/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.List;


/**
 * InitModulo
 * 
 * @author Laurent
 *
 */
public class InitModulo   {

	private Long idModulo = null;
	private String codiceModulo = null;

	private List<InitModuloVersione> versioni = null;

	private String ultimaVersione = null;
	private Integer ultimaMaggioreVersione = null;
	private Integer ultimaMinoreVersione = null;
	private Integer ultimaPatchVersione = null;
	
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public String getCodiceModulo() {
		return codiceModulo;
	}
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
	public List<InitModuloVersione> getVersioni() {
		return versioni;
	}
	public void setVersioni(List<InitModuloVersione> versioni) {
		this.versioni = versioni;
	}
	public String getUltimaVersione() {
		return ultimaVersione;
	}
	public void setUltimaVersione(String ultimaVersione) {
		this.ultimaVersione = ultimaVersione;
	}
	public Integer getUltimaMaggioreVersione() {
		return ultimaMaggioreVersione;
	}
	public void setUltimaMaggioreVersione(Integer ultimaMaggioreVersione) {
		this.ultimaMaggioreVersione = ultimaMaggioreVersione;
	}
	public Integer getUltimaMinoreVersione() {
		return ultimaMinoreVersione;
	}
	public void setUltimaMinoreVersione(Integer ultimaMinoreVersione) {
		this.ultimaMinoreVersione = ultimaMinoreVersione;
	}
	public Integer getUltimaPatchVersione() {
		return ultimaPatchVersione;
	}
	public void setUltimaPatchVersione(Integer ultimaPatchVersione) {
		this.ultimaPatchVersione = ultimaPatchVersione;
	}

}

