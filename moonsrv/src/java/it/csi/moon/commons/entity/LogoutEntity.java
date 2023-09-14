/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella di logout da FO
 * <br>
 * <br>Tabella moon_fo_d_logout
 * <br>PK: idLogout
 * <br>AK: idPortale,livAuth (0)
 * 
 * http://kbt.csi.it/shibboleth-13/item/387-parametri-ambienti-shibfed
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 31/08/2020 - versione iniziale
 */
public class LogoutEntity {

	private Long idLogout;
	private Long idPortale;
	private Integer livAuth;
	private String url;
	private Date dataUpd;
	private String attoreUpd;
	
	public LogoutEntity() {	
	}

	public LogoutEntity(Long idLogout, Long idPortale, Integer livAuth, String url, Date dataUpd, String attoreUpd) {
		super();
		this.idLogout = idLogout;
		this.idPortale = idPortale;
		this.livAuth = livAuth;
		this.url = url;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdLogout() {
		return idLogout;
	}
	public void setIdLogout(Long idLogout) {
		this.idLogout = idLogout;
	}
	public Long getIdPortale() {
		return idPortale;
	}
	public void setIdPortale(Long idPortale) {
		this.idPortale = idPortale;
	}
	public Integer getLivAuth() {
		return livAuth;
	}
	public void setLivAuth(Integer livAuth) {
		this.livAuth = livAuth;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
		return "LogoutEntity [idLogout=" + idLogout + ", idPortale=" + idPortale + ", livAuth=" + livAuth + ", url="
				+ url + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}
	
}
