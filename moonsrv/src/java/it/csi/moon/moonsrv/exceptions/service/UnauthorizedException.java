/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.exceptions.service;

import java.io.Serializable;

public class UnauthorizedException extends ServiceException implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Access token mancante o non valido";

	public UnauthorizedException() {
		super(DEFAULT_MSG);
	}

	public UnauthorizedException(String msg) {
		super(DEFAULT_MSG + " - " + msg);
	}

	public UnauthorizedException(Throwable thr) {
		super(DEFAULT_MSG + " - " + thr.getMessage());
	}

}
