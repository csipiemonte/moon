/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/*
 * Entity relativo alla tabella moon_fo_d_categoria
 * 
 */
public class CategoriaEntity {
	private Integer idCategoria;
	private String nomeCategoria;
	private Integer idCategoriaPadre;
	private Date dataUpd;
	private String attoreUpd;
	
	public Integer getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getNomeCategoria() {
		return nomeCategoria;
	}
	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
	public Integer getIdCategoriaPadre() {
		return idCategoriaPadre;
	}
	public void setIdCategoriaPadre(Integer idCategoriaPadre) {
		this.idCategoriaPadre = idCategoriaPadre;
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
		return "CategoriaEntity [idCategoria=" + idCategoria + ", nomeCategoria=" + nomeCategoria
				+ ", idCategoriaPadre=" + idCategoriaPadre + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}

}
