/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.exceptions.business;

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
	
	//
	public BusinessException(String msg) {
		super(msg);
	}
	public BusinessException(String msg, String code) {
		super(msg,code);
	}
	public BusinessException(String msg, String code, String title) {
		super(msg,code,title);
	}
	
	//
	public BusinessException(BaseException baseE) {
		super(baseE.getMessage(),baseE.getCode(),baseE.getTitle());
	}
	
	//
	public BusinessException(Throwable thr) {
		super(thr.getMessage());
	}
	public BusinessException(Throwable msg, String code) {
		super(msg,code);
	}
	public BusinessException(Throwable msg, String code, String title) {
		super(msg,code,title);
	}

	@Override
	public String toString() {
		return "BusinessException [msg=" + getMessage() + " , code=" + getCode() + " , title=" + getTitle() + "]";
	}
}
