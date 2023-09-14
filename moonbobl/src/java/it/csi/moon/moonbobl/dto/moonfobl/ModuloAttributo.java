/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Modulo attribut ( moon_io_d_moduloattributi )
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloAttributo {

	private Long idAttributo;
	private String nome;
	private String valore;
	private Date dataUpd = null;
	private String attoreUpd = null;

	public ModuloAttributo() {
		super();
	}

	public ModuloAttributo(String nome, String valore) {
		super();
		this.nome = nome;
		this.valore = valore;
	}
	
	public Long getIdAttributo() {
		return idAttributo;
	}
	public void setIdAttributo(Long idAttributo) {
		this.idAttributo = idAttributo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getValore() {
		return valore;
	}
	public void setValore(String valore) {
		this.valore = valore;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
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
		return "ModuloAttributo [idAttributo=" + idAttributo + ", nome=" + nome + ", valore=" + valore + ", dataUpd="
				+ dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}

}
