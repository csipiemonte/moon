/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.exceptions.service;

import java.io.Serializable;

import it.csi.moon.moonfobl.exceptions.business.BaseException;

/**
 * @author Laurent
 *
 */
public class UnauthorizedException extends ServiceException implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Unauthorized";
	
	public UnauthorizedException() {
		super(MSG);
	}
	public UnauthorizedException(String msg) {
		super(msg);
	}
	public UnauthorizedException(String msg, String code) {
		super(msg, code);
	}
	public UnauthorizedException(BaseException baseException) {
		super(baseException);
	}
}
