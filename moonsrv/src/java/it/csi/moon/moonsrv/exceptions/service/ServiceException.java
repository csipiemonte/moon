/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.exceptions.service;

import java.io.Serializable;
import java.io.StringWriter;

import it.csi.moon.moonsrv.exceptions.business.BaseException;

/**
 * @author franc
 * Eccezione rilanciata dal servizio
 *
 */
public class ServiceException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;
	private String code = "ERR";
	private String title = "ERRORE";

	public ServiceException() {
		super();
	}
	
	public ServiceException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
	public ServiceException(String message, Throwable cause, String code)
	{
		super(message, cause);
		this.code=code;
	}
	
	public ServiceException(BaseException baseException) {
		super(baseException.getMessage());
		this.code = baseException.getCode();
		this.title = baseException.getTitle();
	}
	
	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	public ServiceException(String msg) {
		super(msg);
	}
	
	public ServiceException(String msg, String code) {
		super(msg);
		this.code=code;
	}
	
	
	public String getInternalErrorMessage() {
		Throwable cause = this.getCause();
		if(cause!=null) {
			StringWriter sw = new StringWriter();
			return sw.toString();
		} else {
			return getMessage();
		}
	}

	public String getCode() {
		return code;
	}
	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "BaseException [msg=" + getMessage() + ", code=" + code + ", title=" + title + "]";
	}
	
}
