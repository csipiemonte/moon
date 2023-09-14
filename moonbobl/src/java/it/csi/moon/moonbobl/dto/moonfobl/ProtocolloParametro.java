/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Date;

/**
 * Parametro per la protocollazione (moon_pr_d_parametri)
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 09/12/2021 - versione iniziale
 */
public class ProtocolloParametro {

	private Long idParametro;
	private Ente ente;
	private Area area;
	private Long idModulo;
	private String nomeAttributo;
	private String valore;
	private Date dataUpd;
	private String attoreUpd;
	private Integer order;

	public ProtocolloParametro() {
		super();
	}

	public ProtocolloParametro(Long idParametro, Ente ente, Area area, Long idModulo, String nomeAttributo, String valore, Date dataUpd,
			String attoreUpd, Integer order) {
		super();
		this.idParametro = idParametro;
		this.ente = ente;
		this.area = area;
		this.idModulo = idModulo;
		this.nomeAttributo = nomeAttributo;
		this.valore = valore;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
		this.order = order;
	}

	public Long getIdParametro() {
		return idParametro;
	}
	public void setIdParametro(Long idParametro) {
		this.idParametro = idParametro;
	}
	public Ente getEnte() {
		return ente;
	}
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
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
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}

}
