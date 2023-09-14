/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.exceptions.business;

public class UnauthorizedBusinessException extends BusinessException {
	private static final long serialVersionUID = 1L;
	private final static String DEFAULT_MSG = "Access token mancante o non valido";
	public UnauthorizedBusinessException() {
		super(DEFAULT_MSG);
		// TODO Auto-generated constructor stub
	}
	public UnauthorizedBusinessException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	public UnauthorizedBusinessException(Throwable msg) {
		super(DEFAULT_MSG + " - "+msg.getMessage());
		// TODO Auto-generated constructor stub
	}
}
