/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.exceptions.service;

import java.io.Serializable;

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
	 public UnprocessableEntityException(String msg)
	 {
	  super(msg);
	 }
}
