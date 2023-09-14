/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonprint.exceptions.business;

/**
 * @author franc
 *
 */
public class BusinessException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String  DEFAULT_MSG = "Errore su layer business, operazione non eseguita";
	public BusinessException() {
		super(DEFAULT_MSG);
	}
	public BusinessException(String msg) {
		super(DEFAULT_MSG + " - "+ msg);
	}
	public BusinessException(Throwable thr) {
		super(DEFAULT_MSG + " - "+thr.getMessage());
	}

}
