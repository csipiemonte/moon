/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

public class NotifyLogEntity {

	private Long idLogNotify;
	private Date dataLogNotify;
	private Long idEnte;
	private Long idModulo;
	private Long idIstanza;
	private String uuidMessaggio;
	private String uuidPayload; // serve per conoscere lo stato del mex cn GET a notify
	private String esito;
	
	private Boolean email;
	private Boolean sms;
	private Boolean push;
	private Boolean mex;
	private Boolean io;
	
	
	public NotifyLogEntity() {
		super();
	}	
	public NotifyLogEntity(Long idEnte, Long idModulo, Long idIstanza, String uuidMessaggio,
			String uuidPayload, String esito, Boolean email, Boolean sms, Boolean push, Boolean mex, Boolean io) {
		super();
		this.idEnte = idEnte;
		this.idModulo = idModulo;
		this.idIstanza = idIstanza;
		this.uuidMessaggio = uuidMessaggio;
		this.uuidPayload = uuidPayload;
		this.esito = esito;
		this.email = email;
		this.sms = sms;
		this.push = push;
		this.mex = mex;
		this.io = io;
	}

	public Long getIdLogNotify() {
		return idLogNotify;
	}
	public void setIdLogNotify(Long idLogNotify) {
		this.idLogNotify = idLogNotify;
	}
	public Date getDataLogNotify() {
		return dataLogNotify;
	}
	public void setDataLogNotify(Date dataLogNotify) {
		this.dataLogNotify = dataLogNotify;
	}
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public String getUuidMessaggio() {
		return uuidMessaggio;
	}
	public void setUuidMessaggio(String uuidMessaggio) {
		this.uuidMessaggio = uuidMessaggio;
	}
	public String getUuidPayload() {
		return uuidPayload;
	}
	public void setUuidPayload(String uuidPayload) {
		this.uuidPayload = uuidPayload;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public Boolean getEmail() {
		return email;
	}
	public void setEmail(Boolean email) {
		this.email = email;
	}
	public Boolean getSms() {
		return sms;
	}
	public void setSms(Boolean sms) {
		this.sms = sms;
	}
	public Boolean getPush() {
		return push;
	}
	public void setPush(Boolean push) {
		this.push = push;
	}
	public Boolean getMex() {
		return mex;
	}
	public void setMex(Boolean mex) {
		this.mex = mex;
	}
	public Boolean getIo() {
		return io;
	}
	public void setIo(Boolean io) {
		this.io = io;
	}	
}
