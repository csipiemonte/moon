/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Entity della tabella degli attributi del modulo
 * <br>
 * <br>Tabella moon_io_d_moduloattributi
 * <br>PK: idAttributo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloAttributoEntity {

	private Long idAttributo;
	private Long idModulo;
	private String nomeAttributo;
	private String valore;
	private Date dataUpd;
	private String attoreUpd;

	public ModuloAttributoEntity() {
		super();
	}

	public ModuloAttributoEntity(Long idAttributo, Long idModulo, String nomeAttributo, String valore, Date dataUpd,
			String attoreUpd) {
		super();
		this.idAttributo = idAttributo;
		this.idModulo = idModulo;
		this.nomeAttributo = nomeAttributo;
		this.valore = valore;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdAttributo() {
		return idAttributo;
	}

	public void setIdAttributo(Long idAttributo) {
		this.idAttributo = idAttributo;
	}

	public Long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}

	public String getNomeAttributo() {
		return nomeAttributo;
	}

	public void setNomeAttributo(String nomeAttributo) {
		this.nomeAttributo = nomeAttributo;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	} 
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone = "CET")
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
		return "ModuloAttributoEntity [idAttributo=" + idAttributo + ", idModulo=" + idModulo + ", nomeAttributo="
				+ nomeAttributo + ", valore=" + valore + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}

}
