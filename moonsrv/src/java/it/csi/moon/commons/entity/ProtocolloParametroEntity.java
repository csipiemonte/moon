/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella degli attributi di configurazione del protocollo
 * <br>
 * <br>Tabella moon_pr_d_parametri
 * <br>PK: idParametro
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 10/06/2020 - versione iniziale
 */
public class ProtocolloParametroEntity {

	private Long idParametro;
	private Long idEnte;
	private Long idArea;
	private Long idModulo;
	private String nomeAttributo;
	private String valore;
	private Date dataUpd;
	private String attoreUpd;

	public ProtocolloParametroEntity() {
		super();
	}

	public ProtocolloParametroEntity(Long idParametro, Long idEnte, Long idArea, Long idModulo, String nomeAttributo, String valore, Date dataUpd,
			String attoreUpd) {
		super();
		this.idParametro = idParametro;
		this.idEnte = idEnte;
		this.idArea = idArea;
		this.idModulo = idModulo;
		this.nomeAttributo = nomeAttributo;
		this.valore = valore;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdParametro() {
		return idParametro;
	}
	public void setIdParametro(Long idParametro) {
		this.idParametro = idParametro;
	}
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
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
