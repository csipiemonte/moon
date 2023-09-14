/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.exceptions.business;

public class TooManyItemFoundBusinessException extends BusinessException {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Troppi elementi trovati";
	public TooManyItemFoundBusinessException() {
		super(DEFAULT_MSG);
		// TODO Auto-generated constructor stub
	}
	public TooManyItemFoundBusinessException(String msg) {
		super(DEFAULT_MSG + " - "+ msg);
		// TODO Auto-generated constructor stub
	}
	public TooManyItemFoundBusinessException(Throwable msg) {
		super(DEFAULT_MSG + " - "+msg.getMessage());
		// TODO Auto-generated constructor stub
	}
}
