/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.exceptions.business;

public class ItemNotFoundBusinessException extends BusinessException {
	
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Elemento non trovato";
	
	public ItemNotFoundBusinessException() {
		super(DEFAULT_MSG);
	}
	
	//
	public ItemNotFoundBusinessException(String msg) {
		super(msg);
	}
	public ItemNotFoundBusinessException(String msg, String code) {
		super(msg,code);	
	}
	public ItemNotFoundBusinessException(String msg, String code, String title) {
		super(msg,code,title);	
	}

	//
	public ItemNotFoundBusinessException(Throwable thr) {
		super(thr.getMessage());
	}
	public ItemNotFoundBusinessException(Throwable thr, String code) {
		super(thr,code);	
	}
	public ItemNotFoundBusinessException(Throwable thr, String code, String title) {
		super(thr,code,title);	
	}

}
