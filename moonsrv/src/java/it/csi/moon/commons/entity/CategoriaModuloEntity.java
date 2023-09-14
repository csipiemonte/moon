/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/*
 * Entity relativo alla tabella moon_fo_r_categoria_modulo
 * 
 */
public class CategoriaModuloEntity {
	
	private Integer idCategoria;
	private Long idModulo;
	private Date dataUpd;
	private String attoreUpd;
	
	
	public CategoriaModuloEntity() {
		super();
	}
	
	public CategoriaModuloEntity(Integer idCategoria, Long idModulo, Date dataUpd, String attoreUpd) {
		super();
		this.idCategoria = idCategoria;
		this.idModulo = idModulo;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}

	@Override
	public String toString() {
		return "CategoriaModuloEntity [idCategoria=" + idCategoria + ", idModulo=" + idModulo + ", dataUpd=" + dataUpd
				+ ", attoreUpd=" + attoreUpd + "]";
	}

}
