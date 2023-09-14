/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.exceptions.business;

import it.csi.moon.commons.dto.MoonError;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Errore generico ";
	private String code = "MOONSRV-00000";
	private String title = "ERRORE";

	public BaseException() {
		super(DEFAULT_MSG);
	}

	//
	public BaseException(String msg) {
		super(msg);
	}
	public BaseException(String msg, String code) {
		super(msg);
		this.code = code;
	}
	public BaseException(String msg, String code, String title) {
		super(msg);
		this.code = code;
		this.title = title;
	}
	public BaseException(MoonError error) {
		super(error.getMsg());
		this.code = error.getCode();
		this.title = error.getTitle();
	}

	//
	public BaseException(Throwable msg) {
		super(msg);
	}
	public BaseException(Throwable msg, String code) {
		super(msg);
		this.code = code;
	}
	public BaseException(Throwable msg, String code, String title) {
		super(msg);
		this.code = code;
		this.title = title;
	}
	
	//
	public String getCode() {
		return code;
	}
	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "BaseException [msg=" + getMessage() + " , code=" + code + " , title=" + title + "]";
	}
	
}
