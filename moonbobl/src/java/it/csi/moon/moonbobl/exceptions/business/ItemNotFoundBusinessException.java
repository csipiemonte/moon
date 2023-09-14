/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.exceptions.business;

public class ItemNotFoundBusinessException extends BusinessException {
	private static final long serialVersionUID = 1L;
	private final static String DEFAULT_MSG = "Elemento non trovato";
	public ItemNotFoundBusinessException() {
		super(DEFAULT_MSG);
		// TODO Auto-generated constructor stub
	}
	public ItemNotFoundBusinessException(String msg) {
		super(DEFAULT_MSG + " - "+ msg);
		// TODO Auto-generated constructor stub
	}
	public ItemNotFoundBusinessException(Throwable msg) {
		super(DEFAULT_MSG + " - "+msg.getMessage());
		// TODO Auto-generated constructor stub
	}
}
