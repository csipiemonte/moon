/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.exceptions.business;

/**
 * @author Danilo
 *
 */
public class FormatBusinessException extends BusinessException {

	private static final long serialVersionUID = 1L;
	private static final String  DEFAULT_MSG = "Errore di formato, operazione non eseguita";
	
	public FormatBusinessException() {
		super(DEFAULT_MSG);
	}
	
	//
	public FormatBusinessException(String msg) {
		super(msg);
	}
	public FormatBusinessException(String msg, String code) {
		super(msg,code);	
	}
	public FormatBusinessException(String msg, String code, String title) {
		super(msg,code,title);	
	}

	//
	public FormatBusinessException(Throwable thr) {
		super(thr.getMessage());
	}
	public FormatBusinessException(Throwable msg, String code) {
		super(msg,code);	
	}
	public FormatBusinessException(Throwable msg, String code, String title) {
		super(msg,code,title);	
	}

}
