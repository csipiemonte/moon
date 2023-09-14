/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.exceptions.business;

import org.apache.commons.lang.StringUtils;

public class LoginBusinessException extends BusinessException {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Errore, login non eseguito";
	
	private String errCode = "401";
	private String title = "ERRORE";
	
	public String getCode() {
		return errCode;
	}
	
	public String getTitle() {
		return title;
	}

	public LoginBusinessException() {
		super(DEFAULT_MSG);
	}
	public LoginBusinessException(String msg) {
		super(StringUtils.isBlank(msg)?DEFAULT_MSG: msg);
	}
	public LoginBusinessException(String msg, String code) {
		super(msg);
		this.errCode = code;
	}
	public LoginBusinessException(String msg, String code, String title) {
		super(msg);
		this.errCode = code;
		this.title = title;
	}
	public LoginBusinessException(ErrType errType) {
		super(errType.getMsg());
		this.errCode = errType.getCode();
	}
	public LoginBusinessException(Throwable thr) {
		super(DEFAULT_MSG + " - "+thr.getMessage());
	}
	
}
