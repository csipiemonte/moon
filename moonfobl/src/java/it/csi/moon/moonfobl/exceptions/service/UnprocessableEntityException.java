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
 * @author franc
 *
 */
public class UnprocessableEntityException extends ServiceException implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Errore: Unprocessable Entity - Invalid Input Parameter";
	
	public UnprocessableEntityException() {
		super(MSG);
	}
	public UnprocessableEntityException(String msg) {
		super(msg);
	}
	public UnprocessableEntityException(String msg, String code) {
		super(msg, code);
	}
	public UnprocessableEntityException(String msg, String code, String title) {
		super(msg, code, title);
	}
	public UnprocessableEntityException(BaseException baseException) {
		super(baseException);
	}
}
