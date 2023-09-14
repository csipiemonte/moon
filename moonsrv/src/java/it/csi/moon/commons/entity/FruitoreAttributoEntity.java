/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella degli attributi del fruitore
 * <br>
 * <br>Tabella moon_wf_t_fruitore_attributo
 * <br>PK: idAttributo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class FruitoreAttributoEntity {

	private Long idAttributo;
	private Integer idFruitore;
	private String nomeAttributo;
	private String valore;
	private Date dataUpd;
	private String attoreUpd;

	public FruitoreAttributoEntity() {
		super();
	}

	public FruitoreAttributoEntity(Long idAttributo, Integer idFruitore, String nomeAttributo, String valore, Date dataUpd,
			String attoreUpd) {
		super();
		this.idAttributo = idAttributo;
		this.idFruitore = idFruitore;
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

	public Integer getIdFruitore() {
		return idFruitore;
	}

	public void setIdFruitore(Integer idFruitore) {
		this.idFruitore = idFruitore;
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

}
