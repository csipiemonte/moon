/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.exceptions.service;

import java.io.Serializable;
import java.io.StringWriter;

import it.csi.moon.moonfobl.exceptions.business.BaseException;

/**
 * @author franc
 *
 */
public class ForbiddenException extends ServiceException implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "Forbidden";

	public ForbiddenException() {
		super(MSG);
	}
	public ForbiddenException(Throwable cause) {
		super(cause);
	}
	public ForbiddenException(String msg) {
		super(msg);
	}
	public ForbiddenException(BaseException baseException) {
		super(baseException);
	}
	 
	public String getInternalErrorMessage() {
		Throwable cause = this.getCause();
		if(cause != null) {
			StringWriter sw = new StringWriter();
			return sw.toString();
		} else 
			return getMessage();
	 }

}
