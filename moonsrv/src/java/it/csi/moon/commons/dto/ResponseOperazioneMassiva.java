/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResponseOperazioneMassiva {
	
	private String operation;
	private String status;
	private Long idTag;
	private Date started;
	private Date ended;
	private Integer total;
	private Integer ok;
	private Integer ko;
	
	public String getOperation() {
		return operation;
	}
	public String getStatus() {
		return status;
	}
	public Long getIdTag() {
		return idTag;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getStarted() {
		return started;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getEnded() {
		return ended;
	}
	public Integer getTotal() {
		return total;
	}
	public Integer getOk() {
		return ok;
	}
	public Integer getKo() {
		return ko;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setIdTag(Long idTag) {
		this.idTag = idTag;
	}
	public void setStarted(Date started) {
		this.started = started;
	}
	public void setEnded(Date ended) {
		this.ended = ended;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public void setOk(Integer ok) {
		this.ok = ok;
	}
	public void setKo(Integer ko) {
		this.ko = ko;
	}

}
