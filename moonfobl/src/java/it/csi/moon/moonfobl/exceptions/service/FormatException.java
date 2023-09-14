/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.exceptions.service;

import it.csi.moon.moonfobl.exceptions.business.BaseException;

public class FormatException extends ServiceException {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Errore: Formato parametro non valido";
	
	public FormatException() {
		super(MSG);
	}
	public FormatException(String msg) {
		super(msg);
	}
	public FormatException(BaseException baseException) {
		super(baseException);
	}
}
