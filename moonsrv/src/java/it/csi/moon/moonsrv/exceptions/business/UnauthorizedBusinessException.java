/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.exceptions.business;

public class UnauthorizedBusinessException extends BusinessException {
	
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Access token mancante o non valido";
	
	public UnauthorizedBusinessException() {
		super(DEFAULT_MSG);
	}
	
	//
	public UnauthorizedBusinessException(String msg) {
		super(msg);
	}
	public UnauthorizedBusinessException(String msg, String code) {
		super(msg,code);	
	}
	public UnauthorizedBusinessException(String msg, String code, String title) {
		super(msg,code,title);	
	}

	//
	public UnauthorizedBusinessException(Throwable thr) {
		super(thr.getMessage());
	}
	public UnauthorizedBusinessException(Throwable msg, String code) {
		super(msg,code);	
	}
	public UnauthorizedBusinessException(Throwable msg, String code, String title) {
		super(msg,code,title);	
	}

}
