/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.exceptions.business;

public class UnivocitaIstanzaBusinessException extends BusinessException {
	private static final long serialVersionUID = 1L;
//	private final static String DEFAULT_MSG = "Il modulo non è più disponibile.";
	private final static String DEFAULT_MSG = "La domanda non può essere accettata in quanto è già stata presentata.";
	
	private String errCode = "401";
	private String title = "ERROR";
	
	public String getCode() {
		return errCode;
	}
	
	public String getTitle() {
		return title;
	}

	public UnivocitaIstanzaBusinessException() {
		super(DEFAULT_MSG);
		// TODO Auto-generated constructor stub
	}
	public UnivocitaIstanzaBusinessException(String msg) {
		super(DEFAULT_MSG + " - "+ msg);
		// TODO Auto-generated constructor stub
	}
	public UnivocitaIstanzaBusinessException(Throwable msg) {
		super(DEFAULT_MSG + " - "+msg.getMessage());
		// TODO Auto-generated constructor stub
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
