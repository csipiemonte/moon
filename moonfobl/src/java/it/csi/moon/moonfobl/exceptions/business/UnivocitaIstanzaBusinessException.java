/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.exceptions.business;

import org.apache.commons.lang.StringUtils;

public class UnivocitaIstanzaBusinessException extends BusinessException {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Il modulo non è più disponibile.";
	
	private String errCode = "401";
	private String title = "ERRORE";
	
	public String getCode() {
		return errCode;
	}
	
	public String getTitle() {
		return title;
	}

	public UnivocitaIstanzaBusinessException() {
		super(DEFAULT_MSG);
	}
	public UnivocitaIstanzaBusinessException(String msg) {
		super(StringUtils.isBlank(msg)?DEFAULT_MSG: msg);
	}
	public UnivocitaIstanzaBusinessException(Throwable msg) {
		super(DEFAULT_MSG + " - "+msg.getMessage());
	}
	
	public UnivocitaIstanzaBusinessException(String msg, String code) {
		super(msg);
		this.errCode = code;
	}
	
	public UnivocitaIstanzaBusinessException(String msg, String code, String title) {
		super(msg);
		this.errCode = code;
		this.title = title;
	}

	@Override
	public String toString() {
		return "UnivocitaIstanzaBusinessException [errCode=" + errCode + ", title=" + title + ", msg= " + getMessage() + "]";
	}
	
}
