/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.exceptions.service.handlers.dto;

public class ServiceResponse {
	private String code;
	private String msg;
	private String title;
	
	public ServiceResponse(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public ServiceResponse(String code, String msg, String title) {
		super();
		this.code = code;
		this.msg = msg;
		this.title = title;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
